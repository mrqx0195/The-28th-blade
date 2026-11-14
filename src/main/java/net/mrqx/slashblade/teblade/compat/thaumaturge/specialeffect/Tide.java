package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeAttachmentTypes;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeMobEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SlashArtCastTracker;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SpecialEffectGates;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public final class Tide extends SpecialEffect {
    private static final int SLOW_DURATION_TICKS = 60;
    private static final int SLOW_AMPLIFIER = 0;
    
    public Tide() {
        super(20, true, true);
    }
    
    @SubscribeEvent
    public static void onAfterDamage(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (event.getSource().is(TEBladeDamageTypesBootstrap.SECONDARY)
            || SlashArtCastTracker.attributedCast(event.getSource()) != null
            || !AttackUtils.isDirectMeleeAttack(event.getSource())
            || !(event.getSource().getEntity() instanceof LivingEntity user)) {
            return;
        }
        onCastHit(user, target, meleeAttackId(user));
    }
    
    public static void onCastHit(LivingEntity user, LivingEntity target, long attackId) {
        if (user.level().isClientSide() || !target.isAlive() || target.equals(user) || !user.isAlive()
            || !SpecialEffectGates.isActive(user, TEBladeThaumaturgeSpecialEffects.TIDE)) {
            return;
        }
        long now = user.level().getGameTime();
        if (target.getData(TEBladeThaumaturgeAttachmentTypes.TIDE_STACKS).hit(now, user.getUUID(), attackId)) {
            target.addEffect(new MobEffectInstance(TEBladeThaumaturgeMobEffects.TIDE_SLOW, SLOW_DURATION_TICKS,
                SLOW_AMPLIFIER), user);
        }
    }
    
    private static long meleeAttackId(LivingEntity user) {
        return -1L - user.level().getGameTime();
    }
}
