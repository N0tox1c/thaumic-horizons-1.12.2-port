//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySheeder extends EntitySpider implements IShearable {

    private static final DataParameter<Byte> SHEARED = EntityDataManager.createKey(EntitySheeder.class, DataSerializers.BYTE);

    private int sheepTimer;
    private final EntityAIEatGrass sheeder;

    public EntitySheeder(final World p_i1743_1_) {
        super(p_i1743_1_);
        this.sheeder = new EntityAIEatGrass(this);
        this.setPathPriority(PathNodeType.WATER, 8.0F);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.5));
        this.tasks.addTask(3, new EntityAITempt(this, 0.44, Items.WHEAT, false));
        this.tasks.addTask(5, this.sheeder);
        this.tasks.addTask(6, new EntityAIWander(this, 0.4));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.isDead && !this.world.isRemote
                && this.world.getDifficulty() == EnumDifficulty.PEACEFUL
                && this.getHealth() > 0.0f) {
            this.isDead = false;
        }
    }

    protected Entity findPlayerToAttack() {
        return null;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    public boolean isShearable(final ItemStack item, final IBlockAccess world, final BlockPos pos) {
        return !this.getSheared() && !this.isChild();
    }

    public ArrayList<ItemStack> onSheared(final ItemStack item, final IBlockAccess world, final BlockPos pos, final int fortune) {
        final ArrayList<ItemStack> ret = new ArrayList<>();
        this.setSheared(true);
        for (int i = 2 + this.rand.nextInt(4), j = 0; j < i; ++j) {
            ret.add(new ItemStack(Items.STRING, 1, 0));
        }
        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
        return ret;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SHEARED, (byte) 0);
    }

    public void setSheared(final boolean p_70893_1_) {
        final byte b0 = this.dataManager.get(SHEARED);
        if (p_70893_1_) {
            this.dataManager.set(SHEARED, (byte) (b0 | 0x10));
        } else {
            this.dataManager.set(SHEARED, (byte) (b0 & 0xFFFFFFEF));
        }
    }

    public boolean getSheared() {
        return (this.dataManager.get(SHEARED) & 0x10) != 0x0;
    }

    public void eatGrassBonus() {
        this.setSheared(false);
    }

    public void writeEntityToNBT(final NBTTagCompound p_70014_1_) {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setBoolean("Sheared", this.getSheared());
    }

    public void readEntityFromNBT(final NBTTagCompound p_70037_1_) {
        super.readEntityFromNBT(p_70037_1_);
        this.setSheared(p_70037_1_.getBoolean("Sheared"));
    }

    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        if (!this.getSheared()) {
            this.entityDropItem(new ItemStack(Items.STRING), 0.0f);
        }
    }

    protected Item getDropItem() {
        return Items.STRING;
    }

    protected boolean isAIEnabled() {
        return true;
    }

    protected void updateAITasks() {
        this.sheepTimer = this.sheeder.getEatingGrassTimer();
        super.updateAITasks();
    }

    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }
        super.onLivingUpdate();
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(final byte p_70103_1_) {
        if (p_70103_1_ == 10) {
            this.sheepTimer = 40;
        } else {
            super.handleStatusUpdate(p_70103_1_);
        }
    }
}
