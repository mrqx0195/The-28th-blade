package net.mrqx.slashblade.teblade.utils;

public final class BladeHealingBudget {
    private final int windowTicks;
    private final float maximum;
    private long windowStart;
    private float used;
    private boolean initialized;
    
    public BladeHealingBudget(int windowTicks, float maximum) {
        if (windowTicks <= 0 || !Float.isFinite(maximum) || maximum <= 0) {
            throw new IllegalArgumentException("Invalid healing budget configuration");
        }
        this.windowTicks = windowTicks;
        this.maximum = maximum;
    }
    
    public float claim(long tick, float requested) {
        if (tick < 0 || !Float.isFinite(requested) || requested < 0) {
            throw new IllegalArgumentException("Invalid healing budget request");
        }
        if (!initialized || tick < windowStart || tick - windowStart >= windowTicks) {
            initialized = true;
            windowStart = tick;
            used = 0;
        }
        float granted = Math.clamp(requested, 0, maximum - used);
        used += granted;
        return granted;
    }
}
