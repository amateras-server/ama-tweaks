package org.amateras_smp.amatweaks.mixins.features.selectiverendering;

//#if MC >= 12110
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void onRenderEntity(E entity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> cir) {
        if (!FeatureToggle.TWEAK_SELECTIVE_ENTITY_RENDERING.getBooleanValue()) return;
        UsageRestriction.ListType type = (UsageRestriction.ListType) Configs.Lists.SELECTIVE_ENTITY_RENDERING_LIST_TYPE.getOptionListValue();
        if (type == UsageRestriction.ListType.NONE) return;

        String targetEntity = EntityType.getKey(entity.getType()).toString();

        if (type == UsageRestriction.ListType.BLACKLIST) {
            if(Configs.Lists.SELECTIVE_ENTITY_RENDERING_BLACKLIST.getStrings().contains(targetEntity)) {
                cir.setReturnValue(false);
            }
        } else if (type == UsageRestriction.ListType.WHITELIST) {
            if(!Configs.Lists.SELECTIVE_ENTITY_RENDERING_WHITELIST.getStrings().contains(targetEntity)) {
                cir.setReturnValue(false);
            }
        }
    }
}
//#endif
