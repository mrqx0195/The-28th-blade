package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SpecialEffectGates;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.mrqx.slashblade.teblade.utils.MovementSpeedBuff;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public final class Thunder extends SpecialEffect {
    private static final ResourceLocation SPEED_MODIFIER = TEBlade.prefix("thunder_speed");
    private static final double SPEED_BONUS = 0.08;
    private static final float CHAIN_DAMAGE_SCALE = 0.15F;
    
    public Thunder() {
        super(20, true, true);
    }
    
    @SubscribeEvent
    public static void onTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity user) || user.level().isClientSide()) {
            return;
        }
        if (!(user.getMainHandItem().getItem() instanceof ItemSlashBlade)) {
            if (MovementSpeedBuff.hasMovementSpeed(user, SPEED_MODIFIER)) {
                MovementSpeedBuff.clearMovementSpeed(user, SPEED_MODIFIER);
            }
            return;
        }
        if (SpecialEffectGates.isActive(user, TEBladeThaumaturgeSpecialEffects.THUNDER)) {
            MovementSpeedBuff.applyMovementSpeed(user, SPEED_MODIFIER, SPEED_BONUS);
        } else if (MovementSpeedBuff.hasMovementSpeed(user, SPEED_MODIFIER)) {
            MovementSpeedBuff.clearMovementSpeed(user, SPEED_MODIFIER);
        }
    }
    
    @SubscribeEvent
    public static void onHit(LivingDamageEvent.Post event) {
        DamageSource source = event.getSource();
        float damage = event.getNewDamage();
        if (source.is(TEBladeDamageTypesBootstrap.SECONDARY) || !AttackUtils.isDirectMeleeAttack(source)
            || !(source.getEntity() instanceof LivingEntity user) || !user.isSprinting()
            || !event.getEntity().isAlive()
            || !SpecialEffectGates.isActive(user, TEBladeThaumaturgeSpecialEffects.THUNDER)) {
            return;
        }
        Entity target = event.getEntity();
        target.hurt(user.damageSources().source(TEBladeDamageTypesBootstrap.SECONDARY_THUNDER), damage * CHAIN_DAMAGE_SCALE);
    }
}
