// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;

public class BlockTypeEquals {
    public static boolean isSneakingInteractionCancel(BlockState blockState) {
        return blockState.is(Blocks.CRAFTING_TABLE) ||
                blockState.is(Blocks.STONECUTTER) ||
                blockState.is(Blocks.CARTOGRAPHY_TABLE) ||
                blockState.is(Blocks.SMITHING_TABLE) ||
                blockState.is(Blocks.GRINDSTONE) ||
                blockState.is(Blocks.LOOM) ||
                blockState.is(Blocks.FURNACE) ||
                blockState.is(Blocks.SMOKER) ||
                blockState.is(Blocks.BLAST_FURNACE) ||
                blockState.is(Blocks.ANVIL) ||
                blockState.is(Blocks.CHIPPED_ANVIL) ||
                blockState.is(Blocks.DAMAGED_ANVIL) ||
                blockState.is(Blocks.ENCHANTING_TABLE) ||
                blockState.is(Blocks.BREWING_STAND) ||
                blockState.is(Blocks.BEACON) ||
                blockState.is(Blocks.CHEST) ||
                blockState.is(Blocks.BARREL) ||
                blockState.is(Blocks.ENDER_CHEST) ||
                blockState.is(Blocks.DISPENSER) ||
                blockState.is(Blocks.DROPPER) ||
                blockState.is(Blocks.HOPPER) ||
                blockState.is(Blocks.TRAPPED_CHEST) ||
                blockState.is(Blocks.SHULKER_BOX) ||
                blockState.is(Blocks.BLACK_SHULKER_BOX) ||
                blockState.is(Blocks.BLUE_SHULKER_BOX) ||
                blockState.is(Blocks.BROWN_SHULKER_BOX) ||
                blockState.is(Blocks.CYAN_SHULKER_BOX) ||
                blockState.is(Blocks.GRAY_SHULKER_BOX) ||
                blockState.is(Blocks.GREEN_SHULKER_BOX) ||
                blockState.is(Blocks.LIGHT_BLUE_SHULKER_BOX) ||
                blockState.is(Blocks.LIGHT_GRAY_SHULKER_BOX) ||
                blockState.is(Blocks.LIME_SHULKER_BOX) ||
                blockState.is(Blocks.MAGENTA_SHULKER_BOX) ||
                blockState.is(Blocks.ORANGE_SHULKER_BOX) ||
                blockState.is(Blocks.PINK_SHULKER_BOX) ||
                blockState.is(Blocks.PURPLE_SHULKER_BOX) ||
                blockState.is(Blocks.RED_SHULKER_BOX) ||
                blockState.is(Blocks.WHITE_SHULKER_BOX) ||
                blockState.is(Blocks.YELLOW_SHULKER_BOX) ||
                blockState.is(Blocks.COMPOSTER) ||
                blockState.is(Blocks.NOTE_BLOCK) ||
                blockState.is(Blocks.FLOWER_POT) ||
                blockState.is(Blocks.DAYLIGHT_DETECTOR) ||
                blockState.is(Blocks.REPEATER) ||
                blockState.is(Blocks.COMPARATOR) ||
                blockState.is(Blocks.ACACIA_TRAPDOOR) ||
                blockState.is(Blocks.BIRCH_TRAPDOOR) ||
                blockState.is(Blocks.CRIMSON_TRAPDOOR) ||
                blockState.is(Blocks.DARK_OAK_TRAPDOOR) ||
                blockState.is(Blocks.JUNGLE_TRAPDOOR) ||

                //#if MC >= 12000
                blockState.is(Blocks.BAMBOO_TRAPDOOR) ||
                blockState.is(Blocks.CHERRY_TRAPDOOR) ||
                blockState.is(Blocks.MANGROVE_TRAPDOOR) ||
                //#endif

                blockState.is(Blocks.OAK_TRAPDOOR) ||
                blockState.is(Blocks.SPRUCE_TRAPDOOR) ||
                blockState.is(Blocks.WARPED_TRAPDOOR) ||
                blockState.is(Blocks.REDSTONE_WIRE) ||
                blockState.is(Blocks.LEVER) ||
                blockState.is(Blocks.BLACK_BED) ||
                blockState.is(Blocks.BLUE_BED) ||
                blockState.is(Blocks.BROWN_BED) ||
                blockState.is(Blocks.CYAN_BED) ||
                blockState.is(Blocks.GRAY_BED) ||
                blockState.is(Blocks.GREEN_BED) ||
                blockState.is(Blocks.LIGHT_BLUE_BED) ||
                blockState.is(Blocks.LIGHT_GRAY_BED) ||
                blockState.is(Blocks.LIME_BED) ||
                blockState.is(Blocks.MAGENTA_BED) ||
                blockState.is(Blocks.ORANGE_BED) ||
                blockState.is(Blocks.PINK_BED) ||
                blockState.is(Blocks.PURPLE_BED) ||
                blockState.is(Blocks.RED_BED) ||
                blockState.is(Blocks.WHITE_BED) ||
                blockState.is(Blocks.YELLOW_BED) ||
                blockState.is(Blocks.ACACIA_DOOR) ||
                blockState.is(Blocks.BIRCH_DOOR) ||
                blockState.is(Blocks.CRIMSON_DOOR) ||
                blockState.is(Blocks.DARK_OAK_DOOR) ||
                blockState.is(Blocks.JUNGLE_DOOR) ||

                //#if MC >= 12000
                blockState.is(Blocks.BAMBOO_DOOR) ||
                blockState.is(Blocks.CHERRY_DOOR) ||
                blockState.is(Blocks.MANGROVE_DOOR) ||
                //#endif

                blockState.is(Blocks.OAK_DOOR) ||
                blockState.is(Blocks.SPRUCE_DOOR) ||
                blockState.is(Blocks.WARPED_DOOR) ||
                blockState.is(Blocks.ACACIA_FENCE_GATE) ||
                blockState.is(Blocks.BIRCH_FENCE_GATE) ||
                blockState.is(Blocks.CRIMSON_FENCE_GATE) ||
                blockState.is(Blocks.DARK_OAK_FENCE_GATE) ||
                blockState.is(Blocks.JUNGLE_FENCE_GATE) ||

                //#if MC >= 12000
                blockState.is(Blocks.BAMBOO_FENCE_GATE) ||
                blockState.is(Blocks.CHERRY_FENCE_GATE) ||
                blockState.is(Blocks.MANGROVE_FENCE_GATE) ||
                //#endif

                blockState.is(Blocks.OAK_FENCE_GATE) ||
                blockState.is(Blocks.SPRUCE_FENCE_GATE) ||
                blockState.is(Blocks.WARPED_FENCE_GATE) ||
                blockState.is(Blocks.ACACIA_BUTTON) ||
                blockState.is(Blocks.BIRCH_BUTTON) ||
                blockState.is(Blocks.CRIMSON_BUTTON) ||
                blockState.is(Blocks.DARK_OAK_BUTTON) ||
                blockState.is(Blocks.JUNGLE_BUTTON) ||

                //#if MC >= 12000
                blockState.is(Blocks.BAMBOO_BUTTON) ||
                blockState.is(Blocks.CHERRY_BUTTON) ||
                blockState.is(Blocks.MANGROVE_BUTTON) ||
                //#endif

                blockState.is(Blocks.OAK_BUTTON) ||
                blockState.is(Blocks.SPRUCE_BUTTON) ||
                blockState.is(Blocks.WARPED_BUTTON) ||
                blockState.is(Blocks.STONE_BUTTON) ||
                blockState.is(Blocks.ACACIA_SIGN) ||
                blockState.is(Blocks.BIRCH_SIGN) ||
                blockState.is(Blocks.CRIMSON_SIGN) ||
                blockState.is(Blocks.DARK_OAK_SIGN) ||
                blockState.is(Blocks.JUNGLE_SIGN) ||

                //#if MC >= 12000
                blockState.is(Blocks.BAMBOO_SIGN) ||
                blockState.is(Blocks.CHERRY_SIGN) ||
                blockState.is(Blocks.MANGROVE_SIGN) ||
                //#endif

                blockState.is(Blocks.OAK_SIGN) ||
                blockState.is(Blocks.SPRUCE_SIGN) ||
                blockState.is(Blocks.WARPED_SIGN) ||

                //#if MC >= 12000
                blockState.is(Blocks.ACACIA_HANGING_SIGN) ||
                blockState.is(Blocks.BAMBOO_HANGING_SIGN) ||
                blockState.is(Blocks.BIRCH_HANGING_SIGN) ||
                blockState.is(Blocks.CHERRY_HANGING_SIGN) ||
                blockState.is(Blocks.CRIMSON_HANGING_SIGN) ||
                blockState.is(Blocks.DARK_OAK_HANGING_SIGN) ||
                blockState.is(Blocks.JUNGLE_HANGING_SIGN) ||
                blockState.is(Blocks.MANGROVE_HANGING_SIGN) ||
                blockState.is(Blocks.OAK_HANGING_SIGN) ||
                blockState.is(Blocks.SPRUCE_HANGING_SIGN) ||
                blockState.is(Blocks.WARPED_HANGING_SIGN) ||
                blockState.is(Blocks.DECORATED_POT) ||
                //#endif

                blockState.is(Blocks.RESPAWN_ANCHOR) ||
                blockState.is(Blocks.CAKE) ||
                blockState.is(Blocks.BELL);
    }
}
