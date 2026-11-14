package net.mrqx.slashblade.teblade.registry.combo.dreadstorm;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil.slashblade.capability.inputstate.IInputState;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.event.handler.FallHandler;
import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.JudgementCut;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.InputCommand;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.TimeValueHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.registry.MrqxComboStateRegistry;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.registry.combo.TEBladeComboStateRegistry;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.mrqx.truepower.attachment.ITruePowerStunData;
import net.mrqx.truepower.config.TruePowerCommonConfig;
import net.mrqx.truepower.event.handler.ComboModifyHandler;
import net.mrqx.truepower.util.TruePowerAttackManager;
import net.mrqx.truepower.util.TruePowerComboHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

/**
 * R ：攻击
 * <p>
 * WASD ：移动键（+锁定）
 * <p>
 * , ：停顿
 * <p>
 * ~ ：此前同此 Combo 的基础
 * <p>
 * - ：长按
 * <p>
 *
 * @see TEBladeComboStateRegistry
 * @see mods.flammpfeil.slashblade.registry.ComboStateRegistry
 */
public final class DreadStormComboStateRegistry {
    public static final DeferredRegister<ComboState> COMBO_STATE = TEBladeComboStateRegistry.COMBO_STATE;
    
    //<editor-fold desc="———————————————————— ID ————————————————————">
    public static final String DREAD_STORM_PREFIX = "dread_storm_";
    public static final String COMBO_PREFIX = DREAD_STORM_PREFIX + "combo_";
    public static final String LOOP_SUFFIX = "_loop";
    public static final String LOOP2_SUFFIX = LOOP_SUFFIX + "2";
    public static final String LOOP3_SUFFIX = LOOP_SUFFIX + "3";
    public static final String END_SUFFIX = "_end";
    public static final String END2_SUFFIX = END_SUFFIX + "2";
    public static final String END3_SUFFIX = END_SUFFIX + "3";
    
    public static final String COMBO_A_PREFIX = COMBO_PREFIX + "a";
    
    public static final String COMBO_A1_ID = COMBO_A_PREFIX + "1";
    public static final String COMBO_A1_END_ID = COMBO_A1_ID + END_SUFFIX;
    public static final String COMBO_A1_END2_ID = COMBO_A1_ID + END2_SUFFIX;
    
    public static final String COMBO_A2_ID = COMBO_A_PREFIX + "2";
    public static final String COMBO_A2_END_ID = COMBO_A2_ID + END_SUFFIX;
    public static final String COMBO_A2_END2_ID = COMBO_A2_ID + END2_SUFFIX;
    
    public static final String COMBO_A3_ID = COMBO_A_PREFIX + "3";
    public static final String COMBO_A3_END_ID = COMBO_A3_ID + END_SUFFIX;
    
    public static final String COMBO_A4_ID = COMBO_A_PREFIX + "4";
    public static final String COMBO_A4_END_ID = COMBO_A4_ID + END_SUFFIX;
    public static final String COMBO_A4_END2_ID = COMBO_A4_ID + END2_SUFFIX;
    
    public static final String COMBO_A5_ID = COMBO_A_PREFIX + "5";
    public static final String COMBO_A5_END_ID = COMBO_A5_ID + END_SUFFIX;
    
    public static final String COMBO_B_PREFIX = COMBO_PREFIX + "b";
    
    public static final String COMBO_B2_ID = COMBO_B_PREFIX + "2";
    public static final String COMBO_B2_END_ID = COMBO_B2_ID + END_SUFFIX;
    public static final String COMBO_B2_END2_ID = COMBO_B2_ID + END2_SUFFIX;
    public static final String COMBO_B2_END3_ID = COMBO_B2_ID + END3_SUFFIX;
    
    public static final String COMBO_B3_ID = COMBO_B_PREFIX + "3";
    public static final String COMBO_B3_END_ID = COMBO_B3_ID + END_SUFFIX;
    public static final String COMBO_B3_END2_ID = COMBO_B3_ID + END2_SUFFIX;
    public static final String COMBO_B3_END3_ID = COMBO_B3_ID + END3_SUFFIX;
    
    public static final String COMBO_B4_ID = COMBO_B_PREFIX + "4";
    public static final String COMBO_B4_END_ID = COMBO_B4_ID + END_SUFFIX;
    public static final String COMBO_B4_END2_ID = COMBO_B4_ID + END2_SUFFIX;
    
    public static final String COMBO_C_PREFIX = COMBO_PREFIX + "c";
    
    public static final String COMBO_C3_ID = COMBO_C_PREFIX + "3";
    public static final String COMBO_C3_END_ID = COMBO_C3_ID + END_SUFFIX;
    public static final String COMBO_C3_END2_ID = COMBO_C3_ID + END2_SUFFIX;
    public static final String COMBO_C3_END3_ID = COMBO_C3_ID + END3_SUFFIX;
    
    public static final String COMBO_C4_ID = COMBO_C_PREFIX + "4";
    public static final String COMBO_C4_END_ID = COMBO_C4_ID + END_SUFFIX;
    public static final String COMBO_C4_END2_ID = COMBO_C4_ID + END2_SUFFIX;
    
    public static final String COMBO_C5_ID = COMBO_C_PREFIX + "5";
    public static final String COMBO_C5_END_ID = COMBO_C5_ID + END_SUFFIX;
    
    public static final String COMBO_D_PREFIX = COMBO_PREFIX + "d";
    
    public static final String COMBO_D1_ID = COMBO_D_PREFIX + "1";
    public static final String COMBO_D1_END_ID = COMBO_D1_ID + END_SUFFIX;
    public static final String COMBO_D1_END2_ID = COMBO_D1_ID + END2_SUFFIX;
    public static final String COMBO_D_END_ID = COMBO_D_PREFIX + END_SUFFIX;
    
    public static final String COMBO_D2_ID = COMBO_D_PREFIX + "2";
    public static final String COMBO_D3_ID = COMBO_D_PREFIX + "3";
    public static final String COMBO_D4_ID = COMBO_D_PREFIX + "4";
    public static final String COMBO_D5_ID = COMBO_D_PREFIX + "5";
    public static final String COMBO_D6_ID = COMBO_D_PREFIX + "6";
    public static final String COMBO_D7_ID = COMBO_D_PREFIX + "7";
    public static final String COMBO_D8_ID = COMBO_D_PREFIX + "8";
    
    public static final String AERIAL_RAVE_PREFIX = DREAD_STORM_PREFIX + "aerial_rave_";
    
    public static final String AERIAL_RAVE_A_PREFIX = AERIAL_RAVE_PREFIX + "a";
    
    public static final String AERIAL_RAVE_A1_ID = AERIAL_RAVE_A_PREFIX + "1";
    public static final String AERIAL_RAVE_A1_END_ID = AERIAL_RAVE_A1_ID + END_SUFFIX;
    
    public static final String AERIAL_RAVE_A2_ID = AERIAL_RAVE_A_PREFIX + "2";
    public static final String AERIAL_RAVE_A2_END_ID = AERIAL_RAVE_A2_ID + END_SUFFIX;
    public static final String AERIAL_RAVE_A2_END2_ID = AERIAL_RAVE_A2_ID + END2_SUFFIX;
    
    public static final String AERIAL_RAVE_A3_ID = AERIAL_RAVE_A_PREFIX + "3";
    public static final String AERIAL_RAVE_A3_END_ID = AERIAL_RAVE_A3_ID + END_SUFFIX;
    
    public static final String AERIAL_RAVE_B_PREFIX = AERIAL_RAVE_PREFIX + "b";
    
    public static final String AERIAL_RAVE_B3_ID = AERIAL_RAVE_B_PREFIX + "3";
    public static final String AERIAL_RAVE_B3_END_ID = AERIAL_RAVE_B3_ID + END_SUFFIX;
    
