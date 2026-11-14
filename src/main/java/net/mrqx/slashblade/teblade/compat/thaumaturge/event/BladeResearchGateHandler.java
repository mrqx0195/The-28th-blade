package net.mrqx.slashblade.teblade.compat.thaumaturge.event;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.research.ResearchEvent;
import com.leclowndu93150.thaumaturge.api.research.ResearchUnlockConditions;
import com.leclowndu93150.thaumaturge.api.research.pool.AspectPoolAccess;
import com.leclowndu93150.thaumaturge.content.research.ResearchManager;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mrqx.slashblade.teblade.compat.thaumaturge.TEBladeResearchKeys;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeAttachmentTypes;
import net.mrqx.slashblade.teblade.data.thaumaturge.aspect.TEBladeAspects;
import net.mrqx.slashblade.teblade.data.thaumaturge.research.BladeResearchEntries;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public final class BladeResearchGateHandler {
    private static final ResourceLocation PRIMORDIAL_CURSE = ThaumaturgeBladeForms.PRIMORDIAL_CURSE.id();
    private static final int PRIMORDIAL_CURSE_STAGE = 1;
    
    private static final List<ResourceLocation> OBSERVED = List.of(
        BladeResearchEntries.UNLOCK,
        BladeResearchEntries.BLADE_RODAI,
        BladeResearchEntries.family(ThaumaturgeBladeForms.Family.MYSTERY),
        BladeResearchEntries.family(ThaumaturgeBladeForms.Family.VOID));
    
    private static final Map<ResourceLocation, List<ResourceKey<IAspect>>> GRANTS = Map.of(
        BladeResearchEntries.UNLOCK, List.of(TEBladeAspects.SLASH_BLADE),
        BladeResearchEntries.BLADE_RODAI, List.of(TEBladeAspects.PROUD_SOUL),
        BladeResearchEntries.family(ThaumaturgeBladeForms.Family.MYSTERY), List.of(TEBladeAspects.AKURYO),
        BladeResearchEntries.SIX_BREATHS, List.of(TEBladeAspects.RAIN, TEBladeAspects.TEMPEST, TEBladeAspects.STASIS));
    
    private BladeResearchGateHandler() {
    }
    
    public static void registerUnlockConditions() {
        Component locked = Component.translatable(TEBladeResearchKeys.observation("locked"));
        for (ResourceLocation entry : OBSERVED) {
            ResearchUnlockConditions.require(entry, (player, research) -> meets(player, entry), locked);
        }
    }
    
    public static List<BladeObservation.Condition> requirements(Player player, ResourceLocation id) {
        return player.registryAccess().registry(BladeObservation.REGISTRY_KEY)
            .map(registry -> registry.get(id)).map(BladeObservation::conditions).orElse(List.of());
    }
    
    public static void creditPrimordialCurse(ServerPlayer player) {
        player.setData(TEBladeThaumaturgeAttachmentTypes.PRIMORDIAL_CRAFTED, true);
        if (ResearchManager.of(player).stageOf(PRIMORDIAL_CURSE) == PRIMORDIAL_CURSE_STAGE) {
            ResearchManager.advanceStage(player, PRIMORDIAL_CURSE);
        }
    }
    
    private static boolean meets(Player player, ResourceLocation id) {
        List<BladeObservation.Condition> conditions = requirements(player, id);
        for (BladeObservation.Condition condition : conditions) {
            long count = IntStream.range(0, player.getInventory().getContainerSize()).mapToObj(slot -> player.getInventory().getItem(slot)).filter(condition::matches).mapToLong(ItemStack::getCount).sum();
            if (count < condition.count()) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean crafted(Player player) {
        return player.getData(TEBladeThaumaturgeAttachmentTypes.PRIMORDIAL_CRAFTED);
    }
    
    @SubscribeEvent
    public static void onAdvance(ResearchEvent.StageAdvanced event) {
        if (!PRIMORDIAL_CURSE.equals(event.research())) {
            return;
        }
        if (event.previousStage() == PRIMORDIAL_CURSE_STAGE && !crafted(event.player())) {
            event.setCanceled(true);
            event.player().displayClientMessage(
                Component.translatable(TEBladeResearchKeys.note(PRIMORDIAL_CURSE, "requires_craft")), false);
        }
    }
    
    @SubscribeEvent
    public static void onCompleted(ResearchEvent.Completed event) {
        if (!(event.player() instanceof ServerPlayer player)) {
            return;
        }
        List<ResourceKey<IAspect>> grants = GRANTS.get(event.research());
        if (grants == null) {
            return;
        }
        Registry<IAspect> registry = player.registryAccess().registryOrThrow(IAspect.REGISTRY_KEY);
        for (ResourceKey<IAspect> key : grants) {
            registry.getHolder(key).ifPresent(holder -> AspectPoolAccess.grant(player, holder, 1));
        }
    }
}
