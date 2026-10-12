//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities.ai;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

import com.kentington.thaumichorizons.common.entities.EntityOrePig;

public class EntityAIEatStone extends EntityAIBase {

    private final EntityOrePig thePig;
    private Entity targetEntity;
    int count;

    public EntityAIEatStone(final EntityOrePig par1EntityCreature) {
        this.count = 0;
        this.thePig = par1EntityCreature;
    }

    public boolean shouldExecute() {
        return this.findItem();
    }

    private boolean findItem() {
        final float dmod = 16.0f;
        double mindis_e = 128.0f;

        final List<Entity> targets = (List<Entity>) this.thePig.world.getEntitiesWithinAABBExcludingEntity(
                this.thePig,
                new AxisAlignedBB(
                        this.thePig.posX - 16.0,
                        this.thePig.posY - 16.0,
                        this.thePig.posZ - 16.0,
                        this.thePig.posX + 16.0,
                        this.thePig.posY + 16.0,
                        this.thePig.posZ + 16.0));
        if (targets.size() == 0) {
            return false;
        }
        for (Entity e : targets) {

            if (e instanceof EntityItem
                    && ((EntityItem) e).getItem().getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE)
                    && ((EntityItem) e).writeToNBT(new net.minecraft.nbt.NBTTagCompound()).getShort("PickupDelay") < 5) {
                double distance2 = e.getDistanceSq(this.thePig.posX, this.thePig.posY, this.thePig.posZ);
                if (distance2 >= dmod * dmod || mindis_e < distance2) {
                    continue;
                }
                mindis_e = distance2;
                this.targetEntity = e;
            }
        }

        return this.targetEntity != null;
    }

    @Override public boolean shouldContinueExecuting() {
        return this.count-- > 0 && !this.thePig.getNavigator().noPath() && this.targetEntity.isEntityAlive();
    }

    public void resetTask() {
        this.count = 0;
        this.targetEntity = null;
        this.thePig.getNavigator().clearPath();
    }

    public void updateTask() {
        this.thePig.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0f, 30.0f);
        final double dist = this.thePig.getDistanceSq(this.targetEntity);
        if (dist <= 2.0) {
            this.pickUp();
        }
    }

    private void pickUp() {
        final int amount = 0;
        if (this.targetEntity instanceof EntityItem) {
            this.thePig.eatStone();
            final ItemStack entityItem = ((EntityItem) this.targetEntity).getItem();
            entityItem.shrink(1);
            if (((EntityItem) this.targetEntity).getItem().getCount() <= 0) {
                this.targetEntity.setDead();
            }
        }
        com.kentington.thaumichorizons.common.lib.THSoundBridge.playAtEntity(this.targetEntity.world, this.targetEntity, "random.burp", 0.2f, ((this.targetEntity.world.rand.nextFloat() - this.targetEntity.world.rand.nextFloat()) * 0.7f
                        + 1.0f) * 2.0f);
    }

    public void startExecuting() {
        this.count = 500;
        this.thePig.getNavigator().tryMoveToEntityLiving(this.targetEntity, this.thePig.getAIMoveSpeed() + 1.0f);
    }
}
