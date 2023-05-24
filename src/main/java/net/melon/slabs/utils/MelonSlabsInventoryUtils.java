package net.melon.slabs.utils;

import java.util.List;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MelonSlabsInventoryUtils {

    //empties all the slots from inventory A into inventory B, returns false if not possible, true if possible and successful
    public static boolean dumpInventory (List<ItemStack> inventory, PlayerInventory playerInventory){
        return dumpInventory(inventory, playerInventory, null);
    }

    //empties all the specified slots inventory A into inventory B, returns false if not possible, true if possible and successful
    public static boolean dumpInventory (List<ItemStack> inventory, PlayerInventory playerInventory, int[] slots){
        throw new UnsupportedOperationException();
    }

    //empties one specified slot from inventory A into inventory B, returns false if not possible, true if possible and successful
    public static boolean dumpStack (List<ItemStack> inventory, PlayerInventory playerInventory, int slots){
        throw new UnsupportedOperationException();
    }
}
