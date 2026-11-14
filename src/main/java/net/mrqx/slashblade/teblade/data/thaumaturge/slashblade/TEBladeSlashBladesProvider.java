package net.mrqx.slashblade.teblade.data.thaumaturge.slashblade;

import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class TEBladeSlashBladesProvider extends DatapackBuiltinEntriesProvider {
    public TEBladeSlashBladesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, definitions(), Set.of(TEBlade.MODID));
    }
    
    private static RegistrySetBuilder definitions() {
        return new RegistrySetBuilder().add(SlashBladeDefinition.REGISTRY_KEY, TEBladeSlashBladesProvider::bootstrap);
    }
    
    private static void bootstrap(BootstrapContext<SlashBladeDefinition> context) {
        context.register(BladeDefinitions.key(ThaumaturgeBladeForms.GREATWOOD_DUMMY),
            BladeDefinitions.create(ThaumaturgeBladeForms.GREATWOOD_DUMMY, SlashArtsRegistry.PIERCING.getId()));
        context.register(BladeDefinitions.key(ThaumaturgeBladeForms.SILVERWOOD_SAYA),
            BladeDefinitions.create(ThaumaturgeBladeForms.SILVERWOOD_SAYA, SlashArtsRegistry.JUDGEMENT_CUT.getId()));
        for (ThaumaturgeBladeForms.Family family : ThaumaturgeBladeForms.Family.values()) {
            register(context, ThaumaturgeBladeForms.base(family));
            for (ThaumaturgeBladeForms.Element element : ThaumaturgeBladeForms.Element.values()) {
                register(context, ThaumaturgeBladeForms.variant(family, element));
            }
        }
        register(context, ThaumaturgeBladeForms.PRIMORDIAL_CURSE);
    }
    
    private static void register(BootstrapContext<SlashBladeDefinition> context, ThaumaturgeBladeForms.Form form) {
        context.register(BladeDefinitions.key(form), BladeDefinitions.create(form));
    }
    
    @Override
    public String getName() {
        return "TEBlade Thaumaturge SlashBlades";
    }
}
