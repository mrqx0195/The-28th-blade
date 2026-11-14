package net.mrqx.slashblade.teblade.data.thaumaturge.recipe;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.content.infusion.InfusionRecipe;
import com.leclowndu93150.thaumaturge.data.recipe.builders.CrucibleRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.InfusionRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.workbench.ArcaneWorkbenchShapedRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.workbench.ArcaneWorkbenchShapelessRecipeBuilder;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItemTags;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import mods.flammpfeil.slashblade.data.builtin.SlashBladeBuiltInRegistry;
import mods.flammpfeil.slashblade.recipe.RequestDefinition;
import mods.flammpfeil.slashblade.recipe.SlashBladeIngredient;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.compat.thaumaturge.recipe.BladeElementRecipeCosts;
import net.mrqx.slashblade.teblade.compat.thaumaturge.recipe.BladeElementResearchRecipe;
import net.mrqx.slashblade.teblade.compat.thaumaturge.recipe.BladeRecipeIngredients;
import net.mrqx.slashblade.teblade.compat.thaumaturge.recipe.BladeReversionRecipe;
import net.mrqx.slashblade.teblade.data.slashblade.TEBladeDefinitionsProvider;
import net.mrqx.slashblade.teblade.data.thaumaturge.aspect.TEBladeAspects;
import net.mrqx.slashblade.teblade.data.thaumaturge.research.BladeResearchEntries;
import net.mrqx.slashblade.teblade.data.thaumaturge.slashblade.BladeDefinitions;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("SameParameterValue")
public final class TEBladeThaumaturgeRecipes {
    public static final ResourceLocation TERMINAL = TEBlade.prefix("infusion/primordial_curse");
    private static final int TERMINAL_STAGE = 1;
    private static final int THEME_ASPECT_TOTAL = 20;
    
    @SuppressWarnings("NotNullFieldNotInitialized")
    private static HolderLookup.RegistryLookup<Item> items;
    @SuppressWarnings("NotNullFieldNotInitialized")
    private static HolderLookup.RegistryLookup<IAspect> aspects;
    
    public static ResourceLocation materialId(ThaumaturgeBladeForms.Form form) {
        return TEBlade.prefix("arcane/" + form.path());
    }
    
    public static ResourceLocation rodaiId(ThaumaturgeBladeForms.Family family) {
        return TEBlade.prefix("infusion/" + ThaumaturgeBladeForms.base(family).path());
    }
    
    public static ResourceLocation elementId(ThaumaturgeBladeForms.Family family, ThaumaturgeBladeForms.Element element) {
        return TEBlade.prefix("infusion/" + ThaumaturgeBladeForms.variant(family, element).path());
    }
    
    public static ResourceLocation reversionId(ThaumaturgeBladeForms.Family family,
                                               ThaumaturgeBladeForms.Element element) {
        return TEBlade.prefix("infusion/revert_" + ThaumaturgeBladeForms.variant(family, element).path());
    }
    
    public static List<ResourceLocation> elementIds(ThaumaturgeBladeForms.Element element) {
        return Arrays.stream(ThaumaturgeBladeForms.Family.values())
            .map(family -> elementId(family, element))
            .collect(Collectors.toCollection(() -> new ArrayList<>(ThaumaturgeBladeForms.Family.values().length)));
    }
    
    public static List<ResourceLocation> reversionIds() {
        List<ResourceLocation> ids = new ArrayList<>(
            ThaumaturgeBladeForms.Family.values().length * ThaumaturgeBladeForms.Element.values().length);
        for (ThaumaturgeBladeForms.Family family : ThaumaturgeBladeForms.Family.values()) {
            for (ThaumaturgeBladeForms.Element element : ThaumaturgeBladeForms.Element.values()) {
                ids.add(reversionId(family, element));
            }
        }
        return ids;
    }
    
    public static void generate(RecipeOutput output, HolderLookup.Provider registries) {
        TEBladeThaumaturgeRecipes.items = registries.lookupOrThrow(Registries.ITEM);
        TEBladeThaumaturgeRecipes.aspects = registries.lookupOrThrow(IAspect.REGISTRY_KEY);
        crucibleRecipes(output);
        
        baseSlashBlade(output, registries);
        rodai(output);
        elements(output);
        reversions(output);
        terminal(output);
    }
    
