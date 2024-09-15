package net.melon.slabs.blocks;

import com.mojang.serialization.MapCodec;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.melon.slabs.entities.JuicerBlockEntity;
import net.melon.slabs.items.MelonSlabsItems;
import net.melon.slabs.screens.JuicerScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties.PropertyRetriever;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
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

    public static final MapCodec<Juicer> CODEC = createCodec((settings) -> {
      return new Juicer();
    });

    public static final VoxelShape[] NORTH_SHAPES;
    public static final VoxelShape NORTH_SHAPE;
    public static final VoxelShape[] EAST_SHAPES;
    public static final VoxelShape EAST_SHAPE;
    public static final VoxelShape[] SOUTH_SHAPES;
    public static final VoxelShape SOUTH_SHAPE;
    public static final VoxelShape[] WEST_SHAPES;
    public static final VoxelShape WEST_SHAPE;


    public static final DirectionProperty FACING;

    public static final BooleanProperty HAS_BOTTLE;
    // private static final Text TITLE;

    public Juicer() {
        super(FabricBlockSettings.copy(Blocks.OAK_WOOD));
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH).with(HAS_BOTTLE, false));
    }

    public MapCodec<? extends Juicer> getCodec() {
        return CODEC;
    }

    public Item asItem() {
        return MelonSlabsItems.JUICER;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(HAS_BOTTLE);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    

    @Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
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

    //Block Properties
    static{
        FACING = HorizontalFacingBlock.FACING;
        HAS_BOTTLE = BooleanProperty.of("has_bottle");
        // TITLE = new TranslatableTextContent("container.crafting");
    }

    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getShape(state.get(FACING));
    }
  
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getShape(state.get(FACING));
    }

    //gets the shape based on the direction the juicer is facing
    protected VoxelShape getShape(Direction direction){

        // return NORTH_SHAPE;
        if (direction == Direction.NORTH) {
            return (NORTH_SHAPE);
        } else if (direction == Direction.EAST) {
            return (EAST_SHAPE);
        } else if (direction == Direction.SOUTH) {
            return (SOUTH_SHAPE);
        }


        return (WEST_SHAPE);
    }

    static{
        SOUTH_SHAPES = new VoxelShape[]{
            Block.createCuboidShape(0, 12, 0, 16, 16, 16),
            Block.createCuboidShape(0, 1, 0, 4, 12, 4),
            Block.createCuboidShape(12, 1, 0, 16, 12, 4),
            Block.createCuboidShape(12, 1, 12, 16, 12, 16),
            Block.createCuboidShape(0, 1, 12, 4, 12, 16),
            Block.createCuboidShape(4, 1, 12, 6, 5, 16),
            Block.createCuboidShape(10, 1, 12, 12, 5, 16),
            Block.createCuboidShape(8.5, 1, 12, 10, 5, 13),
            Block.createCuboidShape(6, 1, 12, 7.5, 5, 13),
            Block.createCuboidShape(7.5, 1, 12, 8.5, 3, 13),
            Block.createCuboidShape(7.5, 4, 12, 8.5, 5, 13),
            Block.createCuboidShape(0, 1, 4, 4, 5, 12),
            Block.createCuboidShape(12, 1, 4, 16, 5, 12),
            Block.createCuboidShape(4, 1, 0, 12, 5, 4),
            Block.createCuboidShape(0, 0, 0, 16, 1, 16)
        };
        SOUTH_SHAPE = VoxelShapes.union(SOUTH_SHAPES[0], SOUTH_SHAPES[1], SOUTH_SHAPES[2], SOUTH_SHAPES[3], SOUTH_SHAPES[4], SOUTH_SHAPES[5],
            SOUTH_SHAPES[6], SOUTH_SHAPES[7], SOUTH_SHAPES[8], SOUTH_SHAPES[9], SOUTH_SHAPES[10], 
            SOUTH_SHAPES[11], SOUTH_SHAPES[12], SOUTH_SHAPES[13], SOUTH_SHAPES[14]
        );
        WEST_SHAPES = new VoxelShape[]{
            Block.createCuboidShape(0, 12, 0, 16, 16, 16),
            Block.createCuboidShape(12, 1, 0, 16, 12, 4),
            Block.createCuboidShape(12, 1, 12, 16, 12, 16),
            Block.createCuboidShape(0, 1, 12, 4, 12, 16),
            Block.createCuboidShape(0, 1, 0, 4, 12, 4),
            Block.createCuboidShape(0, 1, 4, 4, 5, 6),
            Block.createCuboidShape(0, 1, 10, 4, 5, 12),
            Block.createCuboidShape(3, 1, 8.5, 4, 5, 10),
            Block.createCuboidShape(3, 1, 6, 4, 5, 7.5),
            Block.createCuboidShape(3, 1, 7.5, 4, 3, 8.5),
            Block.createCuboidShape(3, 4, 7.5, 4, 5, 8.5),
            Block.createCuboidShape(4, 1, 0, 12, 5, 4),
            Block.createCuboidShape(4, 1, 12, 12, 5, 16),
            Block.createCuboidShape(12, 1, 4, 16, 5, 12),
            Block.createCuboidShape(0, 0, 0, 16, 1, 16)
        };
        WEST_SHAPE = VoxelShapes.union(WEST_SHAPES[0], WEST_SHAPES[1], WEST_SHAPES[2], WEST_SHAPES[3], WEST_SHAPES[4], WEST_SHAPES[5],
            WEST_SHAPES[6], WEST_SHAPES[7], WEST_SHAPES[8], WEST_SHAPES[9], WEST_SHAPES[10], 
            WEST_SHAPES[11], WEST_SHAPES[12], WEST_SHAPES[13], WEST_SHAPES[14]
        );

        NORTH_SHAPES = new VoxelShape[]{
            Block.createCuboidShape(0, 12, 0, 16, 16, 16),
            Block.createCuboidShape(12, 1, 12, 16, 12, 16),
            Block.createCuboidShape(0, 1, 12, 4, 12, 16),
            Block.createCuboidShape(0, 1, 0, 4, 12, 4),
            Block.createCuboidShape(12, 1, 0, 16, 12, 4),
            Block.createCuboidShape(10, 1, 0, 12, 5, 4),
            Block.createCuboidShape(4, 1, 0, 6, 5, 4),
            Block.createCuboidShape(6, 1, 3, 7.5, 5, 4),
            Block.createCuboidShape(8.5, 1, 3, 10, 5, 4),
            Block.createCuboidShape(7.5, 1, 3, 8.5, 3, 4),
            Block.createCuboidShape(7.5, 4, 3, 8.5, 5, 4),
            Block.createCuboidShape(12, 1, 4, 16, 5, 12),
            Block.createCuboidShape(0, 1, 4, 4, 5, 12),
            Block.createCuboidShape(4, 1, 12, 12, 5, 16),
            Block.createCuboidShape(0, 0, 0, 16, 1, 16)
        };
        NORTH_SHAPE = VoxelShapes.union(NORTH_SHAPES[0], NORTH_SHAPES[1], NORTH_SHAPES[2], NORTH_SHAPES[3], NORTH_SHAPES[4], NORTH_SHAPES[5],
            NORTH_SHAPES[6], NORTH_SHAPES[7], NORTH_SHAPES[8], NORTH_SHAPES[9], NORTH_SHAPES[10], 
            NORTH_SHAPES[11], NORTH_SHAPES[12], NORTH_SHAPES[13], NORTH_SHAPES[14]
        );

        EAST_SHAPES = new VoxelShape[]{
            Block.createCuboidShape(0, 12, 0, 16, 16, 16),
            Block.createCuboidShape(0, 1, 12, 4, 12, 16),
            Block.createCuboidShape(0, 1, 0, 4, 12, 4),
            Block.createCuboidShape(12, 1, 0, 16, 12, 4),
            Block.createCuboidShape(12, 1, 12, 16, 12, 16),
            Block.createCuboidShape(12, 1, 10, 16, 5, 12),
            Block.createCuboidShape(12, 1, 4, 16, 5, 6),
            Block.createCuboidShape(12, 1, 6, 13, 5, 7.5),
            Block.createCuboidShape(12, 1, 8.5, 13, 5, 10),
            Block.createCuboidShape(12, 1, 7.5, 13, 3, 8.5),
            Block.createCuboidShape(12, 4, 7.5, 13, 5, 8.5),
            Block.createCuboidShape(4, 1, 12, 12, 5, 16),
            Block.createCuboidShape(4, 1, 0, 12, 5, 4),
            Block.createCuboidShape(0, 1, 4, 4, 5, 12),
            Block.createCuboidShape(0, 0, 0, 16, 1, 16)
        };
        EAST_SHAPE = VoxelShapes.union(EAST_SHAPES[0], EAST_SHAPES[1], EAST_SHAPES[2], EAST_SHAPES[3], EAST_SHAPES[4], EAST_SHAPES[5],
            EAST_SHAPES[6], EAST_SHAPES[7], EAST_SHAPES[8], EAST_SHAPES[9], EAST_SHAPES[10], 
            EAST_SHAPES[11], EAST_SHAPES[12], EAST_SHAPES[13], EAST_SHAPES[14]
        );

    }

}