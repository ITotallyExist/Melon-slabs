package net.melon.slabs.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;


public class CactusSlab extends CactusBlock{
    protected static final VoxelShape OUTLINE_SHAPE;

    protected static final VoxelShape COLLISION_SHAPE;

    public CactusSlab() {
        super(FabricBlockSettings.copy(Blocks.CACTUS));
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(AGE, 0));
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }
  
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (Blocks.CACTUS.canPlaceAt(state, world, pos)){
            int i;
            for(i = 1; world.getBlockState(pos.down(i)).isOf(Blocks.CACTUS); ++i) {
            }

            if (i < 4) {
                int j = (Integer)state.get(AGE);
                if (j >= 7) {
                    world.setBlockState(pos, Blocks.CACTUS.getDefaultState().with(AGE,0));
                    state.neighborUpdate(world, pos, this, pos, false);
                } else {
                    world.setBlockState(pos, (BlockState)state.with(AGE, j + 1), 4);
                }
            }
        }
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) { 
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        return (blockState.getMaterial().isSolid() && blockState.isFullCube(world, blockPos)) || blockState.isOf(Blocks.CACTUS);
        //return false;   
    }


    static {
        OUTLINE_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);

        COLLISION_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 7.0D, 15.0D);
    }
}