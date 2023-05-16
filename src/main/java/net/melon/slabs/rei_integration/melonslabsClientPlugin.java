package net.melon.slabs.rei_integration;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import net.melon.slabs.screens.JuicerRecipe;
import net.melon.slabs.screens.JuicerScreen;

public class melonslabsClientPlugin implements REIClientPlugin {
    
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new JuicerCategory());


    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        //registry.add();
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        //basically calling register a bunch of times to fill the registry
        registry.registerFiller(JuicerRecipe.class, JuicerDisplay::new);

    }
}