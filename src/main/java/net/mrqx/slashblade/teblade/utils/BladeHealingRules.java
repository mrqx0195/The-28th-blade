package net.mrqx.slashblade.teblade.utils;

public final class BladeHealingRules {
    public static float observedHealthDamage(float damageAfterAbsorption, float healthBefore, float healthAfter) {
        float bounded = actualHealthDamage(damageAfterAbsorption, healthBefore);
        if (!Float.isFinite(healthAfter) || healthAfter < 0) {
            throw new IllegalArgumentException("Invalid final health value");
        }
        return Math.clamp(healthBefore - healthAfter, 0, bounded);
    }
    
    public static float actualHealthDamage(float damageAfterAbsorption, float healthBefore) {
        if (!Float.isFinite(damageAfterAbsorption) || !Float.isFinite(healthBefore)
            || damageAfterAbsorption < 0 || healthBefore < 0) {
            throw new IllegalArgumentException("Invalid health damage values");
        }
        return Math.min(damageAfterAbsorption, healthBefore);
    }
    
    public static float bloodthirst(float actualHealthDamage, float health, float maxHealth) {
        if (!Float.isFinite(actualHealthDamage) || !Float.isFinite(health)
            || !Float.isFinite(maxHealth) || actualHealthDamage < 0
            || health < 0 || maxHealth <= 0 || health > maxHealth) {
            throw new IllegalArgumentException("Invalid bloodthirst health values");
        }
        if (health == 0) {
            return 0;
        }
        float ratio = health / maxHealth < 0.30F ? 0.20F : 0.12F;
        return Math.min(maxHealth - health, Math.min(2.0F, actualHealthDamage * ratio));
    }
}
