// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.tweakermore;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.materials.MaterialListBase;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.fallenbreath.tweakermore.impl.features.autoContainerProcess.processors.ContainerMaterialListItemCollector;
import me.fallenbreath.tweakermore.impl.features.autoContainerProcess.processors.ProcessResult;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.slot.Slot;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.config.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Restriction(require = @Condition(Reference.ModIds.tweakermore))
@Mixin(ContainerMaterialListItemCollector.class)
public class ContainerMaterialListItemCollectorMixin {
    @Inject(method = "process", at = @At("RETURN"))
    private void onPostProcess(ClientPlayerEntity player, HandledScreen<?> containerScreen, List<Slot> allSlots, List<Slot> playerInvSlots, List<Slot> containerInvSlots, CallbackInfoReturnable<ProcessResult> cir, @Local MaterialListBase materialList) {
        if (materialList != null && Configs.Generic.REFRESH_PREFILTERED_POST_AUTO_COLLECT_MATERIAL.getBooleanValue()) {
            materialList.refreshPreFilteredList();
        }
    }
}
