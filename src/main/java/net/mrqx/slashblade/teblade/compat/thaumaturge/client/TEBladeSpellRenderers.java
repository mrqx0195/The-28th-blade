package net.mrqx.slashblade.teblade.compat.thaumaturge.client;

import mods.flammpfeil.slashblade.client.renderer.entity.DriveRenderer;
import mods.flammpfeil.slashblade.client.renderer.entity.JudgementCutRenderer;
import mods.flammpfeil.slashblade.client.renderer.entity.SummonedSwordRenderer;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.compat.TEBladeCompatManager;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.TEBladeThaumaturgeEntityTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = TEBlade.MODID, value = Dist.CLIENT)
public final class TEBladeSpellRenderers {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (!TEBladeCompatManager.THAUMATURGE) {
            return;
        }
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.SPELL_DRIVE.get(), DriveRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.SPELL_SUMMONED_SWORD.get(), SummonedSwordRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.SPELL_JUDGEMENT_CUT.get(), JudgementCutRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.SPIRAL_SWORDS.get(), SummonedSwordRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.STORM_SWORDS.get(), SummonedSwordRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.BLISTERING_SWORDS.get(), SummonedSwordRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.HEAVY_RAIN_SWORDS.get(), SummonedSwordRenderer::new);
    }
}
