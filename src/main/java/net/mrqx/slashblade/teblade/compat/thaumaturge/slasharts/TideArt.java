package net.mrqx.slashblade.teblade.compat.thaumaturge.slasharts;

import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.utils.SlashBladeMovementUtils;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeMobEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.CombatTargets;
import net.mrqx.slashblade.teblade.utils.EntityMovementUtils;
import net.mrqx.truepower.util.TruePowerComboHelper;

public final class TideArt {
    private static final double DASH_DISTANCE = 3.0;
    private static final float DAMAGE_SCALE = 0.50F;
    private static final int ROOT_TICKS = 20;
    private static final int FROZEN_TICKS = 80;
    private static final int SLOW_TICKS = 60;
    private static final int SLOW_AMPLIFIER = 1;
    private static final double MAX_LATERAL_OFFSET = 1.0;
    private static final double MAX_VERTICAL_OFFSET = 1.0;
    
    public static void dash(LivingEntity user, boolean perfect) {
        if (user.level().isClientSide()) {
            return;
        }
        Entity target = TruePowerComboHelper.getTarget(user);
        if (target != null) {
            SlashBladeMovementUtils.tryTrickToTarget(user, target);
        }
        ((ServerLevel) user.level()).sendParticles(ParticleTypes.SPLASH,
            user.getX(), user.getY() + 1, user.getZ(), 12, 0.5, 0.4, 0.5, 0.05);
        Vec3 start = user.position();
        Vec3 forward = user.getLookAngle().multiply(1, 0, 1).normalize();
        EntityMovementUtils.moveAndSync(user, forward.scale(DASH_DISTANCE));
        segment(user, start, user.position(), perfect);
    }
    
    public static void segment(LivingEntity user, Vec3 start, Vec3 end, boolean perfect) {
        for (LivingEntity target : CombatTargets.inArea(user, new AABB(start, end).inflate(1))) {
            if (!contains(start, end, target.position())) {
                continue;
            }
            float before = target.getHealth();
            AttackManager.doMeleeAttack(user, target, true, false, DAMAGE_SCALE);
            if (target.isAlive() && target.getHealth() < before) {
                applyControl(target, perfect);
            }
        }
    }
    
    static void applyControl(LivingEntity target, boolean perfect) {
        if (perfect && !(target instanceof Player)) {
            target.addEffect(new MobEffectInstance(TEBladeThaumaturgeMobEffects.EARTH_ROOT, ROOT_TICKS, 0));
            if (target.canFreeze()) {
                target.setTicksFrozen(Math.max(target.getTicksFrozen(), FROZEN_TICKS));
            }
        }
        target.addEffect(new MobEffectInstance(TEBladeThaumaturgeMobEffects.TIDE_SLOW, SLOW_TICKS, SLOW_AMPLIFIER));
    }
    
    static boolean contains(Vec3 start, Vec3 end, Vec3 target) {
        Vec3 path = end.subtract(start).multiply(1, 0, 1);
        double length = path.length();
        Vec3 forward = path.scale(1 / length);
        Vec3 offset = target.subtract(start);
        double along = offset.dot(forward);
        double lateral = offset.x * forward.z - offset.z * forward.x;
        return along <= length && Math.abs(lateral) <= MAX_LATERAL_OFFSET && Math.abs(offset.y) <= MAX_VERTICAL_OFFSET;
    }
}