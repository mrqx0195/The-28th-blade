package net.mrqx.slashblade.teblade.compat.thaumaturge.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.mrqx.slashblade.teblade.TEBlade;

public final class EarthRoot extends MobEffect {
    public EarthRoot() {
        super(MobEffectCategory.HARMFUL, 0x786044);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, TEBlade.prefix("earth_root_modifier"), -1,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
