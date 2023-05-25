package net.melon.slabs.blocks;


import java.util.ArrayList;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.melon.slabs.criteria.MelonSlabsCriteria;
import net.melon.slabs.sounds.MelonSlabsSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FrankenMelon extends Block{
    public static final DirectionProperty FACING;
    public static final BooleanProperty LIT;
    public FrankenMelon() {
        super(FabricBlockSettings.of(Material.GOURD).ticksRandomly().sounds(BlockSoundGroup.WOOD).hardness(1.0f).resistance(1.0f));
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(LIT, true).with(FACING, Direction.NORTH));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    private Direction getRandomDirection(){
        return(getRandomDirection(Random.create()));
    }

    private Direction getRandomDirection(Random random){
        Direction[] directions = new Direction[4];

        directions[0] = Direction.NORTH;
        directions[1] = Direction.EAST;
        directions[2] = Direction.SOUTH;
        directions[3] = Direction.WEST;

        return(directions[Random.create().nextInt(4)]);
    }

    //@Override
    //makes it light when lightning
    // public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity){
    //     if (!world.isClient()){
    //         if (!state.get(LIT)){
    //             if (entity instanceof net.minecraft.entity.projectile.TridentEntity){
    //                 if (world.isThundering() && EnchantmentHelper.hasChanneling(((TridentMixin) entity).getTridentStack()) && world.isSkyVisible(pos.up())){
    //                     world.setBlockState(pos, state.with(LIT, true).with(FACING, this.getRandomDirection()), Block.NOTIFY_LISTENERS);

    //                     Entity ownerEntity = ((net.minecraft.entity.projectile.TridentEntity) entity).getOwner();
    //                     MelonSlabsCriteria.CREATED_FRANKENMELON.trigger((ownerEntity instanceof ServerPlayerEntity ? (ServerPlayerEntity)ownerEntity : null));

    //                     LightningEntity lightningEntity = (LightningEntity)EntityType.LIGHTNING_BOLT.create(world);
    //                     lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
    //                     lightningEntity.setChanneler(null);
    //                     world.spawnEntity(lightningEntity);
    //                 }
    //             }
    //         } else if (entity instanceof net.minecraft.entity.projectile.ProjectileEntity){
    //             this.getHurt(state, world, pos);
    //         }
    //     }
    //     super.onEntityCollision(state, world, pos, entity);
    // }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        BlockPos blockPos = hit.getBlockPos();
        
        if (state.get(LIT)){
            this.getHurt(state, world, hit.getBlockPos());
        } else {
            if (world.isThundering() && projectile instanceof TridentEntity && ((TridentEntity)projectile).hasChanneling() && world.isSkyVisible(blockPos.up())) {
                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos.up()));
                Entity ownerEntity = projectile.getOwner();
                lightningEntity.setChanneler(ownerEntity instanceof ServerPlayerEntity ? (ServerPlayerEntity)ownerEntity : null);
                MelonSlabsCriteria.CREATED_FRANKENMELON.trigger((ownerEntity instanceof ServerPlayerEntity ? (ServerPlayerEntity)ownerEntity : null));
                world.spawnEntity(lightningEntity);
                world.playSound(null, blockPos, SoundEvents.ITEM_TRIDENT_THUNDER, SoundCategory.WEATHER, 5.0f, 1.0f);

                this.lightFrankenmelon(blockPos, world, state);
            }
        }
    }

    //Pos, world, and blockstate, are the places where this block is
    public void lightFrankenmelon (BlockPos pos, World world, BlockState state){
        world.setBlockState(pos, state.with(LIT, true).with(FACING, this.getRandomDirection()), Block.NOTIFY_LISTENERS);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
        return (BlockState) this.getDefaultState().with(FACING, direction);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    //make it so that frankenmelon lights when a lightning rod above it gets struck
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) {
            return;
        } else {
            if (state.get(LIT)){
                return;
            }

            //check if there is a powered lightning rod above
            BlockState upBlockState = world.getBlockState(pos.up());
            if (upBlockState.isOf(Blocks.LIGHTNING_ROD)){
                if(upBlockState.get(Properties.POWERED)){
                    this.lightFrankenmelon(pos, world, state);
                }
            }
        }
    }

    //randomly teleports one block at a time
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(LIT)){
            ArrayList<BlockPos> posList = new ArrayList<BlockPos>();

            //add current location so there is always a location to teleport to
            posList.add(pos);

            //generates a list of locations that can be teleported onto in a 3 by 3 cube around the block
            for (int x = -1; x<2; x++){
                for (int y = -1; y<2; y++){
                    for (int z = -1; z<2; z++){
                        //make sure it doesn't teleport to the block directly above
                        if (! (x == 0 && y == 0 && z == 1)){
                            BlockPos locationToCheck = pos.north(x).west(y).up(z);
                            //add blockpos is it is a valid teleport destination
                            if (canTeleportInto(locationToCheck, world)){
                                posList.add(locationToCheck);
                            }
                        }
                    }
                }
            }

            BlockPos randomPos = posList.get(random.nextInt(posList.size()));

            //place the block (facing the same direction) at new location after removing old location
            world.removeBlock(pos, false);
            world.updateNeighbors(pos, this);
            world.setBlockState(randomPos, this.getDefaultState().with(FACING, this.getRandomDirection(random)), Block.NOTIFY_LISTENERS);
            world.updateNeighbors(randomPos, this);
        }
    }


    //returns wether or not a block can be teleported into by a frankenmelon
    private boolean canTeleportInto (BlockPos pos, ServerWorld world){
        BlockState blockState = world.getBlockState(pos.down());
        return (blockState.getMaterial().isSolid() && blockState.isFullCube(world, pos) && (world.isAir(pos) || world.getBlockState(pos).getMaterial().isReplaceable()));
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        this.getHurt(state, world, pos);
        super.onBlockBreakStart(state, world, pos, player);
    }

    //makes the sound of the frankenmelon getting hurt
    private void getHurt (BlockState state, World world, BlockPos pos){
        if (!world.isClient && state.get(LIT)) {
            Random rd = Random.create();
            world.playSound(null, pos, MelonSlabsSounds.FRANKENMELON_HURT_EVENT, SoundCategory.BLOCKS, 0.2f + rd.nextFloat()/10f, 0.8f + + rd.nextFloat()/2.5f);
        }
    }

    static{
        FACING = HorizontalFacingBlock.FACING;
        LIT = Properties.LIT;
    }
}