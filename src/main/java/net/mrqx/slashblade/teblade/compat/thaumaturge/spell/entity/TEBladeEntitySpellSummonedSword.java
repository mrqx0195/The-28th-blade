package net.mrqx.slashblade.teblade.compat.thaumaturge.spell.entity;

import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import com.leclowndu93150.thaumaturge.content.spell.carrier.CarrierPayload;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;
import java.util.List;

public class TEBladeEntitySpellSummonedSword extends EntityAbstractSummonedSword {
    @Nullable
    private CarrierPayload payload;
    private boolean hitResumed;
    private boolean burstResumed;
    
    public TEBladeEntitySpellSummonedSword(EntityType<? extends Projectile> entityType, Level level) {
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
        if (target.equals(this) || target.equals(this.getShooter())) {
            return;
        }
        if (!this.hitResumed) {
            this.hitResumed = true;
            this.resume(TEBladeCarrierSupport.at(target, this.position(), this.getDeltaMovement()));
        }
        if (this.getPierce() <= 0) {
            this.setHitEntity(target);
        }
    }
    
    @Override
    public void burst() {
        if (!this.level().isClientSide() && !this.burstResumed) {
            this.burstResumed = true;
            this.resume(TEBladeCarrierSupport.at(this.getHitEntity(), this.position(), this.getDeltaMovement()));
        }
        super.burst();
    }
    
    @Override
    public List<MobEffectInstance> getPotionEffects() {
        return List.of();
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
        tag.putBoolean("teblade_hit_resumed", this.hitResumed);
        tag.putBoolean("teblade_burst_resumed", this.burstResumed);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.payload = TEBladeCarrierSupport.load(this, tag);
        this.hitResumed = tag.getBoolean("teblade_hit_resumed");
        this.burstResumed = tag.getBoolean("teblade_burst_resumed");
    }
}
