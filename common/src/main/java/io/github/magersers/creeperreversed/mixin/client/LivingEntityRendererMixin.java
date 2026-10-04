package io.github.magersers.creeperreversed.mixin.client;

import io.github.magersers.creeperreversed.FuseData;
import io.github.magersers.creeperreversed.FuseFlash;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "getWhiteOverlayProgress", at = @At("HEAD"), cancellable = true)
    private void creeperReversed$flashSkin(LivingEntity entity, float partialTick, CallbackInfoReturnable<Float> cir) {
        if (entity instanceof Player player) {
            float flash = FuseFlash.whiteOverlay(player.getEntityData().get(FuseData.TICKS));
            if (flash > 0) cir.setReturnValue(flash);
        }
    }
}
