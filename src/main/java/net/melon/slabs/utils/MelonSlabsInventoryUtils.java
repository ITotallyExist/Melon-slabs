package net.melon.slabs.utils;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class MelonSlabsInventoryUtils {

    //empties all the slots from inventory A into inventory B, returns false if not possible, true if possible and successful
    public static boolean dumpInventory (Inventory inventory, PlayerInventory playerInventory){
        throw new UnsupportedOperationException();

        //int[] slots = {};
        //return dumpInventory(inventory, playerInventory, slots);
    }

    //empties all the specified slots inventory A into inventory B, returns false if not possible, true if possible and successful
    public static boolean dumpInventory (Inventory inventory, PlayerInventory playerInventory, int[] slots){
        for (int i=0; i<slots.length; i++){
            if (!inventory.getStack(slots[i]).isEmpty()){
                if (!dumpStack(inventory, playerInventory, slots[i])){
                    return false;
                }
            }
        }
        
        return true;
    }

    //empties one specified slot from inventory A into inventory B, returns false if not possible, true if possible and successful
    public static boolean dumpStack (Inventory inventory, PlayerInventory playerInventory, int slot){
        boolean possible = playerInventory.insertStack(inventory.getStack(slot));

        if (possible){
            inventory.setStack(slot, ItemStack.EMPTY);
        }

        return possible;
    }
}
