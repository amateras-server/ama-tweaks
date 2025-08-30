// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.addon.litematica;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.amateras_smp.amatweaks.config.Configs;


public class PickRedirect {
    public static ItemStack getShouldPickItem (BlockState schematicState, ItemStack shouldPickItemStack) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null) return shouldPickItemStack;
        PlayerInventory inventory = mc.player.getInventory();

        // if shouldPickStack is in the player inventory why should I override the pick behavior?
        if (inventory.getSlotWithStack(shouldPickItemStack) != -1) return shouldPickItemStack;

        Block shouldPickBlock = null;
        for (String entry : Configs.Lists.PICK_REDIRECT_MAP.getStrings()) {
            String[] splitted = entry.split("\s*,\s*");
            //#if MC < 12100
            if (Registries.BLOCK.get(new Identifier(splitted[0])) == schematicState.getBlock()) {
                shouldPickBlock = Registries.BLOCK.get(new Identifier(splitted[1]));
            }
            //#elseif MC < 12104
            //$$ if (Registries.BLOCK.get(Identifier.of(splitted[0])) == schematicState.getBlock()) {
            //$$ 	shouldPickBlock = Registries.BLOCK.get(Identifier.of(splitted[1]));
            //$$ }
            //#else
            //$$ if (Registries.BLOCK.getEntry(Identifier.of(splitted[0])).get().value() == schematicState.getBlock()) {
            //$$ 	shouldPickBlock = Registries.BLOCK.getEntry(Identifier.of(splitted[1])).get().value();
            //$$ }
            //#endif

            if (shouldPickBlock != null) {
                break;
            }
        }
        if (shouldPickBlock == null) return shouldPickItemStack;

        int slot = inventory.getSlotWithStack(shouldPickBlock.asItem().getDefaultStack());
        if (slot == -1) return shouldPickItemStack;
        return inventory.getStack(slot);
    }
}
