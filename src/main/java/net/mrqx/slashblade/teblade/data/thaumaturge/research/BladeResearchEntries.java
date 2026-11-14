package net.mrqx.slashblade.teblade.data.thaumaturge.research;

import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;
import com.leclowndu93150.thaumaturge.api.research.*;
import com.leclowndu93150.thaumaturge.content.research.ResearchEntry;
import com.leclowndu93150.thaumaturge.content.research.ResearchStage;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.thaumaturge.TEBladeResearchKeys;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.data.thaumaturge.aspect.TEBladeAspects;
import net.mrqx.slashblade.teblade.data.thaumaturge.recipe.TEBladeThaumaturgeRecipes;

import java.util.*;
import java.util.stream.Collectors;

public final class BladeResearchEntries {
    public static final ResourceKey<IResearchCategory> CATEGORY = ResourceKey.create(IResearchCategory.REGISTRY_KEY, TEBlade.prefix("bladecraft"));
    
    public static final ResourceLocation SCANNED = TEBlade.prefix("scanned/slashblade");
    public static final ResourceLocation UNLOCK = TEBlade.prefix("unlock_bladecraft");
    public static final ResourceLocation BLADE_RODAI = TEBlade.prefix("blade_rodai");
    public static final ResourceLocation SIX_BREATHS = TEBlade.prefix("six_breaths");
    public static final ResourceLocation BLADE_REVERT = TEBlade.prefix("blade_revert");
    public static final ResourceLocation PRIMORDIAL_CURSE = ThaumaturgeBladeForms.PRIMORDIAL_CURSE.id();
    public static final ResourceLocation SPELL_DRIVE = TEBlade.prefix("spell_drive");
    public static final ResourceLocation SPELL_JUDGEMENT_CUT = TEBlade.prefix("spell_judgement_cut");
    public static final ResourceLocation SPELL_SUMMONED_SWORDS = TEBlade.prefix("spell_summoned_swords");
    
    private static final ResourceLocation UPSTREAM_ELEMENTAL_TOOLS = TTIds.rl("elemental_tools");
    private static final ResourceLocation UPSTREAM_ROD_SILVERWOOD = TTIds.rl("rod_silverwood");
    private static final ResourceLocation UPSTREAM_BASE_ELDRITCH = TTIds.rl("base_eldritch");
    private static final ResourceLocation UPSTREAM_CRIMSON_RITES = TTIds.rl("crimson_rites");
    private static final ResourceLocation UPSTREAM_BOTTLE_TAINT = TTIds.rl("bottle_taint");
    private static final ResourceLocation UPSTREAM_PRIMORDIAL_PEARL = TTIds.rl("primordial_pearl");
    private static final ResourceLocation UPSTREAM_WARP = TTIds.rl("warp");
    
    public static ResourceLocation breath(ThaumaturgeBladeForms.Element element) {
        return TEBlade.prefix("breath_" + element.name().toLowerCase(Locale.ROOT));
    }
    
    public static ResourceLocation family(ThaumaturgeBladeForms.Family family) {
        return TEBlade.prefix(ThaumaturgeBladeForms.base(family).path());
    }
    
    public static void bootstrap(BootstrapContext<IResearchEntry> context) {
        firstSteps(context);
        families(context);
        breaths(context);
        endgame(context);
        spellArts(context);
    }
    
    private static void firstSteps(BootstrapContext<IResearchEntry> context) {
        HolderGetter<IAspect> aspects = context.lookup(IAspect.REGISTRY_KEY);
        entry(context, SCANNED, TTResearchCategories.BASICS, new Cell(10, 8),
            SlashBladeItems.SLASHBLADE.getKey().location(), parents(), Set.of(ResearchEntryMeta.HIDDEN),
            Gate.OBSERVATION, Price.FREE, BladeResearchStages.single(SCANNED));
        entry(context, UNLOCK, CATEGORY, new Cell(-2, 0), SlashBladeItems.PROUDSOUL_TINY.getKey().location(),
            parents(UPSTREAM_ELEMENTAL_TOOLS, SCANNED), Set.of(ResearchEntryMeta.ROUND, ResearchEntryMeta.HIDDEN),
            Gate.OBSERVATION, price(AspectList.of(aspect(aspects, TEBladeAspects.PROUD_SOUL, 5))),
            BladeResearchStages.pages(UNLOCK, List.of(), 0));
        entry(context, BLADE_RODAI, CATEGORY, new Cell(0, 0), TTItems.WAND_ROD_SILVERWOOD.getKey().location(),
            parents(UNLOCK, UPSTREAM_ROD_SILVERWOOD), Set.of(), Gate.OBSERVATION,
            cost(aspect(aspects, TEBladeAspects.SLASH_BLADE, 5), aspect(aspects, TTAspects.HERBA, 5)),
            BladeResearchStages.pages(BLADE_RODAI, List.of(
                TEBladeThaumaturgeRecipes.materialId(ThaumaturgeBladeForms.GREATWOOD_DUMMY),
                TEBladeThaumaturgeRecipes.materialId(ThaumaturgeBladeForms.SILVERWOOD_SAYA)), 0));
    }
    
