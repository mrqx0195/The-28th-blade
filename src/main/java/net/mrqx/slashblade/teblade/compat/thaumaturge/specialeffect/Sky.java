package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeAttachmentTypes;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SpecialEffectGates;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.mrqx.slashblade.teblade.utils.MovementSpeedBuff;
import net.mrqx.slashblade.teblade.utils.SlashEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Objects;

public final class Sky extends SpecialEffect {
    private static final ResourceLocation SPEED_MODIFIER = TEBlade.prefix("sky_speed");
    private static final int WINDOW_TICKS = 80;
    private static final float DAMAGE_MULTIPLIER = 1.08F;
    private static final double SPEED_BONUS = 0.15;
    
    public Sky() {
        super(25, true, true);
    }
    
    public static void onConfirmedCast(LivingEntity actor, boolean perfect) {
        if (!actor.level().isClientSide() && perfect
            && SpecialEffectGates.isActive(actor, TEBladeThaumaturgeSpecialEffects.SKY)) {
            actor.setData(TEBladeThaumaturgeAttachmentTypes.SKY_WINDOW, actor.tickCount + WINDOW_TICKS);
        }
    }
    
    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Pre event) {
        DamageSource source = event.getSource();
        if (source.is(TEBladeDamageTypesBootstrap.SECONDARY) || !(source.getEntity() instanceof LivingEntity user)
            || !SpecialEffectGates.isActive(user, TEBladeThaumaturgeSpecialEffects.SKY)) {
            return;
        }
        int until = user.getData(TEBladeThaumaturgeAttachmentTypes.SKY_WINDOW);
        if (user.tickCount >= until) {
            return;
        }
        boolean bladeHit = (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK))
            && Objects.equals(source.getDirectEntity(), user);
        float damage = event.getNewDamage();
        if (bladeHit || SlashEntities.isOwnedBy(source.getDirectEntity(), user)) {
            event.setNewDamage(damage * DAMAGE_MULTIPLIER);
        }
    }
    
    @SubscribeEvent
    public static void onTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity user) || user.level().isClientSide()) {
            return;
        }
        int until = user.getData(TEBladeThaumaturgeAttachmentTypes.SKY_WINDOW);
        if (until == 0) {
            return;
        }
        if (user.tickCount < until && SpecialEffectGates.isActive(user, TEBladeThaumaturgeSpecialEffects.SKY)) {
            MovementSpeedBuff.applyMovementSpeed(user, SPEED_MODIFIER, SPEED_BONUS);
            return;
        }
        user.setData(TEBladeThaumaturgeAttachmentTypes.SKY_WINDOW, 0);
        MovementSpeedBuff.clearMovementSpeed(user, SPEED_MODIFIER);
    }
}
