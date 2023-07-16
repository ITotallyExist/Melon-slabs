package net.melon.slabs.status_effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

//level 1 -- tipsy
    //multiplies all xp earned by 1.5
//level 2 -- drunk
    //multiplies all xp earned by 0.5
    //small chance per tick to drop held item
//levl 3 -- blackout
    //screen fades to black, you drop half your items at random, and you "wake up" in the nearest mushroom biome
    //you lose a few hearts and some hunger
        //if this killed you, you get the death message "drank too much"
            //and an achievement?
//level 3 + -- unobtainable without cheats, dead immediately, with the death message "died of alcohol poisoning"

public class IntoxicatedStatusEffect extends StatusEffect {

    public IntoxicatedStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

     // This method is called every tick to check whether it should apply the status effect or not
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        //true
        return true;
    }
    
    // This method is called when it applies the status effect. We implement custom functionality here.
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        if (amplifier == 0){
            //tipsy

        } else if (amplifier == 1){
            //drunk

        } else if (amplifier == 2){
            //blackout
        } else {
            //alcohol poisoning
            //entity.damage(MelonSlabsDamageSources.ALCOHOL_POISONING_SOURCE, Float.MAX_VALUE);
            
           
        }   
    }
    
}
