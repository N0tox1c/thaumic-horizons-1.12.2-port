//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

import com.kentington.thaumichorizons.common.lib.EntityInfusionProperties;

public class EntityAISitTH extends EntityAIBase {

    private final EntityLiving theEntity;
    private boolean isSitting;

    public EntityAISitTH(final EntityLiving p_i1654_1_) {
        this.theEntity = p_i1654_1_;
        this.setMutexBits(5);
    }

    public boolean shouldExecute() {
        if (this.theEntity.isInWater()) {
            return false;
        }
        if (!this.theEntity.onGround) {
            return false;
        }
        final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.theEntity);
        final EntityLivingBase entitylivingbase = this.theEntity.world.getPlayerEntityByName(prop.getOwner());
        return entitylivingbase == null || ((this.theEntity.getDistanceSq(entitylivingbase) >= 144.0
                || entitylivingbase.getRevengeTarget() == null) && this.isSitting);
    }

    public void startExecuting() {
        final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.theEntity);
        this.theEntity.getNavigator().clearPath();
        prop.setSitting(true);
    }

    public void resetTask() {
        final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.theEntity);
        prop.setSitting(false);
    }

    public void setSitting(final boolean p_75270_1_) {
        this.isSitting = p_75270_1_;
    }
}
