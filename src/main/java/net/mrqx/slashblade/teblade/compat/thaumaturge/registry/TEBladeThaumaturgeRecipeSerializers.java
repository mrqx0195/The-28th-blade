package net.mrqx.slashblade.teblade.compat.thaumaturge.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.mrqx.slashblade.teblade.compat.thaumaturge.recipe.BladeElementResearchRecipe;
import net.mrqx.slashblade.teblade.compat.thaumaturge.recipe.BladeReversionRecipe;
import net.mrqx.slashblade.teblade.registry.TEBladeRecipeSerializersRegistry;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class TEBladeThaumaturgeRecipeSerializers {
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BladeReversionRecipe>> BLADE_REVERSION =
        TEBladeRecipeSerializersRegistry.SERIALIZERS.register("blade_reversion", () -> BladeReversionRecipe.SERIALIZER);
    
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BladeElementResearchRecipe>> BLADE_ELEMENT_RESEARCH =
        TEBladeRecipeSerializersRegistry.SERIALIZERS.register("blade_element_research", () -> BladeElementResearchRecipe.SERIALIZER);
    
    public static void init() {
    }
}
