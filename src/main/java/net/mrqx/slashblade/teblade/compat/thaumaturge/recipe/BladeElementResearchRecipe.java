package net.mrqx.slashblade.teblade.compat.thaumaturge.recipe;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.content.infusion.InfusionInput;
import com.leclowndu93150.thaumaturge.content.infusion.InfusionJobRecipe;
import com.leclowndu93150.thaumaturge.content.infusion.InfusionRecipe;
import com.leclowndu93150.thaumaturge.content.recipe.SimpleRecipeSerializer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public record BladeElementResearchRecipe(InfusionRecipe delegate, ResearchGate family) implements InfusionJobRecipe {
    public static final MapCodec<BladeElementResearchRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        InfusionRecipe.MAP_CODEC.codec().fieldOf("infusion").forGetter(BladeElementResearchRecipe::delegate),
        ResearchGate.CODEC.fieldOf("family_research").forGetter(BladeElementResearchRecipe::family)
    ).apply(i, BladeElementResearchRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BladeElementResearchRecipe> STREAM = StreamCodec.of(
        (buf, recipe) -> {
            InfusionRecipe.STREAM_CODEC.encode(buf, recipe.delegate());
            ResearchGate.STREAM_CODEC.encode(buf, recipe.family());
        }, buf -> new BladeElementResearchRecipe(InfusionRecipe.STREAM_CODEC.decode(buf), ResearchGate.STREAM_CODEC.decode(buf)));
    public static final RecipeSerializer<BladeElementResearchRecipe> SERIALIZER = new SimpleRecipeSerializer<>(CODEC, STREAM);
    
    @Override
    public Ingredient catalyst() {
        return delegate.catalyst();
    }
    
    @Override
    public List<Ingredient> components() {
        return delegate.components();
    }
    
    @Override
    public AspectList aspects() {
        return delegate.aspects();
    }
    
    @Override
    public int instability() {
        return delegate.instability();
    }
    
    @Override
    public ItemStack resultItem() {
        return delegate.resultItem();
    }
    
    @Override
    public Optional<ResearchGate> researchGate() {
        return delegate.researchGate();
    }
    
    @Override
    public boolean doesPassGate(Player player) {
        return delegate.doesPassGate(player) && ResearchGate.passes(player, family);
    }
    
    @Override
    public boolean matches(InfusionInput input, Level level) {
        return delegate.matches(input, level);
    }
    
    @Override
    public ItemStack assemble(InfusionInput input, HolderLookup.Provider registries) {
        return delegate.assemble(input, registries);
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return delegate.canCraftInDimensions(width, height);
    }
    
    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return delegate.getResultItem(registries);
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
    
    @Override
    public RecipeType<?> getType() {
        return delegate.getType();
    }
    
    @Override
    public boolean isSpecial() {
        return true;
    }
}
