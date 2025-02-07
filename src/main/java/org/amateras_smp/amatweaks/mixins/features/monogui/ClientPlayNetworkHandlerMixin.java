// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.monogui;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11900
import net.minecraft.network.message.MessageType;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
//#else
//$$ import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
//$$ import net.minecraft.network.MessageType;
//#endif

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    //#if MC >= 11900 && MC < 12005
    @Inject(method = "onChatMessage", at = @At("HEAD"))
    private void onChatMessagePacket(ChatMessageS2CPacket packet, CallbackInfo ci, @Local(argsOnly = true) LocalRef<ChatMessageS2CPacket> packetRef) {
        if (!FeatureToggle.TWEAK_MONO_TEAM_COLOR.getBooleanValue()) return;
        MessageType.Serialized newParameters = new MessageType.Serialized(
                packet.serializedParameters().typeId(),
                packet.serializedParameters().name().copy().setStyle(Style.EMPTY.withColor(Formatting.RESET)),
                packet.serializedParameters().targetName()
        );


        ChatMessageS2CPacket newPacket = new ChatMessageS2CPacket(
                packet.sender(),
                packet.index(),
                packet.signature(),
                packet.body(),
                packet.unsignedContent(),
                packet.filterMask(),
                newParameters
        );
        packetRef.set(newPacket);
    }
    //#endif
}
