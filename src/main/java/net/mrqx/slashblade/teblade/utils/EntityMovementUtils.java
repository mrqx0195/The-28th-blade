package net.mrqx.slashblade.teblade.utils;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public final class EntityMovementUtils {
    public static void moveAndSync(LivingEntity user, Vec3 displacement) {
        if (user.level().isClientSide()) {
            return;
        }
        Vec3 start = user.position();
        user.move(MoverType.SELF, displacement);
        Vec3 motion = user.position().subtract(start);
        user.setDeltaMovement(motion);
        user.hurtMarked = true;
        if (user instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(user.getId(), motion));
        }
    }
}