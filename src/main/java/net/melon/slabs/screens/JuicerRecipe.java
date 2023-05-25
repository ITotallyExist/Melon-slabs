package net.melon.slabs.screens;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

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
	private final Ingredient bottleInput;
	private final ItemStack result;
	private final Identifier id;
	public static final String ID = "juicer_recipe";

	public JuicerRecipe(Identifier id, ItemStack result, Ingredient inputA, Ingredient inputB, Ingredient inputC, Ingredient bottleInput) {
		this.id = id;
		this.inputA = inputA;
		this.inputB = inputB;
		this.inputC = inputC;
		this.bottleInput = bottleInput;
		this.result = result;
	}
 
	//so that the rei display can have a list of ingredients to work with
	public List<Ingredient> getInputs(){
		List<Ingredient> ingredients = new ArrayList<Ingredient>();

		ingredients.add(this.inputA);
		ingredients.add(this.inputB);
		ingredients.add(this.inputC);
		ingredients.add(this.bottleInput);


		return (ingredients);
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

	public Ingredient getBottleInput() {
		return this.bottleInput;
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

		//no bottle in bottle slot
		if (!bottleInput.test(inv.getStack(3))) return false;

		boolean result = false;
		//we check all possible setups where each slot corresponds to each of the inputs for a mach because this is shapeless
		int i,j;
		for (i=0; i<3; i++){
			for (j=1; j<3; j++){
				int jIndex = (j+i)%3;//so its not the same as i, but covers all other possiblities given all possibilities of j one to one
				if (inputA.test(inv.getStack(i)) && inputB.test(inv.getStack(jIndex)) && inputC.test(inv.getStack((2*(i+jIndex))%3))){
					//match made!
					result = true;
				}
			}
		}

		return result;
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
		JsonObject bottleInput;
        String outputItem;
        int outputAmount;
    }

}