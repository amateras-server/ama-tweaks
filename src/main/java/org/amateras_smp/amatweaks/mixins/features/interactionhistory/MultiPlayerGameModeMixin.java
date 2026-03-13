// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.interactionhistory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.InteractionHistory;
import org.amateras_smp.amatweaks.impl.util.BlockTypeEquals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!FeatureToggle.TWEAK_INTERACTION_HISTORY.getBooleanValue()) return;
        if (Minecraft.getInstance().player != null) {
            InteractionHistory.onBlockInteraction(Minecraft.getInstance().player.level().getBlockState(pos).getBlock(), pos, "break");
        }
    }

    @Inject(method = "useItemOn", at = @At("RETURN"))
    private void onInteractBlock(LocalPlayer player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!FeatureToggle.TWEAK_INTERACTION_HISTORY.getBooleanValue()) return;
        UseOnContext itemUsageContext = new UseOnContext(player, hand, hitResult);
        BlockPlaceContext ctx = new BlockPlaceContext(itemUsageContext);
        if (cir.getReturnValue() == InteractionResult.SUCCESS) {
            if (!BlockTypeEquals.isSneakingInteractionCancel(ctx.getLevel().getBlockState(hitResult.getBlockPos())) || ctx.isSecondaryUseActive()) {
                if (!ctx.getLevel().getBlockState(hitResult.getBlockPos()).is(Blocks.AIR)) {
                    InteractionHistory.onBlockInteraction(ctx.getLevel().getBlockState(ctx.getClickedPos()).getBlock(), ctx.getClickedPos(), "place");
                    return;
                }
            }
            InteractionHistory.onBlockInteraction(player.level().getBlockState(hitResult.getBlockPos()).getBlock(), hitResult.getBlockPos(), "interact");
        }
    }

    @Inject(method = "attack", at = @At("HEAD"))
    private void onInteractEntity(Player player, Entity target, CallbackInfo ci) {
        if (!FeatureToggle.TWEAK_INTERACTION_HISTORY.getBooleanValue()) return;
        InteractionHistory.onEntityInteraction(target);
    }
}
