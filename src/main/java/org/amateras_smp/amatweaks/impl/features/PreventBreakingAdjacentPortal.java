// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.restrictions.BlockRestriction;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return false;
        Level level = player.level();
        if (!isThereAdjacentPortal(level, pos)) return false;
        if (level.getBlockState(pos).is(Blocks.NETHER_PORTAL)) return false;

        Block block = level.getBlockState(pos).getBlock();
        return !PREVENT_BREAKING_ADJACENT_PORTAL_RESTRICTION.isAllowed(block);
    }

    private static boolean isThereAdjacentPortal(Level world, BlockPos pos) {
        return world.getBlockState(pos.relative(Direction.WEST)).is(Blocks.NETHER_PORTAL) && world.getBlockState(pos.relative(Direction.WEST)).getValue(NetherPortalBlock.AXIS) == Direction.Axis.X ||
            world.getBlockState(pos.relative(Direction.EAST)).is(Blocks.NETHER_PORTAL) && world.getBlockState(pos.relative(Direction.EAST)).getValue(NetherPortalBlock.AXIS) == Direction.Axis.X ||
            world.getBlockState(pos.relative(Direction.NORTH)).is(Blocks.NETHER_PORTAL) && world.getBlockState(pos.relative(Direction.NORTH)).getValue(NetherPortalBlock.AXIS) == Direction.Axis.Z ||
            world.getBlockState(pos.relative(Direction.SOUTH)).is(Blocks.NETHER_PORTAL) && world.getBlockState(pos.relative(Direction.SOUTH)).getValue(NetherPortalBlock.AXIS) == Direction.Axis.Z ||
            world.getBlockState(pos.relative(Direction.DOWN)).is(Blocks.NETHER_PORTAL) ||
            world.getBlockState(pos.relative(Direction.UP)).is(Blocks.NETHER_PORTAL);
    }
}
