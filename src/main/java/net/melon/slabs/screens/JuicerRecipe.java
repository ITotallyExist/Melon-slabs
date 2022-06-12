package net.melon.slabs.screens;

import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class JuicerRecipe implements Recipe<JuicerInventory> {
	//You can add as much inputs as you want here.
	//It is important to always use Ingredient, so you can support tags.
	private final Ingredient inputA;
	private final Ingredient inputB;
	private final Ingredient inputC;
	private final ItemStack result;
	private final Identifier id;
	public static final String ID = "juicer_recipe";

	public JuicerRecipe(Identifier id, ItemStack result, Ingredient inputA, Ingredient inputB, Ingredient inputC) {
		this.id = id;
		this.inputA = inputA;
		this.inputB = inputB;
		this.inputC = inputC;
		this.result = result;
	}
 
	public Ingredient getInputA() {
		return this.inputA;
	}
 
	public Ingredient getInputB() {
		return this.inputB;
	}

	public Ingredient getInputC() {
		return this.inputC;
	}
 
	@Override
	public ItemStack getOutput() {
		return this.result;
	}
 
	@Override
	public Identifier getId() {
		return this.id;
	}

	@Override
    public ItemStack craft(JuicerInventory inventory) {
        return ItemStack.EMPTY;
    }
 
	@Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override 
	public boolean matches(JuicerInventory inv, World world) {
		// System.out.println("match test");
		if(inv.size() < 4) return false;
		return inputA.test(inv.getStack(0)) && inputB.test(inv.getStack(1))&& inputC.test(inv.getStack(2)) && inv.getStack(3).isOf(Items.GLASS_BOTTLE);
	}

    @Override
    public RecipeType<?> getType() {
        return MelonSlabsScreens.JUICER_RECIPE_TYPE;
    }
    
	//returns the serializer that we made to go from json to not json and send messages over server
	@Override
    public RecipeSerializer<?> getSerializer() {
        return JuicerRecipeSerializer.INSTANCE;
    }

    class JuicerRecipeJsonFormat {
        JsonObject inputA;
        JsonObject inputB;
        JsonObject inputC;
        String outputItem;
        int outputAmount;
    }

}