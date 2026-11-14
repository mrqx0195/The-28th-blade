package net.mrqx.slashblade.teblade.compat.thaumaturge.recipe;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.content.infusion.InfusionInput;
import com.leclowndu93150.thaumaturge.content.infusion.InfusionJobRecipe;
import com.leclowndu93150.thaumaturge.content.recipe.SimpleRecipeSerializer;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import com.leclowndu93150.thaumaturge.registry.TTRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.BladeTransition;
import net.mrqx.slashblade.teblade.data.thaumaturge.research.BladeResearchEntries;

import java.util.List;
import java.util.Optional;

public record BladeReversionRecipe(Ingredient catalyst, ItemStack target) implements InfusionJobRecipe {
    public static final MapCodec<BladeReversionRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        Ingredient.CODEC.fieldOf("catalyst").forGetter(BladeReversionRecipe::catalyst),
        ItemStack.CODEC.fieldOf("result").forGetter(BladeReversionRecipe::target)
    ).apply(i, BladeReversionRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BladeReversionRecipe> STREAM = StreamCodec.of(
        (buf, recipe) -> {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.catalyst());
            ItemStack.STREAM_CODEC.encode(buf, recipe.target());
        }, buf -> new BladeReversionRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(buf), ItemStack.STREAM_CODEC.decode(buf)));
    public static final RecipeSerializer<BladeReversionRecipe> SERIALIZER = new SimpleRecipeSerializer<>(CODEC, STREAM);
    
    @Override
    public List<Ingredient> components() {
        return List.of(Ingredient.of(TTItems.SALIS_MUNDUS.get()));
    }
    
    @Override
    public AspectList aspects() {
        return AspectList.EMPTY;
    }
    
    @Override
    public int instability() {
        return 0;
    }
    
    @Override
    public Optional<ResearchGate> researchGate() {
        return Optional.of(new ResearchGate(
            BladeResearchEntries.BLADE_REVERT, Optional.empty(), false));
    }
    
    @Override
    public ItemStack resultItem() {
        return target.copy();
    }
    
    @Override
    public boolean matches(InfusionInput input, Level level) {
        return catalyst.test(input.catalyst()) && matchComponents(input.components()) != null
            && BladeTransition.canTransform(input.catalyst(), target);
    }
    
    @Override
    public ItemStack assemble(InfusionInput input, HolderLookup.Provider registries) {
        ItemStack result = BladeTransition.transformRecorded(input.catalyst(), target);
        return result == null ? target.copy() : result;
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }
    
    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return target.copy();
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
    
    @Override
    public RecipeType<?> getType() {
        return TTRecipeTypes.INFUSION.get();
    }
    
    @Override
    public boolean isSpecial() {
        return true;
    }
}
