package net.mrqx.slashblade.teblade.compat.thaumaturge.registry;

import net.minecraft.world.phys.Vec3;
import net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect.Bloodthirst;
import net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect.TideStacks;
import net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect.VoidStep;
import net.mrqx.slashblade.teblade.compat.thaumaturge.utils.SlashArtCastTracker;
import net.mrqx.slashblade.teblade.registry.TEBladeAttachmentTypesRegistry;
import net.mrqx.slashblade.teblade.utils.BladeHealingBudget;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.Optional;
import java.util.function.Supplier;

public final class TEBladeThaumaturgeAttachmentTypes {
    public static final Supplier<AttachmentType<Integer>> SKY_WINDOW = typed("sky_window", () -> 0);
    public static final Supplier<AttachmentType<VoidStep.Window>> VOID_STEP_WINDOW =
        typed("void_step_window", () -> new VoidStep.Window(0, 0));
    public static final Supplier<AttachmentType<TideStacks>> TIDE_STACKS = typed("tide_stacks", TideStacks::new);
    public static final Supplier<AttachmentType<Optional<Bloodthirst.Snapshot>>> BLOODTHIRST_SNAPSHOT =
        typed("bloodthirst_snapshot", Optional::empty);
    public static final Supplier<AttachmentType<BladeHealingBudget>> BLOODTHIRST_BUDGET = typed("bloodthirst_budget",
        () -> new BladeHealingBudget(Bloodthirst.BUDGET_WINDOW_TICKS, Bloodthirst.BUDGET_MAXIMUM));
    public static final Supplier<AttachmentType<Optional<SlashArtCastTracker.Cast>>> SLASH_ART_CAST =
        typed("slash_art_cast", Optional::empty);
    public static final Supplier<AttachmentType<Boolean>> PRIMORDIAL_CRAFTED = typed("primordial_crafted", () -> false);
    public static final Supplier<AttachmentType<Integer>> PRIMORDIAL_CAST_REMAINDER =
        typed("primordial_cast_remainder", () -> 0);
    public static final Supplier<AttachmentType<Optional<Vec3>>> FLAME_CAST_CENTER =
        typed("flame_cast_center", Optional::empty);
    
    private static <T> Supplier<AttachmentType<T>> typed(String name, Supplier<T> defaultValue) {
        return TEBladeAttachmentTypesRegistry.ATTACHMENT_TYPES.register(name,
            () -> AttachmentType.builder(defaultValue).build());
    }
    
    public static void init() {
    }
}
