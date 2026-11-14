package net.mrqx.slashblade.teblade.compat.thaumaturge.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class MovementSpeedBuff {
    public static void applyMovementSpeed(LivingEntity user, ResourceLocation modifierId, double amount) {
        AttributeInstance speed = user.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null && !speed.hasModifier(modifierId)) {
            speed.addTransientModifier(
                new AttributeModifier(modifierId, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }
    
    public static boolean hasMovementSpeed(LivingEntity user, ResourceLocation modifierId) {
        AttributeInstance speed = user.getAttribute(Attributes.MOVEMENT_SPEED);
        return speed != null && speed.hasModifier(modifierId);
    }
    
    public static void clearMovementSpeed(LivingEntity user, ResourceLocation modifierId) {
        AttributeInstance speed = user.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            speed.removeModifier(modifierId);
        }
    }
}
