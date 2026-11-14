package net.mrqx.slashblade.teblade.compat.thaumaturge.event;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.neoforged.bus.api.SubscribeEvent;

public final class BladeSourceRepairHandler {
    private static final int REPAIR_INTERVAL_TICKS = 20;
    
    @SubscribeEvent
    public static void onUpdate(SlashBladeEvent.UpdateEvent event) {
        if (event.getLevel().isClientSide()
            || !(event.getEntity() instanceof LivingEntity living)
            || living.tickCount % REPAIR_INTERVAL_TICKS != 0) {
            return;
        }
        ISlashBladeState state = event.getSlashBladeState();
        if (state.getDamage() <= 0) {
            return;
        }
        ThaumaturgeBladeForms.Form form = ThaumaturgeBladeForms.byTranslationKey(state.getTranslationKey());
        if (form == null) {
            return;
        }
        ThaumaturgeBladeForms.Family family = ThaumaturgeBladeForms.familyOf(form);
        if (family != ThaumaturgeBladeForms.Family.VOID && family != ThaumaturgeBladeForms.Family.BLOOD) {
            return;
        }
        ItemStack blade = event.getBlade();
        blade.setDamageValue(blade.getDamageValue() - 1);
    }
}
