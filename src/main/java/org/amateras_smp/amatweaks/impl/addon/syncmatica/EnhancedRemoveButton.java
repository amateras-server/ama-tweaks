// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.addon.syncmatica;

import ch.endte.syncmatica.Context;
import ch.endte.syncmatica.communication.ClientCommunicationManager;
import ch.endte.syncmatica.communication.ExchangeTarget;
import ch.endte.syncmatica.communication.PacketType;
import ch.endte.syncmatica.litematica.LitematicManager;
import ch.endte.syncmatica.litematica.ScreenHelper;
import ch.endte.syncmatica.litematica.gui.WidgetSyncmaticaServerPlacementEntry;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import org.amateras_smp.amatweaks.config.Configs;

import java.util.UUID;

public class EnhancedRemoveButton {
    public static class ButtonListener implements IButtonActionListener {
        WidgetSyncmaticaServerPlacementEntry placement;
        UUID placementId;

        public ButtonListener(WidgetSyncmaticaServerPlacementEntry placement, UUID placementId) {
            this.placement = placement;
            this.placementId = placementId;
        }

        public void actionPerformedWithButton(ButtonBase button, int arg1) {
            if(Configs.Disable.DISABLE_SYNCMATICA_REMOVE_BUTTON.getBooleanValue()) {
                ScreenHelper.ifPresent((s) -> {
                    s.addMessage(Message.MessageType.ERROR, "ama_tweaks.error.syncmatica_placements.remove_disabled");
                });
                return;
            }
            if (Configs.Generic.SYNCMATICA_REMOVE_REQUIRE_SHIFT.getBooleanValue() && !GuiBase.isShiftDown()) {
                ScreenHelper.ifPresent((s) -> s.addMessage(Message.MessageType.ERROR, "ama_tweaks.error.syncmatica_placements.remove_require_shift"));
                return;
            }
            Context con = LitematicManager.getInstance().getActiveContext();
            ExchangeTarget server = ((ClientCommunicationManager)con.getCommunicationManager()).getServer();
            PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
            packetBuf.writeUuid(placementId);
            //#if MC > 12004
            //$$ server.sendPacket(PacketType.REMOVE_SYNCMATIC, packetBuf, LitematicManager.getInstance().getActiveContext());
            //#else
            server.sendPacket(PacketType.REMOVE_SYNCMATIC.identifier, packetBuf, LitematicManager.getInstance().getActiveContext());
            //#endif
        }
    }
}
