package net.mrqx.slashblade.teblade.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.item.SwordType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.mrqx.slashblade.teblade.registry.TEBladeSwordTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.EnumSet;
import java.util.List;

@Mixin(ItemSlashBlade.class)
public abstract class MixinItemSlashBladeClient extends SwordItem {
    protected MixinItemSlashBladeClient(Tier tier, Properties properties) {
        super(tier, properties);
    }
    
    @WrapOperation(method = "appendSwordType(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), remap = false)
    private boolean appendExtraSwordTypeTooltip(List<Component> instance, Object element, Operation<Boolean> original, @Local(name = "swordType") EnumSet<SwordType> swordType) {
        if (element instanceof MutableComponent component) {
            TEBladeSwordTypes.appendTooltip(swordType, component);
        }
        return original.call(instance, element);
    }
}
