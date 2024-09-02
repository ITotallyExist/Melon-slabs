package net.melon.slabs;

import net.fabricmc.api.ClientModInitializer;

import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.melon.slabs.entities.MelonSlabsEntities;
//import net.melon.slabs.packets.MelonSlabsPackets;
import net.melon.slabs.screens.MelonSlabsScreens;

public class MelonSlabsClient implements ClientModInitializer {
    //mod id
    public static final String MOD_ID = "melonslabs";

    @Override
    public void onInitializeClient() {
 
        MelonSlabsBlocks.putRenderLayers();

        MelonSlabsScreens.registerScreensAndRecipesClient();

        //MelonSlabsPackets.registerClientPackets();

        MelonSlabsEntities.registerEntityRenderers();
    }
}