package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import com.leclowndu93150.thaumaturge.api.spell.behavior.SpellBehaviorType;
import com.leclowndu93150.thaumaturge.api.spell.cast.CastContext;
import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import com.leclowndu93150.thaumaturge.content.spell.carrier.CarrierPayload;
import com.leclowndu93150.thaumaturge.content.spell.delivery.AbstractCarrierDelivery;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.entity.TEBladeEntitySpellSummonedSword;

public class TEBladeSummonedSwordsDelivery extends AbstractCarrierDelivery {
    public static final MapCodec<TEBladeSummonedSwordsDelivery> CODEC = MapCodec.unit(TEBladeSummonedSwordsDelivery::new);
    
    public static final float SPEED = 3.0F;
    public static final double MUZZLE_OFFSET = 0.6D;
    private static final double LATERAL_STEP = 0.45D;
    private static final double VERTICAL_STEP = 0.18D;
    
    public TEBladeSummonedSwordsDelivery() {
    }
    
    @Override
    public SpellBehaviorType<?> type() {
        return TEBladeThaumaturgeSpellBehaviorTypes.PHANTOM_SWORDS.get();
    }
    
    @Override
    protected void launch(CastContext ctx, LivingEntity caster, CarrierPayload payload, SpellTarget origin) {
        ServerLevel level = ctx.level();
        TEBladeSpellAim.Aim aim = TEBladeSpellAim.resolve(ctx, origin);
        Vec3 look = aim.direction();
        Vec3 muzzle = caster.getEyePosition().add(look.scale(MUZZLE_OFFSET));
        Vec3 heading = TEBladeSpellAim.launch(muzzle, aim.point(), caster.getLookAngle());
        Vec3 side = sideOf(heading);
        int count = Mth.clamp(ctx.setting("count"), 1, 6);
        int tint = payload.look().color() & 0xFFFFFF;
        for (int i = 0; i < count; i++) {
            int tier = i / 2 + 1;
            double lateral = (i % 2 == 0 ? 1.0D : -1.0D) * LATERAL_STEP * tier;
            double vertical = VERTICAL_STEP * (i / 2.0);
            Vec3 spawn = muzzle.add(side.scale(lateral)).add(0.0D, vertical, 0.0D);
            
            TEBladeEntitySpellSummonedSword sword = new TEBladeEntitySpellSummonedSword(
                TEBladeThaumaturgeEntityTypes.SPELL_SUMMONED_SWORD.get(), level);
            sword.setPos(spawn);
            sword.setDamage(0.0D);
            sword.setColor(tint);
            sword.setRoll(0.0F);
            sword.setOwner(caster);
            sword.setShooter(caster);
            sword.shoot(heading.x, heading.y, heading.z, SPEED, 0.0F);
            sword.setPayload(payload);
            level.addFreshEntity(sword);
        }
    }
    
    private static Vec3 sideOf(Vec3 dir) {
        Vec3 side = new Vec3(-dir.z, 0.0D, dir.x);
        if (side.lengthSqr() < 1.0E-6D) {
            return new Vec3(1.0D, 0.0D, 0.0D);
        }
        return side.normalize();
    }
}