    private static void families(BootstrapContext<IResearchEntry> context) {
        HolderGetter<IAspect> aspects = context.lookup(IAspect.REGISTRY_KEY);
        for (ThaumaturgeBladeForms.Family family : ThaumaturgeBladeForms.Family.values()) {
            Set<ResearchParent> prerequisites = switch (family) {
                case MYSTERY -> parents(BLADE_RODAI);
                case VOID -> parents(BLADE_RODAI, UPSTREAM_BASE_ELDRITCH);
                case BLOOD -> parents(BLADE_RODAI, UPSTREAM_CRIMSON_RITES);
                case TAINT -> parents(BLADE_RODAI, UPSTREAM_BOTTLE_TAINT);
            };
            Gate gate = family == ThaumaturgeBladeForms.Family.BLOOD || family == ThaumaturgeBladeForms.Family.TAINT
                ? Gate.theory(2) : Gate.OBSERVATION;
            entry(context, family(family), CATEGORY, new Cell(2, -3 + family.ordinal() * 2), familyIcon(family),
                prerequisites, Set.of(), gate, familyCost(aspects, family),
                BladeResearchStages.pages(family(family), List.of(TEBladeThaumaturgeRecipes.rodaiId(family)),
                    family == ThaumaturgeBladeForms.Family.MYSTERY ? 0 : 1));
        }
    }
    
    private static void breaths(BootstrapContext<IResearchEntry> context) {
        HolderGetter<IAspect> aspects = context.lookup(IAspect.REGISTRY_KEY);
        entry(context, SIX_BREATHS, CATEGORY, new Cell(5, 0), TTItems.ESSENTIA_CRYSTAL.getKey().location(),
            parents(BLADE_RODAI, UPSTREAM_ELEMENTAL_TOOLS), Set.of(ResearchEntryMeta.HIDDEN), Gate.theory(2),
            cost(aspect(aspects, TEBladeAspects.SLASH_BLADE, 5), aspect(aspects, TTAspects.PRAECANTATIO, 10)),
            BladeResearchStages.pages(SIX_BREATHS, List.of(), 0));
        for (ThaumaturgeBladeForms.Element element : ThaumaturgeBladeForms.Element.values()) {
            List<AspectInstance> entries = elementAspects(element).stream().map(aspect -> aspect(aspects, aspect, 5)).collect(Collectors.toCollection(() -> new ArrayList<>(2)));
            entry(context, breath(element), CATEGORY,
                new Cell(8 + element.ordinal() % 2 * 2, -3 + element.ordinal() / 2 * 3),
                TTItems.ESSENTIA_CRYSTAL.getKey().location(), parents(SIX_BREATHS), Set.of(), Gate.OBSERVATION,
                new Price(AspectList.ofEntries(entries), crystals(context, element)),
                BladeResearchStages.pages(breath(element), TEBladeThaumaturgeRecipes.elementIds(element), 0));
        }
    }
    
