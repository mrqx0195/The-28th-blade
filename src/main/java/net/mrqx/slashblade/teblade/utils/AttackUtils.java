package net.mrqx.slashblade.teblade.utils;

import mods.flammpfeil.slashblade.RegistryEvents;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiConsumer;

public final class AttackUtils {
    public static boolean isDirectMeleeAttack(DamageSource source) {
        return (source.is(DamageTypes.PLAYER_ATTACK)
            || source.is(DamageTypes.MOB_ATTACK))
            && source.getEntity() instanceof LivingEntity
            && Objects.equals(source.getEntity(), source.getDirectEntity());
    }
    
    public static boolean checkAndPerformSpecialEffect(DamageSource damageSource, DeferredHolder<SpecialEffect, SpecialEffect> specialEffect, BiConsumer<LivingEntity, ISlashBladeState> consumer) {
        if (damageSource.getEntity() instanceof LivingEntity livingEntity) {
            return checkAndPerformSpecialEffect(livingEntity, specialEffect, consumer);
        } else if (damageSource.getDirectEntity() instanceof LivingEntity livingEntity) {
            return checkAndPerformSpecialEffect(livingEntity, specialEffect, consumer);
        } else {
            return false;
        }
    }
    
    public static boolean checkAndPerformSpecialEffect(LivingEntity user, DeferredHolder<SpecialEffect, SpecialEffect> specialEffect, BiConsumer<LivingEntity, ISlashBladeState> consumer) {
        return BladeStateAccess.of(user.getMainHandItem())
            .map(state -> checkAndPerformSpecialEffect(user, state, specialEffect, consumer))
            .orElse(false);
    }
    
    public static boolean checkAndPerformSpecialEffect(@Nullable Entity user, ISlashBladeState state, DeferredHolder<SpecialEffect, SpecialEffect> specialEffect, BiConsumer<LivingEntity, ISlashBladeState> consumer) {
        if (user instanceof LivingEntity livingEntity
            && !livingEntity.level().isClientSide
            && state.hasSpecialEffect(specialEffect.getId())
            && specialEffect.get().isEffective(livingEntity)) {
            consumer.accept(livingEntity, state);
            return true;
        }
        return false;
    }
    
    @Nullable
    public static EntitySlashEffect doNoShadowAttack(LivingEntity living, double damage, int lifetime, float roll) {
        if (living.level().isClientSide()) {
            return null;
        }
        
        Vec3 pos = living.position().add(0.0D, (double) living.getEyeHeight() * 0.75D, 0.0D)
            .add(living.getLookAngle().scale(0.3f));
        
        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, living.getViewYRot(0)).scale(Vec3.ZERO.y))
            .add(VectorHelper.getVectorForRotation(0, living.getViewYRot(0) + 90).scale(Vec3.ZERO.z))
            .add(living.getLookAngle().scale(Vec3.ZERO.z));
        
        EntitySlashEffect jc = getNoShadowEffect(living, pos);
        
        jc.setDamage(damage);
        jc.setKnockBack(KnockBacks.cancel);
        jc.setRank(living.getData(CapabilityConcentrationRank.RANK_POINT).getRankLevel(living.level().getGameTime()));
        jc.setLifetime(lifetime);
        
        jc.setRotationRoll(roll);
        jc.setYRot(living.getYRot());
        jc.setXRot(0);
        
        living.level().addFreshEntity(jc);
        
        return jc;
    }
    
    private static EntitySlashEffect getNoShadowEffect(LivingEntity living, Vec3 pos) {
        EntitySlashEffect jc = new EntitySlashEffect(RegistryEvents.SlashEffect, living.level()) {
            
            @Override
            public SoundEvent getSlashSound() {
                return SoundEvents.BLAZE_HURT;
            }
            
            @Override
            protected void tryDespawn() {
                if (!this.level().isClientSide()) {
                    if (this.getLifetime() < this.tickCount) {
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F,
                            0.625F + 0.1f * this.random.nextFloat());
                        ((ServerLevel) this.level()).sendParticles(ParticleTypes.ENCHANTED_HIT, this.getX(),
                            this.getY(), this.getZ(), 16, 0.7, 0.7, 0.7, 0.02);
                        this.getAlreadyHits().stream().filter(Entity::isAlive).forEach(entity -> {
                            float yRot = this.getOwner() != null ? this.getOwner().getYRot() : 0;
                            entity.addDeltaMovement(new Vec3(
                                Math.sin(yRot * (float) Math.PI / 180.0F) * 0.5,
                                0.05D,
                                -Math.cos(yRot * (float) Math.PI / 180.0F) * 0.5));
                        });
                        this.remove(RemovalReason.DISCARDED);
                    }
                }
            }
        };
        jc.setPos(pos.x, pos.y, pos.z);
        
        jc.setOwner(living);
        
        int colorCode = BladeStateAccess.of(living.getMainHandItem())
            .map(ISlashBladeState::getColorCode).orElse(0xFFFFFF);
        jc.setColor(colorCode);
        
        jc.setMute(false);
        jc.setIsCritical(false);
        
        return jc;
    }
    
    public static int getPowerLevel(LivingEntity user) {
        ItemStack blade = user.getMainHandItem();
        if (blade.isEmpty()) {
            return 0;
        }
        return blade.getEnchantmentLevel(user.level().registryAccess().holderOrThrow(Enchantments.POWER));
    }
}
