//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.world.World;

public class EntityHorseUndeadS extends EntitySkeletonHorse {

    public EntityHorseUndeadS(final World p_i1685_1_) {
        super(p_i1685_1_);
    }

    public int getHorseType() {
        return 4;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    public boolean func_110256_cu() {
        return false;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
                .setBaseValue(this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + 0.1);
    }
}
