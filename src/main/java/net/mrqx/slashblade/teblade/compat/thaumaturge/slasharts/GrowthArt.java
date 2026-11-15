package net.mrqx.slashblade.teblade.compat.thaumaturge.slasharts;

import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.sbr_core.utils.SlashBladeMovementUtils;
import net.mrqx.slashblade.teblade.utils.CombatTargets;
import net.mrqx.truepower.util.TruePowerComboHelper;

public final class GrowthArt {
    private static final double RADIUS = 3.0;
    private static final float DAMAGE_SCALE = 0.70F;
    private static final float PERFECT_HEAL = 8.0F;
    private static final float HEAL = 4.0F;
    private static final int RESISTANCE_TICKS = 40;
    
    public static void perform(LivingEntity user, boolean perfect) {
        if (user.level().isClientSide()) {
            return;
        }
        Entity target = TruePowerComboHelper.getTarget(user);
        if (target != null) {
            SlashBladeMovementUtils.tryTrickToTarget(user, target);
        }
        ((ServerLevel) user.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER,
            user.getX(), user.getY() + 1, user.getZ(), 12, 0.5, 0.4, 0.5, 0.05);
        for (LivingEntity entity : CombatTargets.around(user, RADIUS)) {
            AttackManager.doMeleeAttack(user, entity, false, false, DAMAGE_SCALE);
        }
        user.heal(perfect ? PERFECT_HEAL : HEAL);
        if (perfect) {
            user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, RESISTANCE_TICKS, 0));
        }
    }
}