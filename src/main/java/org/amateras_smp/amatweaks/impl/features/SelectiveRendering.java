// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.restrictions.BlockRestriction;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.client.MinecraftClient;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;

import java.util.Objects;

public class SelectiveRendering {
    public static BlockRestriction BLOCKS_LIST = new BlockRestriction();

    private static boolean isEnabled;
    private static UsageRestriction.ListType listType;
    private static String blackListString;
    private static String whiteListString;


    private static boolean isChanged() {
        return  isEnabled != FeatureToggle.TWEAK_SELECTIVE_BLOCK_RENDERING.getBooleanValue() ||
                listType != Configs.Lists.SELECTIVE_BLOCK_RENDERING_LIST_TYPE.getOptionListValue() ||
                !Objects.equals(blackListString, Configs.Lists.SELECTIVE_BLOCK_RENDERING_BLACKLIST.getStrings().toString()) ||
                !Objects.equals(whiteListString, Configs.Lists.SELECTIVE_BLOCK_RENDERING_WHITELIST.getStrings().toString());
    }

    public static void applyConfig() {
        isEnabled = FeatureToggle.TWEAK_SELECTIVE_BLOCK_RENDERING.getBooleanValue();
        listType = (UsageRestriction.ListType) Configs.Lists.SELECTIVE_BLOCK_RENDERING_LIST_TYPE.getOptionListValue();
        blackListString = Configs.Lists.SELECTIVE_BLOCK_RENDERING_BLACKLIST.getStrings().toString();
        whiteListString = Configs.Lists.SELECTIVE_BLOCK_RENDERING_WHITELIST.getStrings().toString();
    }

    public static void buildLists() {
        if (isChanged()) {
            BLOCKS_LIST.setListType((UsageRestriction.ListType) Configs.Lists.SELECTIVE_BLOCK_RENDERING_LIST_TYPE.getOptionListValue());
            BLOCKS_LIST.setListContents(
                    Configs.Lists.SELECTIVE_BLOCK_RENDERING_BLACKLIST.getStrings(),
                    Configs.Lists.SELECTIVE_BLOCK_RENDERING_WHITELIST.getStrings()
            );

            // reloads all the world rendering so that the settings change will be applied.
            if (Configs.Generic.REFRESH_WORLD_RENDERER_ON_RENDER_BLOCKS_CHANGED.getBooleanValue()) {
                MinecraftClient.getInstance().worldRenderer.reload();
            }
            applyConfig();
        }
    }
}
