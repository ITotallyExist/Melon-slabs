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
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class MelonSlabsItems {
        

        //Block Items
        public static final Item JILL_O_LANTERN = registerItem("jill_o_lantern",new BlockItem(MelonSlabsBlocks.JILL_O_LANTERN, new Item.Settings()));
        public static final Item MELON_STAIRS = registerItem("melon_stairs",new BlockItem(MelonSlabsBlocks.MELON_STAIRS, new Item.Settings()));
        public static final Item MELON_SLAB = registerItem("melon_slab",new BlockItem(MelonSlabsBlocks.MELON_SLAB, new Item.Settings()));
        public static final Item MELON_SLAB_ALMOST_FULL = registerItem("melon_slab_almost_full",new BlockItem(MelonSlabsBlocks.MELON_SLAB_ALMOST_FULL, new Item.Settings()));
        public static final Item MELON_SLAB_ALMOST_EMPTY = registerItem("melon_slab_almost_empty",new BlockItem(MelonSlabsBlocks.MELON_SLAB_ALMOST_EMPTY, new Item.Settings()));
        public static final Item MELON_RIND = registerItem("melon_rind",new BlockItem(MelonSlabsBlocks.MELON_RIND, new Item.Settings()));
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

        //Armor Items
        public static final Item MELON_HAT = registerItem("melon_hat",new MelonHat(new Item.Settings().maxCount(5)));


        private static Item registerItem(String name, Item item){
            return Registry.register(Registries.ITEM, new Identifier("melonslabs",name), item);
        }

        //Item group
        public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("melonslabs", "group"));

        public static void registerItemGroups(){
            Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
            .icon(() -> new ItemStack(MelonSlabsItems.MELON_SLAB))
            .displayName(Text.translatable("itemGroup.melonslabs.group"))
            .build()); // build() no longer registers by itself
        }

        private static void addItemToModGroup( Item item) {
            addItemToGroup (item, ITEM_GROUP);
        }

        private static void addItemToFoodGroup(Item item) {
            addItemToGroup (item, ItemGroups.FOOD_AND_DRINK);
        }

        private static void addItemToNaturalBlocksGroup(Item item) {
            addItemToGroup (item, ItemGroups.NATURAL);
        }

        private static void addItemToBuildingBlocksGroup(Item item) {
            addItemToGroup (item, ItemGroups.BUILDING_BLOCKS);
        }

        private static void addItemToEquipmentGroup(Item item) {
            addItemToGroup (item, ItemGroups.SEARCH);
        }

        private static void addItemToGroup (Item item, RegistryKey<ItemGroup> group){
            ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
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
            addItemToModGroup(CACTUS_SLAB);
            addItemToModGroup(JILL_O_LANTERN);
            addItemToModGroup(CARVED_MELON);
            addItemToModGroup(MELON_STAIRS);
            addItemToModGroup(MELON_SLAB);
            addItemToModGroup(MELON_SLAB_ALMOST_FULL);
            addItemToModGroup(MELON_SLAB_ALMOST_EMPTY);
            addItemToModGroup(MELON_RIND);
            addItemToModGroup(CARVED_MELON_SLAB);
            addItemToModGroup(JILL_O_SLAB);
            addItemToModGroup(PUMPKIN_STAIRS);
            addItemToModGroup(PUMPKIN_SLAB);
            addItemToModGroup(CARVED_PUMPKIN_SLAB);
            addItemToModGroup(JACK_O_SLAB);

            //Food Items
            addItemToModGroup(PUMPKIN_SLICE);
            addItemToModGroup(COOKED_PUMPKIN_SLICE);

            //Juices
            addItemToModGroup(JUICER);
            addItemToModGroup(MELON_JUICE);
            addItemToModGroup(APPLE_JUICE);
            addItemToModGroup(GOLDEN_APPLE_JUICE);
            addItemToModGroup(ENCHANTED_GOLDEN_APPLE_JUICE);

            //Magic Items
            addItemToModGroup(FRANKENMELON);
            addItemToModGroup(TORTURED_SOUL);

            //Armor Items
            addItemToModGroup(MELON_HAT);

            //Adding to food group
            addItemToFoodGroup(PUMPKIN_SLICE);
            addItemToFoodGroup(COOKED_PUMPKIN_SLICE);
            addItemToFoodGroup(MELON_JUICE);
            addItemToFoodGroup(APPLE_JUICE);
            addItemToFoodGroup(GOLDEN_APPLE_JUICE);
            addItemToFoodGroup(ENCHANTED_GOLDEN_APPLE_JUICE);

            //Adding to naturalblocks group
            addItemToNaturalBlocksGroup(CARVED_MELON);
            addItemToNaturalBlocksGroup(JILL_O_LANTERN);

            //Adding to building blocks group
            addItemToBuildingBlocksGroup(MELON_STAIRS);
            addItemToBuildingBlocksGroup(MELON_SLAB);
            addItemToBuildingBlocksGroup(MELON_SLAB_ALMOST_FULL);
            addItemToBuildingBlocksGroup(MELON_SLAB_ALMOST_EMPTY);
            addItemToBuildingBlocksGroup(MELON_RIND);
            addItemToBuildingBlocksGroup(CARVED_MELON_SLAB);
            addItemToBuildingBlocksGroup(JILL_O_SLAB);
            addItemToBuildingBlocksGroup(PUMPKIN_STAIRS);
            addItemToBuildingBlocksGroup(PUMPKIN_SLAB);
            addItemToBuildingBlocksGroup(CARVED_PUMPKIN_SLAB);
            addItemToBuildingBlocksGroup(JACK_O_SLAB);

            //Adding to Functional Blocks group
            addItemToGroup(JUICER, ItemGroups.FUNCTIONAL);

            //Adding items to equipment group
            addItemToEquipmentGroup(MELON_HAT);
        }
}