package io.github.magersers.creeperreversed;
public final class FuseTest {
    public static void main(String[] args) {
        Fuse fuse = new Fuse();
        if (!fuse.starting(true)) throw new AssertionError("Initial hiss missing");
        for (int i = 0; i < 29; i++) if (fuse.tick(true)) throw new AssertionError("Early explosion");
        if (!fuse.tick(true)) throw new AssertionError("Must explode on tick 30");
        fuse.reset();
        for (int i = 0; i < 10; i++) fuse.tick(true);
        for (int i = 0; i < 15; i++) if (fuse.tick(false)) throw new AssertionError("Defusing exploded");
        if (fuse.ticks() != 0) throw new AssertionError("Negative fuse");
        for (int i = 0; i < 5; i++) fuse.tick(true);
        fuse.reset();
        if (fuse.ticks() != 0 || !fuse.starting(true)) throw new AssertionError("Reset failed");
        System.out.println("Fuse tests passed: timing, defusing, clamping, reset.");
    }
}
