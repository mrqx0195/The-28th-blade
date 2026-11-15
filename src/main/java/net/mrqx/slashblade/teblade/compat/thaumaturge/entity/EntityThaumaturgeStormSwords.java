package net.mrqx.slashblade.teblade.compat.thaumaturge.entity;

import mods.flammpfeil.slashblade.entity.EntityStormSwords;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.TEBladeSpellFormations;
import net.neoforged.neoforge.event.EventHooks;
import org.joml.Vector3f;

public class EntityThaumaturgeStormSwords extends EntityStormSwords {
    private static final EntityDataAccessor<Boolean> ANCHORED = SynchedEntityData.defineId(EntityThaumaturgeStormSwords.class,
        EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Vector3f> ANCHOR = SynchedEntityData.defineId(EntityThaumaturgeStormSwords.class,
        EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> LAUNCHED = SynchedEntityData.defineId(EntityThaumaturgeStormSwords.class,
        EntityDataSerializers.BOOLEAN);
    private static final String ANCHORED_TAG = "teblade_anchored";
    private static final String ANCHOR_X_TAG = "teblade_anchor_x";
    private static final String ANCHOR_Y_TAG = "teblade_anchor_y";
    private static final String ANCHOR_Z_TAG = "teblade_anchor_z";
    private static final String LAUNCHED_TAG = "teblade_launched";
    
    
    public EntityThaumaturgeStormSwords(EntityType<? extends Projectile> type, Level level) {
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
        if (this.itFired()) {
            this.faceEntityStandby();
            this.stopRiding();
            
            this.tickCount = 0;
            Vec3 dir = this.getViewVector(1.0f);
            if (this.anchored()) {
                dir = this.anchor().subtract(this.position()).multiply(1, 0, 1).normalize();
            }
            this.shoot(dir.x, dir.y, dir.z, 3.0f, 1.0f);
            this.setLaunched();
            return;
        }
        
        this.setDeltaMovement(Vec3.ZERO);
        this.baseTick();
        
        this.faceEntityStandby();
        
        if (20 <= this.tickCount) {
            this.doFire();
        }
        
        if (!this.level().isClientSide()) {
            this.hitCheck();
        }
    }
    
    private void hitCheck() {
        Vec3 positionVec = this.position();
        Vec3 dirVec = this.getViewVector(1.0f);
        EntityHitResult raytraceresult = null;
        
        EntityHitResult entityraytraceresult = this.getRayTrace(positionVec, dirVec);
        if (entityraytraceresult != null) {
            raytraceresult = entityraytraceresult;
        }
        
        if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.ENTITY) {
            Entity entity = raytraceresult.getEntity();
            Entity entity1 = this.getShooter();
            if (entity instanceof Player && entity1 instanceof Player
                && !((Player) entity1).canHarmPlayer((Player) entity)) {
                raytraceresult = null;
            }
        }
        
        if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.ENTITY
            && !EventHooks.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
            this.resetAlreadyHits();
            this.hasImpulse = true;
        }
    }
    
    private void faceEntityStandby() {
        long cycle = 5 + this.tickCount;
        long tickOffset = 0;
        if (this.level().isClientSide()) {
            tickOffset = 1;
        }
        
        int ticks = (int) ((this.tickCount + tickOffset) % cycle);
        
        double rotParTick = 360.0 / (double) cycle;
        double offset = getDelay();
        double degYaw = (ticks * rotParTick + offset) % 360.0;
        double yaw = Math.toRadians(degYaw);
        
        Vec3 dir = new Vec3(0, 0, 1);
        
        dir = dir.yRot((float) -yaw);
        dir = dir.normalize().scale(4);
        
        if (this.anchored()) {
            dir = dir.add(this.anchor());
            dir = dir.add(0, TEBladeSpellFormations.VIRTUAL_EYE_HEIGHT / 2.0, 0);
        }
        
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
        
        setPos(dir);
        
        setRot((float) (-degYaw) - 180, 0);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        Vec3 anchor = this.anchor();
        tag.putBoolean(ANCHORED_TAG, this.anchored());
        tag.putDouble(ANCHOR_X_TAG, anchor.x);
        tag.putDouble(ANCHOR_Y_TAG, anchor.y);
        tag.putDouble(ANCHOR_Z_TAG, anchor.z);
        tag.putBoolean(LAUNCHED_TAG, this.launched());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.getBoolean(ANCHORED_TAG)) {
            this.anchorAt(new Vec3(tag.getDouble(ANCHOR_X_TAG), tag.getDouble(ANCHOR_Y_TAG), tag.getDouble(ANCHOR_Z_TAG)));
        }
        if (tag.getBoolean(LAUNCHED_TAG)) {
            this.setLaunched();
        }
    }
}
