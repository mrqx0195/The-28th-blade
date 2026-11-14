package net.mrqx.slashblade.teblade.compat.thaumaturge.blade;

import com.leclowndu93150.thaumaturge.api.items.InfusionEnchantment;
import com.leclowndu93150.thaumaturge.content.equipment.InfusionEnchantments;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms.Form;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class BladeTransition {
    private record Pair(ISlashBladeState sourceState, Form sourceForm,
                        ISlashBladeState templateState, Form targetForm) {
    }
    
    public static boolean canTransform(ItemStack source, ItemStack targetTemplate) {
        return resolve(source, targetTemplate) != null;
    }
    
    @Nullable
    public static ItemStack transformRecorded(ItemStack source, ItemStack targetTemplate) {
        Pair pair = resolve(source, targetTemplate);
        if (pair == null) {
            return null;
        }
        ItemStack result = targetTemplate.copyWithCount(1);
        ISlashBladeState resultState = BladeStateAccess.of(result).orElse(null);
        if (resultState == null) {
            TEBlade.LOGGER.error("The transition result {} carries no slash blade state, skipping the transformation",
                result.getItem());
            return null;
        }
        copyProgress(source, pair.sourceState(), result, resultState, pair.templateState());
        mergeProgress(source, result, pair.sourceForm(), pair.targetForm());
        applyNativeEffects(resultState, pair.targetForm());
        Set<ResourceLocation> effects = carriedEffects(source, pair.sourceState());
        effects.forEach(resultState::addSpecialEffect);
        ExternalBladeEffects.write(result, effects);
        return result;
    }
    
    public static void applyNativeEffects(ISlashBladeState state, Form form) {
        BladeNativeEffects.of(form).forEach(state::addSpecialEffect);
    }
    
    @Nullable
    private static Pair resolve(ItemStack source, ItemStack targetTemplate) {
        if (source.isEmpty() || targetTemplate.isEmpty()) {
            return null;
        }
        ISlashBladeState sourceState = BladeStateAccess.of(source).orElse(null);
        if (sourceState == null) {
            return null;
        }
        ISlashBladeState templateState = BladeStateAccess.of(targetTemplate).orElse(null);
        if (templateState == null) {
            return null;
        }
        Form sourceForm = ThaumaturgeBladeForms.byTranslationKey(sourceState.getTranslationKey());
        Form targetForm = ThaumaturgeBladeForms.byTranslationKey(templateState.getTranslationKey());
        if (sourceForm == null || targetForm == null
            || !ThaumaturgeBladeForms.canPreserveProgress(sourceForm, targetForm)) {
            return null;
        }
        return new Pair(sourceState, sourceForm, templateState, targetForm);
    }
    
    private static Set<ResourceLocation> carriedEffects(ItemStack source, ISlashBladeState sourceState) {
        Set<ResourceLocation> recorded = ExternalBladeEffects.read(source);
        Set<ResourceLocation> nativeEffects = BladeNativeEffects.all();
        return recorded.stream().filter(id -> !nativeEffects.contains(id) && sourceState.getSpecialEffects().contains(id)).collect(Collectors.toCollection(() -> new HashSet<>(Math.min(recorded.size(), 8))));
    }
    
    private static void copyProgress(ItemStack source, ISlashBladeState sourceState,
                                     ItemStack result, ISlashBladeState resultState,
                                     ISlashBladeState templateState) {
        int damage = transitionDamage(sourceState.getDamage(), sourceState.getMaxDamage(), templateState.getMaxDamage());
        resultState.setRefine(sourceState.getRefine());
        resultState.setKillCount(sourceState.getKillCount());
        resultState.setProudSoulCount(sourceState.getProudSoulCount());
        resultState.setDamage(damage);
        resultState.setSealed(sourceState.isSealed());
        resultState.setBroken(sourceState.isBroken());
        result.set(DataComponents.ENCHANTMENTS,
            source.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
        if (source.has(DataComponents.CUSTOM_NAME)) {
            result.set(DataComponents.CUSTOM_NAME, source.get(DataComponents.CUSTOM_NAME));
        } else {
            result.remove(DataComponents.CUSTOM_NAME);
        }
    }
    
    private static void mergeProgress(ItemStack source, ItemStack result, Form sourceForm, Form targetForm) {
        InfusionEnchantments currentInfusions = source.getOrDefault(
            TTDataComponents.INFUSION_ENCHANTMENTS.get(), InfusionEnchantments.EMPTY);
        InfusionEnchantments sourceNative = BladeNativeInfusions.forForm(sourceForm);
        InfusionEnchantments targetNative = BladeNativeInfusions.forForm(targetForm);
        Map<InfusionEnchantment, Integer> levels = new EnumMap<>(InfusionEnchantment.class);
        for (InfusionEnchantment enchantment : InfusionEnchantment.values()) {
            int level = currentInfusions.level(enchantment)
                - sourceNative.level(enchantment) + targetNative.level(enchantment);
            if (level > 0) {
                levels.put(enchantment, level);
            }
        }
        if (levels.isEmpty()) {
            result.remove(TTDataComponents.INFUSION_ENCHANTMENTS.get());
        } else {
            result.set(TTDataComponents.INFUSION_ENCHANTMENTS.get(), new InfusionEnchantments(Map.copyOf(levels)));
        }
        int warp = source.getOrDefault(TTDataComponents.STACK_WARP.get(), 0)
            - sourceForm.nativeWarp() + targetForm.nativeWarp();
        result.set(TTDataComponents.STACK_WARP.get(), warp);
    }
    
    private static int transitionDamage(int damage, int oldMaxDamage, int newMaxDamage) {
        if (oldMaxDamage <= 0 || newMaxDamage <= 0) {
            return 0;
        }
        long boundedDamage = Math.clamp(damage, 0, oldMaxDamage);
        return (int) Math.min(newMaxDamage, Math.round((double) boundedDamage * newMaxDamage / oldMaxDamage));
    }
}