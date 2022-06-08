package net.melon.slabs.entities;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.registry.Registry;

public class MelonSlabsEntities {
    //public static BlockEntityType<JuicerBlockEntity> JUICER_BLOCK_ENTITY;

 

    public static void registerEntities(){
        //JUICER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "melonslabs:juicer_block_entity", FabricBlockEntityTypeBuilder.create(JuicerBlockEntity::new, MelonSlabsBlocks.JUICER).build(null));
    }
}
