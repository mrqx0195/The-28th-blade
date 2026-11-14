package net.mrqx.slashblade.teblade.compat.thaumaturge.registry;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect.*;
import net.mrqx.slashblade.teblade.registry.TEBladeSpecialEffectsRegistry;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class TEBladeThaumaturgeSpecialEffects {
    public static final DeferredHolder<SpecialEffect, SpecialEffect> VIS_REPAIR = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("vis_repair", VisRepair::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> FLUX_BLADE = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("flux_blade", FluxBlade::new);
    
    public static final DeferredHolder<SpecialEffect, SpecialEffect> BLOODTHIRST = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("bloodthirst", Bloodthirst::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> MYSTERY_RITE = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("mystery_rite", MysteryRite::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> GROWTH = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("growth", Growth::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> TAINT_MARK = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("taint_mark", TaintMark::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> VOID_STEP = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("void_step", VoidStep::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> FLAME = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("flame", Flame::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> EARTH = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("earth", Earth::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> THUNDER = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("thunder", Thunder::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> SKY = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("sky", Sky::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> TIDE = TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register("tide", Tide::new);
    
    public static void init() {
    }
}
