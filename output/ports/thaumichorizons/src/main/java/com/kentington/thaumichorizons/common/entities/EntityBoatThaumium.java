//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.item.Item;
import net.minecraft.init.MobEffects;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBoatThaumium extends EntityBoat {

    private double speedMultiplier;
    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;

    @SideOnly(Side.CLIENT)
    private double velocityX;

    @SideOnly(Side.CLIENT)
    private double velocityY;

    @SideOnly(Side.CLIENT)
    private double velocityZ;

    public EntityBoatThaumium(final World p_i1704_1_) {
        super(p_i1704_1_);
        this.speedMultiplier = 0.1;
        this.preventEntitySpawning = true;
        this.setSize(1.5f, 0.6f);
        this.isImmuneToFire = true;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void entityInit() {
        super.entityInit();
    }

    public AxisAlignedBB getCollisionBox(final Entity p_70114_1_) {
        return p_70114_1_.getEntityBoundingBox();
    }

    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    public boolean canBePushed() {
        return true;
    }

    public EntityBoatThaumium(final World p_i1705_1_, final double p_i1705_2_, final double p_i1705_4_,
            final double p_i1705_6_) {
        this(p_i1705_1_);
        this.setPosition(p_i1705_2_, p_i1705_4_, p_i1705_6_);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = p_i1705_2_;
        this.prevPosY = p_i1705_4_;
        this.prevPosZ = p_i1705_6_;
    }

    public double getMountedYOffset() {
        // 1.7.10 posY was the hull centre; 1.12.2 posY is its bottom.
        return this.height / 2.0 - 0.30000001192092896;
    }

    public boolean attackEntityFrom(final DamageSource p_70097_1_, final float p_70097_2_) {
        if (this.isEntityInvulnerable(p_70097_1_)) {
            return false;
        }
        if (!this.world.isRemote && !this.isDead) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + p_70097_2_ * 10.0f);
            this.markVelocityChanged();
            final boolean flag = p_70097_1_.getTrueSource() instanceof EntityPlayer
                    && ((EntityPlayer) p_70097_1_.getTrueSource()).capabilities.isCreativeMode;
            if (flag || this.getDamageTaken() > 40.0f) {
                this.removePassengers();
                if (!flag && this.world.getGameRules().getBoolean("doEntityDrops")) {
                    this.dropItemWithOffset(ThaumicHorizons.itemBoatThaumium, 1, 0.0f);
                }
                this.setDead();
            }
            return true;
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void performHurtAnimation() {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11.0f);
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(final double p_70056_1_, final double p_70056_3_, final double p_70056_5_,
            final float p_70056_7_, final float p_70056_8_, final int p_70056_9_, final boolean teleport) {
        if (!this.isBeingRidden()) {
            this.boatPosRotationIncrements = p_70056_9_ + 5;
        } else {
            final double d3 = p_70056_1_ - this.posX;
            final double d4 = p_70056_3_ - this.posY;
            final double d5 = p_70056_5_ - this.posZ;
            final double d6 = d3 * d3 + d4 * d4 + d5 * d5;
            if (d6 <= 1.0) {
                return;
            }
            this.boatPosRotationIncrements = 3;
        }
        this.boatX = p_70056_1_;
        this.boatY = p_70056_3_;
        this.boatZ = p_70056_5_;
        this.boatYaw = p_70056_7_;
        this.boatPitch = p_70056_8_;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(final double p_70016_1_, final double p_70016_3_, final double p_70016_5_) {
        this.motionX = p_70016_1_;
        this.velocityX = p_70016_1_;
        this.motionY = p_70016_3_;
        this.velocityY = p_70016_3_;
        this.motionZ = p_70016_5_;
        this.velocityZ = p_70016_5_;
    }

    public void onUpdate() {
        this.onEntityUpdate();
        boolean isLava = false;
        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }
        if (this.getDamageTaken() > 0.0f) {
            this.setDamageTaken(this.getDamageTaken() - 1.0f);
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        final byte b0 = 5;
        double d0 = 0.0;
        for (int i = 0; i < b0; ++i) {
            final double d2 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * i / b0 - 0.125;
            final double d3 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (i + 1) / b0
                    - 0.125;
            final AxisAlignedBB axisalignedbb = new AxisAlignedBB(
                    this.getEntityBoundingBox().minX,
                    d2,
                    this.getEntityBoundingBox().minZ,
                    this.getEntityBoundingBox().maxX,
                    d3,
                    this.getEntityBoundingBox().maxZ);
            if (this.world.isMaterialInBB(axisalignedbb, Material.WATER)) {
                d0 += 1.0 / b0;
                isLava = false;
            } else if (this.world.isMaterialInBB(axisalignedbb, Material.LAVA)) {
                d0 += 1.0 / b0;
                isLava = true;
            }
        }
        final double d4 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (d4 > 0.26249999999999996) {
            final double d5 = Math.cos(this.rotationYaw * 3.141592653589793 / 180.0);
            final double d6 = Math.sin(this.rotationYaw * 3.141592653589793 / 180.0);
            for (int j = 0; j < 1.0 + d4 * 60.0; ++j) {
                final double d7 = this.rand.nextFloat() * 2.0f - 1.0f;
                final double d8 = (this.rand.nextInt(2) * 2 - 1) * 0.7;
                if (this.rand.nextBoolean()) {
                    final double d9 = this.posX - d5 * d7 * 0.8 + d6 * d8;
                    final double d10 = this.posZ - d6 * d7 * 0.8 - d5 * d8;
                    if (!isLava) {
                        this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, d9, this.posY + this.height / 2.0 - 0.125, d10, this.motionX, this.motionY, this.motionZ);
                    } else if (this.rand.nextInt(5) == 1) {
                        this.world.spawnParticle(EnumParticleTypes.LAVA, d9, this.posY + this.height / 2.0 - 0.125, d10, this.motionX, this.motionY, this.motionZ);
                    }
                } else {
                    final double d9 = this.posX + d5 + d6 * d7 * 0.7;
                    final double d10 = this.posZ + d6 - d5 * d7 * 0.7;
                    if (!isLava) {
                        this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, d9, this.posY + this.height / 2.0 - 0.125, d10, this.motionX, this.motionY, this.motionZ);
                    } else if (this.rand.nextInt(5) == 1) {
                        this.world.spawnParticle(EnumParticleTypes.LAVA, d9, this.posY + this.height / 2.0 - 0.125, d10, this.motionX, this.motionY, this.motionZ);
                    }
                }
            }
        }
        if (this.world.isRemote) {
            if (this.boatPosRotationIncrements > 0) {
                final double d5 = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
                final double d6 = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
                final double d11 = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
                final double d12 = MathHelper.wrapDegrees(this.boatYaw - this.rotationYaw);
                this.rotationYaw += (float) (d12 / this.boatPosRotationIncrements);
                this.rotationPitch += (float) ((this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(d5, d6, d11);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                final double d5 = this.posX + this.motionX;
                final double d6 = this.posY + this.motionY;
                final double d11 = this.posZ + this.motionZ;
                this.setPosition(d5, d6, d11);
                if (this.onGround) {
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                }
                this.motionX *= 0.9900000095367432;
                this.motionY *= 0.949999988079071;
                this.motionZ *= 0.9900000095367432;
            }
        } else {
            if (this.getControllingPassenger() instanceof EntityLivingBase) {
                this.getControllingPassenger().extinguish();
                ((EntityLivingBase) this.getControllingPassenger())
                        .addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 10, 0, true, true));
            }
            if (d0 < 1.0) {
                final double d5 = d0 * 2.0 - 1.0;
                this.motionY += 0.03999999910593033 * d5;
            } else {
                if (this.motionY < 0.0) {
                    this.motionY /= 2.0;
                }
                this.motionY += 0.007000000216066837;
            }
            if (this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = ((EntityLivingBase) (this.getControllingPassenger()));

                final float f = this.getControllingPassenger().rotationYaw + -entitylivingbase.moveStrafing * 90.0f;
                this.motionX += -Math.sin(f * (float)Math.PI / 180.0f) * this.speedMultiplier
                        * entitylivingbase.moveForward
                        * 0.05000000074505806;
                this.motionZ += Math.cos(f * (float)Math.PI / 180.0f) * this.speedMultiplier
                        * entitylivingbase.moveForward
                        * 0.05000000074505806;
            }
            double d5 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d5 > 0.45) {
                final double d6 = 0.45 / d5;
                this.motionX *= d6;
                this.motionZ *= d6;
                d5 = 0.45;
            }
            if (d5 > d4 && this.speedMultiplier < 0.5) {
                this.speedMultiplier += (0.5 - this.speedMultiplier) / 50.0;
                if (this.speedMultiplier > 0.5) {
                    this.speedMultiplier = 0.5;
                }
            } else {
                this.speedMultiplier -= (this.speedMultiplier - 0.1) / 50.0;
                if (this.speedMultiplier < 0.1) {
                    this.speedMultiplier = 0.1;
                }
            }
            for (int l = 0; l < 4; ++l) {
                final int i2 = MathHelper.floor(this.posX + (l % 2 - 0.5) * 0.8);
                final int j = MathHelper.floor(this.posZ + (l / 2 - 0.5) * 0.8);
                for (int j2 = 0; j2 < 2; ++j2) {
                    final int k = MathHelper.floor(this.posY + this.height / 2.0) + j2;
                    final Block block = this.world.getBlockState(new BlockPos(i2, k, j)).getBlock();
                    if (block == Blocks.SNOW_LAYER) {
                        this.world.setBlockToAir(new BlockPos(i2, k, j));
                        this.collidedHorizontally = false;
                    } else if (block == Blocks.WATERLILY) {
                        this.world.destroyBlock(new BlockPos(i2, k, j), true);
                        this.collidedHorizontally = false;
                    }
                }
            }
            if (this.onGround) {
                this.motionX *= 0.5;
                this.motionY *= 0.5;
                this.motionZ *= 0.5;
            }
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9900000095367432;
            this.motionY *= 0.949999988079071;
            this.motionZ *= 0.9900000095367432;
            this.rotationPitch = 0.0f;
            double d6 = this.rotationYaw;
            final double d11 = this.prevPosX - this.posX;
            final double d12 = this.prevPosZ - this.posZ;
            if (d11 * d11 + d12 * d12 > 0.001) {
                d6 = (float) (Math.atan2(d12, d11) * 180.0 / 3.141592653589793);
            }
            double d13 = MathHelper.wrapDegrees(d6 - this.rotationYaw);
            if (d13 > 20.0) {
                d13 = 20.0;
            }
            if (d13 < -20.0) {
                d13 = -20.0;
            }
            this.setRotation(this.rotationYaw += (float) d13, this.rotationPitch);
            if (!this.world.isRemote) {
                final List list = this.world.getEntitiesWithinAABBExcludingEntity(
                        this,
                        this.getEntityBoundingBox().grow(0.20000000298023224, 0.0, 0.20000000298023224));
                if (list != null && !list.isEmpty()) {
                    for (Object o : list) {
                        final Entity entity = (Entity) o;
                        if (entity != this.getControllingPassenger() && entity.canBePushed() && entity instanceof EntityBoat) {
                            entity.applyEntityCollision(this);
                        }
                    }
                }
                if (this.getControllingPassenger() != null && this.getControllingPassenger().isDead) {
                    this.getControllingPassenger().dismountRidingEntity();
                }
            }
        }
    }

    public void updatePassenger(final Entity passenger) {
        if (this.isPassenger(passenger)) {
            final double d0 = Math.cos(this.rotationYaw * 3.141592653589793 / 180.0) * 0.4;
            final double d2 = Math.sin(this.rotationYaw * 3.141592653589793 / 180.0) * 0.4;
            passenger.setPosition(
                    this.posX + d0,
                    this.posY + this.getMountedYOffset() + passenger.getYOffset(),
                    this.posZ + d2);
        }
    }

    protected void writeEntityToNBT(final NBTTagCompound p_70014_1_) { super.writeEntityToNBT(p_70014_1_); }

    protected void readEntityFromNBT(final NBTTagCompound p_70037_1_) { super.readEntityFromNBT(p_70037_1_); }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0f;
    }

    public boolean processInitialInteract(final EntityPlayer p_130002_1_, final EnumHand hand) {
        if (p_130002_1_.isSneaking()) return false;
        if (this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityPlayer
                && this.getControllingPassenger() != p_130002_1_) {
            return true;
        }
        if (!this.world.isRemote) {
            p_130002_1_.startRiding(this);
        }
        return true;
    }

    protected void updateFallState(final double p_70064_1_, final boolean p_70064_3_, final IBlockState state, final BlockPos pos) {
        final int i = MathHelper.floor(this.posX);
        final int j = MathHelper.floor(this.posY);
        final int k = MathHelper.floor(this.posZ);
        if (!p_70064_3_) {
            if (this.world.getBlockState(new BlockPos(i, j - 1, k)).getMaterial() != Material.WATER
                    && this.world.getBlockState(new BlockPos(i, j - 1, k)).getMaterial() != Material.LAVA
                    && p_70064_1_ < 0.0) {
                this.fallDistance -= (float) p_70064_1_;
            }
        }
    }

    public void setDamageTaken(final float p_70266_1_) {
        super.setDamageTaken(p_70266_1_);
    }

    public float getDamageTaken() {
        return super.getDamageTaken();
    }

    public void setTimeSinceHit(final int p_70265_1_) {
        super.setTimeSinceHit(p_70265_1_);
    }

    public int getTimeSinceHit() {
        return super.getTimeSinceHit();
    }

    public void setForwardDirection(final int p_70269_1_) {
        super.setForwardDirection(p_70269_1_);
    }

    public int getForwardDirection() {
        return super.getForwardDirection();
    }

    @Override
    protected boolean canFitPassenger(final Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    public boolean canPassengerSteer() {
        // Use server physics with CPacketInput, not client CPacketVehicleMove.
        return false;
    }

    @Override
    public Item getItemBoat() {
        return ThaumicHorizons.itemBoatThaumium;
    }

    public EntityItem entityDropItem(final ItemStack p_70099_1_, final float p_70099_2_) {
        if (!p_70099_1_.isEmpty()) {
            final EntityItemInvulnerable entityitem = new EntityItemInvulnerable(
                    this.world,
                    this.posX,
                    this.posY + p_70099_2_,
                    this.posZ,
                    p_70099_1_);
            entityitem.setDefaultPickupDelay();
            if (this.captureDrops) {
                this.capturedDrops.add(entityitem);
            } else {
                this.world.spawnEntity(entityitem);
            }
            return entityitem;
        }
        return null;
    }
}
