package net.mrqx.slashblade.teblade.compat.thaumaturge.mixin;

import com.leclowndu93150.thaumaturge.api.research.scan.ScannedEntity;
import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScannedEntity.class)
public abstract class MixinScannedEntity {
    @Final
    @Shadow
    private Entity entity;
    
    @Inject(method = "carriedStack", at = @At("RETURN"), remap = false, cancellable = true)
    private void teblade$carriedStack(CallbackInfoReturnable<ItemStack> cir) {
        if (!(entity instanceof BladeStandEntity bladeStand) || cir.getReturnValue() != null) {
            return;
        }
        ItemStack item = bladeStand.getItem();
        if (item.isEmpty()) {
            cir.setReturnValue(item.copy());
            return;
        }
        cir.setReturnValue((bladeStand.currentType == null
            ? SlashBladeItems.BLADESTAND_1.get()
            : bladeStand.currentType).getDefaultInstance());
    }
}
