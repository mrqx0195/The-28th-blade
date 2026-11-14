package net.mrqx.slashblade.teblade.compat.thaumaturge;

import net.minecraft.resources.ResourceLocation;

public final class TEBladeResearchKeys {
    public static String title(ResourceLocation id) {
        return base(id) + ".title";
    }
    
    public static String stage(ResourceLocation id, int index) {
        return base(id) + ".stage_" + index;
    }
    
    public static String note(ResourceLocation id, String key) {
        return base(id) + "." + key;
    }
    
    public static String aspect(ResourceLocation id) {
        return "aspect." + id.getNamespace() + "." + id.getPath();
    }
    
    public static String observation(String key) {
        return "research.teblade.observation." + key;
    }
    
    private static String base(ResourceLocation id) {
        return "research." + id.getNamespace() + "." + id.getPath().replace('/', '.');
    }
}
