package net.mrqx.slashblade.teblade.compat.thaumaturge.slasharts;

import mods.flammpfeil.slashblade.util.AttackHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.utils.SlashBladeMovementUtils;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.mrqx.slashblade.teblade.utils.CombatTargets;
import net.mrqx.slashblade.teblade.utils.EntityMovementUtils;
import net.mrqx.truepower.attachment.ITruePowerStunData;
import net.mrqx.truepower.util.TruePowerComboHelper;

public final class ThunderArt {
    private static final float LIGHTNING_DAMAGE_SCALE = 0.25F;
    private static final int SLOWDOWN_TICKS = 20;
    private static final int STUN_TICKS = 20;
    private static final int BOLT_HEIGHT_STEPS = 5;
    private static final double DASH_DISTANCE = 2.0;
    private static final double MAX_LATERAL_OFFSET = 1.0;
    private static final double MAX_VERTICAL_OFFSET = 1.0;
    private static final double LIFT_VELOCITY = 0.4;
    
    public static boolean lightning(LivingEntity user, LivingEntity target) {
        if (user.level().isClientSide() || !CombatTargets.isValidTarget(user, target)) {
            return false;
        }
        for (int height = 0; height < BOLT_HEIGHT_STEPS; height++) {
            ((ServerLevel) user.level()).sendParticles(ParticleTypes.ELECTRIC_SPARK,
                target.getX(), target.getY() + height * 0.5, target.getZ(), 4, 0.15, 0.2, 0.15, 0.02);
        }
        float damage = (float) AttackHelper.calculateTotalDamage(user, target, LIGHTNING_DAMAGE_SCALE, false);
        boolean hit = target.hurt(user.damageSources().source(TEBladeDamageTypesBootstrap.SECONDARY_THUNDER), damage);
        if (hit && target.isAlive()) {
            if (target instanceof Player) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWDOWN_TICKS, 0));
            } else {
                ITruePowerStunData.get(target).triggerStun(STUN_TICKS);
            }
        }
        return hit;
    }
    
    public static void dash(LivingEntity user, boolean last) {
        if (user.level().isClientSide()) {
            return;
        }
        Entity target = TruePowerComboHelper.getTarget(user);
        if (target != null) {
            SlashBladeMovementUtils.tryTrickToTarget(user, target);
        }
        ((ServerLevel) user.level()).sendParticles(ParticleTypes.ELECTRIC_SPARK,
            user.getX(), user.getY() + 1, user.getZ(), 12, 0.5, 0.4, 0.5, 0.05);
        Vec3 start = user.position();
        Vec3 forward = user.getLookAngle().multiply(1, 0, 1).normalize();
        EntityMovementUtils.moveAndSync(user, forward.scale(DASH_DISTANCE));
        segment(user, start, user.position(), last);
    }
    
    public static void segment(LivingEntity user, Vec3 start, Vec3 end, boolean last) {
        Vec3 delta = end.subtract(start).multiply(1, 0, 1);
        double length = delta.length();
        Vec3 forward = delta.scale(1 / length);
        AABB area = new AABB(start, end).inflate(1);
        for (LivingEntity target : CombatTargets.inArea(user, area)) {
            Vec3 offset = target.position().subtract(start);
            double along = offset.dot(forward);
            double lateral = offset.x * forward.z - offset.z * forward.x;
            if (along > length || Math.abs(lateral) > MAX_LATERAL_OFFSET || Math.abs(offset.y) > MAX_VERTICAL_OFFSET) {
                continue;
            }
            float before = target.getHealth();
            lightning(user, target);
            if (last && target.isAlive() && target.getHealth() < before) {
                target.setDeltaMovement(target.getDeltaMovement().add(0, LIFT_VELOCITY, 0));
                target.hurtMarked = true;
            }
        }
    }
}