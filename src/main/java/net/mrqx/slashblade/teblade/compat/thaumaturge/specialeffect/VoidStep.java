package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeAttachmentTypes;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SpecialEffectGates;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public final class VoidStep extends SpecialEffect {
    private static final int PROTECTION_TICKS = 5;
    private static final int COOLDOWN_TICKS = 160;
    
    public VoidStep() {
        super(30, false, false);
    }
    
    public record Window(int protectedUntil, int readyAt) {
    }
    
    public static void onConfirmedHit(LivingEntity user) {
        if (user.level().isClientSide()
            || !SpecialEffectGates.isActive(user, TEBladeThaumaturgeSpecialEffects.VOID_STEP,
            ThaumaturgeBladeForms.Family.VOID)) {
            return;
        }
        int now = user.tickCount;
        if (now >= user.getData(TEBladeThaumaturgeAttachmentTypes.VOID_STEP_WINDOW).readyAt()) {
            user.setData(TEBladeThaumaturgeAttachmentTypes.VOID_STEP_WINDOW,
                new Window(now + PROTECTION_TICKS, now + COOLDOWN_TICKS));
        }
    }
    
    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity user = event.getEntity();
        if (user.level().isClientSide() || !user.isAlive()
            || user.tickCount >= user.getData(TEBladeThaumaturgeAttachmentTypes.VOID_STEP_WINDOW).protectedUntil()
            || event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return;
        }
        event.setCanceled(true);
    }
}
