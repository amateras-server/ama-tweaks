// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.amateras_smp.amatweaks.AmaTweaks;

//#if MC < 11900
//$$ import com.mojang.brigadier.exceptions.CommandSyntaxException;
//#endif

public class ClientCommandUtil {
    public static boolean executeCommand(String input) {
        AmaTweaks.LOGGER.debug("Executing client command : \"{}\"", input);
        Minecraft client = Minecraft.getInstance();
        ClientPacketListener connection = client.getConnection();
        if (connection == null) return false;
        //#if MC >= 12108
        connection.sendCommand(input);
        return true;
        //#elseif MC >= 11900
        //$$ return connection.sendUnsignedCommand(input);
        //#else
        //$$ try {
        //$$     connection.getCommands().execute(input, connection.getSuggestionsProvider());
        //$$ } catch (CommandSyntaxException e) {
        //$$     throw new RuntimeException(e);
        //$$ }
        //$$ return true;
        //#endif
    }
}
