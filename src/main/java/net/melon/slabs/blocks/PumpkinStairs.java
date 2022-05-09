package net.melon.slabs.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;

public class PumpkinStairs extends StairsBlock{

    public PumpkinStairs() {
        super(Blocks.PUMPKIN.getDefaultState(), FabricBlockSettings.copy(Blocks.PUMPKIN));
    }
    
}