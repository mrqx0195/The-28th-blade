package net.mrqx.slashblade.teblade.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.mrqx.slashblade.teblade.TEBlade;

public final class TEBladeDamageTypesBootstrap {
    public static final ResourceKey<DamageType>
        WITNESS_RAINBOW = ResourceKey.create(Registries.DAMAGE_TYPE, TEBlade.prefix("witness_rainbow"));
    
    public static final ResourceKey<DamageType>
        SECONDARY_THUNDER = ResourceKey.create(Registries.DAMAGE_TYPE, TEBlade.prefix("secondary_thunder"));
    
    public static final ResourceKey<DamageType>
        SECONDARY_FLAME = ResourceKey.create(Registries.DAMAGE_TYPE, TEBlade.prefix("secondary_flame"));
    
    public static final TagKey<DamageType> SECONDARY = TagKey.create(Registries.DAMAGE_TYPE, TEBlade.prefix("secondary"));
    
    public static void bootstrap(BootstrapContext<DamageType> context) {
        registerDamageType(context, WITNESS_RAINBOW, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 7);
        registerDamageType(context, SECONDARY_THUNDER, DamageScaling.NEVER, 0);
        registerDamageType(context, SECONDARY_FLAME, DamageScaling.NEVER, 0);
    }
    
    private static void registerDamageType(BootstrapContext<DamageType> context, ResourceKey<DamageType> damageType,
                                           DamageScaling scaling, float pExhaustion) {
        context.register(damageType, new DamageType(damageType.location().getNamespace() + "." + damageType.location().getPath(),
            scaling, pExhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT));
    }
}