    public static final String AERIAL_RAVE_B4_ID = AERIAL_RAVE_B_PREFIX + "4";
    public static final String AERIAL_RAVE_B4_LOOP_ID = AERIAL_RAVE_B4_ID + LOOP_SUFFIX;
    public static final String AERIAL_RAVE_B4_LOOP2_ID = AERIAL_RAVE_B4_ID + LOOP2_SUFFIX;
    public static final String AERIAL_RAVE_B4_LOOP3_ID = AERIAL_RAVE_B4_ID + LOOP3_SUFFIX;
    public static final String AERIAL_RAVE_B4_END_ID = AERIAL_RAVE_B4_ID + END_SUFFIX;
    public static final String AERIAL_RAVE_B4_END2_ID = AERIAL_RAVE_B4_ID + END2_SUFFIX;
    
    public static final String UPPER_SLASH_ID = DREAD_STORM_PREFIX + "upper_slash";
    public static final String UPPER_SLASH_END_ID = UPPER_SLASH_ID + END_SUFFIX;
    public static final String UPPER_SLASH_END2_ID = UPPER_SLASH_ID + END2_SUFFIX;
    
    public static final String UPPER_SLASH2_ID = UPPER_SLASH_ID + "2";
    public static final String UPPER_SLASH2_END_ID = UPPER_SLASH2_ID + END_SUFFIX;
    
    public static final String UPPER_SLASH_JUMP_ID = UPPER_SLASH_ID + "_jump";
    public static final String UPPER_SLASH_JUMP_END_ID = UPPER_SLASH_JUMP_ID + END_SUFFIX;
    
    public static final String AERIAL_CLEAVE_ID = DREAD_STORM_PREFIX + "aerial_cleave";
    public static final String AERIAL_CLEAVE_SLASH_ID = AERIAL_CLEAVE_ID + "_slash";
    public static final String AERIAL_CLEAVE_END_ID = AERIAL_CLEAVE_ID + END_SUFFIX;
    
    public static final String DIVING_SLASH_ID = DREAD_STORM_PREFIX + "diving_slash";
    public static final String DIVING_SLASH_END_ID = DIVING_SLASH_ID + END_SUFFIX;
    public static final String DIVING_SLASH_END2_ID = DIVING_SLASH_ID + END2_SUFFIX;
    
    public static final String RAPID_SLASH_ID = DREAD_STORM_PREFIX + "rapid_slash";
    public static final String RAPID_SLASH_QUICK_ID = RAPID_SLASH_ID + "_quick";
    public static final String RAPID_SLASH_END_ID = RAPID_SLASH_ID + END_SUFFIX;
    public static final String RAPID_SLASH_END2_ID = RAPID_SLASH_ID + END2_SUFFIX;
    
    public static final String RISING_STAR_ID = DREAD_STORM_PREFIX + "rising_star";
    public static final String RISING_STAR_END_ID = RISING_STAR_ID + END_SUFFIX;
    
    public static final String DIVING_STAR_ID = DREAD_STORM_PREFIX + "diving_star";
    public static final String DIVING_STAR_LOOP_ID = DIVING_STAR_ID + LOOP_SUFFIX;
    public static final String DIVING_STAR_END_ID = DIVING_STAR_ID + END_SUFFIX;
    public static final String DIVING_STAR_END2_ID = DIVING_STAR_ID + END2_SUFFIX;
    
    public static final String VOID_SLASH_ID = DREAD_STORM_PREFIX + "void_slash";
    public static final String VOID_SLASH_END_ID = VOID_SLASH_ID + END_SUFFIX;
    
    public static final String NO_SHADOW_ID = DREAD_STORM_PREFIX + "no_shadow";
    public static final String NO_SHADOW_END_ID = NO_SHADOW_ID + END_SUFFIX;
    
    public static final String JUDGEMENT_CUT_ID = DREAD_STORM_PREFIX + "judgement_cut";
    public static final String JUDGEMENT_CUT_SLASH_ID = JUDGEMENT_CUT_ID + "_slash";
    public static final String JUDGEMENT_CUT_SHEATH_ID = JUDGEMENT_CUT_ID + "_sheath";
    
    public static final String JUDGEMENT_CUT_SLASH_AIR_ID = JUDGEMENT_CUT_SLASH_ID + "_air";
    public static final String JUDGEMENT_CUT_SHEATH_AIR_ID = JUDGEMENT_CUT_SHEATH_ID + "_air";
    
    public static final String JUDGEMENT_CUT_SLASH_JUST_ID = JUDGEMENT_CUT_SLASH_ID + "_just";
    public static final String JUDGEMENT_CUT_SLASH_JUST2_ID = JUDGEMENT_CUT_SLASH_JUST_ID + "2";
    public static final String JUDGEMENT_CUT_SLASH_JUST3_ID = JUDGEMENT_CUT_SLASH_JUST_ID + "3";
    public static final String JUDGEMENT_CUT_SLASH_JUST_SHEATH_ID = JUDGEMENT_CUT_SLASH_JUST_ID + "_sheath";
    
    public static final String JUDGEMENT_CUT_STINGER_ID = JUDGEMENT_CUT_ID + "_stinger";
    public static final String JUDGEMENT_CUT_STINGER_END_ID = JUDGEMENT_CUT_ID + "_stinger" + END_SUFFIX;
    public static final String JUDGEMENT_CUT_STINGER_END2_ID = JUDGEMENT_CUT_ID + "_stinger" + END2_SUFFIX;
    
    public static final ResourceLocation COMBO_A4_MOTION_LOC = TEBlade.vmdLocBlade(COMBO_A4_ID);
    public static final ResourceLocation COMBO_B_MOTION_LOC = TEBlade.vmdLocBlade(COMBO_B_PREFIX);
    public static final ResourceLocation AERIAL_RAVE_B4_LOOP_MOTION_LOC = TEBlade.vmdLocBlade(AERIAL_RAVE_B4_LOOP_ID);
    public static final ResourceLocation UPPER_SLASH_MOTION_LOC = TEBlade.vmdLocBlade(UPPER_SLASH_ID);
    public static final ResourceLocation RAPID_SLASH_MOTION_LOC = TEBlade.vmdLocBlade(RAPID_SLASH_ID);
    public static final ResourceLocation DIVING_STAR_MOTION_LOC = TEBlade.vmdLocBlade(DIVING_STAR_ID);
    public static final ResourceLocation NO_SHADOW_MOTION_LOC = TEBlade.vmdLocBlade(NO_SHADOW_ID);
    public static final ResourceLocation JUDGEMENT_CUT_STINGER_MOTION_LOC = TEBladeComboStateRegistry.JUDGEMENT_CUT_STINGER_MOTION_LOC;
    //</editor-fold>
    
    public static final DeferredHolder<ComboState, ComboState> DREAD_STORM_STANDBY = COMBO_STATE.register(DREAD_STORM_PREFIX + "standby",
        ComboState.Builder.newInstance().startAndEnd(0, 1).loop().timeout(1000).motionLoc(DefaultResources.ExMotionLocation)
            .next(DreadStormComboCommands::processComboCommand)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            ::build);
    
    public static final ComboState.TimeLineTickAction DIVING_STAR_TICK_ACTION = ComboState.TimeLineTickAction.getBuilder()
        .put(0, entity -> {
            EntitySlashEffect slash = AttackManager.doSlash(entity, -50, Vec3.ZERO, false, false, 0.3, KnockBacks.cancel);
            if (slash != null) {
                slash.setYRot(slash.getYRot() + 180);
                slash.setLifetime(5);
            }
        })
        .put(1, entity -> {
            EntitySlashEffect slash = AttackManager.doSlash(entity, 50, Vec3.ZERO, true, true, 0.3, KnockBacks.cancel);
            if (slash != null) {
                slash.setLifetime(5);
            }
        })
        .build();
    
