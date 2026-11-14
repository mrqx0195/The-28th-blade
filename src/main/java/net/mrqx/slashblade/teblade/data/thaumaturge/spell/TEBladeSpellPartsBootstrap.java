package net.mrqx.slashblade.teblade.data.thaumaturge.spell;

import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.api.spell.behavior.SpellBehavior;
import com.leclowndu93150.thaumaturge.api.spell.part.AspectInput;
import com.leclowndu93150.thaumaturge.api.spell.part.SettingSpec;
import com.leclowndu93150.thaumaturge.api.spell.part.SpellPart;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.TEBladeCrescentEdgeDelivery;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.TEBladeJudgementCutEffect;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.TEBladeSummonedSwordsArrayEffect;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.TEBladeSummonedSwordsDelivery;
import net.mrqx.slashblade.teblade.data.thaumaturge.aspect.TEBladeAspects;
import net.mrqx.slashblade.teblade.data.thaumaturge.research.BladeResearchEntries;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("SameParameterValue")
public final class TEBladeSpellPartsBootstrap {
    private static final float DEFAULT_POWER = 1.0F;
    private static final float DEFAULT_VIS = 0.0F;
    private static final float DEFAULT_MULTIPLIER = 1.0F;
    private static final List<String> FORMATION_LABELS = List.of(
        "spell.teblade.setting.formation.spiral",
        "spell.teblade.setting.formation.storm",
        "spell.teblade.setting.formation.blistering",
        "spell.teblade.setting.formation.heavy_rain");
    
    public static void bootstrap(BootstrapContext<SpellPart> ctx) {
        // TODO: 绘制并换正式图标，研究中的图标也得换
        ctx.register(partKey("drive"), part(
            new TEBladeCrescentEdgeDelivery(),
            "thaumaturge:textures/foci/bolt.png",
            0xFF8A5B,
            4,
            new AspectInput.Fixed(TEBladeAspects.AKURYO),
            List.of(setting("range", 1, 8, 1, 4, 0.5F)),
            gate(BladeResearchEntries.SPELL_DRIVE)));
        ctx.register(partKey("summoned_swords"), part(
            new TEBladeSummonedSwordsDelivery(),
            "thaumaturge:textures/foci/projectile.png",
            0x7FCBFF,
            5,
            new AspectInput.Fixed(TEBladeAspects.PROUD_SOUL),
            List.of(setting("count", 1, 6, 1, 2, 0.5F)),
            gate(BladeResearchEntries.SPELL_SUMMONED_SWORDS)));
        ctx.register(partKey("judgement_cut"), part(
            new TEBladeJudgementCutEffect(),
            "thaumaturge:textures/foci/rift.png",
            0x8A5CFF,
            7,
            new AspectInput.Fixed(TEBladeAspects.SLASH_BLADE),
            List.of(setting("hits", 3, 8, 1, 6, 1.5F)),
            gate(BladeResearchEntries.SPELL_JUDGEMENT_CUT)));
        ctx.register(partKey("summoned_swords_array"), part(
            new TEBladeSummonedSwordsArrayEffect(),
            "thaumaturge:textures/foci/spread.png",
            0x3ED9C8,
            9,
            new AspectInput.Fixed(TEBladeAspects.PROUD_SOUL),
            List.of(
                labeled("formation", 0, 3, 0, 0.5F, FORMATION_LABELS),
                setting("count", 4, 8, 1, 4, 0.75F)),
            gate(BladeResearchEntries.SPELL_SUMMONED_SWORDS)));
    }
    
    private static SpellPart part(SpellBehavior behavior, String icon, int color, int complexity, AspectInput aspectInput,
                                  List<SettingSpec> settings, Optional<ResearchGate> research) {
        return new SpellPart(
            behavior,
            ResourceLocation.parse(icon),
            color,
            complexity,
            DEFAULT_VIS,
            DEFAULT_POWER,
            aspectInput,
            settings,
            research,
            0,
            false,
            Optional.empty(),
            DEFAULT_MULTIPLIER,
            DEFAULT_MULTIPLIER,
            TTIds.rl("sparkle"));
    }
    
    private static SettingSpec setting(String key, int min, int max, int step, int defaultValue, float complexity) {
        return new SettingSpec(key, min, max, step, defaultValue, complexity, 0.0F, Optional.empty(),
            Optional.of("spell.teblade.setting." + key));
    }
    
    private static SettingSpec labeled(String key, int min, int max, int defaultValue, float complexity,
                                       List<String> labels) {
        return new SettingSpec(key, min, max, 1, defaultValue, complexity, 0.0F, Optional.of(labels),
            Optional.of("spell.teblade.setting." + key));
    }
    
    private static Optional<ResearchGate> gate(ResourceLocation entry) {
        return Optional.of(new ResearchGate(entry, Optional.empty(), false));
    }
    
    private static ResourceKey<SpellPart> partKey(String path) {
        return ResourceKey.create(SpellPart.REGISTRY_KEY, TEBlade.prefix(path));
    }
}
