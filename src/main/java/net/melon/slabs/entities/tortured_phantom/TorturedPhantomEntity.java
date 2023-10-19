package net.melon.slabs.entities.tortured_phantom;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import net.melon.slabs.entities.MelonSlabsEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

public class TorturedPhantomEntity
extends FlyingEntity
implements Monster {
    public static final float field_30475 = 7.448451f;
    public static final int WING_FLAP_TICKS = MathHelper.ceil(24.166098f);
    private static final TrackedData<Integer> SIZE = DataTracker.registerData(TorturedPhantomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    Vec3d targetPosition = Vec3d.ZERO;
    BlockPos circlingCenter = BlockPos.ORIGIN;
    PhantomMovementType movementType = PhantomMovementType.CIRCLE;

    private final TargetPredicate ATTACKABLE_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(64.0).ignoreVisibility();


    public TorturedPhantomEntity(EntityType<? extends TorturedPhantomEntity> entityType, World world) {
        super((EntityType<? extends FlyingEntity>)entityType, world);
        this.experiencePoints = 5;
        this.moveControl = new PhantomMoveControl(this);
        this.lookControl = new PhantomLookControl(this);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6);
    }

    @Override
    public boolean isFlappingWings() {
        return (this.getWingFlapTickOffset() + this.age) % WING_FLAP_TICKS == 0;
    }

    @Override
    protected BodyControl createBodyControl() {
        return new PhantomBodyControl(this);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new StartAttackGoal());
        this.goalSelector.add(2, new SwoopMovementGoal());
        this.goalSelector.add(3, new CircleMovementGoal());
        this.targetSelector.add(1, new FindTargetGoal());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SIZE, 0);
    }

    public void setPhantomSize(int size) {
        this.dataTracker.set(SIZE, MathHelper.clamp(size, 0, 1024));
    }

    private void onSizeChanged() {
        this.calculateDimensions();
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(6 + this.getPhantomSize());
    }

    public int getPhantomSize() {
        return this.dataTracker.get(SIZE);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.35f;
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (SIZE.equals(data)) {
            this.onSizeChanged();
        }
        super.onTrackedDataSet(data);
    }

    public int getWingFlapTickOffset() {
        return this.getId() * 3;
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.method_48926().isClient) {
            float f = MathHelper.cos((float)(this.getWingFlapTickOffset() + this.age) * 7.448451f * ((float)Math.PI / 180) + (float)Math.PI);
            float g = MathHelper.cos((float)(this.getWingFlapTickOffset() + this.age + 1) * 7.448451f * ((float)Math.PI / 180) + (float)Math.PI);
            if (f > 0.0f && g <= 0.0f) {
                this.method_48926().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, this.getSoundCategory(), 0.95f + this.random.nextFloat() * 0.05f, 0.95f + this.random.nextFloat() * 0.05f, false);
            }
            int i = this.getPhantomSize();
            float h = MathHelper.cos(this.getYaw() * ((float)Math.PI / 180)) * (1.3f + 0.21f * (float)i);
            float j = MathHelper.sin(this.getYaw() * ((float)Math.PI / 180)) * (1.3f + 0.21f * (float)i);
            float k = (0.3f + f * 0.45f) * ((float)i * 0.2f + 1.0f);
            this.method_48926().addParticle(ParticleTypes.MYCELIUM, this.getX() + (double)h, this.getY() + (double)k, this.getZ() + (double)j, 0.0, 0.0, 0.0);
            this.method_48926().addParticle(ParticleTypes.MYCELIUM, this.getX() - (double)h, this.getY() + (double)k, this.getZ() - (double)j, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void tickMovement() {
        if (this.isAlive() && this.isAffectedByDaylight()) {
            this.setOnFireFor(8);
        }
        super.tickMovement();
    }

    @Override
    protected void mobTick() {
        super.mobTick();
    }

    private boolean hasValidTarget(){
        LivingEntity target = this.getTarget();

        if (target == null) {
                return false;
        }
        if (!target.isAlive()) {
                return false;
        }
        if (target instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity)target;
            if (target.isSpectator() || playerEntity.isCreative()) {
                return false;
            }
        }

        return true;
    }

    private boolean getNewTarget(){
        LivingEntity entity = TorturedPhantomEntity.this.method_48926().getClosestEntity(LivingEntity.class, ATTACKABLE_PREDICATE, TorturedPhantomEntity.this, TorturedPhantomEntity.this.getX(), TorturedPhantomEntity.this.getY(), TorturedPhantomEntity.this.getZ(), TorturedPhantomEntity.this.getBoundingBox().expand(16.0, 64.0, 16.0));


        if (!(entity == null)){

            TorturedPhantomEntity.this.setTarget(entity);
            return true;
        }

        return false;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.circlingCenter = this.getBlockPos().up(5);
        this.setPhantomSize(0);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("AX")) {
            this.circlingCenter = new BlockPos(nbt.getInt("AX"), nbt.getInt("AY"), nbt.getInt("AZ"));
        }
        this.setPhantomSize(nbt.getInt("Size"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("AX", this.circlingCenter.getX());
        nbt.putInt("AY", this.circlingCenter.getY());
        nbt.putInt("AZ", this.circlingCenter.getZ());
        nbt.putInt("Size", this.getPhantomSize());
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PHANTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PHANTOM_DEATH;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0f;
    }

    @Override
    public boolean canTarget(EntityType<?> type) {
        if (type == MelonSlabsEntities.TORTURED_PHANTOM){
            return false;
        }
        return true;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        int i = this.getPhantomSize();
        EntityDimensions entityDimensions = super.getDimensions(pose);
        float f = (entityDimensions.width + 0.2f * (float)i) / entityDimensions.width;
        return entityDimensions.scaled(f);
    }

    @Override
    public double getMountedHeightOffset() {
        return this.getStandingEyeHeight();
    }

    static enum PhantomMovementType {
        CIRCLE,
        SWOOP;

    }

    class PhantomMoveControl
    extends MoveControl {
        private float targetSpeed;
        private static final float SPEED_BOOST = 5.0f;

        public PhantomMoveControl(MobEntity owner) {
            super(owner);
            this.targetSpeed = 0.1f*SPEED_BOOST;
        }

        @Override
        public void tick() {
            if (TorturedPhantomEntity.this.horizontalCollision) {
                TorturedPhantomEntity.this.setYaw(TorturedPhantomEntity.this.getYaw() + 180.0f);
                this.targetSpeed = 0.1f*SPEED_BOOST;
            }
            double d = TorturedPhantomEntity.this.targetPosition.x - TorturedPhantomEntity.this.getX();
            double e = TorturedPhantomEntity.this.targetPosition.y - TorturedPhantomEntity.this.getY();
            double f = TorturedPhantomEntity.this.targetPosition.z - TorturedPhantomEntity.this.getZ();
            double g = Math.sqrt(d * d + f * f);
            if (Math.abs(g) > (double)1.0E-5f) {
                double h = 1.0 - Math.abs(e * (double)0.7f) / g;
                g = Math.sqrt((d *= h) * d + (f *= h) * f);
                double i = Math.sqrt(d * d + f * f + e * e);
                float j = TorturedPhantomEntity.this.getYaw();
                float k = (float)MathHelper.atan2(f, d);
                float l = MathHelper.wrapDegrees(TorturedPhantomEntity.this.getYaw() + 90.0f);
                float m = MathHelper.wrapDegrees(k * 57.295776f);
                TorturedPhantomEntity.this.setYaw(MathHelper.stepUnwrappedAngleTowards(l, m, 4.0f) - 90.0f);
                TorturedPhantomEntity.this.bodyYaw = TorturedPhantomEntity.this.getYaw();
                this.targetSpeed = MathHelper.angleBetween(j, TorturedPhantomEntity.this.getYaw()) < 3.0f ? MathHelper.stepTowards(this.targetSpeed, SPEED_BOOST*1.8f, 0.005f * (SPEED_BOOST*1.8f / this.targetSpeed)) : MathHelper.stepTowards(this.targetSpeed, SPEED_BOOST*0.2f, SPEED_BOOST*0.025f);
                float n = (float)(-(MathHelper.atan2(-e, g) * 57.2957763671875));
                TorturedPhantomEntity.this.setPitch(n);
                float o = TorturedPhantomEntity.this.getYaw() + 90.0f;
                double p = (double)(this.targetSpeed * MathHelper.cos(o * ((float)Math.PI / 180))) * Math.abs(d / i);
                double q = (double)(this.targetSpeed * MathHelper.sin(o * ((float)Math.PI / 180))) * Math.abs(f / i);
                double r = (double)(this.targetSpeed * MathHelper.sin(n * ((float)Math.PI / 180))) * Math.abs(e / i);
                Vec3d vec3d = TorturedPhantomEntity.this.getVelocity();
                //set target speed here
                TorturedPhantomEntity.this.setVelocity(vec3d.add(new Vec3d(p, r, q).subtract(vec3d).multiply(0.2)));
            }
        }
    }

    class PhantomLookControl
    extends LookControl {
        public PhantomLookControl(MobEntity entity) {
            super(entity);
        }

        @Override
        public void tick() {
        }
    }

    class PhantomBodyControl
    extends BodyControl {
        public PhantomBodyControl(MobEntity entity) {
            super(entity);
        }

        @Override
        public void tick() {
            TorturedPhantomEntity.this.headYaw = TorturedPhantomEntity.this.bodyYaw;
            TorturedPhantomEntity.this.bodyYaw = TorturedPhantomEntity.this.getYaw();
        }
    }

    class StartAttackGoal
    extends Goal {
        private int cooldown;

        StartAttackGoal() {
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = TorturedPhantomEntity.this.getTarget();
            if (livingEntity != null) {
                return TorturedPhantomEntity.this.isTarget(livingEntity, TargetPredicate.DEFAULT);
            }
            return false;
        }

        @Override
        public void start() {
            this.cooldown = this.getTickCount(3);
            TorturedPhantomEntity.this.movementType = PhantomMovementType.CIRCLE;
            this.startSwoop();
        }

        @Override
        public void stop() {
            TorturedPhantomEntity.this.circlingCenter = TorturedPhantomEntity.this.method_48926().getTopPosition(Heightmap.Type.MOTION_BLOCKING, TorturedPhantomEntity.this.circlingCenter).up(10 + TorturedPhantomEntity.this.random.nextInt(20));
        }

        @Override
        public void tick() {
            if (TorturedPhantomEntity.this.movementType == PhantomMovementType.CIRCLE) {
                --this.cooldown;
                if (this.cooldown <= 0) {
                    TorturedPhantomEntity.this.movementType = PhantomMovementType.SWOOP;
                    this.startSwoop();
                    this.cooldown = this.getTickCount((8 + TorturedPhantomEntity.this.random.nextInt(4)) * 2);
                    TorturedPhantomEntity.this.playSound(SoundEvents.ENTITY_PHANTOM_SWOOP, 10.0f, 0.95f + TorturedPhantomEntity.this.random.nextFloat() * 0.1f);
                }
            }
        }

        private void startSwoop() {
            TorturedPhantomEntity.this.circlingCenter = TorturedPhantomEntity.this.getTarget().getBlockPos().up(20 + TorturedPhantomEntity.this.random.nextInt(20));
            if (TorturedPhantomEntity.this.circlingCenter.getY() < TorturedPhantomEntity.this.method_48926().getSeaLevel()) {
                TorturedPhantomEntity.this.circlingCenter = new BlockPos(TorturedPhantomEntity.this.circlingCenter.getX(), TorturedPhantomEntity.this.method_48926().getSeaLevel() + 1, TorturedPhantomEntity.this.circlingCenter.getZ());
            }
        }
    }

    class SwoopMovementGoal
    extends MovementGoal {
        SwoopMovementGoal() {
        }

        @Override
        public boolean canStart() {
            return TorturedPhantomEntity.this.getTarget() != null && TorturedPhantomEntity.this.movementType == PhantomMovementType.SWOOP;
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity livingEntity = TorturedPhantomEntity.this.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity)livingEntity;
                if (livingEntity.isSpectator() || playerEntity.isCreative()) {
                    return false;
                }
            }
            if (!this.canStart()) {
                return false;
            }
            // if (TorturedPhantomEntity.this.age > this.nextCatCheckAge) {
            //     this.nextCatCheckAge = TorturedPhantomEntity.this.age + 20;
            //     List<Entity> list = TorturedPhantomEntity.this.getWorld().getEntitiesByClass(CatEntity.class, TorturedPhantomEntity.this.getBoundingBox().expand(16.0), EntityPredicates.VALID_ENTITY);
            //     for (CatEntity catEntity : list) {
            //         catEntity.hiss();
            //     }
            //     this.catsNearby = !list.isEmpty();
            // }
            return true;
            //return !this.catsNearby;
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            //TorturedPhantomEntity.this.setTarget(null);
            TorturedPhantomEntity.this.movementType = PhantomMovementType.CIRCLE;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = TorturedPhantomEntity.this.getTarget();
            if (livingEntity == null) {
                return;
            }
            TorturedPhantomEntity.this.targetPosition = new Vec3d(livingEntity.getX(), livingEntity.getBodyY(0.5), livingEntity.getZ());
            if (TorturedPhantomEntity.this.getBoundingBox().expand(0.2f).intersects(livingEntity.getBoundingBox())) {
                TorturedPhantomEntity.this.tryAttack(livingEntity);
                
                if (!TorturedPhantomEntity.this.hasValidTarget()){
                    TorturedPhantomEntity.this.getNewTarget();
                }

                TorturedPhantomEntity.this.movementType = PhantomMovementType.CIRCLE;
                if (!TorturedPhantomEntity.this.isSilent()) {
                    TorturedPhantomEntity.this.method_48926().syncWorldEvent(WorldEvents.PHANTOM_BITES, TorturedPhantomEntity.this.getBlockPos(), 0);
                }
            } else if (TorturedPhantomEntity.this.horizontalCollision || TorturedPhantomEntity.this.hurtTime > 0) {
                TorturedPhantomEntity.this.movementType = PhantomMovementType.CIRCLE;
            }
        }
    }

    class CircleMovementGoal
    extends MovementGoal {
        private float angle;
        private float radius;
        private float yOffset;
        private float circlingDirection;

        CircleMovementGoal() {
        }

        @Override
        public boolean canStart() {
            //return false;
            return TorturedPhantomEntity.this.getTarget() == null || TorturedPhantomEntity.this.movementType == PhantomMovementType.CIRCLE;
        }

        @Override
        public void start() {
            this.radius = 5.0f + TorturedPhantomEntity.this.random.nextFloat() * 10.0f;
            this.yOffset = -4.0f + TorturedPhantomEntity.this.random.nextFloat() * 9.0f;
            this.circlingDirection = TorturedPhantomEntity.this.random.nextBoolean() ? 1.0f : -1.0f;
            this.adjustDirection();
        }

        @Override
        public void tick() {
            if (TorturedPhantomEntity.this.random.nextInt(this.getTickCount(350)) == 0) {
                this.yOffset = -4.0f + TorturedPhantomEntity.this.random.nextFloat() * 9.0f;
            }
            if (TorturedPhantomEntity.this.random.nextInt(this.getTickCount(250)) == 0) {
                this.radius += 1.0f;
                if (this.radius > 15.0f) {
                    this.radius = 5.0f;
                    this.circlingDirection = -this.circlingDirection;
                }
            }
            if (TorturedPhantomEntity.this.random.nextInt(this.getTickCount(450)) == 0) {
                this.angle = TorturedPhantomEntity.this.random.nextFloat() * 2.0f * (float)Math.PI;
                this.adjustDirection();
            }
            if (this.isNearTarget()) {
                this.adjustDirection();
            }
            if (TorturedPhantomEntity.this.targetPosition.y < TorturedPhantomEntity.this.getY() && !TorturedPhantomEntity.this.method_48926().isAir(TorturedPhantomEntity.this.getBlockPos().down(1))) {
                this.yOffset = Math.max(1.0f, this.yOffset);
                this.adjustDirection();
            }
            if (TorturedPhantomEntity.this.targetPosition.y > TorturedPhantomEntity.this.getY() && !TorturedPhantomEntity.this.method_48926().isAir(TorturedPhantomEntity.this.getBlockPos().up(1))) {
                this.yOffset = Math.min(-1.0f, this.yOffset);
                this.adjustDirection();
            }
        }

        private void adjustDirection() {
            if (BlockPos.ORIGIN.equals(TorturedPhantomEntity.this.circlingCenter)) {
                TorturedPhantomEntity.this.circlingCenter = TorturedPhantomEntity.this.getBlockPos();
            }
            this.angle += this.circlingDirection * 15.0f * ((float)Math.PI / 180);
            TorturedPhantomEntity.this.targetPosition = Vec3d.of(TorturedPhantomEntity.this.circlingCenter).add(this.radius * MathHelper.cos(this.angle), -4.0f + this.yOffset, this.radius * MathHelper.sin(this.angle));
        }
    }

    class FindTargetGoal
    extends Goal {
        //private final TargetPredicate PLAYERS_IN_RANGE_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(64.0);

        private int delay = FindTargetGoal.toGoalTicks(20);

        FindTargetGoal() {
        }

        @Override
        public boolean canStart() {
            if (this.delay > 0) {
                --this.delay;
                return false;
            }
            this.delay = FindTargetGoal.toGoalTicks(60);
            
            if (!TorturedPhantomEntity.this.hasValidTarget()){
                return TorturedPhantomEntity.this.getNewTarget();
            } else{
                return true;
            }
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity livingEntity = TorturedPhantomEntity.this.getTarget();

            if (livingEntity != null) {
                if (!livingEntity.isAlive()){
                    return false;
                }

                return TorturedPhantomEntity.this.isTarget(livingEntity, TargetPredicate.DEFAULT);
            }
            return false;
        }
    }

    abstract class MovementGoal
    extends Goal {
        public MovementGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        protected boolean isNearTarget() {
            return TorturedPhantomEntity.this.targetPosition.squaredDistanceTo(TorturedPhantomEntity.this.getX(), TorturedPhantomEntity.this.getY(), TorturedPhantomEntity.this.getZ()) < 4.0;
        }
    }
}

