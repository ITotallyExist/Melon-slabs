package net.melon.slabs.rei_integration;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;

import net.melon.slabs.items.MelonSlabsItems;
import net.melon.slabs.screens.JuicerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class JuicerCategory implements DisplayCategory<JuicerDisplay> {
    public static final CategoryIdentifier<JuicerDisplay> JUICER_DISPLAY = CategoryIdentifier.of("melonslabs", "juicer_display");

    @Override
    public CategoryIdentifier<? extends JuicerDisplay> getCategoryIdentifier() {
        return (JUICER_DISPLAY);
    }

    @Override
    public Text getTitle() {
        return (Text.translatable("block.melonslabs.juicer"));
    }

    @Override
    public Renderer getIcon() {
        return (EntryStacks.of(MelonSlabsItems.JUICER));
    }

    // @Override
    // public int getDisplayHeight(){
    //     return 75;
    // }

    @Override
    public List<Widget> setupDisplay(JuicerDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getMinY()+7);
        List<Widget> widgets = new ArrayList<Widget>();

        // The base background of the display
        // Please try to not remove this to preserve an uniform style to REI
        widgets.add(Widgets.createRecipeBase(bounds));

        //then we add the arrows to the background
        Rectangle imageBounds = new Rectangle( startPoint.x + 21, startPoint.y + 17, 20, 18);
        Rectangle imageBounds2 = new Rectangle(startPoint.x + 40, startPoint.y+19, 31, 21);
        widgets.add(Widgets.createTexturedWidget(new Identifier("melonslabs", "textures/gui/juicer_arrow_1.png"), imageBounds,0,0,20,18));
        widgets.add(Widgets.createTexturedWidget(new Identifier("melonslabs", "textures/gui/juicer_arrow_2.png"), imageBounds2, 0,0,31,21));


        
        // We create a result slot background AND
        // disable the actual background of the result slot, so the result slot can look bigger
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 77, startPoint.y + 18)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 77, startPoint.y + 18))
                .entries(display.getOutput()) // Get the output 
                .disableBackground() // Disable the background because we have our bigger background
                .markOutput()); // Mark this as the output for REI to identify
        
        //first we get a nice list of the inputs for this recipe
        List<EntryIngredient> recipeInputs = display.getInputEntries();
        Integer numRecipeInputs = recipeInputs.size();
        List<EntryIngredient> displayInputs = new ArrayList<EntryIngredient>();

        EntryIngredient emptyIngredient = EntryIngredients.of(ItemStack.EMPTY);

        if (numRecipeInputs == 0){
            displayInputs.add(emptyIngredient);
            displayInputs.add(emptyIngredient);
            displayInputs.add(emptyIngredient);
        } else if (numRecipeInputs == 1){
            displayInputs.add(emptyIngredient);
            displayInputs.add(recipeInputs.get(0));
            displayInputs.add(emptyIngredient);
        } else if (numRecipeInputs == 2){
            displayInputs.add(emptyIngredient);
            displayInputs.add(recipeInputs.get(1));
            displayInputs.add(recipeInputs.get(0));
        } else {
            displayInputs.add(recipeInputs.get(2));
            displayInputs.add(recipeInputs.get(1));
            displayInputs.add(recipeInputs.get(0));
        }
        

        // then We add the input slots
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5))
                .entries(displayInputs.get(2)) // Get the first input ingredient
                .markInput()); // Mark this as the input for REI to identify
        
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 23, startPoint.y ))
                .entries(displayInputs.get(1)) 
                .markInput());
        
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 42, startPoint.y + 5))
                .entries(displayInputs.get(0)) 
                .markInput());
        

        //we add the slot for the glass bottle
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 23, startPoint.y + 36))
                .entries(EntryIngredients.of(new ItemStack(Items.GLASS_BOTTLE))) 
                .markInput());

        
        
        // We return the list of widgets for REI to display
        return widgets;
    } 
    
}
