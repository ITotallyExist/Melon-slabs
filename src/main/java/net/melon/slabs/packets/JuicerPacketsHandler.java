package net.melon.slabs.packets;

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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
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

        BlockPos juicerPos = buf.readBlockPos();

        JuicerDisplay recipeDisplay = DISPLAY_SERIALIZER.read(buf.readNbt());

        boolean isStackedCrafting = buf.readBoolean();
        server.execute(() ->{

            ServerWorld world = player.getWorld();
            //TODO: REFACTOR: do server-side verification that this is possible before all the moving
                //in doing that, maybe make an iscraftpossible function that takes in two inventories (the juicer and the player) 
                    //and refactor transferhandler to use that instead of its internal code (i.e. move internal code there)
            
            //check that this block is, in fact, a juicer block
            if (!world.getBlockState(juicerPos).isOf(MelonSlabsBlocks.JUICER)){
                return;
            }
            
            Inventory playerInventory = player.getInventory();
            
            DefaultedList<ItemStack> juicerInventory = ((JuicerBlockEntity) world.getBlockEntity(juicerPos)).getItems() ;

            if (isStackedCrafting){
                
                     else if (context.isActuallyCrafting() && context.isStackedCrafting()){//move materials into their correct slots using the screenhandler, crafting as many as possible given status of inventory
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
    
            } else {
                //empty out juicer (except for glass bottles)
                MelonSlabsInventoryUtils.dumpInventory(juicerInventory, playerInventory, [0,1,2]);
                    
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
