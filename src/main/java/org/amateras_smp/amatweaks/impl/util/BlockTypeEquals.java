// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class BlockTypeEquals {
    public static boolean isSneakingInteractionCancel(BlockState blockState) {
        return blockState.isOf(Blocks.CRAFTING_TABLE) ||
                blockState.isOf(Blocks.STONECUTTER) ||
                blockState.isOf(Blocks.CARTOGRAPHY_TABLE) ||
                blockState.isOf(Blocks.SMITHING_TABLE) ||
                blockState.isOf(Blocks.GRINDSTONE) ||
                blockState.isOf(Blocks.LOOM) ||
                blockState.isOf(Blocks.FURNACE) ||
                blockState.isOf(Blocks.SMOKER) ||
                blockState.isOf(Blocks.BLAST_FURNACE) ||
                blockState.isOf(Blocks.ANVIL) ||
                blockState.isOf(Blocks.CHIPPED_ANVIL) ||
                blockState.isOf(Blocks.DAMAGED_ANVIL) ||
                blockState.isOf(Blocks.ENCHANTING_TABLE) ||
                blockState.isOf(Blocks.BREWING_STAND) ||
                blockState.isOf(Blocks.BEACON) ||
                blockState.isOf(Blocks.CHEST) ||
                blockState.isOf(Blocks.BARREL) ||
                blockState.isOf(Blocks.ENDER_CHEST) ||
                blockState.isOf(Blocks.DISPENSER) ||
                blockState.isOf(Blocks.DROPPER) ||
                blockState.isOf(Blocks.HOPPER) ||
                blockState.isOf(Blocks.TRAPPED_CHEST) ||
                blockState.isOf(Blocks.SHULKER_BOX) ||
                blockState.isOf(Blocks.BLACK_SHULKER_BOX) ||
                blockState.isOf(Blocks.BLUE_SHULKER_BOX) ||
                blockState.isOf(Blocks.BROWN_SHULKER_BOX) ||
                blockState.isOf(Blocks.CYAN_SHULKER_BOX) ||
                blockState.isOf(Blocks.GRAY_SHULKER_BOX) ||
                blockState.isOf(Blocks.GREEN_SHULKER_BOX) ||
                blockState.isOf(Blocks.LIGHT_BLUE_SHULKER_BOX) ||
                blockState.isOf(Blocks.LIGHT_GRAY_SHULKER_BOX) ||
                blockState.isOf(Blocks.LIME_SHULKER_BOX) ||
                blockState.isOf(Blocks.MAGENTA_SHULKER_BOX) ||
                blockState.isOf(Blocks.ORANGE_SHULKER_BOX) ||
                blockState.isOf(Blocks.PINK_SHULKER_BOX) ||
                blockState.isOf(Blocks.PURPLE_SHULKER_BOX) ||
                blockState.isOf(Blocks.RED_SHULKER_BOX) ||
                blockState.isOf(Blocks.WHITE_SHULKER_BOX) ||
                blockState.isOf(Blocks.YELLOW_SHULKER_BOX) ||
                blockState.isOf(Blocks.COMPOSTER) ||
                blockState.isOf(Blocks.NOTE_BLOCK) ||
                blockState.isOf(Blocks.FLOWER_POT) ||
                blockState.isOf(Blocks.DAYLIGHT_DETECTOR) ||
                blockState.isOf(Blocks.REPEATER) ||
                blockState.isOf(Blocks.COMPARATOR) ||
                blockState.isOf(Blocks.ACACIA_TRAPDOOR) ||
                blockState.isOf(Blocks.BIRCH_TRAPDOOR) ||
                blockState.isOf(Blocks.CRIMSON_TRAPDOOR) ||
                blockState.isOf(Blocks.DARK_OAK_TRAPDOOR) ||
                blockState.isOf(Blocks.JUNGLE_TRAPDOOR) ||

                //#if MC >= 12000
                blockState.isOf(Blocks.BAMBOO_TRAPDOOR) ||
                blockState.isOf(Blocks.CHERRY_TRAPDOOR) ||
                blockState.isOf(Blocks.MANGROVE_TRAPDOOR) ||
                //#endif

                blockState.isOf(Blocks.OAK_TRAPDOOR) ||
                blockState.isOf(Blocks.SPRUCE_TRAPDOOR) ||
                blockState.isOf(Blocks.WARPED_TRAPDOOR) ||
                blockState.isOf(Blocks.REDSTONE_WIRE) ||
                blockState.isOf(Blocks.LEVER) ||
                blockState.isOf(Blocks.BLACK_BED) ||
                blockState.isOf(Blocks.BLUE_BED) ||
                blockState.isOf(Blocks.BROWN_BED) ||
                blockState.isOf(Blocks.CYAN_BED) ||
                blockState.isOf(Blocks.GRAY_BED) ||
                blockState.isOf(Blocks.GREEN_BED) ||
                blockState.isOf(Blocks.LIGHT_BLUE_BED) ||
                blockState.isOf(Blocks.LIGHT_GRAY_BED) ||
                blockState.isOf(Blocks.LIME_BED) ||
                blockState.isOf(Blocks.MAGENTA_BED) ||
                blockState.isOf(Blocks.ORANGE_BED) ||
                blockState.isOf(Blocks.PINK_BED) ||
                blockState.isOf(Blocks.PURPLE_BED) ||
                blockState.isOf(Blocks.RED_BED) ||
                blockState.isOf(Blocks.WHITE_BED) ||
                blockState.isOf(Blocks.YELLOW_BED) ||
                blockState.isOf(Blocks.ACACIA_DOOR) ||
                blockState.isOf(Blocks.BIRCH_DOOR) ||
                blockState.isOf(Blocks.CRIMSON_DOOR) ||
                blockState.isOf(Blocks.DARK_OAK_DOOR) ||
                blockState.isOf(Blocks.JUNGLE_DOOR) ||

                //#if MC >= 12000
                blockState.isOf(Blocks.BAMBOO_DOOR) ||
                blockState.isOf(Blocks.CHERRY_DOOR) ||
                blockState.isOf(Blocks.MANGROVE_DOOR) ||
                //#endif

                blockState.isOf(Blocks.OAK_DOOR) ||
                blockState.isOf(Blocks.SPRUCE_DOOR) ||
                blockState.isOf(Blocks.WARPED_DOOR) ||
                blockState.isOf(Blocks.ACACIA_FENCE_GATE) ||
                blockState.isOf(Blocks.BIRCH_FENCE_GATE) ||
                blockState.isOf(Blocks.CRIMSON_FENCE_GATE) ||
                blockState.isOf(Blocks.DARK_OAK_FENCE_GATE) ||
                blockState.isOf(Blocks.JUNGLE_FENCE_GATE) ||

                //#if MC >= 12000
                blockState.isOf(Blocks.BAMBOO_FENCE_GATE) ||
                blockState.isOf(Blocks.CHERRY_FENCE_GATE) ||
                blockState.isOf(Blocks.MANGROVE_FENCE_GATE) ||
                //#endif

                blockState.isOf(Blocks.OAK_FENCE_GATE) ||
                blockState.isOf(Blocks.SPRUCE_FENCE_GATE) ||
                blockState.isOf(Blocks.WARPED_FENCE_GATE) ||
                blockState.isOf(Blocks.ACACIA_BUTTON) ||
                blockState.isOf(Blocks.BIRCH_BUTTON) ||
                blockState.isOf(Blocks.CRIMSON_BUTTON) ||
                blockState.isOf(Blocks.DARK_OAK_BUTTON) ||
                blockState.isOf(Blocks.JUNGLE_BUTTON) ||

                //#if MC >= 12000
                blockState.isOf(Blocks.BAMBOO_BUTTON) ||
                blockState.isOf(Blocks.CHERRY_BUTTON) ||
                blockState.isOf(Blocks.MANGROVE_BUTTON) ||
                //#endif

                blockState.isOf(Blocks.OAK_BUTTON) ||
                blockState.isOf(Blocks.SPRUCE_BUTTON) ||
                blockState.isOf(Blocks.WARPED_BUTTON) ||
                blockState.isOf(Blocks.STONE_BUTTON) ||
                blockState.isOf(Blocks.ACACIA_SIGN) ||
                blockState.isOf(Blocks.BIRCH_SIGN) ||
                blockState.isOf(Blocks.CRIMSON_SIGN) ||
                blockState.isOf(Blocks.DARK_OAK_SIGN) ||
                blockState.isOf(Blocks.JUNGLE_SIGN) ||

                //#if MC >= 12000
                blockState.isOf(Blocks.BAMBOO_SIGN) ||
                blockState.isOf(Blocks.CHERRY_SIGN) ||
                blockState.isOf(Blocks.MANGROVE_SIGN) ||
                //#endif

                blockState.isOf(Blocks.OAK_SIGN) ||
                blockState.isOf(Blocks.SPRUCE_SIGN) ||
                blockState.isOf(Blocks.WARPED_SIGN) ||

                //#if MC >= 12000
                blockState.isOf(Blocks.ACACIA_HANGING_SIGN) ||
                blockState.isOf(Blocks.BAMBOO_HANGING_SIGN) ||
                blockState.isOf(Blocks.BIRCH_HANGING_SIGN) ||
                blockState.isOf(Blocks.CHERRY_HANGING_SIGN) ||
                blockState.isOf(Blocks.CRIMSON_HANGING_SIGN) ||
                blockState.isOf(Blocks.DARK_OAK_HANGING_SIGN) ||
                blockState.isOf(Blocks.JUNGLE_HANGING_SIGN) ||
                blockState.isOf(Blocks.MANGROVE_HANGING_SIGN) ||
                blockState.isOf(Blocks.OAK_HANGING_SIGN) ||
                blockState.isOf(Blocks.SPRUCE_HANGING_SIGN) ||
                blockState.isOf(Blocks.WARPED_HANGING_SIGN) ||
                blockState.isOf(Blocks.DECORATED_POT) ||
                //#endif

                blockState.isOf(Blocks.RESPAWN_ANCHOR) ||
                blockState.isOf(Blocks.CAKE) ||
                blockState.isOf(Blocks.BELL);
    }
}
