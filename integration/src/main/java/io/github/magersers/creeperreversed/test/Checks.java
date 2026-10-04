package io.github.magersers.creeperreversed.test;

import com.mojang.authlib.GameProfile;
import io.github.magersers.creeperreversed.FuseData;
import io.github.magersers.creeperreversed.FleePlayerGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.level.block.Blocks;
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
    public static void run(MinecraftServer server) throws Exception {
        ServerLevel level = server.overworld();
        level.getChunk(0, 0);
        level.getGameRules().getRule(GameRules.RULE_MOBGRIEFING).set(false, server);
        Creeper creeper = new Creeper(EntityType.CREEPER, level);
        creeper.setPos(2, 250, 0);
        creeper.setNoAi(true);
        creeper.setNoGravity(true);
        level.addFreshEntity(creeper);
        TestPlayer player = new TestPlayer(level);
        ticks(player, 59, 0);
        check(player.isAlive(), "Player exploded before tick 60");
        check(player.getEntityData().get(FuseData.TICKS) == 59, "Fuse metadata not synchronized");
        ticks(player, 1, 0);
        check(!player.isAlive(), "Player did not explode on tick 60");
        check(player.getEntityData().get(FuseData.TICKS) == 0, "Flash not cleared on detonation");

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
        check(player.getEntityData().get(FuseData.TICKS) == 0, "Flash not cleared on escape");
        ticks(player, 59, 0);
        check(player.isAlive(), "Fuse was not reset after escape");

        player = new TestPlayer(level);
        player.creative = true;
        ticks(player, 80, 0);
        check(player.isAlive(), "Creative player exploded");
        player = new TestPlayer(level);
        player.spectator = true;
        ticks(player, 80, 0);
        check(player.isAlive(), "Spectator player exploded");

        creeper.ignite();
        for (int i = 0; i < 40; i++) creeper.tick();
        check(creeper.isAlive() && !creeper.isRemoved(), "Ignited creeper exploded");
        creeper.discard();

        for (int x = -20; x <= 20; x++) for (int z = -20; z <= 20; z++) {
            level.setBlockAndUpdate(new BlockPos(x, 249, z), Blocks.STONE.defaultBlockState());
        }
        creeper = new Creeper(EntityType.CREEPER, level);
        creeper.setPos(0.5, 250, 0.5);
        creeper.setOnGround(true);
        level.addFreshEntity(creeper);
        player = new TestPlayer(level);
        player.setPos(2.5, 250, 0.5);
        level.addFreshEntity(player);
        var field = Mob.class.getDeclaredField("goalSelector");
        field.setAccessible(true);
        GoalSelector goals = (GoalSelector) field.get(creeper);
        check(goals.getAvailableGoals().stream().noneMatch(w -> w.getGoal() instanceof SwellGoal
                || w.getGoal() instanceof MeleeAttackGoal), "Creeper still chases or primes");
        FleePlayerGoal flee = (FleePlayerGoal) goals.getAvailableGoals().stream()
                .map(w -> w.getGoal()).filter(g -> g instanceof FleePlayerGoal).findFirst().orElseThrow();
        boolean route = false;
        creeper.getRandom().setSeed(123);
        for (int i = 0; i < 30 && !route; i++) route = flee.canUse();
        check(route, "Creeper could not find an escape route from a player");
        flee.start();
        check(creeper.getNavigation().getPath() != null, "Creeper did not start fleeing");
        var destination = creeper.getNavigation().getPath().getEndNode();
        check(player.distanceToSqr(destination.x, destination.y, destination.z) > player.distanceToSqr(creeper),
                "Creeper path heads toward the player");
        creeper.discard();
        player.discard();
    }
}
