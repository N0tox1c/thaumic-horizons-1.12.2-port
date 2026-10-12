//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;

public class EntityGravekeeper extends EntityOcelot {

    public EntityGravekeeper(final World p_i1688_1_) {
        super(p_i1688_1_);
        this.setTamed(true);
        // Replace the vanilla chicken target by its semantic identity; taskEntries is a Set.
        for (EntityAITasks.EntityAITaskEntry task : new java.util.ArrayList<>(this.targetTasks.taskEntries)) {
            if (task.action instanceof EntityAINearestAttackableTarget) this.targetTasks.removeTask(task.action);
        }
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntitySkeleton.class, 0, false, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityZombie.class, 0, false, false, null));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityWither.class, 0, false, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityPigZombie.class, 0, false, false, null));
        this.targetTasks.addTask(6, new EntityAINearestAttackableTarget(this, EntityGiantZombie.class, 0, false, false, null));
        this.targetTasks.addTask(7, new EntityAINearestAttackableTarget(this, EntityBrainyZombie.class, 0, false, false, null));
        this.targetTasks.addTask(8, new EntityAINearestAttackableTarget(this, EntityEldritchGuardian.class, 0, false, false, null));
        this.targetTasks.addTask(9, new EntityAINearestAttackableTarget(this, EntityGiantBrainyZombie.class, 0, false, false, null));
        this.targetTasks.addTask(10, new EntityAINearestAttackableTarget(this, EntityInhabitedZombie.class, 0, false, false, null));
    }

    public boolean attackEntityAsMob(final Entity p_70652_1_) {
        return (p_70652_1_ instanceof EntityLivingBase && ((EntityLivingBase) p_70652_1_).isEntityUndead())
                || p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 2.0f);
    }

    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag()
                : (this.isTamed() ? I18n.translateToLocal("entity.ThaumicHorizons.Gravekeeper.name")
                        : super.getName());
    }

    public void updateAITasks() {
        super.updateAITasks();
        final List<EntityLivingBase> critters = (List<EntityLivingBase>) this.world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                new AxisAlignedBB(
                        this.posX - 5.0,
                        this.posY - 5.0,
                        this.posZ - 5.0,
                        this.posX + 5.0,
                        this.posY + 5.0,
                        this.posZ + 5.0));
        for (final EntityLivingBase ent : critters) {
            if (ent.isEntityUndead()) {
                ent.setFire(1);
                Thaumcraft.proxy.beamBore(
                        this.world,
                        this.posX,
                        this.posY + this.height / 2.0f,
                        this.posZ,
                        ent.posX,
                        ent.posY + ent.height / 2.0f,
                        ent.posZ,
                        0,
                        16773444,
                        false,
                        2.5f,
                        null, 1);
            }
        }
    }

    protected void entityInit() {
        super.entityInit();
    }

    public boolean isTamed() {
        return true;
    }
}
