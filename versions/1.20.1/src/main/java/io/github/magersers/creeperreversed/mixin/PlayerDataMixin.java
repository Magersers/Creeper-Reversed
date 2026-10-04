package io.github.magersers.creeperreversed.mixin;

import io.github.magersers.creeperreversed.FuseData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerDataMixin {
    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void creeperReversed$defineFuse(CallbackInfo ci) {
        ((Player) (Object) this).getEntityData().define(FuseData.TICKS, 0);
    }
}
