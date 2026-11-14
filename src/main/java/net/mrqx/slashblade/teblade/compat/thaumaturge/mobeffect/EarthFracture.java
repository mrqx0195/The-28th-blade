package net.mrqx.slashblade.teblade.compat.thaumaturge.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.mrqx.slashblade.teblade.TEBlade;

public final class EarthFracture extends MobEffect {
    public EarthFracture() {
        super(MobEffectCategory.HARMFUL, 0x88704A);
        addAttributeModifier(Attributes.ARMOR, TEBlade.prefix("earth_fracture_modifier"), -4,
            AttributeModifier.Operation.ADD_VALUE);
    }
}
