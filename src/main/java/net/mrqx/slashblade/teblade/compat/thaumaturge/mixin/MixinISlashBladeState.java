package net.mrqx.slashblade.teblade.compat.thaumaturge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SlashArtCastTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ISlashBladeState.class)
public interface MixinISlashBladeState {
    @WrapOperation(method = "doChargeAction", at = @At(value = "INVOKE", target = "Lmods/flammpfeil/slashblade/capability/slashblade/ISlashBladeState;updateComboSeq(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/resources/ResourceLocation;)V"), remap = false)
    default void teblade$confirmCharge(ISlashBladeState state, LivingEntity user, ResourceLocation combo, Operation<Void> original) {
        SlashArtCastTracker.performCharge(user, state, combo, () -> original.call(state, user, combo),
            () -> combo.equals(state.getComboSeq()));
    }
}
