package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import mods.flammpfeil.slashblade.SlashBladeConfig;
import mods.flammpfeil.slashblade.util.AttackHelper;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.truepower.config.TruePowerCommonConfig;

import javax.annotation.Nullable;

public final class TEBladeSpellSwordDamage {
    public static final float AREA_COMBO_RATIO = 0.16F;
    private static final double MIN_BASE = 0.05D;
    private static final double MAX_STORED = 100000.0D;
    
    public static double baseDamage(LivingEntity caster) {
        double base = caster.getAttributeValue(Attributes.ATTACK_DAMAGE)
            + AttackHelper.getSweepingBonus(caster)
            + AttackHelper.getRankBonus(caster);
        return Math.max(MIN_BASE, base);
    }
    
    public static double attackScale(LivingEntity caster) {
        return Math.max(1.0E-4D,
            (double) AttackManager.getSlashBladeDamageScale(caster) * SlashBladeConfig.SLASHBLADE_DAMAGE_MULTIPLIER.get());
    }
    
    public static boolean truePowerTakesOver() {
        return TruePowerCommonConfig.MODIFY_SUMMONED_SWORD_DAMAGE.get();
    }
    
    public static double truePowerMultiplier() {
        return TruePowerCommonConfig.SUMMONED_SWORD_DAMAGE_MULTIPLIER.get();
    }
    
    public static double storedDamage(@Nullable LivingEntity caster, double desired) {
        if (caster == null) {
            return clamp(desired);
        }
        if (!truePowerTakesOver()) {
            return clamp(desired);
        }
        double multiplier = truePowerMultiplier();
        if (multiplier <= 0.0D) {
            return clamp(desired);
        }
        double factor = baseDamage(caster) * attackScale(caster) * multiplier;
        if (factor <= 0.0D) {
            return clamp(desired);
        }
        return clamp(desired / factor);
    }
    
    public static double storedAreaDamage(@Nullable LivingEntity caster, double desired) {
        if (caster == null) {
            return clamp(desired);
        }
        double attackDamage = Math.max(MIN_BASE, caster.getAttributeValue(Attributes.ATTACK_DAMAGE));
        double rankBonus = AttackHelper.getRankBonus(caster);
        double powerEnchant = 0.1D * powerEnchantLevel(caster);
        return clamp((desired / (double) AREA_COMBO_RATIO - rankBonus) / attackDamage - powerEnchant);
    }
    
    private static int powerEnchantLevel(LivingEntity caster) {
        return caster.registryAccess()
            .lookup(Registries.ENCHANTMENT)
            .flatMap(lookup -> lookup.get(Enchantments.POWER))
            .map(power -> EnchantmentHelper.getTagEnchantmentLevel(power, caster.getMainHandItem()))
            .orElse(0);
    }
    
    private static double clamp(double value) {
        if (!Double.isFinite(value)) {
            TEBlade.LOGGER.warn("Slash blade spell damage resolved to a non-finite value, clamping to 0");
            return 0.0D;
        }
        return Math.clamp(value, 0.0D, MAX_STORED);
    }
}
