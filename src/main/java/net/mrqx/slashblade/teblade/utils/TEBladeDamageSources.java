package net.mrqx.slashblade.teblade.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;

import javax.annotation.Nullable;

public final class TEBladeDamageSources {
    public static DamageSource witnessRainbow(Level level, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 damageSourcePosition) {
        return new DamageSource(holder(level, TEBladeDamageTypesBootstrap.WITNESS_RAINBOW), directEntity, causingEntity, damageSourcePosition);
    }
    
    private static Holder<DamageType> holder(Level level, ResourceKey<DamageType> key) {
        return level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key);
    }
}
