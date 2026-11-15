package net.mrqx.slashblade.teblade.compat.thaumaturge.entity;

import mods.flammpfeil.slashblade.entity.EntityHeavyRainSwords;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class EntityThaumaturgeHeavyRainSwords extends EntityHeavyRainSwords {
    private static final EntityDataAccessor<Boolean> ANCHORED = SynchedEntityData.defineId(EntityThaumaturgeHeavyRainSwords.class,
        EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Vector3f> ANCHOR = SynchedEntityData.defineId(EntityThaumaturgeHeavyRainSwords.class,
        EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> LAUNCHED = SynchedEntityData.defineId(EntityThaumaturgeHeavyRainSwords.class,
        EntityDataSerializers.BOOLEAN);
    private static final String ANCHORED_TAG = "teblade_anchored";
    private static final String ANCHOR_X_TAG = "teblade_anchor_x";
    private static final String ANCHOR_Y_TAG = "teblade_anchor_y";
    private static final String ANCHOR_Z_TAG = "teblade_anchor_z";
    private static final String FIRE_TIME_TAG = "teblade_fire_time";
    private static final String LAUNCHED_TAG = "teblade_launched";
    
    private long tebladeFireTime = -1L;
    
    public EntityThaumaturgeHeavyRainSwords(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }
    
    public void anchorAt(Vec3 anchor) {
        this.entityData.set(ANCHOR, new Vector3f((float) anchor.x, (float) anchor.y, (float) anchor.z));
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
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        
        builder.define(ANCHORED, false);
        builder.define(ANCHOR, new Vector3f());
        builder.define(LAUNCHED, false);
    }
    
    @Override
    public void tick() {
        if (!this.anchored() || this.launched()) {
            super.tick();
            return;
        }
        
        this.anchorRideTick();
    }
    
    private void anchorRideTick() {
        if (this.itFired() && this.tebladeFireTime <= this.tickCount) {
            this.faceEntityStandby();
            
            this.stopRiding();
            
            Vec3 dir = new Vec3(0, -1, 0);
            this.shoot(dir.x, dir.y, dir.z, 4.0f, 2.0f);
            
            this.tickCount = 0;
            this.setLaunched();
            
            return;
        }
        
        this.setDeltaMovement(Vec3.ZERO);
        this.baseTick();
        
        this.faceEntityStandby();
        
        if (!this.itFired()) {
            int basedelay = 10;
            this.tebladeFireTime = this.tickCount + basedelay + getDelay();
            this.doFire();
        }
    }
    
    private void faceEntityStandby() {
        setPos(this.position());
        
        setRot(this.getYRot(), -90);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        Vec3 anchor = this.anchor();
        tag.putBoolean(ANCHORED_TAG, this.anchored());
        tag.putDouble(ANCHOR_X_TAG, anchor.x);
        tag.putDouble(ANCHOR_Y_TAG, anchor.y);
        tag.putDouble(ANCHOR_Z_TAG, anchor.z);
        tag.putLong(FIRE_TIME_TAG, this.tebladeFireTime);
        tag.putBoolean(LAUNCHED_TAG, this.launched());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.getBoolean(ANCHORED_TAG)) {
            this.anchorAt(new Vec3(tag.getDouble(ANCHOR_X_TAG), tag.getDouble(ANCHOR_Y_TAG), tag.getDouble(ANCHOR_Z_TAG)));
        }
        this.tebladeFireTime = tag.getLong(FIRE_TIME_TAG);
        if (tag.getBoolean(LAUNCHED_TAG)) {
            this.setLaunched();
        }
    }
}
