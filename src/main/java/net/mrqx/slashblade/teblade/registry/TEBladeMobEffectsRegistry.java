package net.mrqx.slashblade.teblade.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.mrqx.slashblade.teblade.TEBlade;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TEBladeMobEffectsRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, TEBlade.MODID);
}
