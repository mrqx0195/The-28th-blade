package net.mrqx.slashblade.teblade.registry.combo.dreadstorm;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.truepower.event.PreInputEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public final class DreadStormRapidSlashHandler {
    @SubscribeEvent
    public static void onNextComboEvent(SlashBladeEvent.NextComboEvent event) {
        LivingEntity user = event.getUser();
        if (!user.level().isClientSide) {
            long elapsed = ComboState.getElapsed(user);
            if (event.getNextCombo().equals(DreadStormComboStateRegistry.RAPID_SLASH_QUICK.getId()) && elapsed < 4 || elapsed > 6) {
                ResourceLocation rapidSlashId = DreadStormComboStateRegistry.RAPID_SLASH.getId();
                if (event.getSlashBladeState().resolvCurrentComboState(user).equals(rapidSlashId)) {
                    event.setNextCombo(rapidSlashId);
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onPreInputEvent(PreInputEvent event) {
        ISlashBladeState state = event.getSlashBladeState();
        LivingEntity entity = event.getEntity();
        if (AttackManager.isPowered(entity) && state.resolvCurrentComboState(entity).equals(DreadStormComboStateRegistry.RAPID_SLASH.getId())) {
            long elapsed = ComboState.getElapsed(entity);
            if (elapsed >= 4 && elapsed <= 6) {
                if (event.getInputStream().checkInputWithTimeLineKeyInput(DreadStormInputTimeLines.PRE_CLICK_INPUT_KEY)) {
                    state.updateComboSeq(entity, DreadStormComboStateRegistry.RAPID_SLASH_QUICK.getId());
                }
            }
        }
    }
}
