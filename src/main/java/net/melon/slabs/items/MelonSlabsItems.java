package net.melon.slabs.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class MelonSlabsItems {
        

        //Block Items
        public static final Item JILL_O_LANTERN = registerItem("jill_o_lantern",new BlockItem(MelonSlabsBlocks.JILL_O_LANTERN, new Item.Settings()));
        public static final Item MELON_STAIRS = registerItem("melon_stairs",new BlockItem(MelonSlabsBlocks.MELON_STAIRS, new Item.Settings()));
        public static final Item MELON_SLAB = registerItem("melon_slab",new BlockItem(MelonSlabsBlocks.MELON_SLAB, new Item.Settings()));
        public static final Item CARVED_MELON_SLAB = registerItem("carved_melon_slab",new BlockItem(MelonSlabsBlocks.CARVED_MELON_SLAB, new Item.Settings()));
        public static final Item CARVED_MELON = registerItem("carved_melon", new BlockItem(MelonSlabsBlocks.CARVED_MELON, new Item.Settings()));
        public static final Item JILL_O_SLAB = registerItem("jill_o_slab",new BlockItem(MelonSlabsBlocks.JILL_O_SLAB, new Item.Settings()));
        public static final Item CACTUS_SLAB = registerItem("cactus_slab",new BlockItem(MelonSlabsBlocks.CACTUS_SLAB, new Item.Settings()));
        public static final Item PUMPKIN_STAIRS = registerItem("pumpkin_stairs",new BlockItem(MelonSlabsBlocks.PUMPKIN_STAIRS, new Item.Settings()));
        public static final Item PUMPKIN_SLAB = registerItem("pumpkin_slab",new BlockItem(MelonSlabsBlocks.PUMPKIN_SLAB, new Item.Settings()));
        public static final Item CARVED_PUMPKIN_SLAB = registerItem("carved_pumpkin_slab",new BlockItem(MelonSlabsBlocks.CARVED_PUMPKIN_SLAB, new Item.Settings()));
        public static final Item JACK_O_SLAB = registerItem("jack_o_slab",new BlockItem(MelonSlabsBlocks.JACK_O_SLAB, new Item.Settings()));
        public static final Item FRANKENMELON = registerItem("frankenmelon",new BlockItem(MelonSlabsBlocks.FRANKENMELON, new Item.Settings()));
        public static final Item JUICER = registerItem("juicer",new BlockItem(MelonSlabsBlocks.JUICER, new Item.Settings()));

        //Food Items
        public static final Item PUMPKIN_SLICE = registerItem("pumpkin_slice",new Item(new Item.Settings().food(new FoodComponent.Builder().hunger(1).saturationModifier(0.3f).snack().build())));
        public static final Item COOKED_PUMPKIN_SLICE = registerItem("cooked_pumpkin_slice",new Item(new Item.Settings().food(new FoodComponent.Builder().hunger(4).saturationModifier(0.6f).build())));

        //Juices
        public static final Item MELON_JUICE = registerItem("melon_juice", new JuiceItem(new Item.Settings().maxCount(16).recipeRemainder(Items.GLASS_BOTTLE).food(new FoodComponent.Builder().hunger(6).saturationModifier(0.3f).build())));
        public static final Item APPLE_JUICE = registerItem("apple_juice",new JuiceItem(new Item.Settings().maxCount(16).recipeRemainder(Items.GLASS_BOTTLE).food(new FoodComponent.Builder().hunger(8).saturationModifier(0.3f).build())));
        //twice the absorption, regen lasts 1.5x as long                                                                                                                        
        public static final Item GOLDEN_APPLE_JUICE = registerItem("golden_apple_juice",new JuiceItem(new Item.Settings().maxCount(4).recipeRemainder(Items.GLASS_BOTTLE).rarity(Rarity.RARE).food(new FoodComponent.Builder().hunger(8).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 150, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 1), 1.0f).alwaysEdible().build())));
        //1.5x the absorption, regen lasts twice as long, resistance 2 instead of 1, fire resistance last 1.5x as long, 33% chance of giving you wither that lasts as long as the regen
        public static final Item ENCHANTED_GOLDEN_APPLE_JUICE = registerItem("enchanted_golden_apple_juice",new GlintedJuiceItem(new Item.Settings().maxCount(4).recipeRemainder(Items.GLASS_BOTTLE).rarity(Rarity.EPIC).food(new FoodComponent.Builder().hunger(8).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 800, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 9000, 0), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 7), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1600, 0), 0.33f).alwaysEdible().build())));

        //Magic Items
        public static final Item TORTURED_SOUL = registerItem("tortured_soul",new TorturedSoulItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE).recipeRemainder(Items.GLASS_BOTTLE)));

        private static Item registerItem(String name, Item item){
            return Registry.register(Registries.ITEM, new Identifier("melonslabs",name), item);
        }

        //Item group
        public static final ItemGroup GROUP = FabricItemGroup.builder(new Identifier("melonslabs", "group"))
        .displayName(Text.translatable("itemGroup.melonslabs.group"))
        .icon(() -> new ItemStack(MelonSlabsItems.MELON_SLAB))
        .build();

        private static void addItemToGroup( Item item) {
            ItemGroupEvents.modifyEntriesEvent(GROUP).register(entries -> entries.add(item));
        }

        //register items
        public static void registerItems(){
            
            // Registry.register(Registries.ITEM, "melonslabs:cactus_slab", CACTUS_SLAB);
            // Registry.register(Registries.ITEM, "melonslabs:jill_o_lantern", JILL_O_LANTERN);
            // Registry.register(Registries.ITEM, "melonslabs:carved_melon", CARVED_MELON);
            // Registry.register(Registries.ITEM, "melonslabs:melon_stairs", MELON_STAIRS);
            // Registry.register(Registries.ITEM, "melonslabs:melon_slab", MELON_SLAB);
            // Registry.register(Registries.ITEM, "melonslabs:carved_melon_slab", CARVED_MELON_SLAB);
            // Registry.register(Registries.ITEM, "melonslabs:jill_o_slab", JILL_O_SLAB);
            // Registry.register(Registries.ITEM, "melonslabs:pumpkin_stairs", PUMPKIN_STAIRS);
            // Registry.register(Registries.ITEM, "melonslabs:pumpkin_slab", PUMPKIN_SLAB);
            // Registry.register(Registries.ITEM, "melonslabs:carved_pumpkin_slab", CARVED_PUMPKIN_SLAB);
            // Registry.register(Registries.ITEM, "melonslabs:jack_o_slab", JACK_O_SLAB);
            // Registry.register(Registries.ITEM, "melonslabs:pumpkin_slice", PUMPKIN_SLICE);
            // Registry.register(Registries.ITEM, "melonslabs:cooked_pumpkin_slice", COOKED_PUMPKIN_SLICE);
            // Registry.register(Registries.ITEM, "melonslabs:frankenmelon", FRANKENMELON);
            // Registry.register(Registries.ITEM, "melonslabs:juicer", JUICER);
            // Registry.register(Registries.ITEM, "melonslabs:melon_juice", MELON_JUICE);
            // Registry.register(Registries.ITEM, "melonslabs:apple_juice", APPLE_JUICE);
            // Registry.register(Registries.ITEM, "melonslabs:golden_apple_juice", GOLDEN_APPLE_JUICE);
            // Registry.register(Registries.ITEM, "melonslabs:enchanted_golden_apple_juice", ENCHANTED_GOLDEN_APPLE_JUICE);
            // Registry.register(Registries.ITEM, "melonslabs:tortured_soul", TORTURED_SOUL);

            //Blocks
            addItemToGroup(CACTUS_SLAB);
            addItemToGroup(JILL_O_LANTERN);
            addItemToGroup(CARVED_MELON);
            addItemToGroup(MELON_STAIRS);
            addItemToGroup(MELON_SLAB);
            addItemToGroup(CARVED_MELON_SLAB);
            addItemToGroup(JILL_O_SLAB);
            addItemToGroup(PUMPKIN_STAIRS);
            addItemToGroup(PUMPKIN_SLAB);
            addItemToGroup(CARVED_PUMPKIN_SLAB);
            addItemToGroup(JACK_O_SLAB);

            //Food Items
            addItemToGroup(PUMPKIN_SLICE);
            addItemToGroup(COOKED_PUMPKIN_SLICE);

            //Juices
            addItemToGroup(JUICER);
            addItemToGroup(MELON_JUICE);
            addItemToGroup(APPLE_JUICE);
            addItemToGroup(GOLDEN_APPLE_JUICE);
            addItemToGroup(ENCHANTED_GOLDEN_APPLE_JUICE);

            //Magic Items
            addItemToGroup(FRANKENMELON);
            addItemToGroup(TORTURED_SOUL);

        }
}