package net.melon.slabs.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class MelonSlabAlmostFull extends SlabBlock{

    public MelonSlabAlmostFull() {
        super(FabricBlockSettings.copy(Blocks.MELON));
    }
    
    //used to make it so that we cant get a double slab melon rind
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) { 
        BlockState blockState = world.getBlockState(pos);

        //cant place it here if we have another one of its type here
        if (blockState.isOf(this)){
            return false;
        }

        return(super.canPlaceAt( state,  world,  pos));
    }
}