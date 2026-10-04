package io.github.magersers.creeperreversed;

/** Vanilla-style 30-tick fuse, with gradual defusing outside the danger radius. */
public final class Fuse {
    private int ticks;
    public boolean starting(boolean armed) { return armed && ticks == 0; }
    public boolean tick(boolean armed) {
        ticks = Math.max(0, ticks + (armed ? 1 : -1));
        return ticks >= 30;
    }
    public int ticks() { return ticks; }
    public void reset() { ticks = 0; }
}
