package net.mrqx.slashblade.teblade.client;

import net.mrqx.sbr_core.MrqxSlashBladeCore;
import net.mrqx.slashblade.teblade.compat.TEBladeCompatManager;
import net.mrqx.slashblade.teblade.event.registry.AdvanceSlashEffectRenderConsumerRegistryHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(Dist.CLIENT)
public final class TEBladeClientHandler {
    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        TEBladeCompatManager.clientInit(event);
        IEventBus coreEventBus = MrqxSlashBladeCore.getModEventBus();
        if (coreEventBus != null) {
            coreEventBus.register(AdvanceSlashEffectRenderConsumerRegistryHandler.class);
        }
    }
}
