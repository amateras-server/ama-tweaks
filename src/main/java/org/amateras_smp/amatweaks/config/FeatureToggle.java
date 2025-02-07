// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.config.IConfigNotifiable;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyCallbackToggleBooleanConfigWithMessage;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;
import fi.dy.masa.malilib.util.StringUtils;
import org.amateras_smp.amatweaks.AmaTweaks;
import org.amateras_smp.amatweaks.Reference;

public enum FeatureToggle implements IHotkeyTogglable, IConfigNotifiable<IConfigBoolean> {
    TWEAK_AUTO_EAT("tweakAutoEat", false, "", "Automatically eats food from your inventory when your food level is not full."),
    TWEAK_AUTO_FIREWORK_GLIDE("tweakAutoFireworkGlide", false, "", "Use fireworks rocket automatically while elytra gliding."),
    TWEAK_AUTO_RESTOCK_INVENTORY("tweakAutoRestockInventory", false, "", "Restock a stack of items specified by inventoryRestockList when open a container."),
    TWEAK_COMPACT_SCOREBOARD("tweakCompactScoreboard", false, "", "Displays compact values in scoreboard like : 12345 -> 1.2K, 98765432 -> 9.87M."),
    TWEAK_HOLD_BACK("tweakHoldBack", false, "", "Hold moving back."),
    TWEAK_HOLD_FORWARD("tweakHoldForward", false, "", "Hold moving forward."),
    TWEAK_HOLD_LEFT("tweakHoldLeft", false, "", "Hold moving left."),
    TWEAK_HOLD_RIGHT("tweakHoldRight", false, "", "Hold moving right."),
    TWEAK_INTERACTION_HISTORY("tweakInteractionHistory", false, "", "Remember some interactions to break blocks, use items, attack entities. Can check the history by typing /history command."),
    TWEAK_MONO_GUI("tweakMonoGui", false, "", "Render monochrome gui. including tweakMonoChat, tweakMonoTeamColor."),
    TWEAK_MONO_TEAM_COLOR("tweakMonoTeamColor", false, "", "Render monochrome team color."),
    TWEAK_PICK_BLOCK_REDIRECT("tweakPickBlockRedirect", false, "", "Automatically replaces blocks that should be picked by litematica or tweakermore."),
    TWEAK_PREVENT_BREAKING_ADJACENT_PORTAL("tweakPreventBreakingAdjacentPortal", false, "", "Prevents breaking blocks adjacent to nether portal block."),
    TWEAK_PREVENT_PLACEMENT_ON_PORTAL_SIDES("tweakPreventPlacementOnPortalSides", false, "", "Disables placement on sliced nether portal sides."),
    TWEAK_SAFE_STEP_PROTECTION("tweakSafeStepProtection",false, "","Restrict Breaking Blocks below you when you're moving forward."),
    TWEAK_SELECTIVE_AUTO_PICK("tweakSelectiveAutoPick", false, "", "Adds whitelist/blacklist for autoPickSchematicBlock(in tweakermore)."),
    TWEAK_SELECTIVE_BLOCK_RENDERING("tweakSelectiveBlockRendering", false, "", "Renders blocks in accordance with custom selected list."),
    TWEAK_SELECTIVE_ENTITY_RENDERING("tweakSelectiveEntityRendering", false, "", "Renders entities in accordance with custom selected list."),
    TWEAK_SELECTIVE_TOOL_SWITCH("tweakSelectiveToolSwitch", false, "", "Adds whitelist/blacklist for tweakToolSwitch(in tweakeroo).");


    public static final ImmutableList<FeatureToggle> VALUES = ImmutableList.copyOf(values());

    private final String name;
    private String comment;
    private String prettyName;
    private String translatedName;
    private final IKeybind keybind;
    private final boolean defaultValueBoolean;
    private final boolean singlePlayer;
    private boolean valueBoolean;
    private IValueChangeCallback<IConfigBoolean> callback;

