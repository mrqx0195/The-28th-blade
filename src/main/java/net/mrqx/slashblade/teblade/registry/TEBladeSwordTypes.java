package net.mrqx.slashblade.teblade.registry;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.SwordType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.BiFunction;

public final class TEBladeSwordTypes {
    public static final EnumProxy<SwordType> ANOMALY = buildEnumProxy();
    public static final EnumProxy<SwordType> TERMINAL = buildEnumProxy();
    
    public static EnumProxy<SwordType> buildEnumProxy() {
        return buildEnumProxy((state, item) -> false);
    }
    
    public static EnumProxy<SwordType> buildEnumProxy(BiFunction<Optional<ISlashBladeState>, ItemStack, Boolean> checkFunction) {
        return new EnumProxy<>(SwordType.class, checkFunction);
    }
    
    public static void appendTooltip(EnumSet<SwordType> swordType, MutableComponent component) {
        if (swordType.contains(ANOMALY.getValue())) {
            component.append(" ").append(Component.translatable("teblade.sword_type.anomaly").withColor(0x1D3467));
        }
        if (swordType.contains(TERMINAL.getValue())) {
            component.append(" ").append(Component.translatable("teblade.sword_type.terminal").withColor(0x6357CA));
        }
    }
}
