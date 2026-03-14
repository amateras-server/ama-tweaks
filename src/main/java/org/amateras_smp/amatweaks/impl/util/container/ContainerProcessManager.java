// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util.container;

import com.google.common.collect.ImmutableList;
import me.fallenbreath.tweakermore.impl.features.autoContainerProcess.processors.ProcessResult;
import me.fallenbreath.tweakermore.mixins.tweaks.features.autoContainerProcess.ItemScrollerInventoryUtilsAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.features.AutoRestockInventory;

import java.util.List;
import java.util.stream.Collectors;

public class ContainerProcessManager {
    private static final List<IContainerProcessor> CONTAINER_PROCESSORS = ImmutableList.of(
        new AutoRestockInventory()
    );

    private static boolean hasTweakEnabled() {
        return CONTAINER_PROCESSORS.stream().anyMatch(IContainerProcessor::isEnabled);
    }

    public static List<IContainerProcessor> getProcessors() {
        return CONTAINER_PROCESSORS;
    }

    public static void process(AbstractContainerMenu container) {
        if (!hasTweakEnabled()) return;
        Minecraft mc = Minecraft.getInstance();
        Screen screen = mc.screen;
        LocalPlayer player = mc.player;
        if (player != null && screen instanceof AbstractContainerScreen<?> containerScreen) {
            if (player.isSpectator()) return;
            if (containerScreen.getMenu() != container || !((AutoProcessableScreen) screen).shouldProcess$AMT())
                return;
            if (isInBlackList(containerScreen)) return;

            ((AutoProcessableScreen) screen).setShouldProcess$AMT(false);
            List<Slot> allSlots = container.slots;
            List<Slot> playerInvSlots = allSlots.stream().filter(slot -> slot.container instanceof Inventory).collect(Collectors.toList());
            if (allSlots.isEmpty() || playerInvSlots.isEmpty()) return;
            List<Slot> containerInvSlots = allSlots.stream().filter(slot -> ItemScrollerInventoryUtilsAccessor.areSlotsInSameInventory(slot, allSlots.get(0))).collect(Collectors.toList());
            if (containerInvSlots.isEmpty()) return;

            boolean closeGui = false;
            for (IContainerProcessor processor : CONTAINER_PROCESSORS) {
                if (processor.isEnabled()) {
                    ProcessResult result = processor.process(player, containerScreen, allSlots, playerInvSlots, containerInvSlots);
                    closeGui |= result.closeGui;
                    if (result.cancelProcessing) {
                        break;
                    }
                }
            }
            if (closeGui && Configs.Generic.AUTO_RESTOCK_CLOSE_GUI.getBooleanValue()) {
                player.closeContainer();
            }

        }
    }

    private static <T extends AbstractContainerMenu> boolean isInBlackList(AbstractContainerScreen<T> containerScreen) {
        return
            containerScreen instanceof InventoryScreen || // not screen with inventory only (1)
                containerScreen instanceof CreativeModeInventoryScreen ||  // not screen with inventory only (2)
                containerScreen instanceof CraftingScreen ||   // not crafting table
                containerScreen instanceof MerchantScreen;  // not villager trading screen
    }
}
