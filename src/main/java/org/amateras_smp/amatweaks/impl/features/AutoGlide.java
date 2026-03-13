// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.InventoryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.BlockTypeEquals;
import org.amateras_smp.amatweaks.impl.util.InventoryUtil;

//#if MC >= 12006
import java.util.Objects;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
//#else
//$$ import net.minecraft.nbt.CompoundTag;
//#endif

public class AutoGlide {
    private static int beforeHeldHotbarSlot = -1;
    private static int rocketTakenInventorySlot = -1;

    public static void autoUseRocket(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null) return;

        HitResult hit = mc.hitResult;
        if (hit == null) return;
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hitBlock = (BlockHitResult) hit;
            BlockPos hitBlockPos = hitBlock.getBlockPos();

            // make sure hitBlock can't be interacted.
            if (BlockTypeEquals.isSneakingInteractionCancel(player.level().getBlockState(hitBlockPos))) {
                return;
            }
        } else if (hit.getType() == HitResult.Type.ENTITY) {
            // hit target is entity, so just end this function.
            return;
        }

        if (player.getInventory().getItem(InventoryUtil.getSelectedSlot(player.getInventory())).is(Items.FIREWORK_ROCKET)) {
            use(mc, player);
            return;
        }

        int maxFlightLevelInInventory = 0;
        int rocketSlot = -1;
        for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(Items.FIREWORK_ROCKET)) {
                //#if MC >= 12006
                DataComponentMap component = stack.getComponents();
                if (component.has(DataComponents.FIREWORKS)) {
                if (component.get(DataComponents.FIREWORKS) == null) return;
                int flightLevel = Objects.requireNonNull(component.get(DataComponents.FIREWORKS)).flightDuration();
                //#else
                //$$ CompoundTag nbt = stack.getTag();
                //$$ if (nbt != null && nbt.contains("Fireworks")) {
                //$$     int flightLevel = nbt.getCompound("Fireworks").getInt("Flight");
                //#endif
                    if (flightLevel > maxFlightLevelInInventory) {
                        maxFlightLevelInInventory = flightLevel;
                        rocketSlot = i;
                    }
                }
            }
        }
        if (rocketSlot == -1) return;
        if (InventoryUtil.getSelectedSlot(player.getInventory()) != rocketSlot) {
            holdOrSwap(rocketSlot, Configs.Generic.FIREWORK_SWITCHABLE_SLOT.getIntegerValue());
        }
        use(mc, player);
    }

    private static void use(Minecraft mc, LocalPlayer player) {
        if (mc.gameMode == null) return;
        //#if MC >= 11900
        mc.gameMode.useItem(player, InteractionHand.MAIN_HAND);
        //#else
        //$$ mc.gameMode.useItem(player, player.clientLevel, InteractionHand.MAIN_HAND);
        //#endif
        afterUse(player, player.connection);
    }

    private static void afterUse(LocalPlayer player, ClientPacketListener networkHandler) {
        if (Configs.Generic.AUTO_GLIDE_PUT_BACK_ROCKET.getBooleanValue() && rocketTakenInventorySlot != -1) {
            InventoryUtils.swapSlots(player.containerMenu, rocketTakenInventorySlot, InventoryUtil.getSelectedSlot(player.getInventory()));
            rocketTakenInventorySlot = -1;
        }
        if (beforeHeldHotbarSlot != -1) {
            InventoryUtil.setSelectedSlot(player.getInventory(), beforeHeldHotbarSlot);
            networkHandler.send(new ServerboundSetCarriedItemPacket(beforeHeldHotbarSlot));
            beforeHeldHotbarSlot = -1;
        }
    }

    private static void holdOrSwap(int sourceInventorySlot, int targetHotbarSlot) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.getConnection() == null || mc.gameMode == null) return;
        Inventory inventory = player.getInventory();
        AbstractContainerMenu container = player.inventoryMenu;
        if (sourceInventorySlot >= 0 && sourceInventorySlot != InventoryUtil.getSelectedSlot(inventory) && container == player.containerMenu) {
            beforeHeldHotbarSlot = InventoryUtil.getSelectedSlot(inventory);

            // source is hotbar slot -> hold source slot
            // or else -> swap source and target, then hold target slot

            if (Inventory.isHotbarSlot(sourceInventorySlot)) {
                InventoryUtil.setSelectedSlot(inventory, sourceInventorySlot);
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(sourceInventorySlot));
            } else {
                if (InventoryUtil.getSelectedSlot(inventory) != targetHotbarSlot) {
                    InventoryUtil.setSelectedSlot(inventory, targetHotbarSlot);
                    mc.getConnection().send(new ServerboundSetCarriedItemPacket(targetHotbarSlot));
                }

                InventoryUtils.swapSlots(container, sourceInventorySlot, targetHotbarSlot);

                rocketTakenInventorySlot = sourceInventorySlot;
            }
        }
    }
}
