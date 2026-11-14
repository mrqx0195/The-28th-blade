package net.mrqx.slashblade.teblade.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;
import net.mrqx.slashblade.teblade.registry.TEBladeSpecialEffectsRegistry;
import net.mrqx.slashblade.teblade.registry.combo.TEBladeComboStateRegistry;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(ItemSlashBlade.class)
public abstract class MixinItemSlashBlade extends SwordItem {
    public MixinItemSlashBlade(Tier tier, Properties properties, Tool toolComponentData) {
        super(tier, properties, toolComponentData);
    }

    @WrapMethod(method = "setDamage(Lnet/minecraft/world/item/ItemStack;I)V")
    private void wrapSetDamage(ItemStack stack, int damage, Operation<Void> original) {
        ISlashBladeState state = BladeStateAccess.of(stack).orElseThrow();
        int stateDamage = state.getDamage();
        if (state.isBroken() && state.hasSpecialEffect(TEBladeSpecialEffectsRegistry.WITNESS_RAINBOW.getId()) && damage > stateDamage) {
            damage = stateDamage - (damage - stateDamage);
        }
        original.call(stack, damage);
    }

    @WrapMethod(method = "damageItem(Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)I")
    private int onDamageItem(ItemStack stack, int amount, LivingEntity entity, Consumer<Item> onBroken, Operation<Integer> original) {
        return original.call(stack, amount, entity, onBroken.andThen(item -> {
            ISlashBladeState state = BladeStateAccess.of(stack).orElseThrow();
            if (!state.isBroken() && state.hasSpecialEffect(TEBladeSpecialEffectsRegistry.SUDDEN_RAIN.getId())) {
                state.updateComboSeq(entity, TEBladeComboStateRegistry.RAINSHOWER_CUT_END.getId());
            }
        }));
    }
}
