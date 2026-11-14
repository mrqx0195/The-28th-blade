package net.mrqx.slashblade.teblade.registry.combo.dreadstorm;

import mods.flammpfeil.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil.slashblade.capability.inputstate.IInputState;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.sbr_core.utils.InputStream;
import net.mrqx.sbr_core.utils.JustSlashArtManager;
import net.mrqx.truepower.combo.ComboCommandsHandler;
import net.mrqx.truepower.config.TruePowerCommonConfig;

import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;

/**
 * @see ComboCommandsHandler
 */
@SuppressWarnings("unused")
public final class DreadStormComboCommands {
    public static ResourceLocation processComboCommand(LivingEntity livingEntity) {
        IInputState inputState = livingEntity.getData(CapabilityInputState.INPUT_STATE.get());
        EnumSet<InputCommand> commands = inputState.getCommands(livingEntity);
        ResourceLocation defaultCombo = defaultCombo(livingEntity, commands);
        if (BladeStateAccess.of(livingEntity.getMainHandItem()).isEmpty()
            || livingEntity.level().isClientSide
            || (TruePowerCommonConfig.BLADE_ARTS_NEED_SHIFT.get() && !commands.contains(InputCommand.SNEAK))) {
            return defaultCombo;
        }
        ResourceLocation id;
        InputStream inputStream = InputStream.getOrCreateInputStream(livingEntity);
        id = DreadStormInputTimeLines.INPUTS.entrySet().stream()
            .filter(entry -> inputStream.checkTimeLineInput(entry.getKey()))
            .findFirst()
            .map(Map.Entry::getValue)
            .orElse(defaultCombo);
        if (!Objects.equals(id, defaultCombo)) {
            JustSlashArtManager.resetJustCount(livingEntity);
        } else if (inputStream.checkInputWithTimeLineKeyInput(ComboCommandsHandler.SUPER_SLASH_ARTS_SPRINT_INPUT_KEY)) {
            ResourceLocation ssa = ComboCommandsHandler.tryReleaseSSA(livingEntity);
            if (ssa != null && !ssa.equals(ComboStateRegistry.NONE.getId())) {
                ComboCommandsHandler.prepareDoSSA(livingEntity);
                id = ssa;
            } else {
                id = ComboStateRegistry.NONE.getId();
            }
        }
        return id;
    }
    
    public static ResourceLocation defaultCombo(LivingEntity livingEntity, EnumSet<InputCommand> commands) {
        return livingEntity.onGround()
            ? defaultGroundCombo(livingEntity, commands)
            : defaultAerialCombo(livingEntity, commands);
    }
    
    public static ResourceLocation defaultGroundCombo(LivingEntity livingEntity, EnumSet<InputCommand> commands) {
        if (commands.contains(InputCommand.FORWARD)) {
            return DreadStormComboStateRegistry.RAPID_SLASH.getId();
        }
        if (commands.contains(InputCommand.BACK)) {
            return DreadStormComboStateRegistry.UPPER_SLASH.getId();
        }
        return DreadStormComboStateRegistry.COMBO_A1.getId();
    }
    
    public static ResourceLocation defaultAerialCombo(LivingEntity livingEntity, EnumSet<InputCommand> commands) {
        if (commands.contains(InputCommand.FORWARD)) {
            return DreadStormComboStateRegistry.RAPID_SLASH.getId();
        }
        if (commands.contains(InputCommand.BACK)) {
            return DreadStormComboStateRegistry.AERIAL_CLEAVE.getId();
        }
        return DreadStormComboStateRegistry.AERIAL_RAVE_A1.getId();
    }
}
