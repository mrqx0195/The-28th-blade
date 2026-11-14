package net.mrqx.slashblade.teblade.compat.thaumaturge.event;

import com.leclowndu93150.thaumaturge.api.infusion.InfusionCraftedEvent;
import net.minecraft.world.item.ItemStack;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.BladeTransition;
import net.neoforged.bus.api.SubscribeEvent;

public final class BladeInfusionCraftedHandler {
    @SubscribeEvent
    public static void onCrafted(InfusionCraftedEvent event) {
        ItemStack transformed = BladeTransition.transformRecorded(event.getCatalyst(), event.getResult());
        if (transformed != null) {
            event.setResult(transformed);
        }
    }
}
