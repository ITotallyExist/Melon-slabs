package net.melon.slabs.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemConvertible;

@Mixin(ComposterBlock.class)
public class ComposterMixin{

    @Shadow @Final public static Object2FloatMap<ItemConvertible> ITEM_TO_LEVEL_INCREASE_CHANCE;

    @Inject(at = @At("HEAD"), method = "registerDefaultCompostableItems")
    private static void registerDefaultCompostableITems(final CallbackInfo info) {
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.CARVED_MELON.asItem(), 0.65f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.MELON_SLAB.asItem(), 0.55f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.MELON_SLAB_ALMOST_EMPTY.asItem(), 0.55f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.MELON_SLAB_ALMOST_EMPTY.asItem(), 0.55f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.MELON_RIND.asItem(), 0.55f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.CARVED_MELON_SLAB.asItem(), 0.55f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.MELON_STAIRS.asItem(), 0.6f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.PUMPKIN_SLAB.asItem(), 0.55f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.CARVED_PUMPKIN_SLAB.asItem(), 0.55f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.PUMPKIN_STAIRS.asItem(), 0.6f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.PUMPKIN_SLICE.asItem(), 0.5f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.COOKED_PUMPKIN_SLICE.asItem(), 0.3f);
        ITEM_TO_LEVEL_INCREASE_CHANCE.put(MelonSlabsItems.CACTUS_SLAB.asItem(), 0.3f);
    }
}