    private static void crucibleRecipes(RecipeOutput output) {
        crucible(SlashBladeItems.PROUDSOUL.get().getDefaultInstance(), Ingredient.of(SlashBladeItems.PROUDSOUL_TINY.get()))
            .aspect(TEBladeAspects.PROUD_SOUL, 4)
            .gate(new ResearchGate(BladeResearchEntries.UNLOCK, Optional.empty(), false))
            .save(output);
        crucible(SlashBladeItems.PROUDSOUL_INGOT.get().getDefaultInstance(), Ingredient.of(SlashBladeItems.PROUDSOUL.get()))
            .aspect(TEBladeAspects.PROUD_SOUL, 5)
            .aspect(TTAspects.METALLUM, 15)
            .gate(new ResearchGate(BladeResearchEntries.UNLOCK, Optional.empty(), false))
            .save(output);
        crucible(SlashBladeItems.PROUDSOUL_SPHERE.get().getDefaultInstance(), Ingredient.of(SlashBladeItems.PROUDSOUL_INGOT.get()))
            .aspect(TEBladeAspects.PROUD_SOUL, 5)
            .aspect(TEBladeAspects.SLASH_BLADE, 5)
            .gate(new ResearchGate(BladeResearchEntries.UNLOCK, Optional.empty(), false))
            .save(output);
        crucible(SlashBladeItems.PROUDSOUL_CRYSTAL.get().getDefaultInstance(), Ingredient.of(SlashBladeItems.PROUDSOUL_SPHERE.get()))
            .aspect(TEBladeAspects.PROUD_SOUL, 5)
            .aspect(TEBladeAspects.SLASH_BLADE, 5)
            .gate(new ResearchGate(BladeResearchEntries.UNLOCK, Optional.empty(), false))
            .save(output);
        crucible(SlashBladeItems.PROUDSOUL_TRAPEZOHEDRON.get().getDefaultInstance(), Ingredient.of(SlashBladeItems.PROUDSOUL_CRYSTAL.get()))
            .aspect(TEBladeAspects.PROUD_SOUL, 5)
            .aspect(TEBladeAspects.SLASH_BLADE, 5)
            .gate(new ResearchGate(BladeResearchEntries.UNLOCK, Optional.empty(), false))
            .save(output);
    }
    
