package net.mrqx.slashblade.teblade.utils;

import mods.flammpfeil.slashblade.recipe.SlashBladeIngredient;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.Map;

public final class ItemUtils {
    private static final Map<ResourceLocation, Ingredient> INGREDIENT_CACHE = new HashMap<>();
    
    public static boolean isBlade(ResourceKey<SlashBladeDefinition> slashBladeDefinition, ItemStack itemStack) {
        ResourceLocation key = slashBladeDefinition.location();
        if (!INGREDIENT_CACHE.containsKey(key)) {
            INGREDIENT_CACHE.put(key, SlashBladeIngredient.of(key));
        }
        return INGREDIENT_CACHE.get(key).test(itemStack);
    }
}
