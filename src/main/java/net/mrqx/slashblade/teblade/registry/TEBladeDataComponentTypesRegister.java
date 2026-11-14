package net.mrqx.slashblade.teblade.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.mrqx.slashblade.teblade.TEBlade;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TEBladeDataComponentTypesRegister {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TEBlade.MODID);
}