    private static void baseSlashBlade(RecipeOutput output, HolderLookup.Provider registries) {
        arcaneShaped(RecipeCategory.COMBAT, BladeDefinitions.create(ThaumaturgeBladeForms.GREATWOOD_DUMMY).getBlade(), 10)
            .pattern(" WP")
            .pattern("WBW")
            .pattern("PW ")
            .define('W', TTItemTags.GREATWOOD_LOGS)
            .define('P', SlashBladeItems.PROUDSOUL_TINY.get())
            .define('B', SlashBladeItems.SLASHBLADE_WOOD.get())
            .gate(new ResearchGate(BladeResearchEntries.UNLOCK, Optional.empty(), false))
            .save(output, materialId(ThaumaturgeBladeForms.GREATWOOD_DUMMY));
        
        arcaneShaped(RecipeCategory.COMBAT, BladeDefinitions.create(ThaumaturgeBladeForms.SILVERWOOD_SAYA).getBlade(), 20)
            .pattern(" WP")
            .pattern("WPW")
            .pattern("BW ")
            .define('W', TTItemTags.SILVERWOOD_LOGS)
            .define('P', SlashBladeItems.PROUDSOUL.get())
            .define('B', SlashBladeItems.SLASHBLADE_WHITE.get())
            .gate(new ResearchGate(BladeResearchEntries.UNLOCK, Optional.empty(), false))
            .save(output, materialId(ThaumaturgeBladeForms.SILVERWOOD_SAYA));
        
        new InfusionRecipeBuilder(TEBladeThaumaturgeRecipes.aspects, RecipeCategory.COMBAT,
            TEBladeDefinitionsProvider.getBlade(registries, TEBladeDefinitionsProvider.RAINSHOWER),
            BladeRecipeIngredients.exact(ThaumaturgeBladeForms.variant(ThaumaturgeBladeForms.Family.MYSTERY, ThaumaturgeBladeForms.Element.TIDE)))
            .component(Ingredient.of(Items.HEART_OF_THE_SEA))
            .component(Ingredient.of(Items.SLIME_BLOCK))
            .component(Ingredient.of(TTItems.CRYSTAL_AQUA))
            .component(Ingredient.of(SlashBladeItems.PROUDSOUL_TRAPEZOHEDRON.get()))
            .instability(5)
            .aspect(TEBladeAspects.RAIN, 17)
            .aspect(TEBladeAspects.STASIS, 14)
            .aspect(TEBladeAspects.SLASH_BLADE, 29)
            .aspect(TEBladeAspects.PROUD_SOUL, 46)
            .aspect(TEBladeAspects.AKURYO, 1)
            .gate(new ResearchGate(BladeResearchEntries.breath(ThaumaturgeBladeForms.Element.TIDE), Optional.empty(), false))
            .save(output, TEBlade.prefix("rainshower_infusion"));
        
        new InfusionRecipeBuilder(TEBladeThaumaturgeRecipes.aspects, RecipeCategory.COMBAT,
            TEBladeDefinitionsProvider.getBlade(registries, TEBladeDefinitionsProvider.FOX_LOOKING_FOR_EDGE_OF_GROUNDEDGE),
            SlashBladeIngredient.of(RequestDefinition.Builder.newInstance().name(SlashBladeBuiltInRegistry.FOX_BLACK.location()).build()))
            .component(Ingredient.of(Items.EMERALD))
            .component(Ingredient.of(Items.SWEET_BERRIES))
            .component(Ingredient.of(Items.RABBIT_FOOT))
            .component(Ingredient.of(Items.RABBIT_HIDE))
            .component(Ingredient.of(Items.EGG))
            .component(Ingredient.of(Items.WHEAT))
            .component(Ingredient.of(Items.LEATHER))
            .component(Ingredient.of(Items.FEATHER))
            .component(Ingredient.of(Items.POWDER_SNOW_BUCKET))
            .instability(5)
            .aspect(TTAspects.BESTIA, 8)
            .aspect(TTAspects.GELUM, 22)
            .aspect(TTAspects.TERRA, 28)
            .aspect(TTAspects.VACUOS, 15)
            .aspect(TEBladeAspects.PROUD_SOUL, 10)
            .gate(new ResearchGate(BladeResearchEntries.BLADE_RODAI, Optional.empty(), false))
            .save(output, TEBlade.prefix("fox_looking_for_edge_of_groundedge_black_infusion"));
        
        new InfusionRecipeBuilder(TEBladeThaumaturgeRecipes.aspects, RecipeCategory.COMBAT,
            TEBladeDefinitionsProvider.getBlade(registries, TEBladeDefinitionsProvider.FOX_LOOKING_FOR_EDGE_OF_GROUNDEDGE),
            SlashBladeIngredient.of(RequestDefinition.Builder.newInstance().name(SlashBladeBuiltInRegistry.FOX_WHITE.location()).build()))
            .component(Ingredient.of(Items.EMERALD))
            .component(Ingredient.of(Items.SWEET_BERRIES))
            .component(Ingredient.of(Items.RABBIT_FOOT))
            .component(Ingredient.of(Items.RABBIT_STEW))
            .component(Ingredient.of(Items.EGG))
            .component(Ingredient.of(Items.WHEAT))
            .component(Ingredient.of(Items.LEATHER))
            .component(Ingredient.of(Items.FEATHER))
            .component(Ingredient.of(Items.POWDER_SNOW_BUCKET))
            .instability(5)
            .aspect(TTAspects.BESTIA, 8)
            .aspect(TTAspects.GELUM, 22)
            .aspect(TTAspects.TERRA, 28)
            .aspect(TTAspects.VACUOS, 15)
            .aspect(TEBladeAspects.PROUD_SOUL, 10)
            .gate(new ResearchGate(BladeResearchEntries.BLADE_RODAI, Optional.empty(), false))
            .save(output, TEBlade.prefix("fox_looking_for_edge_of_groundedge_white_infusion"));
    }
    
