package net.mrqx.slashblade.teblade.compat.thaumaturge.mobeffect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.mrqx.slashblade.teblade.TEBlade;

public final class TideSlow extends MobEffect {
    private static final double TIDE_FACTOR = 0.2;
    private static final double VANILLA_SLOW_FACTOR = 0.15;
    private static final double AMOUNT_EPSILON = 1.0E-10;
    private static final int RECHECK_INTERVAL_TICKS = 20;
    private static final ResourceLocation SPEED_MODIFIER = TEBlade.prefix("tide_slow_movement");
    
    public TideSlow() {
        super(MobEffectCategory.HARMFUL, 0x398FB5);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER, -TIDE_FACTOR,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
    
    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);
        applyEffectTick(entity, amplifier);
    }
    
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % RECHECK_INTERVAL_TICKS == 0;
    }
    
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        AttributeInstance attribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute == null) {
            return true;
        }
        MobEffectInstance vanilla = entity.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
        double vanillaFactor = vanilla == null
            ? 1.0 : Math.max(0.0, 1.0 - VANILLA_SLOW_FACTOR * (vanilla.getAmplifier() + 1.0));
        double tideFactor = Math.max(0.0, 1.0 - TIDE_FACTOR * (amplifier + 1.0));
        double amount = vanillaFactor <= tideFactor ? 0.0 : tideFactor / vanillaFactor - 1.0;
        AttributeModifier current = attribute.getModifier(SPEED_MODIFIER);
        if (current == null || Math.abs(current.amount() - amount) > AMOUNT_EPSILON) {
            attribute.removeModifier(SPEED_MODIFIER);
            attribute.addTransientModifier(new AttributeModifier(SPEED_MODIFIER, amount,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        return true;
    }
}
