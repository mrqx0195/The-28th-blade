package net.mrqx.slashblade.teblade.specialeffect;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mrqx.sbr_core.entity.EntityAdvanceSlashEffect;
import net.mrqx.sbr_core.registry.MrqxEntityTypesRegistry;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.registry.TEBladeSpecialEffectsRegistry;
import net.mrqx.slashblade.teblade.utils.TEBladeDamageSources;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public final class WitnessRainbow extends SpecialEffect {
    public static final ResourceLocation WITNESS_RAINBOW_RENDER_CONSUMER_ID = TEBlade.prefix("witness_rainbow");
    
    public WitnessRainbow() {
        super(0, false, false);
    }
    
    @Override
    public boolean isEffective(Entity user) {
        return true;
    }
    
    @SubscribeEvent
    public static void onBreak(SlashBladeEvent.BreakEvent event) {
        if (event.getSlashBladeState().hasSpecialEffect(TEBladeSpecialEffectsRegistry.WITNESS_RAINBOW.getId())) {
            event.getBlade().setDamageValue(event.getBlade().getMaxDamage() - 2);
        }
    }
    
    @SubscribeEvent
    public static void onDoSlash(SlashBladeEvent.DoSlashEvent event) {
        ISlashBladeState state = event.getSlashBladeState();
        LivingEntity user = event.getUser();
        if (state.isBroken()
            && state.hasSpecialEffect(TEBladeSpecialEffectsRegistry.WITNESS_RAINBOW.getId())
            && user.getAttributeValue(Attributes.ATTACK_DAMAGE) > 0
        ) {
            event.setCanceled(true);
            
            Level level = user.level();
            
            EntityAdvanceSlashEffect slash = new EntityAdvanceSlashEffect(MrqxEntityTypesRegistry.ADVANCE_SLASH_EFFECT.get(), level);
            Vec3 pos = user.position().add(0.0D, (double) user.getEyeHeight() * 0.75D, 0.0D)
                .add(user.getLookAngle().scale(0.3f));
            
            pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, user.getViewYRot(0)))
                .add(VectorHelper.getVectorForRotation(0, user.getViewYRot(0) + 90))
                .add(user.getLookAngle());
            
            slash.setPos(pos.x(), pos.y(), pos.z());
            slash.setOwner(user);
            slash.setRotationRoll(event.getRoll());
            slash.setYRot(user.getYRot());
            slash.setXRot(0);
            slash.setIsCritical(event.isCritical());
            slash.setDamage((Math.abs(event.getDamage())) * 10 + state.getRefine() / 2f);
            slash.setKnockBack(event.getKnockback());
            slash.setRank(user.getData(CapabilityConcentrationRank.RANK_POINT.get()).getRankLevel(level.getGameTime()));
            slash.setDamageSource(TEBladeDamageSources.witnessRainbow(level, slash, user, new Vec3(slash.getX(), slash.getEyeY(), slash.getZ())));
            slash.setRenderConsumerId(WITNESS_RAINBOW_RENDER_CONSUMER_ID);
            
            level.addFreshEntity(slash);
        }
    }
}
