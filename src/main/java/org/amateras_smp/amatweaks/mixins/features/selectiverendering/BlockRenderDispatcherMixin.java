package org.amateras_smp.amatweaks.mixins.features.selectiverendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.SelectiveRendering;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderDispatcher.class)
public class BlockRenderDispatcherMixin {
    @Inject(method = "renderSingleBlock", at = @At("HEAD"), cancellable = true)
    private void onRenderSingleBlock(BlockState state, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo ci) {
        if (FeatureToggle.TWEAK_SELECTIVE_BLOCK_RENDERING.getBooleanValue() && !SelectiveRendering.BLOCKS_LIST.isAllowed(state.getBlock())) {
            ci.cancel();
        }
    }

    @Inject(method = "renderLiquid", at = @At("HEAD"), cancellable = true)
    private void onRenderLiquid(BlockPos blockPos, BlockAndTintGetter blockAndTintGetter, VertexConsumer vertexConsumer, BlockState state, FluidState fluidState, CallbackInfo ci) {
        if (FeatureToggle.TWEAK_SELECTIVE_BLOCK_RENDERING.getBooleanValue() && !SelectiveRendering.BLOCKS_LIST.isAllowed(state.getBlock())) {
            ci.cancel();
        }
    }
}
