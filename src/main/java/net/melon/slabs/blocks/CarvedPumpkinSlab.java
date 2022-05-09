package net.melon.slabs.blocks;

import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.fluid.Fluids;


public class CarvedPumpkinSlab extends SlabBlock{
    public static final DirectionProperty FACING;
    public CarvedPumpkinSlab() {
        super(FabricBlockSettings.copy(Blocks.PUMPKIN));
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = ctx.getWorld().getBlockState(blockPos);
        if (blockState.isOf(this)) {
            return (BlockState)((BlockState)blockState.with(TYPE, SlabType.DOUBLE)).with(WATERLOGGED, false);
        } else if (blockState.isOf(MelonSlabsBlocks.JACK_O_SLAB)){
            return (BlockState)((BlockState)MelonSlabsBlocks.JACK_O_SLAB.getDefaultState().with(FACING, blockState.get(FACING)).with(TYPE, SlabType.DOUBLE)).with(WATERLOGGED, false);
        } else {
            FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
            BlockState blockState2 = (BlockState)((BlockState)this.getDefaultState().with(TYPE, SlabType.BOTTOM)).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER).with(FACING, ctx.getPlayerFacing().getOpposite());
            Direction direction = ctx.getSide();
            return direction != Direction.DOWN && (direction == Direction.UP || ctx.getHitPos().y - (double)blockPos.getY() <= 0.5D) ? blockState2 : (BlockState)blockState2.with(TYPE, SlabType.TOP).with(FACING, ctx.getPlayerFacing().getOpposite());
        }
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        ItemStack itemStack = context.getStack();
        SlabType slabType = (SlabType)state.get(TYPE);
        if (slabType != SlabType.DOUBLE && (itemStack.getItem() == this.asItem() || itemStack.getItem() == MelonSlabsItems.JACK_O_SLAB.asItem())) {
           if (context.canReplaceExisting()) {
              boolean bl = context.getHitPos().y - (double)context.getBlockPos().getY() > 0.5D;
              Direction direction = context.getSide();
              if (slabType == SlabType.BOTTOM) {
                 return direction == Direction.UP || bl && direction.getAxis().isHorizontal();
              } else {
                 return direction == Direction.DOWN || !bl && direction.getAxis().isHorizontal();
              }
           } else {
              return true;
           }
        } else {
           return false;
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == Items.TORCH) {

            SlabType slabType = (SlabType)state.get(TYPE);

            world.setBlockState(pos, (BlockState)(MelonSlabsBlocks.JACK_O_SLAB.getDefaultState().with(FACING, state.get(FACING)).with(TYPE, slabType)).with(WATERLOGGED, false));
            
            if (!player.isCreative()){
                itemStack.decrement(1);
            }
            return ActionResult.success(world.isClient);
  
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }
    
    static{
        FACING = HorizontalFacingBlock.FACING;
    }
}