package net.mrqx.slashblade.teblade.compat.thaumaturge.event;

import net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect.*;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SlashArtCastTracker;
import net.neoforged.neoforge.common.NeoForge;

public final class TEBladeThaumaturgeEvents {
    public static void init() {
        NeoForge.EVENT_BUS.register(PrimordialCurse.class);
        NeoForge.EVENT_BUS.register(Tide.class);
        NeoForge.EVENT_BUS.register(Sky.class);
        NeoForge.EVENT_BUS.register(Thunder.class);
        NeoForge.EVENT_BUS.register(Earth.class);
        NeoForge.EVENT_BUS.register(Flame.class);
        NeoForge.EVENT_BUS.register(VoidStep.class);
        NeoForge.EVENT_BUS.register(SlashArtCastTracker.class);
        NeoForge.EVENT_BUS.register(TaintMark.class);
        NeoForge.EVENT_BUS.register(Growth.class);
        NeoForge.EVENT_BUS.register(VisRepair.class);
        NeoForge.EVENT_BUS.register(FluxBlade.class);
        NeoForge.EVENT_BUS.register(Bloodthirst.class);
        NeoForge.EVENT_BUS.register(BladeEffectRemovalHandler.class);
        NeoForge.EVENT_BUS.register(BladeInfusionCraftedHandler.class);
        NeoForge.EVENT_BUS.register(BladeResearchGateHandler.class);
        NeoForge.EVENT_BUS.register(BladeNativeComponentHandler.class);
        NeoForge.EVENT_BUS.register(BladeSourceRepairHandler.class);
    }
}