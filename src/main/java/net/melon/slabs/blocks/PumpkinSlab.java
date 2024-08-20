package net.melon.slabs.blocks;

import java.util.function.Consumer;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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


public class PumpkinSlab extends SlabBlock{

    public PumpkinSlab() {
        super(FabricBlockSettings.copy(Blocks.PUMPKIN));
    }
    
    public Item asItem() {
        return MelonSlabsItems.PUMPKIN_SLAB;
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
                world.setBlockState(pos, (BlockState)MelonSlabsBlocks.CARVED_PUMPKIN_SLAB.getDefaultState().with(CarvedPumpkinSlab.FACING, direction2).with(TYPE, slabType).with(WATERLOGGED, waterlogged), 11);
                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D + (double)direction2.getOffsetX() * 0.65D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D + (double)direction2.getOffsetZ() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, itemNum));
                itemEntity.setVelocity(0.05D * (double)direction2.getOffsetX() + world.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction2.getOffsetZ() + world.random.nextDouble() * 0.02D);
                world.spawnEntity(itemEntity);
            }
  
           return ActionResult.success(world.isClient);
        } else {
           return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    @Override
    //the same as default but allows replacement by melon slabs (to make frankenmelons)
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        ItemStack itemStack = context.getStack();
        SlabType slabType = (SlabType)state.get(TYPE);
        if (slabType != SlabType.DOUBLE && (itemStack.getItem() == this.asItem() || (itemStack.getItem() == MelonSlabsItems.MELON_SLAB.asItem() && slabType == SlabType.BOTTOM))) {
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
}