    private static void rodai(RecipeOutput output) {
        for (ThaumaturgeBladeForms.Family family : ThaumaturgeBladeForms.Family.values()) {
            ThaumaturgeBladeForms.Form center = family == ThaumaturgeBladeForms.Family.TAINT
                ? ThaumaturgeBladeForms.GREATWOOD_DUMMY : ThaumaturgeBladeForms.SILVERWOOD_SAYA;
            List<ResourceKey<IAspect>> themes = themes(family);
            InfusionRecipeBuilder builder = new InfusionRecipeBuilder(TEBladeThaumaturgeRecipes.aspects, RecipeCategory.COMBAT,
                BladeDefinitions.create(ThaumaturgeBladeForms.base(family)).getBlade(),
                BladeRecipeIngredients.exact(center))
                .component(Ingredient.of(sacrifice(family)))
                .component(Ingredient.of(TTItems.SALIS_MUNDUS.get()))
                .instability(instability(family))
                .gate(new ResearchGate(BladeResearchEntries.family(family), Optional.empty(), false));
            for (ResourceKey<IAspect> theme : themes) {
                builder.aspect(theme, THEME_ASPECT_TOTAL / themes.size());
            }
            builder.aspect(TEBladeAspects.SLASH_BLADE, 15)
                .aspect(TEBladeAspects.PROUD_SOUL, 10)
                .aspect(TEBladeAspects.AKURYO, 5);
            builder.save(output, rodaiId(family));
        }
    }
    
    private static void elements(RecipeOutput output) {
        for (ThaumaturgeBladeForms.Family family : ThaumaturgeBladeForms.Family.values()) {
            for (ThaumaturgeBladeForms.Element element : ThaumaturgeBladeForms.Element.values()) {
                BladeElementRecipeCosts.Cost cost = BladeElementRecipeCosts.forElement(element);
                
                List<Ingredient> components = new ArrayList<>();
                for (BladeElementRecipeCosts.AspectCost crystal : cost.crystals()) {
                    Ingredient ingredient = DataComponentIngredient.of(false, TTDataComponents.CRYSTAL_ASPECT.get(),
                        new AspectInstance(TEBladeThaumaturgeRecipes.aspects.getOrThrow(crystal.aspect()), 1), TTItems.ESSENTIA_CRYSTAL.get());
                    for (int i = 0; i < crystal.amount(); i++) {
                        components.add(ingredient);
                    }
                }
                components.add(Ingredient.of(TTItemTags.NUGGETS_QUARTZ));
                components.add(Ingredient.of(TTItemTags.PLANKS_GREATWOOD));
                
                List<AspectInstance> aspects = cost.aspects().stream().map(aspect -> new AspectInstance(TEBladeThaumaturgeRecipes.aspects.getOrThrow(aspect.aspect()), aspect.amount())).collect(Collectors.toList());
                for (ResourceKey<IAspect> aspect : List.of(TEBladeAspects.SLASH_BLADE, TEBladeAspects.PROUD_SOUL,
                    TEBladeAspects.AKURYO)) {
                    aspects.add(new AspectInstance(TEBladeThaumaturgeRecipes.aspects.getOrThrow(aspect), 5));
                }
                
                output.accept(elementId(family, element), new BladeElementResearchRecipe(new InfusionRecipe(
                    BladeRecipeIngredients.exact(ThaumaturgeBladeForms.base(family)), components,
                    AspectList.ofEntries(aspects), cost.instability(),
                    BladeDefinitions.create(ThaumaturgeBladeForms.variant(family, element)).getBlade(),
                    Optional.of(new ResearchGate(BladeResearchEntries.breath(element), Optional.empty(), false))),
                    new ResearchGate(BladeResearchEntries.family(family), Optional.empty(), false)), null);
            }
        }
    }
    
    private static void reversions(RecipeOutput output) {
        for (ThaumaturgeBladeForms.Family family : ThaumaturgeBladeForms.Family.values()) {
            for (ThaumaturgeBladeForms.Element element : ThaumaturgeBladeForms.Element.values()) {
                output.accept(reversionId(family, element), new BladeReversionRecipe(
                    BladeRecipeIngredients.exact(ThaumaturgeBladeForms.variant(family, element)),
                    BladeDefinitions.create(ThaumaturgeBladeForms.base(family)).getBlade()), null);
            }
        }
    }
    
