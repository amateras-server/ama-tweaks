// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.restrictions.ItemRestriction;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import org.amateras_smp.amatweaks.InitHandler;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.impl.addon.tweakermore.SelectiveAutoPick;
import org.amateras_smp.amatweaks.impl.addon.tweakeroo.SelectiveToolSwitch;
import org.amateras_smp.amatweaks.impl.features.InteractionHistory;
import org.amateras_smp.amatweaks.impl.features.PreventBreakingAdjacentPortal;
import org.amateras_smp.amatweaks.impl.features.SelectiveRendering;

import java.io.File;

public class Configs implements IConfigHandler {
    private static final String CONFIG_FILE_NAME = Reference.kModId + ".json";

    public static class Generic {
        public static final ConfigBoolean AUTO_EAT_PUT_BACK_FOOD = new ConfigBoolean("autoEatPutBackFood", false, "\"tweakAutoEat\" puts back the food to the slot where it was.");
        public static final ConfigDouble AUTO_EAT_THRESHOLD = new ConfigDouble("autoEatThreshold", 1.0, 0, 1.0, "The hunger level threshold for \"tweakAutoEat\".");
        public static final ConfigInteger AUTO_FIREWORK_USE_INTERVAL = new ConfigInteger("autoFireworkUseInterval", 60, 1, 1000, "The interval game tick for automatically try to use firework rockets with \"tweakAutoFireworkGlide\".");
        public static final ConfigBoolean AUTO_GLIDE_PUT_BACK_ROCKET = new ConfigBoolean("autoGlidePutBackRocket", true, "\"tweakAutoFireworkGlide\" puts back the firework rocket to the slot where it was.");
        public static final ConfigDouble AUTO_GLIDE_SPEED_THRESHOLD = new ConfigDouble("autoGlideSpeedThreshold", 15.0, 0, 1000, "The speed threshold for \"tweakAutoFireworkGlide\" to use firework rockets.");
        public static final ConfigBoolean CANCEL_AUTO_EAT_WHILE_DOING_ACTION = new ConfigBoolean("cancelAutoEatWhileDoingAction", false, "\"tweakAutoEat\" will not be triggered while using or attacking.");
        public static final ConfigBoolean ENABLE_DEBUG_PRINTS = new ConfigBoolean("enableDebugPrints", false, "Enables debug prints for ama-tweaks developer.");
        public static final ConfigInteger FIREWORK_SWITCHABLE_SLOT = new ConfigInteger ("fireworkSwitchableSlot", 0, 0, 8, "The slot to switch firework rocket by \"tweakAutoFireworkGlide\". starts from 0.");
        public static final ConfigInteger FOOD_SWITCHABLE_SLOT = new ConfigInteger ("foodSwitchableSlot", 0, 0, 8, "The slot to switch food by \"tweakAutoEat\". starts from 0.");
        public static final ConfigBoolean GLIDING_AUTO_EAT_DISABLED = new ConfigBoolean("glidingAutoEatDisabled", false, "Disables \"tweakAutoEat\" when you're gliding with elytra.");
        public static final ConfigInteger INTERACTION_HISTORY_MAX_SIZE = new ConfigInteger("interactionHistoryMaxSize", 10, 10, 1000, "The number of interactions to keep by \"tweakInteractionHistory\".");
        public static final ConfigBoolean INVENTORY_RESTOCK_ONLY_ALLOW_SHULKER_BOX = new ConfigBoolean("inventoryRestockOnlyAllowShulkerBox", false, "\"tweaksAutoRestockInventory\" will be only triggered when you open shulker boxes");
        public static final ConfigBoolean ON_AUTO_RESTOCK_CLOSE_GUI = new ConfigBoolean("onAutoRestockCloseGui", true, "Closes container GUI screen on \"tweakAutoRestockInventory\" restocks.");
        public static final ConfigBoolean PERSISTENT_GAMMA_OVERRIDE = new ConfigBoolean("persistentGammaOverride", false, "Fixes a bug of tweakeroo that \"tweakGammaOverride\" will not be enabled on client restart.");
        public static final ConfigBoolean REFRESH_PREFILTERED_POST_AUTO_COLLECT_MATERIAL = new ConfigBoolean("refreshPrefilteredPostAutoCollectMaterial", false, "Refreshes pre-filtered material list at the end of \"autoCollectMaterial\" by tweakermore.");
        public static final ConfigBoolean REFRESH_WORLD_RENDERER_ON_RENDER_BLOCKS_CHANGED = new ConfigBoolean("refreshWorldRendererOnRenderBlocksChanged", true, "Refreshes client world renderer when \"tweakSelectiveBlockRendering\" settings(contains Lists, FeatureToggle) changed.");
        public static final ConfigBoolean SYNCMATICA_REMOVE_REQUIRE_SHIFT = new ConfigBoolean("syncmaticaRemoveRequireShift", false, "Requires shift to remove shared schematic from the server");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                AUTO_EAT_PUT_BACK_FOOD,
                AUTO_EAT_THRESHOLD,
                AUTO_FIREWORK_USE_INTERVAL,
                AUTO_GLIDE_PUT_BACK_ROCKET,
                AUTO_GLIDE_SPEED_THRESHOLD,
                CANCEL_AUTO_EAT_WHILE_DOING_ACTION,
                ENABLE_DEBUG_PRINTS,
                FIREWORK_SWITCHABLE_SLOT,
                FOOD_SWITCHABLE_SLOT,
                GLIDING_AUTO_EAT_DISABLED,
                INTERACTION_HISTORY_MAX_SIZE,
                INVENTORY_RESTOCK_ONLY_ALLOW_SHULKER_BOX,
                ON_AUTO_RESTOCK_CLOSE_GUI,
                PERSISTENT_GAMMA_OVERRIDE,
                REFRESH_PREFILTERED_POST_AUTO_COLLECT_MATERIAL,
                REFRESH_WORLD_RENDERER_ON_RENDER_BLOCKS_CHANGED,
                SYNCMATICA_REMOVE_REQUIRE_SHIFT
        );
    }

    public static class Lists {
        public static final ConfigStringList INVENTORY_RESTOCK_LIST = new ConfigStringList("inventoryRestockList", ImmutableList.of("minecraft:firework_rocket", "minecraft:golden_carrot", "minecraft:experience_bottle"), "The items to restock with tweakAutoRestockHotbar.");
        public static final ItemRestriction INVENTORY_RESTOCK_ITEMS = new ItemRestriction();

        public static final ConfigStringList PICK_REDIRECT_MAP = new ConfigStringList("pickRedirectMap", ImmutableList.of("minecraft:farmland, minecraft:dirt", "minecraft:dirt_path, minecraft:dirt", "minecraft:water, minecraft:ice"), "replacement reference of litematica block pick");

        public static final ConfigOptionList PORTAL_BREAKING_RESTRICTION_LIST_TYPE = new ConfigOptionList("portalBreakingRestrictionListType", UsageRestriction.ListType.WHITELIST, "The type of the list used for \"tweakPreventBreakingAdjacentPortal\" restriction effects.");
        public static final ConfigStringList PORTAL_BREAKING_RESTRICTION_BLACKLIST = new ConfigStringList("portalBreakingRestrictionBlackList", ImmutableList.of(""), "The items that will be restricted by \"tweakPreventBreakingAdjacentPortal\".");
        public static final ConfigStringList PORTAL_BREAKING_RESTRICTION_WHITELIST = new ConfigStringList("portalBreakingRestrictionWhiteList", ImmutableList.of("minecraft:obsidian"), "The items that will not be restricted by \"tweakPreventBreakingAdjacentPortal\".");

        public static final ConfigOptionList SELECTIVE_AUTO_PICK_LIST_TYPE = new ConfigOptionList("selectiveAutoPickListType", UsageRestriction.ListType.NONE, "The type of the list used for selective auto pick.");
        public static final ConfigStringList SELECTIVE_AUTO_PICK_WHITELIST = new ConfigStringList("selectiveAutoPickWhiteList", ImmutableList.of(), "The items when it is in hand auto pick will work.");
        public static final ConfigStringList SELECTIVE_AUTO_PICK_BLACKLIST = new ConfigStringList("selectiveAutoPickBlackList", ImmutableList.of("minecraft:golden_carrot", "minecraft:ender_chest", "minecraft:shulker_box", "minecraft:totem_of_undying"), "The items when it is in hand auto pick will not work.");

        public static final ConfigOptionList SELECTIVE_BLOCK_RENDERING_LIST_TYPE = new ConfigOptionList("selectiveBlockRenderingListType", UsageRestriction.ListType.NONE, "The type of the list used for selective block rendering.");
        public static final ConfigStringList SELECTIVE_BLOCK_RENDERING_WHITELIST = new ConfigStringList("selectiveBlockRenderingWhiteList", ImmutableList.of(), "The blocks that will be rendered.");
        public static final ConfigStringList SELECTIVE_BLOCK_RENDERING_BLACKLIST = new ConfigStringList("selectiveBlockRenderingBlackList", ImmutableList.of(), "The blocks that will not be rendered.");

        public static final ConfigOptionList SELECTIVE_ENTITY_RENDERING_LIST_TYPE = new ConfigOptionList("selectiveEntityRenderingListType", UsageRestriction.ListType.NONE, "The type of the list used for selective entity rendering.");
        public static final ConfigStringList SELECTIVE_ENTITY_RENDERING_WHITELIST = new ConfigStringList("selectiveEntityRenderingWhiteList", ImmutableList.of(), "The entities that will be rendered.");
        public static final ConfigStringList SELECTIVE_ENTITY_RENDERING_BLACKLIST = new ConfigStringList("selectiveEntityRenderingBlackList", ImmutableList.of(), "The entities that will not be rendered.");

        public static final ConfigOptionList SELECTIVE_TOOL_SWITCH_LIST_TYPE = new ConfigOptionList("selectiveToolSwitchListType", UsageRestriction.ListType.NONE, "The type of the list used for selective tool switch.");
        public static final ConfigStringList SELECTIVE_TOOL_SWITCH_WHITELIST = new ConfigStringList("selectiveToolSwitchWhiteList", ImmutableList.of(), "The blocks that tweakToolSwitch will work on break.");
        public static final ConfigStringList SELECTIVE_TOOL_SWITCH_BLACKLIST = new ConfigStringList("selectiveToolSwitchBlackList", ImmutableList.of(), "The blocks that tweakToolSwitch will not work on break.");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                INVENTORY_RESTOCK_LIST,
                PICK_REDIRECT_MAP,
                PORTAL_BREAKING_RESTRICTION_LIST_TYPE,
                PORTAL_BREAKING_RESTRICTION_BLACKLIST,
                PORTAL_BREAKING_RESTRICTION_WHITELIST,
                SELECTIVE_AUTO_PICK_LIST_TYPE,
                SELECTIVE_AUTO_PICK_WHITELIST,
                SELECTIVE_AUTO_PICK_BLACKLIST,
                SELECTIVE_BLOCK_RENDERING_LIST_TYPE,
                SELECTIVE_BLOCK_RENDERING_WHITELIST,
                SELECTIVE_BLOCK_RENDERING_BLACKLIST,
                SELECTIVE_ENTITY_RENDERING_LIST_TYPE,
                SELECTIVE_ENTITY_RENDERING_WHITELIST,
                SELECTIVE_ENTITY_RENDERING_BLACKLIST,
                SELECTIVE_TOOL_SWITCH_LIST_TYPE,
                SELECTIVE_TOOL_SWITCH_WHITELIST,
                SELECTIVE_TOOL_SWITCH_BLACKLIST
        );
    }

    public static class Disable {
        public static final ConfigBooleanHotkeyed DISABLE_SYNCMATICA_REMOVE_BUTTON = new ConfigBooleanHotkeyed("disableSyncmaticaRemoveButton", false, "", "Disables the schematic remove button from syncmatica GUI.");

        public static final ImmutableList<IHotkeyTogglable> OPTIONS = ImmutableList.of(
                DISABLE_SYNCMATICA_REMOVE_BUTTON
        );
    }

    public static void onConfigLoaded() {
        Lists.INVENTORY_RESTOCK_ITEMS.setListContents(ImmutableList.of(""), Configs.Lists.INVENTORY_RESTOCK_LIST.getStrings());
        InitHandler.initLogLevel(Generic.ENABLE_DEBUG_PRINTS.getBooleanValue());

        InteractionHistory.resize();

        PreventBreakingAdjacentPortal.buildLists();

        SelectiveAutoPick.buildLists();
        SelectiveToolSwitch.buildLists();
        SelectiveRendering.buildLists();
        SelectiveRendering.applyConfig();
    }

    public static void loadFromFile() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
                // ConfigUtils.readConfigBase(root, "Fixes", Fixes.OPTIONS);
                ConfigUtils.readConfigBase(root, "Lists", Lists.OPTIONS);
                ConfigUtils.readHotkeys(root, "GenericHotkeys", Hotkeys.HOTKEY_LIST);
                ConfigUtils.readHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);
                ConfigUtils.readConfigBase(root, "Disables", Disable.OPTIONS);
            }
        }

        onConfigLoaded();
    }

    public static void saveToFile() {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Configs.Generic.OPTIONS);
            // ConfigUtils.writeConfigBase(root, "Fixes", Configs.Fixes.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Lists", Configs.Lists.OPTIONS);
            ConfigUtils.writeHotkeys(root, "GenericHotkeys", Hotkeys.HOTKEY_LIST);
            ConfigUtils.writeHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);
            ConfigUtils.writeConfigBase(root, "Disables", Disable.OPTIONS);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveToFile();
    }
}
