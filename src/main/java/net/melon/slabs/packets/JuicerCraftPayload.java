package net.melon.slabs.packets;

import net.minecraft.network.packet.CustomPayload;

//Create a custom payload that handles juicer shift-crafting (with REI i think? or maybe just vanilla shift too)
public class JuicerCraftPayload implements CustomPayload {

    @Override
    public Id<? extends CustomPayload> getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }
    
}
