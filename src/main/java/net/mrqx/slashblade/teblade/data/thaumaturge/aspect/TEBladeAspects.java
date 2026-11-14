package net.mrqx.slashblade.teblade.data.thaumaturge.aspect;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import net.minecraft.resources.ResourceKey;
import net.mrqx.slashblade.teblade.TEBlade;

public final class TEBladeAspects {
    public static final ResourceKey<IAspect> AKURYO = key("akuryo");
    public static final ResourceKey<IAspect> PROUD_SOUL = key("proud_soul");
    public static final ResourceKey<IAspect> SLASH_BLADE = key("slash_blade");
    public static final ResourceKey<IAspect> RAIN = key("rain");
    public static final ResourceKey<IAspect> TEMPEST = key("tempest");
    public static final ResourceKey<IAspect> STASIS = key("stasis");
    
    private static ResourceKey<IAspect> key(String tag) {
        return ResourceKey.create(IAspect.REGISTRY_KEY, TEBlade.prefix(tag));
    }
}
