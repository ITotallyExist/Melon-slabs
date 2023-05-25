package net.melon.slabs.screens;

import com.google.gson.Gson;

//Turns JSON into Recipe for Minecraft to load recipe JSON.
//Turns PacketByteBuf to Recipe, and Recipe into PacketByteBuf for Minecraft to sync the recipe through the network.

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.melon.slabs.screens.JuicerRecipe.JuicerRecipeJsonFormat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class JuicerRecipeSerializer implements RecipeSerializer<JuicerRecipe>{
    // Define ExampleRecipeSerializer as a singleton by making its constructor private and exposing an instance.
    private JuicerRecipeSerializer() {
    }

    public static final JuicerRecipeSerializer INSTANCE = new JuicerRecipeSerializer();

    // This will be the "type" field in the json
    public static final Identifier ID = new Identifier("melonslabs:juicer_recipe");


    @Override
    // Turns json into Recipe
    public JuicerRecipe read(Identifier id, JsonObject json) {
        JuicerRecipe.JuicerRecipeJsonFormat recipeJson = new Gson().fromJson(json, JuicerRecipeJsonFormat.class);

        // Validate all fields are there
        if (recipeJson.inputA == null || recipeJson.outputItem == null) {
            throw new JsonSyntaxException("A required attribute is missing!");
        }
        // We'll allow to not specify the output, and default it to 1.
        if (recipeJson.outputAmount == 0) recipeJson.outputAmount = 1;

        Ingredient inputA = Ingredient.fromJson(recipeJson.inputA);
        //they dont need to have all three
        Ingredient inputB = Ingredient.empty();
        if (recipeJson.inputB != null){
            inputB = Ingredient.fromJson(recipeJson.inputB);
        }

        Ingredient inputC = Ingredient.empty();
        if (recipeJson.inputC != null){
            inputC = Ingredient.fromJson(recipeJson.inputC);
        }

        Ingredient bottleInput = Ingredient.empty();
        if (recipeJson.bottleInput != null){
            bottleInput = Ingredient.fromJson(recipeJson.bottleInput);
        }

        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem))
            // Validate the inputted item actually exists
            .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.outputItem));
        ItemStack output = new ItemStack(outputItem, recipeJson.outputAmount);

        return new JuicerRecipe(id, output, inputA, inputB, inputC, bottleInput);
    }
    @Override
    // Turns Recipe into PacketByteBuf
    public void write(PacketByteBuf packetData, JuicerRecipe recipe) {
        recipe.getInputA().write(packetData);
        recipe.getInputB().write(packetData);
        recipe.getInputC().write(packetData);
        recipe.getBottleInput().write(packetData);
        packetData.writeItemStack(recipe.getOutput());
    }

    @Override
    // Turns PacketByteBuf into Recipe
    public JuicerRecipe read(Identifier recipeId, PacketByteBuf packetData) {
        // Make sure the read in the same order you have written!
        Ingredient inputA = Ingredient.fromPacket(packetData);
        Ingredient inputB = Ingredient.fromPacket(packetData);
        Ingredient inputC = Ingredient.fromPacket(packetData);
        Ingredient bottleInput = Ingredient.fromPacket(packetData);
        ItemStack output = packetData.readItemStack();
        return new JuicerRecipe(recipeId, output, inputA, inputB, inputC, bottleInput);
    }
}