package net.melon.slabs.blocks;

import java.util.function.Consumer;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MelonSlab extends SlabBlock{

    public MelonSlab() {
        super(FabricBlockSettings.copy(Blocks.MELON));
    }
    
    @SuppressWarnings("all")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == Items.SHEARS) {
            if (!world.isClient) {
                if (!player.isCreative()){
                    itemStack.damage(1, (PlayerEntity)player, (Consumer)(PlayerEntity) -> {
                        ((LivingEntity) PlayerEntity).sendToolBreakStatus(hand);
                    });
                }
                
                Boolean waterlogged = state.get(WATERLOGGED);
                SlabType slabType = (SlabType)state.get(TYPE);

                Integer itemNum = (slabType == SlabType.DOUBLE) ? 4 : 3;

                Direction direction = hit.getSide();
                Direction direction2 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction;
                world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.setBlockState(pos, (BlockState)MelonSlabsBlocks.CARVED_MELON_SLAB.getDefaultState().with(CarvedMelonSlab.FACING, direction2).with(TYPE, slabType).with(WATERLOGGED, waterlogged), 11);
                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D + (double)direction2.getOffsetX() * 0.65D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D + (double)direction2.getOffsetZ() * 0.65D, new ItemStack(Items.MELON_SEEDS, itemNum));
                itemEntity.setVelocity(0.05D * (double)direction2.getOffsetX() + world.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction2.getOffsetZ() + world.random.nextDouble() * 0.02D);
                world.spawnEntity(itemEntity);
            }
  
           return ActionResult.success(world.isClient);
        } else {
           return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    //default but edited to allow frankenmelons if placed on a pumpkin slab
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = ctx.getWorld().getBlockState(blockPos);
        if (blockState.isOf(this)) {
           return (BlockState)((BlockState)blockState.with(TYPE, SlabType.DOUBLE)).with(WATERLOGGED, false);
        } else if (blockState.isOf(MelonSlabsBlocks.PUMPKIN_SLAB)){
            return (MelonSlabsBlocks.FRANKENMELON.getDefaultState().with(FrankenMelon.LIT, false));
        } else {
           FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
           BlockState blockState2 = (BlockState)((BlockState)this.getDefaultState().with(TYPE, SlabType.BOTTOM)).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
           Direction direction = ctx.getSide();
           return direction != Direction.DOWN && (direction == Direction.UP || ctx.getHitPos().y - (double)blockPos.getY() <= 0.5D) ? blockState2 : (BlockState)blockState2.with(TYPE, SlabType.TOP);
        }
     }
}