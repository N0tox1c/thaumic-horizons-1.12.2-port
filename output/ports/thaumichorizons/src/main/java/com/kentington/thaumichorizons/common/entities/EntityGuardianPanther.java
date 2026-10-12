//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class EntityGuardianPanther extends EntityOcelot implements IEntityInfusedStats {

    public EntityGuardianPanther(final World p_i1688_1_) {
        super(p_i1688_1_);
        this.setTamed(true);
        this.setSize(1.2f, 1.6f);
        final ArrayList<EntityAITasks.EntityAITaskEntry> toRemove = new ArrayList<>(this.tasks.taskEntries);
        for (final EntityAITasks.EntityAITaskEntry task : toRemove) {
            this.tasks.removeTask(task.action);
        }
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.5f));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0, 10.0f, 5.0f));
        this.tasks.addTask(6, new EntityAIMate(this, 0.8));
        this.tasks.addTask(7, new EntityAIWander(this, 0.8));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0f));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5000000119209289);
    }

    public boolean processInteract(final EntityPlayer p_70085_1_, final net.minecraft.util.EnumHand hand) {
        final ItemStack itemstack = p_70085_1_.getHeldItem(hand);
        if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemFood) {
            final ItemFood itemfood = ((ItemFood) (itemstack.getItem()));

            if (this.getHealth() < this.getMaxHealth()) {
                if (this.world.isRemote) return true;
                int healing = itemfood.getHealAmount(itemstack);
                itemstack.shrink(1);
                this.heal((float) healing);
                if (itemstack.getCount() <= 0) {
                    p_70085_1_.setHeldItem(hand, ItemStack.EMPTY);
                }
                return true;
            }
        }
        return super.processInteract(p_70085_1_, hand);
    }

    public boolean attackEntityAsMob(final Entity p_70652_1_) {
        return p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 6.0f);
    }

    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag()
                : (this.isTamed() ? I18n.translateToLocal("entity.ThaumicHorizons.GuardianPanther.name")
                        : super.getName());
    }

    public void updateAITasks() {
        super.updateAITasks();
    }

    protected void entityInit() {
        super.entityInit();
    }

    public boolean isTamed() {
        return true;
    }

    public void resetStats() {
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5000000119209289);
    }
}
