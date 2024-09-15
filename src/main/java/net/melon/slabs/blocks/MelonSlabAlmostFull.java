package net.melon.slabs.blocks;

import java.util.function.Consumer;
import java.util.stream.Stream;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class MelonSlabAlmostFull extends SlabBlock{
    protected static final VoxelShape OUTLINE_SHAPE_BOTTOM;
    protected static final VoxelShape OUTLINE_SHAPE_TOP;

    public MelonSlabAlmostFull() {
        super(FabricBlockSettings.copy(Blocks.MELON));
    }
    
    public Item asItem() {
        return MelonSlabsItems.MELON_SLAB_ALMOST_FULL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(TYPE) != SlabType.DOUBLE){
            //if player can eat
            if (!player.canConsume(false)) {
                return ActionResult.PASS;
            }

            if (world.isClient) {
                return ActionResult.SUCCESS;
            }

            //eat food
            player.getHungerManager().add(MelonSlab.FOOD_POINTS, MelonSlab.SATURATION_FACTOR);
            world.emitGameEvent((Entity)player, GameEvent.EAT, pos);
            
            //set the block state to an eaten one but with the same slabtype
            world.setBlockState(pos, MelonSlabsBlocks.MELON_SLAB_ALMOST_EMPTY.getDefaultState().with(TYPE, state.get(TYPE)), Block.NOTIFY_ALL);

            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hit);
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
        OUTLINE_SHAPE_BOTTOM = VoxelShapes.combineAndSimplify(Stream.of(
            Block.createCuboidShape(0, 0, 0, 2, 8, 16),
            Block.createCuboidShape(2, 0, 0, 14, 8, 2),
            Block.createCuboidShape(14, 0, 0, 16, 8, 16),
            Block.createCuboidShape(2, 0, 6, 14, 8, 16)
            ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get(), Block.createCuboidShape(2, 0, 2, 14, 2, 6), BooleanBiFunction.OR);

        OUTLINE_SHAPE_TOP = VoxelShapes.combineAndSimplify(Stream.of(
            Block.createCuboidShape(0, 8, 0, 2, 16, 16),
            Block.createCuboidShape(2, 8, 0, 14, 16, 2),
            Block.createCuboidShape(14, 8, 0, 16, 16, 16),
            Block.createCuboidShape(2, 8, 6, 14, 16, 16)
            ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get(), Block.createCuboidShape(2, 14, 2, 14, 16, 6), BooleanBiFunction.OR);
    }
}