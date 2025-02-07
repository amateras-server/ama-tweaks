// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.InventoryUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.BlockTypeEquals;

//#if MC < 12006
import net.minecraft.nbt.NbtCompound;
//#else
//$$ import java.util.Objects;
//$$ import net.minecraft.component.ComponentMap;
//$$ import net.minecraft.component.DataComponentTypes;
//#endif

public class AutoGlide {
    private static int beforeHeldHotbarSlot = -1;
    private static int rocketTakenInventorySlot = -1;

    public static void autoUseRocket(MinecraftClient mc) {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        HitResult hit = mc.crosshairTarget;
        if (hit == null) return;
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hitBlock = (BlockHitResult) hit;
            BlockPos hitBlockPos = hitBlock.getBlockPos();

            // make sure hitBlock can't be interacted.
            if (BlockTypeEquals.isSneakingInteractionCancel(player.getWorld().getBlockState(hitBlockPos))) {
                return;
            }
        } else if (hit.getType() == HitResult.Type.ENTITY) {
            // hit target is entity, so just end this function.
            return;
        }

        if (player.getInventory().getStack(player.getInventory().selectedSlot).isOf(Items.FIREWORK_ROCKET)) {
            use(mc, player);
            return;
        }

        int maxFlightLevelInInventory = 0;
        int rocketSlot = -1;
        for (int i = 0; i < PlayerInventory.MAIN_SIZE; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(Items.FIREWORK_ROCKET)) {
                //#if MC >= 12006
                //$$ ComponentMap component = stack.getComponents();
                //$$ if (component.contains(DataComponentTypes.FIREWORKS)) {
                //$$ if (component.get(DataComponentTypes.FIREWORKS) == null) return;
                //$$ int flightLevel = Objects.requireNonNull(component.get(DataComponentTypes.FIREWORKS)).flightDuration();
                //#else
                NbtCompound nbt = stack.getNbt();
                if (nbt != null && nbt.contains("Fireworks")) {
                    int flightLevel = nbt.getCompound("Fireworks").getInt("Flight");
                //#endif
                    if (flightLevel > maxFlightLevelInInventory) {
                        maxFlightLevelInInventory = flightLevel;
                        rocketSlot = i;
                    }
                }
            }
        }
        if (rocketSlot == -1) return;
        if (player.getInventory().selectedSlot != rocketSlot) {
            holdOrSwap(rocketSlot, Configs.Generic.FIREWORK_SWITCHABLE_SLOT.getIntegerValue());
        }
        use(mc, player);
    }

    private static void use(MinecraftClient mc, ClientPlayerEntity player) {
        if (mc.interactionManager == null) return;
        //#if MC >= 11900
        mc.interactionManager.interactItem(player, Hand.MAIN_HAND);
        //#else
        //$$ mc.interactionManager.interactItem(player, player.clientWorld, Hand.MAIN_HAND);
        //#endif
        afterUse(player, player.networkHandler);
    }

    private static void afterUse(ClientPlayerEntity player, ClientPlayNetworkHandler networkHandler) {
        if (Configs.Generic.AUTO_GLIDE_PUT_BACK_ROCKET.getBooleanValue() && rocketTakenInventorySlot != -1) {
            InventoryUtils.swapSlots(player.currentScreenHandler, rocketTakenInventorySlot, player.getInventory().selectedSlot);
            rocketTakenInventorySlot = -1;
        }
        if (beforeHeldHotbarSlot != -1) {
            player.getInventory().selectedSlot = beforeHeldHotbarSlot;
            networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(beforeHeldHotbarSlot));
            beforeHeldHotbarSlot = -1;
        }
    }

    private static void holdOrSwap(int sourceInventorySlot, int targetHotbarSlot) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        //#if MC >= 11802
        if (player == null || player.getWorld() == null || mc.getNetworkHandler() == null || mc.interactionManager == null) return;
        //#else
        //$$ if (player == null || player.world == null || mc.getNetworkHandler() == null || mc.interactionManager == null) return;
        //#endif
        PlayerInventory inventory = player.getInventory();
        ScreenHandler container = player.playerScreenHandler;
        if (sourceInventorySlot >= 0 && sourceInventorySlot != inventory.selectedSlot && player.currentScreenHandler == player.playerScreenHandler) {
            beforeHeldHotbarSlot = inventory.selectedSlot;

            // source is hotbar slot -> hold source slot
            // or else -> swap source and target, then hold target slot

            if (PlayerInventory.isValidHotbarIndex(sourceInventorySlot)) {
                inventory.selectedSlot = sourceInventorySlot;
                mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(inventory.selectedSlot));
            } else {
                if (inventory.selectedSlot != targetHotbarSlot) {
                    inventory.selectedSlot = targetHotbarSlot;
                    mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(player.getInventory().selectedSlot));
                }

                InventoryUtils.swapSlots(container, sourceInventorySlot, targetHotbarSlot);

                rocketTakenInventorySlot = sourceInventorySlot;
            }
        }
    }
}
