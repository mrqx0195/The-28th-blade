package net.mrqx.slashblade.teblade.compat.thaumaturge.mixin;

import com.leclowndu93150.thaumaturge.content.equipment.InfusionEnchantmentEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.player.Player;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InfusionEnchantmentEvents.class)
public abstract class MixinInfusionEnchantmentEvents {
    @WrapOperation(method = "onAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSources;playerAttack(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/damagesource/DamageSource;"))
    private static DamageSource teblade$isolateArcing(DamageSources sources, Player player, Operation<DamageSource> original) {
        boolean supported = BladeStateAccess.of(player.getMainHandItem())
            .map(state -> ThaumaturgeBladeForms.byTranslationKey(state.getTranslationKey()) != null)
            .orElse(false);
        
        return supported ? sources.source(TEBladeDamageTypesBootstrap.SECONDARY_THUNDER) : original.call(sources, player);
    }
}
