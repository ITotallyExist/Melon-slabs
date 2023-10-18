package net.melon.slabs.entities;

import java.util.ArrayList;

import net.melon.slabs.entities.tortured_phantom.TorturedPhantomEntity;
import net.melon.slabs.items.MelonSlabsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
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
        if(!this.getWorld().isClient){
            //create phantoms
            ArrayList<TorturedPhantomEntity> phantoms = createPhantoms();

            Entity entity = entityHitResult.getEntity();
            if (entity.isAlive() && entity.isLiving()){//if entity is alive
                //make phantoms target entity
                angerPhantoms(phantoms, (LivingEntity) entity);
            } else {
                //make phantoms target nearest living entity
                angerPhantoms(phantoms);
            }

            //spawn phantoms
            spawnPhantoms(phantoms);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0f, World.ExplosionSourceType.NONE);

        //TODO: spawn phantoms targeting nearest entity if this didnt already hit an entity
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);

            //if did not hit entity
            if (hitResult.getType() != HitResult.Type.ENTITY){
                //create phantoms
                ArrayList<TorturedPhantomEntity> phantoms = createPhantoms();
                //anger towards nearest living entity 
                angerPhantoms(phantoms);
                //spawn phantoms
                spawnPhantoms(phantoms);
            }

            this.discard();
        }
    }

    //creates phantoms at the correct location
    //points them in random directions
    private ArrayList<TorturedPhantomEntity> createPhantoms(){
        Random random = this.getWorld().getRandom();

        int numSpawned = random.nextBetween(1,3);

        ArrayList<TorturedPhantomEntity> phantomArray = new ArrayList<TorturedPhantomEntity>();

        for (int i = 0; i<numSpawned; i++){
            TorturedPhantomEntity torturedPhantomEntity = MelonSlabsEntities.TORTURED_PHANTOM.create(this.getWorld()); // EntityType.PHANTOM.create(this.getWorld());
            torturedPhantomEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), random.nextFloat(), random.nextFloat());
            torturedPhantomEntity.setPhantomSize(random.nextBetween(1,2));
            phantomArray.add(torturedPhantomEntity); //out of bounds for length zero
        }

        return (phantomArray);
    }

    //sets the phantoms to target the nearest living entity
        //phantoms will by default attack the nearest player, we need to make them attack the nearest thing because they are angry phantoms
    private void angerPhantoms(ArrayList<TorturedPhantomEntity> phantoms){
        if (phantoms.size() <= 0){
            return;
        }

        //entity selection process
            //create a target predicate that determines how the phantoms will decide their target
        TargetPredicate targetPredicate = TargetPredicate.createAttackable();
        targetPredicate.ignoreVisibility();

        //define box, will only target anything if there is a living entity in the box
        //TODO: test with really small box to see behavior if null is returned from getClosestEntity

        Box box = phantoms.get(0).getBoundingBox();

        LivingEntity entity = this.getWorld().getClosestEntity(LivingEntity.class, targetPredicate, null, this.getX(), this.getY(), this.getZ(), box);

        if (!(entity == null)){
            angerPhantoms(phantoms, entity);
        }
    }

    //sets the phantoms to target the given entity
    private void angerPhantoms(ArrayList<TorturedPhantomEntity> phantoms, LivingEntity entity){
        for (TorturedPhantomEntity phantom : phantoms){
            phantom.setTarget(entity);
        }
    }

    //spawns phantoms
    //only call on server
    private void spawnPhantoms(ArrayList<TorturedPhantomEntity> phantomArray){
        for (TorturedPhantomEntity entity : phantomArray){
            this.getWorld().spawnEntity(entity);
        }
    }
    
}
