package io.github.magersers.creeperreversed.test;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import java.util.UUID;

public final class Checks {
    private static final class TestPlayer extends Player {
        boolean creative;
        boolean spectator;
        TestPlayer(ServerLevel level) {
            super(level, new BlockPos(0, 250, 0), 0, new GameProfile(UUID.randomUUID(), "FuseTest"));
        }
        @Override public boolean isCreative() { return creative; }
        @Override public boolean isSpectator() { return spectator; }
    }
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    private static void ticks(TestPlayer player, int count, double x) {
        for (int i = 0; i < count; i++) {
            player.setPos(x, 250, 0);
            player.tick();
        }
    }
    public static void run(MinecraftServer server) {
        ServerLevel level = server.overworld();
        level.getChunk(0, 0);
        level.getGameRules().getRule(GameRules.RULE_MOBGRIEFING).set(false, server);
        Creeper creeper = new Creeper(EntityType.CREEPER, level);
        creeper.setPos(2, 250, 0);
        creeper.setNoAi(true);
        creeper.setNoGravity(true);
        level.addFreshEntity(creeper);
        TestPlayer player = new TestPlayer(level);
        ticks(player, 29, 0);
        check(player.isAlive(), "Player exploded before tick 30");
        ticks(player, 1, 0);
        check(!player.isAlive(), "Player did not explode on tick 30");

        // The triggering creeper may die in the player's blast; replace it.
        creeper.discard();
        creeper = new Creeper(EntityType.CREEPER, level);
        creeper.setPos(2, 250, 0);
        creeper.setNoAi(true);
        creeper.setNoGravity(true);
        level.addFreshEntity(creeper);
        player = new TestPlayer(level);
        ticks(player, 15, 0);
        ticks(player, 20, 20);
        check(player.isAlive(), "Escaping failed to defuse");
        ticks(player, 29, 0);
        check(player.isAlive(), "Fuse was not reset after escape");

        player = new TestPlayer(level);
        player.creative = true;
        ticks(player, 40, 0);
        check(player.isAlive(), "Creative player exploded");
        player = new TestPlayer(level);
        player.spectator = true;
        ticks(player, 40, 0);
        check(player.isAlive(), "Spectator player exploded");

        creeper.ignite();
        for (int i = 0; i < 40; i++) creeper.tick();
        check(creeper.isAlive() && !creeper.isRemoved(), "Ignited creeper exploded");
        creeper.discard();
    }
}
