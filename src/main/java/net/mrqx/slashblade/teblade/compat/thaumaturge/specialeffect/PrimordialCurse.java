package net.mrqx.slashblade.teblade.compat.thaumaturge.specialeffect;

import com.leclowndu93150.thaumaturge.api.warp.IPlayerWarp;
import com.leclowndu93150.thaumaturge.api.warp.WarpHelper;
import com.leclowndu93150.thaumaturge.api.warp.WarpType;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.entity.IShootable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.mrqx.slashblade.teblade.compat.thaumaturge.blade.ThaumaturgeBladeForms;
import net.mrqx.slashblade.teblade.compat.thaumaturge.registry.TEBladeThaumaturgeAttachmentTypes;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Arrays;
import java.util.Objects;

public final class PrimordialCurse {
    private static final int CASTS_PER_WARP = 3;
    private static final int WARP_CAST_INDEX = 2;
    private static final int WARP_PER_CYCLE = 1;
    private static final long MAX_COUNTED_WARP = 50L;
    private static final float WARP_DAMAGE_BONUS = 0.02F;
    
    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Pre event) {
        DamageSource source = event.getSource();
        if (source.is(TEBladeDamageTypesBootstrap.SECONDARY) || !(source.getEntity() instanceof ServerPlayer player)
            || !player.isAlive()) {
            return;
        }
        ThaumaturgeBladeForms.Form form = BladeStateAccess.of(player.getMainHandItem())
            .map(state -> ThaumaturgeBladeForms.byTranslationKey(state.getTranslationKey())).orElse(null);
        if (!ThaumaturgeBladeForms.PRIMORDIAL_CURSE.equals(form)) {
            return;
        }
        Entity direct = source.getDirectEntity();
        boolean melee = source.is(DamageTypes.PLAYER_ATTACK) && Objects.equals(direct, player);
        boolean slash = (direct instanceof EntitySlashEffect || direct instanceof EntityJudgementCut
            || direct instanceof EntityDrive) && Objects.equals(((IShootable) direct).getShooter(), player);
        float damage = event.getNewDamage();
        if (!(melee || slash)) {
            return;
        }
        IPlayerWarp warp = WarpHelper.getWarp(player);
        long total = Arrays.stream(WarpType.values()).mapToLong(warp::get).sum();
        event.setNewDamage(damage * (1 + Math.min(MAX_COUNTED_WARP, total) * WARP_DAMAGE_BONUS));
    }
    
    public static void recordCast(ServerPlayer user, ISlashBladeState state) {
        if (!ThaumaturgeBladeForms.PRIMORDIAL_CURSE.equals(ThaumaturgeBladeForms.byTranslationKey(state.getTranslationKey()))) {
            return;
        }
        int remainder = user.getData(TEBladeThaumaturgeAttachmentTypes.PRIMORDIAL_CAST_REMAINDER);
        if (remainder == WARP_CAST_INDEX) {
            WarpHelper.addWarp(user, WARP_PER_CYCLE, WarpType.TEMPORARY);
        }
        user.setData(TEBladeThaumaturgeAttachmentTypes.PRIMORDIAL_CAST_REMAINDER, (remainder + 1) % CASTS_PER_WARP);
    }
}