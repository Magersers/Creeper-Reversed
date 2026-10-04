package io.github.magersers.creeperreversed.mixin;

import net.minecraft.world.entity.monster.Creeper;
import io.github.magersers.creeperreversed.FleePlayerGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends Monster {
    protected CreeperMixin(EntityType<? extends Monster> type, Level level) { super(type, level); }
    @Shadow @Final private static EntityDataAccessor<Boolean> DATA_IS_IGNITED;
    @Shadow private int swell;
    @Shadow public abstract void setSwellDir(int direction);

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void creeperReversed$fleePlayers(CallbackInfo ci) {
        goalSelector.removeAllGoals(goal -> goal instanceof SwellGoal || goal instanceof MeleeAttackGoal);
        targetSelector.removeAllGoals(goal -> goal instanceof NearestAttackableTargetGoal);
        goalSelector.addGoal(1, new FleePlayerGoal((Creeper) (Object) this));
    }

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
