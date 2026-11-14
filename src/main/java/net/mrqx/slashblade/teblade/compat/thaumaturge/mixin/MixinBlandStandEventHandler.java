package net.mrqx.slashblade.teblade.compat.thaumaturge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.event.bladestand.BlandStandEventHandler;
import net.minecraft.resources.ResourceLocation;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ExternalBladeEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlandStandEventHandler.class)
public abstract class MixinBlandStandEventHandler {
    @WrapOperation(method = "eventChangeSE",
        at = @At(value = "INVOKE",
            target = "Lmods/flammpfeil/slashblade/capability/slashblade/ISlashBladeState;addSpecialEffect(Lnet/minecraft/resources/ResourceLocation;)Z"),
        remap = false)
    private static boolean teblade$recordInstalledEffect(ISlashBladeState state, ResourceLocation effect,
        Operation<Boolean> original, @Local(argsOnly = true) SlashBladeEvent.BladeStandAttackEvent event) {
        boolean supported = ThaumaturgeBladeForms.byTranslationKey(state.getTranslationKey()) != null;
        if (!supported || effect.equals(TEBladeThaumaturgeSpecialEffects.BLOODTHIRST.getId())
            || effect.equals(TEBladeThaumaturgeSpecialEffects.MYSTERY_RITE.getId())) {
            return original.call(state, effect);
        }

        boolean installed = original.call(state, effect);
        if (installed) {
            ExternalBladeEffects.edit(event.getBlade(), effects -> effects.add(effect));
        }
        return installed;
    }
}
