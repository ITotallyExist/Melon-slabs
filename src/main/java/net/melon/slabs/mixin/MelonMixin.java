package net.melon.slabs.mixin;

import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.MelonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.melon.slabs.blocks.CarvedMelon;
import net.melon.slabs.blocks.MelonSlabsBlocks;

@Mixin(MelonBlock.class)
public class MelonMixin extends Block{

    public MelonMixin(){
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
                
                Integer itemNum =  4;

                Direction direction = hit.getSide();
                Direction direction2 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction;
                world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.setBlockState(pos, (BlockState)MelonSlabsBlocks.CARVED_MELON.getDefaultState().with(CarvedMelon.FACING, direction2), 11);
                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D + (double)direction2.getOffsetX() * 0.65D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D + (double)direction2.getOffsetZ() * 0.65D, new ItemStack(Items.MELON_SEEDS, itemNum));
                itemEntity.setVelocity(0.05D * (double)direction2.getOffsetX() + world.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction2.getOffsetZ() + world.random.nextDouble() * 0.02D);
                world.spawnEntity(itemEntity);//hi
            }
  
           return ActionResult.success(world.isClient);
        } else {
           return super.onUse(state, world, pos, player, hand, hit);
        }
    }
}