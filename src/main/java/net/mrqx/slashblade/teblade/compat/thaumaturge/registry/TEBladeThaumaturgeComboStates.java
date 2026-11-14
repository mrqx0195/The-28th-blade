package net.mrqx.slashblade.teblade.compat.thaumaturge.registry;

import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import net.mrqx.slashblade.teblade.compat.thaumaturge.slasharts.*;
import net.mrqx.slashblade.teblade.registry.combo.TEBladeComboStateRegistry;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class TEBladeThaumaturgeComboStates {
    public static final DeferredHolder<ComboState, ComboState> GROWTH_MOTION = TEBladeComboStateRegistry.COMBO_STATE.register("growth_art",
        ComboState.Builder.newInstance().startAndEnd(500, 576).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(8, user -> GrowthArt.perform(user, false))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_A4_END.getId())::build);
    public static final DeferredHolder<ComboState, ComboState> GROWTH_MOTION_JUST = TEBladeComboStateRegistry.COMBO_STATE.register("growth_art_just",
        ComboState.Builder.newInstance().startAndEnd(500, 576).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(8, user -> GrowthArt.perform(user, true))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_A4_END.getId())::build);
    
    public static final DeferredHolder<ComboState, ComboState> EARTH_MOTION = TEBladeComboStateRegistry.COMBO_STATE.register("earth_art",
        ComboState.Builder.newInstance().startAndEnd(400, 459).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(2, user -> EarthArt.perform(user, false))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_C_END.getId())::build);
    public static final DeferredHolder<ComboState, ComboState> EARTH_MOTION_JUST = TEBladeComboStateRegistry.COMBO_STATE.register("earth_art_just",
        ComboState.Builder.newInstance().startAndEnd(400, 459).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(2, user -> EarthArt.perform(user, true))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_C_END.getId())::build);
    
    public static final DeferredHolder<ComboState, ComboState> FLAME_MOTION = TEBladeComboStateRegistry.COMBO_STATE.register("flame_art",
        ComboState.Builder.newInstance().startAndEnd(800, 839).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(7, FlameArt::perform)
                .put(14, FlameArt::perform)
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_A4_EX_END.getId())::build);
    public static final DeferredHolder<ComboState, ComboState> FLAME_MOTION_JUST = TEBladeComboStateRegistry.COMBO_STATE.register("flame_art_just",
        ComboState.Builder.newInstance().startAndEnd(800, 839).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(0, FlameArt::saveCenter)
                .put(7, FlameArt::perform)
                .put(14, FlameArt::perform)
                .put(16, user -> FlameArt.burst(user, FlameArt.savedCenter(user)))
                .put(20, user -> FlameArt.burst(user, FlameArt.savedCenter(user)))
                .put(24, user -> FlameArt.burst(user, FlameArt.savedCenter(user)))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_A4_EX_END.getId())::build);
    
    public static final DeferredHolder<ComboState, ComboState> THUNDER_MOTION = TEBladeComboStateRegistry.COMBO_STATE.register("thunder_art",
        ComboState.Builder.newInstance().startAndEnd(900, 1013).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(15, user -> ThunderArt.dash(user, false))
                .put(17, user -> ThunderArt.dash(user, false))
                .put(19, user -> ThunderArt.dash(user, true))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_A5_END.getId())::build);
    public static final DeferredHolder<ComboState, ComboState> THUNDER_MOTION_JUST = TEBladeComboStateRegistry.COMBO_STATE.register("thunder_art_just",
        ComboState.Builder.newInstance().startAndEnd(900, 1013).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(15, user -> ThunderArt.dash(user, false))
                .put(17, user -> ThunderArt.dash(user, false))
                .put(19, user -> ThunderArt.dash(user, false))
                .put(21, user -> ThunderArt.dash(user, false))
                .put(23, user -> ThunderArt.dash(user, true))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_A5_END.getId())::build);
    
    public static final DeferredHolder<ComboState, ComboState> SKY_MOTION = TEBladeComboStateRegistry.COMBO_STATE.register("sky_art",
        ComboState.Builder.newInstance().startAndEnd(200, 218).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(2, user -> SkyArt.perform(user, false))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_A3_END.getId())::build);
    public static final DeferredHolder<ComboState, ComboState> SKY_MOTION_JUST = TEBladeComboStateRegistry.COMBO_STATE.register("sky_art_just",
        ComboState.Builder.newInstance().startAndEnd(200, 218).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(2, user -> SkyArt.perform(user, true))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_A3_END.getId())::build);
    
    public static final DeferredHolder<ComboState, ComboState> TIDE_MOTION = TEBladeComboStateRegistry.COMBO_STATE.register("tide_art",
        ComboState.Builder.newInstance().startAndEnd(800, 839).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(7, user -> TideArt.dash(user, false))
                .put(14, user -> TideArt.dash(user, false))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_A4_EX_END.getId())::build);
    public static final DeferredHolder<ComboState, ComboState> TIDE_MOTION_JUST = TEBladeComboStateRegistry.COMBO_STATE.register("tide_art_just",
        ComboState.Builder.newInstance().startAndEnd(800, 839).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(7, user -> TideArt.dash(user, true))
                .put(14, user -> TideArt.dash(user, true))
                .build())
            .nextOfTimeout(entity -> ComboStateRegistry.COMBO_A4_EX_END.getId())::build);
    
    public static void init() {
    }
}
