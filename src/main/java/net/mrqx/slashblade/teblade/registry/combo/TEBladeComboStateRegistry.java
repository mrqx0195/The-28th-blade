package net.mrqx.slashblade.teblade.registry.combo;

import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.event.handler.FallHandler;
import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.RayTraceHelper;
import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.registry.MrqxComboStateRegistry;
import net.mrqx.sbr_core.utils.MrqxSummonedSwordArts;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.registry.combo.dreadstorm.DreadStormComboStateRegistry;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.mrqx.truepower.event.handler.ComboModifyHandler;
import net.mrqx.truepower.util.TruePowerAttackManager;
import net.mrqx.truepower.util.TruePowerComboHelper;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class TEBladeComboStateRegistry {
    public static final DeferredRegister<ComboState> COMBO_STATE = DeferredRegister.create(ComboState.REGISTRY_KEY, TEBlade.MODID);
    
    //<editor-fold desc="———————————————————— ID ————————————————————">
    public static final String LOOP_SUFFIX = "_loop";
    public static final String LOOP2_SUFFIX = LOOP_SUFFIX + "2";
    public static final String LOOP3_SUFFIX = LOOP_SUFFIX + "3";
    public static final String END_SUFFIX = "_end";
    public static final String END2_SUFFIX = END_SUFFIX + "2";
    public static final String END3_SUFFIX = END_SUFFIX + "3";
    
    public static final String RAINSHOWER_CUT_ID = "rainshower" + "_cut";
    public static final String RAINSHOWER_CUT_SLASH_ID = RAINSHOWER_CUT_ID + "_slash";
    public static final String RAINSHOWER_CUT_SHEATH_ID = RAINSHOWER_CUT_ID + "_sheath";
    
    public static final String RAINSHOWER_CUT_SLASH_AIR_ID = RAINSHOWER_CUT_SLASH_ID + "_air";
    public static final String RAINSHOWER_CUT_SHEATH_AIR_ID = RAINSHOWER_CUT_SHEATH_ID + "_air";
    
    public static final String RAINSHOWER_CUT_SLASH_JUST_ID = RAINSHOWER_CUT_SLASH_ID + "_just";
    public static final String RAINSHOWER_CUT_SLASH_JUST2_ID = RAINSHOWER_CUT_SLASH_JUST_ID + "2";
    public static final String RAINSHOWER_CUT_SLASH_JUST3_ID = RAINSHOWER_CUT_SLASH_JUST_ID + "3";
    public static final String RAINSHOWER_CUT_SLASH_JUST_SHEATH_ID = RAINSHOWER_CUT_SLASH_JUST_ID + "_sheath";
    
    public static final String RAINSHOWER_CUT_END_ID = RAINSHOWER_CUT_ID + END_SUFFIX;
    
    public static final String RAINSHOWER_CUT_STINGER_ID = RAINSHOWER_CUT_ID + "_stinger";
    public static final String RAINSHOWER_CUT_STINGER_END_ID = RAINSHOWER_CUT_ID + "_stinger" + END_SUFFIX;
    public static final String RAINSHOWER_CUT_STINGER_END2_ID = RAINSHOWER_CUT_ID + "_stinger" + END2_SUFFIX;
    
    public static final ResourceLocation JUDGEMENT_CUT_STINGER_MOTION_LOC = TEBlade.vmdLocBlade("judgement_cut_stinger");
    //</editor-fold>
    
    //<editor-fold desc="———————————————————— RainShower Cut ————————————————————">
    public static final Function<LivingEntity, ResourceLocation> RAINSHOWER_CUT_NEXT_STINGER = entity -> {
        if (!entity.onGround()) {
            long elapsed = ComboState.getElapsed(entity);
            if (elapsed < 5) {
                return TEBlade.prefix(RAINSHOWER_CUT_STINGER_ID);
            }
        }
        return MrqxComboStateRegistry.NONE_COMBO;
    };
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT = COMBO_STATE.register(RAINSHOWER_CUT_ID,
        ComboState.Builder.newInstance().startAndEnd(1900, 1923).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(RAINSHOWER_CUT_STINGER_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(RAINSHOWER_CUT_SLASH_ID))
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
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_SLASH = COMBO_STATE.register(RAINSHOWER_CUT_SLASH_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(RAINSHOWER_CUT_SLASH_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(RAINSHOWER_CUT_SHEATH_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(0, entity -> {
                    Entity target = TruePowerComboHelper.getTarget(entity);
                    MrqxSummonedSwordArts.HEAVY_RAIN_SWORD_POS.accept(entity, target != null ? target.position() : entity.position(), (double) AttackUtils.getPowerLevel(entity), 8);
                })
                .build())
            .addTickAction(FallHandler::fallDecrease).addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_SHEATH = COMBO_STATE.register(RAINSHOWER_CUT_SHEATH_ID,
        ComboState.Builder.newInstance().startAndEnd(1928, 1963).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(RAINSHOWER_CUT_NEXT_STINGER)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_SLASH_AIR = COMBO_STATE.register(RAINSHOWER_CUT_SLASH_AIR_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(RAINSHOWER_CUT_SLASH_AIR_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(RAINSHOWER_CUT_SHEATH_AIR_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(0, entity -> {
                    Entity target = TruePowerComboHelper.getTarget(entity);
                    MrqxSummonedSwordArts.HEAVY_RAIN_SWORD_POS.accept(entity, target != null ? target.position() : entity.position(), (double) AttackUtils.getPowerLevel(entity), 8);
                })
                .build())
            .addTickAction(FallHandler::fallResist)
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_SHEATH_AIR = COMBO_STATE.register(RAINSHOWER_CUT_SHEATH_AIR_ID,
        ComboState.Builder.newInstance().startAndEnd(1928, 1963).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(RAINSHOWER_CUT_NEXT_STINGER)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_SLASH_JUST = COMBO_STATE.register(RAINSHOWER_CUT_SLASH_JUST_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(RAINSHOWER_CUT_SLASH_JUST_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(RAINSHOWER_CUT_SLASH_JUST2_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(1, entity -> {
                    Entity target = TruePowerComboHelper.getTarget(entity);
                    MrqxSummonedSwordArts.HEAVY_RAIN_SWORD_POS.accept(entity, target != null ? target.position() : entity.position(), (double) AttackUtils.getPowerLevel(entity), 8);
                })
                .build())
            .addTickAction(FallHandler::fallResist).addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_SLASH_JUST2 = COMBO_STATE.register(RAINSHOWER_CUT_SLASH_JUST2_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(RAINSHOWER_CUT_NEXT_STINGER)
            .nextOfTimeout(entity -> TEBlade.prefix(RAINSHOWER_CUT_SLASH_JUST3_ID))
            .addTickAction(FallHandler::fallResist)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_SLASH_JUST3 = COMBO_STATE.register(RAINSHOWER_CUT_SLASH_JUST3_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(RAINSHOWER_CUT_NEXT_STINGER)
            .nextOfTimeout(entity -> TEBlade.prefix(RAINSHOWER_CUT_SLASH_JUST_SHEATH_ID))
            .addTickAction(FallHandler::fallResist)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_SHEATH_JUST = COMBO_STATE.register(RAINSHOWER_CUT_SLASH_JUST_SHEATH_ID,
        ComboState.Builder.newInstance().startAndEnd(1928, 1963).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(RAINSHOWER_CUT_NEXT_STINGER)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(FallHandler::fallDecrease)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_END = COMBO_STATE.register(RAINSHOWER_CUT_END_ID,
        ComboState.Builder.newInstance().startAndEnd(1923, 1928).priority(50).motionLoc(DefaultResources.ExMotionLocation)
            .next(entity -> TEBlade.prefix(RAINSHOWER_CUT_END_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(RAINSHOWER_CUT_SLASH_JUST_SHEATH_ID))
            .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                .put(0, entity -> {
                    Optional<HitResult> result = RayTraceHelper.rayTrace(entity.level(), entity, entity.getEyePosition(1.0f),
                        entity.getLookAngle(), 40, 40, e -> true);
                    List<Entity> foundEntities = new ArrayList<>();
                    
                    result.filter(r -> r.getType() == HitResult.Type.ENTITY).filter(r -> {
                        EntityHitResult er = (EntityHitResult) r;
                        Entity target = er.getEntity();
                        
                        if (target instanceof PartEntity) {
                            target = ((PartEntity<?>) target).getParent();
                        }
                        
                        boolean isMatch = false;
                        
                        if (target instanceof LivingEntity) {
                            isMatch = TargetSelector.lockon.test(entity, (LivingEntity) target);
                        }
                        
                        return isMatch;
                    }).map(r -> ((EntityHitResult) r).getEntity()).ifPresent(foundEntities::add);
                    
                    foundEntities.addAll(entity.level().getNearbyEntities(LivingEntity.class,
                        TargetSelector.lockon, entity, entity.getBoundingBox().inflate(12.0D, 6.0D, 12.0D)));
                    
                    foundEntities.forEach(target ->
                        MrqxSummonedSwordArts.HEAVY_RAIN_SWORD_POS.accept(entity, target.position(), (double) AttackUtils.getPowerLevel(entity) * 2, 28));
                    
                    BladeStateAccess.of(entity.getMainHandItem()).ifPresent(state -> state.setBroken(true));
                })
                .build())
            .addTickAction(FallHandler::fallResist).addHitEffect(StunManager::setStun)::build);
    
    // ————————————————————
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_STINGER = COMBO_STATE.register(RAINSHOWER_CUT_STINGER_ID,
        ComboState.Builder.newInstance().startAndEnd(0, 27).priority(50).motionLoc(JUDGEMENT_CUT_STINGER_MOTION_LOC)
            .next(entity -> TEBlade.prefix(RAINSHOWER_CUT_STINGER_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(RAINSHOWER_CUT_STINGER_END_ID))
            .addTickAction(entity -> {
                long elapsed = ComboState.getElapsed(entity);
                Level level = entity.level();
                
                if (6 <= elapsed && elapsed < 16) {
                    if (elapsed < 12) {
                        ComboModifyHandler.step(entity, 2.5);
                        if (TruePowerComboHelper.getTarget(entity) instanceof LivingEntity target) {
                            MrqxSummonedSwordArts.BLISTERING_SWORD.accept(entity, target, (double) AttackUtils.getPowerLevel(entity), 2);
                        }
                    } else if (entity.onGround()) {
                        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 1, 0));
                    }
                    AttackManager.areaAttack(entity, KnockBacks.smash.action, 0.2f, true, true, true);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY(), entity.getZ(), 10, 0.3, 0.3, 0.3, 0.15);
                    }
                }
            })
            .addHitEffect(StunManager::setStun)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_STINGER_END = COMBO_STATE.register(RAINSHOWER_CUT_STINGER_END_ID,
        ComboState.Builder.newInstance().startAndEnd(27, 85).priority(100).motionLoc(JUDGEMENT_CUT_STINGER_MOTION_LOC)
            .next(entity -> TEBlade.prefix(RAINSHOWER_CUT_STINGER_END_ID))
            .nextOfTimeout(entity -> TEBlade.prefix(RAINSHOWER_CUT_STINGER_END2_ID))
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    
    public static final DeferredHolder<ComboState, ComboState> RAINSHOWER_CUT_STINGER_END2 = COMBO_STATE.register(RAINSHOWER_CUT_STINGER_END2_ID,
        ComboState.Builder.newInstance().startAndEnd(85, 105).priority(100).motionLoc(JUDGEMENT_CUT_STINGER_MOTION_LOC)
            .next(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .nextOfTimeout(MrqxComboStateRegistry.NONE_COMBO_FUNCTION)
            .addTickAction(MrqxComboStateRegistry.SHEATH_SOUND_ACTION)
            .releaseAction(ComboState::releaseActionQuickCharge)::build);
    //</editor-fold>
    
    static {
        DreadStormComboStateRegistry.init();
    }
}
