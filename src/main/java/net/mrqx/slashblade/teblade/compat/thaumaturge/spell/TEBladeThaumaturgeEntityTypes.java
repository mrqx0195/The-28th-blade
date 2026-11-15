package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.entity.*;
import net.mrqx.slashblade.teblade.registry.TEBladeEntityTypesRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public final class TEBladeThaumaturgeEntityTypes {
    public static final DeferredHolder<EntityType<?>, EntityType<EntityThaumaturgeSpellDrive>> SPELL_DRIVE = register(
        "spell_drive",
        () -> EntityType.Builder.of(EntityThaumaturgeSpellDrive::new, MobCategory.MISC)
            .sized(3.0F, 3.0F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<EntityThaumaturgeSummonedSword>> SPELL_SUMMONED_SWORD = register(
        "spell_summoned_sword",
        () -> EntityType.Builder.of(EntityThaumaturgeSummonedSword::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<EntityThaumaturgeSpellJudgementCut>> SPELL_JUDGEMENT_CUT = register(
        "spell_judgement_cut",
        () -> EntityType.Builder.of(EntityThaumaturgeSpellJudgementCut::new, MobCategory.MISC)
            .sized(2.5F, 2.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<EntityThaumaturgeSpiralSwords>> THAUMATURGE_SPIRAL_SWORDS = register(
        "thaumaturge_spiral_swords",
        () -> EntityType.Builder.of(EntityThaumaturgeSpiralSwords::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<EntityThaumaturgeStormSwords>> THAUMATURGE_STORM_SWORDS = register(
        "thaumaturge_storm_swords",
        () -> EntityType.Builder.of(EntityThaumaturgeStormSwords::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<EntityThaumaturgeBlisteringSwords>> THAUMATURGE_BLISTERING_SWORDS = register(
        "thaumaturge_blistering_swords",
        () -> EntityType.Builder.of(EntityThaumaturgeBlisteringSwords::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    public static final DeferredHolder<EntityType<?>, EntityType<EntityThaumaturgeHeavyRainSwords>> THAUMATURGE_HEAVY_RAIN_SWORDS = register(
        "thaumaturge_heavy_rain_swords",
        () -> EntityType.Builder.of(EntityThaumaturgeHeavyRainSwords::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    
    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
        return TEBladeEntityTypesRegister.ENTITY_TYPES.register(name, () -> builder.get().build(TEBlade.prefix(name).toString()));
    }
    
    public static void init() {
    }
}
