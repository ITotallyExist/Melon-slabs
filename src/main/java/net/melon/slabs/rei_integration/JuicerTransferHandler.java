package net.melon.slabs.rei_integration;

import Result;
import java.util.ArrayList;
import java.util.List;

import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.transfer.info.MenuInfo;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.MenuTransferException;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import net.melon.slabs.packets.JuicerPacketsHandler;
import net.melon.slabs.screens.JuicerScreen;
import net.melon.slabs.screens.JuicerScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class JuicerTransferHandler implements TransferHandler{

    @Override
    public Result handle(Context context) {
        boolean applicable;

        Display display = context.getDisplay();
        HandledScreen<?> handledScreen = context.getContainerScreen();
        if (handledScreen == null) {
            return Result.createNotApplicable();
        }
        ScreenHandler menu = context.getMenu();
        MenuInfoContext<ScreenHandler, PlayerEntity, Display> menuInfoContext = ofContext(menu, display);
        MenuInfo<ScreenHandler, Display> menuInfo = MenuInfoRegistry.getInstance().getClient(display, menuInfoContext, menu);
        if (menuInfo == null) {
            return Result.createNotApplicable();
        }
        try {
            menuInfo.validate(menuInfoContext);
        } catch (MenuTransferException e) {
            if (e.isApplicable()) {
                return Result.createFailed(e.getError());
            } else {
                return Result.createNotApplicable();
            }
        }

        if (!ClientHelper.getInstance().canUseMovePackets()) {
            return Result.createFailed(Text.translatable("error.rei.not.on.server"));
        }

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

    private static MenuInfoContext<ScreenHandler, PlayerEntity, Display> ofContext(ScreenHandler menu, Display display) {
        return new MenuInfoContext<ScreenHandler, PlayerEntity, Display>() {
            @Override
            public ScreenHandler getMenu() {
                return menu;
            }
            
            @Override
            public PlayerEntity getPlayerEntity() {
                return MinecraftClient.getInstance().player;
            }
            
            @Override
            public CategoryIdentifier<Display> getCategoryIdentifier() {
                return (CategoryIdentifier<Display>) display.getCategoryIdentifier();
            }
            
            @Override
            public Display getDisplay() {
                return display;
            }
        };
    }
    
}
