package net.melon.slabs.packets;

import me.shedaniel.rei.api.common.display.DisplaySerializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.melon.slabs.rei_integration.JuicerDisplay;
import net.melon.slabs.rei_integration.common.JuicerDisplaySerializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
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

        server.execute(() ->{
            //move the stuff between inventories here
        });
    }
}
