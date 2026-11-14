package net.mrqx.slashblade.teblade.specialeffect;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mrqx.slashblade.teblade.registry.TEBladeSpecialEffectsRegistry;
import net.mrqx.slashblade.teblade.utils.AttackUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

@EventBusSubscriber
public final class Automatic extends SpecialEffect {
    public Automatic() {
        super(128, true, true);
    }
    
    @SubscribeEvent
    public static void onTick(SlashBladeEvent.UpdateEvent event) {
        if (event.isSelected() && event.getEntity() instanceof LivingEntity entity && entity.isUsingItem()) {
            AttackUtils.checkAndPerformSpecialEffect(entity, event.getSlashBladeState(), TEBladeSpecialEffectsRegistry.AUTOMATIC, (livingEntity, state) -> {
                ItemStack blade = event.getBlade();
                int elapsed = blade.getUseDuration(livingEntity) - livingEntity.getUseItemRemainingTicks();
                SlashArts.ArtsType chargeAction = doFakeChargeAction(state, livingEntity, elapsed);
                if (chargeAction == SlashArts.ArtsType.Jackpot) {
                    livingEntity.releaseUsingItem();
                }
            });
        }
    }
    
    /**
     * @see ISlashBladeState#doChargeAction(LivingEntity, int)
     */
    @Nullable
    public static SlashArts.ArtsType doFakeChargeAction(ISlashBladeState state, LivingEntity user, int elapsed) {
        if (elapsed <= 2 || !SwordType.from(user.getMainHandItem()).contains(SwordType.ENCHANTED) || state.isBroken() || state.isSealed()) {
            return SlashArts.ArtsType.Fail;
        }
        
        Map.Entry<Integer, ResourceLocation> currentloc = state.resolvCurrentComboStateTicks(user);
        
        ComboState current = ComboStateRegistry.REGISTRY.get(currentloc.getValue());
        if (current == null) {
            return SlashArts.ArtsType.Fail;
        }
        
        if (!ComboStateRegistry.NONE.getId().equals(currentloc.getValue())
            && Objects.equals(current.getNext(user), currentloc.getValue())) {
            return SlashArts.ArtsType.Fail;
        }
        
        int fullChargeTicks = state.getFullChargeTicks(user);
        int justReceptionSpan = SlashArts.getJustReceptionSpan(user);
        int justChargePeriod = fullChargeTicks + justReceptionSpan;
        
        RangeMap<Integer, SlashArts.ArtsType> charge_accept = ImmutableRangeMap.<Integer, SlashArts.ArtsType>builder()
            .put(Range.lessThan(fullChargeTicks), SlashArts.ArtsType.Fail)
            .put(Range.closedOpen(fullChargeTicks, justChargePeriod), SlashArts.ArtsType.Jackpot)
            .put(Range.atLeast(justChargePeriod), SlashArts.ArtsType.Success).build();
        
        SlashArts.ArtsType type = charge_accept.get(elapsed);
        
        if (type != SlashArts.ArtsType.Jackpot) {
            SlashArts.ArtsType result = current.releaseAction(user, currentloc.getKey());
            
            if (result != SlashArts.ArtsType.Fail) {
                type = result;
            }
        }
        
        if (type != null) {
            SlashArts slashArts = state.getSlashArts();
            ResourceLocation csloc = switch (type) {
                case Jackpot -> slashArts.getComboStateJust(user);
                case Success -> slashArts.getComboState(user);
                case Super -> slashArts.getComboStateSuper().apply(user);
                default -> ComboStateRegistry.NONE.getId();
            };
            ComboState cs = ComboStateRegistry.REGISTRY.get(csloc);
            if (!ComboStateRegistry.NONE.getId().equals(csloc)) {
                if (!currentloc.getValue().equals(csloc) && cs != null) {
                    if (current.getPriority() > cs.getPriority()) {
                        return type;
                    }
                }
            }
        }
        
        return type;
    }
}
