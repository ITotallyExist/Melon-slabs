package net.melon.slabs;

import net.fabricmc.api.ModInitializer;

import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.melon.slabs.entities.MelonSlabsEntities;
import net.melon.slabs.criteria.MelonSlabsCriteria;
import net.melon.slabs.items.MelonSlabsItems;
import net.melon.slabs.packets.MelonSlabsPackets;
import net.melon.slabs.screens.MelonSlabsScreens;
import net.melon.slabs.sounds.MelonSlabsSounds;
import net.melon.slabs.status_effects.MelonSlabsStatusEffects;

public class MelonSlabs implements ModInitializer {
    //mod id
    public static final String MOD_ID = "melonslabs";

    @Override
    public void onInitialize() {
        //advancement criteria
        MelonSlabsCriteria.loadClass();

        //sounds
        MelonSlabsSounds.registerSoundEvents();
        
        //blocks
        MelonSlabsBlocks.registerBlocks();
            //transparent blocks and such
            //MelonSlabsBlocks.putRenderLayers();

        //items
        MelonSlabsItems.registerItemGroups();
        MelonSlabsItems.registerItems();

        //entites (including blockentites)
        MelonSlabsEntities.registerEntities();

        //ui stuff and crafting and recipes
        MelonSlabsScreens.registerScreensAndRecipes();

        //server client communications stuff
        MelonSlabsPackets.registerServerPackets();

        //status effects
        MelonSlabsStatusEffects.registerStatusEffects();

        //damage sources/types/death messages

    }
}