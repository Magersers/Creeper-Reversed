package io.github.magersers.creeperreversed.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.magersers.creeperreversed.FuseData;
import io.github.magersers.creeperreversed.FuseFlash;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {
    @Unique private float creeperReversed$handFlash;

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void creeperReversed$prepareHand(PoseStack pose, MultiBufferSource buffers, int light,
            AbstractClientPlayer player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        creeperReversed$handFlash = FuseFlash.whiteOverlay(player.getEntityData().get(FuseData.TICKS));
    }

    // Covers both arms and both sleeves, including the two-handed map view.
    @ModifyArg(method = "renderHand", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"), index = 3)
    private int creeperReversed$flashHand(int overlay) {
        return creeperReversed$handFlash > 0
                ? OverlayTexture.pack(OverlayTexture.u(creeperReversed$handFlash), OverlayTexture.v(false)) : overlay;
    }
}
