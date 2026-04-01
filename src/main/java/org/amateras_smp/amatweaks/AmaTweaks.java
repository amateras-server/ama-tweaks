// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// import org.apache.logging.log4j.core.config.Configurator;
// import static org.apache.logging.log4j.Level.DEBUG;

public class AmaTweaks implements ClientModInitializer {
    public static Logger LOGGER;

    @Override
    public void onInitializeClient() {
        LOGGER = LogManager.getLogger(Reference.kModName);
        // Configurator.setLevel(LOGGER, DEBUG);

        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
        InitHandler.registerCommandsOnClientLoad();
        LOGGER.info("{} (version {}) has initialized!", Reference.kModName, Reference.kModVersion);
    }
}
