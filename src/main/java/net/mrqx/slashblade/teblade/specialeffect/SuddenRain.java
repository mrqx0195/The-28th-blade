package net.mrqx.slashblade.teblade.specialeffect;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.world.entity.Entity;

public final class SuddenRain extends SpecialEffect {
    public SuddenRain() {
        super(0, false, false);
    }
    
    @Override
    public boolean isEffective(Entity user) {
        return true;
    }
}
