package net.mrqx.slashblade.teblade.compat.thaumaturge.client;

import net.neoforged.bus.api.IEventBus;

public final class TEBladeThaumaturgeClient {
    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(TEBladeSpellRenderers::onRegisterRenderers);
    }
}
