// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.restrictions.BlockRestriction;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.amateras_smp.amatweaks.config.Configs;

public class PreventBreakingAdjacentPortal {
    public static final BlockRestriction PREVENT_BREAKING_ADJACENT_PORTAL_RESTRICTION = new BlockRestriction();

    public static void buildLists() {
        PREVENT_BREAKING_ADJACENT_PORTAL_RESTRICTION.setListType((UsageRestriction.ListType) Configs.Lists.PORTAL_BREAKING_RESTRICTION_LIST_TYPE.getOptionListValue());
        PREVENT_BREAKING_ADJACENT_PORTAL_RESTRICTION.setListContents(
                Configs.Lists.PORTAL_BREAKING_RESTRICTION_BLACKLIST.getStrings(),
                Configs.Lists.PORTAL_BREAKING_RESTRICTION_WHITELIST.getStrings());
    }

    public static boolean restriction(BlockPos pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) return false;
        if (!isThereAdjacentPortal(player.clientWorld, pos)) return false;
        if (player.clientWorld.getBlockState(pos).isOf(Blocks.NETHER_PORTAL)) return false;

        Block block = player.clientWorld.getBlockState(pos).getBlock();
        return !PREVENT_BREAKING_ADJACENT_PORTAL_RESTRICTION.isAllowed(block);
    }

    private static boolean isThereAdjacentPortal(ClientWorld world, BlockPos pos) {
        return  world.getBlockState(pos.offset(Direction.WEST)).isOf(Blocks.NETHER_PORTAL) && world.getBlockState(pos.offset(Direction.WEST)).get(NetherPortalBlock.AXIS) == Direction.Axis.X ||
                world.getBlockState(pos.offset(Direction.EAST)).isOf(Blocks.NETHER_PORTAL) && world.getBlockState(pos.offset(Direction.EAST)).get(NetherPortalBlock.AXIS) == Direction.Axis.X ||
                world.getBlockState(pos.offset(Direction.NORTH)).isOf(Blocks.NETHER_PORTAL) && world.getBlockState(pos.offset(Direction.NORTH)).get(NetherPortalBlock.AXIS) == Direction.Axis.Z ||
                world.getBlockState(pos.offset(Direction.SOUTH)).isOf(Blocks.NETHER_PORTAL) && world.getBlockState(pos.offset(Direction.SOUTH)).get(NetherPortalBlock.AXIS) == Direction.Axis.Z ||
                world.getBlockState(pos.offset(Direction.DOWN)).isOf(Blocks.NETHER_PORTAL) ||
                world.getBlockState(pos.offset(Direction.UP)).isOf(Blocks.NETHER_PORTAL);
    }
}
