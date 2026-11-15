package net.mrqx.slashblade.teblade.compat.thaumaturge.entity;

import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

public class EntityThaumaturgeSpellJudgementCut extends EntityJudgementCut {
    public EntityThaumaturgeSpellJudgementCut(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public List<MobEffectInstance> getPotionEffects() {
        return List.of();
    }
}
