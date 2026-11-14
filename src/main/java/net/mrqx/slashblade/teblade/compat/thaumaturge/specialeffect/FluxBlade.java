package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import com.leclowndu93150.thaumaturge.api.damagesource.TTDamageSources;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.neoforged.bus.api.SubscribeEvent;

public final class FluxBlade extends SpecialEffect {
    public FluxBlade() {
        super(20, true, true);
    }
    
    @SubscribeEvent
    public static void onHit(SlashBladeEvent.HitEvent event) {
        AttackUtils.checkAndPerformSpecialEffect(event.getUser(), event.getSlashBladeState(), TEBladeThaumaturgeSpecialEffects.FLUX_BLADE,
            (livingEntity, state) -> AttackManager.doManagedAttack(target ->
                    target.hurt(TTDamageSources.swarm(target.level(), livingEntity), state.getBaseAttackModifier() * 0.1F),
                event.getTarget(), true, true)
        );
    }
}
