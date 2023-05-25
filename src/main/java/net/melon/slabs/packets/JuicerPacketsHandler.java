package net.melon.slabs.packets;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.rei.api.common.display.DisplaySerializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.melon.slabs.entities.JuicerBlockEntity;
import net.melon.slabs.rei_integration.JuicerDisplay;
import net.melon.slabs.rei_integration.common.JuicerDisplaySerializer;
import net.melon.slabs.screens.JuicerInventory;
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
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

//here we add all the callbacks for when various packets are called for the juicer block
public class JuicerPacketsHandler {

    private static final DisplaySerializer<JuicerDisplay> DISPLAY_SERIALIZER = new JuicerDisplaySerializer();

    public static void sendCraftPacket (BlockPos pos, JuicerDisplay recipeDisplay, boolean isStackedCrafting){

        NbtCompound recipeNbtCompound = DISPLAY_SERIALIZER.save(new NbtCompound(), recipeDisplay);

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBlockPos(pos);
        buf.writeNbt(recipeNbtCompound);
        buf.writeBoolean(isStackedCrafting);

        ClientPlayNetworking.send(MelonSlabsPackets.JUICER_CRAFT_PACKET, buf);
    }

    public static void recieveCraftPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender sender){
        //read the packet byte buff data here

        System.out.println("hi!");

        BlockPos juicerPosOld = buf.readBlockPos();
        BlockPos juicerPos = new BlockPos(459, 64, 150);

        JuicerDisplay recipeDisplay = DISPLAY_SERIALIZER.read(buf.readNbt());

        boolean isStackedCrafting = buf.readBoolean();
        server.execute(() -> {

            ServerWorld world = player.getWorld();
            //TODO: REFACTOR: do server-side verification that this is possible before all the moving
                //in doing that, maybe make an iscraftpossible function that takes in two inventories (the juicer and the player) 
                    //and refactor transferhandler to use that instead of its internal code (i.e. move internal code there)
            
            //check that this block is, in fact, a juicer block
            if (!world.getBlockState(juicerPos).isOf(MelonSlabsBlocks.JUICER)){
                System.out.println("l moment!");

                return;
            }
            
            PlayerInventory playerInventory = player.getInventory();
            
            JuicerBlockEntity juicerBlockEntity = ((JuicerBlockEntity) world.getBlockEntity(juicerPos));
            DefaultedList<ItemStack> juicerInventoryDefaulted = juicerBlockEntity.getItems();
            List<ItemStack> juicerInventory = new ArrayList<ItemStack>(juicerInventoryDefaulted);


            List<ItemStack> recipeInputs = recipeDisplay.getFullInputs();

            System.out.println(recipeInputs);

            //if you can dump the inventory, then do.  If you cant, then immediately return
            int[] slotsToDump = {0,1,2};

            if (!MelonSlabsInventoryUtils.dumpInventory(juicerInventory, playerInventory, slotsToDump)){
                System.out.println("woops!");

                return;
            }
            
            //check if player can craft
            int possibleCrafts = MelonSlabsRecipeUtils.possibleCrafts(recipeInputs, playerInventory);
            if (possibleCrafts <= 0){
                System.out.println("f in the chat");

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

                        juicerInventoryDefaulted.set(i, newStack);
                    }
                }
                    

                //if glass bottle slot doesnt have glass bottles in it
                if (!juicerInventory.get(3).isOf(Items.GLASS_BOTTLE)){
                    //remove from player inventory
                    ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE, Inventories.remove(playerInventory, itemStack -> {return itemStack.isOf(Items.GLASS_BOTTLE);}, possibleCrafts, false));

                    juicerInventoryDefaulted.set(3, newStack);

                } else if (juicerInventory.get(3).getCount() < possibleCrafts){
                    int bottleCount = juicerInventory.get(3).getCount();

                    ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE, bottleCount + Inventories.remove(playerInventory, itemStack -> {return itemStack.isOf(Items.GLASS_BOTTLE);}, possibleCrafts - bottleCount, false));
                    
                    //add to juicer inventory
                    juicerInventoryDefaulted.set(3, newStack);
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
                        juicerInventoryDefaulted.set(i, newStack);               
                    }
                }
                    

                //if glass bottle slot doesnt have glass bottles in it
                if (!juicerInventory.get(3).isOf(Items.GLASS_BOTTLE)){
                    //remove from player inventory
                    //remove from player inventory
                    ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE, Inventories.remove(playerInventory, itemStack -> {return itemStack.isOf(Items.GLASS_BOTTLE);}, 1, false));

                    juicerInventoryDefaulted.set(3, newStack);
                }
            }

            //after moving the items around, server opens the crafter for the player
            NamedScreenHandlerFactory screenHandlerFactory = world.getBlockState(juicerPos).createScreenHandlerFactory(world, juicerPos);

            if (screenHandlerFactory != null) {
                //With this call the server will request the client to open the appropriate Screenhandler
                player.openHandledScreen(screenHandlerFactory);
            }
        });
    }
}
