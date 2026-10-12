//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

import com.kentington.thaumichorizons.common.lib.EntityInfusionProperties;

public class EntityAIOwnerHurtTargetTH extends EntityAITarget {

    EntityLiving theEntityTameable;
    EntityLivingBase theTarget;
    private int field_142050_e;

    public EntityAIOwnerHurtTargetTH(final EntityLiving p_i1668_1_) {
        super((EntityCreature) p_i1668_1_, false);
        this.theEntityTameable = p_i1668_1_;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.theEntityTameable);
        final EntityLivingBase entitylivingbase = this.theEntityTameable.world
                .getPlayerEntityByName(prop.getOwner());
        if (entitylivingbase == null) {
            return false;
        }
        this.theTarget = entitylivingbase.getLastAttackedEntity();
        final int i = entitylivingbase.getLastAttackedEntityTime();
        return i != this.field_142050_e && this.isSuitableTarget(this.theTarget, false);
    }

    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theTarget);
        final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.theEntityTameable);
        final EntityLivingBase entitylivingbase = this.theEntityTameable.world
                .getPlayerEntityByName(prop.getOwner());
        if (entitylivingbase != null) {
            this.field_142050_e = entitylivingbase.getLastAttackedEntityTime();
        }
        super.startExecuting();
    }
}
