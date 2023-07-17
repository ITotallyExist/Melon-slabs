package net.melon.slabs.entities;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;


public class MelonSlabsEntities {
    public static BlockEntityType<JuicerBlockEntity> JUICER_BLOCK_ENTITY;
    
    public static EntityType<TorturedSoulEntity> TORTURED_SOUL; 

 

    public static void registerEntities(){
        JUICER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "melonslabs:juicer_block_entity", FabricBlockEntityTypeBuilder.create(JuicerBlockEntity::new, MelonSlabsBlocks.JUICER).build(null));
        
        TORTURED_SOUL =  Registry.register(Registries.ENTITY_TYPE, "melonslabs:tortured_soul", Builder.<TorturedSoulEntity>create(TorturedSoulEntity::new, SpawnGroup.MISC).setDimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(10).build("tortured_soul"));
    }

    public static void registerEntityRenderers(){
        EntityRendererRegistry.register(TORTURED_SOUL, FlyingItemEntityRenderer::new);
    }

}
