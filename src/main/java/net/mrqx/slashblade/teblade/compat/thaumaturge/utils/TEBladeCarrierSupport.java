package net.mrqx.slashblade.teblade.compat.thaumaturge.utils;

import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import com.leclowndu93150.thaumaturge.content.spell.carrier.CarrierPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public final class TEBladeCarrierSupport {
    public static void save(Entity holder, CompoundTag tag, @Nullable CarrierPayload payload) {
        if (payload != null) {
            CarrierPayload.save(tag, payload, holder.level().registryAccess());
        }
    }
    
    @Nullable
    public static CarrierPayload load(Entity holder, CompoundTag tag) {
        return CarrierPayload.load(tag, holder.level().registryAccess());
    }
    
    public static SpellTarget at(@Nullable Entity hit, Vec3 position, Vec3 direction) {
        Vec3 heading = direction.lengthSqr() < 1.0E-6D ? new Vec3(0.0D, 0.0D, 1.0D) : direction.normalize();
        if (hit != null && hit.isAlive() && !hit.isRemoved()) {
            return SpellTarget.entity(hit, heading);
        }
        return SpellTarget.of(BlockHitResult.miss(position, Direction.UP, BlockPos.containing(position)), heading);
    }
}