    FeatureToggle(String name, boolean defaultValue, String defaultHotkey)
    {
        this(name, defaultValue, false, defaultHotkey, KeybindSettings.DEFAULT,
                buildTranslateName(name, "comment"),
                buildTranslateName(name, "prettyName"),
                buildTranslateName(name, "name"));
    }

    FeatureToggle(String name, boolean defaultValue, String defaultHotkey, KeybindSettings settings)
    {
        this(name, defaultValue, false, defaultHotkey, settings,
                buildTranslateName(name, "comment"),
                buildTranslateName(name, "prettyName"),
                buildTranslateName(name, "name"));
    }

    FeatureToggle(String name, boolean defaultValue, boolean singlePlayer, String defaultHotkey)
    {
        this(name, defaultValue, singlePlayer, defaultHotkey, KeybindSettings.DEFAULT,
                buildTranslateName(name, "comment"),
                buildTranslateName(name, "prettyName"),
                buildTranslateName(name, "name"));
    }

    FeatureToggle(String name, boolean defaultValue, String defaultHotkey, String comment, String prettyName, String translatedName)
    {
        this(name, defaultValue, false, defaultHotkey,
                comment,
                prettyName,
                translatedName);
    }

    FeatureToggle(String name, boolean defaultValue, boolean singlePlayer, String defaultHotkey, String comment, String prettyName, String translatedName)
    {
        this(name, defaultValue, singlePlayer, defaultHotkey, KeybindSettings.DEFAULT,
                comment,
                prettyName,
                translatedName);
    }

    // Backwards Compatible constructors - START
    FeatureToggle(String name, boolean defaultValue, String defaultHotkey, String comment)
    {
        this(name, defaultValue, false, defaultHotkey, KeybindSettings.DEFAULT,
                comment,
                buildTranslateName(name, "prettyName"),
                buildTranslateName(name, "name"));
    }

    FeatureToggle(String name, boolean defaultValue, boolean singlePlayer, String defaultHotkey, String comment)
    {
        this(name, defaultValue, singlePlayer, defaultHotkey, KeybindSettings.DEFAULT,
                comment,
                buildTranslateName(name, "prettyName"),
                buildTranslateName(name, "name"));
    }

    FeatureToggle(String name, boolean defaultValue, String defaultHotkey, KeybindSettings settings, String comment)
    {
        this(name, defaultValue, false, defaultHotkey, settings,
                comment,
                buildTranslateName(name, "prettyName"),
                buildTranslateName(name, "name"));
    }

    FeatureToggle(String name, boolean defaultValue, boolean singlePlayer, String defaultHotkey, KeybindSettings settings, String comment)
    {
        this(name, defaultValue, singlePlayer, defaultHotkey, settings,
                comment,
                buildTranslateName(name, "prettyName"),
                buildTranslateName(name, "name"));
    }

    FeatureToggle(String name, boolean defaultValue, String defaultHotkey, String comment, String prettyName)
    {
        this(name, defaultValue, false, defaultHotkey,
                comment,
                prettyName,
                buildTranslateName(name, "name"));
    }

    FeatureToggle(String name, boolean defaultValue, boolean singlePlayer, String defaultHotkey, String comment, String prettyName)
    {
        this(name, defaultValue, singlePlayer, defaultHotkey, KeybindSettings.DEFAULT,
                comment,
                prettyName,
                buildTranslateName(name, "name"));
    }

    FeatureToggle(String name, boolean defaultValue, boolean singlePlayer, String defaultHotkey, KeybindSettings settings, String comment, String prettyName)
    {
        this(name, defaultValue, singlePlayer, defaultHotkey, settings,
                comment,
                prettyName,
                buildTranslateName(name, "name"));
    }
    // Backwards Compatible constructors - END

