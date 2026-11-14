package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import com.leclowndu93150.thaumaturge.api.spell.behavior.SpellBehaviorType;
import com.leclowndu93150.thaumaturge.api.spell.cast.CastContext;
import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import com.leclowndu93150.thaumaturge.content.spell.carrier.CarrierPayload;
import com.leclowndu93150.thaumaturge.content.spell.delivery.AbstractCarrierDelivery;
import com.mojang.serialization.MapCodec;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.entity.TEBladeEntitySpellDrive;

public class TEBladeCrescentEdgeDelivery extends AbstractCarrierDelivery {
    public static final MapCodec<TEBladeCrescentEdgeDelivery> CODEC = MapCodec.unit(TEBladeCrescentEdgeDelivery::new);
    
    public static final float SPEED = 2.0F;
    public static final double MUZZLE_OFFSET = 0.6D;
    private static final int MIN_LIFETIME = 8;
    private static final int MAX_LIFETIME = 60;
    
    public TEBladeCrescentEdgeDelivery() {
    }
    
    @Override
    public SpellBehaviorType<?> type() {
        return TEBladeThaumaturgeSpellBehaviorTypes.CRESCENT_EDGE.get();
    }
    
    @Override
    protected void launch(CastContext ctx, LivingEntity caster, CarrierPayload payload, SpellTarget origin) {
        ServerLevel level = ctx.level();
        TEBladeSpellAim.Aim aim = TEBladeSpellAim.resolve(ctx, origin);
        Vec3 look = aim.direction();
        Vec3 muzzle = caster.getEyePosition().add(look.scale(MUZZLE_OFFSET));
        Vec3 heading = TEBladeSpellAim.launch(muzzle, aim.point(), caster.getLookAngle());
        int range = Mth.clamp(ctx.setting("range"), 1, 8);
        double distance = muzzle.distanceTo(aim.point()) + range * 2.0D;
        int lifetime = Mth.clamp((int) Math.ceil(distance / (double) SPEED) + 6, MIN_LIFETIME, MAX_LIFETIME);
        
        TEBladeEntitySpellDrive drive = new TEBladeEntitySpellDrive(TEBladeThaumaturgeEntityTypes.SPELL_DRIVE.get(), level);
        drive.setPos(muzzle);
        drive.setDamage(0.0D);
        drive.setOwner(caster);
        drive.setShooter(caster);
        drive.setSpeed(SPEED);
        drive.setIsCritical(false);
        drive.setKnockBack(KnockBacks.cancel);
        drive.setRotationRoll(ctx.random().nextFloat() * 360.0F);
        drive.setLifetime(lifetime);
        drive.setColor(payload.look().color() & 0xFFFFFF);
        drive.shoot(heading.x, heading.y, heading.z, SPEED, 0.0F);
        drive.setPayload(payload);
        level.addFreshEntity(drive);
    }
}
