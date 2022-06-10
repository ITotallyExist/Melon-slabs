package net.melon.slabs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.melon.slabs.entities.MelonSlabsEntities;
import net.melon.slabs.criteria.MelonSlabsCriteria;
import net.melon.slabs.items.MelonSlabsItems;
import net.melon.slabs.screens.MelonSlabsScreens;
import net.melon.slabs.sounds.MelonSlabsSounds;

public class MelonSlabsClient implements ClientModInitializer {
    //mod id
    public static final String MOD_ID = "melonslabs";

    @Override
    public void onInitializeClient() {
 
            MelonSlabsBlocks.putRenderLayers();

    }
}