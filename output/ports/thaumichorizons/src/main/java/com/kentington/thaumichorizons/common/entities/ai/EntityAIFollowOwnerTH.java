//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities.ai;

import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.lib.EntityInfusionProperties;

public class EntityAIFollowOwnerTH extends EntityAIBase {

    private final EntityLiving thePet;
    private EntityLivingBase theOwner;
    World theWorld;
    private final double moveSpeed;
    private final PathNavigate petPathfinder;
    private int field_75343_h;
    float maxDist;
    float minDist;
    private float field_75344_i;

    public EntityAIFollowOwnerTH(final EntityLiving p_i1625_1_, final double p_i1625_2_, final float p_i1625_4_,
            final float p_i1625_5_) {
        this.thePet = p_i1625_1_;
        this.theWorld = p_i1625_1_.world;
        this.moveSpeed = p_i1625_2_;
        this.petPathfinder = p_i1625_1_.getNavigator();
        this.minDist = p_i1625_4_;
        this.maxDist = p_i1625_5_;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.thePet);
        final EntityLivingBase entitylivingbase = this.thePet.world.getPlayerEntityByName(prop.getOwner());
        if (entitylivingbase == null) {
            return false;
        }
        if (prop.isSitting()) {
            return false;
        }
        if (this.thePet.getDistanceSq(entitylivingbase) < this.minDist * this.minDist) {
            return false;
        }
        this.theOwner = entitylivingbase;
        return true;
    }

    public boolean shouldContinueExecuting() {
        final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.thePet);
        return !this.petPathfinder.noPath()
                && this.thePet.getDistanceSq(this.theOwner) > this.maxDist * this.maxDist
                && !prop.isSitting();
    }

    public void startExecuting() {
        this.field_75343_h = 0;
        this.field_75344_i = this.thePet.getPathPriority(net.minecraft.pathfinding.PathNodeType.WATER);
        this.thePet.setPathPriority(net.minecraft.pathfinding.PathNodeType.WATER, 0.0f);
    }

    public void resetTask() {
        this.theOwner = null;
        this.petPathfinder.clearPath();
        this.thePet.setPathPriority(net.minecraft.pathfinding.PathNodeType.WATER, this.field_75344_i);
    }

    public void updateTask() {
        this.thePet.getLookHelper()
                .setLookPositionWithEntity(this.theOwner, 10.0f, (float) this.thePet.getVerticalFaceSpeed());
        final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.thePet);
        if (!prop.isSitting() && --this.field_75343_h <= 0) {
            this.field_75343_h = 10;
            if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.moveSpeed) && !this.thePet.getLeashed()
                    && this.thePet.getDistanceSq(this.theOwner) >= 144.0) {
                final int i = MathHelper.floor(this.theOwner.posX) - 2;
                final int j = MathHelper.floor(this.theOwner.posZ) - 2;
                final int k = MathHelper.floor(this.theOwner.getEntityBoundingBox().minY);
                for (int l = 0; l <= 4; ++l) {
                    for (int i2 = 0; i2 <= 4; ++i2) {
                        if ((l < 1 || i2 < 1 || l > 3 || i2 > 3)
                                && this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i2)).isSideSolid(this.theWorld, new BlockPos(i + l, k - 1, j + i2), net.minecraft.util.EnumFacing.UP)
                                && !this.theWorld.getBlockState(new BlockPos(i + l, k, j + i2)).isNormalCube()
                                && !this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i2)).isNormalCube()) {
                            this.thePet.setLocationAndAngles(
                                    i + l + 0.5f,
                                    k,
                                    j + i2 + 0.5f,
                                    this.thePet.rotationYaw,
                                    this.thePet.rotationPitch);
                            this.petPathfinder.clearPath();
                            return;
                        }
                    }
                }
            }
        }
    }
}
