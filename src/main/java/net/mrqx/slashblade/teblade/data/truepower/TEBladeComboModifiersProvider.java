package net.mrqx.slashblade.teblade.data.truepower;

import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.TimeValueHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.registry.combo.dreadstorm.DreadStormComboStateRegistry;
import net.mrqx.truepower.data.ComboModifierData;
import net.mrqx.truepower.data.combomodifier.TruePowerComboModifiers;
import net.mrqx.truepower.util.CollideAction;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TEBladeComboModifiersProvider extends DatapackBuiltinEntriesProvider {
    public TEBladeComboModifiersProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, new RegistrySetBuilder().add(ComboModifierData.MODIFIER_REGISTRY_KEY, TEBladeComboModifiersProvider::registerAll), Set.of(TEBlade.MODID));
    }
    
    @Override
    public String getName() {
        return "TEBlade Combo Modifiers Registry";
    }
    
    public static final List<TruePowerComboModifiers.EntryHolder> ENTRIES = new ArrayList<>();
    
    public static void registerAll(BootstrapContext<ComboModifierData.ComboModifierEntry> context) {
        for (TruePowerComboModifiers.EntryHolder holder : ENTRIES) {
            // TODO: fix in True POWER, maybe a Function<String, ResourceKey> ?
            context.register(ResourceKey.create(ComboModifierData.MODIFIER_REGISTRY_KEY, TEBlade.prefix(holder.key().location().getPath())), holder.entry());
        }
    }
    
    static {
        //<editor-fold desc="———————————————————— Dread Storm ————————————————————">
        
        //<editor-fold desc="———————————————————— Combo A ————————————————————">
        builder(DreadStormComboStateRegistry.COMBO_A4)
            .behavior(b -> b
                .cancel(12).jumpCancelOnly()
                .step(0, 2)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.COMBO_A4_END)
            .behavior(b -> b
                .cancel(0).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.COMBO_A4_END2)
            .behavior()
            .build(ENTRIES::add);
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Combo B ————————————————————">
        builder(DreadStormComboStateRegistry.COMBO_B2)
            .behavior(b -> b
                .jumpCancelOnly()
                .step(5, 0.5)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.COMBO_B2_END)
            .behavior(b -> b
                .cancel(0).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.COMBO_B2_END2)
            .behavior(b -> b
                .cancel(0).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.COMBO_B2_END3)
            .behavior()
            .build(ENTRIES::add);
        
        builder(DreadStormComboStateRegistry.COMBO_B3)
            .behavior(b -> b
                .jumpCancelOnly()
                .step(6, 1)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.COMBO_B3_END)
            .behavior(b -> b
                .cancel(0).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.COMBO_B3_END2)
            .behavior(b -> b
                .cancel(0).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.COMBO_B3_END3)
            .behavior()
            .build(ENTRIES::add);
        
        builder(DreadStormComboStateRegistry.COMBO_B4)
            .behavior(b -> b
                .tickShouldLockOn(4, 14, false)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.COMBO_B4_END)
            .behavior(b -> b
                .cancel(0).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.COMBO_B4_END2)
            .behavior()
            .build(ENTRIES::add);
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Aerial Rave B ————————————————————">
        builder(DreadStormComboStateRegistry.AERIAL_RAVE_B4)
            .behavior(b -> b
                .stun(6, 10)
                .stun(7, 10)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.AERIAL_RAVE_B4_LOOP)
            .behavior(b -> b
                .stun(0, 10)
                .stun(1, 10)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.AERIAL_RAVE_B4_LOOP2)
            .behavior(b -> b
                .stun(0, 10)
                .stun(1, 10)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.AERIAL_RAVE_B4_LOOP3)
            .behavior(b -> b
                .stun(0, 10)
                .stun(1, 10)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.AERIAL_RAVE_B4_END)
            .behavior()
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.AERIAL_RAVE_B4_END2)
            .behavior()
            .build(ENTRIES::add);
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Upper Slash ————————————————————">
        builder(DreadStormComboStateRegistry.UPPER_SLASH)
            .behavior(b -> b.stun((int) TimeValueHelper.getTicksFromFrames(7), 35, true))
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.UPPER_SLASH_END)
            .behavior()
            .build(ENTRIES::add);
        
        builder(DreadStormComboStateRegistry.UPPER_SLASH2)
            .behavior(b -> b.stun(0, 30, false))
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.UPPER_SLASH2_END)
            .behavior()
            .build(ENTRIES::add);
        
        builder(DreadStormComboStateRegistry.UPPER_SLASH_JUMP)
            .behavior(TruePowerComboModifiers.BehaviorBuilder::jumpVelocityBoost)
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.UPPER_SLASH_JUMP_END)
            .behavior()
            .build(ENTRIES::add);
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Aerial Cleave ————————————————————">
        builder(DreadStormComboStateRegistry.AERIAL_CLEAVE_SLASH)
            .behavior(b -> b
                .stun((int) TimeValueHelper.getTicksFromFrames(5), 15)
                .stun((int) TimeValueHelper.getTicksFromFrames(5) + 1, 15)
            )
            .build(ENTRIES::add);
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Rapid Slash ————————————————————">
        builder(DreadStormComboStateRegistry.RAPID_SLASH)
            .behavior(b -> b
                .clickRapidSlash()
                .tickCollideAction(0, 6, CollideAction.IGNORE)
                .tickShouldLockOn(2, 6, false)
                .tickSnapLockOn(0, 2)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.RAPID_SLASH_END)
            .behavior(TruePowerComboModifiers.BehaviorBuilder::clickSummonBlastSword)
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.RAPID_SLASH_QUICK)
            .behavior()
            .build(ENTRIES::add);
        
        builder(DreadStormComboStateRegistry.DIVING_STAR)
            .behavior(b -> b
                .tickSnapLockOn(0, 2)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.DIVING_STAR_LOOP)
            .behavior()
            .build(ENTRIES::add);
        //</editor-fold>
        
        //<editor-fold desc="———————————————————— Judgement Cut ————————————————————">
        builder(DreadStormComboStateRegistry.JUDGEMENT_CUT_STINGER)
            .behavior(b -> b
                .tickShouldLockOn(6, 16, false)
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.JUDGEMENT_CUT_STINGER_END)
            .behavior(b -> b
                .cancel(0).jumpCancelOnly()
            )
            .build(ENTRIES::add);
        builder(DreadStormComboStateRegistry.JUDGEMENT_CUT_STINGER_END2)
            .behavior()
            .build(ENTRIES::add);
        //</editor-fold>
        //</editor-fold>
    }
    
    
    public static TruePowerComboModifiers.ModifierBuilder builder(DeferredHolder<ComboState, ComboState> combo) {
        return TruePowerComboModifiers.builder(combo);
    }
}
