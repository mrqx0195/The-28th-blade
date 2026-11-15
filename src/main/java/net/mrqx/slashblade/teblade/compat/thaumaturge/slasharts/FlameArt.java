package net.mrqx.slashblade.teblade.compat.thaumaturge.slasharts;

import mods.flammpfeil.slashblade.util.AttackHelper;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.utils.SlashBladeMovementUtils;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeAttachmentTypes;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.mrqx.slashblade.teblade.utils.CombatTargets;
import net.mrqx.truepower.util.TruePowerComboHelper;

import java.util.Optional;

public final class FlameArt {
    private static final double BURST_RADIUS = 2.0;
    private static final float BURST_DAMAGE_SCALE = 0.40F;
    private static final double SLASH_RADIUS = 4.0;
    private static final float SLASH_DAMAGE_SCALE = 0.55F;
    private static final double SAVE_CENTER_OFFSET = 2.0;
    private static final double SLASH_CENTER_OFFSET = 1.5;
    private static final int IGNITE_SECONDS = 5;
    private static final int IGNITE_REMAINING_LIMIT = 100;
    
    public static void burst(LivingEntity user, Vec3 center) {
        if (user.level().isClientSide()) {
            return;
        }
        ((ServerLevel) user.level()).sendParticles(ParticleTypes.FLAME,
            center.x, center.y + 0.5, center.z, 24, 1.0, 0.5, 1.0, 0.04);
        AABB area = new AABB(center, center).inflate(BURST_RADIUS);
        for (LivingEntity target : CombatTargets.inArea(user, area)) {
            if (target.position().distanceToSqr(center) > BURST_RADIUS * BURST_RADIUS) {
                continue;
            }
            float damage = (float) AttackHelper.calculateTotalDamage(user, target, BURST_DAMAGE_SCALE, false);
            target.hurt(user.damageSources().source(TEBladeDamageTypesBootstrap.SECONDARY_FLAME), damage);
        }
    }
    
    public static void perform(LivingEntity user) {
        if (user.level().isClientSide()) {
            return;
        }
        Entity target = TruePowerComboHelper.getTarget(user);
        if (target != null) {
            SlashBladeMovementUtils.tryTrickToTarget(user, target);
        }
        ((ServerLevel) user.level()).sendParticles(ParticleTypes.FLAME,
            user.getX(), user.getY() + 1, user.getZ(), 12, 0.5, 0.4, 0.5, 0.05);
        Vec3 forward = user.getLookAngle().multiply(1, 0, 1).normalize();
        Vec3 slashCenter = user.position().add(forward.scale(SLASH_CENTER_OFFSET));
        ((ServerLevel) user.level()).sendParticles(ParticleTypes.SWEEP_ATTACK,
            slashCenter.x, slashCenter.y + 1, slashCenter.z, 1, 0, 0, 0, 0);
        for (LivingEntity entity : CombatTargets.around(user, SLASH_RADIUS)) {
            if (entity.position().subtract(user.position()).dot(forward) <= 0) {
                continue;
            }
            float before = entity.getHealth();
            AttackManager.doMeleeAttack(user, entity, true, false, SLASH_DAMAGE_SCALE);
            if (entity.isAlive() && entity.getHealth() < before
                && entity.getRemainingFireTicks() < IGNITE_REMAINING_LIMIT) {
                entity.igniteForSeconds(IGNITE_SECONDS);
            }
        }
    }
    
    public static void saveCenter(LivingEntity user) {
        if (user.level().isClientSide()) {
            return;
        }
        Vec3 center = user.position().add(user.getLookAngle().multiply(1, 0, 1).normalize().scale(SAVE_CENTER_OFFSET));
        user.setData(TEBladeThaumaturgeAttachmentTypes.FLAME_CAST_CENTER, Optional.of(center));
    }
    
    public static Vec3 savedCenter(LivingEntity user) {
        return user.getData(TEBladeThaumaturgeAttachmentTypes.FLAME_CAST_CENTER).orElseGet(user::position);
    }
}
