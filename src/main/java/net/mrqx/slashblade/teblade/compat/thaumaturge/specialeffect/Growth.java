package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.neoforged.bus.api.SubscribeEvent;

public final class Growth extends SpecialEffect {
    private static final int REPAIR_INTERVAL_TICKS = 160;
    private static final int REPAIR_AMOUNT = 1;
    
    public Growth() {
        super(15, true, true);
    }
    
    @SubscribeEvent
    public static void onTick(SlashBladeEvent.UpdateEvent event) {
        ISlashBladeState state = event.getSlashBladeState();
        if (event.getEntity().tickCount % REPAIR_INTERVAL_TICKS != 0 || state.isBroken()
            || state.getDamage() <= 0) {
            return;
        }
        AttackUtils.checkAndPerformSpecialEffect(event.getEntity(), state, TEBladeThaumaturgeSpecialEffects.GROWTH,
            (living, blade) -> blade.setDamage(blade.getDamage() - REPAIR_AMOUNT));
    }
}
