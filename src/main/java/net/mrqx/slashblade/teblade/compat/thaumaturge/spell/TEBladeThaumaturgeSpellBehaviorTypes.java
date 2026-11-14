package net.mrqx.slashblade.teblade.compat.thaumaturge.spell;

import com.leclowndu93150.thaumaturge.api.spell.behavior.SpellBehaviorType;
import com.leclowndu93150.thaumaturge.api.spell.part.SpellPartKind;
import net.mrqx.slashblade.teblade.TEBlade;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TEBladeThaumaturgeSpellBehaviorTypes {
    public static final DeferredRegister<SpellBehaviorType<?>> BEHAVIORS =
        DeferredRegister.create(SpellBehaviorType.REGISTRY_KEY, TEBlade.MODID);
    
    public static final DeferredHolder<SpellBehaviorType<?>, SpellBehaviorType<TEBladeCrescentEdgeDelivery>> CRESCENT_EDGE =
        BEHAVIORS.register("drive",
            () -> new SpellBehaviorType<>(SpellPartKind.DELIVERY, TEBladeCrescentEdgeDelivery.CODEC));
    
    public static final DeferredHolder<SpellBehaviorType<?>, SpellBehaviorType<TEBladeSummonedSwordsDelivery>> PHANTOM_SWORDS =
        BEHAVIORS.register("summoned_swords",
            () -> new SpellBehaviorType<>(SpellPartKind.DELIVERY, TEBladeSummonedSwordsDelivery.CODEC));
    
    public static final DeferredHolder<SpellBehaviorType<?>, SpellBehaviorType<TEBladeJudgementCutEffect>> JUDGEMENT_CUT =
        BEHAVIORS.register("judgement_cut",
            () -> new SpellBehaviorType<>(SpellPartKind.EFFECT, TEBladeJudgementCutEffect.CODEC));
    
    public static final DeferredHolder<SpellBehaviorType<?>, SpellBehaviorType<TEBladeSummonedSwordsArrayEffect>> PHANTOM_SWORDS_ARRAY =
        BEHAVIORS.register("summoned_swords_array",
            () -> new SpellBehaviorType<>(SpellPartKind.EFFECT, TEBladeSummonedSwordsArrayEffect.CODEC));
    
    public static void register(IEventBus modEventBus) {
        BEHAVIORS.register(modEventBus);
    }
}
