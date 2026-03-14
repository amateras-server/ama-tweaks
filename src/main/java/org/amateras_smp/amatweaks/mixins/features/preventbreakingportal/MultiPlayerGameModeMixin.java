// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.preventbreakingportal;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.PreventBreakingAdjacentPortal;
import org.amateras_smp.amatweaks.impl.features.PreventPlacementOnPortalSides;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC <= 11802
//$$ import net.minecraft.client.multiplayer.ClientLevel;
//#endif

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void onAttackBlock(BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (!FeatureToggle.TWEAK_PREVENT_BREAKING_ADJACENT_PORTAL.getBooleanValue()) return;
        if (!PreventBreakingAdjacentPortal.restriction(pos)) return;
        String preRed = GuiBase.TXT_RED;
        String rst = GuiBase.TXT_RST;
        String message = preRed + "breaking restricted by tweakPreventBreakingAdjacentToPortal" + rst;
        InfoUtils.printActionbarMessage(message);

        cir.setReturnValue(false);
    }

    @Inject(
            method = "useItemOn",
            at = @At("HEAD"),
            cancellable = true
    )
    //#if MC >= 11900
    private void onInteractBlock(LocalPlayer player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        //#else
        //$$ private void onInteractBlock(LocalPlayer player, ClientLevel world, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        //#endif
        UseOnContext itemUsageContext = new UseOnContext(player, hand, hitResult);
        BlockPlaceContext ctx = new BlockPlaceContext(itemUsageContext);

        if (PreventPlacementOnPortalSides.restriction(ctx.getLevel(), ctx, hitResult)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
