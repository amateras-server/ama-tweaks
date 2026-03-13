// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.litematica;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.materials.MaterialCache;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.addon.litematica.PickRedirect;
import org.amateras_smp.amatweaks.Reference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(Reference.ModIds.litematica))
@Mixin(MaterialCache.class)
public class MaterialCacheMixin {
    @Inject(method = "getRequiredBuildItemForState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void onGetBuildItem(BlockState state, Level world, BlockPos pos, CallbackInfoReturnable<ItemStack> cir, @Local ItemStack stack) {
        if (!FeatureToggle.TWEAK_PICK_BLOCK_REDIRECT.getBooleanValue()) return;
        cir.setReturnValue(PickRedirect.getShouldPickItem(state, stack));
    }
}
