package net.mrqx.slashblade.teblade.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.mrqx.slashblade.teblade.TEBlade;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TEBladeEntityTypesRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, TEBlade.MODID);
}
