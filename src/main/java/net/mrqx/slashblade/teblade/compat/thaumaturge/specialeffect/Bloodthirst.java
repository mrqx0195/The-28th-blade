package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeAttachmentTypes;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SpecialEffectGates;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.mrqx.slashblade.teblade.utils.BladeHealingBudget;
import net.mrqx.slashblade.teblade.utils.BladeHealingRules;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public final class Bloodthirst extends SpecialEffect {
    public static final int BUDGET_WINDOW_TICKS = 20;
    public static final float BUDGET_MAXIMUM = 2.0F;
    
    public record Snapshot(DamageSource source, float health, long tick) {
    }
    
    public Bloodthirst() {
        super(30, false, false);
    }
    
    @Nullable
    private static LivingEntity attacker(DamageSource source) {
        if (source.is(TEBladeDamageTypesBootstrap.SECONDARY) || !AttackUtils.isDirectMeleeAttack(source)
            || !(source.getEntity() instanceof LivingEntity user)
            || !SpecialEffectGates.isActive(user, TEBladeThaumaturgeSpecialEffects.BLOODTHIRST,
            ThaumaturgeBladeForms.Family.BLOOD)) {
            return null;
        }
        return user;
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBeforeDamage(LivingDamageEvent.Pre event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide() || event.getSource().is(TEBladeDamageTypesBootstrap.SECONDARY)) {
            return;
        }
        victim.setData(TEBladeThaumaturgeAttachmentTypes.BLOODTHIRST_SNAPSHOT,
            attacker(event.getSource()) == null ? Optional.empty()
                : Optional.of(new Snapshot(event.getSource(), victim.getHealth(), victim.level().getGameTime())));
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAfterDamage(LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide() || event.getSource().is(TEBladeDamageTypesBootstrap.SECONDARY)) {
            return;
        }
        Optional<Snapshot> recorded = victim.getData(TEBladeThaumaturgeAttachmentTypes.BLOODTHIRST_SNAPSHOT);
        victim.setData(TEBladeThaumaturgeAttachmentTypes.BLOODTHIRST_SNAPSHOT, Optional.empty());
        if (recorded.isEmpty()) {
            return;
        }
        Snapshot snapshot = recorded.get();
        if (!snapshot.source().equals(event.getSource()) || snapshot.tick() != victim.level().getGameTime()) {
            return;
        }
        LivingEntity user = attacker(event.getSource());
        if (user == null || Objects.equals(user, victim)) {
            return;
        }
        float damage = event.getNewDamage();
        float after = victim.getHealth();
        float lost = BladeHealingRules.observedHealthDamage(damage, snapshot.health(), after);
        float health = user.getHealth();
        float maximum = user.getMaxHealth();
        float requested = BladeHealingRules.bloodthirst(lost, health, maximum);
        BladeHealingBudget budget = user.getData(TEBladeThaumaturgeAttachmentTypes.BLOODTHIRST_BUDGET);
        float granted = budget.claim(user.level().getGameTime(), requested);
        if (granted > 0) {
            user.heal(granted);
        }
    }
}
