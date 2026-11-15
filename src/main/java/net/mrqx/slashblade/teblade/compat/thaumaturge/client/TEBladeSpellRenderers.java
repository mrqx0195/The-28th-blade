package net.mrqx.slashblade.teblade.compat.thaumaturge.client;

import mods.flammpfeil.slashblade.client.renderer.entity.DriveRenderer;
import mods.flammpfeil.slashblade.client.renderer.entity.JudgementCutRenderer;
import mods.flammpfeil.slashblade.client.renderer.entity.SummonedSwordRenderer;
import net.mrqx.slashblade.teblade.compat.TEBladeCompatManager;
import net.mrqx.slashblade.teblade.compat.thaumaturge.spell.TEBladeThaumaturgeEntityTypes;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class TEBladeSpellRenderers {
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (!TEBladeCompatManager.THAUMATURGE) {
            return;
        }
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.SPELL_DRIVE.get(), DriveRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.SPELL_SUMMONED_SWORD.get(), SummonedSwordRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.SPELL_JUDGEMENT_CUT.get(), JudgementCutRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.THAUMATURGE_SPIRAL_SWORDS.get(), SummonedSwordRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.THAUMATURGE_STORM_SWORDS.get(), SummonedSwordRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.THAUMATURGE_BLISTERING_SWORDS.get(), SummonedSwordRenderer::new);
        event.registerEntityRenderer(TEBladeThaumaturgeEntityTypes.THAUMATURGE_HEAVY_RAIN_SWORDS.get(), SummonedSwordRenderer::new);
    }
}