    private static void endgame(BootstrapContext<IResearchEntry> context) {
        HolderGetter<IAspect> aspects = context.lookup(IAspect.REGISTRY_KEY);
        entry(context, BLADE_REVERT, CATEGORY, new Cell(5, 4), TTItems.SALIS_MUNDUS.getKey().location(),
            parents(SIX_BREATHS), Set.of(), Gate.theory(2),
            new Price(AspectList.ofEntries(List.of(
                aspect(aspects, TTAspects.ORDO, 5), aspect(aspects, TTAspects.PRAECANTATIO, 5))),
                salts(context)),
            BladeResearchStages.pages(BLADE_REVERT, TEBladeThaumaturgeRecipes.reversionIds(), 0));
        entry(context, PRIMORDIAL_CURSE, CATEGORY, new Cell(13, 0), TTItems.PRIMORDIAL_PEARL.getKey().location(),
            parents(SIX_BREATHS, UPSTREAM_PRIMORDIAL_PEARL, UPSTREAM_WARP),
            Set.of(ResearchEntryMeta.SPIKY, ResearchEntryMeta.HIDDEN), Gate.theory(3),
            cost(
                aspect(aspects, TEBladeAspects.AKURYO, 10),
                aspect(aspects, TEBladeAspects.PROUD_SOUL, 10),
                aspect(aspects, TEBladeAspects.SLASH_BLADE, 10),
                aspect(aspects, TTAspects.ALIENIS, 10),
                aspect(aspects, TTAspects.PRAECANTATIO, 10)
            ),
            BladeResearchStages.terminal()
        );
    }
    
    private static void spellArts(BootstrapContext<IResearchEntry> context) {
        HolderGetter<IAspect> aspects = context.lookup(IAspect.REGISTRY_KEY);
        entry(context, SPELL_DRIVE, CATEGORY, new Cell(-3, 2), SlashBladeItems.SLASHBLADE.getKey().location(),
            parents(UNLOCK), Set.of(ResearchEntryMeta.REVERSE), Gate.theory(2),
            cost(aspect(aspects, TEBladeAspects.SLASH_BLADE, 3), aspect(aspects, TTAspects.MOTUS, 3)), BladeResearchStages.single(SPELL_DRIVE));
        entry(context, SPELL_JUDGEMENT_CUT, CATEGORY, new Cell(-1, 2), TTItems.VOID_SWORD.getKey().location(),
            parents(UNLOCK), Set.of(ResearchEntryMeta.REVERSE), Gate.theory(3),
            cost(aspect(aspects, TEBladeAspects.PROUD_SOUL, 5), aspect(aspects, TTAspects.VACUOS, 5)), BladeResearchStages.single(SPELL_JUDGEMENT_CUT));
        entry(context, SPELL_SUMMONED_SWORDS, CATEGORY, new Cell(-2, 3), TTItems.THAUMIUM_SWORD.getKey().location(),
            parents(BLADE_RODAI), Set.of(), Gate.theory(3),
            cost(aspect(aspects, TEBladeAspects.AKURYO, 5), aspect(aspects, TTAspects.PRAECANTATIO, 5)), BladeResearchStages.single(SPELL_SUMMONED_SWORDS));
    }
    
    private static ResourceLocation familyIcon(ThaumaturgeBladeForms.Family family) {
        return switch (family) {
            case MYSTERY -> TTItems.THAUMIUM_SWORD.getKey().location();
            case VOID -> TTItems.VOID_SWORD.getKey().location();
            case BLOOD -> TTItems.CRIMSON_BLADE.getKey().location();
            case TAINT -> TTItems.CRYSTAL_VITIUM.getKey().location();
        };
    }
    
    private static Price familyCost(HolderGetter<IAspect> aspects, ThaumaturgeBladeForms.Family family) {
        return switch (family) {
            case MYSTERY -> cost(
                aspect(aspects, TEBladeAspects.PROUD_SOUL, 5),
                aspect(aspects, TTAspects.PRAECANTATIO, 5)
            );
            case VOID -> cost(
                aspect(aspects, TEBladeAspects.SLASH_BLADE, 5),
                aspect(aspects, TTAspects.VACUOS, 5)
            );
            case BLOOD -> cost(
                aspect(aspects, TEBladeAspects.AKURYO, 5),
                aspect(aspects, TTAspects.VICTUS, 5)
            );
            case TAINT -> cost(
                aspect(aspects, TEBladeAspects.AKURYO, 5),
                aspect(aspects, TTAspects.VITIUM, 5)
            );
        };
    }
    
    private static List<ResourceKey<IAspect>> elementAspects(ThaumaturgeBladeForms.Element element) {
        return switch (element) {
            case THUNDER, SKY -> List.of(TTAspects.AER, TTAspects.MOTUS);
            case TIDE -> List.of(TTAspects.AQUA, TTAspects.HERBA);
            case FLAME -> List.of(TTAspects.IGNIS, TTAspects.METALLUM);
            case EARTH -> List.of(TTAspects.TERRA, TTAspects.FABRICO);
            case GROWTH -> List.of(TTAspects.ORDO, TTAspects.PERDITIO);
        };
    }
    
