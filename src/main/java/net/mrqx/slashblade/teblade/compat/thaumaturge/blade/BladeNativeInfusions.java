package net.mrqx.slashblade.teblade.compat.thaumaturge.blade;

import com.leclowndu93150.thaumaturge.api.items.InfusionEnchantment;
import com.leclowndu93150.thaumaturge.content.equipment.InfusionEnchantments;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms.Element;

import java.util.Map;

public final class BladeNativeInfusions {
    private static final InfusionEnchantments THUNDER = new InfusionEnchantments(Map.of(InfusionEnchantment.ARCING, 2));
    private static final InfusionEnchantments SKY = new InfusionEnchantments(Map.of(InfusionEnchantment.ESSENCE, 2));
    private static final InfusionEnchantments TIDE = new InfusionEnchantments(Map.of(InfusionEnchantment.COLLECTOR, 1));
    
    public static InfusionEnchantments forForm(ThaumaturgeBladeForms.Form form) {
        Element element = ThaumaturgeBladeForms.elementOf(form);
        if (element == Element.THUNDER) {
            return THUNDER;
        }
        if (element == Element.SKY) {
            return SKY;
        }
        if (element == Element.TIDE) {
            return TIDE;
        }
        return InfusionEnchantments.EMPTY;
    }
}