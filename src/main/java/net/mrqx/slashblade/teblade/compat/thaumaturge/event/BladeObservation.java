package net.mrqx.slashblade.teblade.compat.thaumaturge.event;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.flammpfeil.slashblade.advancement.SlashBladeItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mrqx.slashblade.teblade.TEBlade;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record BladeObservation(List<Condition> conditions) {
    public static final ResourceKey<Registry<BladeObservation>> REGISTRY_KEY = ResourceKey.createRegistryKey(TEBlade.prefix("blade_observation"));
    public static final Codec<BladeObservation> CODEC = Condition.CODEC.listOf().fieldOf("conditions")
        .xmap(BladeObservation::new, BladeObservation::conditions).codec();
    
    public static void register(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(REGISTRY_KEY, CODEC, CODEC);
    }
    
    public record Condition(Item item, int count, Optional<ResourceLocation> aspect, int maxDamage,
                            Map<ItemSubPredicate.Type<?>, ItemSubPredicate> predicates) {
        public static final Codec<Condition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(Condition::item),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("count", 1).forGetter(Condition::count),
            ResourceLocation.CODEC.optionalFieldOf("aspect").forGetter(Condition::aspect),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("max_damage", Integer.MAX_VALUE).forGetter(Condition::maxDamage),
            ItemSubPredicate.CODEC.optionalFieldOf("predicates", Map.of()).forGetter(Condition::predicates)
        ).apply(instance, Condition::new));
        
        public Optional<SlashBladeItemPredicate> bladePredicate() {
            return predicates.values().stream().filter(SlashBladeItemPredicate.class::isInstance)
                .map(SlashBladeItemPredicate.class::cast).findFirst();
        }
        
        public boolean matches(ItemStack stack) {
            if (stack.isEmpty() || !stack.is(item) || stack.getDamageValue() > maxDamage) {
                return false;
            }
            if (!predicates.values().stream().allMatch(predicate -> predicate.matches(stack))) {
                return false;
            }
            if (aspect.isEmpty()) {
                return true;
            }
            AspectInstance value = stack.get(TTDataComponents.CRYSTAL_ASPECT.get());
            return value != null && value.amount() == 1 && value.aspect().unwrapKey()
                .map(key -> key.location().equals(aspect.get())).orElse(false);
        }
    }
}
