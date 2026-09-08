// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.gui;

import com.google.common.collect.ImmutableList;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.config.Hotkeys;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.BooleanHotkeyGuiWrapper;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

//#if MC >= 12111
import fi.dy.masa.malilib.gui.interfaces.IConfigGuiAllTab;
//#endif

public class GuiConfigs extends GuiConfigsBase
    //#if MC >= 12111
    implements IConfigGuiAllTab
    //#endif
{

    public static ImmutableList<FeatureToggle> TWEAK_LIST = FeatureToggle.VALUES;

    private static ConfigGuiTab tab = ConfigGuiTab.ALL;

    public GuiConfigs() {
        super(10, 50, Reference.kModId, null, Reference.kModName + " %s", String.format("%s", Reference.kModVersion));
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;

        for (ConfigGuiTab tab : ConfigGuiTab.values()) {
            if (!this.useAllTab() && tab == ConfigGuiTab.ALL) continue;
            x += this.createButton(x, y, -1, tab);
        }
    }

    private int createButton(int x, int y, int width, ConfigGuiTab tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        button.setEnabled(GuiConfigs.tab != tab);
        this.addButton(button, new ButtonListener(tab, this));

        return button.getWidth() + 2;
    }

    @Override
    protected int getConfigWidth() {
        ConfigGuiTab tab = GuiConfigs.tab;

        if (tab == ConfigGuiTab.GENERIC) {
            return 120;
        // } else if (tab == ConfigGuiTab.FIXES) {
        //     return 60;
        } else if (tab == ConfigGuiTab.LISTS) {
            return 200;
        }

        return 260;
    }

    @Override
    protected boolean useKeybindSearch() {
        return GuiConfigs.tab == ConfigGuiTab.ALL ||
            GuiConfigs.tab == ConfigGuiTab.TWEAKS ||
            GuiConfigs.tab == ConfigGuiTab.GENERIC_HOTKEYS ||
            GuiConfigs.tab == ConfigGuiTab.DISABLES;
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<? extends IConfigBase> configs;
        ConfigGuiTab tab = GuiConfigs.tab;

        if (tab == ConfigGuiTab.ALL) {
            return this.getAllConfigs();
        } else if (tab == ConfigGuiTab.GENERIC) {
            configs = Configs.Generic.OPTIONS;
        } else if (tab == ConfigGuiTab.LISTS) {
            configs = Configs.Lists.OPTIONS;
        } else if (tab == ConfigGuiTab.TWEAKS) {
            return ConfigOptionWrapper.createFor(TWEAK_LIST.stream().map(this::wrapConfig).toList());
        } else if (tab == ConfigGuiTab.GENERIC_HOTKEYS) {
            configs = Hotkeys.HOTKEY_LIST;
        } else if (tab == ConfigGuiTab.DISABLES) {
            configs = Configs.Disable.OPTIONS;
        } else {
            return Collections.emptyList();
        }

        return ConfigOptionWrapper.createFor(configs);
    }

    //#if MC >= 12111
    @Override
    //#endif
    public boolean useAllTab() {
        return true;
    }

    //#if MC >= 12111
    @Override
    //#endif
    public List<ConfigOptionWrapper> getAllConfigs() {
        List<ConfigOptionWrapper> configs = new ArrayList<>();

        configs.addAll(ConfigOptionWrapper.createFor(Configs.Generic.OPTIONS));
        configs.addAll(ConfigOptionWrapper.createFor(Configs.Lists.OPTIONS));
        configs.addAll(ConfigOptionWrapper.createFor(TWEAK_LIST.stream().map(this::wrapConfig).toList()));
        configs.addAll(ConfigOptionWrapper.createFor(Hotkeys.HOTKEY_LIST));
        configs.addAll(ConfigOptionWrapper.createFor(Configs.Disable.OPTIONS));

        return configs;
    }

    protected BooleanHotkeyGuiWrapper wrapConfig(FeatureToggle config) {
        return new BooleanHotkeyGuiWrapper(config.getName(), config, config.getKeybind());
    }

    private record ButtonListener(ConfigGuiTab tab,
                                  GuiConfigs parent) implements IButtonActionListener {

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            GuiConfigs.tab = this.tab;
            this.parent.reCreateListWidget(); // apply the new config width
            Objects.requireNonNull(this.parent.getListWidget()).resetScrollbarPosition();
            this.parent.initGui();
        }
    }

    public enum ConfigGuiTab {
        ALL("All"),
        GENERIC("Generic"),
        // FIXES("Fixes"),
        LISTS("Lists"),
        TWEAKS("Tweaks"),
        GENERIC_HOTKEYS("Hotkeys"),
        DISABLES("Yeets");

        private final String translationKey;

        ConfigGuiTab(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getDisplayName() {
            return StringUtils.translate(this.translationKey);
        }
    }
}
