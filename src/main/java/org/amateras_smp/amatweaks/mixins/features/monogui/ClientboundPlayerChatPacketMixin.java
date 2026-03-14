// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.monogui;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.chat.ChatType;

//#if MC >= 11900
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
//#else
//$$ import net.minecraft.network.protocol.game.ClientboundChatPacket;
//#endif

@Mixin(ClientPacketListener.class)
public class ClientboundPlayerChatPacketMixin {
    //#if MC >= 11900
    @Inject(method = "handlePlayerChat", at = @At("HEAD"))
    private void onChatMessagePacket(ClientboundPlayerChatPacket packet, CallbackInfo ci, @Local(argsOnly = true) LocalRef<net.minecraft.network.protocol.game.ClientboundPlayerChatPacket> packetRef) {
        if (!FeatureToggle.TWEAK_MONO_TEAM_COLOR.getBooleanValue()) return;
        //#if MC >= 12006
        ChatType.Bound newParameters = new ChatType.Bound
        //#else
        //$$ ChatType.BoundNetwork newParameters = new ChatType.BoundNetwork
        //#endif
            (
                packet.chatType().chatType(),
                packet.chatType().name().copy().setStyle(Style.EMPTY.withColor(ChatFormatting.RESET)),
                packet.chatType().targetName()
            );


        ClientboundPlayerChatPacket newPacket = new ClientboundPlayerChatPacket(
            //#if MC >= 12105
            packet.globalIndex(),
            //#endif
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
