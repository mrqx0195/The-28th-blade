package net.mrqx.slashblade.teblade.data.thaumaturge;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.spell.affinity.AspectAffinity;
import com.leclowndu93150.thaumaturge.api.spell.part.SpellPart;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.event.BladeObservation;
import net.mrqx.slashblade.teblade.data.thaumaturge.aspect.TEBladeAspectsBootstrap;
import net.mrqx.slashblade.teblade.data.thaumaturge.observation.TEBladeObservationBootstrap;
import net.mrqx.slashblade.teblade.data.thaumaturge.spell.TEBladeSpellAffinitiesBootstrap;
import net.mrqx.slashblade.teblade.data.thaumaturge.spell.TEBladeSpellPartsBootstrap;
import net.mrqx.slashblade.teblade.data.thaumaturge.datamap.TEBladeAspectDataMapsProvider;
import net.mrqx.slashblade.teblade.data.thaumaturge.datamap.TEBladeInfernalBonusProvider;
import net.mrqx.slashblade.teblade.data.thaumaturge.research.TEBladeResearchDataGen;
import net.mrqx.slashblade.teblade.data.thaumaturge.slashblade.TEBladeSlashBladesProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class TEBladeThaumaturgeProvider extends DatapackBuiltinEntriesProvider {
    public TEBladeThaumaturgeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, registries(), Set.of(TEBlade.MODID));
    }
    
    public static void addRegistries(RegistrySetBuilder builder) {
        builder.add(IAspect.REGISTRY_KEY, TEBladeAspectsBootstrap::bootstrap);
        builder.add(SpellPart.REGISTRY_KEY, TEBladeSpellPartsBootstrap::bootstrap);
        builder.add(AspectAffinity.REGISTRY_KEY, TEBladeSpellAffinitiesBootstrap::bootstrap);
        builder.add(BladeObservation.REGISTRY_KEY, TEBladeObservationBootstrap::bootstrap);
    }
    
    private static RegistrySetBuilder registries() {
        RegistrySetBuilder builder = new RegistrySetBuilder();
        addRegistries(builder);
        return builder;
    }
    
    public static void registerProviders(GatherDataEvent event) {
        event.createProvider(TEBladeThaumaturgeProvider::new);
        event.createProvider(TEBladeAspectDataMapsProvider::new);
        event.createProvider(TEBladeInfernalBonusProvider::new);
        event.createProvider(TEBladeSlashBladesProvider::new);
        TEBladeResearchDataGen.registerProviders(event);
    }
    
    @Override
    public String getName() {
        return "TEBlade Thaumaturge Registry";
    }
}