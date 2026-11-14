package net.mrqx.slashblade.teblade;

import com.mojang.logging.LogUtils;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.slashblade.teblade.compat.TEBladeCompatManager;
import net.mrqx.slashblade.teblade.playeranim.PlayerAnimationRegistryHandler;
import net.mrqx.slashblade.teblade.registry.*;
import net.mrqx.slashblade.teblade.registry.combo.TEBladeComboStateRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(TEBlade.MODID)
@EventBusSubscriber
public final class TEBlade {
    public static final String MODID = "teblade";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
    
    public static ResourceLocation vmdLocBlade(String path) {
        return TEBlade.prefix("vmd/" + path + ".blade.vmd");
    }
    
    public static ResourceLocation vmdLocPlayer(String path) {
        return TEBlade.prefix("vmd/" + path + ".player.vmd");
    }
    
    public TEBlade(IEventBus modEventBus, ModContainer modContainer) {
        TEBladeAttachmentTypesRegistry.ATTACHMENT_TYPES.register(modEventBus);
        TEBladeComboStateRegistry.COMBO_STATE.register(modEventBus);
        TEBladeDataComponentTypesRegister.DATA_COMPONENT_TYPES.register(modEventBus);
        TEBladeEntityTypesRegister.ENTITY_TYPES.register(modEventBus);
        TEBladeMobEffectsRegistry.MOB_EFFECTS.register(modEventBus);
        TEBladeRecipeSerializersRegistry.SERIALIZERS.register(modEventBus);
        TEBladeSlashArtsRegistry.SLASH_ARTS.register(modEventBus);
        TEBladeSpecialEffectsRegistry.SPECIAL_EFFECTS.register(modEventBus);
//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        TEBladeCompatManager.modInit(modEventBus, modContainer);
    }
    
    @SubscribeEvent
    public static void onFMLCommonSetupEvent(FMLCommonSetupEvent event) {
        SlashBlade.MOD_EVENT_BUS.register(PlayerAnimationRegistryHandler.class);
        TEBladeCompatManager.commonSetup(event);
    }
}
