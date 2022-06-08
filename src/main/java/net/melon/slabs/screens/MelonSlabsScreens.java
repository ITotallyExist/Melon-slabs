package net.melon.slabs.screens;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MelonSlabsScreens {
    public static void registerScreensAndRecipes(){
        Registry.register(Registry.RECIPE_SERIALIZER, JuicerRecipeSerializer.ID,
                JuicerRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier("example", JuicerRecipe.Type.ID), JuicerRecipe.Type.INSTANCE);
    }
}
