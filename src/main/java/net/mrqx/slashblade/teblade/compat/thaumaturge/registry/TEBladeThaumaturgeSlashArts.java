package net.mrqx.slashblade.teblade.compat.thaumaturge.registry;

import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.mrqx.slashblade.teblade.registry.TEBladeSlashArtsRegistry;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class TEBladeThaumaturgeSlashArts {
    public static final DeferredHolder<SlashArts, SlashArts> GROWTH = TEBladeSlashArtsRegistry.SLASH_ARTS.register("growth_art",
        () -> new SlashArts(entity -> TEBladeThaumaturgeComboStates.GROWTH_MOTION.getId())
            .setComboStateJust(entity -> TEBladeThaumaturgeComboStates.GROWTH_MOTION_JUST.getId()).setProudSoulCost(20));
    
    public static final DeferredHolder<SlashArts, SlashArts> EARTH = TEBladeSlashArtsRegistry.SLASH_ARTS.register("earth_art",
        () -> new SlashArts(entity -> TEBladeThaumaturgeComboStates.EARTH_MOTION.getId())
            .setComboStateJust(entity -> TEBladeThaumaturgeComboStates.EARTH_MOTION_JUST.getId()).setProudSoulCost(20));
    
    public static final DeferredHolder<SlashArts, SlashArts> FLAME = TEBladeSlashArtsRegistry.SLASH_ARTS.register("flame_art",
        () -> new SlashArts(entity -> TEBladeThaumaturgeComboStates.FLAME_MOTION.getId())
            .setComboStateJust(entity -> TEBladeThaumaturgeComboStates.FLAME_MOTION_JUST.getId()).setProudSoulCost(20));
    
    public static final DeferredHolder<SlashArts, SlashArts> THUNDER = TEBladeSlashArtsRegistry.SLASH_ARTS.register("thunder_art",
        () -> new SlashArts(entity -> TEBladeThaumaturgeComboStates.THUNDER_MOTION.getId())
            .setComboStateJust(entity -> TEBladeThaumaturgeComboStates.THUNDER_MOTION_JUST.getId()).setProudSoulCost(20));
    
    public static final DeferredHolder<SlashArts, SlashArts> SKY = TEBladeSlashArtsRegistry.SLASH_ARTS.register("sky_art",
        () -> new SlashArts(entity -> TEBladeThaumaturgeComboStates.SKY_MOTION.getId())
            .setComboStateJust(entity -> TEBladeThaumaturgeComboStates.SKY_MOTION_JUST.getId()).setProudSoulCost(20));
    
    public static final DeferredHolder<SlashArts, SlashArts> TIDE = TEBladeSlashArtsRegistry.SLASH_ARTS.register("tide_art",
        () -> new SlashArts(entity -> TEBladeThaumaturgeComboStates.TIDE_MOTION.getId())
            .setComboStateJust(entity -> TEBladeThaumaturgeComboStates.TIDE_MOTION_JUST.getId()).setProudSoulCost(20));
    
    public static void init() {
    }
}
