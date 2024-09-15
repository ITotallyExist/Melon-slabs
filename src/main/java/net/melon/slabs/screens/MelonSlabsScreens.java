package net.melon.slabs.screens;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class MelonSlabsScreens {
    public static final ScreenHandlerType<JuicerScreenHandler> JUICER_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, new Identifier("melonslabs","juicer"), new ScreenHandlerType<>(JuicerScreenHandler::new, FeatureSet.empty()));
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
        HandledScreens.register(JUICER_SCREEN_HANDLER, JuicerScreen::new);
        //Registry.register(JUICER_SCREEN_HANDLER, JuicerScreen::new);
    }

}
