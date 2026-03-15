// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import com.google.common.base.Joiner;
import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.ChatFormatting;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.util.container.IContainerProcessor;
import org.amateras_smp.amatweaks.impl.util.BuiltInRegistriesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AutoRestockInventory implements IContainerProcessor {
    @Override
    public FeatureToggle getConfig() {
        return FeatureToggle.TWEAK_AUTO_RESTOCK_INVENTORY;
    }

    public ProcessResult process(LocalPlayer player, AbstractContainerScreen<?> containerScreen, List<Slot> allSlots, List<Slot> playerInvSlots, List<Slot> containerInvSlots) {
        Minecraft mc = Minecraft.getInstance();
        HitResult hit = mc.hitResult;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK)
            return new ProcessResult(false, false);
        BlockHitResult hitBlock = (BlockHitResult) hit;
        BlockPos hitBlockPos = hitBlock.getBlockPos();
        ClientLevel clientWorld = mc.level;

        if (mc.player == null || clientWorld == null)
            return new ProcessResult(false, false);
        BlockEntity container = clientWorld.getBlockEntity(hitBlockPos);
        if (container == null) return new ProcessResult(false, false);
        if (container instanceof EnderChestBlockEntity)
            return new ProcessResult(false, false);

        List<Slot> shouldRestockSlots = new ArrayList<>();
        for (Slot playerSlot : playerInvSlots) {
            ItemStack stack = playerSlot.getItem();
            if (stack.isEmpty()) continue;
            String itemId = BuiltInRegistriesUtil.ITEM.getKey(stack.getItem()).toString();
            if (isInventoryRestockListContains(itemId)) {
                if (stack.getCount() < stack.getMaxStackSize()) {
                    shouldRestockSlots.add(playerSlot);
                }
            }
        }

        if (shouldRestockSlots.isEmpty())
            return new ProcessResult(false, false);

        boolean anyRestockable = false;
        outer:
        for (Slot playerSlot : shouldRestockSlots) {
            for (Slot containerSlot : containerInvSlots) {
                if (!containerSlot.getItem().isEmpty() &&
                    InventoryUtils.areStacksEqual(containerSlot.getItem(), playerSlot.getItem())) {
                    anyRestockable = true;
                    break outer;
                }
            }
        }
        if (!anyRestockable) return new ProcessResult(false, false);

        HashMap<String, Integer> restockedMap = executeRestock(containerScreen, shouldRestockSlots, containerInvSlots);

        if (restockedMap.isEmpty()) return new ProcessResult(false, false);

        List<String> restockedContents = new ArrayList<>();

        for (HashMap.Entry<String, Integer> entry : restockedMap.entrySet()) {
            restockedContents.add(String.format("%s +%s", entry.getKey(), GuiBase.TXT_GREEN + entry.getValue() + GuiBase.TXT_RST));
        }
        String message = FeatureToggle.TWEAK_AUTO_RESTOCK_INVENTORY.getPrettyName() + " : " + Joiner.on(", ").join(restockedContents);
        InfoUtils.printActionbarMessage(message);

        return new ProcessResult(true, true);
    }

    private boolean isInventoryRestockListContains(String itemId) {
        return Configs.Lists.INVENTORY_RESTOCK_LIST.getStrings().stream().anyMatch(target -> target.equals(itemId));
    }

    private HashMap<String, Integer> executeRestock(AbstractContainerScreen<?> containerScreen, List<Slot> shouldRestockSlots, List<Slot> containerSlots) {
        HashMap<String, Integer> restockedMap = new HashMap<>();

        int[] containerCounts = new int[containerSlots.size()];
        for (int i = 0; i < containerSlots.size(); i++) {
            containerCounts[i] = containerSlots.get(i).getItem().isEmpty() ? 0 : containerSlots.get(i).getItem().getCount();
        }

        for (Slot playerSlot : shouldRestockSlots) {
            ItemStack playerStack = playerSlot.getItem().copy();
            if (playerStack.isEmpty()) continue;

            int remainingRestockAmount = playerStack.getMaxStackSize() - playerStack.getCount();
            int restockedAmount = 0;

            for (int idx = 0; idx < containerSlots.size(); idx++) {
                if (remainingRestockAmount <= 0) break;

                Slot containerSlot = containerSlots.get(idx);
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
                ChatFormatting formatting = playerStack.getRarity().
                    //#if MC >= 12006
                        color();
                //#else
                //$$ color;
                //#endif
                String stackName = formatting + playerStack.getHoverName().getString() + GuiBase.TXT_RST;
                restockedMap.put(stackName, restockedMap.getOrDefault(stackName, 0) + restockedAmount);
            }
        }
        return restockedMap;
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
}
