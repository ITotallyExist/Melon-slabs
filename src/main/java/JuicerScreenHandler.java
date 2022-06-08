// package net.melon.slabs.screens;

// import java.util.Optional;

// import net.melon.slabs.blocks.MelonSlabsBlocks;
// import net.minecraft.block.Blocks;
// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.entity.player.PlayerInventory;
// import net.minecraft.inventory.CraftingInventory;
// import net.minecraft.inventory.CraftingResultInventory;
// import net.minecraft.inventory.Inventory;
// import net.minecraft.item.ItemStack;
// import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
// import net.minecraft.recipe.CraftingRecipe;
// import net.minecraft.recipe.Recipe;
// import net.minecraft.recipe.RecipeMatcher;
// import net.minecraft.recipe.RecipeType;
// import net.minecraft.recipe.book.RecipeBookCategory;
// import net.minecraft.screen.AbstractRecipeScreenHandler;
// import net.minecraft.screen.ScreenHandler;
// import net.minecraft.screen.ScreenHandlerContext;
// import net.minecraft.screen.ScreenHandlerType;
// import net.minecraft.screen.slot.CraftingResultSlot;
// import net.minecraft.screen.slot.Slot;
// import net.minecraft.server.network.ServerPlayerEntity;
// import net.minecraft.world.World;

// public class JuicerScreenHandler extends AbstractRecipeScreenHandler<JuicerInventory> {
//     public static final int field_30781 = 0;
//     private static final int field_30782 = 1;
//     private static final int field_30783 = 10;
//     private static final int field_30784 = 10;
//     private static final int field_30785 = 37;
//     private static final int field_30786 = 37;
//     private static final int field_30787 = 46;
//     private final JuicerInventory input = new JuicerInventory(this, 4, 1);
//     private final CraftingResultInventory result = new CraftingResultInventory();
//     private final ScreenHandlerContext context;
//     private final PlayerEntity player;

//     public JuicerScreenHandler(int syncId, PlayerInventory playerInventory) {
//         this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
//     }

//     public JuicerScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
//         super(ScreenHandlerType.CRAFTING, syncId);
//         int j;
//         int i;
//         this.context = context;
//         this.player = playerInventory.player;
//         this.addSlot(new CraftingResultSlot(playerInventory.player, this.input, this.result, 0, 124, 35));

//         //adding the crafting slots
//         this.addSlot(new Slot(this.input, 0, 30 , 17 + 9));
//         this.addSlot(new Slot(this.input, 1, 48 , 17));
//         this.addSlot(new Slot(this.input, 2, 66 , 17 + 9));
//         this.addSlot(new Slot(this.input, 3, 48 , 17 + (2*18)));

//         for (i = 0; i < 3; ++i) {
//             for (j = 0; j < 9; ++j) {
//                 this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
//             }
//         }
//         for (i = 0; i < 9; ++i) {
//             this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
//         }
//     }

//     protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory) {
//         CraftingRecipe craftingRecipe;
//         if (world.isClient) {
//             return;
//         }
//         ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
//         ItemStack itemStack = ItemStack.EMPTY;
//         Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingInventory, world);
//         if (optional.isPresent() && resultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe = optional.get())) {
//             itemStack = craftingRecipe.craft(craftingInventory);
//         }
//         resultInventory.setStack(0, itemStack);
//         handler.setPreviousTrackedSlot(0, itemStack);
//         serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
//     }

//     @Override
//     public void onContentChanged(Inventory inventory) {
//         this.context.run((world, pos) -> JuicerScreenHandler.updateResult(this, world, this.player, this.input, this.result));
//     }

//     @Override
//     public void populateRecipeFinder(RecipeMatcher finder) {
//         this.input.provideRecipeInputs(finder);
//     }

//     @Override
//     public void clearCraftingSlots() {
//         this.input.clear();
//         this.result.clear();
//     }

//     @Override
//     public boolean matches(Recipe<? super JuicerInventory> recipe) {
//         return recipe.matches(this.input, this.player.world);
//     }

//     @Override
//     public void close(PlayerEntity player) {
//         super.close(player);
//         this.context.run((world, pos) -> this.dropInventory(player, this.input));
//     }

//     @Override
//     public boolean canUse(PlayerEntity player) {
//         return JuicerScreenHandler.canUse(this.context, player, MelonSlabsBlocks.JUICER);
//     }

//     @Override
//     public ItemStack transferSlot(PlayerEntity player, int index) {
//         ItemStack itemStack = ItemStack.EMPTY;
//         Slot slot = (Slot)this.slots.get(index);
//         if (slot != null && slot.hasStack()) {
//             ItemStack itemStack2 = slot.getStack();
//             itemStack = itemStack2.copy();
//             if (index == 0) {
//                 this.context.run((world, pos) -> itemStack2.getItem().onCraft(itemStack2, (World)world, player));
//                 if (!this.insertItem(itemStack2, 10, 46, true)) {
//                     return ItemStack.EMPTY;
//                 }
//                 slot.onQuickTransfer(itemStack2, itemStack);
//             } else if (index >= 10 && index < 46 ? !this.insertItem(itemStack2, 1, 10, false) && (index < 37 ? !this.insertItem(itemStack2, 37, 46, false) : !this.insertItem(itemStack2, 10, 37, false)) : !this.insertItem(itemStack2, 10, 46, false)) {
//                 return ItemStack.EMPTY;
//             }
//             if (itemStack2.isEmpty()) {
//                 slot.setStack(ItemStack.EMPTY);
//             } else {
//                 slot.markDirty();
//             }
//             if (itemStack2.getCount() == itemStack.getCount()) {
//                 return ItemStack.EMPTY;
//             }
//             slot.onTakeItem(player, itemStack2);
//             if (index == 0) {
//                 player.dropItem(itemStack2, false);
//             }
//         }
//         return itemStack;
//     }

//     @Override
//     public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
//         return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
//     }

//     @Override
//     public int getCraftingResultSlotIndex() {
//         return 0;
//     }

//     @Override
//     public int getCraftingWidth() {
//         return this.input.getWidth();
//     }

//     @Override
//     public int getCraftingHeight() {
//         return this.input.getHeight();
//     }

//     @Override
//     public int getCraftingSlotCount() {
//         return 4;
//     }

//     @Override
//     public RecipeBookCategory getCategory() {
//         return RecipeBookCategory.CRAFTING;
//     }

//     @Override
//     public boolean canInsertIntoSlot(int index) {
//         return index != this.getCraftingResultSlotIndex();
//     }
// }

