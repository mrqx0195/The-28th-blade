package net.mrqx.slashblade.teblade.compat.thaumaturge.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SlashArtCastTracker;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SlashArts.class)
public abstract class MixinSlashArts {
    @WrapMethod(method = "doArts", remap = false)
    private ResourceLocation teblade$recordArt(SlashArts.ArtsType type, LivingEntity user, Operation<ResourceLocation> original) {
        ResourceLocation resolved = original.call(type, user);
        SlashArtCastTracker.recordArt(user, type);
        return resolved;
    }
}
