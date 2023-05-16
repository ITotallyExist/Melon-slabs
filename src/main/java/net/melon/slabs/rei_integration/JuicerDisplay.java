package net.melon.slabs.rei_integration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.melon.slabs.screens.JuicerRecipe;
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

    public List<EntryIngredient> getInputs(){
        return (this.inputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return(JuicerCategory.JUICER_DISPLAY);
    }
    
}
