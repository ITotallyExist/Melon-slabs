package net.melon.slabs.blocks;

import java.util.stream.Stream;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class MelonRind extends SlabBlock {
    protected static final VoxelShape OUTLINE_SHAPE_BOTTOM;
    protected static final VoxelShape OUTLINE_SHAPE_TOP;

    public MelonRind() {
        super(FabricBlockSettings.copy(Blocks.MELON));
    }
    
    public Item asItem() {
        return MelonSlabsItems.MELON_RIND;
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

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        SlabType slabType = state.get(TYPE);

        switch (slabType) {
            case DOUBLE: {
                return VoxelShapes.fullCube();
            }
            case TOP: {
                return OUTLINE_SHAPE_TOP;
            }
            case BOTTOM: {
                return OUTLINE_SHAPE_BOTTOM;
            }
        }

        return OUTLINE_SHAPE_BOTTOM;
    }
  
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        SlabType slabType = state.get(TYPE);

        switch (slabType) {
            case DOUBLE: {
                return VoxelShapes.fullCube();
            }
            case TOP: {
                return OUTLINE_SHAPE_TOP;
            }
            case BOTTOM: {
                return OUTLINE_SHAPE_BOTTOM;
            }
        }

        return OUTLINE_SHAPE_BOTTOM;
    }

    static {
        OUTLINE_SHAPE_BOTTOM = 
            VoxelShapes.combineAndSimplify(Stream.of(
            Block.createCuboidShape(0, 0, 0, 2, 8, 16),
            Block.createCuboidShape(2, 0, 0, 14, 8, 2),
            Block.createCuboidShape(14, 0, 0, 16, 8, 16),
            Block.createCuboidShape(2, 0, 14, 14, 8, 16)
            ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get(), Block.createCuboidShape(2, 0, 2, 14, 2, 14), BooleanBiFunction.OR);

        OUTLINE_SHAPE_TOP = VoxelShapes.combineAndSimplify(Stream.of(
            Block.createCuboidShape(0, 8, 0, 2, 16, 16),
            Block.createCuboidShape(2, 8, 0, 14, 16, 2),
            Block.createCuboidShape(14, 8, 0, 16, 16, 16),
            Block.createCuboidShape(2, 8, 14, 14, 16, 16)
            ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get(), Block.createCuboidShape(2, 14, 2, 14, 16, 14), BooleanBiFunction.OR);    
        }
}