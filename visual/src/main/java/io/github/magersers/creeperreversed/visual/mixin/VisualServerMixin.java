package io.github.magersers.creeperreversed.visual.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Opt-in disposable-world visual test, never present in release artifacts. */
@Mixin(MinecraftServer.class)
public abstract class VisualServerMixin {
    @Unique private int creeperReversed$visualTicks;
    @Inject(method = "tickServer", at = @At("TAIL"))
    private void creeperReversed$stageScene(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        if (server.getTickCount() == 1) {
            var level = server.overworld();
            level.getEntitiesOfClass(Creeper.class, new net.minecraft.world.phys.AABB(-24, 245, -24, 24, 260, 24))
                    .forEach(Creeper::discard);
        }
        if (server.getPlayerList().getPlayers().isEmpty()) {
            if (creeperReversed$visualTicks > 0) server.halt(false);
            return;
        }
        creeperReversed$visualTicks++;
        if (creeperReversed$visualTicks == 40) {
            ServerPlayer player = server.getPlayerList().getPlayers().get(0);
            var level = server.overworld();
            for (int x = -8; x <= 8; x++) for (int z = -8; z <= 8; z++) {
                level.setBlockAndUpdate(new BlockPos(x, 249, z), Blocks.STONE.defaultBlockState());
            }
            level.setDayTime(6000);
            level.setWeatherParameters(6000, 0, false, false);
            player.setGameMode(GameType.SURVIVAL);
            player.teleportTo(level, 0.5, 250, 0.5, 0, 0);
        }
        if (creeperReversed$visualTicks == 80) {
            var level = server.overworld();
            Creeper creeper = new Creeper(EntityType.CREEPER, level);
            creeper.setPos(2.5, 250, 0.5);
            creeper.setNoAi(true);
            creeper.setNoGravity(true);
            level.addFreshEntity(creeper);
        }
        if (creeperReversed$visualTicks > 250) server.halt(false);
    }
}
