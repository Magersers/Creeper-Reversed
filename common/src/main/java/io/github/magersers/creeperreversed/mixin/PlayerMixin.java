package io.github.magersers.creeperreversed.mixin;

import io.github.magersers.creeperreversed.Fuse;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Unique private final Fuse creeperReversed$fuse = new Fuse();
    @Unique private Creeper creeperReversed$trigger;

    @Inject(method = "tick", at = @At("TAIL"))
    private void creeperReversed$tick(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!(player.level() instanceof ServerLevel level)) return;
        if (!player.isAlive() || player.isCreative() || player.isSpectator()) {
            creeperReversed$fuse.reset();
            creeperReversed$trigger = null;
            return;
        }
        if (creeperReversed$trigger == null || !creeperReversed$trigger.isAlive()
                || creeperReversed$trigger.level() != level) {
            creeperReversed$trigger = null;
            double nearest = 9.0;
            for (Creeper creeper : level.getEntitiesOfClass(Creeper.class, player.getBoundingBox().inflate(3))) {
                double distance = player.distanceToSqr(creeper);
                if (creeper.isAlive() && distance < nearest && player.hasLineOfSight(creeper)) {
                    nearest = distance;
                    creeperReversed$trigger = creeper;
                }
            }
        }
        Creeper trigger = creeperReversed$trigger;
        boolean armed = trigger != null && player.distanceToSqr(trigger) < 49.0
                && player.hasLineOfSight(trigger);
        if (creeperReversed$fuse.starting(armed)) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.CREEPER_PRIMED, SoundSource.PLAYERS, 1.0F, 0.5F);
        }
        boolean explode = creeperReversed$fuse.tick(armed);
        if (creeperReversed$fuse.ticks() > 0) {
            level.sendParticles(ParticleTypes.SMOKE, player.getX(), player.getY() + 1.1,
                    player.getZ(), 2, 0.2, 0.4, 0.2, 0.01);
        } else {
            creeperReversed$trigger = null;
        }
        if (explode) {
            float power = trigger != null && trigger.isPowered() ? 6.0F : 3.0F;
            creeperReversed$fuse.reset();
            creeperReversed$trigger = null;
            level.explode(player, player.getX(), player.getY(), player.getZ(), power, Level.ExplosionInteraction.MOB);
            player.hurt(player.damageSources().genericKill(), Float.MAX_VALUE);
        }
    }
}
