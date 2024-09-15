package net.melon.slabs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
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

@Mixin(targets = "net.minecraft.block.AbstractBlock$AbstractBlockState")
public class AbstractBlockStateMixin{

    @SuppressWarnings("all")
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void makeMelonsShearable(World world, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (((AbstractBlock.AbstractBlockState) (Object) this).isOf(Blocks.MELON)){
            BlockPos pos = hit.getBlockPos();

            ItemStack itemStack = player.getStackInHand(hand);
            if (itemStack.getItem() == Items.SHEARS) {
                if (!world.isClient) {
                    if (!player.isCreative()){
                        itemStack.damage(1, player, player.getSlotForHand(hand));
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
    
                cir.setReturnValue(ActionResult.success(world.isClient));
            }
        }
    }
}