package net.melon.slabs.utils;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MelonSlabsInventoryUtils {

    //TODO: all of these

    //empties all the slots from inventory A into inventory B, returns false if not possible, true if possible and successful
    public static boolean dumpInventory (List<ItemStack> inventoryA, List<ItemStack> inventoryB){
        return dumpInventory(inventoryA, inventoryB, null);
    }

    //empties all the specified slots inventory A into inventory B, returns false if not possible, true if possible and successful
    public static boolean dumpInventory (List<ItemStack> inventoryA, List<ItemStack> inventoryB, int[] slots){
        return false;
    }

    //empties one specified slot from inventory A into inventory B, returns false if not possible, true if possible and successful
    public static boolean dumpStack (List<ItemStack> inventoryA, List<ItemStack> inventoryB, int slots){
        return false;
    }



    public static int getCount (Item itemType){
        return 0;
    }
}
