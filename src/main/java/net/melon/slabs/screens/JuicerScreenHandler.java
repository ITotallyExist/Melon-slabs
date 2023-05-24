package net.melon.slabs.screens;

import java.util.List;
import java.util.Optional;

import me.shedaniel.rei.impl.client.gui.changelog.JParseDown.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JuicerScreenHandler extends ScreenHandler  {
    private final JuicerInventory inventory;
    private final BlockPos blockPos;
 
    //just to access crafting recipes
    private final World world;

    public JuicerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, JuicerInventory.empty(), BlockPos.ORIGIN);
    }

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public JuicerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, JuicerInventory.empty(), pos);
    }
 
    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public JuicerScreenHandler(int syncId, PlayerInventory playerInventory, JuicerInventory inventory, BlockPos pos) {
        
        super(MelonSlabsScreens.JUICER_SCREEN_HANDLER, syncId);
        this.world = playerInventory.player.world;

        this.blockPos = pos;

        checkSize(inventory, 5);
        this.inventory = inventory;
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);
 
        //places slots
        int m;
        int l;
        //Our inventory
        this.addSlot(new IngredientSlot(inventory, 0, 90, 22));
        this.addSlot(new IngredientSlot(inventory, 1, 71, 17));
        this.addSlot(new IngredientSlot(inventory, 2, 52, 22));

        this.addSlot(new BottleSlot(inventory, 3, 71, 53));
        this.addSlot(new ResultSlot(inventory, 4, 125, 35));

        // for (m = 0; m < 3; ++m) {
        //     for (l = 0; l < 3; ++l) {
        //         this.addSlot(new Slot(inventory, l + m * 3, 62 + l * 18, 17 + m * 18));
        //     }
        // }
        //The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        //The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
 
    }
 
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
 
    public Inventory getJuicerInventory(){
        return this.inventory;
    }

    public BlockPos getBlockPos (){
        return this.blockPos;
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (invSlot == 4){//taking from result slot
                //get the ingredient slot witht the minimum number of items in it
                //as long as that slot is not empty
                int maxTotalCrafts = Integer.MAX_VALUE;

                int slotNum;
                for (slotNum = 0; slotNum < 4; slotNum ++){
                    if (!this.slots.get(slotNum).getStack().isEmpty()){
                        maxTotalCrafts = Math.min(maxTotalCrafts, this.slots.get(slotNum).getStack().getCount());
                    }
                }


                // newStack.setCount(originalStack.getCount()*totalCrafts);
                

                int i;
                for (i=0; i<maxTotalCrafts; i++){
                    if (!this.insertItem(newStack.copy(), this.inventory.size(), this.slots.size(), true)) {
                        final int finalCrafts = i;
                        this.inventory.getItems().forEach((item) -> {item.setCount(item.getCount()-finalCrafts);});
                        this.doCrafting();
                        return ItemStack.EMPTY;
                    }
                }
                
                final int finalTotalCrafts = i;
                this.inventory.getItems().forEach((item) -> {item.setCount(item.getCount()-finalTotalCrafts);});
                this.doCrafting();


            } else {

                if (invSlot < this.inventory.size()) {
                    //this.inventory.size - 1 is because we dont want to insert from results slot
                    if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }

                //this.inventory.size - 1 is because we dont want to insert into results slot
                // } else if (!this.insertItem(newStack, invSlot, invSlot, false)){//if we are trying to insert a glass bottle

                //     return ItemStack.EMPTY;
                    
                } else if (!this.insertItem(originalStack, 0, this.inventory.size()-1, true)) {//if we are trying to insert anything else
                    return ItemStack.EMPTY;
                }
    
                if (originalStack.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                } else {
                    slot.markDirty();
                }
            }
        }
        return newStack;

    }


    @Override
    public void onContentChanged(Inventory inventory) {
        doCrafting();
    }

    //returns how many of a certain item exist in the combined inventories
    public int getCountOf (Item item){
        return this.getCountOf(item, false);
    }

    //if playerOnly, will return only the count of that item type in the player's inventory
    public int getCountOf (Item item, boolean playerOnly){
        int count = 0;

        List<ItemStack> stacks = this.getStacks();

        for (int i = 0; i<stacks.size(); i++){
            if (stacks.get(i).isOf(item)){
                count += stacks.get(i).getCount();
            }
        }

        return count;
    }

    //returns how many empty slots are in the player's inventory
    public int getPlayerEmptyCount(){
        int count = 0;

        for (int i = 5; i<this.slots.size(); i++){
            if (!this.slots.get(i).hasStack()){
                count += 1;
            }
        }

        return count;
    }

    //returns false if there are not enough of that item to remove from the player inventory
    //if there are, it removes them
    public boolean removeFromPlayer (Item item, int count){
        if (this.getCountOf(item, true) < count){
            return false;
        }

        //each of the slots in the player's inventory
        for (int i = 5; i<this.slots.size(); i++){
            ItemStack itemStack = this.slots.get(i).getStack();
            if (itemStack.isOf(item)){

                //this stack alon isnt enough to meet quota
                if (count > itemStack.getCount()){
                    count -= itemStack.getCount();
                    itemStack.decrement(itemStack.getCount());
                    this.slots.get(i).markDirty();
                } else {
                    itemStack.decrement(count);
                    count = 0;
                    this.slots.get(i).markDirty();
                    break;
                }
                
            }
        }

        return true;
    }

    //emtpies the three ingredient slots (not the bottle slot) into the player inventory
    //returns false if this is not possible
        //else true
    public boolean emptyCraftingSlots(){
        int emptySlotsNeeded = 0;

        for (int i=0; i< 3; i++){
            if (this.slots.get(i).hasStack()){
                emptySlotsNeeded ++;
            }
        }

        if (this.getPlayerEmptyCount() < emptySlotsNeeded){
            return false;
        }

        for (int i=0; i< 3; i++){
            if (this.slots.get(i).hasStack()){
                for (int j = 5; j<slots.size(); j++){
                    if (!this.slots.get(j).hasStack()){
                        this.slots.get(j).setStack(this.slots.get(i).getStack());
                        this.slots.get(i).setStack(ItemStack.EMPTY);
                    }
                }
            }
        }

        return true;
    }

    private void doCrafting(){
        //System.out.println("crafting");
        //Optional<JuicerRecipe> match = (Optional<JuicerRecipe>) this.world.getRecipeManager().get(new Identifier("melonslabs",JuicerRecipe.ID)).stream().findFirst();
        Optional<JuicerRecipe> match = this.world.getRecipeManager().getFirstMatch(MelonSlabsScreens.JUICER_RECIPE_TYPE, inventory, world);
        if (match.isPresent()){
            this.slots.get(4).setStack(match.get().getOutput().copy());
            //System.out.println("match");

        } else {
            //System.out.println("nomatch");
            this.slots.get(4).setStack(ItemStack.EMPTY);
        }
    }

    static class IngredientSlot
    extends Slot {
        public IngredientSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }
    }

    static class BottleSlot
    extends Slot {
        public BottleSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return BottleSlot.matches(stack);
        }

        public static boolean matches(ItemStack stack) {
            return stack.isOf(Items.GLASS_BOTTLE);
        }
    }

    static class ResultSlot
    extends Slot {
        public ResultSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        //overriding this slot so that it doesnt mark as dirty weh
        @Override
        public void setStack(ItemStack stack) {
            this.inventory.setStack(4, stack);
            //this.markDirty();
        }
    

        //make it so that you cant put things into the results stack
        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }
    }
}

