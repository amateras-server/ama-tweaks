// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks;

import net.fabricmc.loader.api.FabricLoader;

public class Reference {
    public static final String kModId = "ama-tweaks";
    public static final String kModName = "AmaTweaks";
    public static final String kSnakeCaseModName = "ama_tweaks";
    public static final String kModVersion = FabricLoader.getInstance().getModContainer(kModId).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();

    public static class ModIds {
        public static final String minecraft = "minecraft";
        public static final String amatweaks = kModId;
        public static final String amacarpet = "ama-carpet";
        public static final String fabric_loader = "fabricloader";
        public static final String fabric_api = "fabric";
        public static final String kyoyu = "kyoyu";
        public static final String malilib = "malilib";
        public static final String tweakeroo = "tweakeroo";
        public static final String tweakermore = "tweakermore";
        public static final String masaadditions = "masaadditions";
        public static final String itemscroller = "itemscroller";
        public static final String litematica = "litematica";
        public static final String syncmatica = "syncmatica";
        public static final String minihud = "minihud";
        public static final String cheat_utils = "cheatutils";
        public static final String compact_chat = "compactchat";
        public static final String more_chat_history = "morechathistory";
        public static final String parachute = "parachute";
        public static final String raise_chat_limit = "raise-chat-limit";
        public static final String wheres_my_chat_history = "wmch";
        public static final String carpet_tis_addition = "carpet-tis-addition";
        public static final String caxton = "caxton";
        public static final String custom_skin_loader = "customskinloader";
        public static final String easier_crafting = "easiercrafting";
        public static final String extra_player_renderer = "explayerenderer";
        public static final String fabric_carpet = "carpet";
        public static final String iris = "iris";
        public static final String locked_window_size = "locked_window_size";
        public static final String optifine = "optifabric";
        public static final String pistorder = "pistorder";
        public static final String replay_mod = "replaymod";
        public static final String sodium = "sodium";
        public static final String xaero_betterpvp = "xaerobetterpvp";
        public static final String xaero_minimap = "xaerominimap";
        public static final String xaero_worldmap = "xaeroworldmap";
    }
}
