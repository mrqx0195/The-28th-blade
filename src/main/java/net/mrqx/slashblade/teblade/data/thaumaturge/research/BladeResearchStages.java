package net.mrqx.slashblade.teblade.data.thaumaturge.research;

import com.leclowndu93150.thaumaturge.api.research.IResearchStage;
import com.leclowndu93150.thaumaturge.content.research.ResearchStage;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.slashblade.teblade.compat.thaumaturge.TEBladeResearchKeys;
import net.mrqx.slashblade.teblade.data.thaumaturge.recipe.TEBladeThaumaturgeRecipes;

import java.util.List;
import java.util.Optional;

public final class BladeResearchStages {
    public static List<IResearchStage> single(ResourceLocation id) {
        return List.of(stage(id, 0, List.of(), 0));
    }
    
    public static List<IResearchStage> pages(ResourceLocation id, List<ResourceLocation> recipes, int warp) {
        return List.of(stage(id, 0, List.of(), 0), stage(id, 1, recipes, warp));
    }
    
    public static List<IResearchStage> terminal() {
        return List.of(stage(BladeResearchEntries.PRIMORDIAL_CURSE, 0, List.of(), 0),
            stage(BladeResearchEntries.PRIMORDIAL_CURSE, 1, List.of(TEBladeThaumaturgeRecipes.TERMINAL), 2),
            stage(BladeResearchEntries.PRIMORDIAL_CURSE, 2, List.of(), 0));
    }
    
    private static ResearchStage stage(ResourceLocation id, int index, List<ResourceLocation> recipes, int warp) {
        return new ResearchStage(TEBladeResearchKeys.stage(id, index), recipes, Optional.empty(),
            List.of(), List.of(), List.of(), List.of(), List.of(), warp);
    }
}
