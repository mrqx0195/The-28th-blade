package net.mrqx.slashblade.teblade.compat.thaumaturge.slasharts;

import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.utils.SlashBladeMovementUtils;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.utils.CombatTargets;
import net.mrqx.truepower.util.TruePowerComboHelper;

public final class SkyArt {
    private static final ResourceLocation ARMOR_PENETRATION = TEBlade.prefix("sky_art_armor_penetration");
    private static final float ARMOR_PENETRATION_AMOUNT = -0.5F;
    private static final double PERFECT_REACH = 16.0;
    private static final double REACH = 8.0;
    private static final float PERFECT_DAMAGE_SCALE = 1.65F;
    private static final float DAMAGE_SCALE = 1.10F;
    private static final double MAX_LATERAL_OFFSET = 0.75;
    private static final double MAX_VERTICAL_OFFSET = 1.0;
    
    public static void perform(LivingEntity user, boolean perfect) {
        if (user.level().isClientSide()) {
            return;
        }
        Entity target = TruePowerComboHelper.getTarget(user);
        if (target != null) {
            SlashBladeMovementUtils.tryTrickToTarget(user, target);
        }
        ((ServerLevel) user.level()).sendParticles(ParticleTypes.CLOUD,
            user.getX(), user.getY() + 1, user.getZ(), 12, 0.5, 0.4, 0.5, 0.05);
        Vec3 start = user.position();
        Vec3 direction = user.getLookAngle();
        double reach = perfect ? PERFECT_REACH : REACH;
        Vec3 end = start.add(direction.multiply(1, 0, 1).normalize().scale(reach));
        
        int trailLength = (int) reach;
        Vec3 trailStep = end.subtract(start).scale(1.0 / trailLength);
        for (int step = 1; step <= trailLength; step++) {
            Vec3 point = start.add(trailStep.scale(step));
            ((ServerLevel) user.level()).sendParticles(ParticleTypes.END_ROD,
                point.x, point.y + 1, point.z, 1, 0, 0, 0, 0);
        }
        for (LivingEntity entity : CombatTargets.inArea(user, new AABB(start, end).inflate(1))) {
            if (!contains(start, direction, entity.position(), perfect)) {
                continue;
            }
            AttributeInstance armor = entity.getAttribute(Attributes.ARMOR);
            boolean penetrated = armor != null && !armor.hasModifier(ARMOR_PENETRATION);
            if (penetrated) {
                armor.addTransientModifier(new AttributeModifier(ARMOR_PENETRATION, ARMOR_PENETRATION_AMOUNT,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
            try {
                AttackManager.doMeleeAttack(user, entity, true, false,
                    perfect ? PERFECT_DAMAGE_SCALE : DAMAGE_SCALE);
            } finally {
                if (penetrated) {
                    armor.removeModifier(ARMOR_PENETRATION);
                }
            }
        }
    }
    
    public static boolean contains(Vec3 start, Vec3 direction, Vec3 target, boolean perfect) {
        Vec3 horizontal = direction.multiply(1, 0, 1);
        double magnitude = horizontal.length();
        Vec3 forward = horizontal.scale(1 / magnitude);
        Vec3 offset = target.subtract(start);
        double along = offset.dot(forward);
        double lateral = offset.x * forward.z - offset.z * forward.x;
        double reach = perfect ? PERFECT_REACH : REACH;
        return along <= reach && Math.abs(lateral) <= MAX_LATERAL_OFFSET && Math.abs(offset.y) <= MAX_VERTICAL_OFFSET;
    }
}