// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.amateras_smp.amatweaks.impl.features.InteractionHistory;

//#if MC >= 11900
import net.minecraft.text.Text;
//#else
//$$ import net.minecraft.text.LiteralText;
//#endif


public class HistoryCommand {
    public static Command<FabricClientCommandSource> command = HistoryCommand::callback;
    public static Command<FabricClientCommandSource> clearCommand = HistoryCommand::clear;

    public static int callback(CommandContext<FabricClientCommandSource> context) {
        InteractionHistory.printInteraction();
        StringBuilder message = new StringBuilder();
        if (!InteractionHistory.blockInteractionHistory.isEmpty()) {
            message.append("=== Block Interactions ===\n");
            for (var b : InteractionHistory.blockInteractionHistory) {
                message.append(b.toString()).append("\n");
            }
        }
        if (!InteractionHistory.entityInteractionHistory.isEmpty()) {
            message.append("=== Entity Interactions ===\n");
            for (var e : InteractionHistory.entityInteractionHistory) {
                message.append(e.toString()).append("\n");
            }
        }
        if (!message.isEmpty() && message.charAt(message.length() - 1) == '\n') {
            message.deleteCharAt(message.length() - 1);
        }

        if (!message.toString().isBlank()) {
            //#if MC >= 11900
            context.getSource().sendFeedback(Text.literal(message.toString()));
            //#else
            //$$ context.getSource().sendFeedback(new LiteralText(message.toString()));
            //#endif
        }

        return Command.SINGLE_SUCCESS;
    }
    private static int clear(CommandContext<FabricClientCommandSource> context) {
        InteractionHistory.blockInteractionHistory.clear();
        InteractionHistory.entityInteractionHistory.clear();

        String message = "Cleared interaction history cache";
        //#if MC >= 11900
        context.getSource().sendFeedback(Text.literal(message));
        //#else
        //$$ context.getSource().sendFeedback(new LiteralText(message));
        //#endif
        return Command.SINGLE_SUCCESS;
    }
}
