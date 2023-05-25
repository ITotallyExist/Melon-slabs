package net.melon.slabs.packets;

import java.util.List;

import me.shedaniel.rei.api.common.display.DisplaySerializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.melon.slabs.rei_integration.JuicerDisplay;
import net.melon.slabs.rei_integration.common.JuicerDisplaySerializer;
import net.melon.slabs.screens.JuicerScreenHandler;
import net.melon.slabs.utils.MelonSlabsInventoryUtils;
import net.melon.slabs.utils.MelonSlabsRecipeUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

//here we add all the callbacks for when various packets are called for the juicer block
public class JuicerPacketsHandler {

    private static final DisplaySerializer<JuicerDisplay> DISPLAY_SERIALIZER = new JuicerDisplaySerializer();

    public static void sendCraftPacket (JuicerDisplay recipeDisplay, boolean isStackedCrafting){

        NbtCompound recipeNbtCompound = DISPLAY_SERIALIZER.save(new NbtCompound(), recipeDisplay);

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeNbt(recipeNbtCompound);
        buf.writeBoolean(isStackedCrafting);

        ClientPlayNetworking.send(MelonSlabsPackets.JUICER_CRAFT_PACKET, buf);
    }

    public static void recieveCraftPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender sender){
        //read the packet byte buff data here

        ScreenHandler screenHandler = player.currentScreenHandler;

        JuicerDisplay recipeDisplay = DISPLAY_SERIALIZER.read(buf.readNbt());

        boolean isStackedCrafting = buf.readBoolean();
        server.execute(() -> {

            
            //check that this block is, in fact, a juicer block
            if (!(screenHandler instanceof JuicerScreenHandler)){
                System.out.println("Not the proper workstation");

                return;
            }
            
            PlayerInventory playerInventory = player.getInventory();
            
            Inventory juicerInventory = ((JuicerScreenHandler)screenHandler).getJuicerInventory();


            List<ItemStack> recipeInputs = recipeDisplay.getFullInputs();

            System.out.println(recipeInputs);

            //if you can dump the inventory, then do.  If you cant, then immediately return
            int[] slotsToDump = {0,1,2};

            if (!MelonSlabsInventoryUtils.dumpInventory(juicerInventory, playerInventory, slotsToDump)){
                System.out.println("Not enough space in inventory");

                return;
            }
            
            //check if player can craft
            int possibleCrafts = MelonSlabsRecipeUtils.possibleCrafts(recipeInputs, playerInventory);
            if (possibleCrafts <= 0){
                System.out.println("Not enough resources to craft");

                return;
            }
            

            if (isStackedCrafting){
                //for each ingredient slot                    
                for (int i=0; i<recipeInputs.size()-1; i++){
                    //if not empty
                    if (!recipeInputs.get(i).isEmpty()){
                        final Item item = recipeInputs.get(i).getItem();

                            //remove from player inventory
                        ItemStack newStack = new ItemStack(item, Inventories.remove(playerInventory, itemStack -> {return itemStack.isOf(item);}, possibleCrafts, false));

                        juicerInventory.setStack(i, newStack);
                    }
                }
                    

                //if glass bottle slot doesnt have glass bottles in it
                if (!juicerInventory.getStack(3).isOf(Items.GLASS_BOTTLE)){
                    //remove from player inventory
                    ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE, Inventories.remove(playerInventory, itemStack -> {return itemStack.isOf(Items.GLASS_BOTTLE);}, possibleCrafts, false));

                    juicerInventory.setStack(3, newStack);

                } else if (juicerInventory.getStack(3).getCount() < possibleCrafts){
                    int bottleCount = juicerInventory.getStack(3).getCount();

                    ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE, bottleCount + Inventories.remove(playerInventory, itemStack -> {return itemStack.isOf(Items.GLASS_BOTTLE);}, possibleCrafts - bottleCount, false));
                    
                    //add to juicer inventory
                    juicerInventory.setStack(3, newStack);
                }
            } else {
                        
                //put one of each material into slot
                //for each input
                for (int i=0; i<recipeInputs.size()-1; i++){
                    //if not empty
                    if (!recipeInputs.get(i).isEmpty()){
                        Item item = recipeInputs.get(i).getItem();

                        //remove from player inventory
                        ItemStack newStack = new ItemStack(item, Inventories.remove(playerInventory, itemStack -> {return itemStack.isOf(item);}, 1, false));
                        //add to juicer inventory
                        juicerInventory.setStack(i, newStack);               
                    }
                }
                    

                //if glass bottle slot doesnt have glass bottles in it
                if (!juicerInventory.getStack(3).isOf(Items.GLASS_BOTTLE)){
                    //remove from player inventory
                    //remove from player inventory
                    ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE, Inventories.remove(playerInventory, itemStack -> {return itemStack.isOf(Items.GLASS_BOTTLE);}, 1, false));

                    juicerInventory.setStack(3, newStack);
                }
            }

            //after moving the items around, server opens the crafter for the player
            // NamedScreenHandlerFactory screenHandlerFactory = world.getBlockState(juicerPos).createScreenHandlerFactory(world, juicerPos);

            // if (screenHandlerFactory != null) {
            //     //With this call the server will request the client to open the appropriate Screenhandler
            //     player.openHandledScreen(screenHandlerFactory);
            // }
        });
    }
}
