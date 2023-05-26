package net.melon.slabs.sounds;


import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class MelonSlabsSounds {
    public static SoundEvent FRANKENMELON_HURT_EVENT = SoundEvent.of(new Identifier("melonslabs:frankenmelon_hurt"));

    public static void registerSoundEvents(){
        Registry.register(Registries.SOUND_EVENT, "melonslabs:frankenmelon_hurt", FRANKENMELON_HURT_EVENT);
    }
}