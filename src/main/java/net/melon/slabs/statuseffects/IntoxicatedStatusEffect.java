package net.melon.slabs.statuseffects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class IntoxicatedStatusEffect extends StatusEffect {
    public IntoxicatedStatusEffect() {
      super(
        StatusEffectCategory.BENEFICIAL, // whether beneficial or harmful for entities
        0x527D26); // color in RGB
    }
   
    // This method is called every tick to check whether it should apply the status effect or not
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
      // In our case, we just make it return true so that it applies the status effect every tick.
      return true;
    }
   
}