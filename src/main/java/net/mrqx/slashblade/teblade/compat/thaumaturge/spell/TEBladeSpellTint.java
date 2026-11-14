package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import com.leclowndu93150.thaumaturge.api.spell.cast.CastContext;
import com.leclowndu93150.thaumaturge.content.spell.delivery.SpellLook;

public final class TEBladeSpellTint {
    public static final int SLASH_BLADE_DEFAULT = 0x3333FF;
    
    public static int of(CastContext ctx) {
        return SpellLook.downstream(ctx).color() & 0xFFFFFF;
    }
}
