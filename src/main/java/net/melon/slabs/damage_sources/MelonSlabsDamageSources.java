package net.melon.slabs.damage_sources;

import com.mojang.serialization.Lifecycle;

import net.fabricmc.fabric.mixin.registry.sync.RegistriesAccessor;
import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class MelonSlabsDamageSources {
    public static final DamageType ALCOHOL_POISONING_TYPE = new DamageType("alcohol_poisoning", 0.0f);

    public static final Registry<DamageType> DAMAGE_TYPES = new SimpleRegistry<DamageType>(RegistryKeys.DAMAGE_TYPE, Lifecycle.stable(), false);



    public static DamageSource ALCOHOL_POISONING_SOURCE;


    public static void registerDamageTypes(){
        Registry.register( DAMAGE_TYPES, new Identifier("melonslabs","alcohol_poisoning"), ALCOHOL_POISONING_TYPE);
        ALCOHOL_POISONING_SOURCE = new DamageSource(DAMAGE_TYPES.getEntry(ALCOHOL_POISONING_TYPE));
    }
    

}
