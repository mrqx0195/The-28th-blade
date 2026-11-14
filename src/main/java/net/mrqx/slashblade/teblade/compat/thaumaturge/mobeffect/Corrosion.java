package net.mrqx.slashblade.teblade.compat.thaumaturge.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.mrqx.slashblade.teblade.TEBlade;

public final class Corrosion extends MobEffect {
    public Corrosion() {
        super(MobEffectCategory.HARMFUL, 0x80548F);
        addAttributeModifier(Attributes.ARMOR, TEBlade.prefix("corrosion_modifier"), -2,
            AttributeModifier.Operation.ADD_VALUE);
    }
}
