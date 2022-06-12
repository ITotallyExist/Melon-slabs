package net.melon.slabs.screens;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MelonSlabsScreens {
    public static final ScreenHandlerType<JuicerScreenHandler> JUICER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier("melonslabs","juicer"), JuicerScreenHandler::new);
    public static RecipeType<JuicerRecipe> JUICER_RECIPE_TYPE;

    public static void registerScreensAndRecipes(){
        Registry.register(Registry.RECIPE_SERIALIZER, JuicerRecipeSerializer.ID,JuicerRecipeSerializer.INSTANCE);
        JUICER_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, new Identifier("melonslabs",JuicerRecipe.ID), new RecipeType<JuicerRecipe>(){

            public String toString() {
                return JuicerRecipe.ID;
            }
        });
    }

    public static void registerScreensAndRecipesClient(){
        ScreenRegistry.register(JUICER_SCREEN_HANDLER, JuicerScreen::new);
    }

}
