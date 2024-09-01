package net.melon.slabs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

//unused, for compatibility reasons, check the entitymixin.java file

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity{

    public FallingBlockEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (player.getEquippedStack(EquipmentSlot.HEAD).isOf (MelonSlabsItems.MELON_HAT)){
            
            player.getWorld().syncWorldEvent(WorldEvents.ANVIL_LANDS, BlockPos.ofFloored(player.getPos()).up(), 0);

            //avoiding random here so we can do this on both the client and server sides to avoid desync
            double direction = 3.14159265*2*Math.sin(10*player.getPos().x*player.getPos().y*player.getPos().z);
            //do the bouncing
            this.setVelocity(0.3*Math.sin(direction), 0.2, 0.3*Math.cos(direction));
            this.tick();
        }
    }
}
