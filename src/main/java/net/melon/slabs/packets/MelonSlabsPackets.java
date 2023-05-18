package net.melon.slabs.packets;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class MelonSlabsPackets {
    //packets sent from client
    public static final Identifier JUICER_CRAFT_PACKET = new Identifier("melonslabs", "juicer_craft");


    public static void registerServerPackets(){
        ServerPlayNetworking.registerGlobalReceiver(JUICER_CRAFT_PACKET,  (server, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) ->{
            JuicerPacketsHandler.recieveCraftPacket(server, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender);
        });
    }

    public static void registerClientPackets(){

    }
}
