// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.addon.tweakeroo;

import fi.dy.masa.malilib.util.restrictions.BlockRestriction;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.block.BlockState;
import org.amateras_smp.amatweaks.config.Configs;

public class SelectiveToolSwitch {
    public static final BlockRestriction TOOL_RESTRICTION = new BlockRestriction();

    public static void buildLists() {
        TOOL_RESTRICTION.setListType((UsageRestriction.ListType) Configs.Lists.SELECTIVE_TOOL_SWITCH_LIST_TYPE.getOptionListValue());
        TOOL_RESTRICTION.setListContents(
                Configs.Lists.SELECTIVE_TOOL_SWITCH_BLACKLIST.getStrings(),
                Configs.Lists.SELECTIVE_TOOL_SWITCH_WHITELIST.getStrings());
    }

    public static boolean restrict(BlockState state) {
        return !TOOL_RESTRICTION.isAllowed(state.getBlock());
    }
}
