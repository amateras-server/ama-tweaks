// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.selectiverendering;

//#if MC < 12110
//$$ import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
//$$ import net.minecraft.client.renderer.MultiBufferSource;
//$$ import net.minecraft.client.renderer.LevelRenderer;
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import net.minecraft.world.entity.Entity;
//$$ import net.minecraft.world.entity.EntityType;
//$$ import org.amateras_smp.amatweaks.config.Configs;
//$$ import org.amateras_smp.amatweaks.config.FeatureToggle;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ @Mixin(LevelRenderer.class)
//$$ public class LevelRendererMixin {
//$$     @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
//$$     private void onRenderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
//$$         if (!FeatureToggle.TWEAK_SELECTIVE_ENTITY_RENDERING.getBooleanValue()) return;
//$$
//$$         UsageRestriction.ListType type = (UsageRestriction.ListType) Configs.Lists.SELECTIVE_ENTITY_RENDERING_LIST_TYPE.getOptionListValue();
//$$         if (type == UsageRestriction.ListType.NONE) return;
//$$
//$$         String targetEntity = EntityType.getKey(entity.getType()).toString();
//$$
//$$         if (type == UsageRestriction.ListType.BLACKLIST) {
//$$             if(Configs.Lists.SELECTIVE_ENTITY_RENDERING_BLACKLIST.getStrings().contains(targetEntity)) {
//$$                 ci.cancel();
//$$             }
//$$         } else if (type == UsageRestriction.ListType.WHITELIST) {
//$$             if(!Configs.Lists.SELECTIVE_ENTITY_RENDERING_WHITELIST.getStrings().contains(targetEntity)) {
//$$                 ci.cancel();
//$$             }
//$$         }
//$$     }
//$$ }
//#endif
