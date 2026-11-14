package net.mrqx.slashblade.teblade.data.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.mrqx.slashblade.teblade.TEBlade;
import net.mrqx.slashblade.teblade.data.TEBladeDamageTypesBootstrap;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public final class TEBladeDamageTypeTagGenerator extends DamageTypeTagsProvider {
    public TEBladeDamageTypeTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, TEBlade.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR)
            .add(TEBladeDamageTypesBootstrap.WITNESS_RAINBOW);
        
        this.tag(DamageTypeTags.BYPASSES_WOLF_ARMOR)
            .add(TEBladeDamageTypesBootstrap.WITNESS_RAINBOW);
        
        this.tag(DamageTypeTags.PANIC_CAUSES)
            .add(TEBladeDamageTypesBootstrap.WITNESS_RAINBOW);
        
        this.tag(DamageTypeTags.CAN_BREAK_ARMOR_STAND)
            .add(TEBladeDamageTypesBootstrap.WITNESS_RAINBOW);
        
        this.tag(DamageTypeTags.BYPASSES_COOLDOWN)
            .add(TEBladeDamageTypesBootstrap.WITNESS_RAINBOW)
            .add(TEBladeDamageTypesBootstrap.SECONDARY_THUNDER)
            .add(TEBladeDamageTypesBootstrap.SECONDARY_FLAME);
        
        this.tag(TEBladeDamageTypesBootstrap.SECONDARY)
            .add(TEBladeDamageTypesBootstrap.SECONDARY_THUNDER)
            .add(TEBladeDamageTypesBootstrap.SECONDARY_FLAME);
    }
}
