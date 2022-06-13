package net.melon.slabs.entities;

import net.melon.slabs.screens.JuicerInventory;
import net.melon.slabs.screens.JuicerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import org.jetbrains.annotations.Nullable;

public class JuicerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, JuicerInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
 
    private ScreenHandler handler;

    public JuicerBlockEntity(BlockPos pos, BlockState state) {
        super(MelonSlabsEntities.JUICER_BLOCK_ENTITY, pos, state);
    }
 
 
    //From the ImplementedInventory Interface
 
    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
 
    }
 
    //These Methods are from the NamedScreenHandlerFactory Interface
    //createMenu creates the ScreenHandler itself
    //getDisplayName will Provide its name which is normally shown at the top
 
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        this.handler = new JuicerScreenHandler(syncId, playerInventory, this);
        markDirty();//this makes it so that the crafting recipe result appears to you one you open the menu, because it is not always marked as dirty (to prevent infinite loops), we just refresh it here just in case
        return this.handler;
    }
 
    @Override
    public void markDirty() {
        super.markDirty();
        this.handler.onContentChanged(this);
    }

    public void clearCraftingResult(){
        this.inventory.set(4, ItemStack.EMPTY);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
 
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }
 
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 4) {
            return false;
        }
        if (slot == 3) {
            return stack.isOf(Items.GLASS_BOTTLE);
        }
        return true;
    }

    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }
}