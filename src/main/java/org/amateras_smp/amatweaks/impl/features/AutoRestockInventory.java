// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import com.google.common.base.Joiner;
import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.InfoUtils;
import me.fallenbreath.tweakermore.impl.features.autoContainerProcess.processors.ProcessResult;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.util.container.IContainerProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AutoRestockInventory implements IContainerProcessor {
    @Override
    public FeatureToggle getConfig() {
        return FeatureToggle.TWEAK_AUTO_RESTOCK_INVENTORY;
    }

    public ProcessResult process(ClientPlayerEntity player, HandledScreen<?> containerScreen, List<Slot> allSlots, List<Slot> playerInvSlots, List<Slot> containerInvSlots) {
        MinecraftClient mc = MinecraftClient.getInstance();
        HitResult hit = mc.crosshairTarget;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) return new ProcessResult(false, false);
        BlockHitResult hitBlock = (BlockHitResult) hit;
        BlockPos hitBlockPos = hitBlock.getBlockPos();
        World clientWorld = mc.world;

        if(mc.player == null || clientWorld == null) return new ProcessResult(false, false);
        BlockEntity container = clientWorld.getBlockEntity(hitBlockPos);
        if (container == null) return new ProcessResult(false, false);
        if (container instanceof EnderChestBlockEntity) {
            return new ProcessResult(false, false);
        }

        HashMap<Item, Integer> restockableMap = new HashMap<>();
        List<Slot> shouldRestockSlots = new ArrayList<>();

        for (Slot containerSlot : containerInvSlots) {
            String itemId = Registries.ITEM.getId(containerSlot.getStack().getItem()).toString();
            if (isInventoryRestockListContains(itemId)) {
                restockableMap.put(containerSlot.getStack().getItem(), restockableMap.getOrDefault(containerSlot.getStack().getItem(), 0) + containerSlot.getStack().getCount());
            }
        }
        if (restockableMap.isEmpty()) return new ProcessResult(false, false);

        for (Slot playerSlot : playerInvSlots) {
            if (restockableMap.getOrDefault(playerSlot.getStack().getItem(), 0) > 0) {
                if (playerSlot.getStack().getCount() < playerSlot.getStack().getMaxCount()) {
                    shouldRestockSlots.add(playerSlot);
                }
            }
        }

        if (shouldRestockSlots.isEmpty()) return new ProcessResult(false, false);

        HashMap<String, Integer> restockedMap = executeRestock(containerScreen, shouldRestockSlots, containerInvSlots);
        List<String> restockedContents = new ArrayList<>();

        for (HashMap.Entry<String, Integer> entry : restockedMap.entrySet()) {
            restockedContents.add(String.format("%s +%s", entry.getKey(), GuiBase.TXT_GREEN + entry.getValue() + GuiBase.TXT_RST));
        }
        String message = FeatureToggle.TWEAK_AUTO_RESTOCK_INVENTORY.getPrettyName() + " : " + Joiner.on(", ").join(restockedContents);
        InfoUtils.printActionbarMessage(message);

        // first restock
        // HashMap<Item, Integer> remainingRestockableMap = executeRestock2(containerScreen, shouldRestockSlots, containerInvSlots, restockableMap);

        //  if (remainingRestockableMap.isEmpty()) {
        //      return new ProcessResult(true, true);
        //  }
        //
        //  shouldRestockSlots.clear();
        //  for (Slot slot : playerInvSlots) {
        //      if (!slot.hasStack()) shouldRestockSlots.add(slot);
        //  }
        //
        //  // second restock : restocks items which not in player inventory
        //  executeRestock(containerScreen, shouldRestockSlots, containerInvSlots, remainingRestockableMap);

        return new ProcessResult(true, true);
    }

    private boolean isInventoryRestockListContains(String itemId) {
        return Configs.Lists.INVENTORY_RESTOCK_LIST.getStrings().stream().anyMatch(target -> target.equals(itemId));
    }
    private HashMap<String, Integer> executeRestock(HandledScreen<?> containerScreen, List<Slot> shouldRestockSlots, List<Slot> containerSlots) {
        HashMap<String, Integer> restockedMap = new HashMap<>();
        for (Slot playerSlot : shouldRestockSlots) {
            ItemStack playerStack = playerSlot.getStack().copy();
            int remainingRestockAmount = playerStack.getMaxCount() - playerStack.getCount();
            int restockedAmount = 0;
            for (int idx = containerSlots.size() - 1; idx >= 0; idx--) {
                Slot containerSlot = containerSlots.get(idx);
                // for (Slot containerSlot : containerSlots) {
                ItemStack restockStack = containerSlot.getStack().copy();
                if (restockStack.isEmpty()) continue;
                if (InventoryUtils.areStacksEqual(restockStack, playerStack)) {
                    int restockAmount = Math.min(remainingRestockAmount, restockStack.getCount());
                    moveToPlayerInventory(containerScreen, containerSlot, playerSlot, restockAmount);
                    restockedAmount += restockAmount;
                    remainingRestockAmount -= restockAmount;
                }
                if (remainingRestockAmount <= 0) {
                    break;
                }
            }
            Formatting formatting = playerStack.getRarity().
                    //#if MC < 12006
                    formatting;
                    //#else
                    //$$ getFormatting();
                    //#endif
            String stackName = formatting + playerStack.getName().getString() + GuiBase.TXT_RST;
            restockedMap.put(stackName, restockedMap.getOrDefault(stackName, 0) + restockedAmount);
        }
        return restockedMap;
    }

    private HashMap<Item, Integer> executeRestock2(HandledScreen<?> containerScreen, List<Slot> shouldRestockSlots, List<Slot> containerSlots, HashMap<Item, Integer> restockableMap) {
        for (Slot playerSlot : shouldRestockSlots) {
            ItemStack playerStack = playerSlot.getStack().copy();
            int requireRestockAmount = playerStack.getMaxCount() - playerStack.getCount();
            int remainingRestockAmount = requireRestockAmount;
            for (int idx = containerSlots.size() - 1; idx >= 0; idx--) {
                Slot containerSlot = containerSlots.get(idx);
            // for (Slot containerSlot : containerSlots) {
                ItemStack restockStack = containerSlot.getStack().copy();
                if (restockStack.isEmpty()) continue;
                if (InventoryUtils.areStacksEqual(restockStack, playerStack)) {
                    int restockAmount = Math.min(remainingRestockAmount, restockStack.getCount());
                    moveToPlayerInventory(containerScreen, containerSlot, playerSlot, restockAmount);
                    remainingRestockAmount -= restockAmount;
                }
                if (remainingRestockAmount <= 0) {
                    break;
                }
            }
            // remove as restocked item
            restockableMap.remove(playerStack.getItem());
        }
        return restockableMap;
    }

    private void moveToPlayerInventory(HandledScreen<?> containerScreen, Slot containerSlot, Slot playerSlot, int moveAmount) {
        if (moveAmount == containerSlot.getStack().getCount())
        {
            InventoryUtils.shiftClickSlot(containerScreen, containerSlot.id);
            return;
        }
        InventoryUtils.leftClickSlot(containerScreen, containerSlot.id);
        for (int i = 0; i < moveAmount; i++) {
            InventoryUtils.rightClickSlot(containerScreen, playerSlot.id);
        }
        InventoryUtils.leftClickSlot(containerScreen, containerSlot.id);
    }
}
