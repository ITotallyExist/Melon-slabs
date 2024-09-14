package net.melon.slabs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;

//mixing into parent class (entity) when we care about behaviour of child class (fallingentity) so that we dont have to override inherited function
    //for compatibility issues

@Mixin(Entity.class)
public class EntityMixin {

    // Inject into the onPlayerCollision method
    @Inject(method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"))
    private void makeFallingBlocksBounce(PlayerEntity player, CallbackInfo ci) {
        //behavior only in child class
        if (((Entity) (Object) this) instanceof FallingBlockEntity){
            if (player.getEquippedStack(EquipmentSlot.HEAD).isOf (MelonSlabsItems.MELON_HAT)){
                //plays a sound as fallingblock hits player head
                player.playSoundToPlayer(SoundEvents.ENTITY_ARMOR_STAND_HIT, SoundCategory.BLOCKS, 5.0f, 1.0f);

                //avoiding random here so we can do this on both the client and server sides to avoid desync
                //the time element is so that if multiple blocks fall on you in a row they dont all go to the same place
                double direction = 3.14159265*2*Math.sin(10*player.getPos().x*player.getPos().y*player.getPos().z + player.getWorld().getTime()*2048);
                //do the bouncing
                ((Entity) (Object) this).setVelocity(0.3*Math.sin(direction), 0.2, 0.3*Math.cos(direction));
                ((Entity) (Object) this).tick();
            }
        }
    }
}
