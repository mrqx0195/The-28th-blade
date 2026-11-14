package net.mrqx.slashblade.teblade.specialeffect;

import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.mrqx.sbr_core.utils.JustSlashArtManager;
import net.mrqx.slashblade.teblade.registry.TEBladeSpecialEffectsRegistry;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public final class HourOfJudgment extends SpecialEffect {
    public HourOfJudgment() {
        super(50, true, true);
    }
    
    @SubscribeEvent
    public static void onPerformSlashArt(SlashBladeEvent.PerformSlashArtEvent event) {
        AttackUtils.checkAndPerformSpecialEffect(event.getEntityLiving(), event.getSlashBladeState(), TEBladeSpecialEffectsRegistry.HOUR_OF_JUDGMENT,
            (livingEntity, state) -> JustSlashArtManager.resetJustCount(livingEntity));
    }
}
