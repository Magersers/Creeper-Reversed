package io.github.magersers.creeperreversed;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;

/** Vanilla entity metadata delivers the fuse to the owner and tracking clients. */
public final class FuseData {
    // Assigned from Player's static initializer by PlayerMixin.
    public static EntityDataAccessor<Integer> TICKS;
    private FuseData() {}
}
