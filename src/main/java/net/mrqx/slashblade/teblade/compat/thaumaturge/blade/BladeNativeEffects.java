package net.mrqx.slashblade.teblade.compat.thaumaturge.blade;

import net.minecraft.resources.ResourceLocation;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BladeNativeEffects {
    private static final ResourceLocation EFFECT_BLOODTHIRST = TEBladeThaumaturgeSpecialEffects.BLOODTHIRST.getId();
    private static final ResourceLocation EFFECT_MYSTERY_RITE = TEBladeThaumaturgeSpecialEffects.MYSTERY_RITE.getId();
    private static final ResourceLocation EFFECT_TAINT_MARK = TEBladeThaumaturgeSpecialEffects.TAINT_MARK.getId();
    private static final ResourceLocation EFFECT_VOID_STEP = TEBladeThaumaturgeSpecialEffects.VOID_STEP.getId();
    private static final ResourceLocation EFFECT_GROWTH = TEBladeThaumaturgeSpecialEffects.GROWTH.getId();
    private static final ResourceLocation EFFECT_FLAME = TEBladeThaumaturgeSpecialEffects.FLAME.getId();
    private static final ResourceLocation EFFECT_EARTH = TEBladeThaumaturgeSpecialEffects.EARTH.getId();
    private static final ResourceLocation EFFECT_THUNDER = TEBladeThaumaturgeSpecialEffects.THUNDER.getId();
    private static final ResourceLocation EFFECT_SKY = TEBladeThaumaturgeSpecialEffects.SKY.getId();
    private static final ResourceLocation EFFECT_TIDE = TEBladeThaumaturgeSpecialEffects.TIDE.getId();
    private static final ResourceLocation EFFECT_VIS_REPAIR = TEBladeThaumaturgeSpecialEffects.VIS_REPAIR.getId();
    private static final ResourceLocation EFFECT_FLUX_BLADE = TEBladeThaumaturgeSpecialEffects.FLUX_BLADE.getId();
    
    private static final List<ResourceLocation> FAMILY_EFFECTS = List.of(EFFECT_BLOODTHIRST,
        EFFECT_MYSTERY_RITE, EFFECT_TAINT_MARK, EFFECT_VOID_STEP);
    private static final List<ResourceLocation> ELEMENT_EFFECTS = List.of(EFFECT_GROWTH, EFFECT_FLAME,
        EFFECT_EARTH, EFFECT_THUNDER, EFFECT_SKY, EFFECT_TIDE);
    private static final List<ResourceLocation> TERMINAL_EFFECTS = List.of(EFFECT_VIS_REPAIR,
        EFFECT_FLUX_BLADE, EFFECT_MYSTERY_RITE, EFFECT_VOID_STEP);
    private static final Set<ResourceLocation> ALL = Stream.of(FAMILY_EFFECTS, ELEMENT_EFFECTS, TERMINAL_EFFECTS)
        .flatMap(List::stream).collect(Collectors.toUnmodifiableSet());
    
    public static List<ResourceLocation> of(ThaumaturgeBladeForms.Form form) {
        if (form.equals(ThaumaturgeBladeForms.PRIMORDIAL_CURSE)) {
            return TERMINAL_EFFECTS;
        }
        ThaumaturgeBladeForms.Family family = ThaumaturgeBladeForms.familyOf(form);
        ThaumaturgeBladeForms.Element element = ThaumaturgeBladeForms.elementOf(form);
        if (family == null && element == null) {
            return List.of();
        }
        if (element == null) {
            return List.of(familyEffect(family));
        }
        if (family == null) {
            return List.of(elementEffect(element));
        }
        return List.of(familyEffect(family), elementEffect(element));
    }
    
    public static Set<ResourceLocation> all() {
        return ALL;
    }
    
    private static ResourceLocation familyEffect(ThaumaturgeBladeForms.Family family) {
        return switch (family) {
            case BLOOD -> EFFECT_BLOODTHIRST;
            case MYSTERY -> EFFECT_MYSTERY_RITE;
            case TAINT -> EFFECT_TAINT_MARK;
            case VOID -> EFFECT_VOID_STEP;
        };
    }
    
    private static ResourceLocation elementEffect(ThaumaturgeBladeForms.Element element) {
        return switch (element) {
            case GROWTH -> EFFECT_GROWTH;
            case FLAME -> EFFECT_FLAME;
            case EARTH -> EFFECT_EARTH;
            case THUNDER -> EFFECT_THUNDER;
            case SKY -> EFFECT_SKY;
            case TIDE -> EFFECT_TIDE;
        };
    }
}