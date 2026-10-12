//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities.ai;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIHurtByTargetTH extends EntityAITargetTH {

    boolean entityCallsForHelp;
    private int field_142052_b;

    public EntityAIHurtByTargetTH(final EntityLiving p_i1660_1_, final boolean p_i1660_2_) {
        super(p_i1660_1_, false);
        this.entityCallsForHelp = p_i1660_2_;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        final int i = this.taskOwner.getRevengeTimer();
        return i != this.field_142052_b && this.isSuitableTarget(this.taskOwner.getRevengeTarget(), false);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
        this.field_142052_b = this.taskOwner.getRevengeTimer();
        if (this.entityCallsForHelp) {
            final double d0 = this.getTargetDistance();
            final List<? extends EntityLiving> list = this.taskOwner.world.getEntitiesWithinAABB(
                    this.taskOwner.getClass(),
                    new AxisAlignedBB(
                            this.taskOwner.posX,
                            this.taskOwner.posY,
                            this.taskOwner.posZ,
                            this.taskOwner.posX + 1.0,
                            this.taskOwner.posY + 1.0,
                            this.taskOwner.posZ + 1.0).expand(d0, 10.0, d0));
            for (final EntityLiving EntityLiving : list) {
                if (this.taskOwner != EntityLiving && EntityLiving.getAttackTarget() == null
                        && !EntityLiving.isOnSameTeam(this.taskOwner.getRevengeTarget())) {
                    EntityLiving.setAttackTarget(this.taskOwner.getRevengeTarget());
                }
            }
        }
        super.startExecuting();
    }
}
