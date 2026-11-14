package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import com.leclowndu93150.thaumaturge.api.warp.WarpHelper;
import com.leclowndu93150.thaumaturge.api.warp.WarpType;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeMobEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SpecialEffectGates;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public final class TaintMark extends SpecialEffect {
    private static final int CORROSION_DURATION_TICKS = 160;
    private static final int MAX_CORROSION_AMPLIFIER = 4;
    private static final int WARP_PER_HIT = 1;
    
    public TaintMark() {
        super(40, false, false);
    }
    
    @SubscribeEvent
    public static void onAfterDamage(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide() || !target.isAlive()
            || event.getSource().is(TEBladeDamageTypesBootstrap.SECONDARY)
            || !AttackUtils.isDirectMeleeAttack(event.getSource())
            || !(event.getSource().getEntity() instanceof LivingEntity user) || target.equals(user)
            || !SpecialEffectGates.isActive(user, TEBladeThaumaturgeSpecialEffects.TAINT_MARK,
            ThaumaturgeBladeForms.Family.TAINT)) {
            return;
        }
        MobEffectInstance previous = target.getEffect(TEBladeThaumaturgeMobEffects.CORROSION);
        int amplifier = previous == null ? 0
            : Math.min(MAX_CORROSION_AMPLIFIER, previous.getAmplifier() + 1);
        if (target.addEffect(new MobEffectInstance(TEBladeThaumaturgeMobEffects.CORROSION,
            CORROSION_DURATION_TICKS, amplifier), user) && user instanceof ServerPlayer serverPlayer) {
            WarpHelper.addWarp(serverPlayer, WARP_PER_HIT, WarpType.TEMPORARY);
        }
    }
}
