// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.autorestockinventory;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.gui.screen.Screen;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.impl.util.container.AutoProcessableScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Restriction(require = @Condition(Reference.ModIds.itemscroller))
@Mixin(Screen.class)
public abstract class ScreenMixin implements AutoProcessableScreen
{
    @Unique
    private boolean shouldProcessFlag = false;

    @Override
    public void setShouldProcess$AMT(boolean value)
    {
        this.shouldProcessFlag = value;
    }

    @Override
    public boolean shouldProcess$AMT()
    {
        return this.shouldProcessFlag;
    }
}
