package net.mrqx.slashblade.teblade.event.craft;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.mrqx.slashblade.teblade.data.slashblade.TEBladeDefinitionsProvider;
import net.mrqx.slashblade.teblade.utils.ItemUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;

@EventBusSubscriber
public final class CraftEventHandler {
    public static final List<SoundEvent> ANVIL_SOUNDS = List.of(SoundEvents.ANVIL_PLACE, SoundEvents.ANVIL_LAND, SoundEvents.ANVIL_USE, SoundEvents.ANVIL_DESTROY);
    
    @SubscribeEvent
    public static void onItemCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack crafting = event.getCrafting();
        if (!level.isClientSide && ItemUtils.isBlade(TEBladeDefinitionsProvider.FRAGMENTED_SLASHBLADE, crafting)) {
            if (event.getInventory().hasAnyMatching(itemStack -> itemStack.is(Items.ANVIL))) {
                ItemStack anvil = Items.CHIPPED_ANVIL.getDefaultInstance();
                if (!player.getInventory().add(anvil)) {
                    player.drop(anvil, false);
                }
            } else if (event.getInventory().hasAnyMatching(itemStack -> itemStack.is(Items.CHIPPED_ANVIL))) {
                ItemStack anvil = Items.DAMAGED_ANVIL.getDefaultInstance();
                if (!player.getInventory().add(anvil)) {
                    player.drop(anvil, false);
                }
            }
            BladeStateAccess.of(crafting).ifPresent(state -> state.setProudSoulCount(state.getProudSoulCount() + 200));
            crafting.setDamageValue(crafting.getMaxDamage() / 2);
            level.playSound(null, new BlockPos(player.blockPosition()), ANVIL_SOUNDS.get(level.random.nextInt(ANVIL_SOUNDS.size())), player.getSoundSource(), 1, level.random.nextFloat() * 0.1F + 0.9F);
        }
    }
}
