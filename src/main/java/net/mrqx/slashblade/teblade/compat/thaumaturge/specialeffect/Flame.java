package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SpecialEffectGates;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public final class Flame extends SpecialEffect {
    private static final float ON_FIRE_DAMAGE_MULTIPLIER = 1.1F;
    private static final int IGNITE_SECONDS = 3;
    
    public Flame() {
        super(20, true, true);
    }
    
    private static boolean isActiveAttacker(DamageSource source) {
        return !source.is(TEBladeDamageTypesBootstrap.SECONDARY) && AttackUtils.isDirectMeleeAttack(source)
            && source.getEntity() instanceof LivingEntity user
            && SpecialEffectGates.isActive(user, TEBladeThaumaturgeSpecialEffects.FLAME);
    }
    
    @SubscribeEvent
    public static void onBeforeDamage(LivingDamageEvent.Pre event) {
        LivingEntity target = event.getEntity();
        float damage = event.getNewDamage();
        if (target.level().isClientSide() || !target.isOnFire()
            || !isActiveAttacker(event.getSource())) {
            return;
        }
        event.setNewDamage(damage * ON_FIRE_DAMAGE_MULTIPLIER);
    }
    
    @SubscribeEvent
    public static void onAfterDamage(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide() || !target.isAlive() || !isActiveAttacker(event.getSource())) {
            return;
        }
        target.igniteForSeconds(IGNITE_SECONDS);
    }
}
