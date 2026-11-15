package net.mrqx.slashblade.teblade.utils;

import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.entity.IShootable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Objects;

public final class SlashEntities {
    public static boolean isOwnedBy(@Nullable Entity entity, LivingEntity owner) {
        if ((!(entity instanceof EntitySlashEffect) && !(entity instanceof EntityJudgementCut)
            && !(entity instanceof EntityDrive))) {
            return false;
        }
        IShootable shootable = (IShootable) entity;
        return Objects.equals(shootable.getShooter(), owner);
    }
}