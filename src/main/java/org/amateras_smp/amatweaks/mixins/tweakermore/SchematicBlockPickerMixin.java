// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.tweakermore;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.fallenbreath.tweakermore.impl.features.schematicProPlace.SchematicBlockPicker;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.addon.tweakermore.SelectiveAutoPick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(Reference.ModIds.tweakermore))
@Mixin(SchematicBlockPicker.class)
public class SchematicBlockPickerMixin {
    @Inject(method = "doSchematicWorldPickBlock", at = @At("HEAD"), cancellable = true)
    private static void onPickBlock(Minecraft mc, BlockPos pos, InteractionHand hand, CallbackInfo ci) {
        if (!FeatureToggle.TWEAK_SELECTIVE_AUTO_PICK.getBooleanValue() || mc.player == null)
            return;
        if (SelectiveAutoPick.restrict(mc.player, hand)) ci.cancel();
    }
}
