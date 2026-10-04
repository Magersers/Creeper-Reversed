package io.github.magersers.creeperreversed;

import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;

public final class FleePlayerGoal extends AvoidEntityGoal<Player> {
    public FleePlayerGoal(Creeper creeper) {
        super(creeper, Player.class, 6.0F, 1.0D, 1.2D,
                entity -> entity.isAlive() && !entity.isSpectator());
    }
}
