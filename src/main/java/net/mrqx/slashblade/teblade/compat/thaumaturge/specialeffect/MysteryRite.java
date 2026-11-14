package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeDataComponents;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeSpecialEffects;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SpecialEffectGates;

public final class MysteryRite extends SpecialEffect {
    private static final int MAX_STACKS = TEBladeThaumaturgeDataComponents.MAX_MYSTERY_RITE_STACKS;
    
    public MysteryRite() {
        super(20, false, false);
    }
    
    public static void onConfirmed(LivingEntity user, ISlashBladeState state, boolean perfect) {
        Sky.onConfirmedCast(user, perfect);
        if (!SpecialEffectGates.isActive(user, TEBladeThaumaturgeSpecialEffects.MYSTERY_RITE,
            ThaumaturgeBladeForms.Family.MYSTERY) || (user instanceof Player creativePlayer && creativePlayer.isCreative())) {
            return;
        }
        ItemStack stack = user.getMainHandItem();
        Settlement settlement = settle(read(stack), perfect);
        write(stack, settlement.stacks());
        if (settlement.waiveSoulCost()) {
            state.setProudSoulCount(state.getProudSoulCount() + state.getSlashArts().getProudSoulCost());
        }
    }
    
    public static int read(ItemStack stack) {
        Integer stacks = stack.get(TEBladeThaumaturgeDataComponents.MYSTERY_RITE_STACKS.get());
        return stacks == null ? 0 : stacks;
    }
    
    public static void write(ItemStack stack, int stacks) {
        if (stacks == 0) {
            stack.remove(TEBladeThaumaturgeDataComponents.MYSTERY_RITE_STACKS.get());
        } else {
            stack.set(TEBladeThaumaturgeDataComponents.MYSTERY_RITE_STACKS.get(), stacks);
        }
    }
    
    public record Settlement(int stacks, boolean waiveSoulCost) {
    }
    
    public static Settlement settle(int stacks, boolean perfect) {
        boolean waive = stacks >= MAX_STACKS;
        int remaining = waive ? 0 : stacks;
        if (perfect) {
            remaining = Math.min(MAX_STACKS, remaining + 1);
        }
        return new Settlement(remaining, waive);
    }
}
