package net.melon.slabs.blocks;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MelonSlabsBlocks {
    public static final Block MELON_SLAB = new MelonSlab();
    public static final Block MELON_SLAB_ALMOST_FULL = new MelonSlabAlmostFull();
    public static final Block MELON_SLAB_ALMOST_EMPTY = new MelonSlabAlmostEmpty();
    public static final Block MELON_RIND = new MelonRind();
    public static final Block JILL_O_LANTERN = new JillOLantern();
    public static final Block CARVED_MELON = new CarvedMelon();
    public static final Block CARVED_MELON_SLAB = new CarvedMelonSlab();
    public static final Block JILL_O_SLAB = new JillOSlab();
    public static final Block PUMPKIN_SLAB = new PumpkinSlab();
    public static final Block CARVED_PUMPKIN_SLAB = new CarvedPumpkinSlab();
    public static final Block JACK_O_SLAB = new JackOSlab();
    public static final Block PUMPKIN_STAIRS = new PumpkinStairs();
    public static final Block CACTUS_SLAB = new CactusSlab();
    public static final Block MELON_STAIRS = new MelonStairs();
    public static final Block FRANKENMELON = new FrankenMelon();
    public static final Block JUICER = new Juicer();

    public static void registerBlocks(){
        Registry.register(Registries.BLOCK, "melonslabs:cactus_slab", CACTUS_SLAB);
        Registry.register(Registries.BLOCK, "melonslabs:jill_o_lantern", JILL_O_LANTERN);
        Registry.register(Registries.BLOCK, "melonslabs:carved_melon", CARVED_MELON);
        Registry.register(Registries.BLOCK, "melonslabs:melon_stairs", MELON_STAIRS);
        Registry.register(Registries.BLOCK, "melonslabs:melon_slab", MELON_SLAB);
        Registry.register(Registries.BLOCK, "melonslabs:melon_slab_almost_full", MELON_SLAB_ALMOST_FULL);
        Registry.register(Registries.BLOCK, "melonslabs:melon_slab_almost_empty", MELON_SLAB_ALMOST_EMPTY);
        Registry.register(Registries.BLOCK, "melonslabs:melon_rind", MELON_RIND);
        Registry.register(Registries.BLOCK, "melonslabs:carved_melon_slab", CARVED_MELON_SLAB);
        Registry.register(Registries.BLOCK, "melonslabs:jill_o_slab", JILL_O_SLAB);
        Registry.register(Registries.BLOCK, "melonslabs:pumpkin_stairs", PUMPKIN_STAIRS);
        Registry.register(Registries.BLOCK, "melonslabs:pumpkin_slab", PUMPKIN_SLAB);
        Registry.register(Registries.BLOCK, "melonslabs:carved_pumpkin_slab", CARVED_PUMPKIN_SLAB);
        Registry.register(Registries.BLOCK, "melonslabs:jack_o_slab", JACK_O_SLAB);
        Registry.register(Registries.BLOCK, "melonslabs:frankenmelon", FRANKENMELON);
        Registry.register(Registries.BLOCK, "melonslabs:juicer", JUICER);
    }

    public static void putRenderLayers(){
        BlockRenderLayerMap.INSTANCE.putBlock(MelonSlabsBlocks.CACTUS_SLAB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MelonSlabsBlocks.JUICER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MelonSlabsBlocks.JACK_O_SLAB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MelonSlabsBlocks.JILL_O_SLAB, RenderLayer.getCutout());
    }
}