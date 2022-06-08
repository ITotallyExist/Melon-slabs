package net.melon.slabs.screens;

import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
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
		if(inv.size() < 2) return false;
		return inputA.test(inv.getStack(0)) && inputB.test(inv.getStack(1))&& inputC.test(inv.getStack(1));
	}
	
    public static class Type implements RecipeType<JuicerRecipe> {
        // Define JuicerRecipe.Type as a singleton by making its constructor private and exposing an instance.
        private Type() {}
        public static final Type INSTANCE = new Type();

        // This will be needed in step 4
        public static final String ID = "two_slot_recipe";
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
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