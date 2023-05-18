package net.melon.slabs.rei_integration;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.melon.slabs.screens.JuicerScreen;
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
            boolean hasMaterials = true;

            //necessary materials for this recipe
            List<ItemStack> necessaryMaterials = new ArrayList<ItemStack>();
            //start with necessary glass bottle
            necessaryMaterials.add(new ItemStack(Items.GLASS_BOTTLE, 1));
            //check display to get necessary materials

            List<EntryIngredient> inputs = context.getDisplay().getInputEntries();

            //for each item in inputs
            inputs.forEach(entryIngredient -> {
                entryIngredient.forEach(entryStack ->{
                    //make sure it is an item entry (not really necessary because we are dealing with juicer recipes only at this point, but whatevs)
                    if (entryStack.getValueType() == ItemStack.class){
                        ItemStack itemStack = (ItemStack) entryStack.getValue();

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
                    }
                
                });

            });

            //for each material type in the necesarryMaterials,
            for( int i = 0; i<necessaryMaterials.size(); i++){

                //count how many of that type you have in combined inventories of screenhandler
                int available = ((JuicerScreen) context.getContainerScreen()).getScreenHandler().getCountOf(necessaryMaterials.get(i).getItem());
                
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
                //move materials into their correct slots using the screenhandler, only crafting one
            } else if (context.isActuallyCrafting() && context.isStackedCrafting()){
                //move materials into their correct slots using the screenhandler, crafting as many as possible given status of inventory
            }

            return new ResultImpl(true, true);

        }
        return (new ResultImpl (applicable));
    }
    
}
