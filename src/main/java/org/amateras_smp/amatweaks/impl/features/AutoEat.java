// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.InventoryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;

import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.BlockTypeEquals;
import org.amateras_smp.amatweaks.impl.util.InventoryUtil;

//#if MC >= 12006
import net.minecraft.core.component.DataComponents;
//#endif

public class AutoEat {
    private static int beforeHeldHotbarSlot = -1;
    private static int foodTakenInventorySlot = -1;
    private static boolean eating = false;

    public static void autoEat(Minecraft mc, LocalPlayer player, ClientPacketListener networkHandler) {
        if (player.getFoodData().needsFood() && player.getFoodData().getFoodLevel() <= Configs.Generic.AUTO_EAT_THRESHOLD.getDoubleValue() * 20) {
            if (shouldAutoEat(mc)) {
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
                    return;
                }

                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    //#if MC >= 12006
                    if (stack.getItem().components().has(DataComponents.FOOD)) {
                    //#else
                    //$$ if (stack.getItem().isEdible()) {
                    //#endif
                        holdOrSwap(i, Configs.Generic.FOOD_SWITCHABLE_SLOT.getIntegerValue());
                        if (eating) {
                            KeyMapping.set(InputConstants.getKey(mc.options.keyUse.saveString()), true);
                        }
                        eating = true;
                        break;
                    }
                }
            }
        } else {
            autoEatCheck(mc, player, networkHandler);
        }
    }

    private static boolean shouldAutoEat(Minecraft mc) {
        if (mc.gameMode == null || mc.player == null || mc.gameMode.getPlayerMode().isCreative()) return false;
        boolean isBusy = mc.options.keyUse.isDown() || mc.options.keyAttack.isDown();
        if (isBusy && Configs.Generic.AUTO_EAT_DISABLE_WHILE_IN_USE.getBooleanValue()) return false;
        if (mc.player.isFallFlying() && Configs.Generic.AUTO_EAT_DISABLE_WHILE_ELYTRA_FLYING.getBooleanValue()) return false;
        return true;
    }

    private static void autoEatCheck(Minecraft mc, LocalPlayer player, ClientPacketListener networkHandler) {
        if (eating && !player.isUsingItem()) {
            if (Configs.Generic.AUTO_EAT_PUT_BACK_FOOD.getBooleanValue() && foodTakenInventorySlot != -1) {
                InventoryUtils.swapSlots(player.containerMenu, foodTakenInventorySlot, InventoryUtil.getSelectedSlot(player.getInventory()));
                foodTakenInventorySlot = -1;
            }
            if (beforeHeldHotbarSlot != -1) {
                InventoryUtil.setSelectedSlot(player.getInventory(), beforeHeldHotbarSlot);
                networkHandler.send(new ServerboundSetCarriedItemPacket(beforeHeldHotbarSlot));
                beforeHeldHotbarSlot = -1;
            }
            eating = false;

            KeyMapping.set(InputConstants.getKey(mc.options.keyUse.saveString()), false);
        }
    }

    private static void holdOrSwap(int sourceInventorySlot, int targetHotbarSlot) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.getConnection() == null || mc.gameMode == null) return;

        Inventory inventory = player.getInventory();
        InventoryMenu container = player.inventoryMenu;
        if (sourceInventorySlot >= 0 && sourceInventorySlot != InventoryUtil.getSelectedSlot(inventory) && player.containerMenu == container) {
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

                foodTakenInventorySlot = sourceInventorySlot;
            }
        }
    }
}
