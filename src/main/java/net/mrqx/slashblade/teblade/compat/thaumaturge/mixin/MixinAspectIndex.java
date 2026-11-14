package net.mrqx.slashblade.teblade.compat.thaumaturge.mixin;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.aspect.AspectIndex;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.mrqx.slashblade.teblade.data.thaumaturge.aspect.TEBladeAspects;

@Mixin(AspectIndex.class)
public abstract class MixinAspectIndex {
    @Inject(method = "of(Lnet/minecraft/world/item/ItemStack;)Lcom/leclowndu93150/thaumaturge/api/aspect/AspectList;",
        at = @At("RETURN"), remap = false, cancellable = true)
    private void teblade$addSlashBladeAspects(ItemStack stack, CallbackInfoReturnable<AspectList> cir) {
        if (stack.isEmpty()) {
            return;
        }
        BladeStateAccess.of(stack).ifPresent(state -> {
            AspectList list = cir.getReturnValue();
            list = teblade$scaled(list, TEBladeAspects.AKURYO, state.getKillCount(), 100, 100);
            list = teblade$scaled(list, TEBladeAspects.PROUD_SOUL, state.getProudSoulCount(), 100, 100);
            cir.setReturnValue(teblade$scaled(list, TEBladeAspects.SLASH_BLADE, 10 + state.getRefine(), 0, 1));
        });
    }
    
    @Unique
    private static AspectList teblade$scaled(AspectList list, ResourceKey<IAspect> aspect, int value,
                                             int minimum, int divisor) {
        Holder<IAspect> holder = list.entries().stream()
            .map(AspectInstance::aspect)
            .filter(candidate -> candidate.is(aspect))
            .findFirst()
            .orElse(null);
        if (holder == null) {
            return list;
        }
        AspectList remaining = list.without(holder);
        return value >= minimum ? remaining.add(holder, value / divisor) : remaining;
    }
}
