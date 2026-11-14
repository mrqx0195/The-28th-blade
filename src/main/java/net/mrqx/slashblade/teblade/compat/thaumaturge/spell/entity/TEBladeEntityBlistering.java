package net.mrqx.slashblade.teblade.compat.thaumaturge.spell.entity;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.entity.EntityBlisteringSwords;
import mods.flammpfeil.slashblade.entity.IShootable;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.RayTraceHelper;
import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.TEBladeSpellFormations;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class TEBladeEntityBlistering extends EntityBlisteringSwords {
    private static final EntityDataAccessor<Boolean> ANCHORED = SynchedEntityData.defineId(TEBladeEntityBlistering.class,
        EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Vector3f> ANCHOR = SynchedEntityData.defineId(TEBladeEntityBlistering.class,
        EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> LAUNCHED = SynchedEntityData.defineId(TEBladeEntityBlistering.class,
        EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> ANCHOR_YAW = SynchedEntityData.defineId(TEBladeEntityBlistering.class,
        EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANCHOR_PITCH = SynchedEntityData.defineId(TEBladeEntityBlistering.class,
        EntityDataSerializers.FLOAT);
    private static final String ANCHORED_TAG = "teblade_anchored";
    private static final String ANCHOR_X_TAG = "teblade_anchor_x";
    private static final String ANCHOR_Y_TAG = "teblade_anchor_y";
    private static final String ANCHOR_Z_TAG = "teblade_anchor_z";
    private static final String ANCHOR_YAW_TAG = "teblade_anchor_yaw";
    private static final String ANCHOR_PITCH_TAG = "teblade_anchor_pitch";
    private static final String FIRE_IN_TAG = "teblade_fire_in";
    private static final String LAUNCHED_TAG = "teblade_launched";
    
    private int fireIn = -1;
    private long tebladeFireTime = -1L;
    
    public TEBladeEntityBlistering(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }
    
    public void anchorAt(Vec3 anchor, float yaw, float pitch) {
        this.entityData.set(ANCHOR, new Vector3f((float) anchor.x, (float) anchor.y, (float) anchor.z));
        this.entityData.set(ANCHOR_YAW, yaw);
        this.entityData.set(ANCHOR_PITCH, pitch);
        this.entityData.set(ANCHORED, true);
    }
    
    public boolean anchored() {
        return this.entityData.get(ANCHORED);
    }
    
    public boolean launched() {
        return this.entityData.get(LAUNCHED);
    }
    
    private void setLaunched() {
        this.entityData.set(LAUNCHED, true);
    }
    
    public Vec3 anchor() {
        Vector3f anchor = this.entityData.get(ANCHOR);
        return new Vec3(anchor.x, anchor.y, anchor.z);
    }
    
    public void launchAfter(int delayTicks) {
        this.fireIn = Math.max(1, delayTicks);
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        
        builder.define(ANCHORED, false);
        builder.define(ANCHOR, new Vector3f());
        builder.define(LAUNCHED, false);
        builder.define(ANCHOR_YAW, 0.0F);
        builder.define(ANCHOR_PITCH, 0.0F);
    }
    
    @Override
    public void tick() {
        if (!this.anchored() || this.launched()) {
            super.tick();
            return;
        }
        
        this.anchorRideTick();
    }
    
    @Override
    public void rideTick() {
        if (this.itFired() && this.tebladeFireTime < 0L) {
            this.tebladeFireTime = this.tickCount + this.getDelay();
        }
        if (!this.itFired() && this.fireIn > 0 && --this.fireIn <= 0) {
            this.tebladeFireTime = this.tickCount + this.getDelay();
            if (!this.level().isClientSide()) {
                this.doFire();
            }
        }
        super.rideTick();
    }
    
    private void anchorRideTick() {
        if (this.itFired() && this.tebladeFireTime <= this.tickCount) {
            this.faceEntityStandby();
            Vec3 dir = this.getViewVector(0);
            LivingEntity sender = this.getOwner() instanceof LivingEntity owner ? owner : null;
            if (sender == null) {
                this.shoot(dir.x, dir.y, dir.z, 3.0f, 1.0f);
                this.setLaunched();
                return;
            }
            
            this.stopRiding();
            
            this.tickCount = 0;
            
            Level worldIn = sender.level();
            Entity lockTarget = BladeStateAccess.of(sender.getMainHandItem())
                .filter(state -> state.getTargetEntity(worldIn) != null)
                .map(state -> state.getTargetEntity(worldIn)).orElse(null);
            
            Optional<Entity> foundTarget;
            foundTarget = Stream
                .of(Optional.ofNullable(lockTarget),
                    RayTraceHelper
                        .rayTrace(sender.level(), sender, sender.getEyePosition(1.0f),
                            sender.getLookAngle(), 12, 12, (e) -> true)
                        .filter(r -> r.getType() == HitResult.Type.ENTITY).filter(hitResult -> {
                            Entity target = ((EntityHitResult) hitResult).getEntity();
                            
                            boolean isMatch = true;
                            if (target instanceof LivingEntity) {
                                isMatch = TargetSelector.test.test(sender, (LivingEntity) target);
                            }
                            
                            if (target instanceof IShootable) {
                                isMatch = !Objects.equals(((IShootable) target).getShooter(), sender);
                            }
                            
                            return isMatch;
                        }).map(r -> ((EntityHitResult) r).getEntity()))
                .filter(Optional::isPresent).map(Optional::get).findFirst();
            
            Vec3 targetPos = foundTarget.map((e) -> new Vec3(e.getX(), e.getY() + e.getEyeHeight() * 0.5, e.getZ()))
                .orElseGet(() -> {
                    Vec3 start = sender.getEyePosition(1.0f);
                    Vec3 end = start.add(sender.getLookAngle().scale(40));
                    HitResult result;
                    result = worldIn.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE, sender));
                    return result.getLocation();
                });
            
            Vec3 pos = this.getPosition(0.0f);
            dir = targetPos.subtract(pos).normalize();
            
            this.shoot(dir.x, dir.y, dir.z, 3.0f, 1.0f);
            if (sender instanceof ServerPlayer serverPlayer) {
                serverPlayer.playNotifySound(SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            this.setLaunched();
            
            return;
        }
        
        this.setDeltaMovement(Vec3.ZERO);
        this.baseTick();
        
        this.faceEntityStandby();
        
        if (!this.itFired() && this.fireIn > 0 && --this.fireIn <= 0) {
            this.tebladeFireTime = this.tickCount + this.getDelay();
            this.doFire();
        }
    }
    
    private void faceEntityStandby() {
        int spawnNum = getDelay();
        
        boolean isRight = spawnNum % 2 == 0;
        int offsetLevel = spawnNum / 2;
        
        Vec3 pos = Vec3.ZERO;
        
        if (!this.anchored()) {
            this.doFire();
            return;
        }
        
        float anchorYaw = this.entityData.get(ANCHOR_YAW);
        float anchorPitch = this.entityData.get(ANCHOR_PITCH);
        
        pos = pos.add(this.anchor()).add(0, TEBladeSpellFormations.VIRTUAL_EYE_HEIGHT * 0.8, 0);
        
        double xOffset = (1 - 0.1 * offsetLevel) * (isRight ? 1 : -1);
        double yOffset = 0.25 * offsetLevel;
        double zOffset = -0.1 * offsetLevel;
        
        Vec3 offset = new Vec3(xOffset, yOffset, zOffset);
        
        offset = offset.xRot((float) Math.toRadians(-anchorPitch));
        offset = offset.yRot((float) Math.toRadians(-anchorYaw));
        
        pos = pos.add(offset);
        
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
        
        setPos(pos);
        
        setRot(-anchorYaw, -anchorPitch);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        Vec3 anchor = this.anchor();
        tag.putBoolean(ANCHORED_TAG, this.anchored());
        tag.putDouble(ANCHOR_X_TAG, anchor.x);
        tag.putDouble(ANCHOR_Y_TAG, anchor.y);
        tag.putDouble(ANCHOR_Z_TAG, anchor.z);
        tag.putFloat(ANCHOR_YAW_TAG, this.entityData.get(ANCHOR_YAW));
        tag.putFloat(ANCHOR_PITCH_TAG, this.entityData.get(ANCHOR_PITCH));
        tag.putInt(FIRE_IN_TAG, this.fireIn);
        tag.putBoolean(LAUNCHED_TAG, this.launched());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.getBoolean(ANCHORED_TAG)) {
            this.anchorAt(new Vec3(tag.getDouble(ANCHOR_X_TAG), tag.getDouble(ANCHOR_Y_TAG), tag.getDouble(ANCHOR_Z_TAG)),
                tag.getFloat(ANCHOR_YAW_TAG), tag.getFloat(ANCHOR_PITCH_TAG));
        }
        this.fireIn = tag.getInt(FIRE_IN_TAG);
        if (tag.getBoolean(LAUNCHED_TAG)) {
            this.setLaunched();
        }
    }
}
