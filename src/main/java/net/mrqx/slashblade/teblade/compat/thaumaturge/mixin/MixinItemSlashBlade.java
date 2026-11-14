package net.mrqx.slashblade.teblade.compat.thaumaturge.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SlashArtCastTracker;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemSlashBlade.class)
public abstract class MixinItemSlashBlade {
    @WrapMethod(method = "releaseUsing")
    private void teblade$castScope(ItemStack stack, Level level, LivingEntity user, int timeLeft, Operation<Void> original) {
        SlashArtCastTracker.Cast scope = SlashArtCastTracker.begin(user, stack);
        try {
            original.call(stack, level, user, timeLeft);
        } finally {
            SlashArtCastTracker.end(user, scope);
        }
    }
}
