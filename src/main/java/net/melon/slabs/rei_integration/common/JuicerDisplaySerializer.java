package net.melon.slabs.rei_integration.common;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.melon.slabs.rei_integration.JuicerDisplay;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class JuicerDisplaySerializer implements DisplaySerializer<JuicerDisplay> {

    @Override
    public NbtCompound save(NbtCompound tag, JuicerDisplay display) {
        for (int i=0; i<display.getInputEntries().size(); i++){
            tag.put("input"+Integer.toString(i), display.getInputEntries().get(i).saveIngredient());
        }

        for (int i=0; i<display.getOutputEntries().size(); i++){
            tag.put("output"+Integer.toString(i), display.getOutputEntries().get(i).saveIngredient());
        }
        
        return tag;
    }

    @Override
    public JuicerDisplay read(NbtCompound tag) {
        List<EntryIngredient> inputs = new ArrayList<EntryIngredient>();
        List<EntryIngredient> outputs = new ArrayList<EntryIngredient>();

        for (int i=0; tag.contains("input"+Integer.toString(i)); i++){
            inputs.add(EntryIngredient.read((NbtList) tag.get("input"+Integer.toString(i))));
        }

        for (int i=0; tag.contains("output"+Integer.toString(i)); i++){
            outputs.add(EntryIngredient.read((NbtList) tag.get("output"+Integer.toString(i))));
        }

        JuicerDisplay display = new JuicerDisplay(inputs, outputs, null);
        
        return display;
    }
    
}
