// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.selectiverendering;

import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    private void onRenderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if(!FeatureToggle.TWEAK_SELECTIVE_ENTITY_RENDERING.getBooleanValue()) return;

        UsageRestriction.ListType type = (UsageRestriction.ListType) Configs.Lists.SELECTIVE_ENTITY_RENDERING_LIST_TYPE.getOptionListValue();
        if(type == UsageRestriction.ListType.NONE) return;

        String targetEntity = (entity.getType().toString()).replace("entity.minecraft.", "");

        if(type == UsageRestriction.ListType.BLACKLIST) {
            if(Configs.Lists.SELECTIVE_ENTITY_RENDERING_BLACKLIST.getStrings().contains(targetEntity)) {
                ci.cancel();
            }
        } else if (type == UsageRestriction.ListType.WHITELIST) {
            if(!Configs.Lists.SELECTIVE_ENTITY_RENDERING_WHITELIST.getStrings().contains(targetEntity)) {
                ci.cancel();
            }
        }
    }
}
