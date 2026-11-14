package net.mrqx.slashblade.teblade.playeranim;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.compat.playerAnim.VmdAnimation;
import mods.flammpfeil.slashblade.event.client.PlayerAnimationInitEvent;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.registry.combo.TEBladeComboStateRegistry;
import net.mrqx.slashblade.teblade.registry.combo.dreadstorm.DreadStormComboStateRegistry;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public final class PlayerAnimationRegistryHandler {
    public static final ResourceLocation DEFAULT_VMD = SlashBlade.prefix("model/pa/player_motion.vmd");
    public static final ResourceLocation DREAD_STORM_COMBO_A4_VMD = TEBlade.vmdLocPlayer(DreadStormComboStateRegistry.COMBO_A4_ID);
    public static final ResourceLocation DREAD_STORM_COMBO_B_VMD = TEBlade.vmdLocPlayer(DreadStormComboStateRegistry.COMBO_B_PREFIX);
    public static final ResourceLocation AERIAL_RAVE_B4_LOOP_VMD = TEBlade.vmdLocPlayer(DreadStormComboStateRegistry.AERIAL_RAVE_B4_LOOP_ID);
    public static final ResourceLocation UPPER_SLASH_VMD = TEBlade.vmdLocPlayer(DreadStormComboStateRegistry.UPPER_SLASH_ID);
    public static final ResourceLocation RAPID_SLASH_VMD = TEBlade.vmdLocPlayer(DreadStormComboStateRegistry.RAPID_SLASH_ID);
    public static final ResourceLocation DIVING_STAR_VMD = TEBlade.vmdLocPlayer(DreadStormComboStateRegistry.DIVING_STAR_ID);
    public static final ResourceLocation NO_SHADOW_VMD = TEBlade.vmdLocPlayer(DreadStormComboStateRegistry.NO_SHADOW_ID);
    public static final ResourceLocation JUDGEMENT_CUT_STINGER_MOTION_LOC = TEBlade.vmdLocPlayer("judgement_cut_stinger");
    
    @SubscribeEvent
    public static void onSlashBladePlayerAnimationRegistryEvent(PlayerAnimationInitEvent event) {
        Map<ResourceLocation, VmdAnimation> animations = new HashMap<>();
        
        //<editor-fold desc="———————————————————— RainShower Cut ————————————————————">
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT.getId(), new VmdAnimation(DEFAULT_VMD, 1900, 1963, false));
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_SLASH.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_SHEATH.getId(), new VmdAnimation(DEFAULT_VMD, 1928, 1963, false));
        
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_SLASH_AIR.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_SHEATH_AIR.getId(), new VmdAnimation(DEFAULT_VMD, 1928, 1963, false));
        
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_SLASH_JUST.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_SLASH_JUST2.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_SLASH_JUST3.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_SHEATH_JUST.getId(), new VmdAnimation(DEFAULT_VMD, 1928, 1963, false));
        
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_END.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_STINGER.getId(), new VmdAnimation(JUDGEMENT_CUT_STINGER_MOTION_LOC, 0, 105, false));
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_STINGER_END.getId(), new VmdAnimation(JUDGEMENT_CUT_STINGER_MOTION_LOC, 27, 105, false));
        animations.put(TEBladeComboStateRegistry.RAINSHOWER_CUT_STINGER_END2.getId(), new VmdAnimation(JUDGEMENT_CUT_STINGER_MOTION_LOC, 85, 105, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Dread Storm ————————————————————">
        //<editor-fold desc=" ———————————————————— Combo A ————————————————————">
        animations.put(DreadStormComboStateRegistry.COMBO_A1.getId(), new VmdAnimation(DEFAULT_VMD, 1, 41, false));
        animations.put(DreadStormComboStateRegistry.COMBO_A1_END.getId(), new VmdAnimation(DEFAULT_VMD, 10, 41, false));
        animations.put(DreadStormComboStateRegistry.COMBO_A1_END2.getId(), new VmdAnimation(DEFAULT_VMD, 21, 41, false));
        
        animations.put(DreadStormComboStateRegistry.COMBO_A2.getId(), new VmdAnimation(DEFAULT_VMD, 100, 151, false));
        animations.put(DreadStormComboStateRegistry.COMBO_A2_END.getId(), new VmdAnimation(DEFAULT_VMD, 115, 151, false));
        animations.put(DreadStormComboStateRegistry.COMBO_A2_END2.getId(), new VmdAnimation(DEFAULT_VMD, 132, 151, false));
        
        animations.put(DreadStormComboStateRegistry.COMBO_A3.getId(), new VmdAnimation(DEFAULT_VMD, 400, 488, false));
        animations.put(DreadStormComboStateRegistry.COMBO_A3_END.getId(), new VmdAnimation(DEFAULT_VMD, 459, 488, false));
        
        animations.put(DreadStormComboStateRegistry.COMBO_A4.getId(), new VmdAnimation(DREAD_STORM_COMBO_A4_VMD, 0, 93, false));
        animations.put(DreadStormComboStateRegistry.COMBO_A4_END.getId(), new VmdAnimation(DREAD_STORM_COMBO_A4_VMD, 21, 93, false));
        animations.put(DreadStormComboStateRegistry.COMBO_A4_END2.getId(), new VmdAnimation(DREAD_STORM_COMBO_A4_VMD, 79, 93, false));
        
        animations.put(DreadStormComboStateRegistry.COMBO_A5.getId(), new VmdAnimation(DEFAULT_VMD, 500, 608, false));
        animations.put(DreadStormComboStateRegistry.COMBO_A5_END.getId(), new VmdAnimation(DEFAULT_VMD, 576, 608, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Combo B ————————————————————">
        animations.put(DreadStormComboStateRegistry.COMBO_B2.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 0, 101, false));
        animations.put(DreadStormComboStateRegistry.COMBO_B2_END.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 21, 101, false));
        animations.put(DreadStormComboStateRegistry.COMBO_B2_END2.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 25, 101, false));
        animations.put(DreadStormComboStateRegistry.COMBO_B2_END3.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 80, 101, false));
        
        animations.put(DreadStormComboStateRegistry.COMBO_B3.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 120, 195, false));
        animations.put(DreadStormComboStateRegistry.COMBO_B3_END.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 140, 195, false));
        animations.put(DreadStormComboStateRegistry.COMBO_B3_END2.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 157, 195, false));
        animations.put(DreadStormComboStateRegistry.COMBO_B3_END3.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 172, 195, false));
        
        animations.put(DreadStormComboStateRegistry.COMBO_B4.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 210, 307, false));
        animations.put(DreadStormComboStateRegistry.COMBO_B4_END.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 231, 307, false));
        animations.put(DreadStormComboStateRegistry.COMBO_B4_END2.getId(), new VmdAnimation(DREAD_STORM_COMBO_B_VMD, 289, 307, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Combo C ————————————————————">
        animations.put(DreadStormComboStateRegistry.COMBO_C3.getId(), new VmdAnimation(DEFAULT_VMD, 200, 306, false));
        animations.put(DreadStormComboStateRegistry.COMBO_C3_END.getId(), new VmdAnimation(DEFAULT_VMD, 218, 306, false));
        animations.put(DreadStormComboStateRegistry.COMBO_C3_END2.getId(), new VmdAnimation(DEFAULT_VMD, 230, 306, false));
        animations.put(DreadStormComboStateRegistry.COMBO_C3_END3.getId(), new VmdAnimation(DEFAULT_VMD, 281, 306, false));
        
        animations.put(DreadStormComboStateRegistry.COMBO_C4.getId(), new VmdAnimation(DEFAULT_VMD, 800, 894, false));
        animations.put(DreadStormComboStateRegistry.COMBO_C4_END.getId(), new VmdAnimation(DEFAULT_VMD, 839, 894, false));
        animations.put(DreadStormComboStateRegistry.COMBO_C4_END2.getId(), new VmdAnimation(DEFAULT_VMD, 877, 894, false));
        
        animations.put(DreadStormComboStateRegistry.COMBO_C5.getId(), new VmdAnimation(DEFAULT_VMD, 900, 1061, false));
        animations.put(DreadStormComboStateRegistry.COMBO_C5_END.getId(), new VmdAnimation(DEFAULT_VMD, 1013, 1061, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Combo D ————————————————————">
        animations.put(DreadStormComboStateRegistry.COMBO_D1.getId(), new VmdAnimation(DEFAULT_VMD, 720, 787, false));
        animations.put(DreadStormComboStateRegistry.COMBO_D1_END.getId(), new VmdAnimation(DEFAULT_VMD, 743, 787, false));
        animations.put(DreadStormComboStateRegistry.COMBO_D1_END2.getId(), new VmdAnimation(DEFAULT_VMD, 764, 787, false));
        animations.put(DreadStormComboStateRegistry.COMBO_D_END.getId(), new VmdAnimation(DEFAULT_VMD, 720, 787, false));
        
        animations.put(DreadStormComboStateRegistry.COMBO_D2.getId(), new VmdAnimation(DEFAULT_VMD, 710, 787, false));
        animations.put(DreadStormComboStateRegistry.COMBO_D3.getId(), new VmdAnimation(DEFAULT_VMD, 710, 787, false));
        animations.put(DreadStormComboStateRegistry.COMBO_D4.getId(), new VmdAnimation(DEFAULT_VMD, 710, 787, false));
        animations.put(DreadStormComboStateRegistry.COMBO_D5.getId(), new VmdAnimation(DEFAULT_VMD, 710, 787, false));
        animations.put(DreadStormComboStateRegistry.COMBO_D6.getId(), new VmdAnimation(DEFAULT_VMD, 710, 787, false));
        animations.put(DreadStormComboStateRegistry.COMBO_D7.getId(), new VmdAnimation(DEFAULT_VMD, 710, 787, false));
        animations.put(DreadStormComboStateRegistry.COMBO_D8.getId(), new VmdAnimation(DEFAULT_VMD, 710, 787, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Aerial Rave A ————————————————————">
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_A1.getId(), new VmdAnimation(DEFAULT_VMD, 1100, 1132, false));
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_A1_END.getId(), new VmdAnimation(DEFAULT_VMD, 1122, 1132, false));
        
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_A2.getId(), new VmdAnimation(DEFAULT_VMD, 1200, 1241, false));
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_A2_END.getId(), new VmdAnimation(DEFAULT_VMD, 1210, 1241, false));
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_A2_END2.getId(), new VmdAnimation(DEFAULT_VMD, 1231, 1241, false));
        
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_A3.getId(), new VmdAnimation(DEFAULT_VMD, 1300, 1338, false));
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_A3_END.getId(), new VmdAnimation(DEFAULT_VMD, 1328, 1338, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Aerial Rave B ————————————————————">
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_B3.getId(), new VmdAnimation(DEFAULT_VMD, 1400, 1443, false));
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_B3_END.getId(), new VmdAnimation(DEFAULT_VMD, 1437, 1443, false));
        
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_B4.getId(), new VmdAnimation(DEFAULT_VMD, 1500, 1547, false));
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_B4_LOOP.getId(), new VmdAnimation(AERIAL_RAVE_B4_LOOP_VMD, 0, 9, true));
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_B4_LOOP2.getId(), new VmdAnimation(AERIAL_RAVE_B4_LOOP_VMD, 0, 9, true));
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_B4_LOOP3.getId(), new VmdAnimation(AERIAL_RAVE_B4_LOOP_VMD, 0, 9, true));
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_B4_END.getId(), new VmdAnimation(DEFAULT_VMD, 1513, 1547, false));
        animations.put(DreadStormComboStateRegistry.AERIAL_RAVE_B4_END2.getId(), new VmdAnimation(DEFAULT_VMD, 1537, 1547, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Upper Slash ————————————————————">
        animations.put(DreadStormComboStateRegistry.UPPER_SLASH.getId(), new VmdAnimation(UPPER_SLASH_VMD, 0, 97, false));
        animations.put(DreadStormComboStateRegistry.UPPER_SLASH_END.getId(), new VmdAnimation(UPPER_SLASH_VMD, 25, 97, false));
        animations.put(DreadStormComboStateRegistry.UPPER_SLASH_END2.getId(), new VmdAnimation(UPPER_SLASH_VMD, 65, 97, false));
        
        animations.put(DreadStormComboStateRegistry.UPPER_SLASH2.getId(), new VmdAnimation(UPPER_SLASH_VMD, 110, 182, false));
        animations.put(DreadStormComboStateRegistry.UPPER_SLASH2_END.getId(), new VmdAnimation(UPPER_SLASH_VMD, 159, 182, false));
        
        animations.put(DreadStormComboStateRegistry.UPPER_SLASH_JUMP.getId(), new VmdAnimation(DEFAULT_VMD, 1700, 1713, false));
        animations.put(DreadStormComboStateRegistry.UPPER_SLASH_JUMP_END.getId(), new VmdAnimation(DEFAULT_VMD, 1713, 1717, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Aerial Cleave ————————————————————">
        animations.put(DreadStormComboStateRegistry.AERIAL_CLEAVE.getId(), new VmdAnimation(DEFAULT_VMD, 1800, 1804, false));
        animations.put(DreadStormComboStateRegistry.AERIAL_CLEAVE_SLASH.getId(), new VmdAnimation(DEFAULT_VMD, 1505, 1547, false));
        animations.put(DreadStormComboStateRegistry.AERIAL_CLEAVE_END.getId(), new VmdAnimation(DEFAULT_VMD, 1537, 1547, false));
        
        animations.put(DreadStormComboStateRegistry.DIVING_SLASH.getId(), new VmdAnimation(DEFAULT_VMD, 1812, 1817, false));
        animations.put(DreadStormComboStateRegistry.DIVING_SLASH_END.getId(), new VmdAnimation(DEFAULT_VMD, 1816, 1886, false));
        animations.put(DreadStormComboStateRegistry.DIVING_SLASH_END2.getId(), new VmdAnimation(DEFAULT_VMD, 1859, 1886, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Rapid Slash ————————————————————">
        animations.put(DreadStormComboStateRegistry.RAPID_SLASH.getId(), new VmdAnimation(RAPID_SLASH_VMD, 0, 9, true));
        animations.put(DreadStormComboStateRegistry.RAPID_SLASH_END.getId(), new VmdAnimation(RAPID_SLASH_VMD, 9, 56, false));
        animations.put(DreadStormComboStateRegistry.RAPID_SLASH_END2.getId(), new VmdAnimation(RAPID_SLASH_VMD, 33, 56, false));
        
        animations.put(DreadStormComboStateRegistry.RISING_STAR.getId(), new VmdAnimation(DEFAULT_VMD, 2100, 2147, false));
        animations.put(DreadStormComboStateRegistry.RISING_STAR_END.getId(), new VmdAnimation(DEFAULT_VMD, 2137, 2147, false));
        
        animations.put(DreadStormComboStateRegistry.DIVING_STAR.getId(), new VmdAnimation(DIVING_STAR_VMD, 0, 10, false));
        animations.put(DreadStormComboStateRegistry.DIVING_STAR_LOOP.getId(), new VmdAnimation(DIVING_STAR_VMD, 11, 22, true));
        animations.put(DreadStormComboStateRegistry.DIVING_STAR_END.getId(), new VmdAnimation(DIVING_STAR_VMD, 30, 53, false));
        animations.put(DreadStormComboStateRegistry.DIVING_STAR_END2.getId(), new VmdAnimation(RAPID_SLASH_VMD, 18, 33, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Void Slash ————————————————————">
        animations.put(DreadStormComboStateRegistry.VOID_SLASH.getId(), new VmdAnimation(DEFAULT_VMD, 2200, 2299, false));
        animations.put(DreadStormComboStateRegistry.VOID_SLASH_END.getId(), new VmdAnimation(DEFAULT_VMD, 2278, 2299, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— No Shadow ————————————————————">
        animations.put(DreadStormComboStateRegistry.NO_SHADOW.getId(), new VmdAnimation(NO_SHADOW_VMD, 0, 82, false));
        animations.put(DreadStormComboStateRegistry.NO_SHADOW_END.getId(), new VmdAnimation(NO_SHADOW_VMD, 67, 82, false));
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Judgement Cut ————————————————————">
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT.getId(), new VmdAnimation(DEFAULT_VMD, 1900, 1963, false));
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_SLASH.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_SHEATH.getId(), new VmdAnimation(DEFAULT_VMD, 1928, 1963, false));
        
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_SLASH_AIR.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_SHEATH_AIR.getId(), new VmdAnimation(DEFAULT_VMD, 1928, 1963, false));
        
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_SLASH_JUST.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_SLASH_JUST2.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_SLASH_JUST3.getId(), new VmdAnimation(DEFAULT_VMD, 1923, 1963, false));
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_SHEATH_JUST.getId(), new VmdAnimation(DEFAULT_VMD, 1928, 1963, false));
        
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_STINGER.getId(), new VmdAnimation(JUDGEMENT_CUT_STINGER_MOTION_LOC, 0, 105, false));
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_STINGER_END.getId(), new VmdAnimation(JUDGEMENT_CUT_STINGER_MOTION_LOC, 27, 105, false));
        animations.put(DreadStormComboStateRegistry.JUDGEMENT_CUT_STINGER_END2.getId(), new VmdAnimation(JUDGEMENT_CUT_STINGER_MOTION_LOC, 85, 105, false));
        //</editor-fold>
        //</editor-fold>
        
        
        animations.put(TEBlade.prefix("growth_art"), new VmdAnimation(DEFAULT_VMD, 500, 608, false));
        animations.put(TEBlade.prefix("growth_art_just"), new VmdAnimation(DEFAULT_VMD, 500, 608, false));
        animations.put(TEBlade.prefix("earth_art"), new VmdAnimation(DEFAULT_VMD, 400, 488, false));
        animations.put(TEBlade.prefix("earth_art_just"), new VmdAnimation(DEFAULT_VMD, 400, 488, false));
        animations.put(TEBlade.prefix("flame_art"), new VmdAnimation(DEFAULT_VMD, 800, 894, false));
        animations.put(TEBlade.prefix("flame_art_just"), new VmdAnimation(DEFAULT_VMD, 800, 894, false));
        animations.put(TEBlade.prefix("thunder_art"), new VmdAnimation(DEFAULT_VMD, 900, 1061, false));
        animations.put(TEBlade.prefix("thunder_art_just"), new VmdAnimation(DEFAULT_VMD, 900, 1061, false));
        animations.put(TEBlade.prefix("sky_art"), new VmdAnimation(DEFAULT_VMD, 200, 306, false));
        animations.put(TEBlade.prefix("sky_art_just"), new VmdAnimation(DEFAULT_VMD, 200, 306, false));
        animations.put(TEBlade.prefix("tide_art"), new VmdAnimation(DEFAULT_VMD, 800, 894, false));
        animations.put(TEBlade.prefix("tide_art_just"), new VmdAnimation(DEFAULT_VMD, 800, 894, false));
        // ————————————————————
        animations.forEach((resourceLocation, vmdAnimation) ->
            event.register(resourceLocation, () -> vmdAnimation.setBlendArms(false).setBlendLegs(false))
        );
    }
}
