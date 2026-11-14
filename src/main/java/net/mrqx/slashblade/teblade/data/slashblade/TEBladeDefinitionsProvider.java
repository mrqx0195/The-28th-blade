package net.mrqx.slashblade.teblade.data.slashblade;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.PropertiesDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.RenderDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.registry.TEBladeSlashArtsRegistry;
import net.mrqx.slashblade.teblade.registry.TEBladeSpecialEffectsRegistry;
import net.mrqx.slashblade.teblade.registry.TEBladeSwordTypes;
import net.mrqx.slashblade.teblade.registry.combo.dreadstorm.DreadStormComboStateRegistry;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public final class TEBladeDefinitionsProvider extends DatapackBuiltinEntriesProvider {
    private static final Map<ResourceKey<SlashBladeDefinition>, Function<BootstrapContext<SlashBladeDefinition>, SlashBladeDefinition>> DEFINITION_FUNCTIONS = new HashMap<>();
    public static final Map<ResourceKey<SlashBladeDefinition>, SlashBladeDefinition> DEFINITIONS = new HashMap<>();
    
    public static final ResourceKey<SlashBladeDefinition> FRAGMENTED_SLASHBLADE = registerSimple(new SlashBladeDefinition(
        TEBlade.prefix("fragmented_slashblade"),
        RenderDefinition.Builder.newInstance()
            .textureName(TEBlade.prefix("model/fragmented_slashblade.png"))
            .build(),
        PropertiesDefinition.Builder.newInstance()
            .maxDamage(20)
            .build(),
        List.of()
    ));
    public static final ResourceKey<SlashBladeDefinition> RAINSHOWER = register("rainshower", bootstrap -> new SlashBladeDefinition(
        TEBlade.prefix("rainshower"),
        RenderDefinition.Builder.newInstance()
            .textureName(TEBlade.prefix("model/named/rainshower/rainshower.png"))
            .modelName(TEBlade.prefix("model/named/rainshower/rainshower.obj"))
            .effectColor(getARGBFromRGBA(0x151BA8FF))
            .build(),
        PropertiesDefinition.Builder.newInstance()
            .maxDamage(174)
            .slashArtsType(TEBladeSlashArtsRegistry.RAINSHOWER.getId())
            .addSpecialEffect(TEBladeSpecialEffectsRegistry.SUDDEN_RAIN.getId())
            .addSpecialEffect(TEBladeSpecialEffectsRegistry.WITNESS_RAINBOW.getId())
            .defaultSwordType(List.of(SwordType.BEWITCHED, TEBladeSwordTypes.ANOMALY.getValue()))
            .build(),
        List.of(
            new EnchantmentDefinition(getEnchantmentID(bootstrap, Enchantments.POWER), 5),
            new EnchantmentDefinition(getEnchantmentID(bootstrap, Enchantments.EFFICIENCY), 5)
        )
    ));
    public static final ResourceKey<SlashBladeDefinition> FOX_LOOKING_FOR_EDGE_OF_GROUNDEDGE = register("fox_looking_for_edge_of_groundedge", bootstrap -> new SlashBladeDefinition(
        TEBlade.prefix("fox_looking_for_edge_of_groundedge"),
        RenderDefinition.Builder.newInstance()
            .textureName(TEBlade.prefix("model/named/fox/fox_looking_for_edge_of_groundedge.png"))
            .modelName(TEBlade.prefix("model/named/fox/fox_looking_for_edge_of_groundedge.obj"))
            .effectColor(getARGBFromRGBA(0x2B376BFF))
            .build(),
        PropertiesDefinition.Builder.newInstance()
            .maxDamage(80)
            .baseAttackModifier(22)
            .slashArtsType(SlashArtsRegistry.SAKURA_END.getId())
            .addSpecialEffect(TEBladeSpecialEffectsRegistry.FOX_WISH_SNOW_WISH.getId())
            .defaultSwordType(List.of(SwordType.BEWITCHED, TEBladeSwordTypes.ANOMALY.getValue()))
            .build(),
        List.of(
            new EnchantmentDefinition(getEnchantmentID(bootstrap, Enchantments.FROST_WALKER), 8),
            new EnchantmentDefinition(getEnchantmentID(bootstrap, Enchantments.EFFICIENCY), 2),
            new EnchantmentDefinition(getEnchantmentID(bootstrap, Enchantments.QUICK_CHARGE), 2)
        )
    ));
    public static final ResourceKey<SlashBladeDefinition> YAMATO_DREAD_STORM = register("yamato_dread_storm", bootstrap -> new SlashBladeDefinition(
        TEBlade.prefix("yamato_dread_storm"),
        RenderDefinition.Builder.newInstance()
            .textureName(TEBlade.prefix("model/named/yamato/yamato_dread_storm.png"))
            .modelName(TEBlade.prefix("model/named/yamato/yamato_dread_storm.obj"))
            .effectColor(getARGBFromRGBA(0x0A5A86FF))
            .build(),
        PropertiesDefinition.Builder.newInstance()
            .maxDamage(1000)
            .baseAttackModifier(28)
            .rootComboState(DreadStormComboStateRegistry.DREAD_STORM_STANDBY.getId())
            .slashArtsType(TEBladeSlashArtsRegistry.JUDGEMENT_CUT_DREAD_STORM.getId())
            .defaultSwordType(List.of(SwordType.BEWITCHED, TEBladeSwordTypes.TERMINAL.getValue()))
            .build(),
        List.of(
            new EnchantmentDefinition(getEnchantmentID(bootstrap, Enchantments.POWER), 5),
            new EnchantmentDefinition(getEnchantmentID(bootstrap, Enchantments.SOUL_SPEED), 5),
            new EnchantmentDefinition(getEnchantmentID(bootstrap, Enchantments.INFINITY), 5),
            new EnchantmentDefinition(getEnchantmentID(bootstrap, Enchantments.SHARPNESS), 5),
            new EnchantmentDefinition(getEnchantmentID(bootstrap, Enchantments.SWEEPING_EDGE), 5)
        )
    ));
    
    public TEBladeDefinitionsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, new RegistrySetBuilder().add(SlashBladeDefinition.REGISTRY_KEY, TEBladeDefinitionsProvider::registerAll), Set.of(TEBlade.MODID));
    }
    
    public static void registerAll(BootstrapContext<SlashBladeDefinition> bootstrap) {
        DEFINITION_FUNCTIONS.forEach((key, factory) -> {
            SlashBladeDefinition definition = factory.apply(bootstrap);
            bootstrap.register(key, definition);
            TEBladeDefinitionsProvider.DEFINITIONS.put(key, definition);
        });
    }
    
    private static ResourceKey<SlashBladeDefinition> registerSimple(SlashBladeDefinition definition) {
        return register(definition.getName(), __ -> definition);
    }
    
    private static ResourceKey<SlashBladeDefinition> register(String id, Function<BootstrapContext<SlashBladeDefinition>, SlashBladeDefinition> definitionFactory) {
        return register(TEBlade.prefix(id), definitionFactory);
    }
    
    private static ResourceKey<SlashBladeDefinition> register(ResourceLocation id, Function<BootstrapContext<SlashBladeDefinition>, SlashBladeDefinition> definitionFactory) {
        ResourceKey<SlashBladeDefinition> key = ResourceKey.create(SlashBladeDefinition.REGISTRY_KEY, id);
        DEFINITION_FUNCTIONS.put(key, definitionFactory);
        return key;
    }
    
    public static Holder<Enchantment> getEnchantmentID(BootstrapContext<SlashBladeDefinition> bootstrap, ResourceKey<Enchantment> enchantmentKey) {
        return bootstrap.lookup(Registries.ENCHANTMENT).getOrThrow(enchantmentKey);
    }
    
    public static ItemStack getBlade(HolderLookup.Provider registries, ResourceKey<SlashBladeDefinition> key) {
        ItemStack stack = DEFINITIONS.get(key).getBlade();
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantments == null || enchantments.isEmpty()) {
            return stack;
        }
        HolderLookup.RegistryLookup<Enchantment> lookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
        ItemEnchantments.Mutable resolved = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            ResourceKey<Enchantment> enchantmentKey = entry.getKey().unwrapKey()
                .orElseThrow(() -> new IllegalStateException("Unbound enchantment " + entry.getKey()));
            resolved.set(lookup.getOrThrow(enchantmentKey), entry.getIntValue());
        }
        stack.set(DataComponents.ENCHANTMENTS, resolved.toImmutable());
        return stack;
    }
    
    public static int getARGBFromRGBA(int rgba) {
        return (rgba >>> 8) | (rgba << 24);
    }
    
    @Override
    public String getName() {
        return "TEBlade SlashBlade Definition Registry";
    }
}
