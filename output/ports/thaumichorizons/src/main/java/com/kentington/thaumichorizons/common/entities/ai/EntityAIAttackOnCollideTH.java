//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityAIAttackOnCollideTH extends EntityAIBase {

    World worldObj;
    EntityLiving attacker;
    int attackTick;
    double speedTowardsTarget;
    boolean longMemory;
    Path entityPathEntity;
    Class classTarget;
    private int field_75445_i;
    private double field_151497_i;
    private double field_151495_j;
    private double field_151496_k;
    private int failedPathFindingPenalty;

    public EntityAIAttackOnCollideTH(final EntityLiving p_i1635_1_, final Class p_i1635_2_, final double p_i1635_3_,
            final boolean p_i1635_5_) {
        this(p_i1635_1_, p_i1635_3_, p_i1635_5_);
        this.classTarget = p_i1635_2_;
    }

    public EntityAIAttackOnCollideTH(final EntityLiving p_i1636_1_, final double p_i1636_2_, final boolean p_i1636_4_) {
        this.attacker = p_i1636_1_;
        this.worldObj = p_i1636_1_.world;
        this.speedTowardsTarget = p_i1636_2_;
        this.longMemory = p_i1636_4_;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        if (this.classTarget != null && !this.classTarget.isAssignableFrom(entitylivingbase.getClass())) {
            return false;
        }
        if (--this.field_75445_i <= 0) {
            this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
            this.field_75445_i = 4 + this.attacker.getRNG().nextInt(7);
            return this.entityPathEntity != null;
        }
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        final EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive()
                && (this.longMemory || !this.attacker.getNavigator().noPath());
    }

    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.field_75445_i = 0;
    }

    public void resetTask() {
        this.attacker.getNavigator().clearPath();
    }

    public void updateTask() {
        final EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0f, 30.0f);
        final double d0 = this.attacker
                .getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        final double d2 = this.attacker.width * 2.0f * this.attacker.width * 2.0f + entitylivingbase.width;
        --this.field_75445_i;
        if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && this.field_75445_i <= 0
                && ((this.field_151497_i == 0.0 && this.field_151495_j == 0.0 && this.field_151496_k == 0.0)
                        || entitylivingbase.getDistanceSq(this.field_151497_i, this.field_151495_j, this.field_151496_k)
                                >= 1.0
                        || this.attacker.getRNG().nextFloat() < 0.05f)) {
            this.field_151497_i = entitylivingbase.posX;
            this.field_151495_j = entitylivingbase.getEntityBoundingBox().minY;
            this.field_151496_k = entitylivingbase.posZ;
            this.field_75445_i = this.failedPathFindingPenalty + 4 + this.attacker.getRNG().nextInt(7);
            if (this.attacker.getNavigator().getPath() != null) {
                final PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                if (finalPathPoint != null && entitylivingbase
                        .getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1.0) {
                    this.failedPathFindingPenalty = 0;
                } else {
                    this.failedPathFindingPenalty += 10;
                }
            } else {
                this.failedPathFindingPenalty += 10;
            }
            if (d0 > 1024.0) {
                this.field_75445_i += 10;
            } else if (d0 > 256.0) {
                this.field_75445_i += 5;
            }
            if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget)) {
                this.field_75445_i += 15;
            }
        }
        this.attackTick = Math.max(this.attackTick - 1, 0);
        if (d0 <= d2 && this.attackTick <= 20) {
            this.attackTick = 20;
            if (this.attacker.getHeldItemMainhand() != null) {
                this.attacker.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
            }
            float damage = 3.0f;
            if (this.attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null) {
                damage += (float) this.attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
                        .getAttributeValue();
            }
            entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), damage);
        }
    }
}
