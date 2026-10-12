//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import com.kentington.thaumichorizons.common.lib.THSoundBridge;
import net.minecraft.util.math.BlockPos;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.visnet.TileVisNode;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileJarBrain;

public class TileSoulExtractor extends TileVisNode implements ISidedInventory, net.minecraft.util.ITickable{

    public ItemStack soulsand;
    public int ticksLeft;
    public boolean extracting;
    public static final int MAX_TICKS = 1200;
    public int sieveMotion;

    public TileSoulExtractor() {
        this.soulsand = null;
        this.ticksLeft = 0;
        this.extracting = false;
        this.sieveMotion = 0;
    }

    public int getSizeInventory() {
        return 1;
    }

    public ItemStack getStackInSlot(final int p_70301_1_) {
        return this.soulsand == null ? ItemStack.EMPTY : this.soulsand;
    }

    public ItemStack decrStackSize(final int p_70298_1_, final int p_70298_2_) {
        if (this.soulsand == null || this.soulsand.isEmpty()) return ItemStack.EMPTY;
        final int oldsize = this.soulsand.getCount();
        final ItemStack soulsand = this.soulsand;
        soulsand.shrink( p_70298_2_);
        if (this.soulsand.getCount() <= 0) {
            this.soulsand = null;
        }
        return new ItemStack(Blocks.SOUL_SAND, Math.min(p_70298_2_, oldsize));
    }

    public ItemStack getStackInSlotOnClosing(final int p_70304_1_) {
        return null;
    }

    public void setInventorySlotContents(final int p_70299_1_, final ItemStack p_70299_2_) {
        this.soulsand = p_70299_2_ == null || p_70299_2_.isEmpty() ? null : p_70299_2_;
    }

    public String getInventoryName() {
        return "container.soulsieve";
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
        return p_94041_2_.isItemEqual(new ItemStack(Blocks.SOUL_SAND));
    }

    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setInteger("ticks", this.ticksLeft);
        nbttagcompound.setBoolean("extracting", this.extracting);
        final NBTTagList nbttaglist = new NBTTagList();
        final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
        if ((this.soulsand != null && !this.soulsand.isEmpty())) {
            this.soulsand.writeToNBT(nbttagcompound2);
        }
        nbttaglist.appendTag(nbttagcompound2);
        nbttagcompound.setTag("Items", nbttaglist);
    }

    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.ticksLeft = nbttagcompound.getInteger("ticks");
        this.extracting = nbttagcompound.getBoolean("extracting");
        final NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        final NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(0);
        this.soulsand = new ItemStack(nbttagcompound2);
    }

    public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
        return new int[] { 0 };
    }

    public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
        return this.isItemValidForSlot(0, p_102007_2_);
    }

    public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
        return false;
    }

    @Override
    public void update() {
        super.update();
        this.extracting = false;
        if (this.ticksLeft <= 0) {
            if ((this.soulsand == null || this.soulsand.isEmpty())) {
                return;
            }
            final ItemStack soulsand = this.soulsand;
            soulsand.shrink(1);
            if (this.soulsand.getCount() <= 0) {
                this.soulsand = null;
            }
            this.ticksLeft = 1200;
        }
        final TileEntity above = this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ()));
        if (above == null) {
            return;
        }
        if ((above instanceof TileJarBrain && ((TileJarBrain) above).xp < ((TileJarBrain) above).xpMax)
                || (above instanceof ISoulReceiver && ((ISoulReceiver) above).canAcceptSouls())) {
            this.extracting = true;
            if (!this.world.isRemote && this.ticksLeft > 0) {
                final int visBoost = VisNetHandler
                        .drainVis(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), Aspect.AIR, 10);
                this.ticksLeft -= 1 + visBoost;
                if (above instanceof TileJarBrain) {
                    for (int i = 0; i < 1 + visBoost; ++i) {
                        if (Math.random() > 0.99) {
                            final TileJarBrain tileJarBrain = (TileJarBrain) above;
                            ++tileJarBrain.xp;
                            if (((TileJarBrain) above).xp >= ((TileJarBrain) above).xpMax) {
                                ((TileJarBrain) above).xp = ((TileJarBrain) above).xpMax;
                            }
                        }
                    }
                } else {
                    ((ISoulReceiver) above).addSoulBits(1 + visBoost);
                }
                if (this.ticksLeft <= 0) {
                    final TileEntity below = this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ()));
                    final ItemStack sand = new ItemStack(Blocks.SAND);
                    if (below instanceof ISidedInventory) {
                        final int[] accessibleSlotsFromSide;
                        final int[] slots = accessibleSlotsFromSide = ((ISidedInventory) below)
                                .getSlotsForFace(net.minecraft.util.EnumFacing.UP);
                        for (final int j : accessibleSlotsFromSide) {
                            if (((ISidedInventory) below).canInsertItem(j, sand, net.minecraft.util.EnumFacing.UP)) {
                                InventoryUtils.placeItemStackIntoInventory(sand, (IInventory) below, 1, true);
                                break;
                            }
                        }
                    } else if (below instanceof IInventory) {
                        for (int slots2 = ((IInventory) below).getSizeInventory(), k = 0; k < slots2; ++k) {
                            if (((IInventory) below).getStackInSlot(k).isEmpty()
                                    || ((IInventory) below).getStackInSlot(k).getItem() == sand.getItem()) {
                                InventoryUtils.placeItemStackIntoInventory(sand, (IInventory) below, 1, true);
                                break;
                            }
                        }
                    } else {
                        final EntityItem fallenSand = new EntityItem(
                                this.world,
                                this.pos.getX() + 0.5,
                                this.pos.getY(),
                                this.pos.getZ() + 0.5,
                                sand);
                        this.world.spawnEntity(fallenSand);
                    }
                }
                this.markDirty();
                this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
            } else if (this.ticksLeft > 0) {
                ++this.sieveMotion;
                if (this.sieveMotion >= 360) {
                    this.sieveMotion -= 360;
                    THSoundBridge.playLegacy(this.world, 
                            this.pos.getX() + 0.5,
                            this.pos.getY() + 0.5,
                            this.pos.getZ() + 0.5,
                            "dig.sand",
                            1.0f,
                            0.0f,
                            false);
                    final ThaumicHorizons instance = ThaumicHorizons.instance;
                    ThaumicHorizons.proxy.soulParticles(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.world);
                }
            }
        }
    }

    @Override
    public int getRange() {
        return 8;
    }

    @Override
    public boolean isSource() {
        return false;
    }

    public boolean isExtracting() {
        return this.extracting;
    }

    @SideOnly(Side.CLIENT)
    public int getTimeRemainingScaled(final int p_145955_1_) {
        return this.ticksLeft * p_145955_1_ / 1200;
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