    FeatureToggle(String name, boolean defaultValue, boolean singlePlayer, String defaultHotkey, KeybindSettings settings, String comment, String prettyName, String translatedName)
    {
        this.name = name;
        this.valueBoolean = defaultValue;
        this.defaultValueBoolean = defaultValue;
        this.singlePlayer = singlePlayer;
        this.comment = comment;
        this.prettyName = prettyName;
        this.translatedName = translatedName;
        this.keybind = KeybindMulti.fromStorageString(defaultHotkey, settings);
        this.keybind.setCallback(new KeyCallbackToggleBooleanConfigWithMessage(this));
    }

    @Override
    public ConfigType getType() {
        return ConfigType.HOTKEY;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getConfigGuiDisplayName() {
        String name = StringUtils.getTranslatedOrFallback("config.name." + this.getName().toLowerCase(), this.getName());

        if (this.singlePlayer)
        {
            return GuiBase.TXT_GOLD + name + GuiBase.TXT_RST;
        }

        return name;
    }

    @Override
    public String getPrettyName() {
        return StringUtils.getTranslatedOrFallback(this.prettyName,
                !this.prettyName.isEmpty() ? this.prettyName : StringUtils.splitCamelCase(this.name.substring(5)));
    }

    @Override
    public String getComment() {
        String comment = StringUtils.getTranslatedOrFallback("config.comment." + this.getName().toLowerCase(), this.comment);

        if (comment != null && this.singlePlayer) {
            return comment + "\n" + StringUtils.translate(Reference.kSnakeCaseModName + ".label.config_comment.single_player_only");
        }

        return comment;
    }

    private static String buildTranslateName(String name, String type) {
        // return FEATURE_KEY + "." + type + "." + name;
        return name;
    }

    //#if MC >= 12104
    //$$ @Override
    //#endif
    public String getTranslatedName() {
        String name = StringUtils.getTranslatedOrFallback(this.translatedName, this.name);
    
        if (this.singlePlayer) {
            name = GuiBase.TXT_GOLD + name + GuiBase.TXT_RST;
        }
    
        return name;
    }
    
    //#if MC >= 12104
    //$$ @Override
    //#endif
    public void setPrettyName(String s) {
       this.prettyName = s;
    }
    
    //#if MC >= 12104
    //$$ @Override
    //#endif
    public void setTranslatedName(String s) {
       this.translatedName = s;
    }
    
    //#if MC >= 12104
    //$$ @Override
    //#endif
    public void setComment(String s) {
       this.comment = s;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(this.valueBoolean);
    }

    @Override
    public String getDefaultStringValue() {
        return String.valueOf(this.defaultValueBoolean);
    }

    @Override
    public void setValueFromString(String value) {
    }

    @Override
    public void onValueChanged() {
        if (this.callback != null) {
            this.callback.onValueChanged(this);
        }
    }

    @Override
    public void setValueChangeCallback(IValueChangeCallback<IConfigBoolean> callback) {
        this.callback = callback;
    }

    @Override
    public IKeybind getKeybind() {
        return this.keybind;
    }

    @Override
    public boolean getBooleanValue() {
        return this.valueBoolean;
    }

    @Override
    public boolean getDefaultBooleanValue() {
        return this.defaultValueBoolean;
    }

    @Override
    public void setBooleanValue(boolean value) {
        boolean oldValue = this.valueBoolean;
        this.valueBoolean = value;

        if (oldValue != this.valueBoolean) {
            this.onValueChanged();
        }
    }

    @Override
    public boolean isModified() {
        return this.valueBoolean != this.defaultValueBoolean;
    }

    @Override
    public boolean isModified(String newValue) {
        return Boolean.parseBoolean(newValue) != this.defaultValueBoolean;
    }

    @Override
    public void resetToDefault() {
        this.valueBoolean = this.defaultValueBoolean;
    }

    @Override
    public JsonElement getAsJsonElement() {
        return new JsonPrimitive(this.valueBoolean);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonPrimitive()) {
                this.valueBoolean = element.getAsBoolean();
            }
            else {
                AmaTweaks.LOGGER.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element);
            }
        }
        catch (Exception e) {
            AmaTweaks.LOGGER.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element, e);
        }
    }
}
