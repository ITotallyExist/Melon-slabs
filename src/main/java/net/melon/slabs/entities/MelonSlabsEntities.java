package net.melon.slabs.entities;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.melon.slabs.entities.tortured_phantom.TorturedPhantomEntity;
import net.melon.slabs.entities.tortured_phantom.TorturedPhantomEntityRenderer;
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

    public static EntityType<TorturedPhantomEntity> TORTURED_PHANTOM; 


    public static void registerEntities(){
        JUICER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "melonslabs:juicer_block_entity", FabricBlockEntityTypeBuilder.create(JuicerBlockEntity::new, MelonSlabsBlocks.JUICER).build(null));
        
        TORTURED_SOUL =  Registry.register(Registries.ENTITY_TYPE, "melonslabs:tortured_soul", Builder.<TorturedSoulEntity>create(TorturedSoulEntity::new, SpawnGroup.MISC).setDimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(10).build("tortured_soul"));
        
        TORTURED_PHANTOM = Registry.register(Registries.ENTITY_TYPE, "melonslabs:tortured_phantom", Builder.<TorturedPhantomEntity>create(TorturedPhantomEntity::new, SpawnGroup.MONSTER).setDimensions(0.9f, 0.5f).maxTrackingRange(8).build("tortured_phantom"));
    }

    public static void registerEntityAttributes(){
        FabricDefaultAttributeRegistry.register(TORTURED_PHANTOM, TorturedPhantomEntity.createMobAttributes());
    }

    public static void registerEntityRenderers(){
        EntityRendererRegistry.register(TORTURED_SOUL, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(TORTURED_PHANTOM, TorturedPhantomEntityRenderer::new);
    }

}
