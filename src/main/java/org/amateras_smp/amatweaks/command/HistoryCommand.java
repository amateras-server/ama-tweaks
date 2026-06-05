// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.command;

import org.amateras_smp.amatweaks.impl.features.InteractionHistory;
import org.amateras_smp.amatweaks.impl.util.TextUtil;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;

public class HistoryCommand {
    public static Command<FabricClientCommandSource> command = HistoryCommand::callback;
    public static Command<FabricClientCommandSource> clearCommand = HistoryCommand::clear;

    private static final Component PREFIX = TextUtil.createEmpty()
            .append(TextUtil.withFormat("[", ChatFormatting.GRAY))
            .append(TextUtil.withFormat("Ama Tweaks - Interaction History", ChatFormatting.GOLD))
            .append(TextUtil.withFormat("] ", ChatFormatting.GRAY));

    public static int callback(CommandContext<FabricClientCommandSource> context) {
        InteractionHistory.printInteraction();

        MutableComponent message = TextUtil.createEmpty();
        boolean hasContent = false;

        if (!InteractionHistory.blockInteractionHistory.isEmpty()) {
            if (hasContent)
                message.append("\n");

            // header
            message.append(TextUtil.withFormat("═══ ", ChatFormatting.DARK_GRAY))
                    .append(TextUtil.withFormat("Block Interactions", ChatFormatting.AQUA))
                    .append(TextUtil.withFormat(" ═══\n", ChatFormatting.DARK_GRAY));

            for (InteractionHistory.BlockInteraction b : InteractionHistory.blockInteractionHistory) {
                message.append(TextUtil.withFormat(" ❖ ", ChatFormatting.DARK_AQUA))
                        .append(TextUtil.withFormat(b.toString(), ChatFormatting.GRAY))
                        .append("\n");
            }
            hasContent = true;
        }

        if (!InteractionHistory.entityInteractionHistory.isEmpty()) {
            if (hasContent)
                message.append("\n");

            // header
            message.append(TextUtil.withFormat("═══ ", ChatFormatting.DARK_GRAY))
                    .append(TextUtil.withFormat("Entity Interactions", ChatFormatting.LIGHT_PURPLE))
                    .append(TextUtil.withFormat(" ═══\n", ChatFormatting.DARK_GRAY));

            for (InteractionHistory.EntityInteraction e : InteractionHistory.entityInteractionHistory) {
                message.append(TextUtil.withFormat(" ❖ ", ChatFormatting.LIGHT_PURPLE))
                        .append(TextUtil.withFormat(e.toString(), ChatFormatting.GRAY))
                        .append("\n");
            }
            hasContent = true;
        }

        if (hasContent) {
            context.getSource().sendFeedback(message);
        } else {
            context.getSource().sendFeedback(
                    TextUtil.createEmpty()
                            .append(PREFIX)
                            .append(TextUtil.withFormat("No interaction history found.", ChatFormatting.RED)));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int clear(CommandContext<FabricClientCommandSource> context) {
        InteractionHistory.blockInteractionHistory.clear();
        InteractionHistory.entityInteractionHistory.clear();

        Component clearMessage = TextUtil.createEmpty()
                .append(PREFIX)
                .append(TextUtil.withFormat("Successfully cleared interaction history cache.", ChatFormatting.GREEN));

        context.getSource().sendFeedback(clearMessage);
        return Command.SINGLE_SUCCESS;
    }
}
