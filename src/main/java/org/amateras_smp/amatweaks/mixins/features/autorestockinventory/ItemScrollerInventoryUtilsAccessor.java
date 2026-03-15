package org.amateras_smp.amatweaks.mixins.features.autorestockinventory;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.inventory.Slot;
import org.amateras_smp.amatweaks.Reference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Restriction(require = {@Condition(Reference.ModIds.itemscroller)})
@Mixin({InventoryUtils.class})
public interface ItemScrollerInventoryUtilsAccessor {
    @Invoker(
        value = "areSlotsInSameInventory",
        remap = false
    )
    static boolean areSlotsInSameInventory(Slot slot1, Slot slot2) {
        return false;
    }
}
