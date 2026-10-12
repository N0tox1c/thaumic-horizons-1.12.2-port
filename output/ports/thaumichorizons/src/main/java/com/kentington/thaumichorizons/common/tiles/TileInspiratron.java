//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import com.kentington.thaumichorizons.common.lib.THSoundBridge;
import net.minecraft.util.math.BlockPos;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.config.ConfigItems;

public class TileInspiratron extends TileThaumcraft implements ISoulReceiver, ISidedInventory, net.minecraft.util.ITickable{

    public ItemStack paper;
    public ItemStack knowledge;
    public int progress;
    private final int PROGRESS_MAX = 100;
    public float rota;
    public float rotb;
    public float field_40063_b;
    public float field_40061_d;
    public float field_40059_f;
    public float field_40066_q;

    public void update() {
        if (this.world.isRemote) {
            Entity entity = null;
            this.rotb = this.rota;
            if (entity == null) {
                entity = this.world
                        .getClosestPlayer(this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f, 6.0, false);
            }
            if (entity != null) {
                final double d = entity.posX - (this.pos.getX() + 0.5f);
                final double d2 = entity.posZ - (this.pos.getZ() + 0.5f);
                this.field_40066_q = (float) Math.atan2(d2, d);
                this.field_40059_f += 0.1f;
                if (this.field_40059_f < 0.5f || entity.world.rand.nextInt(40) == 0) {
                    final float f3 = this.field_40061_d;
                    do {
                        this.field_40061_d += entity.world.rand.nextInt(4) - entity.world.rand.nextInt(4);
                    } while (f3 == this.field_40061_d);
                }
            } else {
                this.field_40066_q += 0.01f;
            }
            while (this.rota >= (float) Math.PI) {
                this.rota -= ((float) Math.PI * 2F);
            }
            while (this.rota < -(float) Math.PI) {
                this.rota += ((float) Math.PI * 2F);
            }
            while (this.field_40066_q >= (float) Math.PI) {
                this.field_40066_q -= ((float) Math.PI * 2F);
            }
            while (this.field_40066_q < -(float) Math.PI) {
                this.field_40066_q += ((float) Math.PI * 2F);
            }
            float f4;
            for (f4 = this.field_40066_q - this.rota; f4 < -(float) Math.PI; f4 += ((float) Math.PI * 2F)) {}
            this.rota += f4 * 0.04f;
        }
    }

    @Override
    public void addSoulBits(final int bits) {
        for (int i = 0; i < bits; ++i) {
            if (Math.random() >= 0.97) {
                THSoundBridge.play(this.world, 
                        this.pos.getX() + 0.5,
                        this.pos.getY() + 0.5,
                        this.pos.getZ() + 0.5,
                        "thaumcraft:write",
                        0.2f,
                        this.world.rand.nextFloat());
                ++this.progress;
            }
        }
        if (this.progress >= 100) {
            this.progress -= 100;
            if ((this.knowledge == null || this.knowledge.isEmpty())) {
                this.knowledge = new ItemStack(ConfigItems.itemResource, 1, 9);
            } else {
                final ItemStack knowledge = this.knowledge;
                knowledge.grow(1);
            }
            final ItemStack paper = this.paper;
            paper.shrink(1);
            if (this.paper.getCount() <= 0) {
                this.paper = null;
            }
        }
    }

    @Override
    public boolean canAcceptSouls() {
        return (this.paper != null && !this.paper.isEmpty()) && this.paper.getCount() > 0
                && ((this.knowledge == null || this.knowledge.isEmpty()) || this.knowledge.getCount() < 64);
    }

    public int getSizeInventory() {
        return 2;
    }

    public ItemStack getStackInSlot(final int p_70301_1_) {
        if (p_70301_1_ == 0) {
            return this.paper == null ? ItemStack.EMPTY : this.paper;
        }
        if (p_70301_1_ == 1) {
            return this.knowledge == null ? ItemStack.EMPTY : this.knowledge;
        }
        return null;
    }

