package net.mrqx.slashblade.teblade.data;

import com.leclowndu93150.thaumaturge.TTIds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.mrqx.slashblade.teblade.compat.TEBladeCompatManager;
import net.mrqx.slashblade.teblade.data.recipe.TEBladeRecipeProvider;
import net.mrqx.slashblade.teblade.data.slashblade.TEBladeDefinitionsProvider;
import net.mrqx.slashblade.teblade.data.tag.TEBladeDamageTypeTagGenerator;
import net.mrqx.slashblade.teblade.data.thaumaturge.TEBladeThaumaturgeProvider;
import net.mrqx.slashblade.teblade.data.truepower.TEBladeComboModifiersProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public final class DataGen {
    public static ModLoadedCondition THAUMATURGE_CONDITION = new ModLoadedCondition(TTIds.MODID);
    
    @SubscribeEvent
    public static void onDataGen(GatherDataEvent event) {
        RegistrySetBuilder registries = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, TEBladeDamageTypesBootstrap::bootstrap);
        if (TEBladeCompatManager.THAUMATURGE) {
            TEBladeThaumaturgeProvider.addRegistries(registries);
        }
        event.createDatapackRegistryObjects(registries);
        
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        boolean includeClient = event.includeClient();
        boolean includeServer = event.includeServer();
        
        event.createProvider(TEBladeRecipeProvider::new);
        
        event.createProvider(TEBladeDefinitionsProvider::new);
        event.createProvider(TEBladeComboModifiersProvider::new);
        
        generator.addProvider(includeServer, new TEBladeDamageTypeTagGenerator(packOutput, lookupProvider, existingFileHelper));
        
        //<editor-fold desc="———————————————————— Thaumaturge ————————————————————">
        if (TEBladeCompatManager.THAUMATURGE) {
            TEBladeThaumaturgeProvider.registerProviders(event);
        }
        //</editor-fold>
    }
}
