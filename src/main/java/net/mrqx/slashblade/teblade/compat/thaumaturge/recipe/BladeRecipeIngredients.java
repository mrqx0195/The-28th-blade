package net.mrqx.slashblade.teblade.compat.thaumaturge.recipe;

import mods.flammpfeil.slashblade.recipe.SlashBladeIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;

public final class BladeRecipeIngredients {
    public static Ingredient exact(ThaumaturgeBladeForms.Form form) {
        return SlashBladeIngredient.of(TEBlade.prefix(form.path()));
    }
}