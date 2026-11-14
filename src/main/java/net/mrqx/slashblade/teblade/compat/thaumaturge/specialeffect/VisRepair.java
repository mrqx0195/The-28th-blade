package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import com.leclowndu93150.thaumaturge.content.casters.CasterManager;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.world.entity.player.Player;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.neoforged.bus.api.SubscribeEvent;

public final class VisRepair extends SpecialEffect {
    public VisRepair() {
        super(20, true, true);
    }
    
    @SubscribeEvent
    public static void onTick(SlashBladeEvent.UpdateEvent event) {
        ISlashBladeState state = event.getSlashBladeState();
        if (state.getDamage() > 0 && event.getEntity() instanceof Player player) {
            AttackUtils.checkAndPerformSpecialEffect(event.getEntity(), state, TEBladeThaumaturgeSpecialEffects.VIS_REPAIR, (livingEntity, state1) -> {
                float totalVisDiscount = CasterManager.getTotalVisDiscount(player);
                if (CasterManager.consumeVisFromInventory(player, 1 - totalVisDiscount)) {
                    state1.setDamage(state1.getDamage() - 1);
                }
            });
        }
    }
}
