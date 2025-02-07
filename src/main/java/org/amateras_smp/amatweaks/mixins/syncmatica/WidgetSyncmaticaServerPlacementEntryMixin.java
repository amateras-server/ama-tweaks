// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.syncmatica;

import ch.endte.syncmatica.ServerPlacement;
import ch.endte.syncmatica.litematica.gui.WidgetSyncmaticaServerPlacementEntry;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.impl.addon.syncmatica.EnhancedRemoveButton;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Restriction(require = @Condition(Reference.ModIds.syncmatica))
@Mixin(value = WidgetSyncmaticaServerPlacementEntry.class, remap = false)
public class WidgetSyncmaticaServerPlacementEntryMixin {
    @Shadow @Final
    private ServerPlacement placement;

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lch/endte/syncmatica/litematica/gui/WidgetSyncmaticaServerPlacementEntry;addButton(Lfi/dy/masa/malilib/gui/button/ButtonBase;Lfi/dy/masa/malilib/gui/button/IButtonActionListener;)Lfi/dy/masa/malilib/gui/button/ButtonBase;", ordinal = 0), index = 1)
    private IButtonActionListener onInitRemoveButton(IButtonActionListener par2) {
        return new EnhancedRemoveButton.ButtonListener((WidgetSyncmaticaServerPlacementEntry)(Object)this, placement.getId());
    }
}
