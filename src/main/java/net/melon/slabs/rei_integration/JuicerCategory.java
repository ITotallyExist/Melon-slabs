package net.melon.slabs.rei_integration;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;

import net.melon.slabs.items.MelonSlabsItems;

import net.minecraft.text.Text;

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

    @Override
    public List<Widget> setupDisplay(JuicerDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = new ArrayList<Widget>();
    
        // The base background of the display
        // Please try to not remove this to preserve an uniform style to REI
        widgets.add(Widgets.createRecipeBase(bounds));
        
        // The gray arrow
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        
        // We create a result slot background AND
        // disable the actual background of the slots, so the result slot can look bigger
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
                .entries(display.getOutput()) // Get the first output ingredient
                .disableBackground() // Disable the background because we have our bigger background
                .markOutput()); // Mark this as the output for REI to identify
        
        // We add the input slot
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5))
                .entries(display.getInputs().get(0)) // Get the first input ingredient
                .markInput()); // Mark this as the input for REI to identify
        
        // We return the list of widgets for REI to display
        return widgets;
    } 
    
}
