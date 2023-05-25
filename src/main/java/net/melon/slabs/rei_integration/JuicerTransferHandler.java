package net.melon.slabs.rei_integration;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.melon.slabs.packets.JuicerPacketsHandler;
import net.melon.slabs.screens.JuicerScreen;
import net.melon.slabs.screens.JuicerScreenHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class JuicerTransferHandler implements TransferHandler{

    @Override
    public Result handle(Context context) {
        boolean applicable;

        applicable =  context.getContainerScreen() instanceof JuicerScreen;

        if (applicable){//if its the right type of screen

            //screen handler
            JuicerScreenHandler screenHandler = ((JuicerScreen) context.getContainerScreen()).getScreenHandler();

            boolean hasMaterials = true;

            //necessary materials for this recipe
            List<ItemStack> necessaryMaterials = new ArrayList<ItemStack>();
            
            //check display to get necessary materials

            List<ItemStack> inputStacks = ((JuicerDisplay)context.getDisplay()).getFullInputs();


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

            List<Slot> slots = screenHandler.slots;
            //for every slot in the juicer (excluding glass bottle slot)
            for (int i=0; i<3; i++){
                if (slots.get(i).hasStack()){
                    emptySlotsNeeded += 1;
                }
            }
            
            hasRoom = screenHandler.getPlayerEmptyCount() >= emptySlotsNeeded;

            if (!hasRoom){
                return (new ResultImpl(Text.translatable("error.melonslabs.juicer.noroom"), 0));
            }

            //we know it was successful, now we have to actually do it (if we are crafting)
            if (context.isActuallyCrafting()){
                JuicerPacketsHandler.sendCraftPacket((JuicerDisplay) context.getDisplay(), context.isStackedCrafting());
                return new ResultImpl(true, true);
            }

        }
        

        return (new ResultImpl (applicable));
    }
    
}
