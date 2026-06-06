// Copyright (c) 2026 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.item_pickup_filter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.amateras_smp.amatweaks.impl.features.ItemPickupFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handleContainerSetSlot", at = @At("TAIL"))
    private void onContainerSlotUpdate(final ClientboundContainerSetSlotPacket packet, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        // Check if the container being updated is the player's own inventory
        // (containerId 0 always represents the player's inventory/crafting grid)
        if (packet.getContainerId() != 0) {
            return;
        }

        int slotId = packet.getSlot();
        ItemStack itemStack = packet.getItem();

        if (slotId >= Inventory.getSelectionSize() && slotId < Inventory.INVENTORY_SIZE + Inventory.getSelectionSize()) {
            minecraft.execute(() -> {
                // ClickSlot and drop the stack all.
                ItemPickupFilter.acceptOrDrop(minecraft, minecraft.player, slotId, itemStack);
            });
        }
    }
}
