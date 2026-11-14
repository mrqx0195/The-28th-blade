package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import com.leclowndu93150.thaumaturge.api.spell.cast.CastContext;
import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.entity.TEBladeEntityBlistering;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.entity.TEBladeEntityHeavyRain;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.entity.TEBladeEntitySpiral;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.entity.TEBladeEntityStorm;

import javax.annotation.Nullable;

public final class TEBladeSpellFormations {
    public static final int SPIRAL_FIRE_DELAY = 10;
    public static final int BLISTERING_FIRE_DELAY = 10;
    public static final double HEAVY_RAIN_HEIGHT = 7.0D;
    public static final float VIRTUAL_EYE_HEIGHT = 1.8F;
    
    public static void spawn(ServerLevel level, CastContext ctx, int tint, SpellTarget target, int formation, int count, float perSword) {
        LivingEntity caster = ctx.caster();
        LivingEntity vehicle = (LivingEntity) target.entity().filter(entity -> entity.isAlive() && !entity.equals(caster) && entity instanceof LivingEntity).orElse(null);
        if (vehicle == null) {
            volley(level, caster, tint, target, formation, count, perSword);
            return;
        }
        for (int i = 0; i < count; i++) {
            switch (formation) {
                case 0 -> {
                    TEBladeEntitySpiral sword = new TEBladeEntitySpiral(TEBladeThaumaturgeEntityTypes.SPIRAL_SWORDS.get(), level);
                    sword.setPos(vehicle.position());
                    sword.setOwner(caster != null ? caster : vehicle);
                    sword.setColor(tint);
                    sword.setRoll(0.0F);
                    sword.setDamage(TEBladeSpellSwordDamage.storedDamage(caster, perSword));
                    sword.startRiding(vehicle, true);
                    sword.setDelay(360 / count * i);
                    sword.fireAfter(SPIRAL_FIRE_DELAY);
                    level.addFreshEntity(sword);
                }
                case 1 -> {
                    TEBladeEntityStorm sword = new TEBladeEntityStorm(TEBladeThaumaturgeEntityTypes.STORM_SWORDS.get(), level);
                    sword.setPos(vehicle.position());
                    sword.setOwner(caster != null ? caster : vehicle);
                    sword.setColor(tint);
                    sword.setRoll(0.0F);
                    sword.setDamage(TEBladeSpellSwordDamage.storedDamage(caster, perSword));
                    sword.startRiding(vehicle, true);
                    sword.setDelay(360 / count * i);
                    level.addFreshEntity(sword);
                }
                case 2 -> {
                    TEBladeEntityBlistering sword = new TEBladeEntityBlistering(TEBladeThaumaturgeEntityTypes.BLISTERING_SWORDS.get(), level);
                    sword.setPos(vehicle.position());
                    sword.setOwner(caster != null ? caster : vehicle);
                    sword.setColor(tint);
                    sword.setRoll(0.0F);
                    sword.setDamage(TEBladeSpellSwordDamage.storedDamage(caster, perSword));
                    sword.startRiding(vehicle, true);
                    sword.setDelay(i);
                    sword.launchAfter(BLISTERING_FIRE_DELAY + i);
                    level.addFreshEntity(sword);
                }
                default -> {
                    Vec3 above = vehicle.position().add(0.0D, HEAVY_RAIN_HEIGHT, 0.0D);
                    TEBladeEntityHeavyRain sword = new TEBladeEntityHeavyRain(TEBladeThaumaturgeEntityTypes.HEAVY_RAIN_SWORDS.get(), level);
                    sword.setOwner(caster != null ? caster : vehicle);
                    sword.setColor(tint);
                    sword.setRoll(0.0F);
                    sword.setDamage(TEBladeSpellSwordDamage.storedDamage(caster, perSword));
                    sword.startRiding(vehicle, true);
                    sword.setDelay(i);
                    if (i == 0) {
                        sword.setPos(above);
                    } else {
                        sword.setSpread(above);
                    }
                    sword.setXRot(-90.0F);
                    level.addFreshEntity(sword);
                }
            }
        }
    }
    
    public static void volley(ServerLevel level, @Nullable LivingEntity caster, int tint, SpellTarget target, int formation, int count, float perSword) {
        Vec3 point = target.position();
        for (int i = 0; i < count; i++) {
            switch (formation) {
                case 0 -> {
                    TEBladeEntitySpiral sword = new TEBladeEntitySpiral(TEBladeThaumaturgeEntityTypes.SPIRAL_SWORDS.get(), level);
                    sword.setPos(point);
                    sword.anchorAt(point);
                    sword.setOwner(caster);
                    sword.setColor(tint);
                    sword.setRoll(0.0F);
                    sword.setDamage(TEBladeSpellSwordDamage.storedDamage(caster, perSword));
                    sword.setDelay(360 / count * i);
                    sword.fireAfter(SPIRAL_FIRE_DELAY);
                    level.addFreshEntity(sword);
                }
                case 1 -> {
                    TEBladeEntityStorm sword = new TEBladeEntityStorm(TEBladeThaumaturgeEntityTypes.STORM_SWORDS.get(), level);
                    sword.setPos(point);
                    sword.anchorAt(point);
                    sword.setOwner(caster);
                    sword.setColor(tint);
                    sword.setRoll(0.0F);
                    sword.setDamage(TEBladeSpellSwordDamage.storedDamage(caster, perSword));
                    sword.setDelay(360 / count * i);
                    level.addFreshEntity(sword);
                }
                case 2 -> {
                    TEBladeEntityBlistering sword = new TEBladeEntityBlistering(TEBladeThaumaturgeEntityTypes.BLISTERING_SWORDS.get(), level);
                    sword.setPos(point);
                    float yaw = virtualYaw(target.direction());
                    float pitch = virtualPitch(target.direction());
                    sword.anchorAt(point, yaw, pitch);
                    sword.setOwner(caster);
                    sword.setColor(tint);
                    sword.setRoll(0.0F);
                    sword.setDamage(TEBladeSpellSwordDamage.storedDamage(caster, perSword));
                    sword.setDelay(i);
                    sword.launchAfter(BLISTERING_FIRE_DELAY + i);
                    level.addFreshEntity(sword);
                }
                default -> {
                    TEBladeEntityHeavyRain sword = new TEBladeEntityHeavyRain(TEBladeThaumaturgeEntityTypes.HEAVY_RAIN_SWORDS.get(), level);
                    Vec3 above = point.add(0.0D, HEAVY_RAIN_HEIGHT, 0.0D);
                    sword.anchorAt(point);
                    sword.setOwner(caster);
                    sword.setColor(tint);
                    sword.setRoll(0.0F);
                    sword.setDamage(TEBladeSpellSwordDamage.storedDamage(caster, perSword));
                    sword.setDelay(i);
                    if (i == 0) {
                        sword.setPos(above);
                    } else {
                        sword.setSpread(above);
                    }
                    sword.setXRot(-90.0F);
                    level.addFreshEntity(sword);
                }
            }
        }
    }
    
    public static float virtualYaw(Vec3 direction) {
        Vec3 flat = direction.lengthSqr() < 1.0E-6D ? new Vec3(0.0D, 0.0D, 1.0D) : direction;
        return (float) Math.toDegrees(Math.atan2(-flat.x, flat.z));
    }
    
    public static float virtualPitch(Vec3 direction) {
        Vec3 unit = direction.lengthSqr() < 1.0E-6D ? new Vec3(0.0D, 0.0D, 1.0D) : direction.normalize();
        return (float) -Math.toDegrees(Math.asin(Mth.clamp(unit.y, -1.0D, 1.0D)));
    }
}
