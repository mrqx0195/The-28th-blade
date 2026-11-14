package net.mrqx.slashblade.teblade.compat.thaumaturge.blade;

import net.minecraft.resources.ResourceLocation;
import net.mrqx.slashblade.teblade.TEBlade;

import javax.annotation.Nullable;
import java.util.*;

public final class ThaumaturgeBladeForms {
    public static final Form GREATWOOD_DUMMY = new Form("greatwood_dummy", 4.0F, 59, 0);
    public static final Form SILVERWOOD_SAYA = new Form("silverwood_saya", 5.0F, 131, 0);
    public static final Form PRIMORDIAL_CURSE = new Form("primordial_curse", 9.0F, 300, 0);
    
    private static final String TRANSLATION_KEY_PREFIX = "item." + TEBlade.MODID + ".";
    
    private static final Map<Family, Form> BASES;
    private static final Map<Family, Map<Element, Form>> VARIANTS;
    private static final Map<Form, Family> FAMILIES;
    private static final Map<Form, Element> ELEMENTS;
    private static final Map<String, Form> BY_TRANSLATION_KEY;
    private static final Map<String, Form> BY_PATH;
    
    public static final List<Form> ALL;
    
    static {
        Map<Family, Form> bases = new EnumMap<>(Family.class);
        Map<Family, Map<Element, Form>> variants = new EnumMap<>(Family.class);
        Map<Form, Family> families = new HashMap<>();
        Map<Form, Element> elements = new HashMap<>();
        Map<String, Form> byPath = new HashMap<>();
        Map<String, Form> byTranslationKey = new HashMap<>();
        List<Form> forms = new ArrayList<>();
        forms.add(GREATWOOD_DUMMY);
        forms.add(SILVERWOOD_SAYA);
        byPath.put(GREATWOOD_DUMMY.path(), GREATWOOD_DUMMY);
        byPath.put(SILVERWOOD_SAYA.path(), SILVERWOOD_SAYA);
        for (Family family : Family.values()) {
            Form base = new Form(family.path, family.attack, family.durability, family.warp);
            bases.put(family, base);
            families.put(base, family);
            forms.add(base);
            byPath.put(base.path(), base);
            Map<Element, Form> familyVariants = new EnumMap<>(Element.class);
            for (Element element : Element.values()) {
                Form variant = new Form(family.path + "_" + element.path,
                    family.attack + element.attackBonus, family.durability, family.warp);
                familyVariants.put(element, variant);
                families.put(variant, family);
                elements.put(variant, element);
                forms.add(variant);
                byPath.put(variant.path(), variant);
            }
            variants.put(family, Map.copyOf(familyVariants));
        }
        forms.add(PRIMORDIAL_CURSE);
        byPath.put(PRIMORDIAL_CURSE.path(), PRIMORDIAL_CURSE);
        BASES = Map.copyOf(bases);
        VARIANTS = Map.copyOf(variants);
        FAMILIES = Map.copyOf(families);
        ELEMENTS = Map.copyOf(elements);
        BY_PATH = Map.copyOf(byPath);
        ALL = List.copyOf(forms);
        for (Form form : ALL) {
            byTranslationKey.put(form.translationKey(), form);
        }
        BY_TRANSLATION_KEY = Map.copyOf(byTranslationKey);
    }
    
    public static Form base(Family family) {
        return BASES.get(family);
    }
    
    public static Form variant(Family family, Element element) {
        return VARIANTS.get(family).get(element);
    }
    
    @Nullable
    public static Form byPath(String path) {
        return BY_PATH.get(path);
    }
    
    @Nullable
    public static Form byTranslationKey(String translationKey) {
        return BY_TRANSLATION_KEY.get(translationKey);
    }
    
    @Nullable
    public static Family familyOf(Form form) {
        return FAMILIES.get(form);
    }
    
    @Nullable
    public static Element elementOf(Form form) {
        return ELEMENTS.get(form);
    }
    
    public static boolean canPreserveProgress(Form source, Form target) {
        Family family = FAMILIES.get(source);
        if (family == null) {
            return false;
        }
        boolean sourceIsBase = base(family).equals(source);
        boolean targetIsBase = base(family).equals(target);
        if (!sourceIsBase && target.equals(PRIMORDIAL_CURSE)) {
            return true;
        }
        return family == FAMILIES.get(target) && sourceIsBase != targetIsBase;
    }
    
    public enum Family {
        MYSTERY("rodai_mystery", 6.5F, 500, 0),
        VOID("rodai_void", 7.0F, 150, 1),
        BLOOD("rodai_blood", 7.5F, 200, 2),
        TAINT("rodai_taint", 4.0F, 59, 0);
        
        private final String path;
        private final float attack;
        private final int durability;
        private final int warp;
        
        Family(String path, float attack, int durability, int warp) {
            this.path = path;
            this.attack = attack;
            this.durability = durability;
            this.warp = warp;
        }
    }
    
    public enum Element {
        THUNDER("thunder", 1), SKY("sky", 1), TIDE("tide", 0),
        GROWTH("growth", 0), FLAME("flame", 1), EARTH("earth", 0);
        
        private final String path;
        private final int attackBonus;
        
        Element(String path, int attackBonus) {
            this.path = path;
            this.attackBonus = attackBonus;
        }
    }
    
    public record Form(String path, float attack, int durability, int nativeWarp) {
        public ResourceLocation id() {
            return TEBlade.prefix(path);
        }
        
        public String translationKey() {
            return TRANSLATION_KEY_PREFIX + path;
        }
    }
}