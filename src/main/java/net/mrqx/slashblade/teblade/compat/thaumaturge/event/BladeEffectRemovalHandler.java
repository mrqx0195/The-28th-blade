package net.mrqx.slashblade.teblade.compat.thaumaturge.event;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.bladestand.CopySpecialEffectFromBladeEvent;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ExternalBladeEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

public final class BladeEffectRemovalHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAfterCopy(CopySpecialEffectFromBladeEvent event) {
        if (!event.isRemovable()) {
            return;
        }
        ISlashBladeState state = event.getSlashBladeState();
        if (state.hasSpecialEffect(event.getSEKey())) {
            return;
        }
        if (ThaumaturgeBladeForms.byTranslationKey(state.getTranslationKey()) == null) {
            return;
        }
        ExternalBladeEffects.edit(event.getBlade(), effects -> effects.remove(event.getSEKey()));
    }
}
