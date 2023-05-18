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
import net.melon.slabs.rei_integration.JuicerCategory;
import net.melon.slabs.rei_integration.JuicerDisplay;
import net.melon.slabs.screens.JuicerScreenHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class melonslabsCommonPlugin implements REIServerPlugin {
    
    @Override
    public void registerMenuInfo(MenuInfoRegistry registry){
        registry.register(JuicerCategory.JUICER_DISPLAY, JuicerScreenHandler.class, SimpleMenuInfoProvider.of(JuicerMenuInfo::new));
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
	    registry.register(JuicerCategory.JUICER_DISPLAY, new JuicerDisplaySerializer());
    }
}
