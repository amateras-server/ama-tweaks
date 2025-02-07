// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

import java.util.List;

public class Hotkeys {
    public static final ConfigHotkey OPEN_CONFIG_GUI = new ConfigHotkey("openConfigGui","LEFT_ALT,K","Opens Config GUI Screen");
    public static final ConfigHotkey REFRESH_MATERIAL_LIST = new ConfigHotkey("refreshMaterialList", "", "Refreshes schematic material list of litematica.");
    public static final ConfigHotkey SEE_INTERACTION_HISTORY = new ConfigHotkey("seeInteractionHistory", "", "Prints interaction history");

    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_CONFIG_GUI,
            REFRESH_MATERIAL_LIST,
            SEE_INTERACTION_HISTORY
    );
}
