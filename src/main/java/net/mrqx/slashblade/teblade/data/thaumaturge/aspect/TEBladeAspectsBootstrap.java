package net.mrqx.slashblade.teblade.data.thaumaturge.aspect;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.content.aspect.Aspect;
import com.leclowndu93150.thaumaturge.data.worldgen.aspect.AspectBootstrap;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public final class TEBladeAspectsBootstrap {
    public static void bootstrap(BootstrapContext<IAspect> ctx) {
        AspectBootstrap.bootstrap(ctx);
        
        compound(ctx, TEBladeAspects.AKURYO, 0xCD3C4D, Aspect.DEFAULT_BLEND, TTAspects.MORTUUS, TTAspects.AVERSIO);
        compound(ctx, TEBladeAspects.PROUD_SOUL, 0xC60AFF, Aspect.DEFAULT_BLEND, TTAspects.PRAECANTATIO, TEBladeAspects.AKURYO);
        compound(ctx, TEBladeAspects.SLASH_BLADE, 0xF4F4F4, Aspect.DEFAULT_BLEND, TTAspects.INSTRUMENTUM, TEBladeAspects.PROUD_SOUL);
        
        compound(ctx, TEBladeAspects.RAIN, 0x171ED6, Aspect.DEFAULT_BLEND, TTAspects.AER, TTAspects.AQUA);
        compound(ctx, TEBladeAspects.TEMPEST, 0x0B2135, Aspect.DEFAULT_BLEND, TTAspects.PERDITIO, TEBladeAspects.RAIN);
        compound(ctx, TEBladeAspects.STASIS, 0x0073B5, Aspect.DEFAULT_BLEND, TTAspects.MOTUS, TTAspects.VINCULUM);
    }
    
    private static void compound(
        BootstrapContext<IAspect> ctx,
        ResourceKey<IAspect> key,
        int color,
        int blend,
        ResourceKey<IAspect> componentA,
        ResourceKey<IAspect> componentB) {
        Holder<IAspect> a = ctx.lookup(IAspect.REGISTRY_KEY).getOrThrow(componentA);
        Holder<IAspect> b = ctx.lookup(IAspect.REGISTRY_KEY).getOrThrow(componentB);
        ctx.register(
            key,
            new Aspect(
                key.location().getPath(),
                color,
                List.of(a, b),
                Optional.empty(),
                ResourceLocation.fromNamespaceAndPath(
                    key.location().getNamespace(),
                    "textures/aspects/" + key.location().getPath() + ".png"),
                blend));
    }
}