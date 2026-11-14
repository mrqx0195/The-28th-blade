package net.mrqx.slashblade.teblade.data.thaumaturge.datamap;

import com.leclowndu93150.thaumaturge.content.infernalfurnace.InfernalBonus;
import mods.flammpfeil.slashblade.data.tag.SlashBladeItemTags;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.mrqx.slashblade.teblade.data.DataGen;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TEBladeInfernalBonusProvider extends DataMapProvider {
    public TEBladeInfernalBonusProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }
    
    @Override
    protected void gather(HolderLookup.Provider provider) {
        HolderLookup<Item> items = provider.lookupOrThrow(Registries.ITEM);
        Builder<List<InfernalBonus>, Item> builder = this.builder(InfernalBonus.DATA_MAP);
        builder.conditions(DataGen.THAUMATURGE_CONDITION);
        
        add(builder,
            SlashBladeItemTags.PROUD_SOULS,
            InfernalBonus.builder(items, Tags.Items.NUGGETS_IRON)
                .count(1)
                .chance(0.33F)
                .build(),
            InfernalBonus.builder(SlashBladeItems.PROUDSOUL_TINY.get())
                .count(1)
                .chance(0.33F)
                .build()
        );
    }
    
    @Override
    public String getName() {
        return "TEBlade Infernal Furnace Bonus Data Map";
    }
    
    private static void add(Builder<List<InfernalBonus>, Item> b, ItemLike key, InfernalBonus... values) {
        b.add(BuiltInRegistries.ITEM.wrapAsHolder(key.asItem()), Arrays.stream(values).toList(), false);
    }
    
    private static void add(Builder<List<InfernalBonus>, Item> b, TagKey<Item> key, InfernalBonus... values) {
        b.add(key, Arrays.stream(values).toList(), false);
    }
}
