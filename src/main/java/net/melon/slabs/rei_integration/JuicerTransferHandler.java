package net.melon.slabs.rei_integration;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.melon.slabs.screens.JuicerScreen;
import net.melon.slabs.screens.JuicerScreenHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class JuicerTransferHandler implements TransferHandler{

    @Override
    public Result handle(Context context) {
        boolean applicable;
        boolean successful = false;

        applicable =  context.getContainerScreen() instanceof JuicerScreen;

        if (applicable){//if its the right type of screen

            //screen handler
            JuicerScreenHandler screenHandler = ((JuicerScreen) context.getContainerScreen()).getScreenHandler();

            boolean hasMaterials = true;

            //necessary materials for this recipe
            List<ItemStack> necessaryMaterials = new ArrayList<ItemStack>();
            //start with necessary glass bottle
            necessaryMaterials.add(new ItemStack(Items.GLASS_BOTTLE, 1));
            //check display to get necessary materials

            List<EntryIngredient> inputs = context.getDisplay().getInputEntries();

            List<ItemStack> incompleteInputStacks = new ArrayList<ItemStack>();

            inputs.forEach(entryIngredient -> {
                entryIngredient.forEach(entryStack ->{
                    //make sure it is an item entry (not really necessary because we are dealing with juicer recipes only at this point, but whatevs)
                    if (entryStack.getValueType() == ItemStack.class){
                        incompleteInputStacks.add((ItemStack) entryStack.getValue());
                    }

                    if (entryStack.isEmpty()){
                        incompleteInputStacks.add(ItemStack.EMPTY);
                    }
                
                });

            });

            //add empty ingredients
            Integer numRecipeInputs = incompleteInputStacks.size();
            List<ItemStack> inputStacks = new ArrayList<ItemStack>();

            if (numRecipeInputs == 0){
                inputStacks.add(ItemStack.EMPTY);
                inputStacks.add(ItemStack.EMPTY);
                inputStacks.add(ItemStack.EMPTY);
            } else if (numRecipeInputs == 1){
                inputStacks.add(ItemStack.EMPTY);
                inputStacks.add(incompleteInputStacks.get(0));
                inputStacks.add(ItemStack.EMPTY);
            } else if (numRecipeInputs == 2){
                inputStacks.add(ItemStack.EMPTY);
                inputStacks.add(incompleteInputStacks.get(1));
                inputStacks.add(incompleteInputStacks.get(0));
            } else {
                inputStacks= incompleteInputStacks;
            }

            //for each item in inputs
            inputStacks.forEach(itemStack -> {
                //wether we have added it to the list of necessary materials yet
                boolean accountedFor = false;

                //for every necessary material added so far
                for( int i = 0; i<necessaryMaterials.size(); i++){

                    ItemStack accountedItemStack = necessaryMaterials.get(i);
                    //see if it matches the new itemstack
                    if (accountedItemStack.isOf(itemStack.getItem())){

                        //if it does, add  to its count
                        accountedItemStack.increment(itemStack.getCount());
                        
                        //make sure not to double add
                        accountedFor = true;
                        break;
                    }
                }

                if (!accountedFor){//if we couldnt find an existing itemstack to add it to
                    //add itemstack to necessarymaterials

                    necessaryMaterials.add(new ItemStack(itemStack.getItem(), itemStack.getCount()));
                }                
            });

            //for each material type in the necesarryMaterials,
            for( int i = 0; i<necessaryMaterials.size(); i++){

                //count how many of that type you have in combined inventories of screenhandler
                int available = screenHandler.getCountOf(necessaryMaterials.get(i).getItem());
                
                //if we dont have enough of material for recipe
                if (available < necessaryMaterials.get(i).getCount()){
                    hasMaterials = false;
                    break; 
                }
            }
            
                
            

            if (! hasMaterials){
                return (new ResultImpl(Text.translatable("error.melonslabs.juicer.nomaterials"), 0));
            }

            boolean hasRoom = false;
            //check screenhandler to see if there are enough slots to move stuff out of juicer so you can craft in juicer
                //dont condense stacks
            int emptySlotsNeeded = 0;

            List<Slot> slots = context.getContainerScreen().getScreenHandler().slots;
            //for every slot in the juicer (excluding glass bottle slot)
            for (int i=0; i<3; i++){
                if (slots.get(i).hasStack()){
                    emptySlotsNeeded += 1;
                }
            }
            
            hasRoom = ((JuicerScreen) context.getContainerScreen()).getScreenHandler().getPlayerEmptyCount() >= emptySlotsNeeded;

            if (!hasRoom){
                return (new ResultImpl(Text.translatable("error.melonslabs.juicer.noroom"), 0));
            }

            //we know it was successful, now we have to actually do it (if we are crafting)
            if (context.isActuallyCrafting() && !context.isStackedCrafting()){
                //empty out juicer (except for glass bottles)
                screenHandler.emptyCraftingSlots();

                //put one of each material into slot

                //for each input
                for (int i=0; i<3; i++){
                    //if not empty
                    if (!inputStacks.get(i).isEmpty()){
                        Item item = inputStacks.get(i).getItem();

                        //remove from player inventory
                        screenHandler.removeFromPlayer(item, 1);
                        //add to juicer inventory

                        screenHandler.slots.get(i).setStack(new ItemStack(item, 1));
                        screenHandler.slots.get(i).markDirty();
                    }
                }
                    

                //if glass bottle slot doesnt have glass bottles in it
                if (!screenHandler.slots.get(3).getStack().isOf(Items.GLASS_BOTTLE)){
                    //remove from player inventory
                    screenHandler.removeFromPlayer(Items.GLASS_BOTTLE, 1);

                    //add to juicer inventory
                    screenHandler.slots.get(3).setStack(new ItemStack(Items.GLASS_BOTTLE, 1));
                    screenHandler.slots.get(3).markDirty();
                }
            } else if (context.isActuallyCrafting() && context.isStackedCrafting()){//move materials into their correct slots using the screenhandler, crafting as many as possible given status of inventory
                //empty out juicer (except for glass bottles)
                screenHandler.emptyCraftingSlots();
                
                //get the maximum number of crafts to set up
                int maxCrafts = Math.min(screenHandler.getCountOf(Items.GLASS_BOTTLE, false), Items.GLASS_BOTTLE.getMaxCount());

                for (int i = 0; i<necessaryMaterials.size(); i++){
                    if (!necessaryMaterials.get(i).isEmpty()){
                        maxCrafts = Math.min(maxCrafts, screenHandler.getCountOf(necessaryMaterials.get(i).getItem(), true)/necessaryMaterials.get(i).getCount());
                        maxCrafts = Math.min(maxCrafts, necessaryMaterials.get(i).getItem().getMaxCount());
                    }
                }

                for (int i=0; i<3; i++){
                    //if not empty
                    if (!inputStacks.get(i).isEmpty()){
                        Item item = inputStacks.get(i).getItem();


                        //remove from player inventory
                        screenHandler.removeFromPlayer(item, maxCrafts);
                        //add to juicer inventory

                        screenHandler.slots.get(i).setStack(new ItemStack(item, maxCrafts));
                        screenHandler.slots.get(i).markDirty();
                    }
                }
                    

                //if glass bottle slot doesnt have glass bottles in it
                if (!screenHandler.slots.get(3).getStack().isOf(Items.GLASS_BOTTLE)){
                    //remove from player inventory
                    screenHandler.removeFromPlayer(Items.GLASS_BOTTLE, maxCrafts);

                    //add to juicer inventory
                    screenHandler.slots.get(3).setStack(new ItemStack(Items.GLASS_BOTTLE, maxCrafts));
                    screenHandler.slots.get(3).markDirty();
                } else if (screenHandler.slots.get(3).getStack().getCount() < maxCrafts){
                    screenHandler.removeFromPlayer(Items.GLASS_BOTTLE, maxCrafts - screenHandler.slots.get(3).getStack().getCount());

                    //add to juicer inventory
                    screenHandler.slots.get(3).setStack(new ItemStack(Items.GLASS_BOTTLE, maxCrafts));
                    screenHandler.slots.get(3).markDirty();
                }
            }

            return new ResultImpl(true, true);

        }
        return (new ResultImpl (applicable));
    }
    
}
