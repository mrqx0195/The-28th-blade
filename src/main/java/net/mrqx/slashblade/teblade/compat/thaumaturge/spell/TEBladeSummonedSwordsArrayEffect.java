package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import com.leclowndu93150.thaumaturge.api.spell.behavior.AbstractEffectBehavior;
import com.leclowndu93150.thaumaturge.api.spell.behavior.SpellBehaviorType;
import com.leclowndu93150.thaumaturge.api.spell.cast.CastContext;
import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;

public class TEBladeSummonedSwordsArrayEffect extends AbstractEffectBehavior {
    public static final MapCodec<TEBladeSummonedSwordsArrayEffect> CODEC = MapCodec.unit(TEBladeSummonedSwordsArrayEffect::new);
    
    public static final double DAMAGE_PER_SWORD = 3.0D;
    
    public TEBladeSummonedSwordsArrayEffect() {
    }
    
    @Override
    public SpellBehaviorType<?> type() {
        return TEBladeThaumaturgeSpellBehaviorTypes.PHANTOM_SWORDS_ARRAY.get();
    }
    
    @Override
    protected boolean widens() {
        return false;
    }
    
    @Override
    protected void apply(CastContext ctx, SpellTarget target, float power, int index) {
        ServerLevel level = ctx.level();
        int formation = Mth.clamp(ctx.setting("formation"), 0, 3);
        int count = Mth.clamp(ctx.setting("count"), 4, 8);
        float perSword = Mth.ceil(DAMAGE_PER_SWORD * power);
        TEBladeSpellFormations.spawn(level, ctx, TEBladeSpellTint.SLASH_BLADE_DEFAULT, target, formation, count, perSword);
    }
}
