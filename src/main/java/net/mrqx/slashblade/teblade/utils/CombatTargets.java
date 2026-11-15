package net.mrqx.slashblade.teblade.utils;

import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Objects;

public final class CombatTargets {
    public static List<LivingEntity> around(LivingEntity user, double radius) {
        return user.level().getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(radius),
            target -> isHostile(user, target) && user.distanceToSqr(target) <= radius * radius
                && user.hasLineOfSight(target));
    }
    
    public static boolean isValidTarget(LivingEntity user, LivingEntity target) {
        return isHostile(user, target) && user.hasLineOfSight(target)
            && TargetSelector.getAreaAttackPredicate(-1).test(user, target);
    }
    
    public static List<LivingEntity> inArea(LivingEntity user, AABB area) {
        return user.level().getEntitiesOfClass(LivingEntity.class, area, target -> isValidTarget(user, target));
    }
    
    private static boolean isHostile(LivingEntity user, LivingEntity target) {
        return !Objects.equals(target, user) && target.isAlive() && !user.isAlliedTo(target);
    }
}