    public ItemStack decrStackSize(final int p_70298_1_, final int p_70298_2_) {
        if (p_70298_1_ == 0 && (this.paper == null || this.paper.isEmpty())) return ItemStack.EMPTY;
        if (p_70298_1_ == 1 && (this.knowledge == null || this.knowledge.isEmpty())) return ItemStack.EMPTY;
        if (p_70298_1_ == 0) {
            final int oldsize = this.paper.getCount();
            final ItemStack paper = this.paper;
            paper.shrink( p_70298_2_);
            if (this.paper.getCount() <= 0) {
                this.paper = null;
            }
            return new ItemStack(Items.PAPER, Math.min(p_70298_2_, oldsize));
        }
        if (p_70298_1_ == 1) {
            final int oldsize = this.knowledge.getCount();
            final ItemStack knowledge = this.knowledge;
            knowledge.shrink( p_70298_2_);
            if (this.knowledge.getCount() <= 0) {
                this.knowledge = null;
            }
            return new ItemStack(ConfigItems.itemResource, Math.min(p_70298_2_, oldsize), 9);
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(final int p_70304_1_) {
        return null;
    }

    public void setInventorySlotContents(final int p_70299_1_, final ItemStack p_70299_2_) {
        if (p_70299_1_ == 0) {
            this.paper = p_70299_2_ == null || p_70299_2_.isEmpty() ? null : p_70299_2_;
        } else if (p_70299_1_ == 1) {
            this.knowledge = p_70299_2_ == null || p_70299_2_.isEmpty() ? null : p_70299_2_;
        }
    }

    public String getInventoryName() {
        return "container.inspiratron";
    }

    public boolean hasCustomInventoryName() {
        return false;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
        return this.world.getTileEntity(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())) == this
                && p_70300_1_.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }

    public void openInventory() {}

    public void closeInventory() {}

    public boolean isItemValidForSlot(final int p_94041_1_, final ItemStack p_94041_2_) {
        if (p_94041_1_ == 0) {
            return p_94041_2_.isItemEqual(new ItemStack(Items.PAPER));
        }
        return p_94041_1_ == 1 && p_94041_2_.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 9));
    }

    public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
        return new int[] { 0, 1 };
    }

    public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
        return p_102007_1_ != 1 && this.isItemValidForSlot(p_102007_1_, p_102007_2_);
    }

    public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
        return p_102008_1_ == 1 && (this.knowledge != null && !this.knowledge.isEmpty());
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setInteger("progress", this.progress);
        final NBTTagList nbttaglist = new NBTTagList();
        final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
        if ((this.paper != null && !this.paper.isEmpty())) {
            this.paper.writeToNBT(nbttagcompound2);
        }
        nbttaglist.appendTag(nbttagcompound2);
        final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
        if ((this.knowledge != null && !this.knowledge.isEmpty())) {
            this.knowledge.writeToNBT(nbttagcompound3);
        }
        nbttaglist.appendTag(nbttagcompound3);
        nbttagcompound.setTag("Items", nbttaglist);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.progress = nbttagcompound.getInteger("progress");
        final NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(0);
        this.paper = new ItemStack(nbttagcompound2);
        nbttagcompound2 = nbttaglist.getCompoundTagAt(1);
        this.knowledge = new ItemStack(nbttagcompound2);
    }

    @SideOnly(Side.CLIENT)
    public int getTimeRemainingScaled(final int p_145955_1_) {
        final int n = this.progress * p_145955_1_;
        this.getClass();
        return n / 100;
    }

    // Forge 1.12.2 IInventory/ISidedInventory bridge. Original 1.7.10 NBT is retained.
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack != null && !stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack == null || stack.isEmpty()) return ItemStack.EMPTY;
        setInventorySlotContents(slot, ItemStack.EMPTY);
        markDirty();
        return stack;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return isUseableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player) { openInventory(); }

    @Override
    public void closeInventory(EntityPlayer player) { closeInventory(); }

    @Override
    public String getName() { return getInventoryName(); }

    @Override
    public boolean hasCustomName() { return hasCustomInventoryName(); }

    @Override
    public int getField(int id) { return 0; }

    @Override
    public void setField(int id, int value) { }

    @Override
    public int getFieldCount() { return 0; }

    @Override
    public void clear() {
        for (int i = 0; i < getSizeInventory(); i++) setInventorySlotContents(i, ItemStack.EMPTY);
        markDirty();
    }

    @Override
    public int[] getSlotsForFace(net.minecraft.util.EnumFacing face) {
        return getAccessibleSlotsFromSide(face.getIndex());
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, net.minecraft.util.EnumFacing face) {
        return canInsertItem(slot, stack, face.getIndex());
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, net.minecraft.util.EnumFacing face) {
        return canExtractItem(slot, stack, face.getIndex());
    }
}