    private static void terminal(RecipeOutput output) {
        List<Ingredient> centers = new ArrayList<>();
        List<Ingredient> components = new ArrayList<>();
        for (ThaumaturgeBladeForms.Family family : ThaumaturgeBladeForms.Family.values()) {
            components.add(BladeRecipeIngredients.exact(ThaumaturgeBladeForms.base(family)));
            for (ThaumaturgeBladeForms.Element element : ThaumaturgeBladeForms.Element.values()) {
                centers.add(BladeRecipeIngredients.exact(ThaumaturgeBladeForms.variant(family, element)));
            }
        }
        components.add(CompoundIngredient.of(
            DataComponentIngredient.of(false, DataComponents.DAMAGE, 0, TTItems.PRIMORDIAL_PEARL.get()),
            DataComponentIngredient.of(false, DataComponents.DAMAGE, 1, TTItems.PRIMORDIAL_PEARL.get()),
            DataComponentIngredient.of(false, DataComponents.DAMAGE, 2, TTItems.PRIMORDIAL_PEARL.get())));
        
        InfusionRecipeBuilder builder = new InfusionRecipeBuilder(TEBladeThaumaturgeRecipes.aspects, RecipeCategory.COMBAT,
            BladeDefinitions.create(ThaumaturgeBladeForms.PRIMORDIAL_CURSE).getBlade(),
            CompoundIngredient.of(centers.toArray(Ingredient[]::new)))
            .instability(10)
            .gate(new ResearchGate(BladeResearchEntries.PRIMORDIAL_CURSE, Optional.of(TERMINAL_STAGE), false));
        for (ResourceKey<IAspect> primal : TTAspects.PRIMALS) {
            components.add(DataComponentIngredient.of(false, TTDataComponents.CRYSTAL_ASPECT.get(),
                new AspectInstance(TEBladeThaumaturgeRecipes.aspects.getOrThrow(primal), 1), TTItems.ESSENTIA_CRYSTAL.get()));
            builder.aspect(primal, 60);
        }
        builder.aspect(TTAspects.PRAECANTATIO, 40)
            .aspect(TTAspects.ALIENIS, 40)
            .aspect(TEBladeAspects.SLASH_BLADE, 100)
            .aspect(TEBladeAspects.PROUD_SOUL, 100)
            .aspect(TEBladeAspects.AKURYO, 100);
        components.add(Ingredient.of(TTItems.RUNED_TABLET.get()));
        
        components.forEach(builder::component);
        builder.save(output, TERMINAL);
    }
    
    private static ArcaneWorkbenchShapedRecipeBuilder arcaneShaped(RecipeCategory category, ItemStack result, int vis) {
        return new ArcaneWorkbenchShapedRecipeBuilder(category, result, items, TEBladeThaumaturgeRecipes.aspects, vis);
    }
    
    private static ArcaneWorkbenchShapelessRecipeBuilder arcaneShapeless(RecipeCategory category, ItemStack result, int vis) {
        return new ArcaneWorkbenchShapelessRecipeBuilder(category, result, TEBladeThaumaturgeRecipes.aspects, vis, items);
    }
    
    private static CrucibleRecipeBuilder crucible(ItemStack result, Ingredient catalyst) {
        return new CrucibleRecipeBuilder(TEBladeThaumaturgeRecipes.aspects, RecipeCategory.MISC, result, catalyst);
    }
    
    private static Item sacrifice(ThaumaturgeBladeForms.Family family) {
        return switch (family) {
            case MYSTERY -> TTItems.THAUMIUM_SWORD.get();
            case VOID -> TTItems.VOID_SWORD.get();
            case BLOOD -> TTItems.CRIMSON_BLADE.get();
            case TAINT -> TTItems.CRYSTAL_VITIUM.get();
        };
    }
    
    private static List<ResourceKey<IAspect>> themes(ThaumaturgeBladeForms.Family family) {
        return switch (family) {
            case MYSTERY -> List.of(TTAspects.PRAECANTATIO);
            case VOID -> List.of(TTAspects.VACUOS);
            case BLOOD -> List.of(TTAspects.VICTUS, TTAspects.AVERSIO);
            case TAINT -> List.of(TTAspects.VITIUM, TTAspects.PERDITIO);
        };
    }
    
    private static int instability(ThaumaturgeBladeForms.Family family) {
        return switch (family) {
            case MYSTERY, VOID -> 2;
            case BLOOD, TAINT -> 5;
        };
    }
}
