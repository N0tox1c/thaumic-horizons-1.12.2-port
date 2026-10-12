//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities.ai;

import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import thaumcraft.common.entities.golems.EntityGolemBase;

public class EntityAIFollowPlayer extends EntityAIBase {

    private final EntityGolemBase thePet;
    private EntityLivingBase theOwner;
    World theWorld;
    private final double moveSpeed;
    private final PathNavigate petPathfinder;
    private int field_75343_h;
    float maxDist;
    float minDist;
    private float field_75344_i;

    public EntityAIFollowPlayer(final EntityGolemBase p_i1625_1_, final double p_i1625_2_, final float p_i1625_4_,
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
        final EntityLivingBase entitylivingbase = this.theWorld.getPlayerEntityByName(this.thePet.getOwnerName());
        if (entitylivingbase == null) {
            return false;
        }
        if (this.thePet.inactive) {
            return false;
        }
        if (this.thePet.getDistanceSq(entitylivingbase) < this.minDist * this.minDist) {
            return false;
        }
        this.theOwner = entitylivingbase;
        return true;
    }

    public boolean shouldContinueExecuting() {
        return !this.petPathfinder.noPath()
                && this.thePet.getDistanceSq(this.theOwner) > this.maxDist * this.maxDist
                && !this.thePet.inactive;
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
        if (!this.thePet.inactive && --this.field_75343_h <= 0) {
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
