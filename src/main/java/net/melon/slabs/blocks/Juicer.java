package net.melon.slabs.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


public class Juicer extends Block {
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
            //TODO, fix message
            // player.sendMessage(MutableText.of(new TextContent("WIP, check back later")), false);
            // player.sendMessage(new LiteralTextContent("WIP, check back later"), false);

			// player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
			return ActionResult.CONSUME;
		}
	}
 
	// @Override
	// public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
	// 	return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
	// 		return new JuicerScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos));
	// 	}, TITLE);
	// }

    //block entity stuff -- not used, we would use implements BlockEntityProvider  if we want to do this later
    // @Override
    // public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    //     return new JuicerBlockEntity(pos, state);
    // }

    static{
        FACING = HorizontalFacingBlock.FACING;
        // TITLE = new TranslatableTextContent("container.crafting");
    }


}