package net.mrqx.slashblade.teblade.specialeffect;

import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.world.damagesource.DamageTypes;
import net.mrqx.slashblade.teblade.registry.TEBladeSpecialEffectsRegistry;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public final class FoxWishSnowWish extends SpecialEffect {
    public FoxWishSnowWish() {
        super(50, false, false);
    }
    
    @SubscribeEvent
    public static void onHit(SlashBladeEvent.HitEvent event) {
        AttackUtils.checkAndPerformSpecialEffect(event.getUser(), event.getSlashBladeState(), TEBladeSpecialEffectsRegistry.FOX_WISH_SNOW_WISH,
            (livingEntity, state) -> AttackManager.doManagedAttack(target ->
                    target.hurt(target.level().damageSources().freeze(), state.getBaseAttackModifier() * 0.2F),
                event.getTarget(), true, true)
        );
    }
    
    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getSource().is(DamageTypes.FREEZE)) {
            AttackUtils.checkAndPerformSpecialEffect(event.getEntity(), TEBladeSpecialEffectsRegistry.FOX_WISH_SNOW_WISH,
                (livingEntity, state) -> {
                    event.setCanceled(true);
                    livingEntity.setHealth(livingEntity.getHealth() + 2);
                }
            );
        }
    }
}
