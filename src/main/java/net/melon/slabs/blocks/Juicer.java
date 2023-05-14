package net.melon.slabs.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.melon.slabs.entities.JuicerBlockEntity;
import net.melon.slabs.screens.JuicerScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;


public class Juicer extends BlockWithEntity  {

    public static final VoxelShape SHAPE;
    public static final VoxelShape SHAPE1;
    public static final VoxelShape SHAPE2;
    public static final VoxelShape SHAPE3;
    public static final VoxelShape SHAPE4;
    public static final VoxelShape SHAPE5;
    public static final VoxelShape SHAPE6;
    public static final VoxelShape SHAPE7;
    public static final VoxelShape SHAPE8;
    public static final VoxelShape SHAPE9;
    public static final VoxelShape SHAPE10;
    public static final VoxelShape SHAPE11;
    public static final VoxelShape SHAPE12;
    public static final VoxelShape SHAPE13;
    public static final VoxelShape SHAPE14;
    public static final VoxelShape SHAPE15;

    public static final DirectionProperty FACING;
    // private static final Text TITLE;

    public Juicer() {
        super(FabricBlockSettings.copy(Blocks.OAK_WOOD));
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    

    @Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(world.isClient) {
			return ActionResult.SUCCESS;
		} else {
            //player.sendMessage(Text.literal("WIP, check back later"), false);
            
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                //With this call the server will request the client to open the appropriate Screenhandler
                player.openHandledScreen(screenHandlerFactory);
            }
			return ActionResult.CONSUME;
		}
	}
 
	// @Override
	// public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
	// 	return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
	// 		return new JuicerScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos));
	// 	}, TITLE);
	// }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new JuicerBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        //With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    //TODO deprecated, d this with the block state instead
    //This method will drop all items onto the ground when the block is broken
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof JuicerBlockEntity) {
                ((JuicerBlockEntity) blockEntity).clearCraftingResult();
                ItemScatterer.spawn(world, pos, (JuicerBlockEntity)blockEntity);
                // update comparators
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
 
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }
 
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return JuicerScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    static{
        FACING = HorizontalFacingBlock.FACING;
        // TITLE = new TranslatableTextContent("container.crafting");
    }

    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
  
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }


    static{
        SHAPE1 = Block.createCuboidShape(0, 12, 0, 16, 16, 16);
        SHAPE2 = Block.createCuboidShape(0, 1, 0, 4, 12, 4);
        SHAPE3 = Block.createCuboidShape(12, 1, 0, 16, 12, 4);
        SHAPE4 = Block.createCuboidShape(12, 1, 12, 16, 12, 16);
        SHAPE5 = Block.createCuboidShape(0, 1, 12, 4, 12, 16);
        SHAPE6 = Block.createCuboidShape(4, 1, 12, 6, 5, 16);
        SHAPE7 = Block.createCuboidShape(10, 1, 12, 12, 5, 16);
        SHAPE8 = Block.createCuboidShape(8.5, 1, 12, 10, 5, 13);
        SHAPE9 = Block.createCuboidShape(6, 1, 12, 7.5, 5, 13);
        SHAPE10 = Block.createCuboidShape(7.5, 1, 12, 8.5, 3, 13);
        SHAPE11 = Block.createCuboidShape(7.5, 4, 12, 8.5, 5, 13);
        SHAPE12 = Block.createCuboidShape(0, 1, 4, 4, 5, 12);
        SHAPE13 = Block.createCuboidShape(12, 1, 4, 16, 5, 12);
        SHAPE14 = Block.createCuboidShape(4, 1, 0, 12, 5, 4);
        SHAPE15 = Block.createCuboidShape(0, 0, 0, 16, 1, 16);
        SHAPE = VoxelShapes.union(SHAPE1, SHAPE2, SHAPE3, SHAPE4, SHAPE5, SHAPE6, SHAPE7, SHAPE8, SHAPE9, SHAPE10);
    }

}