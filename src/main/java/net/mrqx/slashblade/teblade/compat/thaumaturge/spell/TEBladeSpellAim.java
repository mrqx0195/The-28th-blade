package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import com.leclowndu93150.thaumaturge.api.spell.cast.CastContext;
import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import com.leclowndu93150.thaumaturge.content.spell.world.SpellRayTrace;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public final class TEBladeSpellAim {
    public static final double CAST_RANGE = 16.0D;
    private static final double MIN_AIM_DISTANCE = 0.75D;
    private static final double EPSILON = 1.0E-6D;
    
    public record Aim(Vec3 point, @Nullable Entity entity, Vec3 direction) {
    }
    
    public static Aim resolve(CastContext ctx, SpellTarget incoming) {
        LivingEntity caster = ctx.caster();
        Vec3 direction = direction(incoming, caster);
        boolean origin = caster != null && incoming.entity().filter(entity -> entity.equals(caster)).isPresent();
        if (!origin) {
            return new Aim(incoming.position(), incoming.entity().orElse(null), direction);
        }
        HitResult hit = SpellRayTrace.trace(ctx.level(), caster, incoming.position(), direction, CAST_RANGE);
        if (hit.getType() == HitResult.Type.MISS) {
            return new Aim(incoming.position().add(direction.scale(CAST_RANGE)), null, direction);
        }
        Entity entity = hit instanceof EntityHitResult entityHit ? entityHit.getEntity() : null;
        return new Aim(hit.getLocation(), entity, direction);
    }
    
    public static Vec3 direction(SpellTarget target, @Nullable LivingEntity caster) {
        Vec3 direction = target.direction();
        if (direction.lengthSqr() > EPSILON) {
            return direction.normalize();
        }
        if (caster != null) {
            Vec3 look = caster.getLookAngle();
            if (look.lengthSqr() > EPSILON) {
                return look.normalize();
            }
        }
        return new Vec3(0.0D, 0.0D, 1.0D);
    }
    
    public static Vec3 launch(Vec3 muzzle, Vec3 point, Vec3 fallback) {
        Vec3 to = point.subtract(muzzle);
        if (to.lengthSqr() < MIN_AIM_DISTANCE * MIN_AIM_DISTANCE) {
            return fallback;
        }
        return to.normalize();
    }
}
