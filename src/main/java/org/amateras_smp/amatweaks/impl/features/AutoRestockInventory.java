// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import com.google.common.base.Joiner;
import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.restrictions.ItemRestriction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.util.container.IContainerProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AutoRestockInventory implements IContainerProcessor {
    private static final ItemRestriction INVENTORY_RESTOCK_RESTRICTION = new ItemRestriction();

    @Override
    public FeatureToggle getConfig() {
        return FeatureToggle.TWEAK_AUTO_RESTOCK_INVENTORY;
    }

    public static void buildLists() {
        INVENTORY_RESTOCK_RESTRICTION.setListType((ItemRestriction.ListType) Configs.Lists.INVENTORY_RESTOCK_ITEMS_LIST_TYPE.getOptionListValue());
        INVENTORY_RESTOCK_RESTRICTION.setListContents(
            Configs.Lists.INVENTORY_RESTOCK_ITEMS_BLACK_LIST.getStrings(),
            Configs.Lists.INVENTORY_RESTOCK_ITEMS_WHITE_LIST.getStrings());
    }

    private void moveToPlayerInventory(AbstractContainerScreen<?> containerScreen, Slot containerSlot, Slot playerSlot, int moveAmount) {
        InventoryUtils.leftClickSlot(containerScreen, containerSlot.index);
        if (moveAmount == containerSlot.getItem().getCount()) {
            InventoryUtils.shiftClickSlot(containerScreen, containerSlot.index);
            return;
        }
        for (int i = 0; i < moveAmount; i++) {
            InventoryUtils.rightClickSlot(containerScreen, playerSlot.index);
        }
        InventoryUtils.leftClickSlot(containerScreen, containerSlot.index);
    }

    public ProcessResult process(LocalPlayer player, AbstractContainerScreen<?> containerScreen, List<Slot> allSlots, List<Slot> playerInvSlots, List<Slot> containerInvSlots) {
        // Prepare
        Minecraft mc = Minecraft.getInstance();
        HitResult hit = mc.hitResult;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) {
            return new ProcessResult(false, false);
        }
        BlockHitResult hitBlock = (BlockHitResult) hit;
        BlockPos hitBlockPos = hitBlock.getBlockPos();
        ClientLevel clientWorld = mc.level;

        if (clientWorld == null) {
            return new ProcessResult(false, false);
        }
        BlockEntity container = clientWorld.getBlockEntity(hitBlockPos);
        if (container == null
            || (container instanceof EnderChestBlockEntity && Configs.Generic.AUTO_RESTOCK_IGNORE_ENDER_CHEST.getBooleanValue()
            || (!(container instanceof ShulkerBoxBlockEntity) && Configs.Generic.AUTO_RESTOCK_SHULKER_BOX_ONLY.getBooleanValue()))) {
            return new ProcessResult(false, false);
        }

        List<Slot> shouldRestockSlots = new ArrayList<>();
        List<Slot> emptySlots = new ArrayList<>();
        for (Slot playerSlot : playerInvSlots) {
            ItemStack stack = playerSlot.getItem();
            if (!stack.isEmpty()) {
                if (INVENTORY_RESTOCK_RESTRICTION.isAllowed(stack.getItem())) {
                    if (stack.getCount() < stack.getMaxStackSize()) {
                        shouldRestockSlots.add(playerSlot);
                    }
                }
            } else {
                emptySlots.add(playerSlot);
            }
        }

        if (shouldRestockSlots.isEmpty() && emptySlots.isEmpty())
            return new ProcessResult(false, false);

        HashMap<Item, Integer> restockedMap = executeRestock(containerScreen, playerInvSlots, shouldRestockSlots, emptySlots, containerInvSlots);

        if (restockedMap.isEmpty()) return new ProcessResult(false, false);

        List<String> restockedContents = getRestockedContents(restockedMap);
        String message = FeatureToggle.TWEAK_AUTO_RESTOCK_INVENTORY.getPrettyName() + " : " + Joiner.on(", ").join(restockedContents);
        InfoUtils.printActionbarMessage(message);

        return new ProcessResult(true, true);
    }

    private static List<String> getRestockedContents(HashMap<Item, Integer> restockedMap) {
        List<String> restockedContents = new ArrayList<>();

        for (HashMap.Entry<Item, Integer> entry : restockedMap.entrySet()) {
            ItemStack stack = entry.getKey().getDefaultInstance();
            ChatFormatting formatting = stack.getRarity().
            //#if MC >= 12006
            color();
            //#else
            //$$ color;
            //#endif
            String stackName = formatting + stack.getHoverName().getString() + GuiBase.TXT_RST;
            restockedContents.add(String.format("%s +%s", stackName, GuiBase.TXT_GREEN + entry.getValue() + GuiBase.TXT_RST));
        }
        return restockedContents;
    }

    private HashMap<Item, Integer> executeRestock(AbstractContainerScreen<?> containerScreen, List<Slot> playerInvSlots, List<Slot> shouldRestockSlots, List<Slot> emptySlots, List<Slot> containerSlots) {
        HashMap<Item, Integer> restockedMap = new HashMap<>();

        int[] containerCounts = new int[containerSlots.size()];
        for (int i = 0; i < containerSlots.size(); i++) {
            containerCounts[i] = containerSlots.get(i).getItem().getCount();
        }

        for (Slot playerSlot : shouldRestockSlots) {
            ItemStack playerStack = playerSlot.getItem().copy();
            if (playerStack.isEmpty()) continue;

            int remainingRestockAmount = playerStack.getMaxStackSize() - playerStack.getCount();
            int restockedAmount = 0;

            for (Slot containerSlot : containerSlots) {
                if (remainingRestockAmount <= 0) break;

                int idx = containerSlot.index;
                int availableInSlot = containerCounts[idx];
                if (availableInSlot <= 0) continue;

                if (!InventoryUtils.areStacksEqual(containerSlot.getItem(), playerStack))
                    continue;

                int takeAmount = Math.min(remainingRestockAmount, availableInSlot);

                moveToPlayerInventory(containerScreen, containerSlot, playerSlot, takeAmount);

                containerCounts[idx] -= takeAmount;
                restockedAmount += takeAmount;
                remainingRestockAmount -= takeAmount;
            }

            if (restockedAmount > 0) {
                restockedMap.put(playerStack.getItem(), restockedMap.getOrDefault(playerStack.getItem(), 0) + restockedAmount);
            }
        }

        final int restockMinNumStacks = Configs.Generic.AUTO_RESTOCK_MIN_NUM_STACKS.getIntegerValue();

        if (Configs.Generic.AUTO_RESTOCK_ENABLE_EMPTY_SLOTS.getBooleanValue() && !emptySlots.isEmpty()) {
            int emptySlotIdx = emptySlots.size() - 1;

            List<Item> candidateItems = new ArrayList<>();
            for (Slot cs : containerSlots) {
                Item item = cs.getItem().getItem();
                if (!cs.getItem().isEmpty() && !candidateItems.contains(item) && INVENTORY_RESTOCK_RESTRICTION.isAllowed(item)) {
                    candidateItems.add(item);
                }
            }

            HashMap<Item, Integer> playerInventoryCounts = new HashMap<>();
            for (Slot slot : playerInvSlots) {
                ItemStack stack = slot.getItem();
                if (!stack.isEmpty()) {
                    playerInventoryCounts.put(stack.getItem(), playerInventoryCounts.getOrDefault(stack.getItem(), 0) + stack.getCount());
                }
            }

            for (Item item : candidateItems) {
                int maxStackSize = item.getDefaultMaxStackSize();
                int restockMinCount = maxStackSize * restockMinNumStacks;
                int currentTotal = playerInventoryCounts.getOrDefault(item, 0);

                while (currentTotal < restockMinCount && emptySlotIdx < emptySlots.size()) {
                    Slot currentTargetSlot = emptySlots.get(emptySlotIdx);

                    int currentInSlot = currentTargetSlot.getItem().getCount();
                    int spaceLeftInSlot = maxStackSize - currentInSlot;

                    if (spaceLeftInSlot <= 0) {
                        emptySlotIdx--;
                        continue;
                    }

                    int neededToGoal = restockMinCount - currentTotal;
                    int amountToMove = Math.min(spaceLeftInSlot, neededToGoal);
                    int movedInThisCycle = 0;

                    for (Slot containerSlot : containerSlots) {
                        if (amountToMove <= 0) break;

                        int idx = containerSlot.index;
                        if (containerCounts[idx] <= 0 || !containerSlot.getItem().is(item))
                            continue;

                        int takeAmount = Math.min(amountToMove, containerCounts[idx]);

                        moveToPlayerInventory(containerScreen, containerSlot, currentTargetSlot, takeAmount);

                        containerCounts[idx] -= takeAmount;
                        movedInThisCycle += takeAmount;
                        amountToMove -= takeAmount;
                    }

                    if (movedInThisCycle > 0) {
                        currentTotal += movedInThisCycle;
                        restockedMap.put(item, currentTotal);
                    } else {
                        break;
                    }
                }
            }
        }

        return restockedMap;
    }
}
