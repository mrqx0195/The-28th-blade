package net.mrqx.slashblade.teblade.compat.thaumaturge.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.slashblade.teblade.registry.TEBladeDataComponentTypesRegister;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public final class TEBladeThaumaturgeDataComponents {
    public static final int MAX_EXTERNAL_EFFECTS = 256;
    public static final int MAX_MYSTERY_RITE_STACKS = 3;
    
    private static final Codec<Set<ResourceLocation>> EXTERNAL_EFFECTS_CODEC =
        ResourceLocation.CODEC.listOf().flatXmap(TEBladeThaumaturgeDataComponents::boundEffects, TEBladeThaumaturgeDataComponents::toList);
    
    private static final StreamCodec<ByteBuf, Set<ResourceLocation>> EXTERNAL_EFFECTS_STREAM_CODEC =
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list(MAX_EXTERNAL_EFFECTS)).map(Set::copyOf, List::copyOf);
    
    public static final Supplier<DataComponentType<Set<ResourceLocation>>> EXTERNAL_EFFECTS = TEBladeDataComponentTypesRegister.DATA_COMPONENT_TYPES.register("external_effects",
        () -> DataComponentType.<Set<ResourceLocation>>builder()
            .persistent(EXTERNAL_EFFECTS_CODEC)
            .networkSynchronized(EXTERNAL_EFFECTS_STREAM_CODEC)
            .build());
    
    public static final Supplier<DataComponentType<Integer>> MYSTERY_RITE_STACKS = TEBladeDataComponentTypesRegister.DATA_COMPONENT_TYPES.register("mystery_rite_stacks",
        () -> DataComponentType.<Integer>builder()
            .persistent(Codec.intRange(0, MAX_MYSTERY_RITE_STACKS))
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build());
    
    private static DataResult<Set<ResourceLocation>> boundEffects(List<ResourceLocation> effects) {
        if (effects.size() > MAX_EXTERNAL_EFFECTS) {
            return DataResult.error(() -> "Refusing " + effects.size() + " recorded blade effects, the limit is " + MAX_EXTERNAL_EFFECTS);
        }
        return DataResult.success(Set.copyOf(effects));
    }
    
    private static DataResult<List<ResourceLocation>> toList(Set<ResourceLocation> effects) {
        return DataResult.success(List.copyOf(effects));
    }
    
    public static void init() {
    }
}
