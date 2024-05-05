package com.strangesmell.mcspeed;

import com.google.common.collect.Lists;
import net.minecraft.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.strangesmell.mcspeed.Util.AN2OTime;

public class SpeedBoat extends Boat implements ISpeed ,CollisionGetter{
    private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_LEFT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_RIGHT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ID_BUBBLE_TIME = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_DAPEN = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_XIAOPEN = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_PIAOYI = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_PIAOYITIME = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_AFTERPIAOYI = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_N2O = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_COLLISION = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_ID_LEFT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_ID_RIGHT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_ID_DAPENISDOWN = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_XIAOPENISDOWN = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_PIAOYIISDOWN = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_UPISDOWN = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_DOWNISDOWN = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_LEFTISDOWN = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_RIGHTISDOWN = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ID_INAIR = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_ONLAND = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    public final RandomSource random = RandomSource.create();
    public AABB aabb ;
    private final float[] paddlePositions = new float[2];
    private float outOfControlTicks;
    private float deltaRotation;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;
    public boolean inputLeft;
    public boolean inputRight;
    public boolean inputUp;
    public boolean inputDown;
    private double waterLevel;
    private float landFriction;
    private Status status;
    private Status oldStatus;
    private double lastYd;
    private boolean isAboveBubbleColumn;
    private boolean bubbleColumnDirectionIsDown;
    private float bubbleMultiplier;
    private float bubbleAngle;
    private float bubbleAngleO;

    public SpeedBoat(EntityType<? extends Boat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(0.6f);
    }

    public SpeedBoat(Level pLevel, double pX, double pY, double pZ) {
        this(MCSpeed.SpeedBoat.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
        this.setMaxUpStep(0.6f);
    }

    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height;
    }

