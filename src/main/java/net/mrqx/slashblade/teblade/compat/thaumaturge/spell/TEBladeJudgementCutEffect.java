package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import com.leclowndu93150.thaumaturge.api.spell.behavior.AbstractEffectBehavior;
import com.leclowndu93150.thaumaturge.api.spell.behavior.SpellBehaviorType;
import com.leclowndu93150.thaumaturge.api.spell.cast.CastContext;
import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.entity.EntityThaumaturgeSpellJudgementCut;

public class TEBladeJudgementCutEffect extends AbstractEffectBehavior {
    public static final MapCodec<TEBladeJudgementCutEffect> CODEC = MapCodec.unit(TEBladeJudgementCutEffect::new);
    public static final double DAMAGE_PER_HIT = 1.5;
    
    public TEBladeJudgementCutEffect() {
    }
    
    @Override
    public SpellBehaviorType<?> type() {
        return TEBladeThaumaturgeSpellBehaviorTypes.JUDGEMENT_CUT.get();
    }
    
    @Override
    protected boolean widens() {
        return false;
    }
    
    @Override
    protected void apply(CastContext ctx, SpellTarget target, float power, int index) {
        ServerLevel level = ctx.level();
        int hits = Mth.clamp(ctx.setting("hits"), 3, 8);
        EntityThaumaturgeSpellJudgementCut cut = new EntityThaumaturgeSpellJudgementCut(
            TEBladeThaumaturgeEntityTypes.SPELL_JUDGEMENT_CUT.get(), level);
        cut.setPos(target.position());
        cut.setColor(TEBladeSpellTint.SLASH_BLADE_DEFAULT);
        cut.setIsCritical(false);
        LivingEntity caster = ctx.caster();
        double desired = DAMAGE_PER_HIT * power;
        cut.setDamage(TEBladeSpellSwordDamage.storedAreaDamage(caster, desired));
        cut.setLifetime(hits * 2);
        if (caster != null) {
            cut.setOwner(caster);
            cut.setShooter(caster);
        }
        level.addFreshEntity(cut);
    }
}
