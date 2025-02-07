// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.interactionhistory;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.InteractionHistory;
import org.amateras_smp.amatweaks.impl.util.BlockTypeEquals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC < 11900
//$$ import net.minecraft.client.world.ClientWorld;
//$$ import net.minecraft.world.World;
//#endif

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "breakBlock", at = @At("HEAD"))
    private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!FeatureToggle.TWEAK_INTERACTION_HISTORY.getBooleanValue()) return;
        if (MinecraftClient.getInstance().player != null) {
            InteractionHistory.onBlockInteraction(MinecraftClient.getInstance().player.getWorld().getBlockState(pos).getBlock(), pos, "break");
        }
    }

    @Inject(method = "interactBlock", at = @At("RETURN"))
    //#if MC >= 11900
    private void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
    //#else
    //$$ private void onInteractBlock(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
    //#endif

        if (!FeatureToggle.TWEAK_INTERACTION_HISTORY.getBooleanValue()) return;
        ItemUsageContext itemUsageContext = new ItemUsageContext(player, hand, hitResult);
        ItemPlacementContext ctx = new ItemPlacementContext(itemUsageContext);
        if (cir.getReturnValue() == ActionResult.SUCCESS) {
            if (!BlockTypeEquals.isSneakingInteractionCancel(ctx.getWorld().getBlockState(hitResult.getBlockPos())) || ctx.shouldCancelInteraction()) {
                if (!ctx.getWorld().getBlockState(hitResult.getBlockPos()).isOf(Blocks.AIR)) {
                    InteractionHistory.onBlockInteraction(ctx.getWorld().getBlockState(ctx.getBlockPos()).getBlock(), ctx.getBlockPos(), "place");
                    return;
                }
            }
            InteractionHistory.onBlockInteraction(player.getWorld().getBlockState(hitResult.getBlockPos()).getBlock(), hitResult.getBlockPos(), "interact");
        }
    }

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onInteractEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (!FeatureToggle.TWEAK_INTERACTION_HISTORY.getBooleanValue()) return;
        InteractionHistory.onEntityInteraction(target);
    }
}
