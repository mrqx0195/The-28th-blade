package net.mrqx.slashblade.teblade.compat.thaumaturge.entity;

import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import com.leclowndu93150.thaumaturge.content.spell.carrier.CarrierPayload;
import com.mojang.serialization.Codec;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.TEBladeCarrierSupport;

import javax.annotation.Nullable;
import java.util.*;

public class EntityThaumaturgeSpellDrive extends EntityDrive {
    private static final Codec<Set<UUID>> RESUMED_TARGETS_CODEC =
        UUIDUtil.CODEC.listOf().xmap(HashSet::new, List::copyOf);
    private final Set<UUID> resumedTargets = new HashSet<>();
    @Nullable
    private CarrierPayload payload;
    private boolean blockResumed;
    
    public EntityThaumaturgeSpellDrive(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }
    
    public void setPayload(@Nullable CarrierPayload payload) {
        this.payload = payload;
    }
    
    @Nullable
    public CarrierPayload getPayload() {
        return this.payload;
    }
    
    @Override
    protected void onHitEntity(EntityHitResult hit) {
        if (this.level().isClientSide()) {
            return;
        }
        Entity target = hit.getEntity();
        if (target.equals(this) || target.equals(this.getOwner())) {
            return;
        }
        if (!this.resumedTargets.add(target.getUUID())) {
            return;
        }
        this.resume(TEBladeCarrierSupport.at(target, this.position(), this.getDeltaMovement()));
    }
    
    @Override
    protected void onHitBlock(BlockHitResult hit) {
        if (!this.level().isClientSide() && !this.blockResumed) {
            this.blockResumed = true;
            this.resume(TEBladeCarrierSupport.at(null, this.position(), this.getDeltaMovement()));
        }
        super.onHitBlock(hit);
    }
    
    private void resume(SpellTarget target) {
        if (this.payload == null || !(this.level() instanceof ServerLevel level)) {
            return;
        }
        this.payload.resume(level, List.of(target));
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        TEBladeCarrierSupport.save(this, tag, this.payload);
        tag.putBoolean("teblade_block_resumed", this.blockResumed);
        RESUMED_TARGETS_CODEC.encodeStart(NbtOps.INSTANCE, this.resumedTargets)
            .result()
            .ifPresent(encoded -> tag.put("teblade_resumed_targets", encoded));
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.payload = TEBladeCarrierSupport.load(this, tag);
        this.blockResumed = tag.getBoolean("teblade_block_resumed");
        this.resumedTargets.clear();
        Optional.ofNullable(tag.get("teblade_resumed_targets"))
            .flatMap(encoded -> RESUMED_TARGETS_CODEC.parse(NbtOps.INSTANCE, encoded).result())
            .ifPresent(this.resumedTargets::addAll);
    }
}
