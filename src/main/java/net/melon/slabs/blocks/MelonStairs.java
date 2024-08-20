package net.melon.slabs.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.Item;

public class MelonStairs extends StairsBlock{

    public MelonStairs() {
        super(Blocks.MELON.getDefaultState(), FabricBlockSettings.copy(Blocks.MELON));
    }
    
    public Item asItem() {
        return MelonSlabsItems.MELON_STAIRS;
    }
}