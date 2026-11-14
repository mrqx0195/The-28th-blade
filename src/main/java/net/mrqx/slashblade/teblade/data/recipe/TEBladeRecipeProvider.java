package net.mrqx.slashblade.teblade.data.recipe;

import mods.flammpfeil.slashblade.data.builtin.SlashBladeBuiltInRegistry;
import mods.flammpfeil.slashblade.data.tag.SlashBladeItemTags;
import mods.flammpfeil.slashblade.recipe.RequestDefinition;
import mods.flammpfeil.slashblade.recipe.SlashBladeIngredient;
import mods.flammpfeil.slashblade.recipe.SlashBladeShapedRecipeBuilder;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.mrqx.slashblade.teblade.compat.TEBladeCompatManager;
import net.mrqx.slashblade.teblade.data.slashblade.TEBladeDefinitionsProvider;
import net.mrqx.slashblade.teblade.data.thaumaturge.recipe.TEBladeThaumaturgeRecipes;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public final class TEBladeRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public final CompletableFuture<HolderLookup.Provider> lookup;
    
    public TEBladeRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
        this.lookup = registries;
    }
    
    @Override
    protected void buildRecipes(RecipeOutput output) {
        if (TEBladeCompatManager.THAUMATURGE) {
            TEBladeThaumaturgeRecipes.generate(output, this.lookup.join());
        }
        SlashBladeShapedRecipeBuilder.shaped(TEBladeDefinitionsProvider.FRAGMENTED_SLASHBLADE.location())
            .pattern(" I ")
            .pattern("PBP")
            .define('I', ItemTags.ANVIL)
            .define('P', SlashBladeItemTags.PROUD_SOULS)
            .define('B', SlashBladeIngredient.blankNameless())
            .unlockedBy(getHasName(SlashBladeItems.SLASHBLADE.get()), has(SlashBladeItems.SLASHBLADE.get()))
            .save(output);
        
        SlashBladeShapedRecipeBuilder.shaped(TEBladeDefinitionsProvider.FOX_LOOKING_FOR_EDGE_OF_GROUNDEDGE.location())
            .pattern("ACD")
            .pattern("BSW")
            .pattern("EFG")
            .define('A', Items.ICE)
            .define('C', Items.BLUE_ICE)
            .define('D', Items.PACKED_ICE)
            .define('E', Items.SNOWBALL)
            .define('F', Items.SNOW_BLOCK)
            .define('G', Items.SNOW)
            .define('S', SlashBladeItems.PROUDSOUL_TRAPEZOHEDRON.get())
            .define('B', SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                .name(SlashBladeBuiltInRegistry.FOX_BLACK.location())
                .proudSoul(6000)
                .refineCount(80)
                .addEnchantment(new EnchantmentDefinition(getEnchantmentID(Enchantments.FROST_WALKER), 2))
                .build()))
            .define('W', SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                .name(SlashBladeBuiltInRegistry.FOX_WHITE.location())
                .proudSoul(6000)
                .refineCount(50)
                .addEnchantment(new EnchantmentDefinition(getEnchantmentID(Enchantments.FROST_WALKER), 2))
                .build()))
            .unlockedBy(getHasName(SlashBladeItems.SLASHBLADE.get()), has(SlashBladeItems.SLASHBLADE.get()))
            .save(output);
    }
    
    public Holder<Enchantment> getEnchantmentID(ResourceKey<Enchantment> enchantmentKey) {
        return this.lookup.join().lookup(Registries.ENCHANTMENT).orElseThrow().getOrThrow(enchantmentKey);
    }
}