    protected MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_ID_HURT, 0);
        this.entityData.define(DATA_ID_HURTDIR, 1);
        this.entityData.define(DATA_ID_DAMAGE, 0.0F);
        this.entityData.define(DATA_ID_TYPE, Type.OAK.ordinal());
        this.entityData.define(DATA_ID_PADDLE_LEFT, false);
        this.entityData.define(DATA_ID_PADDLE_RIGHT, false);
        this.entityData.define(DATA_ID_BUBBLE_TIME, 0);
        this.entityData.define(DATA_ID_DAPEN, 0);
        this.entityData.define(DATA_ID_XIAOPEN, 0);
        this.entityData.define(DATA_ID_PIAOYI, 0);
        this.entityData.define(DATA_ID_PIAOYITIME, 0);
        this.entityData.define(DATA_ID_AFTERPIAOYI, 0);
        this.entityData.define(DATA_ID_N2O, 0);
        this.entityData.define(DATA_ID_COLLISION, 0);
        this.entityData.define(DATA_ID_LEFT, 0f);
        this.entityData.define(DATA_ID_RIGHT, 0f);
        this.entityData.define(DATA_ID_DAPENISDOWN, false);
        this.entityData.define(DATA_ID_XIAOPENISDOWN, false);
        this.entityData.define(DATA_ID_PIAOYIISDOWN, false);
        this.entityData.define(DATA_ID_UPISDOWN, false);
        this.entityData.define(DATA_ID_DOWNISDOWN, false);
        this.entityData.define(DATA_ID_LEFTISDOWN, false);
        this.entityData.define(DATA_ID_RIGHTISDOWN, false);
        this.entityData.define(DATA_ID_INAIR, 0);
        this.entityData.define(DATA_ID_ONLAND, 0);
    }

    public boolean canCollideWith(Entity pEntity) {
        return canVehicleCollide(this, pEntity);
    }

    public static boolean canVehicleCollide(Entity pVehicle, Entity pEntity) {
        return (pEntity.canBeCollidedWith() || pEntity.isPushable()) && !pVehicle.isPassengerOfSameVehicle(pEntity);
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    /**
     * Returns {@code true} if this entity should push and be pushed by other entities when colliding.
     */
    public boolean isPushable() {
        return true;
    }

    protected Vec3 getRelativePortalPosition(Direction.Axis pAxis, BlockUtil.FoundRectangle pPortal) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(pAxis, pPortal));
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getPassengersRidingOffset() {
        return this.getVariant() == Type.BAMBOO ? 0.25D : -0.1D;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else if (!this.level().isClientSide && !this.isRemoved()) {
            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            this.setDamage(this.getDamage() + pAmount * 10.0F);
            this.markHurt();
            this.gameEvent(GameEvent.ENTITY_DAMAGE, pSource.getEntity());
            boolean flag = pSource.getEntity() instanceof Player && ((Player)pSource.getEntity()).getAbilities().instabuild;
            if (flag || this.getDamage() > 40.0F) {
                if (!flag && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.destroy(pSource);
                }

                this.discard();
            }

            return true;
        } else {
            return true;
        }
    }

    protected void destroy(DamageSource pDamageSource) {
        this.spawnAtLocation(this.getDropItem());
    }

    public void onAboveBubbleCol(boolean pDownwards) {
        if (!this.level().isClientSide) {
            this.isAboveBubbleColumn = true;
            this.bubbleColumnDirectionIsDown = pDownwards;
            if (this.getBubbleTime() == 0) {
                this.setBubbleTime(60);
            }
        }

        this.level().addParticle(ParticleTypes.SPLASH, this.getX() + (double)this.random.nextFloat(), this.getY() + 0.7D, this.getZ() + (double)this.random.nextFloat(), 0.0D, 0.0D, 0.0D);
        if (this.random.nextInt(20) == 0) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), this.getSwimSplashSound(), this.getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat(), false);
            this.gameEvent(GameEvent.SPLASH, this.getControllingPassenger());
        }

    }

    /**
     * Applies a velocity to the entities, to push them away from each other.
     */
    public void push(Entity pEntity) {
        if (pEntity instanceof Boat) {
            if (pEntity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.push(pEntity);
            }
        } else if (pEntity.getBoundingBox().minY <= this.getBoundingBox().minY) {
            super.push(pEntity);
        }

    }

    public Item getDropItem() {
        Item item;
        switch (this.getVariant()) {
            case SPRUCE:
                item = Items.SPRUCE_BOAT;
                break;
            case BIRCH:
                item = Items.BIRCH_BOAT;
                break;
            case JUNGLE:
                item = Items.JUNGLE_BOAT;
                break;
            case ACACIA:
                item = Items.ACACIA_BOAT;
                break;
            case CHERRY:
                item = Items.CHERRY_BOAT;
                break;
            case DARK_OAK:
                item = Items.DARK_OAK_BOAT;
                break;
            case MANGROVE:
                item = Items.MANGROVE_BOAT;
                break;
            case BAMBOO:
                item = Items.BAMBOO_RAFT;
                break;
            default:
                item = Items.OAK_BOAT;
        }

        return item;
    }

    public void animateHurt(float pYaw) {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() * 11.0F);
    }

    /**
     * Returns {@code true} if other Entities should be prevented from moving through this Entity.
     */
    public boolean isPickable() {
        return !this.isRemoved();
    }

    //may should modify
    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        this.lerpX = pX;
        this.lerpY = pY;
        this.lerpZ = pZ;
        this.lerpYRot = (double)pYaw;
        this.lerpXRot = (double)pPitch;
        this.lerpSteps = 10;
    }

    /**
     * Gets the horizontal facing direction of this Entity, adjusted to take specially-treated entity types into account.
     */
    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }

    @Override
    public WorldBorder getWorldBorder() {
        return null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockGetter getChunkForCollisions(int pChunkX, int pChunkZ) {
        return null;
    }

    public boolean noCollision(@Nullable Entity pEntity, AABB pCollisionBox) {
        for(VoxelShape voxelshape : level().getBlockCollisions(pEntity, pCollisionBox)) {
            return voxelshape.isEmpty();
        }
        return false;
    }

    @Override
    public List<VoxelShape> getEntityCollisions(@org.jetbrains.annotations.Nullable Entity pEntity, AABB pCollisionBox) {
        return null;
    }

    private boolean isFree(AABB pBox) {
        return this.level().noCollision(this, pBox) && !this.level().containsAnyLiquid(pBox);
    }
    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        if(this.getFirstPassenger()!=null){
            //空喷和落地喷
            if(this.status==Status.IN_AIR) {
                setOnLand(0);
                setInAir(getInAir() + 1);
            }
            if(this.status==Status.ON_LAND) {
                setInAir(0);
                setOnLand(getOnLand() + 1);
            }

            //碰撞减少氮气
            this.aabb = this.getBoundingBox();
            if(!this.isFree(aabb.setMaxX(aabb.maxX+0.05).setMinY(aabb.minY+0.6).setMaxZ(aabb.maxZ+0.05).setMinX(aabb.minX-0.05).setMinZ(aabb.minZ-0.05))){
                if(getCollision()<=0){
                    setPiaoyiTime((int) (getPiaoyiTime()*0.5));
                    setCollision(20);
                    level().playSound((Player) (this.getControllingPassenger()),this, MCSpeed.PENGZHUANG.get(),SoundSource.PLAYERS,1,1 );

                }

            }else {
                setCollision(getCollision()-1);
            }
            //漂移结束后再增加氮气
            if(getPiaoyi()>0){

                setAfterPiaoyi(0);
                if(getPiaoyiTime()<AN2OTime) setPiaoyiTime(getPiaoyiTime()+1);
            }else{
                setAfterPiaoyi(getAfterPiaoyi()+1);
            }
            //集气
            if(getPiaoyiTime()>=AN2OTime) {
                if(getPiaoyi()<=0){
                    setPiaoyiTime(0);
                    if(getN2O()<2) setN2O(getN2O()+1);
                }
            }
            //放气
            if(getN2O()>0&& getDapenTime()<20 && getDaPenIsDown()){

                setN2O(getN2O()-1);
                this.setDapenTime(60);
                this.getControllingPassenger().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,20,2,false,false,false));
                this.getControllingPassenger().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,60,1,false,false,false));


                if(!level().isClientSide){
                    ServerLevel serverLevel =  (ServerLevel)level();
                    serverLevel.sendParticles(ParticleTypes.BUBBLE,this.getX()-0.3 + (double) this.getRandom().nextFloat()/2, this.getY() +0.25, this.getZ()-0.3 +  (double) this.getRandom().nextFloat()/10,2,this.getRandom().nextFloat()/4,this.getRandom().nextFloat()/4,this.getRandom().nextFloat()/4,this.getRandom().nextFloat());
                    serverLevel.sendParticles(ParticleTypes.BUBBLE,this.getX()+0.3 + (double) this.getRandom().nextFloat()/10, this.getY()+0.25 , this.getZ()+0.3 +  (double) this.getRandom().nextFloat()/10,2,0,0,0,0);
                    level().playSound((Player) (this.getControllingPassenger()),this, MCSpeed.DAPEN.get(),SoundSource.PLAYERS,1,1 );

                }
            }
            //小喷
            if(((getAfterPiaoyi()<15 && getAfterPiaoyi() > 0) || (getOnLand()>0&&getOnLand()<15) || (getInAir()>0&&getInAir()<15))&& getXiaoPenIsDown()){
                if(getDapenTime()<20) {
                    this.setDapenTime(20);
                    this.getControllingPassenger().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,20,1,false,false,false));
                }
                setAfterPiaoyi(15);
                if(this.status==Status.IN_AIR) setInAir(16);
                if(this.status==Status.ON_LAND) setOnLand(16);
                level().playSound((Player) (this.getControllingPassenger()),this, MCSpeed.XIAOPEN.get(),SoundSource.PLAYERS,1,1 );
            }

            if(getDapenTime()>0) setDapenTime(getDapenTime()-1);

            if(getXiaopenTime()>0) setXiaopenTime(getXiaopenTime()-1);

            if(getDapenTime()>0||getXiaopenTime()>0) {
                if(!this.level().isClientSide){
                    for(int i= 0;i<5;i++){
                        Util.sendParticle2((ServerLevel) (this.level()),this);
                    }
                }
            }
            //漂移
            if((getPiaoyi()==0 || getPiaoyi()==1 )&& getPiaoyiIsDown()){
                setPiaoyi(30);
                if(!level().isClientSide){
                    level().playSound((Player) (this.getControllingPassenger()),this, MCSpeed.PIAOYI.get(),SoundSource.PLAYERS,1,1 );

                }
            }

            //胎痕
            if(getPiaoyi()>0) {
                setPiaoyi(getPiaoyi()-1);
                if(this.level().isClientSide){
                    for(int i= 0;i<5;i++){
                        level().addParticle(ParticleTypes.DRAGON_BREATH,this.getX()-0.3+random.nextFloat()*0.1,this.getY(),this.getZ()-0.3+random.nextFloat()*0.1,0,0,0);
                        level().addParticle(ParticleTypes.DRAGON_BREATH,this.getX()+0.3+random.nextFloat()*0.1,this.getY(),this.getZ()+0.3+random.nextFloat()*0.1,0,0,0);
                    }

                }
            }
        }


        this.oldStatus = this.status;
        this.status = this.getStatus();
        this.setOnGround(this.status == Status.ON_LAND);

        if (this.status != Status.UNDER_WATER && this.status != Status.UNDER_FLOWING_WATER) {
            this.outOfControlTicks = 0.0F;
        } else {
            ++this.outOfControlTicks;
        }

        if (!this.level().isClientSide && this.outOfControlTicks >= 60.0F) {
            this.ejectPassengers();
        }

        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        this.baseTick();
        this.tickLerp();
        if (this.isControlledByLocalInstance()) {
            if (!(this.getFirstPassenger() instanceof Player)) {
                this.setPaddleState(false, false);
            }

            this.floatBoat();
            if (this.level().isClientSide) {
                this.controlBoat();
                this.level().sendPacketToServer(new ServerboundPaddleBoatPacket(this.getPaddleState(0), this.getPaddleState(1)));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }

        this.tickBubbleColumn();

        for(int i = 0; i <= 1; ++i) {
            if (this.getPaddleState(i)) {
                if (!this.isSilent() && (double)(this.paddlePositions[i] % ((float)Math.PI * 2F)) <= (double)((float)Math.PI / 4F) && (double)((this.paddlePositions[i] + ((float)Math.PI / 8F)) % ((float)Math.PI * 2F)) >= (double)((float)Math.PI / 4F)) {
                    SoundEvent soundevent = this.getPaddleSound();
                    if (soundevent != null) {
                        Vec3 vec3 = this.getViewVector(1.0F);
                        double d0 = i == 1 ? -vec3.z : vec3.z;
                        double d1 = i == 1 ? vec3.x : -vec3.x;
                        this.level().playSound((Player)null, this.getX() + d0, this.getY(), this.getZ() + d1, soundevent, this.getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
                    }
                }

                this.paddlePositions[i] += ((float)Math.PI / 8F);
            } else {
                this.paddlePositions[i] = 0.0F;
            }
        }

        this.checkInsideBlocks();
        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate((double)0.2F, (double)-0.01F, (double)0.2F), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            boolean flag = !this.level().isClientSide && !(this.getControllingPassenger() instanceof Player);

            for(int j = 0; j < list.size(); ++j) {
                Entity entity = list.get(j);
                if (!entity.hasPassenger(this)) {
                    if (flag && this.getPassengers().size() < this.getMaxPassengers() && !entity.isPassenger() && this.hasEnoughSpaceFor(entity) && entity instanceof LivingEntity && !(entity instanceof WaterAnimal) && !(entity instanceof Player)) {
                        entity.startRiding(this);
                    } else {
                        this.push(entity);
                    }
                }
            }
        }

    }

    private void tickBubbleColumn() {
        if (this.level().isClientSide) {
            int i = this.getBubbleTime();
            if (i > 0) {
                this.bubbleMultiplier += 0.05F;
            } else {
                this.bubbleMultiplier -= 0.1F;
            }

            this.bubbleMultiplier = Mth.clamp(this.bubbleMultiplier, 0.0F, 1.0F);
            this.bubbleAngleO = this.bubbleAngle;
            this.bubbleAngle = 10.0F * (float)Math.sin((double)(0.5F * (float)this.level().getGameTime())) * this.bubbleMultiplier;
        } else {
            if (!this.isAboveBubbleColumn) {
                this.setBubbleTime(0);
            }

            int k = this.getBubbleTime();
            if (k > 0) {
                --k;
                this.setBubbleTime(k);
                int j = 60 - k - 1;
                if (j > 0 && k == 0) {
                    this.setBubbleTime(0);
                    Vec3 vec3 = this.getDeltaMovement();
                    if (this.bubbleColumnDirectionIsDown) {
                        this.setDeltaMovement(vec3.add(0.0D, -0.7D, 0.0D));
                        this.ejectPassengers();
                    } else {
                        this.setDeltaMovement(vec3.x, this.hasPassenger((p_150274_) -> {
                            return p_150274_ instanceof Player;
                        }) ? 2.7D : 0.6D, vec3.z);
                    }
                }

                this.isAboveBubbleColumn = false;
            }
        }

    }
    //should modify
    @Nullable
    protected SoundEvent getPaddleSound() {
        switch (this.getStatus()) {
            case IN_WATER:
            case UNDER_WATER:
            case UNDER_FLOWING_WATER:
                return SoundEvents.BOAT_PADDLE_WATER;
            case ON_LAND:
                return SoundEvents.BOAT_PADDLE_LAND;
            case IN_AIR:
            default:
                return null;
        }
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
            double d3 = Mth.wrapDegrees(this.lerpYRot - (double)this.getYRot());
            this.setYRot(this.getYRot() + (float)d3 / (float)this.lerpSteps);
            this.setXRot(this.getXRot() + (float)(this.lerpXRot - (double)this.getXRot()) / (float)this.lerpSteps);
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRot(this.getYRot(), this.getXRot());
        }
    }
    //may should modify
    public void setPaddleState(boolean pLeft, boolean pRight) {
        this.entityData.set(DATA_ID_PADDLE_LEFT, pLeft);
        this.entityData.set(DATA_ID_PADDLE_RIGHT, pRight);
    }
    //may should modify
    public float getRowingTime(int pSide, float pLimbSwing) {
        return this.getPaddleState(pSide) ? Mth.clampedLerp(this.paddlePositions[pSide] - ((float)Math.PI / 8F), this.paddlePositions[pSide], pLimbSwing) : 0.0F;
    }
    //may should modify
    /**
     * Determines whether the boat is in water, gliding on land, or in air
     */
    private Status getStatus() {
        Status boat$status = this.isUnderwater();
        if (boat$status != null) {
            this.waterLevel = this.getBoundingBox().maxY;
            return boat$status;
        } else if (this.checkInWater()) {
            return Status.IN_WATER;
        } else {
            float f = this.getGroundFriction();
            if (f > 0.0F) {
                this.landFriction = f;
                return Status.ON_LAND;
            } else {
                return Status.IN_AIR;
            }
        }
    }

    public float getWaterLevelAbove() {
        AABB aabb = this.getBoundingBox();
        int i = Mth.floor(aabb.minX);
        int j = Mth.ceil(aabb.maxX);
        int k = Mth.floor(aabb.maxY);
        int l = Mth.ceil(aabb.maxY - this.lastYd);
        int i1 = Mth.floor(aabb.minZ);
        int j1 = Mth.ceil(aabb.maxZ);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        label39:
        for(int k1 = k; k1 < l; ++k1) {
            float f = 0.0F;

            for(int l1 = i; l1 < j; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutableblockpos.set(l1, k1, i2);
                    FluidState fluidstate = this.level().getFluidState(blockpos$mutableblockpos);
                    if (this.canBoatInFluid(fluidstate)) {
                        f = Math.max(f, fluidstate.getHeight(this.level(), blockpos$mutableblockpos));
                    }

                    if (f >= 1.0F) {
                        continue label39;
                    }
                }
            }

            if (f < 1.0F) {
                return (float)blockpos$mutableblockpos.getY() + f;
            }
        }

        return (float)(l + 1);
    }
    //may should modify
    /**
     * Decides how much the boat should be gliding on the land (based on any slippery blocks)
     */
    public float getGroundFriction() {
        AABB aabb = this.getBoundingBox();
        AABB aabb1 = new AABB(aabb.minX, aabb.minY - 0.001D, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ);
        int i = Mth.floor(aabb1.minX) - 1;
        int j = Mth.ceil(aabb1.maxX) + 1;
        int k = Mth.floor(aabb1.minY) - 1;
        int l = Mth.ceil(aabb1.maxY) + 1;
        int i1 = Mth.floor(aabb1.minZ) - 1;
        int j1 = Mth.ceil(aabb1.maxZ) + 1;
        VoxelShape voxelshape = Shapes.create(aabb1);
        float f = 0.0F;
        int k1 = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int l1 = i; l1 < j; ++l1) {
            for(int i2 = i1; i2 < j1; ++i2) {
                int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
                if (j2 != 2) {
                    for(int k2 = k; k2 < l; ++k2) {
                        if (j2 <= 0 || k2 != k && k2 != l - 1) {
                            blockpos$mutableblockpos.set(l1, k2, i2);
                            BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
                            if (!(blockstate.getBlock() instanceof WaterlilyBlock) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level(), blockpos$mutableblockpos).move((double)l1, (double)k2, (double)i2), voxelshape, BooleanOp.AND)) {
                                f += blockstate.getFriction(this.level(), blockpos$mutableblockpos, this);
                                ++k1;
                            }
                        }
                    }
                }
            }
        }

        return f / (float)k1;
    }

    private boolean checkInWater() {
        AABB aabb = this.getBoundingBox();
        int i = Mth.floor(aabb.minX);
        int j = Mth.ceil(aabb.maxX);
        int k = Mth.floor(aabb.minY);
        int l = Mth.ceil(aabb.minY + 0.001D);
        int i1 = Mth.floor(aabb.minZ);
        int j1 = Mth.ceil(aabb.maxZ);
        boolean flag = false;
        this.waterLevel = -Double.MAX_VALUE;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutableblockpos.set(k1, l1, i2);
                    FluidState fluidstate = this.level().getFluidState(blockpos$mutableblockpos);
                    if (this.canBoatInFluid(fluidstate)) {
                        float f = (float)l1 + fluidstate.getHeight(this.level(), blockpos$mutableblockpos);
                        this.waterLevel = Math.max((double)f, this.waterLevel);
                        flag |= aabb.minY < (double)f;
                    }
                }
            }
        }

        return flag;
    }

    /**
     * Decides whether the boat is currently underwater.
     */
    @Nullable
    private Status isUnderwater() {
        AABB aabb = this.getBoundingBox();
        double d0 = aabb.maxY + 0.001D;
        int i = Mth.floor(aabb.minX);
        int j = Mth.ceil(aabb.maxX);
        int k = Mth.floor(aabb.maxY);
        int l = Mth.ceil(d0);
        int i1 = Mth.floor(aabb.minZ);
        int j1 = Mth.ceil(aabb.maxZ);
        boolean flag = false;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutableblockpos.set(k1, l1, i2);
                    FluidState fluidstate = this.level().getFluidState(blockpos$mutableblockpos);
                    if (this.canBoatInFluid(fluidstate) && d0 < (double)((float)blockpos$mutableblockpos.getY() + fluidstate.getHeight(this.level(), blockpos$mutableblockpos))) {
                        if (!fluidstate.isSource()) {
                            return Status.UNDER_FLOWING_WATER;
                        }

                        flag = true;
                    }
                }
            }
        }

        return flag ? Status.UNDER_WATER : null;
    }

    private void floatBoat(){
        Vec3 vec31 = this.getDeltaMovement();
        if(this.status== Status.ON_LAND){
            //this.setDeltaMovement(vec31.x + Mth.sin(-this.getYRot() * ((float)Math.PI / 180F)) * getFA(), 0, vec31.z +Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * getFA());
            double v2 = Math.pow(this.getDeltaMovement().x,2) + Math.pow(this.getDeltaMovement().z,2) ;
            double v = Math.pow(v2,0.5);
            double v97 = Math.pow(v2,0.5)*0.94;
            double v98 = Math.pow(v2,0.5)*0.98;

            if(this.getPiaoyi()>0){
                this.setDeltaMovement(vec31.x *0.8*0.94+Mth.sin(-this.getYRot() * ((float)Math.PI / 180F)) * v97 * 0.2, 0, vec31.z*0.8*0.94+Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * v97*0.2);
                //this.setDeltaMovement(vec31.x *0., 0, vec31.z*0.97);

            }else{
                this.setDeltaMovement(Mth.sin(-this.getYRot() * ((float)Math.PI / 180F)) * v97, 0.0D, (double)(Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * v97));

            }

        }else {
            if(this.status== Status.IN_AIR){
                this.setDeltaMovement(vec31.x, vec31.y - 0.2, vec31.z);
            }
        }
        this.deltaRotation *= 0.85;
    }
    public RandomSource getRandom(){
        return this.random;
    }
    //may should modify, in client
    private void controlBoat() {

        if (this.isVehicle()) {
            inputUp= getUpIsDown();
            inputDown= getDownIsDown();
            inputLeft= getLeftIsDown();
            inputRight= getRightIsDown();
            Vec3 vec31 = this.getDeltaMovement();
            float f = 0.0F;
            if(getPiaoyi()>0){
                if(this.status==Status.ON_LAND) this.setDeltaMovement(vec31.x *0.97, 0, vec31.z*0.97);
                else this.setDeltaMovement(vec31.x , vec31.y-0.2, vec31.z);
                if (this.inputLeft) {
                    deltaRotation = deltaRotation-1.5f;
                    if(getLeft()<=29) setLeft(getLeft()+1f);
                }else {
                    if(getLeft()>=2) setLeft(getLeft() - 2);
                    else if(getLeft()==1) setLeft(0);
                }

                if (this.inputRight) {
                    deltaRotation = deltaRotation+1.5f;
                    if(getRight()<=29) setRight(getRight()+1f);
                }else {
                    if(getRight()>=2) setRight(getRight() - 2);
                    else if(getRight()==1) setRight(getRight() - 1);

                }

                if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                    f += 0.01F;
                }

                this.setYRot(this.getYRot() + this.deltaRotation);
                if (this.inputUp) {

                    f += (getF()+getBaseF())/getMass()/20;
                }

                if (this.inputDown) {
                    f -= 0.01F;
                }
            }else  {
                if (this.inputLeft) {
                    --this.deltaRotation;
                    if(getLeft()<=29) setLeft(getLeft()+1f);
                }else {
                    if(getLeft()>=2) setLeft(getLeft() - 2);
                    else if(getLeft()==1) setLeft(0);
                }
                if (this.inputRight) {
                    ++this.deltaRotation;
                    if(getRight()<=29) setRight(getRight()+1);
                }else {
                    if(getRight()>=2) setRight(getRight() - 2);
                    else if(getRight()==1) setRight(getRight() - 1);

                }

                if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                    f += 0.005F;
                }

                this.setYRot(this.getYRot() + this.deltaRotation);
                if (this.inputUp) {

                    f += (getF()+getBaseF())/getMass()/20;
                }

                if (this.inputDown) {
                    f -= 0.01F;
                }
            }




            this.setDeltaMovement(this.getDeltaMovement().add((double)(Mth.sin(-this.getYRot() * ((float)Math.PI / 180F)) * f), 0.0D, (double)(Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * f)));
            this.setPaddleState(this.inputRight && !this.inputLeft || this.inputUp, this.inputLeft && !this.inputRight || this.inputUp);
        }
    }

    protected float getSinglePassengerXOffset() {
        return 0.0F;
    }

    public boolean hasEnoughSpaceFor(Entity pEntity) {
        return pEntity.getBbWidth() < this.getBbWidth();
    }

    protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
        if (this.hasPassenger(pPassenger)) {
            float f = this.getSinglePassengerXOffset();
            float f1 = (float)((this.isRemoved() ? (double)0.01F : this.getPassengersRidingOffset()) + pPassenger.getMyRidingOffset());
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(pPassenger);
                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }

                if (pPassenger instanceof Animal) {
                    f += 0.2F;
                }
            }

            Vec3 vec3 = (new Vec3((double)f, 0.0D, 0.0D)).yRot(-this.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            pCallback.accept(pPassenger, this.getX() + vec3.x, this.getY() + (double)f1, this.getZ() + vec3.z);
            pPassenger.setYRot(pPassenger.getYRot() + this.deltaRotation);
            pPassenger.setYHeadRot(pPassenger.getYHeadRot() + this.deltaRotation);
            this.clampRotation(pPassenger);
            if (pPassenger instanceof Animal && this.getPassengers().size() == this.getMaxPassengers()) {
                int j = pPassenger.getId() % 2 == 0 ? 90 : 270;
                pPassenger.setYBodyRot(((Animal)pPassenger).yBodyRot + (float)j);
                pPassenger.setYHeadRot(pPassenger.getYHeadRot() + (float)j);
            }

        }
    }

    public Vec3 getDismountLocationForPassenger(LivingEntity pLivingEntity) {
        Vec3 vec3 = getCollisionHorizontalEscapeVector((double)(this.getBbWidth() * Mth.SQRT_OF_TWO), (double)pLivingEntity.getBbWidth(), pLivingEntity.getYRot());
        double d0 = this.getX() + vec3.x;
        double d1 = this.getZ() + vec3.z;
        BlockPos blockpos = BlockPos.containing(d0, this.getBoundingBox().maxY, d1);
        BlockPos blockpos1 = blockpos.below();
        if (!this.level().isWaterAt(blockpos1)) {
            List<Vec3> list = Lists.newArrayList();
            double d2 = this.level().getBlockFloorHeight(blockpos);
            if (DismountHelper.isBlockFloorValid(d2)) {
                list.add(new Vec3(d0, (double)blockpos.getY() + d2, d1));
            }

            double d3 = this.level().getBlockFloorHeight(blockpos1);
            if (DismountHelper.isBlockFloorValid(d3)) {
                list.add(new Vec3(d0, (double)blockpos1.getY() + d3, d1));
            }

            for(Pose pose : pLivingEntity.getDismountPoses()) {
                for(Vec3 vec31 : list) {
                    if (DismountHelper.canDismountTo(this.level(), vec31, pLivingEntity, pose)) {
                        pLivingEntity.setPose(pose);
                        return vec31;
                    }
                }
            }
        }

        return super.getDismountLocationForPassenger(pLivingEntity);
    }
    //may should modify
    /**
     * Applies this boat's yaw to the given entity. Used to update the orientation of its passenger.
     */
    protected void clampRotation(Entity pEntityToUpdate) {
        pEntityToUpdate.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(pEntityToUpdate.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        pEntityToUpdate.yRotO += f1 - f;
        pEntityToUpdate.setYRot(pEntityToUpdate.getYRot() + f1 - f);
        pEntityToUpdate.setYHeadRot(pEntityToUpdate.getYRot());
    }
    //may should modify
    /**
     * Applies this entity's orientation to another entity. Used to update passenger orientation.
     */
    public void onPassengerTurned(Entity pEntityToUpdate) {
        this.clampRotation(pEntityToUpdate);
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putString("Type", this.getVariant().getSerializedName());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Type", 8)) {
            this.setVariant(Type.byName(pCompound.getString("Type")));
        }

    }

    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (this.outOfControlTicks < 60.0F) {
            if (!this.level().isClientSide) {
                if(pPlayer.startRiding(this) ){
                    for(Player player : level().players()){
                        player.sendSystemMessage(Component.literal(pPlayer.getName().getString()).append(Component.translatable("mcspeed.set")).append(Component.translatable("entity.mcspeed.speed_boat")));
                    }
                }
                return pPlayer.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
        this.lastYd = this.getDeltaMovement().y;
        if (!this.isPassenger()) {
            if (pOnGround) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != Status.ON_LAND) {
                        this.resetFallDistance();
                        return;
                    }

                    this.causeFallDamage(this.fallDistance, 1.0F, this.damageSources().fall());
                    if (!this.level().isClientSide && !this.isRemoved()) {
                        this.kill();
                        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for(int i = 0; i < 3; ++i) {
                                this.spawnAtLocation(this.getVariant().getPlanks());
                            }

                            for(int j = 0; j < 2; ++j) {
                                this.spawnAtLocation(Items.STICK);
                            }
                        }
                    }
                }

                this.resetFallDistance();
            } else if (!this.canBoatInFluid(this.level().getFluidState(this.blockPosition().below())) && pY < 0.0D) {
                this.fallDistance -= (float)pY;
            }

        }
    }

    public boolean getPaddleState(int pSide) {
        return this.entityData.<Boolean>get(pSide == 0 ? DATA_ID_PADDLE_LEFT : DATA_ID_PADDLE_RIGHT) && this.getControllingPassenger() != null;
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamage(float pDamageTaken) {
        this.entityData.set(DATA_ID_DAMAGE, pDamageTaken);
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamage() {
        return this.entityData.get(DATA_ID_DAMAGE);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setHurtTime(int pHurtTime) {
        this.entityData.set(DATA_ID_HURT, pHurtTime);
    }

    /**
     * Gets the time since the last hit.
     */
    public int getHurtTime() {
        return this.entityData.get(DATA_ID_HURT);
    }

    private void setBubbleTime(int pBubbleTime) {
        this.entityData.set(DATA_ID_BUBBLE_TIME, pBubbleTime);
    }

    private int getBubbleTime() {
        return this.entityData.get(DATA_ID_BUBBLE_TIME);
    }

    public float getBubbleAngle(float pPartialTicks) {
        return Mth.lerp(pPartialTicks, this.bubbleAngleO, this.bubbleAngle);
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setHurtDir(int pHurtDirection) {
        this.entityData.set(DATA_ID_HURTDIR, pHurtDirection);
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getHurtDir() {
        return this.entityData.get(DATA_ID_HURTDIR);
    }

    public void setVariant(Type pVariant) {
        this.entityData.set(DATA_ID_TYPE, pVariant.ordinal());
    }

    public Type getVariant() {
        return Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < this.getMaxPassengers() && !this.canBoatInFluid(this.getEyeInFluidType());
    }

    public void setDapenTime(int dapenTime) {
        this.entityData.set(DATA_ID_DAPEN, dapenTime);
    }

    public int getDapenTime() {
        return this.entityData.get(DATA_ID_DAPEN);
    }

    public void setXiaopenTime(int xiaopenTime) {
        this.entityData.set(DATA_ID_XIAOPEN, xiaopenTime);
    }

    public int getXiaopenTime() {
        return this.entityData.get(DATA_ID_XIAOPEN);
    }


    public void setPiaoyi(int piaoyi) {
        this.entityData.set(DATA_ID_PIAOYI, piaoyi);
    }

    public int getPiaoyi() {
        return this.entityData.get(DATA_ID_PIAOYI);
    }

    public void setPiaoyiTime(int piaoyi) {
        this.entityData.set(DATA_ID_PIAOYITIME, piaoyi);
    }

    public int getPiaoyiTime() {
        return this.entityData.get(DATA_ID_PIAOYITIME);
    }

    public void setAfterPiaoyi(int piaoyi) {
        this.entityData.set(DATA_ID_AFTERPIAOYI, piaoyi);
    }

    public int getAfterPiaoyi() {
        return this.entityData.get(DATA_ID_AFTERPIAOYI);
    }

    public void setN2O(int n2O) {
        this.entityData.set(DATA_ID_N2O, n2O);
    }

    public int getN2O() {
        return this.entityData.get(DATA_ID_N2O);
    }

    public void setCollision(int collision) {
        this.entityData.set(DATA_ID_COLLISION, collision);
    }

    public int getCollision() {
        return this.entityData.get(DATA_ID_COLLISION);
    }

    public void setLeft(float left) {
        this.entityData.set(DATA_ID_LEFT, left);
    }

    public float getLeft() {
        return this.entityData.get(DATA_ID_LEFT);
    }

    public void setRight(float right) {
        this.entityData.set(DATA_ID_RIGHT, right);
    }

    public float getRight() {
        return this.entityData.get(DATA_ID_RIGHT);
    }

    public void setPiaoYiIsDown(boolean isDown) {
        this.entityData.set(DATA_ID_PIAOYIISDOWN, isDown);
    }

    public boolean getPiaoyiIsDown() {
        return this.entityData.get(DATA_ID_PIAOYIISDOWN);
    }

    public void setDaPenIsDown(boolean isDown) {
        this.entityData.set(DATA_ID_DAPENISDOWN, isDown);
    }

    public boolean getDaPenIsDown() {
        return this.entityData.get(DATA_ID_DAPENISDOWN);
    }
    public void setXiaoPenIsDown(boolean isDown) {
        this.entityData.set(DATA_ID_XIAOPENISDOWN, isDown);
    }

    public boolean getXiaoPenIsDown() {
        return this.entityData.get(DATA_ID_XIAOPENISDOWN);
    }
    public void setUpIsDown(boolean isDown) {
        this.entityData.set(DATA_ID_UPISDOWN, isDown);
    }

    public boolean getUpIsDown() {
        return this.entityData.get(DATA_ID_UPISDOWN);
    }
    public void setDownIsDown(boolean isDown) {
        this.entityData.set(DATA_ID_DOWNISDOWN, isDown);
    }

    public boolean getDownIsDown() {
        return this.entityData.get(DATA_ID_DOWNISDOWN);
    }
    public void setLeftIsDown(boolean isDown) {
        this.entityData.set(DATA_ID_LEFTISDOWN, isDown);
    }

    public boolean getLeftIsDown() {
        return this.entityData.get(DATA_ID_LEFTISDOWN);
    }
    public void setRightIsDown(boolean isDown) {
        this.entityData.set(DATA_ID_RIGHTISDOWN, isDown);
    }

    public boolean getRightIsDown() {
        return this.entityData.get(DATA_ID_RIGHTISDOWN);
    }

    public void setInAir(int time) {
        this.entityData.set(DATA_ID_INAIR, time);
    }

    public int getInAir() {
        return this.entityData.get(DATA_ID_INAIR);
    }

    public void setOnLand(int time) {
        this.entityData.set(DATA_ID_ONLAND, time);
    }

    public int getOnLand() {
        return this.entityData.get(DATA_ID_ONLAND);
    }

    protected int getMaxPassengers() {
        return 2;
    }

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        LivingEntity livingentity1;
        if (entity instanceof LivingEntity livingentity) {
            livingentity1 = livingentity;
        } else {
            livingentity1 = null;
        }

        return livingentity1;
    }

    public void setInput(boolean pInputLeft, boolean pInputRight, boolean pInputUp, boolean pInputDown) {
        this.inputLeft = pInputLeft;
        this.inputRight = pInputRight;
        this.inputUp = pInputUp;
        this.inputDown = pInputDown;
    }

    public boolean isUnderWater() {
        return this.status == Status.UNDER_WATER || this.status == Status.UNDER_FLOWING_WATER;
    }

    // Forge: Fix MC-119811 by instantly completing lerp on board
    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.isControlledByLocalInstance() && this.lerpSteps > 0) {
            this.lerpSteps = 0;
            this.absMoveTo(this.lerpX, this.lerpY, this.lerpZ, (float)this.lerpYRot, (float)this.lerpXRot);
        }
    }

    public ItemStack getPickResult() {
        return new ItemStack(this.getDropItem());
    }

    @Override
    public float getF() {
        float f =0;
        if(getDapenTime()>0 || getXiaopenTime()>0) return 700;
        else return 0;
    }

    @Override
    public float getBaseF() {
        return 420;
    }

    @Override
    public float getMass() {
        return 700;
    }
    public float getFA(){
        Vec3 vec3 = this.getDeltaMovement();
        double vv = vec3.x* vec3.x + vec3.z* vec3.z;
        return -getGroundFriction()*getMass();
    }
    @Override
    public int getSpeedTime() {
        return 60;//tick
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pPos) {
        return null;
    }

    @Override
    public BlockState getBlockState(BlockPos p_45571_) {
        return null;
    }

    @Override
    public FluidState getFluidState(BlockPos pPos) {
        return null;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getMinBuildHeight() {
        return 0;
    }
}
