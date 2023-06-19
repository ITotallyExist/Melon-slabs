package net.melon.slabs.status_effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MelonSlabsStatusEffects {
    public static final StatusEffect INTOXICATED = new IntoxicatedStatusEffect(StatusEffectCategory.BENEFICIAL, 0);

    public static void registerStatusEffects(){
        Registry.register(Registries.STATUS_EFFECT, new Identifier("melonslabs", "intoxicated") ,INTOXICATED);
    }
}