    //<editor-fold desc="———————————————————— Combo A ————————————————————">
    /*
        在地面：
        R R R R R
        
        ~,R → Combo B
          ~,R → Combo C
            ~,R → Combo C4
              ~,R → Rapid Ex
    */
    public static final DeferredHolder<ComboState, ComboState> COMBO_A1 = COMBO_STATE.register(COMBO_A1_ID,
        ComboState.Builder.newInstance().startAndEnd(1, 10).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(5, entity -> TEBlade.prefix(COMBO_A2_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_A1_END_ID))
            .clickAction(entity -> AttackManager.doSlash(entity, -10, true, false, 0.4))
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_A1_END = COMBO_STATE.register(COMBO_A1_END_ID,
        ComboState.Builder.newInstance().startAndEnd(10, 21).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(COMBO_B2_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_A1_END2_ID))
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_A1_END2 = COMBO_STATE.register(COMBO_A1_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(21, 41).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_A2 = COMBO_STATE.register(COMBO_A2_ID,
        ComboState.Builder.newInstance().startAndEnd(100, 115).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(5, entity -> TEBlade.prefix(COMBO_A3_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_A2_END_ID))
            .clickAction((e) -> AttackManager.doSlash(e, 180 - 10, true, false, 0.5))
            .addHitEffect(StunManager::setStun)::build);
    public static final DeferredHolder<ComboState, ComboState> COMBO_A2_END = COMBO_STATE.register(COMBO_A2_END_ID,
        ComboState.Builder.newInstance().startAndEnd(115, 132).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(COMBO_C3_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_A2_END2_ID))
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_A2_END2 = COMBO_STATE.register(COMBO_A2_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(132, 151).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_A3 = COMBO_STATE.register(COMBO_A3_ID,
        ComboState.Builder.newInstance().startAndEnd(400, 459).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> {
                long elapsed = ComboState.getElapsed(entity);
                if (elapsed > 5) {
                    if (elapsed > 20) {
                        return TEBlade.prefix(COMBO_C4_ID);
                    }
                    return TEBlade.prefix(COMBO_A4_ID);
                }
                return BladeStateAccess.of(entity.getMainHandItem()).map(ISlashBladeState::getComboSeq).orElse(MrqxComboStateRegistry.NONE_COMBO);
            })
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_A3_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(2, entity -> AttackManager.doSlash(entity, -30, false, false, 1.3))
                .put(3, entity -> AttackManager.doSlash(entity, -35, true, false, 1.3))
                .build())
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_A3_END = COMBO_STATE.register(COMBO_A3_END_ID,
        ComboState.Builder.newInstance().startAndEnd(459, 488).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_A4 = COMBO_STATE.register(COMBO_A4_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 21).priority(100).motionLoc(COMBO_A4_MOTION_LOC)
            .next(ComboState.TimeoutNext.buildFromFrame(15, entity -> TEBlade.prefix(COMBO_A5_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_A4_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(4, entity -> {
                    EntitySlashEffect slash = AttackManager.doSlash(entity, 270, true, false, 0.7);
                    if (slash != null) {
                        slash.setXRot(100);
                        slash.setYRot(slash.getYRot() + 180);
                        slash.setBaseSize(1.8F);
                    }
                })
                .put(6, entity -> {
                    EntitySlashEffect slash = AttackManager.doSlash(entity, 90, false, false, 0.8);
                    if (slash != null) {
                        slash.setXRot(-100);
                        slash.setBaseSize(1.7F);
                    }
                })
                .build())
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_A4_END = COMBO_STATE.register(COMBO_A4_END_ID,
        ComboState.Builder.newInstance().startAndEnd(21, 79).priority(100).motionLoc(COMBO_A4_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_A4_END2_ID))::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_A4_END2 = COMBO_STATE.register(COMBO_A4_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(79, 93).priority(100).motionLoc(COMBO_A4_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_A5 = COMBO_STATE.register(COMBO_A5_ID,
        ComboState.Builder.newInstance().startAndEnd(500, 576).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(21, MrqxComboStateRegistry.NONE_COMBO_FUNCTION))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_A5_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(8, entity -> AttackManager.doSlash(entity, 45, false, false, 0.5))
                .put(9, entity -> AttackManager.doSlash(entity, 50, true, false, 0.5))
                .build())
            .addHitEffect(StunManager::setStun)::build);
    public static final DeferredHolder<ComboState, ComboState> COMBO_A5_END = COMBO_STATE.register(COMBO_A5_END_ID,
        ComboState.Builder.newInstance().startAndEnd(576, 608).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— Combo B ————————————————————">
    /*
        在地面：
        R,R,R R
            ~,R → Combo C4
          ~ R → Combo C3
    */
    public static final DeferredHolder<ComboState, ComboState> COMBO_B2 = COMBO_STATE.register(COMBO_B2_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 21).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(ComboState.TimeoutNext.buildFromFrame(12, entity -> TEBlade.prefix(COMBO_C3_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_B2_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(5, entity -> AttackManager.doSlash(entity, 170, false, false, 1.9))
                .build())
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_B2_END = COMBO_STATE.register(COMBO_B2_END_ID,
        ComboState.Builder.newInstance().startAndEnd(21, 25).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(entity -> TEBlade.prefix(COMBO_B3_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_B2_END2_ID))
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_B2_END2 = COMBO_STATE.register(COMBO_B2_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(25, 80).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_B2_END3_ID))::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_B2_END3 = COMBO_STATE.register(COMBO_B2_END3_ID,
        ComboState.Builder.newInstance().startAndEnd(80, 101).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_B3 = COMBO_STATE.register(COMBO_B3_ID,
        ComboState.Builder.newInstance().startAndEnd(120, 140).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(ComboState.TimeoutNext.buildFromFrame(12, entity -> TEBlade.prefix(COMBO_B4_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_B3_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(6, entity -> AttackManager.doSlash(entity, 5, false, false, 2.2))
                .build())
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_B3_END = COMBO_STATE.register(COMBO_B3_END_ID,
        ComboState.Builder.newInstance().startAndEnd(140, 157).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(entity -> TEBlade.prefix(COMBO_C4_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_B3_END2_ID))
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_B3_END2 = COMBO_STATE.register(COMBO_B3_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(157, 172).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_B3_END3_ID))::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_B3_END3 = COMBO_STATE.register(COMBO_B3_END3_ID,
        ComboState.Builder.newInstance().startAndEnd(172, 195).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_B4 = COMBO_STATE.register(COMBO_B4_ID,
        ComboState.Builder.newInstance().startAndEnd(210, 231).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(entity -> TEBlade.prefix(COMBO_B4_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_B4_END_ID))
            .addTickAction(entity -> {
                long elapsed = ComboState.getElapsed(entity);
                Level level = entity.level();
                
                if (4 <= elapsed && elapsed < 14) {
                    if (elapsed < 10) {
                        IInputState capState = entity.getData(CapabilityInputState.INPUT_STATE.get());
                        boolean isRightDown = capState.getCommands().contains(InputCommand.R_DOWN);
                        
                        if (isRightDown) {
                            ComboModifyHandler.step(entity, 2.5);
                        }
                    } else if (entity.onGround()) {
                        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 1, 0));
                    }
                    AttackManager.areaAttack(entity, KnockBacks.smash.action, 1, true, true, true);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY(), entity.getZ(), 10, 0.3, 0.3, 0.3, 0.15);
                    }
                }
            })
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_B4_END = COMBO_STATE.register(COMBO_B4_END_ID,
        ComboState.Builder.newInstance().startAndEnd(231, 289).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(entity -> TEBlade.prefix(COMBO_B4_END_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_B4_END2_ID))
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_B4_END2 = COMBO_STATE.register(COMBO_B4_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(289, 307).priority(100).motionLoc(COMBO_B_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— Combo C ————————————————————">
    /*
        在地面：
        R R,R R R
            ~,R → Combo D
              ~,R → Combo A5
    */
    public static final DeferredHolder<ComboState, ComboState> COMBO_C3 = COMBO_STATE.register(COMBO_C3_ID,
        ComboState.Builder.newInstance().startAndEnd(200, 218).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(9, entity -> TEBlade.prefix(COMBO_C4_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_C3_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(2, entity -> AttackManager.doSlash(entity, -61, false, false, 0.4))
                .put(6, entity -> AttackManager.doSlash(entity, 180 - 42, false, false, 0.7))
                .build())
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_C3_END = COMBO_STATE.register(COMBO_C3_END_ID,
        ComboState.Builder.newInstance().startAndEnd(218, 230).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(COMBO_D1_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_C3_END2_ID))::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_C3_END2 = COMBO_STATE.register(COMBO_C3_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(230, 281).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_C3_END3_ID))::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_C3_END3 = COMBO_STATE.register(COMBO_C3_END3_ID,
        ComboState.Builder.newInstance().startAndEnd(281, 306).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_C4 = COMBO_STATE.register(COMBO_C4_ID,
        ComboState.Builder.newInstance().startAndEnd(800, 839).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(22, entity -> TEBlade.prefix(COMBO_C5_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_C4_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(7, entity -> AttackManager.doSlash(entity, 70, false, false, 0.8))
                .put(14, entity -> AttackManager.doSlash(entity, 180 + 75, false, false, 0.8))
                .build())
            .addHitEffect(StunManager::setStun)::build);
    public static final DeferredHolder<ComboState, ComboState> COMBO_C4_END = COMBO_STATE.register(COMBO_C4_END_ID,
        ComboState.Builder.newInstance().startAndEnd(839, 877).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(COMBO_A5_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_C4_END2_ID))::build);
    public static final DeferredHolder<ComboState, ComboState> COMBO_C4_END2 = COMBO_STATE.register(COMBO_C4_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(877, 894).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_C5 = COMBO_STATE.register(COMBO_C5_ID,
        ComboState.Builder.newInstance().startAndEnd(900, 1013).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(33, MrqxComboStateRegistry.NONE_COMBO_FUNCTION))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_C5_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(15, entity -> {
                    EntitySlashEffect slash = AttackManager.doSlash(entity, 35, false, true, 2.64);
                    if (slash != null) {
                        slash.setBaseSize(2);
                    }
                })
                .put(17, entity -> {
                    EntitySlashEffect slash = AttackManager.doSlash(entity, 35, false, true, 2.64);
                    if (slash != null) {
                        slash.setBaseSize(2);
                    }
                })
                .put(19, entity -> {
                    EntitySlashEffect slash = AttackManager.doSlash(entity, 35, false, true, 2.64);
                    if (slash != null) {
                        slash.setBaseSize(2);
                    }
                })
                .build())
            .addHitEffect(StunManager::setStun)::build);
    public static final DeferredHolder<ComboState, ComboState> COMBO_C5_END = COMBO_STATE.register(COMBO_C5_END_ID,
        ComboState.Builder.newInstance().startAndEnd(1013, 1061).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— Combo D ————————————————————">
    /*
        在地面：
        R R,R,R RRR...
              ~ → 直接尾刀
        TODO: 尾刀后再接两刀
    */
    public static final DeferredHolder<ComboState, ComboState> COMBO_D1 = COMBO_STATE.register(COMBO_D1_ID,
        ComboState.Builder.newInstance().startAndEnd(720, 743).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(COMBO_D2_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_D1_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(12 - 3, entity -> AttackManager.doSlash(entity, 0,
                    new Vec3(entity.getRandom().nextFloat() - 0.5f, 0.8f, 0), false, true, 0.8))
                .put(13 - 3, entity -> AttackManager.doSlash(entity, 5,
                    new Vec3(entity.getRandom().nextFloat() - 0.5f, 0.8f, 0), true, false, 0.8))
                .build())
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_D1_END = COMBO_STATE.register(COMBO_D1_END_ID,
        ComboState.Builder.newInstance().startAndEnd(743, 764).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_D1_END2_ID))::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_D1_END2 = COMBO_STATE.register(COMBO_D1_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(764, 787).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_D_END = COMBO_STATE.register(COMBO_D_END_ID,
        ComboState.Builder.newInstance().startAndEnd(720, 743).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(COMBO_D_END_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_D1_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(12 - 3, entity -> AttackManager.doSlash(entity, 0,
                    new Vec3(entity.getRandom().nextFloat() - 0.5f, 0.8f, 0), false, true, 0.8))
                .put(13 - 3, entity -> AttackManager.doSlash(entity, 5,
                    new Vec3(entity.getRandom().nextFloat() - 0.5f, 0.8f, 0), true, false, 0.8))
                .build())
            .addHitEffect(StunManager::setStun)::build);
    
    //<editor-fold desc="———————————————————— Combo D Slashes ————————————————————">
    
    public static final ComboState.TimeLineTickAction COMBO_D_TICK_ACTION = ComboState.TimeLineTickAction.getBuilder()
        .put(0, entity -> AttackManager.doSlash(entity, -90 + 180 * entity.getRandom().nextFloat(),
            AttackManager.genRushOffset(entity), false, false, 0.15))
        .put(1, entity -> AttackManager.doSlash(entity, 90 + 180 * entity.getRandom().nextFloat(),
            AttackManager.genRushOffset(entity), true, false, 0.15))
        .put(2, entity -> AttackManager.doSlash(entity, -90 + 180 * entity.getRandom().nextFloat(),
            AttackManager.genRushOffset(entity), false, false, 0.15))
        .put(3, entity -> AttackManager.doSlash(entity, 90 + 180 * entity.getRandom().nextFloat(),
            AttackManager.genRushOffset(entity), true, false, 0.15))
        .put(4, entity -> AttackManager.doSlash(entity, -90 + 180 * entity.getRandom().nextFloat(),
            AttackManager.genRushOffset(entity), false, false, 0.15))
        .put(5, entity -> AttackManager.doSlash(entity, 90 + 180 * entity.getRandom().nextFloat(),
            AttackManager.genRushOffset(entity), true, false, 0.15))
        .put(6, entity -> AttackManager.doSlash(entity, -90 + 180 * entity.getRandom().nextFloat(),
            AttackManager.genRushOffset(entity), false, false, 0.15))
        .build();
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_D2 = COMBO_STATE.register(COMBO_D2_ID,
        ComboState.Builder.newInstance().startAndEnd(710, 720).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(6, entity -> TEBlade.prefix(COMBO_D3_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_D_END_ID))
            .addTickAction(COMBO_D_TICK_ACTION)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_D3 = COMBO_STATE.register(COMBO_D3_ID,
        ComboState.Builder.newInstance().startAndEnd(710, 720).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(6, entity -> TEBlade.prefix(COMBO_D4_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_D_END_ID))
            .addTickAction(COMBO_D_TICK_ACTION)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_D4 = COMBO_STATE.register(COMBO_D4_ID,
        ComboState.Builder.newInstance().startAndEnd(710, 720).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(6, entity -> TEBlade.prefix(COMBO_D5_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_D_END_ID))
            .addTickAction(COMBO_D_TICK_ACTION)
            .addHitEffect(StunManager::setStun)::build);
    public static final DeferredHolder<ComboState, ComboState> COMBO_D5 = COMBO_STATE.register(COMBO_D5_ID,
        ComboState.Builder.newInstance().startAndEnd(710, 720).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(6, entity -> TEBlade.prefix(COMBO_D6_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_D_END_ID))
            .addTickAction(COMBO_D_TICK_ACTION)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_D6 = COMBO_STATE.register(COMBO_D6_ID,
        ComboState.Builder.newInstance().startAndEnd(710, 720).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(COMBO_D7_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_D_END_ID))
            .addTickAction(COMBO_D_TICK_ACTION)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_D7 = COMBO_STATE.register(COMBO_D7_ID,
        ComboState.Builder.newInstance().startAndEnd(710, 720).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(COMBO_D8_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_D_END_ID))
            .addTickAction(COMBO_D_TICK_ACTION)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> COMBO_D8 = COMBO_STATE.register(COMBO_D8_ID,
        ComboState.Builder.newInstance().startAndEnd(710, 720).priority(100).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(COMBO_D8_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(COMBO_D_END_ID))
            .addTickAction(COMBO_D_TICK_ACTION)
            .addHitEffect(StunManager::setStun)::build);
    //</editor-fold>
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— Aerial Rave A ————————————————————">
    /*
        在空中：
        R R R
          ~,R → Aerial Rave B
        TODO: 空中突刺，CG 螺旋斩
    */
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_A1 = COMBO_STATE.register(AERIAL_RAVE_A1_ID,
        ComboState.Builder.newInstance().startAndEnd(1100, 1122).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(5, entity -> TEBlade.prefix(AERIAL_RAVE_A2_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_RAVE_A1_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(2, entity -> AttackManager.doSlash(entity, -20, false, false, 0.5))
                .build().andThen(FallHandler::fallDecrease))
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_A1_END = COMBO_STATE.register(AERIAL_RAVE_A1_END_ID,
        ComboState.Builder.newInstance().startAndEnd(1122, 1132).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_A2 = COMBO_STATE.register(AERIAL_RAVE_A2_ID,
        ComboState.Builder.newInstance().startAndEnd(1200, 1210).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(5, entity -> TEBlade.prefix(AERIAL_RAVE_A3_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_RAVE_A2_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(2, entity -> AttackManager.doSlash(entity, 180 - 30, false, false, 0.6))
                .build())
            .addHitEffect(StunManager::setStun).addTickAction(FallHandler::fallDecrease)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_A2_END = COMBO_STATE.register(AERIAL_RAVE_A2_END_ID,
        ComboState.Builder.newInstance().startAndEnd(1210, 1231).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(AERIAL_RAVE_B3_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_RAVE_A2_END2_ID))
            .addTickAction(FallHandler::fallDecrease)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_A2_END2 = COMBO_STATE.register(AERIAL_RAVE_A2_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(1231, 1241).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_A3 = COMBO_STATE.register(AERIAL_RAVE_A3_ID,
        ComboState.Builder.newInstance().startAndEnd(1300, 1328).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(9, MrqxComboStateRegistry.NONE_COMBO_FUNCTION))
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_RAVE_A3_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(2, entity -> AttackManager.doSlash(entity, 0, Vec3.ZERO, false, false, 0.6, KnockBacks.smash))
                .put(3, entity -> AttackManager.doSlash(entity, -3, Vec3.ZERO, true, true, 0.6, KnockBacks.smash))
                .build())
            .addTickAction(FallHandler::fallDecrease)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_A3_END = COMBO_STATE.register(AERIAL_RAVE_A3_END_ID,
        ComboState.Builder.newInstance().startAndEnd(1328, 1338).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— Aerial Rave B ————————————————————">
    /*
        在空中：
        R R,R R
            ~,R → Rising Star
    */
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_B3 = COMBO_STATE.register(AERIAL_RAVE_B3_ID,
        ComboState.Builder.newInstance().startAndEnd(1400, 1437).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(9, entity -> TEBlade.prefix(AERIAL_RAVE_B4_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_RAVE_B3_END_ID))
            .clickAction(entity -> {
                Vec3 motion = entity.getDeltaMovement();
                entity.setDeltaMovement(motion.x, 0.6, motion.z);
            })
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(3, entity -> AttackManager.doSlash(entity, 180 + 57, Vec3.ZERO, false, false, 0.8, KnockBacks.toss))
                .put(6, entity -> AttackManager.doSlash(entity, 180 + 57, Vec3.ZERO, false, false, 0.8, KnockBacks.toss))
                .build())
            .addTickAction(FallHandler::fallResist).addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_B3_END = COMBO_STATE.register(AERIAL_RAVE_B3_END_ID,
        ComboState.Builder.newInstance().startAndEnd(1437, 1443).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(RISING_STAR_ID))
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_B4 = COMBO_STATE.register(AERIAL_RAVE_B4_ID,
        ComboState.Builder.newInstance().startAndEnd(1500, 1513).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(AERIAL_RAVE_B4_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_RAVE_B4_LOOP_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(6, entity -> AttackManager.doSlash(entity, 45, Vec3.ZERO, false, false, 0.3, KnockBacks.cancel))
                .put(7, entity -> AttackManager.doSlash(entity, 50, Vec3.ZERO, true, true, 0.3, KnockBacks.cancel))
                .build())
            .addTickAction(FallHandler::fallResist)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_B4_LOOP = COMBO_STATE.register(AERIAL_RAVE_B4_LOOP_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 5).priority(80).motionLoc(AERIAL_RAVE_B4_LOOP_MOTION_LOC)
            .next(entity -> TEBlade.prefix(AERIAL_RAVE_B4_LOOP_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_RAVE_B4_LOOP2_ID))
            .addTickAction(DIVING_STAR_TICK_ACTION)
            .addTickAction(FallHandler::fallResist)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_B4_LOOP2 = COMBO_STATE.register(AERIAL_RAVE_B4_LOOP2_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 5).priority(80).motionLoc(AERIAL_RAVE_B4_LOOP_MOTION_LOC)
            .next(entity -> TEBlade.prefix(AERIAL_RAVE_B4_LOOP2_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_RAVE_B4_LOOP3_ID))
            .addTickAction(DIVING_STAR_TICK_ACTION)
            .addTickAction(FallHandler::fallResist)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_B4_LOOP3 = COMBO_STATE.register(AERIAL_RAVE_B4_LOOP3_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 5).priority(80).motionLoc(AERIAL_RAVE_B4_LOOP_MOTION_LOC)
            .next(entity -> TEBlade.prefix(AERIAL_RAVE_B4_LOOP3_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_RAVE_B4_END_ID))
            .addTickAction(DIVING_STAR_TICK_ACTION)
            .addTickAction(FallHandler::fallResist)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_B4_END = COMBO_STATE.register(AERIAL_RAVE_B4_END_ID,
        ComboState.Builder.newInstance().startAndEnd(1513, 1537).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_RAVE_B4_END2_ID))
            .addTickAction(FallHandler::fallDecrease)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_RAVE_B4_END2 = COMBO_STATE.register(AERIAL_RAVE_B4_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(1537, 1547).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— Upper Slash ————————————————————">
    /*
        在地面：
        S+R R
          ~-- → Upper Slash Jump
    */
    public static final DeferredHolder<ComboState, ComboState> UPPER_SLASH = COMBO_STATE.register(UPPER_SLASH_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 25).priority(90).motionLoc(UPPER_SLASH_MOTION_LOC)
            .next(ComboState.TimeoutNext.buildFromFrame(11, entity -> TEBlade.prefix(UPPER_SLASH2_ID)))
            .nextOfTimeout(entity -> TEBlade.prefix(UPPER_SLASH_END_ID))
            .addHoldAction(entity -> {
                if (entity.level().isClientSide()) {
                    return;
                }
                int elapsed = entity.getTicksUsingItem();
                
                int fireTime = (int) TimeValueHelper.getTicksFromFrames(9);
                if (fireTime != elapsed) {
                    return;
                }
                
                IInputState inputState = entity.getData(CapabilityInputState.INPUT_STATE.get());
                EnumSet<InputCommand> commands = inputState.getCommands(entity);
                
                if (!commands.contains(InputCommand.R_DOWN)) {
                    return;
                }
                
                BladeStateAccess.of(entity.getMainHandItem())
                    .ifPresent(state -> state.updateComboSeq(entity, TEBlade.prefix(UPPER_SLASH_JUMP_ID)));
            })
            .addTickAction(entity -> {
                if (AttackManager.isPowered(entity)) {
                    TruePowerComboHelper.POWERED_UPPER_SLASH.accept(entity);
                } else {
                    TruePowerComboHelper.UPPER_SLASH.accept(entity);
                }
            })
            .addHitEffect((t, a) -> StunManager.setStun(t, 15))::build);
    
    public static final DeferredHolder<ComboState, ComboState> UPPER_SLASH_END = COMBO_STATE.register(UPPER_SLASH_END_ID,
        ComboState.Builder.newInstance().startAndEnd(25, 65).priority(90).motionLoc(UPPER_SLASH_MOTION_LOC)
            .next(entity -> TEBlade.prefix(UPPER_SLASH_END_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(UPPER_SLASH_END2_ID))
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    public static final DeferredHolder<ComboState, ComboState> UPPER_SLASH_END2 = COMBO_STATE.register(UPPER_SLASH_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(65, 97).priority(90).motionLoc(UPPER_SLASH_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> UPPER_SLASH2 = COMBO_STATE.register(UPPER_SLASH2_ID,
        ComboState.Builder.newInstance().startAndEnd(110, 159).priority(90).motionLoc(UPPER_SLASH_MOTION_LOC)
            .next(entity -> TEBlade.prefix(UPPER_SLASH2_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(UPPER_SLASH2_END_ID))
            .clickAction(entity -> AttackManager.doSlash(entity, 110, Vec3.ZERO, false, false, 0.9, KnockBacks.smash))
            .addHitEffect((t, a) -> StunManager.setStun(t, 15))::build);
    
    public static final DeferredHolder<ComboState, ComboState> UPPER_SLASH2_END = COMBO_STATE.register(UPPER_SLASH2_END_ID,
        ComboState.Builder.newInstance().startAndEnd(159, 182).priority(90).motionLoc(UPPER_SLASH_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(entity -> TEBlade.prefix(UPPER_SLASH_END2_ID))
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> UPPER_SLASH_JUMP = COMBO_STATE.register(UPPER_SLASH_JUMP_ID,
        ComboState.Builder.newInstance().startAndEnd(1700, 1713).priority(90).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(7, MrqxComboStateRegistry.NONE_COMBO_FUNCTION))
            .nextOfTimeout(entity -> TEBlade.prefix(UPPER_SLASH_JUMP_END_ID))
            .clickAction(entity -> {
                Vec3 motion = entity.getDeltaMovement();
                entity.setDeltaMovement(motion.x, 0.6f, motion.z);
                entity.setOnGround(false);
                entity.hasImpulse = true;
            })
            .addTickAction(entity -> {
                Vec3 motion = entity.getDeltaMovement();
                entity.setDeltaMovement(motion.x, 0.6f, motion.z);
                AttackManager.areaAttack(entity, target -> {
                }, 0.4f, true, true, true);
            })
            .addTickAction(FallHandler::fallDecrease)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> UPPER_SLASH_JUMP_END = COMBO_STATE.register(UPPER_SLASH_JUMP_END_ID,
        ComboState.Builder.newInstance().startAndEnd(1713, 1717).priority(90).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .clickAction(entity -> AttackManager.doSlash(entity, -80, Vec3.ZERO, false, false, 1.35, KnockBacks.cancel))
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .addTickAction(FallHandler::fallDecrease)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— Aerial Cleave ————————————————————">
    /*
        在空中：
        S+R
          ~-- → Diving Slash
    */
    public static final DeferredHolder<ComboState, ComboState> AERIAL_CLEAVE = COMBO_STATE.register(AERIAL_CLEAVE_ID,
        ComboState.Builder.newInstance().startAndEnd(1800, 1804).priority(70).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(AERIAL_CLEAVE_ID))
            .nextOfTimeout(entity -> {
                IInputState inputState = entity.getData(CapabilityInputState.INPUT_STATE.get());
                EnumSet<InputCommand> commands = inputState.getCommands(entity);
                
                return commands.contains(InputCommand.R_DOWN) ? TEBlade.prefix(DIVING_SLASH_ID) : TEBlade.prefix(AERIAL_CLEAVE_SLASH_ID);
            })
            .clickAction(entity -> {
                Vec3 motion = entity.getDeltaMovement();
                entity.setDeltaMovement(motion.x, 0.1, motion.z);
                entity.fallDistance = 1;
            })::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_CLEAVE_SLASH = COMBO_STATE.register(AERIAL_CLEAVE_SLASH_ID,
        ComboState.Builder.newInstance().startAndEnd(1509, 1537).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(10, MrqxComboStateRegistry.NONE_COMBO_FUNCTION))
            .nextOfTimeout(entity -> TEBlade.prefix(AERIAL_CLEAVE_END_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(0, entity -> AttackManager.doSlash(entity, 45, Vec3.ZERO, false, false, 1, KnockBacks.meteor))
                .put(1, entity -> AttackManager.doSlash(entity, 50, Vec3.ZERO, true, true, 1, KnockBacks.meteor))
                .build())
            .addTickAction(FallHandler::fallDecrease)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> AERIAL_CLEAVE_END = COMBO_STATE.register(AERIAL_CLEAVE_END_ID,
        ComboState.Builder.newInstance().startAndEnd(1537, 1547).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> DIVING_SLASH = COMBO_STATE.register(DIVING_SLASH_ID,
        ComboState.Builder.newInstance().startAndEnd(1812, 1817).priority(70).loop().timeout(1000).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(DIVING_SLASH_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(DIVING_SLASH_ID))
            .addTickAction(entity -> {
                entity.fallDistance = 1;
                
                Vec3 motion = entity.getDeltaMovement();
                entity.setDeltaMovement(motion.x, motion.y - 3.0, motion.z);
                
                if (entity.level().isClientSide()) {
                    return;
                }
                
                long elapsed = ComboState.getElapsed(entity);
                
                if (elapsed % 2 == 0) {
                    AttackManager.areaAttack(entity, KnockBacks.meteor.action, 0.25f, true, false, true);
                }
                
                if (entity.onGround()) {
                    AttackManager.doSlash(entity, 55, Vec3.ZERO, true, true, 1, KnockBacks.meteor);
                    BladeStateAccess.of(entity.getMainHandItem()).ifPresent((state) -> {
                        state.updateComboSeq(entity, TEBlade.prefix(DIVING_SLASH_END_ID));
                        FallHandler.spawnLandingParticle(entity, 20);
                    });
                }
            })
            .addHitEffect((t, a) -> StunManager.setStun(t, 15))::build);
    
    public static final DeferredHolder<ComboState, ComboState> DIVING_SLASH_END = COMBO_STATE.register(DIVING_SLASH_END_ID,
        ComboState.Builder.newInstance().startAndEnd(1816, 1859).priority(70).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(6, MrqxComboStateRegistry.NONE_COMBO_FUNCTION))
            .nextOfTimeout(entity -> SlashBlade.prefix(DIVING_SLASH_END2_ID))
            .clickAction(entity -> AttackManager.doSlash(entity, 60, Vec3.ZERO, false, false, 0.25, KnockBacks.meteor))
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    public static final DeferredHolder<ComboState, ComboState> DIVING_SLASH_END2 = COMBO_STATE.register(DIVING_SLASH_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(1859, 1886).priority(70).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— Rapid Slash ————————————————————">
    /*
        W+R
        在地面：
          ~-- → Rising Star（命中目标时）
        在空中：
          ~-- → Diving Star（命中目标时）
    */
    /**
     * @see DreadStormRapidSlashHandler
     */
    public static final DeferredHolder<ComboState, ComboState> RAPID_SLASH = COMBO_STATE.register(RAPID_SLASH_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 9).priority(70).motionLoc(RAPID_SLASH_MOTION_LOC)
            .next(entity -> TEBlade.prefix(RAPID_SLASH_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(RAPID_SLASH_END_ID))
            .addHoldAction(entity -> {
                if (entity.level().isClientSide()) {
                    return;
                }
                
                int elapsed = entity.getTicksUsingItem();
                if (elapsed < 1) {
                    return;
                }
                
                AttributeModifier rsr = new AttributeModifier(TEBlade.prefix("rapid_slash_reach"),
                    -4, AttributeModifier.Operation.ADD_VALUE);
                AttributeInstance mai = entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
                if (mai != null) {
                    mai.addTransientModifier(rsr);
                }
//                AttackManager.areaAttack(entity, t -> {
                IInputState capState = entity.getData(CapabilityInputState.INPUT_STATE.get());
                boolean isRightDown = capState.getCommands().contains(InputCommand.R_DOWN);
                
                if (isRightDown) {
                    BladeStateAccess.of(entity.getMainHandItem()).ifPresent(state -> {
                        if (state.getComboSeq().equals(TEBlade.prefix(RAPID_SLASH_ID))) {
                            List<Entity> hits = AttackManager.areaAttack(entity, KnockBacks.toss.action, 0.44f,
                                true, true, true);
                            
                            if (!hits.isEmpty() && hits.contains(TruePowerComboHelper.getTarget(entity))) {
                                state.updateComboSeq(entity, entity.onGround()
                                    ? TEBlade.prefix(RISING_STAR_ID)
                                    : TEBlade.prefix(DIVING_STAR_ID)
                                );
                            }
                        }
                    });
                }
//                }, 0.44f, true, false, true);
                if (mai != null) {
                    mai.removeModifier(rsr);
                }
            })
            .addTickAction(entity -> {
                long elapsed = ComboState.getElapsed(entity);
                
                if (elapsed == 0) {
                    entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                
                if (elapsed <= 3 && !entity.level().isClientSide) {
                    Vec3 input = new Vec3(0.0F, 0.0F, 1.0F);
                    entity.moveRelative(entity.onGround() ? 1.0F : 0.3F, input);
                    Vec3 motion = entity.getDeltaMovement();
                    entity.move(MoverType.SELF, motion);
                    MinecraftServer server = entity.getServer();
                    if (server != null) {
                        server.getPlayerList().broadcastAll(new ClientboundSetEntityMotionPacket(entity.getId(), motion));
                    }
                }
                
                if (2 <= elapsed && elapsed < 6) {
                    float roll = -45 + 90 * entity.getRandom().nextFloat();
                    boolean critical = AttackManager.isPowered(entity);
                    
                    if (elapsed % 2 == 0) {
                        roll += 180;
                    }
                    
                    AttackManager.doSlash(entity, roll, AttackManager.genRushOffset(entity), false, critical, 0.15);
                }
            })
            .addHitEffect(StunManager::setStun)
            .addTickAction(FallHandler::fallResist)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RAPID_SLASH_QUICK = COMBO_STATE.register(RAPID_SLASH_QUICK_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 1).priority(70).motionLoc(RAPID_SLASH_MOTION_LOC)
            .next(entity -> TEBlade.prefix(RAPID_SLASH_QUICK_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(RAPID_SLASH_ID))
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)
            .addTickAction(FallHandler::fallResist)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RAPID_SLASH_END = COMBO_STATE.register(RAPID_SLASH_END_ID,
        ComboState.Builder.newInstance().startAndEnd(9, 33).priority(70).motionLoc(RAPID_SLASH_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(entity -> TEBlade.prefix(RAPID_SLASH_END2_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(0, entity -> AttackManager.doSlash(entity, -30, false, true, 0.4))
                .build())
            .addTickAction(FallHandler::fallDecrease)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RAPID_SLASH_END2 = COMBO_STATE.register(RAPID_SLASH_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(33, 56).priority(70).motionLoc(RAPID_SLASH_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> RISING_STAR = COMBO_STATE.register(RISING_STAR_ID,
        ComboState.Builder.newInstance().startAndEnd(2100, 2137).priority(80).motionLoc(DefaultResources.ExMotionLocation)
            .next(ComboState.TimeoutNext.buildFromFrame(18, MrqxComboStateRegistry.NONE_COMBO_FUNCTION))
            .nextOfTimeout(entity -> TEBlade.prefix(RISING_STAR_END_ID))
            .clickAction(entity -> {
                entity.setDeltaMovement(0, 0.7, 0);
                entity.setOnGround(false);
                entity.hasImpulse = true;
                AttackManager.doSlash(entity, -56, Vec3.ZERO, false, false, 0.8, KnockBacks.toss);
            })
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put((int) TimeValueHelper.getTicksFromFrames(9), entity -> AttackManager.doSlash(entity, -57, Vec3.ZERO, false, false, 0.7, KnockBacks.toss))
                .put((int) TimeValueHelper.getTicksFromFrames(17), entity -> AttackManager.doSlash(entity, -58, Vec3.ZERO, false, false, 0.6, KnockBacks.toss))
                .build())
            .addTickAction(entity -> {
                long elapsed = ComboState.getElapsed(entity);
                
                if (elapsed < 3) {
                    Vec3 motion = entity.getDeltaMovement();
                    double yMotion = motion.y;
                    if (yMotion <= 0) {
                        yMotion = 0.7;
                        
                        entity.setOnGround(false);
                        entity.hasImpulse = true;
                    }
                    
                    entity.setDeltaMovement(0, yMotion, 0);
                }
            })
            .addTickAction(FallHandler::fallResist)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RISING_STAR_END = COMBO_STATE.register(RISING_STAR_END_ID,
        ComboState.Builder.newInstance().startAndEnd(2137, 2147).priority(70).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> DIVING_STAR = COMBO_STATE.register(DIVING_STAR_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 10).priority(80).motionLoc(DIVING_STAR_MOTION_LOC)
            .speed(2) // TODO
            .next(entity -> TEBlade.prefix(DIVING_STAR_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(DIVING_STAR_LOOP_ID))
            .addTickAction(FallHandler::fallResist)::build);
    
    public static final DeferredHolder<ComboState, ComboState> DIVING_STAR_LOOP = COMBO_STATE.register(DIVING_STAR_LOOP_ID,
        ComboState.Builder.newInstance().startAndEnd(11, 22).priority(80).motionLoc(DIVING_STAR_MOTION_LOC)
            .speed(2) // TODO
            .next(entity -> TEBlade.prefix(DIVING_STAR_ID))
            .nextOfTimeout(entity -> entity.onGround()
                ? TEBlade.prefix(DIVING_STAR_END_ID)
                : TEBlade.prefix(DIVING_STAR_LOOP_ID)
            )
            .addTickAction(DIVING_STAR_TICK_ACTION)
            .addTickAction(FallHandler::fallDecrease)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> DIVING_STAR_END = COMBO_STATE.register(DIVING_STAR_END_ID,
        ComboState.Builder.newInstance().startAndEnd(30, 53).priority(80).motionLoc(DIVING_STAR_MOTION_LOC)
            .speed(2) // TODO
            .next(entity -> TEBlade.prefix(DIVING_STAR_END_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(DIVING_STAR_END2_ID))
            .addTickAction(DIVING_STAR_TICK_ACTION)
            .addTickAction(FallHandler::fallDecrease)::build);
    
    public static final DeferredHolder<ComboState, ComboState> DIVING_STAR_END2 = COMBO_STATE.register(DIVING_STAR_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(18, 33).priority(80).motionLoc(RAPID_SLASH_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(entity -> TEBlade.prefix(RAPID_SLASH_END2_ID))
            .addTickAction(FallHandler::fallDecrease)::build);
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— Void Slash ————————————————————">
    /*
        在地面：
        S→W+R R(TODO)
    */
    public static final DeferredHolder<ComboState, ComboState> VOID_SLASH = COMBO_STATE.register(VOID_SLASH_ID,
        ComboState.Builder.newInstance().startAndEnd(2200, 2277).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(VOID_SLASH_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(VOID_SLASH_END_ID))
            .addTickAction(entity -> entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(16, entity -> TruePowerAttackManager.doVoidSlashAttack(entity, AttackManager.isPowered(entity) ? TruePowerCommonConfig.POWERED_VOID_SLASH_DAMAGE_FIRST.get() : TruePowerCommonConfig.VOID_SLASH_DAMAGE.get()))
                .put(17, entity -> {
                    if (AttackManager.isPowered(entity)) {
                        TruePowerAttackManager.doVoidSlashAttack(entity, TruePowerCommonConfig.POWERED_VOID_SLASH_DAMAGE_SECOND.get());
                    }
                })
                .build())
            .addTickAction(FallHandler::fallDecrease)
            .addHitEffect((t, a) -> ITruePowerStunData.get(t).triggerStun(60))::build);
    
    public static final DeferredHolder<ComboState, ComboState> VOID_SLASH_END = COMBO_STATE.register(VOID_SLASH_END_ID,
        ComboState.Builder.newInstance().startAndEnd(2278, 2299).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— No Shadow ————————————————————">
    /*
        在空中：
        S→W+R
    */
    public static final DeferredHolder<ComboState, ComboState> NO_SHADOW = COMBO_STATE.register(NO_SHADOW_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 67).priority(50).motionLoc(NO_SHADOW_MOTION_LOC)
            .next(entity -> TEBlade.prefix(VOID_SLASH_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(NO_SHADOW_END_ID))
            .addTickAction(entity -> entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.06F, 0)))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(2, entity -> AttackUtils.doNoShadowAttack(entity, TruePowerCommonConfig.VOID_SLASH_DAMAGE.get() / 2, 18, -20))
                .put(3, entity -> entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.38F, 0)))
                .put(11, entity -> AttackUtils.doNoShadowAttack(entity, TruePowerCommonConfig.VOID_SLASH_DAMAGE.get() / 2, 22, -24))
                .put(12, entity -> entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.38F, 0)))
                .put(23, entity -> AttackUtils.doNoShadowAttack(entity, TruePowerCommonConfig.VOID_SLASH_DAMAGE.get() / 2, 28, -28))
                .put(24, entity -> entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.38F, 0)))
                .build())
            .addTickAction(FallHandler::fallDecrease)
            .addHitEffect((t, a) -> ITruePowerStunData.get(t).triggerStun(60))::build);
    
    public static final DeferredHolder<ComboState, ComboState> NO_SHADOW_END = COMBO_STATE.register(NO_SHADOW_END_ID,
        ComboState.Builder.newInstance().startAndEnd(67, 82).priority(50).motionLoc(NO_SHADOW_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— Judgement Cut ————————————————————">
    public static final Function<LivingEntity, ResourceLocation> JUDGEMENT_CUT_NEXT_STINGER = entity -> {
        if (!entity.onGround()) {
            long elapsed = ComboState.getElapsed(entity);
            if (elapsed < 5) {
                return TEBlade.prefix(JUDGEMENT_CUT_STINGER_ID);
            }
        }
        return MrqxComboStateRegistry.NONE_COMBO;
    };
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT = COMBO_STATE.register(JUDGEMENT_CUT_ID,
        ComboState.Builder.newInstance().startAndEnd(1900, 1923).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(JUDGEMENT_CUT_STINGER_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(JUDGEMENT_CUT_SLASH_ID))
            .addTickAction(entity -> {
                long elapsed = ComboState.getElapsed(entity);
                
                if (elapsed == 0) {
                    entity.playSound(SoundEvents.TRIDENT_THROW.value(), 0.80F, 0.625F + 0.1f * entity.getRandom().nextFloat());
                }
                
                if (elapsed <= 3) {
                    entity.moveRelative(-0.3f, new Vec3(0, 0, 1));
                    Vec3 vec = TruePowerAttackManager.maybeBackOffFromEdge(entity.getDeltaMovement(), entity);
                    entity.move(MoverType.SELF, vec);
                }
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 1, 0));
            }).addTickAction(FallHandler::fallDecrease)::build);
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_SLASH = COMBO_STATE.register(JUDGEMENT_CUT_SLASH_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(JUDGEMENT_CUT_SLASH_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(JUDGEMENT_CUT_SHEATH_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder().put(0, JudgementCut::doJudgementCut).build())
            .addTickAction(FallHandler::fallDecrease).addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_SHEATH = COMBO_STATE.register(JUDGEMENT_CUT_SHEATH_ID,
        ComboState.Builder.newInstance().startAndEnd(1928, 1963).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(JUDGEMENT_CUT_NEXT_STINGER)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_SLASH_AIR = COMBO_STATE.register(JUDGEMENT_CUT_SLASH_AIR_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(JUDGEMENT_CUT_SLASH_AIR_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(JUDGEMENT_CUT_SHEATH_AIR_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder().put(0, JudgementCut::doJudgementCut).build())
            .addTickAction(FallHandler::fallResist)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_SHEATH_AIR = COMBO_STATE.register(JUDGEMENT_CUT_SHEATH_AIR_ID,
        ComboState.Builder.newInstance().startAndEnd(1928, 1963).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(JUDGEMENT_CUT_NEXT_STINGER)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_SLASH_JUST = COMBO_STATE.register(JUDGEMENT_CUT_SLASH_JUST_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(JUDGEMENT_CUT_SLASH_JUST_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(JUDGEMENT_CUT_SLASH_JUST2_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder().put(1, JudgementCut::doJudgementCutJust).build())
            .addTickAction(FallHandler::fallResist).addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_SLASH_JUST2 = COMBO_STATE.register(JUDGEMENT_CUT_SLASH_JUST2_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(JUDGEMENT_CUT_NEXT_STINGER)
            .nextOfTimeout(entity -> TEBlade.prefix(JUDGEMENT_CUT_SLASH_JUST3_ID))
            .addTickAction(FallHandler::fallResist)::build);
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_SLASH_JUST3 = COMBO_STATE.register(JUDGEMENT_CUT_SLASH_JUST3_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(JUDGEMENT_CUT_NEXT_STINGER)
            .nextOfTimeout(entity -> TEBlade.prefix(JUDGEMENT_CUT_SLASH_JUST_SHEATH_ID))
            .addTickAction(FallHandler::fallResist)::build);
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_SHEATH_JUST = COMBO_STATE.register(JUDGEMENT_CUT_SLASH_JUST_SHEATH_ID,
        ComboState.Builder.newInstance().startAndEnd(1928, 1963).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(JUDGEMENT_CUT_NEXT_STINGER)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_STINGER = COMBO_STATE.register(JUDGEMENT_CUT_STINGER_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 27).priority(50).motionLoc(JUDGEMENT_CUT_STINGER_MOTION_LOC)
            .next(entity -> TEBlade.prefix(JUDGEMENT_CUT_STINGER_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(JUDGEMENT_CUT_STINGER_END_ID))
            .addTickAction(entity -> {
                long elapsed = ComboState.getElapsed(entity);
                Level level = entity.level();
                
                if (6 <= elapsed && elapsed < 16) {
                    if (elapsed < 12) {
                        ComboModifyHandler.step(entity, 2.5);
                    } else if (entity.onGround()) {
                        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 1, 0));
                    }
                    AttackManager.areaAttack(entity, KnockBacks.smash.action, 1, true, true, true);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY(), entity.getZ(), 10, 0.3, 0.3, 0.3, 0.15);
                    }
                }
            })
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_STINGER_END = COMBO_STATE.register(JUDGEMENT_CUT_STINGER_END_ID,
        ComboState.Builder.newInstance().startAndEnd(27, 85).priority(100).motionLoc(JUDGEMENT_CUT_STINGER_MOTION_LOC)
            .next(entity -> TEBlade.prefix(JUDGEMENT_CUT_STINGER_END_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(JUDGEMENT_CUT_STINGER_END2_ID))
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    public static final DeferredHolder<ComboState, ComboState> JUDGEMENT_CUT_STINGER_END2 = COMBO_STATE.register(JUDGEMENT_CUT_STINGER_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(85, 105).priority(100).motionLoc(JUDGEMENT_CUT_STINGER_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    public static void init() {
        // trigger clinit
    }
}
