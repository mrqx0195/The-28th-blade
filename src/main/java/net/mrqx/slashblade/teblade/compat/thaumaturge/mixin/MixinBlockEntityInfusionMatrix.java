package net.mrqx.slashblade.teblade.compat.thaumaturge.mixin;

import com.leclowndu93150.thaumaturge.content.infusion.BlockEntityInfusionMatrix;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.compat.thaumaturge.event.BladeResearchGateHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityInfusionMatrix.class)
public abstract class MixinBlockEntityInfusionMatrix {
    @Inject(method = "awardCraft", at = @At("TAIL"), require = 1)
    private void teblade$creditCommittedResult(ServerPlayer player, ItemStack result, CallbackInfo callback) {
        if (BladeStateAccess.of(result).map(state -> ThaumaturgeBladeForms.PRIMORDIAL_CURSE
            .translationKey().equals(state.getTranslationKey())).orElse(false)) {
            BladeResearchGateHandler.creditPrimordialCurse(player);
        }
    }
}
