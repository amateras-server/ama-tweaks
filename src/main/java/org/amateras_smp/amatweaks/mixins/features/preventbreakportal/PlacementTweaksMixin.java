// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.preventbreakportal;

import fi.dy.masa.tweakeroo.tweaks.PlacementTweaks;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.impl.features.PreventPlacementOnPortalSides;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = {@Condition(Reference.ModIds.tweakeroo), @Condition(Reference.ModIds.tweakermore)})
@Mixin(PlacementTweaks.class)
public class PlacementTweaksMixin {
    @Shadow(
            remap = false
    )
    private static boolean firstWasRotation;
    @Shadow
    private static ItemStack[] stackBeforeUse;

    @Unique
    private static boolean isReplaceable(BlockState state) {
        //#if MC >= 12000
        return state.canBeReplaced();
        //#else
        //$$ return state.getMaterial().isReplaceable();
        //#endif
    }

    @Unique
    private static boolean isFacingValidForDirection(ItemStack stack, Direction facing) {
        Item item = stack.getItem();
        if (!stack.isEmpty() && item instanceof BlockItem) {
            Block block = ((BlockItem)item).getBlock();
            BlockState state = block.defaultBlockState();
            if (state.hasProperty(BlockStateProperties.FACING)) {
                return true;
            }

            if (state.hasProperty(BlockStateProperties.FACING_HOPPER) && !facing.equals(Direction.UP)) {
                return true;
            }

            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && !facing.equals(Direction.UP) && !facing.equals(Direction.DOWN)) {
                return true;
            }
        }

        return false;
    }

    @Unique
    private static BlockHitResult getFinalHitResult(LocalPlayer player, ClientLevel world, BlockPos posIn, Direction sideIn, Vec3 hitVecIn, InteractionHand hand) {
        BlockHitResult hitResult = new BlockHitResult(hitVecIn, sideIn, posIn, false);
        BlockPlaceContext ctx = new BlockPlaceContext(new UseOnContext(player, hand, hitResult));
        BlockState state = world.getBlockState(posIn);
        ItemStack stackOriginal;
        if (!stackBeforeUse[hand.ordinal()].isEmpty() && !fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_HOTBAR_SLOT_CYCLE.getBooleanValue() && !fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_HOTBAR_SLOT_RANDOMIZER.getBooleanValue()) {
            stackOriginal = stackBeforeUse[hand.ordinal()];
        } else {
            stackOriginal = player.getItemInHand(hand).copy();
        }

        if (fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_PLACEMENT_RESTRICTION.getBooleanValue() && !state.canBeReplaced(ctx) && isReplaceable(state)) {
            posIn = posIn.relative(sideIn.getOpposite());
        }

        int afterClickerClickCount = Mth.clamp(fi.dy.masa.tweakeroo.config.Configs.Generic.AFTER_CLICKER_CLICK_COUNT.getIntegerValue(), 0, 32);
        boolean flexible = fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_FLEXIBLE_BLOCK_PLACEMENT.getBooleanValue();
        boolean rotationHeld = fi.dy.masa.tweakeroo.config.Hotkeys.FLEXIBLE_BLOCK_PLACEMENT_ROTATION.getKeybind().isKeybindHeld();
        boolean rememberFlexible = fi.dy.masa.tweakeroo.config.Configs.Generic.REMEMBER_FLEXIBLE.getBooleanValue();
        boolean rotation = rotationHeld || rememberFlexible && firstWasRotation;
        boolean accurate = fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_ACCURATE_BLOCK_PLACEMENT.getBooleanValue();
        boolean keys = fi.dy.masa.tweakeroo.config.Hotkeys.ACCURATE_BLOCK_PLACEMENT_IN.getKeybind().isKeybindHeld() || fi.dy.masa.tweakeroo.config.Hotkeys.ACCURATE_BLOCK_PLACEMENT_REVERSE.getKeybind().isKeybindHeld();
        accurate = accurate && keys;

        double x;
        //#if MC >= 12100
        if (flexible && rotation && !accurate && fi.dy.masa.tweakeroo.config.Configs.Generic.ACCURATE_PLACEMENT_PROTOCOL.getBooleanValue() && isFacingValidForDirection(stackOriginal, sideIn)) {
        //#else
        //$$ if (flexible && rotation && !accurate && fi.dy.masa.tweakeroo.config.Configs.Generic.CARPET_ACCURATE_PLACEMENT_PROTOCOL.getBooleanValue() && isFacingValidForDirection(stackOriginal, sideIn)) {
        //#endif
            Direction facing = sideIn.getOpposite();
            x = posIn.getX() + 2 + facing.get3DDataValue() * 2;
            if (fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_AFTER_CLICKER.getBooleanValue()) {
                x += afterClickerClickCount * 16;
            }

            hitVecIn = new Vec3(x, hitVecIn.y, hitVecIn.z);
        }

        double y;
        if (fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_Y_MIRROR.getBooleanValue() && fi.dy.masa.tweakeroo.config.Hotkeys.PLACEMENT_Y_MIRROR.getKeybind().isKeybindHeld()) {
            y = 1.0 - hitVecIn.y + (double)(2 * posIn.getY());
            hitVecIn = new Vec3(hitVecIn.x, y, hitVecIn.z);
            if (sideIn.getAxis() == Direction.Axis.Y) {
                posIn = posIn.relative(sideIn);
                sideIn = sideIn.getOpposite();
            }
        }

        return new BlockHitResult(hitVecIn, sideIn, posIn, false);
    }

    @Inject(
            method = "processRightClickBlockWrapper",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onProcessRightClickBlockWrapper(MultiPlayerGameMode controller, LocalPlayer player, ClientLevel world, BlockPos posIn, Direction sideIn, Vec3 hitVecIn, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        BlockHitResult hitResult = getFinalHitResult(player, world, posIn, sideIn, hitVecIn, hand);
        BlockPlaceContext ctx = new BlockPlaceContext(new UseOnContext(player, hand, hitResult));

        if (PreventPlacementOnPortalSides.restriction(world, ctx, hitResult)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
