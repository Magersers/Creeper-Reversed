package io.github.magersers.creeperreversed.mixin;

import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin {
    @Shadow @Final private static EntityDataAccessor<Boolean> DATA_IS_IGNITED;
    @Shadow private int swell;
    @Shadow public abstract void setSwellDir(int direction);

    @Inject(method = "tick", at = @At("HEAD"))
    private void creeperReversed$defuse(CallbackInfo ci) {
        swell = 0;
        setSwellDir(-1);
        Creeper creeper = (Creeper) (Object) this;
        if (!creeper.level().isClientSide) creeper.getEntityData().set(DATA_IS_IGNITED, false);
    }

    // Also block forced ignition and custom creepers with a zero-length fuse.
    @Inject(method = "explodeCreeper", at = @At("HEAD"), cancellable = true)
    private void creeperReversed$preventExplosion(CallbackInfo ci) {
        ci.cancel();
    }
}
