package net.melon.slabs.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.Item;

public class PumpkinStairs extends StairsBlock{

    public PumpkinStairs() {
        super(Blocks.PUMPKIN.getDefaultState(), FabricBlockSettings.copy(Blocks.PUMPKIN));
    }
    
    public Item asItem() {
        return MelonSlabsItems.PUMPKIN_STAIRS;
    }
}