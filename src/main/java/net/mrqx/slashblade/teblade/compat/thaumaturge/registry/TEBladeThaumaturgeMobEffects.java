package net.mrqx.slashblade.teblade.compat.thaumaturge.registry;

import net.minecraft.world.effect.MobEffect;
import net.mrqx.slashblade.teblade.compat.thaumaturge.mobeffect.Corrosion;
import net.mrqx.slashblade.teblade.compat.thaumaturge.mobeffect.EarthFracture;
import net.mrqx.slashblade.teblade.compat.thaumaturge.mobeffect.EarthRoot;
import net.mrqx.slashblade.teblade.compat.thaumaturge.mobeffect.TideSlow;
import net.mrqx.slashblade.teblade.registry.TEBladeMobEffectsRegistry;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class TEBladeThaumaturgeMobEffects {
    public static final DeferredHolder<MobEffect, Corrosion> CORROSION = TEBladeMobEffectsRegistry.MOB_EFFECTS.register("corrosion", Corrosion::new);
    public static final DeferredHolder<MobEffect, TideSlow> TIDE_SLOW = TEBladeMobEffectsRegistry.MOB_EFFECTS.register("tide_slow", TideSlow::new);
    public static final DeferredHolder<MobEffect, EarthFracture> EARTH_FRACTURE = TEBladeMobEffectsRegistry.MOB_EFFECTS.register("earth_fracture", EarthFracture::new);
    public static final DeferredHolder<MobEffect, EarthRoot> EARTH_ROOT = TEBladeMobEffectsRegistry.MOB_EFFECTS.register("earth_root", EarthRoot::new);
    
    public static void init() {
    }
}
