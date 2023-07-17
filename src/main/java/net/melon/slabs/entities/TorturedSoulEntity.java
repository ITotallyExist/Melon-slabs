package net.melon.slabs.entities;

import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class TorturedSoulEntity extends ThrownItemEntity {
    public TorturedSoulEntity(EntityType<? extends TorturedSoulEntity> entityType, World world) {
        super((EntityType<? extends ThrownItemEntity>)entityType, world);
    }

    public TorturedSoulEntity(World world, LivingEntity owner) {
        super((EntityType<? extends ThrownItemEntity>)MelonSlabsEntities.TORTURED_SOUL, owner, world);
    }

    public TorturedSoulEntity(World world, double x, double y, double z) {
        super((EntityType<? extends ThrownItemEntity>)MelonSlabsEntities.TORTURED_SOUL, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return MelonSlabsItems.TORTURED_SOUL;
    }

    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return itemStack.isEmpty() ? new ItemStackParticleEffect(ParticleTypes.ITEM,new  ItemStack(MelonSlabsItems.TORTURED_SOUL, 1)) : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            ParticleEffect particleEffect = this.getParticleParameters();
            for (int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        //TODO: spawn phantoms targiting the entity that you hit
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 2.0f, World.ExplosionSourceType.NONE);

        //TODO: spawn phantoms targeting nearest entity if this didnt already hit an entity
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }
    
}
