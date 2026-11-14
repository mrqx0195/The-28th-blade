package net.mrqx.slashblade.teblade.registry;

import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.registry.combo.TEBladeComboStateRegistry;
import net.mrqx.slashblade.teblade.registry.combo.dreadstorm.DreadStormComboStateRegistry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TEBladeSlashArtsRegistry {
    public static final DeferredRegister<SlashArts> SLASH_ARTS = DeferredRegister.create(SlashArts.REGISTRY_KEY, TEBlade.MODID);
    
    public static final DeferredHolder<SlashArts, SlashArts> RAINSHOWER = SLASH_ARTS.register("rainshower",
        () -> new SlashArts(e -> e.onGround()
            ? TEBladeComboStateRegistry.RAINSHOWER_CUT.getId()
            : TEBladeComboStateRegistry.RAINSHOWER_CUT_SLASH_AIR.getId()
        )
            .setComboStateJust(e -> TEBladeComboStateRegistry.RAINSHOWER_CUT_SLASH_JUST.getId())
            .setComboStateSuper(e -> TEBladeComboStateRegistry.RAINSHOWER_CUT_END.getId())
    );
    public static final DeferredHolder<SlashArts, SlashArts> JUDGEMENT_CUT_DREAD_STORM = SLASH_ARTS.register("judgement_cut_dread_storm",
        () -> new SlashArts(e -> e.onGround()
            ? DreadStormComboStateRegistry.JUDGEMENT_CUT.getId()
            : DreadStormComboStateRegistry.JUDGEMENT_CUT_SLASH_AIR.getId()
        )
            .setComboStateJust(e -> DreadStormComboStateRegistry.JUDGEMENT_CUT_SLASH_JUST.getId())
            // TODO: True JCE
            .setComboStateSuper(e -> ComboStateRegistry.JUDGEMENT_CUT_END.getId())
    );
}
