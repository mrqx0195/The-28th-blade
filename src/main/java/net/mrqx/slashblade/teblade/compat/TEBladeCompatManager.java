package net.mrqx.slashblade.teblade.compat;

import com.leclowndu93150.thaumaturge.TTIds;
import net.mrqx.slashblade.teblade.compat.thaumaturge.client.TEBladeThaumaturgeClient;
import net.mrqx.slashblade.teblade.compat.thaumaturge.event.BladeObservation;
import net.mrqx.slashblade.teblade.compat.thaumaturge.event.BladeResearchGateHandler;
import net.mrqx.slashblade.teblade.compat.thaumaturge.event.TEBladeThaumaturgeEvents;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.*;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.TEBladeThaumaturgeEntityTypes;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.TEBladeThaumaturgeSpellBehaviorTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.LoadingModList;

public class TEBladeCompatManager {
    public static final boolean THAUMATURGE = LoadingModList.get().getModFileById(TTIds.MODID) != null;
    
    public static void modInit(IEventBus modEventBus, ModContainer modContainer) {
        if (THAUMATURGE) {
            TEBladeThaumaturgeAttachmentTypes.init();
            TEBladeThaumaturgeComboStates.init();
            TEBladeThaumaturgeDataComponents.init();
            TEBladeThaumaturgeEntityTypes.init();
            TEBladeThaumaturgeMobEffects.init();
            TEBladeThaumaturgeRecipeSerializers.init();
            TEBladeThaumaturgeSlashArts.init();
            TEBladeThaumaturgeSpecialEffects.init();
            TEBladeThaumaturgeSpellBehaviorTypes.BEHAVIORS.register(modEventBus);
            
            modEventBus.addListener(BladeObservation::register);
            TEBladeThaumaturgeEvents.init();
            if (FMLEnvironment.dist.isClient()) {
                TEBladeThaumaturgeClient.init(modEventBus);
            }
        }
    }
    
    public static void clientInit(FMLClientSetupEvent event) {
    }
    
    public static void commonSetup(FMLCommonSetupEvent event) {
        if (THAUMATURGE) {
            event.enqueueWork(BladeResearchGateHandler::registerUnlockConditions);
        }
    }
}
