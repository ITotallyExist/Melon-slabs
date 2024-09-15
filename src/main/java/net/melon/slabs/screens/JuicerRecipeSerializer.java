package net.melon.slabs.screens;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

//Turns JSON into Recipe for Minecraft to load recipe JSON.
//Turns PacketByteBuf to Recipe, and Recipe into PacketByteBuf for Minecraft to sync the recipe through the network.

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.melon.slabs.screens.JuicerRecipe.JuicerRecipeJsonFormat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;


public class JuicerRecipeSerializer implements RecipeSerializer<JuicerRecipe>{
    // Define ExampleRecipeSerializer as a singleton by making its constructor private and exposing an instance.
    private JuicerRecipeSerializer() {
    }

    public static final JuicerRecipeSerializer INSTANCE = new JuicerRecipeSerializer();

    // This will be the "type" field in the json
    public static final Identifier ID = new Identifier("melonslabs:juicer_recipe");

    private static final MapCodec<Ingredient> CODEC_IN_A = Ingredient.ALLOW_EMPTY_CODEC.fieldOf("inputA");
    private static final MapCodec<Ingredient> CODEC_IN_B = Ingredient.ALLOW_EMPTY_CODEC.fieldOf("inputB");
    private static final MapCodec<Ingredient> CODEC_IN_C = Ingredient.ALLOW_EMPTY_CODEC.fieldOf("inputC");
    private static final MapCodec<Ingredient> CODEC_IN_BOTTLE = Ingredient.ALLOW_EMPTY_CODEC.fieldOf("bottleInput");
    private static final MapCodec<ItemStack> CODEC_OUT = ItemStack.CODEC.fieldOf("output");

    public static final MapCodec<JuicerRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CODEC_OUT.forGetter(JuicerRecipe::getOutput),
        CODEC_IN_A.forGetter(JuicerRecipe::getInputA),
        CODEC_IN_B.forGetter(JuicerRecipe::getInputB),
        CODEC_IN_C.forGetter(JuicerRecipe::getInputC),
        CODEC_IN_BOTTLE.forGetter(JuicerRecipe::getBottleInput)
    ).apply(instance, JuicerRecipe::new)).fieldOf("melonslabs:juicer_recipe");


    public static final PacketCodec<RegistryByteBuf, JuicerRecipe> PACKET_CODEC = new PacketCodec<RegistryByteBuf, JuicerRecipe>() {
		public JuicerRecipe decode(RegistryByteBuf byteBuf) {
			Ingredient inputA = Ingredient.PACKET_CODEC.decode(byteBuf);
            Ingredient inputB = Ingredient.PACKET_CODEC.decode(byteBuf);
            Ingredient inputC = Ingredient.PACKET_CODEC.decode(byteBuf);
            Ingredient bottleInput = Ingredient.PACKET_CODEC.decode(byteBuf);
            ItemStack output = ItemStack.PACKET_CODEC.decode(byteBuf);
            return new JuicerRecipe(output, inputA, inputB, inputC, bottleInput);
		}

		public void encode(RegistryByteBuf byteBuf, JuicerRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(byteBuf, recipe.getInputA());
            Ingredient.PACKET_CODEC.encode(byteBuf, recipe.getInputB());
            Ingredient.PACKET_CODEC.encode(byteBuf, recipe.getInputC());
            Ingredient.PACKET_CODEC.encode(byteBuf, recipe.getBottleInput());
            ItemStack.PACKET_CODEC.encode(byteBuf, recipe.getOutput());
		}
	};


    @Override
    
    public MapCodec<JuicerRecipe> codec() {
        return CODEC;
    }


    @Override
    //create a codec that turns a registrybytebuf into a juicer recipe and vice versa
    public PacketCodec<RegistryByteBuf, JuicerRecipe> packetCodec() {

        
        // TODO Auto-generated method stub


        throw new UnsupportedOperationException("Unimplemented method 'packetCodec'");
    }
}