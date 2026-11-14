package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.entity.*;
import net.mrqx.slashblade.teblade.registry.TEBladeEntityTypesRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public final class TEBladeThaumaturgeEntityTypes {
    public static final DeferredHolder<EntityType<?>, EntityType<TEBladeEntitySpellDrive>> SPELL_DRIVE = register(
        "spell_drive",
        () -> EntityType.Builder.of(TEBladeEntitySpellDrive::new, MobCategory.MISC)
            .sized(3.0F, 3.0F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<TEBladeEntitySpellSummonedSword>> SPELL_SUMMONED_SWORD = register(
        "spell_summoned_sword",
        () -> EntityType.Builder.of(TEBladeEntitySpellSummonedSword::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<TEBladeEntitySpellJudgementCut>> SPELL_JUDGEMENT_CUT = register(
        "spell_judgement_cut",
        () -> EntityType.Builder.of(TEBladeEntitySpellJudgementCut::new, MobCategory.MISC)
            .sized(2.5F, 2.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<TEBladeEntitySpiral>> SPIRAL_SWORDS = register(
        "spiral_swords",
        () -> EntityType.Builder.of(TEBladeEntitySpiral::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<TEBladeEntityStorm>> STORM_SWORDS = register(
        "storm_swords",
        () -> EntityType.Builder.of(TEBladeEntityStorm::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<TEBladeEntityBlistering>> BLISTERING_SWORDS = register(
        "blistering_swords",
        () -> EntityType.Builder.of(TEBladeEntityBlistering::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<TEBladeEntityHeavyRain>> HEAVY_RAIN_SWORDS = register(
        "heavy_rain_swords",
        () -> EntityType.Builder.of(TEBladeEntityHeavyRain::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
        return TEBladeEntityTypesRegister.ENTITY_TYPES.register(name, () -> builder.get().build(TEBlade.prefix(name).toString()));
    }
    
    public static void init() {
    }
}
