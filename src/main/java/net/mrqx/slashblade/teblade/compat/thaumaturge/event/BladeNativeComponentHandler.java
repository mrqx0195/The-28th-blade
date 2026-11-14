package net.mrqx.slashblade.teblade.compat.thaumaturge.event;

import com.leclowndu93150.thaumaturge.content.equipment.InfusionEnchantments;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.event.SlashBladeRegistryEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.BladeNativeInfusions;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.BladeTransition;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.neoforged.bus.api.SubscribeEvent;

public final class BladeNativeComponentHandler {
    @SubscribeEvent
    public static void onBladeCreated(SlashBladeRegistryEvent.Post event) {
        ResourceLocation id = event.getSlashBladeDefinition().getName();
        if (!TEBlade.MODID.equals(id.getNamespace())) {
            return;
        }
        ThaumaturgeBladeForms.Form form = ThaumaturgeBladeForms.byPath(id.getPath());
        if (form == null) {
            return;
        }
        ItemStack stack = event.getBlade();
        BladeStateAccess.of(stack).ifPresent(state -> BladeTransition.applyNativeEffects(state, form));
        if (!stack.has(TTDataComponents.STACK_WARP.get())) {
            stack.set(TTDataComponents.STACK_WARP.get(), form.nativeWarp());
        }
        InfusionEnchantments nativeInfusions = BladeNativeInfusions.forForm(form);
        if (!nativeInfusions.isEmpty() && !stack.has(TTDataComponents.INFUSION_ENCHANTMENTS.get())) {
            stack.set(TTDataComponents.INFUSION_ENCHANTMENTS.get(), nativeInfusions);
        }
    }
}
