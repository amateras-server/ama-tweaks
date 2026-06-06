// Copyright (c) 2026 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.item_pickup_filter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.amateras_smp.amatweaks.AmaTweaks;
import org.amateras_smp.amatweaks.impl.features.ItemPickupFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Unique
    private Item amatweaks$lastPickedItem = null;

    @Inject(method = "handleTakeItemEntity", at = @At("HEAD"))
    private void onItemPickup(final ClientboundTakeItemEntityPacket packet, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null || packet.getPlayerId() != minecraft.player.getId()) {
            return;
        }

        Entity pickedEntity = minecraft.level.getEntity(packet.getItemId());
        if (pickedEntity instanceof ItemEntity itemEntity) {
            ItemStack itemStack = itemEntity.getItem();
            if (!itemStack.isEmpty()) {
                this.amatweaks$lastPickedItem = itemStack.getItem();
            }
        }
    }

    @Inject(method = "handleContainerSetSlot", at = @At("TAIL"))
    private void onContainerSlotUpdate(final ClientboundContainerSetSlotPacket packet, CallbackInfo ci) {
        if (amatweaks$lastPickedItem == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        int packetContainerId = packet.getContainerId();
        if (packetContainerId != minecraft.player.containerMenu.containerId) {
            return;
        }

        int slotId = packet.getSlot();
        ItemStack itemStack = packet.getItem();

        if (!itemStack.isEmpty() && itemStack.getItem() == this.amatweaks$lastPickedItem) {
            this.amatweaks$lastPickedItem = null;
            minecraft.execute(() -> {
                // ClickSlot and drop the stack all.
                ItemPickupFilter.acceptOrDrop(minecraft, minecraft.player, packetContainerId, slotId, itemStack);
            });
        }
    }
}
