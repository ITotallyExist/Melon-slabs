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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;

@Mixin(Entity.class)
public class EntityMixin {

// Inject into the collideWithEntity method
    @Inject(method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"))
    public void onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        if (((Entity) (Object) this).getClass() == FallingBlockEntity.class){
            if (player.getEquippedStack(EquipmentSlot.HEAD).isOf (MelonSlabsItems.MELON_HAT)){
                
                player.getWorld().syncWorldEvent(WorldEvents.ANVIL_LANDS, BlockPos.ofFloored(player.getPos()).up(), 0);

                //avoiding random here so we can do this on both the client and server sides to avoid desync
                double direction = 3.14159265*2*Math.sin(10*player.getPos().x*player.getPos().y*player.getPos().z);
                //do the bouncing
                ((Entity) (Object) this).setVelocity(0.3*Math.sin(direction), 0.2, 0.3*Math.cos(direction));
                ((Entity) (Object) this).tick();
            }
        }
    }
}
