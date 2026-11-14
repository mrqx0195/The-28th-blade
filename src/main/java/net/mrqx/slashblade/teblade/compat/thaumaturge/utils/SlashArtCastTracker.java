package net.mrqx.slashblade.teblade.compat.thaumaturge.utils;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.entity.IShootable;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeAttachmentTypes;
import net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect.MysteryRite;
import net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect.PrimordialCurse;
import net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect.Tide;
import net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect.VoidStep;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;

public final class SlashArtCastTracker {
    private static final AtomicLong NEXT_CAST_ID = new AtomicLong(1L);
    
    public enum CastPhase {
        CHARGING,
        CONFIRMED
    }
    
    public record Cast(UUID owner, long id, @Nullable ResourceLocation combo, ItemStack blade, CastPhase phase,
                       @Nullable SlashArts.ArtsType artType, Set<UUID> pendingHits) {
        public Cast {
            blade = blade.copy();
            pendingHits = Set.copyOf(pendingHits);
        }
        
        public Cast copy() {
            return new Cast(owner, id, combo, blade, phase, artType, pendingHits);
        }
        
        public Cast withArtType(@Nullable SlashArts.ArtsType newArtType) {
            return new Cast(owner, id, combo, blade, phase, newArtType, pendingHits);
        }
        
        public Cast withPendingTarget(UUID target) {
            Set<UUID> targets = new HashSet<>(pendingHits);
            targets.add(target);
            return new Cast(owner, id, combo, blade, phase, artType, targets);
        }
    }
    
    @Nullable
    public static Cast begin(LivingEntity user, ItemStack stack) {
        if (user.level().isClientSide()) {
            return null;
        }
        Cast cast = new Cast(user.getUUID(), NEXT_CAST_ID.getAndIncrement(), null, stack, CastPhase.CHARGING, null,
            Set.of());
        user.setData(TEBladeThaumaturgeAttachmentTypes.SLASH_ART_CAST, Optional.of(cast));
        return cast;
    }
    
    public static void end(LivingEntity user, @Nullable Cast scope) {
        if (scope == null) {
            return;
        }
        Cast current = castOf(user);
        if (current != null && current.id() == scope.id() && current.phase() == CastPhase.CHARGING) {
            user.setData(TEBladeThaumaturgeAttachmentTypes.SLASH_ART_CAST, Optional.empty());
        }
    }
    
    public static void recordArt(LivingEntity user, @Nullable SlashArts.ArtsType artType) {
        if (artType == null || artType == SlashArts.ArtsType.Super) {
            return;
        }
        Cast cast = castOf(user);
        if (cast == null || cast.phase() != CastPhase.CHARGING) {
            return;
        }
        user.setData(TEBladeThaumaturgeAttachmentTypes.SLASH_ART_CAST, Optional.of(cast.withArtType(artType)));
    }
    
    public static void performCharge(LivingEntity user, ISlashBladeState state, ResourceLocation combo,
                                     Runnable action, BooleanSupplier success) {
        if (user.level().isClientSide() || !user.isAlive() || BladeStateAccess.of(user.getMainHandItem()).isEmpty()) {
            action.run();
            return;
        }
        Cast scope = castOf(user);
        long castId = scope != null && scope.phase() == CastPhase.CHARGING ? scope.id() : NEXT_CAST_ID.getAndIncrement();
        action.run();
        scope = castOf(user);
        if (!success.getAsBoolean()) {
            end(user, scope);
            return;
        }
        Cast confirmed = new Cast(user.getUUID(), castId, combo, user.getMainHandItem(), CastPhase.CONFIRMED,
            scope == null ? null : scope.artType(), Set.of());
        user.setData(TEBladeThaumaturgeAttachmentTypes.SLASH_ART_CAST, Optional.of(confirmed));
        if (user instanceof ServerPlayer serverPlayer) {
            PrimordialCurse.recordCast(serverPlayer, state);
        }
        MysteryRite.onConfirmed(user, state, confirmed.artType() == SlashArts.ArtsType.Jackpot);
        if (scope != null && !scope.pendingHits().isEmpty()) {
            VoidStep.onConfirmedHit(user);
            for (UUID target : scope.pendingHits()) {
                hitTarget(user, target, confirmed.id());
            }
        }
    }
    
    public static void tickCombo(LivingEntity user, ComboState combo, Runnable action) {
        Cast cast = castOf(user);
        if (cast != null && !stillCasting(user, cast, combo)) {
            user.setData(TEBladeThaumaturgeAttachmentTypes.SLASH_ART_CAST, Optional.empty());
        }
        action.run();
    }
    
    private static boolean stillCasting(LivingEntity user, Cast cast, ComboState combo) {
        ISlashBladeState state = BladeStateAccess.of(user.getMainHandItem()).orElse(null);
        return state != null && cast.combo() != null && cast.combo().equals(state.getComboSeq())
            && ComboStateRegistry.REGISTRY.get(cast.combo()) == combo;
    }
    
    @Nullable
    public static Cast attributedCast(DamageSource source) {
        Entity direct = source.getDirectEntity();
        if (direct == null || direct.equals(source.getEntity())) {
            return null;
        }
        return castOf(direct);
    }
    
    @SubscribeEvent
    public static void onSpawn(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof IShootable shot)) {
            return;
        }
        Entity shooter = shot.getShooter();
        if (shooter == null) {
            return;
        }
        Cast cast = castOf(shooter);
        if (cast != null) {
            event.getEntity().setData(TEBladeThaumaturgeAttachmentTypes.SLASH_ART_CAST, Optional.of(cast.copy()));
        }
    }
    
    @SubscribeEvent
    public static void onHit(LivingDamageEvent.Post event) {
        if (event.getSource().is(TEBladeDamageTypesBootstrap.SECONDARY)
            || !(event.getSource().getEntity() instanceof LivingEntity user)
            || event.getEntity().equals(user)) {
            return;
        }
        Cast cast = attributedCast(event.getSource());
        if (cast == null || !cast.owner().equals(user.getUUID()) || !user.isAlive()
            || !ItemStack.isSameItem(cast.blade(), user.getMainHandItem())) {
            return;
        }
        Cast authoritative = castOf(user);
        if (authoritative != null && authoritative.id() == cast.id()) {
            cast = authoritative;
        }
        if (cast.phase() == CastPhase.CONFIRMED) {
            VoidStep.onConfirmedHit(user);
            Tide.onCastHit(user, event.getEntity(), cast.id());
            return;
        }
        if (authoritative != null && authoritative.id() == cast.id() && authoritative.phase() == CastPhase.CHARGING) {
            user.setData(TEBladeThaumaturgeAttachmentTypes.SLASH_ART_CAST,
                Optional.of(authoritative.withPendingTarget(event.getEntity().getUUID())));
        }
    }
    
    private static void hitTarget(LivingEntity user, UUID target, long castId) {
        if (user.level() instanceof ServerLevel level && level.getEntity(target) instanceof LivingEntity living) {
            Tide.onCastHit(user, living, castId);
        }
    }
    
    @Nullable
    private static Cast castOf(Entity entity) {
        return entity.getData(TEBladeThaumaturgeAttachmentTypes.SLASH_ART_CAST).orElse(null);
    }
}
