package net.melon.slabs.rei_integration;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.melon.slabs.items.MelonSlabsItems;
import net.melon.slabs.screens.JuicerRecipe;
import net.melon.slabs.screens.JuicerScreen;

public class melonslabsClientPlugin implements REIClientPlugin {
    
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new JuicerCategory());
        registry.addWorkstations(JuicerCategory.JUICER_DISPLAY, EntryStacks.of(MelonSlabsItems.JUICER));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
       //registry.add();
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(new JuicerTransferHandler());
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        //basically calling register a bunch of times to fill the registry
        registry.registerFiller(JuicerRecipe.class, JuicerDisplay::new);

    }
}