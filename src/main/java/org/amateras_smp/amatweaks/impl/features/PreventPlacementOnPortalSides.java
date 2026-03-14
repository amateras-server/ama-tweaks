// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

// This file includes code from taichi-tweaks, distributed under the MIT license.

// https://github.com/TaichiServer/taichi-tweaks

// Modified by pugur on 2024/10/16

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.util.BlockTypeEquals;
import org.amateras_smp.amatweaks.mixins.features.preventbreakingportal.BellBlockIMixin;

import static net.minecraft.world.level.block.NetherPortalBlock.AXIS;

public class PreventPlacementOnPortalSides {
    public static boolean restriction(Level world, BlockPlaceContext ctx, BlockHitResult hitResult) {
        if (!FeatureToggle.TWEAK_PREVENT_PLACEMENT_ON_PORTAL_SIDES.getBooleanValue())
            return false;
        if (ctx == null) {
            return false;
        }

        ItemStack itemStack = ctx.getItemInHand();
        if (itemStack.isEmpty()) {
            return false;
        }

        BlockState blockState = world.getBlockState(hitResult.getBlockPos());
        if (blockState.is(Blocks.SCAFFOLDING) && itemStack.getItem() instanceof ScaffoldingBlockItem scaffolding) {
            ctx = scaffolding.updatePlacementContext(ctx);
            if (ctx == null) {
                return false;
            }
        }
        BlockPos blockPos = ctx.getClickedPos();
        boolean firstTime = true;

        while (true) {
            if (checkNeighbors(world, blockPos.north(), Direction.Axis.Z, ctx, hitResult, hitResult.getBlockPos()) ||
                checkNeighbors(world, blockPos.south(), Direction.Axis.Z, ctx, hitResult, hitResult.getBlockPos()) ||
                checkNeighbors(world, blockPos.east(), Direction.Axis.X, ctx, hitResult, hitResult.getBlockPos()) ||
                checkNeighbors(world, blockPos.west(), Direction.Axis.X, ctx, hitResult, hitResult.getBlockPos()) ||
                checkNeighbors(world, blockPos.above(), Direction.Axis.Y, ctx, hitResult, hitResult.getBlockPos()) ||
                checkNeighbors(world, blockPos.below(), Direction.Axis.Y, ctx, hitResult, hitResult.getBlockPos())
            ) {
                String preRed = GuiBase.TXT_RED;
                String rst = GuiBase.TXT_RST;
                String message = preRed + "placement restricted by tweakPreventPlacementOnPortalSides" + rst;
                InfoUtils.printActionbarMessage(message);
                return true;
            }


            //#if MC >= 12000
            if (itemStack.getItem() instanceof DoubleHighBlockItem || itemStack.is(Items.PITCHER_PLANT)) {
                //#else
                //$$ if (itemStack.getItem() instanceof DoubleHighBlockItem) {
                //#endif
                if (blockPos.equals(hitResult.getBlockPos().relative(hitResult.getDirection()))) {
                    blockPos = blockPos.above();
                } else if (firstTime) {
                    blockPos = blockPos.above();
                } else {
                    break;
                }
                firstTime = false;
            } else if (itemStack.getItem() instanceof BedItem) {
                if (blockPos.equals(hitResult.getBlockPos().relative(hitResult.getDirection()))) {
                    Direction direction = ctx.getHorizontalDirection();
                    blockPos = blockPos.relative(direction);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return false;
    }

    public static boolean checkNeighbors(Level world, BlockPos blockPos, Direction.Axis axis, BlockPlaceContext ctx, BlockHitResult hitResult, BlockPos origin) {
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.is(Blocks.NETHER_PORTAL)) {
            if (Direction.Axis.Y == axis || blockState.getValue(AXIS) == axis) {
                if (!ctx.canPlace()) {
                    return false;
                }
                return canUse(ctx, origin, hitResult);
            }
        }
        return false;
    }

    public static boolean canUse(BlockPlaceContext context, BlockPos origin, BlockHitResult hitResult) {
        Level world = context.getLevel();
        BlockState blockState = world.getBlockState(origin);
        Block block = blockState.getBlock();

        if (BlockTypeEquals.isSneakingInteractionCancel(blockState)) {
            return context.isSecondaryUseActive();
        } else if (blockState.is(Blocks.BELL)) {
            if ((!canRing((BellBlock) block, blockState, hitResult, origin))) {
                return true;
            } else {
                return context.isSecondaryUseActive();
            }
        }

        return true;
    }

    public static boolean canRing(BellBlock bell, BlockState blockState, BlockHitResult hitResult, BlockPos blockPos) {
        return ((BellBlockIMixin) bell).ModIsPointOnBell(blockState, hitResult.getDirection(), hitResult.getLocation().y - (double) blockPos.getY());
    }
}
