package net.mrqx.slashblade.teblade.data.thaumaturge.spell;

import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.api.spell.affinity.AspectAffinity;
import com.leclowndu93150.thaumaturge.api.spell.affinity.SpellAction;
import com.leclowndu93150.thaumaturge.content.spell.action.*;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.data.thaumaturge.aspect.TEBladeAspects;
import net.mrqx.slashblade.teblade.data.thaumaturge.research.BladeResearchEntries;

import java.util.List;
import java.util.Optional;

public final class TEBladeSpellAffinitiesBootstrap {
    private static final float TICK = 20.0F;
    
    public static void bootstrap(BootstrapContext<AspectAffinity> ctx) {
        affinity(
            ctx,
            TEBladeAspects.SLASH_BLADE,
            BladeResearchEntries.UNLOCK,
            List.of(new DamageAction(DamageTypes.INDIRECT_MAGIC, 2.0F, 1.0F, 1.0F, false)),
            List.of(),
            List.of());
        affinity(
            ctx,
            TEBladeAspects.PROUD_SOUL,
            BladeResearchEntries.BLADE_RODAI,
            List.of(
                new DamageAction(DamageTypes.INDIRECT_MAGIC, 1.0F, 1.0F, 1.0F, false),
                effect(MobEffects.GLOWING, 100.0F, 0.0F)),
            List.of(),
            List.of());
        affinity(
            ctx,
            TEBladeAspects.AKURYO,
            BladeResearchEntries.family(ThaumaturgeBladeForms.Family.MYSTERY),
            List.of(
                new DamageAction(DamageTypes.INDIRECT_MAGIC, 1.0F, 0.5F, 1.0F, false),
                effect(MobEffects.WITHER, 100.0F, 0.0F)),
            List.of(),
            List.of());
        affinity(
            ctx,
            TEBladeAspects.RAIN,
            BladeResearchEntries.SIX_BREATHS,
            List.of(new ExtinguishAction(1.0F), effect(MobEffects.MOVEMENT_SLOWDOWN, 60.0F, 0.0F)),
            List.of(new ExtinguishAction(1.0F)),
            List.of(effect(MobEffects.WATER_BREATHING, 100.0F, 0.0F)));
        affinity(
            ctx,
            TEBladeAspects.TEMPEST,
            BladeResearchEntries.SIX_BREATHS,
            List.of(new KnockbackAction(0.6F, 0.25F, 0.35F, false)),
            List.of(new WindBurstAction(1.0F, 0.4F, 3.0F)),
            List.of(effect(MobEffects.SLOW_FALLING, 100.0F, 0.0F)));
        affinity(
            ctx,
            TEBladeAspects.STASIS,
            BladeResearchEntries.SIX_BREATHS,
            List.of(effect(MobEffects.MOVEMENT_SLOWDOWN, 80.0F, 1.0F), new FreezeAction(40.0F, TICK)),
            List.of(new FreezeWaterAction(0.0F, 1.5F, 8.0F)),
            List.of(effect(MobEffects.DAMAGE_RESISTANCE, 100.0F, 0.0F)));
    }
    
    private static void affinity(
        BootstrapContext<AspectAffinity> ctx,
        ResourceKey<IAspect> aspect,
        ResourceLocation research,
        List<SpellAction> entity,
        List<SpellAction> block,
        List<SpellAction> imbue) {
        ctx.register(
            ResourceKey.create(AspectAffinity.REGISTRY_KEY, aspect.location()),
            new AspectAffinity(
                TTIds.rl("sparkle"),
                0,
                0.0F,
                Optional.of(new ResearchGate(research, Optional.empty(), false)),
                Optional.empty(),
                entity,
                block,
                imbue));
    }
    
    private static MobEffectAction effect(Holder<MobEffect> effect, float ticks, float amplifier) {
        return new MobEffectAction(effect, ticks, TICK, amplifier, 0.0F, 4, 1.0F);
    }
}
