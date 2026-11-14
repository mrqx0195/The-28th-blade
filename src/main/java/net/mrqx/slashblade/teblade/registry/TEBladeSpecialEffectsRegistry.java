package net.mrqx.slashblade.teblade.registry;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.specialeffect.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TEBladeSpecialEffectsRegistry {
    public static final DeferredRegister<SpecialEffect> SPECIAL_EFFECTS = DeferredRegister.create(SpecialEffect.REGISTRY_KEY, TEBlade.MODID);
    
    public static final DeferredHolder<SpecialEffect, SpecialEffect> AUTOMATIC = SPECIAL_EFFECTS
        .register("automatic", Automatic::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> HOUR_OF_JUDGMENT = SPECIAL_EFFECTS
        .register("hour_of_judgment", HourOfJudgment::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> FOX_WISH_SNOW_WISH = SPECIAL_EFFECTS
        .register("fox_wish_snow_wish", FoxWishSnowWish::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> SUDDEN_RAIN = SPECIAL_EFFECTS
        .register("sudden_rain", SuddenRain::new);
    public static final DeferredHolder<SpecialEffect, SpecialEffect> WITNESS_RAINBOW = SPECIAL_EFFECTS
        .register("witness_rainbow", WitnessRainbow::new);
}
