// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.client.MinecraftClient;
import org.amateras_smp.amatweaks.AmaTweaks;

//#if MC < 11900
//$$ import com.mojang.brigadier.exceptions.CommandSyntaxException;
//#endif

public class ClientCommandUtil {
    public static boolean executeCommand(String input) {
        AmaTweaks.LOGGER.debug("Executing client command : \"{}\"", input);
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) return false;
        //#if MC >= 12108
        //$$ client.getNetworkHandler().sendChatCommand(input);
        //$$ return true;
        //#elseif MC >= 11900
        return client.getNetworkHandler().sendCommand(input);
        //#else
        //$$ try {
        //$$     client.getNetworkHandler().getCommandDispatcher()
        //$$         .execute(input, client.getNetworkHandler().getCommandSource());
        //$$ } catch (CommandSyntaxException e) {
        //$$     throw new RuntimeException(e);
        //$$ }
        //$$ return true;
        //#endif
    }
}
