package net.melon.slabs.screens;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class MelonSlabsScreens {
    public static final ScreenHandlerType<JuicerScreenHandler> JUICER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier("melonslabs","juicer"), JuicerScreenHandler::new);
    public static RecipeType<JuicerRecipe> JUICER_RECIPE_TYPE;

    public static void registerScreensAndRecipes(){
        Registry.register(Registries.RECIPE_SERIALIZER, JuicerRecipeSerializer.ID,JuicerRecipeSerializer.INSTANCE);
        JUICER_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, new Identifier(JuicerRecipe.ID), new RecipeType<JuicerRecipe>(){

            public String toString() {
                return JuicerRecipe.ID;
            }
        });
    }

    public static void registerScreensAndRecipesClient(){
        ScreenRegistry.register(JUICER_SCREEN_HANDLER, JuicerScreen::new);
        //Registry.register(JUICER_SCREEN_HANDLER, JuicerScreen::new);
    }

}
