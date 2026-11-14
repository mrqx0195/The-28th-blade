package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SpecialEffectGates;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

public final class Earth extends SpecialEffect {
    private static final float DAMAGE_MULTIPLIER = 0.94F;
    private static final float KNOCKBACK_MULTIPLIER = 0.5F;
    
    public Earth() {
        super(30, true, true);
    }
    
    @SubscribeEvent
    public static void onBeforeDamage(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        float damage = event.getNewDamage();
        if (entity.level().isClientSide()
            || !SpecialEffectGates.isActive(entity, TEBladeThaumaturgeSpecialEffects.EARTH)) {
            return;
        }
        event.setNewDamage(damage * DAMAGE_MULTIPLIER);
    }
    
    @SubscribeEvent
    public static void onKnockBack(LivingKnockBackEvent event) {
        LivingEntity entity = event.getEntity();
        float strength = event.getStrength();
        if (entity.level().isClientSide()
            || !SpecialEffectGates.isActive(entity, TEBladeThaumaturgeSpecialEffects.EARTH)) {
            return;
        }
        event.setStrength(strength * KNOCKBACK_MULTIPLIER);
    }
}
