package io.github.magersers.creeperreversed.test.mixin;

import io.github.magersers.creeperreversed.test.Checks;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/** Test-only mixin: never included in release JARs. Uses an isolated local test world. */
@Mixin(MinecraftServer.class)
public abstract class ServerTestMixin {
    @Unique private boolean creeperReversed$tested;

    @Inject(method = "tickServer", at = @At("TAIL"))
    private void creeperReversed$test(CallbackInfo ci) throws Exception {
        MinecraftServer server = (MinecraftServer) (Object) this;
        if (creeperReversed$tested || server.getTickCount() < 40) return;
        creeperReversed$tested = true;
        try {
            Checks.run(server);
            Files.writeString(Path.of("integration-result.txt"), "PASS: 60-tick detonation, defusing, creative, spectator, creeper ignition, synced fuse, flee path\n");
            System.out.println("CREEPER_REVERSED_INTEGRATION_PASS");
        } catch (Throwable error) {
            Files.writeString(Path.of("integration-result.txt"), "FAIL: " + error + "\n");
            error.printStackTrace();
        } finally {
            server.halt(false);
        }
    }
}
