package net.mrqx.slashblade.teblade.compat.thaumaturge.utils;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;

public final class SpecialEffectGates {
    public static boolean isActive(LivingEntity user, DeferredHolder<SpecialEffect, SpecialEffect> effect) {
        return isActive(user, effect, null);
    }
    
    public static boolean isActive(LivingEntity user, DeferredHolder<SpecialEffect, SpecialEffect> effect,
                                   @Nullable ThaumaturgeBladeForms.Family family) {
        if (!user.isAlive() || !effect.get().isEffective(user)) {
            return false;
        }
        ISlashBladeState state = BladeStateAccess.of(user.getMainHandItem()).orElse(null);
        if (state == null || !state.hasSpecialEffect(effect.getId())) {
            return false;
        }
        if (family == null) {
            return true;
        }
        ThaumaturgeBladeForms.Form form = ThaumaturgeBladeForms.byTranslationKey(state.getTranslationKey());
        return form != null && ThaumaturgeBladeForms.familyOf(form) == family;
    }
}