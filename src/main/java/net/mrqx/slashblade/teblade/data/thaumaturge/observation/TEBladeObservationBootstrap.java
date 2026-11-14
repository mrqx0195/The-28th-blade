package net.mrqx.slashblade.teblade.data.thaumaturge.observation;

import com.leclowndu93150.thaumaturge.registry.TTItems;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.advancement.SlashBladeItemPredicate;
import mods.flammpfeil.slashblade.recipe.RequestDefinition;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.event.BladeObservation;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class TEBladeObservationBootstrap {
    private static final ResourceLocation SLASHBLADE_PREDICATE = SlashBlade.prefix("slashblade");
    
    public static void bootstrap(BootstrapContext<BladeObservation> ctx) {
        ctx.register(
            observationKey("unlock_bladecraft"),
            observation(condition(ctx, SlashBladeItems.PROUDSOUL_TINY.getKey().location())));
        ctx.register(
            observationKey("blade_rodai"),
            observation(blade(ctx, "greatwood_dummy"), blade(ctx, "silverwood_saya")));
        ctx.register(
            observationKey("rodai_mystery"),
            observation(condition(ctx, TTItems.THAUMIUM_SWORD.getKey().location())));
        ctx.register(
            observationKey("rodai_void"),
            observation(condition(ctx, TTItems.VOID_SWORD.getKey().location())));
    }
    
    private static BladeObservation observation(BladeObservation.Condition... conditions) {
        return new BladeObservation(List.of(conditions));
    }
    
    private static BladeObservation.Condition condition(
        BootstrapContext<BladeObservation> ctx,
        ResourceLocation item) {
        return new BladeObservation.Condition(
            lookupItem(ctx, item), 1, Optional.empty(), Integer.MAX_VALUE, Map.of());
    }
    
    private static BladeObservation.Condition blade(BootstrapContext<BladeObservation> ctx, String name) {
        ItemSubPredicate.Type<?> type = Objects.requireNonNull(
            BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE.get(SLASHBLADE_PREDICATE),
            "missing slashblade item sub predicate");
        RequestDefinition request = new RequestDefinition(
            TEBlade.prefix(name), 0, 0, 0, List.of(), List.of());
        Map<ItemSubPredicate.Type<?>, ItemSubPredicate> predicates = Map.of(type, new SlashBladeItemPredicate(request));
        return new BladeObservation.Condition(
            lookupItem(ctx, SlashBladeItems.SLASHBLADE.getKey().location()), 1, Optional.empty(), Integer.MAX_VALUE, predicates);
    }
    
    private static Item lookupItem(BootstrapContext<BladeObservation> ctx, ResourceLocation item) {
        return ctx.lookup(Registries.ITEM).getOrThrow(ResourceKey.create(Registries.ITEM, item)).value();
    }
    
    private static ResourceKey<BladeObservation> observationKey(String path) {
        return ResourceKey.create(BladeObservation.REGISTRY_KEY, TEBlade.prefix(path));
    }
}
