package net.melon.slabs.items;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class MelonSlabsItems {
        //Item group
        private static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier("melonslabs", "group"), () -> new ItemStack(MelonSlabsItems.MELON_SLAB));

        //Block Items
        public static final Item JILL_O_LANTERN = new BlockItem(MelonSlabsBlocks.JILL_O_LANTERN, new Item.Settings().group(GROUP));
        public static final Item MELON_STAIRS = new BlockItem(MelonSlabsBlocks.MELON_STAIRS, new Item.Settings().group(GROUP));
        public static final Item MELON_SLAB = new BlockItem(MelonSlabsBlocks.MELON_SLAB, new Item.Settings().group(GROUP));
        public static final Item CARVED_MELON_SLAB = new BlockItem(MelonSlabsBlocks.CARVED_MELON_SLAB, new Item.Settings().group(GROUP));
        public static final Item CARVED_MELON = new BlockItem(MelonSlabsBlocks.CARVED_MELON, new Item.Settings().group(GROUP));
        public static final Item JILL_O_SLAB = new BlockItem(MelonSlabsBlocks.JILL_O_SLAB, new Item.Settings().group(GROUP));
        public static final Item CACTUS_SLAB = new BlockItem(MelonSlabsBlocks.CACTUS_SLAB, new Item.Settings().group(GROUP));
        public static final Item PUMPKIN_STAIRS = new BlockItem(MelonSlabsBlocks.PUMPKIN_STAIRS, new Item.Settings().group(GROUP));
        public static final Item PUMPKIN_SLAB = new BlockItem(MelonSlabsBlocks.PUMPKIN_SLAB, new Item.Settings().group(GROUP));
        public static final Item CARVED_PUMPKIN_SLAB = new BlockItem(MelonSlabsBlocks.CARVED_PUMPKIN_SLAB, new Item.Settings().group(GROUP));
        public static final Item JACK_O_SLAB = new BlockItem(MelonSlabsBlocks.JACK_O_SLAB, new Item.Settings().group(GROUP));
        public static final Item FRANKENMELON = new BlockItem(MelonSlabsBlocks.FRANKENMELON, new Item.Settings().group(GROUP));
        public static final Item JUICER = new BlockItem(MelonSlabsBlocks.JUICER, new Item.Settings().group(GROUP));

        //Food Items
        public static final Item PUMPKIN_SLICE = new Item(new Item.Settings().group(GROUP).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.3f).snack().build()));
        public static final Item COOKED_PUMPKIN_SLICE = new Item(new Item.Settings().group(GROUP).food(new FoodComponent.Builder().hunger(4).saturationModifier(0.6f).build()));

        //juices
        public static final Item MELON_JUICE = new JuiceItem(new Item.Settings().maxCount(16).group(GROUP).recipeRemainder(Items.GLASS_BOTTLE).food(new FoodComponent.Builder().hunger(6).saturationModifier(0.3f).build()));                                                                                                             
        public static final Item APPLE_JUICE = new JuiceItem(new Item.Settings().maxCount(16).recipeRemainder(Items.GLASS_BOTTLE).group(GROUP).food(new FoodComponent.Builder().hunger(8).saturationModifier(0.3f).build()));
        //twice the absorption, regen lasts 1.5x as long                                                                                                                        
        public static final Item GOLDEN_APPLE_JUICE = new JuiceItem(new Item.Settings().maxCount(4).recipeRemainder(Items.GLASS_BOTTLE).group(GROUP).rarity(Rarity.RARE).food(new FoodComponent.Builder().hunger(8).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 150, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 1), 1.0f).alwaysEdible().build()));
        //1.5x the absorption, regen lasts twice as long, resistance 2 instead of 1, fire resistance last 1.5x as long, 33% chance of giving you wither that lasts as long as the regen
        public static final Item ENCHANTED_GOLDEN_APPLE_JUICE = new GlintedJuiceItem(new Item.Settings().maxCount(4).recipeRemainder(Items.GLASS_BOTTLE).group(GROUP).rarity(Rarity.EPIC).food(new FoodComponent.Builder().hunger(8).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 800, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 9000, 0), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 7), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1600, 0), 0.33f).alwaysEdible().build()));

        //tortured soul
        public static final Item TORTURED_SOUL = new TorturedSoulItem(new Item.Settings().maxCount(1).group(GROUP).rarity(Rarity.RARE).recipeRemainder(Items.GLASS_BOTTLE));                                                                                                             

        //register items
        public static void registerItems(){
            Registry.register(Registry.ITEM, "melonslabs:cactus_slab", CACTUS_SLAB);
            Registry.register(Registry.ITEM, "melonslabs:jill_o_lantern", JILL_O_LANTERN);
            Registry.register(Registry.ITEM, "melonslabs:carved_melon", CARVED_MELON);
            Registry.register(Registry.ITEM, "melonslabs:melon_stairs", MELON_STAIRS);
            Registry.register(Registry.ITEM, "melonslabs:melon_slab", MELON_SLAB);
            Registry.register(Registry.ITEM, "melonslabs:carved_melon_slab", CARVED_MELON_SLAB);
            Registry.register(Registry.ITEM, "melonslabs:jill_o_slab", JILL_O_SLAB);
            Registry.register(Registry.ITEM, "melonslabs:pumpkin_stairs", PUMPKIN_STAIRS);
            Registry.register(Registry.ITEM, "melonslabs:pumpkin_slab", PUMPKIN_SLAB);
            Registry.register(Registry.ITEM, "melonslabs:carved_pumpkin_slab", CARVED_PUMPKIN_SLAB);
            Registry.register(Registry.ITEM, "melonslabs:jack_o_slab", JACK_O_SLAB);
            Registry.register(Registry.ITEM, "melonslabs:pumpkin_slice", PUMPKIN_SLICE);
            Registry.register(Registry.ITEM, "melonslabs:cooked_pumpkin_slice", COOKED_PUMPKIN_SLICE);
            Registry.register(Registry.ITEM, "melonslabs:frankenmelon", FRANKENMELON);
            Registry.register(Registry.ITEM, "melonslabs:juicer", JUICER);
            Registry.register(Registry.ITEM, "melonslabs:melon_juice", MELON_JUICE);
            Registry.register(Registry.ITEM, "melonslabs:apple_juice", APPLE_JUICE);
            Registry.register(Registry.ITEM, "melonslabs:golden_apple_juice", GOLDEN_APPLE_JUICE);
            Registry.register(Registry.ITEM, "melonslabs:enchanted_golden_apple_juice", ENCHANTED_GOLDEN_APPLE_JUICE);
            Registry.register(Registry.ITEM, "melonslabs:tortured_soul", TORTURED_SOUL);
        }
}