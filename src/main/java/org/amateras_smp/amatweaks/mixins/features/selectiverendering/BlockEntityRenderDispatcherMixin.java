package org.amateras_smp.amatweaks.mixins.features.selectiverendering;


import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.SelectiveRendering;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 12110
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
//#else
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
//#endif

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
    //#if MC >= 12110
    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true)
    private <S extends BlockEntity> void onRender(BlockEntity blockEntity, float f, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, CallbackInfoReturnable<S> cir) {
        if (FeatureToggle.TWEAK_SELECTIVE_BLOCK_RENDERING.getBooleanValue() && !SelectiveRendering.BLOCKS_LIST.isAllowed(blockEntity.getBlockState().getBlock())) {
            cir.cancel();
        }
    }
    //#else
    //$$ @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    //$$ private <E extends BlockEntity> void render(E blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfoReturnable<E> cir) {
    //$$     if (FeatureToggle.TWEAK_SELECTIVE_BLOCK_RENDERING.getBooleanValue() && !SelectiveRendering.BLOCKS_LIST.isAllowed(blockEntity.getBlockState().getBlock())) {
    //$$         cir.cancel();
    //$$     }
    //$$ }
    //#endif
}
