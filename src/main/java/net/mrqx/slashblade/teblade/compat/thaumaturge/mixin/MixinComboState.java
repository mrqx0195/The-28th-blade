package net.mrqx.slashblade.teblade.compat.thaumaturge.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SlashArtCastTracker;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ComboState.class)
public abstract class MixinComboState {
    @WrapMethod(method = "tickAction", remap = false)
    private void teblade$attributeArtTick(LivingEntity user, Operation<Void> original) {
        SlashArtCastTracker.tickCombo(user, (ComboState) (Object) this, () -> original.call(user));
    }
}
