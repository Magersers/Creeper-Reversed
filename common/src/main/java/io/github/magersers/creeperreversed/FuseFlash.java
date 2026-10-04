package io.github.magersers.creeperreversed;

/** The same alternating white-overlay curve used by the vanilla creeper renderer. */
public final class FuseFlash {
    private FuseFlash() {}
    public static float whiteOverlay(int ticks) {
        if (ticks <= 0) return 0.0F;
        float progress = Math.min(ticks / (float) (Fuse.DURATION - 2), 1.0F);
        return (int) (progress * 10.0F) % 2 == 0 ? 0.0F : Math.max(0.5F, progress);
    }
}
