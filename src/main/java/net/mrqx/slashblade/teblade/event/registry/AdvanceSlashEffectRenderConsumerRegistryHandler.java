package net.mrqx.slashblade.teblade.event.registry;

import com.mojang.math.Axis;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.Face;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.mrqx.sbr_core.client.renderer.entity.RendererAdvanceSlashEffect;
import net.mrqx.sbr_core.events.AdvanceSlashEffectRenderConsumerRegistryEvent;
import net.mrqx.slashblade.teblade.specialeffect.WitnessRainbow;
import net.neoforged.bus.api.SubscribeEvent;

import java.awt.*;

public final class AdvanceSlashEffectRenderConsumerRegistryHandler {
    public static final RendererAdvanceSlashEffect.AdvanceSlashEffectRenderConsumer WITNESS_RAINBOW_RENDERER = (renderer, entity, entityYRot, partialTick, matrixStackIn, bufferIn, packedLightIn) -> {
        try (MSAutoCloser ignored = MSAutoCloser.pushMatrix(matrixStackIn)) {
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(entity.getRotationRoll()));
            
            WavefrontObject model = BladeModelManager.getInstance().getModel(entity.getModelLocation());
            
            int lifetime = entity.getLifetime();
            
            float progress = Math.min(lifetime, (entity.tickCount + partialTick)) / lifetime;
            
            double deathTime = lifetime;
            double baseAlpha = (Math.clamp(lifetime - (entity.tickCount) - partialTick, 0, deathTime) / deathTime);
            baseAlpha = -Math.pow(baseAlpha - 1, 4.0) + 1.0;
            
            // time
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(entity.getRotationOffset() - 135.0F * progress));
            
            matrixStackIn.scale(1, 0.25f, 1);
            
            float baseScale = 1.2f;
            matrixStackIn.scale(baseScale, baseScale, baseScale);
            
            float yScale = 0.03f;
            float scale = entity.getBaseSize() * Mth.lerp(progress, 0.03f, 0.035f);
            
            int color = entity.getColor() & 0xFFFFFF;
            
            Color color1 = new Color(color);
            float[] hsb = Color.RGBtoHSB(color1.getRed(), color1.getBlue(), color1.getGreen(), null);
            hsb[0] += (entity.level().getGameTime() + entity.tickCount + partialTick * 2) / 128;
            
            color = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            
            IConcentrationRank.ConcentrationRanks rank = entity.getRankCode();
            
            ResourceLocation rl = renderer.getTextureLocation(entity);
            
            // baseAlpha = 1.0f;
            int alpha = ((0xFF & (int) (0xFF * baseAlpha)) << 24);
            
            if (rank != null) {
                // black alpha inside
                if (IConcentrationRank.ConcentrationRanks.S.level <= rank.level) {
                    try (MSAutoCloser ignored1 = MSAutoCloser.pushMatrix(matrixStackIn)) {
                        float windscale = entity.getBaseSize() * Mth.lerp(progress, 0.035f, 0.03f);
                        matrixStackIn.scale(windscale, yScale, windscale);
                        Face.setAlphaOverride(Face.alphaOverrideYZZ);
                        Face.setUvOperator(1, 1, 0, -0.8f + progress * 0.3f);
                        BladeRenderState.setCol(0x222222 | alpha);
                        BladeRenderState.renderOverridedColorWrite(ItemStack.EMPTY, model, "base", rl, matrixStackIn, bufferIn, packedLightIn);
                    }
                }
                
                // color alpha base
                if (IConcentrationRank.ConcentrationRanks.D.level <= rank.level) {
                    try (MSAutoCloser ignored1 = MSAutoCloser.pushMatrix(matrixStackIn)) {
                        matrixStackIn.scale(scale, yScale, scale);
                        Face.setAlphaOverride(Face.alphaOverrideYZZ);
                        Face.setUvOperator(1, 1, 0, -0.35f + progress * -0.15f);
                        BladeRenderState.setCol(color | alpha);
                        BladeRenderState.renderOverridedColorWrite(ItemStack.EMPTY, model, "base", rl, matrixStackIn, bufferIn, packedLightIn);
                    }
                }
                
                // white add outside
                if (IConcentrationRank.ConcentrationRanks.B.level <= rank.level) {
                    try (MSAutoCloser ignored1 = MSAutoCloser.pushMatrix(matrixStackIn)) {
                        float windscale = entity.getBaseSize() * Mth.lerp(progress, 0.03f, 0.0375f);
                        matrixStackIn.scale(windscale, yScale, windscale);
                        Face.setAlphaOverride(Face.alphaOverrideYZZ);
                        Face.setUvOperator(1, 1, 0, -0.5f + progress * -0.2f);
                        BladeRenderState.setCol(0x404040 | alpha);
                        BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "base", rl, matrixStackIn, bufferIn, packedLightIn);
                    }
                }
            }
            
            // color add base
            try (MSAutoCloser ignored1 = MSAutoCloser.pushMatrix(matrixStackIn)) {
                matrixStackIn.scale(scale, yScale, scale);
                Face.setAlphaOverride(Face.alphaOverrideYZZ);
                Face.setUvOperator(1, 1, 0, -0.35f + progress * -0.15f);
                BladeRenderState.setCol(color | alpha);
                BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "base", rl, matrixStackIn, bufferIn, packedLightIn);
            }
        }
    };
    
    @SubscribeEvent
    public static void onAdvanceSlashEffectRenderConsumerRegistryEvent(AdvanceSlashEffectRenderConsumerRegistryEvent event) {
        event.registerConsumer(WitnessRainbow.WITNESS_RAINBOW_RENDER_CONSUMER_ID, WITNESS_RAINBOW_RENDERER);
    }
}
