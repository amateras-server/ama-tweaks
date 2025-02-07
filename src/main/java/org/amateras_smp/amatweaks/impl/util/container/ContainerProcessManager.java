// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util.container;

import com.google.common.collect.ImmutableList;
import me.fallenbreath.tweakermore.impl.features.autoContainerProcess.processors.ProcessResult;
import me.fallenbreath.tweakermore.mixins.tweaks.features.autoContainerProcess.ItemScrollerInventoryUtilsAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
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

    public static void process(ScreenHandler container) {
        if (!hasTweakEnabled()) return;
        Screen screen = MinecraftClient.getInstance().currentScreen;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && screen instanceof HandledScreen<?> containerScreen) {
            if (player.isSpectator()) return;

            if (containerScreen.getScreenHandler() != container || !((AutoProcessableScreen)screen).shouldProcess$AMT())
            {
                return;
            }

            ((AutoProcessableScreen)screen).setShouldProcess$AMT(false);
            List<Slot> allSlots = container.slots;
            List<Slot> playerInvSlots = allSlots.stream().filter(slot -> slot.inventory instanceof PlayerInventory).collect(Collectors.toList());
            if (allSlots.isEmpty() || playerInvSlots.isEmpty())
            {
                return;
            }
            List<Slot> containerInvSlots = allSlots.stream().filter(slot -> ItemScrollerInventoryUtilsAccessor.areSlotsInSameInventory(slot, allSlots.get(0))).collect(Collectors.toList());
            if (containerInvSlots.isEmpty())
            {
                return;
            }

            boolean closeGui = false;
            for (IContainerProcessor processor : CONTAINER_PROCESSORS)
            {
                if (processor.isEnabled())
                {
                    ProcessResult result = processor.process(player, containerScreen, allSlots, playerInvSlots, containerInvSlots);
                    closeGui |= result.closeGui;
                    if (result.cancelProcessing)
                    {
                        break;
                    }
                }
            }
            if (closeGui && Configs.Generic.ON_AUTO_RESTOCK_CLOSE_GUI.getBooleanValue())
            {
                player.closeHandledScreen();
            }

        }
    }
}
