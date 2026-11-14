package net.mrqx.slashblade.teblade.compat.thaumaturge.recipe;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import net.minecraft.resources.ResourceKey;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;

import java.util.List;

public final class BladeElementRecipeCosts {
    public record AspectCost(ResourceKey<IAspect> aspect, int amount) {
    }
    
    public record Cost(List<AspectCost> crystals, List<AspectCost> aspects, int instability) {
    }
    
    public static Cost forElement(ThaumaturgeBladeForms.Element element) {
        return switch (element) {
            case THUNDER -> new Cost(
                List.of(new AspectCost(TTAspects.AER, 2)),
                List.of(new AspectCost(TTAspects.AER, 20), new AspectCost(TTAspects.AVERSIO, 20),
                    new AspectCost(TTAspects.MOTUS, 20)),
                2);
            case SKY -> new Cost(
                List.of(new AspectCost(TTAspects.AER, 2)),
                List.of(new AspectCost(TTAspects.MOTUS, 40), new AspectCost(TTAspects.AER, 20),
                    new AspectCost(TTAspects.POTENTIA, 20)),
                2);
            case TIDE -> new Cost(
                List.of(new AspectCost(TTAspects.AQUA, 2)),
                List.of(new AspectCost(TTAspects.AQUA, 40), new AspectCost(TTAspects.HERBA, 20)),
                2);
            case GROWTH -> new Cost(
                List.of(new AspectCost(TTAspects.ORDO, 1), new AspectCost(TTAspects.PERDITIO, 1)),
                List.of(new AspectCost(TTAspects.HERBA, 24), new AspectCost(TTAspects.ORDO, 24),
                    new AspectCost(TTAspects.PERDITIO, 24)),
                3);
            case FLAME -> new Cost(
                List.of(new AspectCost(TTAspects.IGNIS, 2)),
                List.of(new AspectCost(TTAspects.IGNIS, 20), new AspectCost(TTAspects.METALLUM, 20),
                    new AspectCost(TTAspects.SENSUS, 20)),
                2);
            case EARTH -> new Cost(
                List.of(new AspectCost(TTAspects.TERRA, 2)),
                List.of(new AspectCost(TTAspects.TERRA, 40), new AspectCost(TTAspects.FABRICO, 20)),
                2);
        };
    }
}
