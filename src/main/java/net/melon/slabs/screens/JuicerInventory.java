package net.melon.slabs.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;


/**
 * A simple {@code Inventory} implementation with only default methods + an item list getter.
 *
 * Originally by Juuz
 */
//the fourth slot is the bottle slot, the other three are for ingredients
//the fifth slot is the output slot
public interface JuicerInventory extends Inventory {
 
    /**
     * Retrieves the item list of this inventory.
     * Must return the same instance every time it's called.
     */
    DefaultedList<ItemStack> getItems();

    /**
     * Creates an inventory from the item list.
     */
    static JuicerInventory of(DefaultedList<ItemStack> items) {
        if (items.size() != 5){//checks if the list is of the right size
            return empty();
        } else {
            return () -> items;
        }
    }
 
    /**
     * Creates a new inventory 
     */

    static JuicerInventory empty () {
        return of(DefaultedList.ofSize(5, ItemStack.EMPTY));
    }
 
    /**
     * Returns the inventory size.
     */
    @Override
    default int size() {
        return 5;
    }
 
    /**
     * Checks if the inventory is empty.
     * @return true if this inventory has only empty stacks, false otherwise.
     */
    @Override
    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
 
    /**
     * Retrieves the item in the slot.
     */
    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }
 
    /**
     * Removes items from an inventory slot.
     * @param slot  The slot to remove from.
     * @param count How many items to remove. If there are less items in the slot than what are requested,
     *              takes all items in that slot.
     */
    @Override
    default ItemStack removeStack(int slot, int count) {
        //here we do an if slot == 4 thing so that if you are taking from the result it removes the ingredients;

        if (slot != 4){

            ItemStack result = Inventories.splitStack(getItems(), slot, count);
            if (!result.isEmpty()) {
                markDirty();
                //doCrafting();
                //this.handler.onContentChanged(this);
            }
            return result;
        } else {
            return removeStack(slot);
        }
    }
 
    /**
     * Removes all items from an inventory slot.
     * @param slot The slot to remove from.
     */
    @Override
    default ItemStack removeStack(int slot) {
        //here we do an if slot == 4 thing so that if you are taking from the result it removes the ingredients;

        ItemStack result = Inventories.removeStack(getItems(), slot);
        
        if (slot == 4){
            getItems().forEach((item) -> {item.setCount(item.getCount()-1);});
        }

        markDirty();
        return result;
    }
 
    // private void doCrafting(){
    //     Optional<JuicerRecipe> match = world.getRecipeManager()
    //     .getFirstMatch(ExampleRecipe.Type.INSTANCE, inventory, world);
    //     markDirty();
    // }

    /**
     * Replaces the current stack in an inventory slot with the provided stack.
     * @param slot  The inventory slot of which to replace the itemstack.
     * @param stack The replacing itemstack. If the stack is too big for
     *              this inventory ({@link Inventory#getMaxCountPerStack()}),
     *              it gets resized to this inventory's maximum amount.
     */
    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }

        //just to stop infinite loops, we dont mark dirty when its the crafting result
        if (slot != 4){
            markDirty();
        }

    }
 
    /**
     * Clears the inventory.
     */
    @Override
    default void clear() {
        getItems().clear();
    }
 
    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     */ 
    @Override
    default void markDirty() {
        // Override if you want behavior.
    }
 
    /**
     * @return true if the player can use the inventory, false otherwise.
     */ 
    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    //makes it so you can only put glass bottles into slot index 3 and nothing into index 4 (the result slot)
    @Override
    default public boolean isValid(int slot, ItemStack stack) {
        if (slot == 4){//the result slot
            return false;
        } else if (slot == 3 && !stack.isOf(Items.GLASS_BOTTLE)){
            return false;
        }
        return true;
    }
}