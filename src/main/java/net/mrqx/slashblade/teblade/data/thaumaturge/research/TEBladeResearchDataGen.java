package net.mrqx.slashblade.teblade.data.thaumaturge.research;

import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import com.leclowndu93150.thaumaturge.api.research.IResearchEntry;
import com.leclowndu93150.thaumaturge.api.research.scan.ScanEntry;
import com.leclowndu93150.thaumaturge.content.research.ResearchCategory;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.data.thaumaturge.aspect.TEBladeAspects;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Optional;
import java.util.Set;

public final class TEBladeResearchDataGen {
    private static final ResourceLocation SLASHBLADE_SCAN = TEBlade.prefix("slashblade");
    
    public static void registerProviders(GatherDataEvent event) {
        RegistrySetBuilder builder = new RegistrySetBuilder()
            .add(IResearchCategory.REGISTRY_KEY, TEBladeResearchDataGen::categories)
            .add(IResearchEntry.REGISTRY_KEY, BladeResearchEntries::bootstrap)
            .add(ScanEntry.REGISTRY_KEY, TEBladeResearchDataGen::scanEntries);
        event.getGenerator().addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
            event.getGenerator().getPackOutput(), event.getLookupProvider(), builder, Set.of(TEBlade.MODID)) {
            @Override
            public String getName() {
                return "TEBlade Thaumaturge Research";
            }
        });
    }
    
    private static void categories(BootstrapContext<IResearchCategory> context) {
        HolderGetter<IAspect> aspects = context.lookup(IAspect.REGISTRY_KEY);
        AspectList formula = AspectList.of(
            new AspectInstance(aspects.getOrThrow(TEBladeAspects.SLASH_BLADE), 6),
            new AspectInstance(aspects.getOrThrow(TEBladeAspects.PROUD_SOUL), 4),
            new AspectInstance(aspects.getOrThrow(TTAspects.INSTRUMENTUM), 3),
            new AspectInstance(aspects.getOrThrow(TTAspects.PRAECANTATIO), 2));
        context.register(BladeResearchEntries.CATEGORY, new ResearchCategory(
            Optional.of(BladeResearchEntries.UNLOCK), formula,
            TTIds.rl("textures/research/cat_infusion.png"),
            TTIds.rl("textures/gui/gui_research_back_3.png"),
            Optional.of(TTIds.rl("textures/gui/gui_research_back_over.png")), 1028));
    }
    
    private static void scanEntries(BootstrapContext<ScanEntry> context) {
        Holder.Reference<Item> item = context.lookup(Registries.ITEM)
            .getOrThrow(SlashBladeItems.SLASHBLADE.getKey());
        context.register(ResourceKey.create(ScanEntry.REGISTRY_KEY, SLASHBLADE_SCAN),
            new ScanEntry(BladeResearchEntries.SCANNED, Optional.empty(),
                Optional.of(HolderSet.direct(item)), Optional.empty()));
    }
}
