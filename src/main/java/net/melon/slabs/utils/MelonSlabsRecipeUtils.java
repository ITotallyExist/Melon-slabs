package net.melon.slabs.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;

public class MelonSlabsRecipeUtils {

    //returns x < 0 if not possible
    //retutns x if possible where x is max number of crafts
    static public int possibleCrafts(List<ItemStack> recipeInputs, PlayerInventory inventory){
        List<ItemStack> requiredMaterials = new ArrayList<ItemStack>();

        recipeInputs.forEach(recipeItem ->{

            boolean accountedFor = false;

            for( int i = 0; i<requiredMaterials.size(); i++){

                ItemStack accountedItemStack = requiredMaterials.get(i);
                //see if it matches the new itemstack
                if (accountedItemStack.isOf(recipeItem.getItem())){

                    //if it does, add  to its count
                    accountedItemStack.increment(recipeItem.getCount());
                    
                    //make sure not to double add
                    accountedFor = true;
                    break;
                }
            }

            if (!accountedFor){
                requiredMaterials.add(recipeItem);
            }
        });
        
        int maxCount = requiredMaterials.get(0).getMaxCount();

        for (int i=0; i<requiredMaterials.size(); i++){
            final int index = i;
            maxCount = Math.min(maxCount, requiredMaterials.get(i).getMaxCount());
            maxCount = Math.min(maxCount, Inventories.remove(inventory, itemStack -> {return itemStack.isOf(requiredMaterials.get(index).getItem());},requiredMaterials.get(i).getMaxCount(), true)/requiredMaterials.get(i).getCount());

        }

        return maxCount;
    }
}
