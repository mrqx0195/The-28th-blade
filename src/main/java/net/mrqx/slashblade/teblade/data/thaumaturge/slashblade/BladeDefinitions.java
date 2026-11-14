package net.mrqx.slashblade.teblade.data.thaumaturge.slashblade;

import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.slashblade.PropertiesDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.RenderDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.BladeNativeEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSlashArts;
import net.mrqx.slashblade.teblade.data.slashblade.TEBladeDefinitionsProvider;
import net.mrqx.slashblade.teblade.registry.TEBladeSwordTypes;

import java.util.List;

public final class BladeDefinitions {
    public static final ResourceLocation PRIMORDIAL_CURSE_TEXTURE = TEBlade.prefix("model/named/primordial_curse/primordial_curse.png");
    public static final ResourceLocation PRIMORDIAL_CURSE_MODEL = TEBlade.prefix("model/named/primordial_curse/primordial_curse.obj");
    public static final int PRIMORDIAL_CURSE_SUMMON_COLOR = 0x4B1F7AFF;
    
    public static SlashBladeDefinition create(ThaumaturgeBladeForms.Form form) {
        return create(form, slashArts(form));
    }
    
    public static SlashBladeDefinition create(ThaumaturgeBladeForms.Form form, ResourceLocation slashArts) {
        PropertiesDefinition.Builder properties = PropertiesDefinition.Builder.newInstance()
            .baseAttackModifier(form.attack())
            .maxDamage(form.durability())
            .slashArtsType(slashArts);
        BladeNativeEffects.of(form).forEach(properties::addSpecialEffect);
        if (form.equals(ThaumaturgeBladeForms.PRIMORDIAL_CURSE)) {
            properties.defaultSwordType(List.of(SwordType.BEWITCHED, TEBladeSwordTypes.ANOMALY.getValue()));
        }
        return new SlashBladeDefinition(id(form), render(form).build(), properties.build(), List.of());
    }
    
    private static RenderDefinition.Builder render(ThaumaturgeBladeForms.Form form) {
        RenderDefinition.Builder builder = RenderDefinition.Builder.newInstance();
        if (form.equals(ThaumaturgeBladeForms.PRIMORDIAL_CURSE)) {
            return builder
                .textureName(PRIMORDIAL_CURSE_TEXTURE)
                .modelName(PRIMORDIAL_CURSE_MODEL)
                .effectColor(TEBladeDefinitionsProvider.getARGBFromRGBA(PRIMORDIAL_CURSE_SUMMON_COLOR));
        }
        if (form.equals(ThaumaturgeBladeForms.GREATWOOD_DUMMY) || form.equals(ThaumaturgeBladeForms.SILVERWOOD_SAYA)) {
            return builder.textureName(TEBlade.prefix("model/" + form.path() + ".png"));
        }
        if (form.path().startsWith("rodai_")) {
            return builder.textureName(TEBlade.prefix("model/rodai/" + form.path() + ".png"));
        }
        return builder;
    }
    
    public static ResourceLocation id(ThaumaturgeBladeForms.Form form) {
        return form.id();
    }
    
    public static ResourceKey<SlashBladeDefinition> key(ThaumaturgeBladeForms.Form form) {
        return ResourceKey.create(SlashBladeDefinition.REGISTRY_KEY, id(form));
    }
    
    private static ResourceLocation slashArts(ThaumaturgeBladeForms.Form form) {
        ThaumaturgeBladeForms.Element element = ThaumaturgeBladeForms.elementOf(form);
        if (element != null) {
            return switch (element) {
                case GROWTH -> TEBladeThaumaturgeSlashArts.GROWTH.getId();
                case EARTH -> TEBladeThaumaturgeSlashArts.EARTH.getId();
                case FLAME -> TEBladeThaumaturgeSlashArts.FLAME.getId();
                case THUNDER -> TEBladeThaumaturgeSlashArts.THUNDER.getId();
                case SKY -> TEBladeThaumaturgeSlashArts.SKY.getId();
                case TIDE -> TEBladeThaumaturgeSlashArts.TIDE.getId();
            };
        }
        ThaumaturgeBladeForms.Family family = ThaumaturgeBladeForms.familyOf(form);
        if (family == null) {
            return SlashArtsRegistry.JUDGEMENT_CUT.getId();
        }
        return switch (family) {
            case MYSTERY -> SlashArtsRegistry.DRIVE_VERTICAL.getId();
            case VOID -> SlashArtsRegistry.VOID_SLASH.getId();
            case BLOOD -> SlashArtsRegistry.PIERCING.getId();
            case TAINT -> SlashArtsRegistry.CIRCLE_SLASH.getId();
        };
    }
}
