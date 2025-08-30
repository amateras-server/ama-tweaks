// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.InventoryUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.BlockTypeEquals;
import org.amateras_smp.amatweaks.impl.util.InventoryUtil;

//#if MC >= 12006
//$$ import net.minecraft.component.ComponentMap;
//$$ import net.minecraft.component.DataComponentTypes;
//#endif


public class AutoEat {
    private static int beforeHeldHotbarSlot = -1;
    private static int foodTakenInventorySlot = -1;
    private static boolean eating = false;

    public static void autoEat(MinecraftClient mc, ClientPlayerEntity player, ClientPlayNetworkHandler networkHandler, boolean onlyCheck) {
        if (!onlyCheck && player.getHungerManager().isNotFull() && player.getHungerManager().getFoodLevel() <= Configs.Generic.AUTO_EAT_THRESHOLD.getDoubleValue() * 20) {
            if (shouldAutoEat(mc)) {
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
                    return;
                }

                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack stack = player.getInventory().getStack(i);
                    //#if MC >= 12006
                    //$$ if (stack.getItem().getComponents().contains(DataComponentTypes.FOOD)) {
                    //#else
                    if (stack.getItem().isFood()) {
                    //#endif
                        holdOrSwap(i, Configs.Generic.FOOD_SWITCHABLE_SLOT.getIntegerValue());
                        if (eating) {
                            KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.useKey.getBoundKeyTranslationKey()), true);
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

    private static boolean shouldAutoEat(MinecraftClient mc) {
        if (mc.interactionManager == null || mc.interactionManager.getCurrentGameMode().isCreative()) return false;
        if (!Configs.Generic.CANCEL_AUTO_EAT_WHILE_DOING_ACTION.getBooleanValue()) return true;
        return !mc.options.useKey.isPressed() && !mc.options.attackKey.isPressed();
    }

    private static void autoEatCheck(MinecraftClient mc, ClientPlayerEntity player, ClientPlayNetworkHandler networkHandler) {
        if (eating && !player.isUsingItem()) {
            if (Configs.Generic.AUTO_EAT_PUT_BACK_FOOD.getBooleanValue() && foodTakenInventorySlot != -1) {
                InventoryUtils.swapSlots(player.currentScreenHandler, foodTakenInventorySlot, InventoryUtil.getSelectedSlot(player.getInventory()));
                foodTakenInventorySlot = -1;
            }
            if (beforeHeldHotbarSlot != -1) {
                InventoryUtil.setSelectedSlot(player.getInventory(), beforeHeldHotbarSlot);
                networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(beforeHeldHotbarSlot));
                beforeHeldHotbarSlot = -1;
            }
            eating = false;

            KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.useKey.getBoundKeyTranslationKey()), false);
        }
    }

    private static void holdOrSwap(int sourceInventorySlot, int targetHotbarSlot) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;

        if (player == null || mc.getNetworkHandler() == null || mc.interactionManager == null) return;

        //#if MC >= 11802
        World world = player.getWorld();
        //#else
        //$$ World world = player.world;
        //#endif

        if (world == null) return;

        PlayerInventory inventory = player.getInventory();
        ScreenHandler container = player.playerScreenHandler;
        if (sourceInventorySlot >= 0 && sourceInventorySlot != InventoryUtil.getSelectedSlot(inventory) && player.currentScreenHandler == player.playerScreenHandler) {
            beforeHeldHotbarSlot = InventoryUtil.getSelectedSlot(inventory);

            // source is hotbar slot -> hold source slot
            // or else -> swap source and target, then hold target slot

            if (PlayerInventory.isValidHotbarIndex(sourceInventorySlot)) {
                InventoryUtil.setSelectedSlot(inventory, sourceInventorySlot);
                mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(sourceInventorySlot));
            } else {
                if (InventoryUtil.getSelectedSlot(inventory) != targetHotbarSlot) {
                    InventoryUtil.setSelectedSlot(inventory, targetHotbarSlot);
                    mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(targetHotbarSlot));
                }

                InventoryUtils.swapSlots(container, sourceInventorySlot, targetHotbarSlot);

                foodTakenInventorySlot = sourceInventorySlot;
            }
        }
    }
}
