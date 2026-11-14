package net.mrqx.slashblade.teblade.compat.thaumaturge.spell.entity;

import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

public class TEBladeEntitySpellJudgementCut extends EntityJudgementCut {
    public TEBladeEntitySpellJudgementCut(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public List<MobEffectInstance> getPotionEffects() {
        return List.of();
    }
}
