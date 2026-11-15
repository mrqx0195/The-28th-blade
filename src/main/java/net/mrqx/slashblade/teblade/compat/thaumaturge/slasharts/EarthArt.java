package net.mrqx.slashblade.teblade.compat.thaumaturge.slasharts;

import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.mrqx.sbr_core.utils.SlashBladeMovementUtils;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeMobEffects;
import net.mrqx.slashblade.teblade.utils.CombatTargets;
import net.mrqx.truepower.util.TruePowerComboHelper;

public final class EarthArt {
    private static final double RADIUS = 3.0;
    private static final float DAMAGE_SCALE = 0.90F;
    private static final int FRACTURE_TICKS = 60;
    private static final int ROOT_TICKS = 30;
    private static final int SLOWDOWN_TICKS = 40;
    private static final double KNOCKBACK_STRENGTH = 0.5;
    
    public static void perform(LivingEntity user, boolean perfect) {
        if (user.level().isClientSide()) {
            return;
        }
        ((ServerLevel) user.level()).sendParticles(ParticleTypes.CRIT,
            user.getX(), user.getY() + 1, user.getZ(), 12, 0.5, 0.4, 0.5, 0.05);
        Entity target = TruePowerComboHelper.getTarget(user);
        if (target != null) {
            SlashBladeMovementUtils.tryTrickToTarget(user, target);
        }
        for (LivingEntity entity : CombatTargets.around(user, RADIUS)) {
            float before = entity.getHealth();
            AttackManager.doMeleeAttack(user, entity, false, false, DAMAGE_SCALE);
            if (!entity.isAlive() || entity.getHealth() >= before) {
                continue;
            }
            if (perfect) {
                entity.addEffect(new MobEffectInstance(TEBladeThaumaturgeMobEffects.EARTH_FRACTURE, FRACTURE_TICKS, 0));
                if (!(entity instanceof Player)) {
                    entity.addEffect(new MobEffectInstance(TEBladeThaumaturgeMobEffects.EARTH_ROOT, ROOT_TICKS, 0));
                }
            }
            entity.knockback(KNOCKBACK_STRENGTH, user.getX() - entity.getX(), user.getZ() - entity.getZ());
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWDOWN_TICKS, 1));
        }
    }
}