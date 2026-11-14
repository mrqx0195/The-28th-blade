package net.mrqx.slashblade.teblade.compat.thaumaturge.blade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeDataComponents;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class ExternalBladeEffects {
    public static Set<ResourceLocation> read(ItemStack stack) {
        Set<ResourceLocation> effects = stack.get(TEBladeThaumaturgeDataComponents.EXTERNAL_EFFECTS.get());
        return effects == null ? Set.of() : effects;
    }
    
    public static void write(ItemStack stack, Set<ResourceLocation> effects) {
        if (effects.isEmpty()) {
            stack.remove(TEBladeThaumaturgeDataComponents.EXTERNAL_EFFECTS.get());
            return;
        }
        stack.set(TEBladeThaumaturgeDataComponents.EXTERNAL_EFFECTS.get(), Set.copyOf(effects));
    }
    
    public static void edit(ItemStack stack, Consumer<Set<ResourceLocation>> mutation) {
        Set<ResourceLocation> recorded = read(stack);
        Set<ResourceLocation> edited = new HashSet<>(recorded);
        mutation.accept(edited);
        if (!edited.equals(recorded)) {
            write(stack, edited);
        }
    }
}
