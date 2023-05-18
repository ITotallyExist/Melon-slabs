package net.melon.slabs.rei_integration.common;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import net.melon.slabs.menu.JuicerMenu;
import net.melon.slabs.rei_integration.JuicerCategory;
import net.melon.slabs.rei_integration.JuicerDisplay;
import net.melon.slabs.screens.JuicerRecipe;
import net.melon.slabs.screens.JuicerScreenHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class melonslabsCommonPlugin implements REIServerPlugin {
    
    @Override
    public void registerMenuInfo(MenuInfoRegistry registry){
        registry.register(JuicerCategory.JUICER_DISPLAY, JuicerScreenHandler.class, SimpleMenuInfoProvider.of(JuicerMenuInfo::new));
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
	    registry.register(JuicerCategory.JUICER_DISPLAY, new DisplaySerializer<JuicerDisplay>() {

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
        });
    }
}
