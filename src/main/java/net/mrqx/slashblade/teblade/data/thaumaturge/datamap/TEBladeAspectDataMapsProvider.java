package net.mrqx.slashblade.teblade.data.thaumaturge.datamap;

import com.leclowndu93150.thaumaturge.api.aspect.*;
import mods.flammpfeil.slashblade.RegistryEvents;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.mrqx.sbr_core.registry.MrqxEntityTypesRegistry;
import net.mrqx.slashblade.teblade.data.DataGen;
import net.mrqx.slashblade.teblade.data.thaumaturge.aspect.TEBladeAspects;
import net.mrqx.truepower.TruePowerMod;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public final class TEBladeAspectDataMapsProvider extends DataMapProvider {
    public TEBladeAspectDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }
    
    @Override
    protected void gather(HolderLookup.Provider provider) {
        HolderGetter<IAspect> aspects = provider.lookupOrThrow(IAspect.REGISTRY_KEY);
        items(aspects);
        entities(aspects);
    }
    
    private void items(HolderGetter<IAspect> aspects) {
        Builder<AspectList, Item> builder = this.builder(AspectDataMaps.BASE_ASPECTS);
        builder.conditions(DataGen.THAUMATURGE_CONDITION);
        
        add(builder, SlashBladeItems.PROUDSOUL.get(),
            AspectList.of(aspect(aspects, TEBladeAspects.PROUD_SOUL, 5)));
        add(builder, SlashBladeItems.PROUDSOUL_TINY.get(),
            AspectList.of(aspect(aspects, TEBladeAspects.PROUD_SOUL, 1)));
        add(builder, SlashBladeItems.PROUDSOUL_INGOT.get(), AspectList.of(
            aspect(aspects, TEBladeAspects.PROUD_SOUL, 10),
            aspect(aspects, TTAspects.METALLUM, 15)));
        add(builder, SlashBladeItems.PROUDSOUL_SPHERE.get(), AspectList.of(
            aspect(aspects, TEBladeAspects.PROUD_SOUL, 15),
            aspect(aspects, TTAspects.METALLUM, 10),
            aspect(aspects, TEBladeAspects.SLASH_BLADE, 5)));
        add(builder, SlashBladeItems.PROUDSOUL_CRYSTAL.get(), AspectList.of(
            aspect(aspects, TEBladeAspects.PROUD_SOUL, 20),
            aspect(aspects, TTAspects.METALLUM, 5),
            aspect(aspects, TEBladeAspects.SLASH_BLADE, 10)));
        add(builder, SlashBladeItems.PROUDSOUL_TRAPEZOHEDRON.get(), AspectList.of(
            aspect(aspects, TEBladeAspects.PROUD_SOUL, 25),
            aspect(aspects, TEBladeAspects.SLASH_BLADE, 15)));
        add(builder, SlashBladeItems.SLASHBLADE.get(), AspectList.of(
            aspect(aspects, TEBladeAspects.PROUD_SOUL, 20),
            aspect(aspects, TEBladeAspects.SLASH_BLADE, 20),
            aspect(aspects, TEBladeAspects.AKURYO, 20)));
    }
    
    private void entities(HolderGetter<IAspect> aspects) {
        Builder<AspectList, EntityType<?>> builder = builder(AspectDataMaps.ENTITY_ASPECTS);
        
        add(builder, RegistryEvents.SlashEffect, AspectList.of(aspect(aspects, TTAspects.AVERSIO, 5)));
        add(builder, MrqxEntityTypesRegistry.ADVANCE_SLASH_EFFECT.get(), AspectList.of(aspect(aspects, TTAspects.AVERSIO, 5)));
        add(builder, RegistryEvents.SummonedSword, sword(aspects));
        add(builder, RegistryEvents.SpiralSwords, sword(aspects));
        add(builder, RegistryEvents.BlisteringSwords, sword(aspects));
        add(builder, RegistryEvents.StormSwords, sword(aspects));
        add(builder, RegistryEvents.HeavyRainSwords, sword(aspects));
        add(builder, MrqxEntityTypesRegistry.AIR_TRICK_SUMMONED_SWORD.get(), sword(aspects));
        add(builder, TruePowerMod.RegistryEvents.BlastSummonedSword, sword(aspects));
        add(builder, RegistryEvents.JudgementCut, AspectList.of(
            aspect(aspects, TTAspects.VOLATUS, 10),
            aspect(aspects, TTAspects.PERDITIO, 10),
            aspect(aspects, TEBladeAspects.PROUD_SOUL, 5)));
        add(builder, RegistryEvents.Drive, AspectList.of(
            aspect(aspects, TTAspects.AVERSIO, 10),
            aspect(aspects, TEBladeAspects.PROUD_SOUL, 5)));
    }
    
    private static AspectList sword(HolderGetter<IAspect> aspects) {
        return AspectList.of(
            aspect(aspects, TTAspects.SPIRITUS, 5),
            aspect(aspects, TEBladeAspects.PROUD_SOUL, 5));
    }
    
    private static void add(Builder<AspectList, Item> builder, Item item, AspectList value) {
        builder.add(BuiltInRegistries.ITEM.wrapAsHolder(item), value, false);
    }
    
    private static void add(Builder<AspectList, EntityType<?>> builder, EntityType<?> type, AspectList value) {
        builder.add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(type), value, false);
    }
    
    private static AspectInstance aspect(HolderGetter<IAspect> aspects, ResourceKey<IAspect> aspect, int amount) {
        return new AspectInstance(aspects.getOrThrow(aspect), amount);
    }
    
    @Override
    public String getName() {
        return "TEBlade Aspect Data Maps";
    }
}
