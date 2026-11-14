package net.mrqx.slashblade.teblade.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.mrqx.slashblade.teblade.TEBlade;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TEBladeRecipeSerializersRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, TEBlade.MODID);
}
