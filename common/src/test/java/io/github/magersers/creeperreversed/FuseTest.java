package io.github.magersers.creeperreversed;
public final class FuseTest {
    public static void main(String[] args) {
        Fuse fuse = new Fuse();
        if (!fuse.starting(true)) throw new AssertionError("Initial hiss missing");
        for (int i = 0; i < 59; i++) if (fuse.tick(true)) throw new AssertionError("Early explosion");
        if (!fuse.tick(true)) throw new AssertionError("Must explode on tick 60");
        fuse.reset();
        for (int i = 0; i < 10; i++) fuse.tick(true);
        for (int i = 0; i < 15; i++) if (fuse.tick(false)) throw new AssertionError("Defusing exploded");
        if (fuse.ticks() != 0) throw new AssertionError("Negative fuse");
        for (int i = 0; i < 5; i++) fuse.tick(true);
        fuse.reset();
        if (fuse.ticks() != 0 || !fuse.starting(true)) throw new AssertionError("Reset failed");
        if (FuseFlash.whiteOverlay(0) != 0 || FuseFlash.whiteOverlay(6) < 0.5F
                || FuseFlash.whiteOverlay(12) != 0 || FuseFlash.whiteOverlay(53) < 0.9F)
            throw new AssertionError("Creeper flash curve incorrect");
        System.out.println("Fuse tests passed: 60-tick timing, defusing, clamping, reset, white flash curve.");
    }
}
