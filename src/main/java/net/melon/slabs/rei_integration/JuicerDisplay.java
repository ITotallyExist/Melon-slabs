package net.melon.slabs.rei_integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.melon.slabs.screens.JuicerRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class JuicerDisplay extends BasicDisplay {
    // public static JUICER_DISPLAY_SERIALIZER = BasicDisplay.Serializer.ofSimple(JuicerDisplay::new);


    public JuicerDisplay(JuicerRecipe recipe){
        this(EntryIngredients.ofIngredients(recipe.getInputs()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
		Optional.ofNullable(recipe.getId()));
    }

    public JuicerDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    public EntryIngredient getOutput(){
        return (this.outputs.get(0));
    }

    //gives a list of itemstacks that is the full inputs required for this recipe, including glass bottles
    public List<ItemStack> getFullInputs(){
        List<EntryIngredient> inputs = this.getInputEntries();

        List<ItemStack> incompleteInputStacks = new ArrayList<ItemStack>();

        inputs.forEach(entryIngredient -> {
            entryIngredient.forEach(entryStack ->{
                //make sure it is an item entry (not really necessary because we are dealing with juicer recipes only at this point, but whatevs)
                if (entryStack.getValueType() == ItemStack.class){
                    incompleteInputStacks.add((ItemStack) entryStack.getValue());
                }

                if (entryStack.isEmpty()){
                    incompleteInputStacks.add(ItemStack.EMPTY);
                }
            
            });

        });

        Integer numRecipeInputs = incompleteInputStacks.size();
        List<ItemStack> inputStacks = new ArrayList<ItemStack>();

        if (numRecipeInputs == 0){
            inputStacks.add(ItemStack.EMPTY);
            inputStacks.add(ItemStack.EMPTY);
            inputStacks.add(ItemStack.EMPTY);
            inputStacks.add(ItemStack.EMPTY);
        } else if (numRecipeInputs == 1){
            inputStacks.add(ItemStack.EMPTY);
            inputStacks.add(ItemStack.EMPTY);
            inputStacks.add(ItemStack.EMPTY);
            inputStacks.add(incompleteInputStacks.get(0));
        } else if (numRecipeInputs == 2){
            inputStacks.add(ItemStack.EMPTY);
            inputStacks.add(incompleteInputStacks.get(0));
            inputStacks.add(ItemStack.EMPTY);
            inputStacks.add(incompleteInputStacks.get(1));
        } else if (numRecipeInputs == 3){
            inputStacks.add(ItemStack.EMPTY);
            inputStacks.add(incompleteInputStacks.get(1));
            inputStacks.add(incompleteInputStacks.get(0));
            inputStacks.add(incompleteInputStacks.get(2));
        } else {
            inputStacks.add(incompleteInputStacks.get(2));
            inputStacks.add(incompleteInputStacks.get(1));
            inputStacks.add(incompleteInputStacks.get(0));
            inputStacks.add(incompleteInputStacks.get(3));
        }

        return incompleteInputStacks;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return(JuicerCategory.JUICER_DISPLAY);
    }
    
}