    private static List<ResourceKey<IAspect>> crystalAspects(ThaumaturgeBladeForms.Element element) {
        return switch (element) {
            case THUNDER, SKY -> List.of(TTAspects.AER);
            case TIDE -> List.of(TTAspects.AQUA);
            case FLAME -> List.of(TTAspects.IGNIS);
            case EARTH -> List.of(TTAspects.TERRA);
            case GROWTH -> List.of(TTAspects.ORDO, TTAspects.PERDITIO);
        };
    }
    
    private static List<ResearchRequirement> crystals(BootstrapContext<IResearchEntry> context,
                                                      ThaumaturgeBladeForms.Element element) {
        HolderGetter<IAspect> aspects = context.lookup(IAspect.REGISTRY_KEY);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        List<ResourceKey<IAspect>> required = crystalAspects(element);
        return required.stream()
            .map(aspect -> new ResearchRequirement(HolderSet.direct(items.getOrThrow(TTItems.ESSENTIA_CRYSTAL.getKey())),
                DataComponentPatch.builder().set(TTDataComponents.CRYSTAL_ASPECT.get(), aspect(aspects, aspect, 1)).build(), 1)
            )
            .collect(Collectors.toCollection(() -> new ArrayList<>(required.size())));
    }
    
    private static List<ResearchRequirement> salts(BootstrapContext<IResearchEntry> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        return List.of(new ResearchRequirement(
            HolderSet.direct(items.getOrThrow(TTItems.SALIS_MUNDUS.getKey())), DataComponentPatch.EMPTY, 1));
    }
    
    private static AspectInstance aspect(HolderGetter<IAspect> aspects, ResourceKey<IAspect> aspect, int amount) {
        return new AspectInstance(aspects.getOrThrow(aspect), amount);
    }
    
    private static Price price(AspectList aspects) {
        return new Price(aspects, List.of());
    }
    
    private static Price cost(AspectInstance... aspects) {
        return price(AspectList.ofEntries(List.of(aspects)));
    }
    
    private static Set<ResearchParent> parents(ResourceLocation... ids) {
        return Arrays.stream(ids)
            .map(ResearchParent::of)
            .collect(Collectors.toCollection(() -> new LinkedHashSet<>(ids.length)));
    }
    
    private static void entry(BootstrapContext<IResearchEntry> context, ResourceLocation id,
                              ResourceKey<IResearchCategory> category, Cell cell, ResourceLocation icon,
                              Set<ResearchParent> parents, Set<ResearchEntryMeta> meta, Gate gate, Price price,
                              List<IResearchStage> stages) {
        Holder.Reference<IResearchCategory> categoryHolder =
            context.lookup(IResearchCategory.REGISTRY_KEY).getOrThrow(category);
        boolean gated = !id.getPath().contains("/");
        List<IResearchStage> adjusted = stages;
        if (gated) {
            IResearchStage first = stages.getFirst();
            List<IResearchStage> gatedStages = new ArrayList<>(stages);
            gatedStages.set(0, new ResearchStage(TEBladeResearchKeys.stage(id, 0), first.recipes(), first.construct(),
                price.obtain(), first.craft(), first.knowledge(),
                List.of(new KnowledgeReward(gate.type(), categoryHolder, 1)), first.requiredResearch(), first.warp()));
            adjusted = List.copyOf(gatedStages);
        }
        EnumSet<ResearchEntryMeta> flags = EnumSet.noneOf(ResearchEntryMeta.class);
        flags.addAll(meta);
        context.register(ResourceKey.create(IResearchEntry.REGISTRY_KEY, id), new ResearchEntry(
            categoryHolder, TEBladeResearchKeys.title(id), parents, Set.of(), cell.column(), cell.row(), adjusted,
            flags, List.of(ResearchIcon.ofItem(icon)), List.of(), price.aspects(), gate.complexity()));
    }
    
    private record Cell(int column, int row) {
    }
    
    private record Gate(KnowledgeType type, int complexity) {
        private static final Gate OBSERVATION = new Gate(KnowledgeType.OBSERVATION, 1);
        
        private static Gate theory(int complexity) {
            return new Gate(KnowledgeType.THEORY, complexity);
        }
    }
    
    private record Price(AspectList aspects, List<ResearchRequirement> obtain) {
        private static final Price FREE = new Price(AspectList.EMPTY, List.of());
    }
}
