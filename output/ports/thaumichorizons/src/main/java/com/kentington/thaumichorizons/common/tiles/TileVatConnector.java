//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileVatConnector extends TileVatSlave implements IEssentiaTransport, ISidedInventory {

    // The connector is a two-slot inventory proxy, not a second inventory.
    private TileVat owner() { return getBoss(-1); }

    @Override
    public int getSizeInventory() { TileVat boss = owner(); return boss == null ? 0 : boss.getSizeInventory(); }

    @Override
    public boolean isEmpty() { TileVat boss = owner(); return boss == null || boss.isEmpty(); }

    @Override
    public ItemStack getStackInSlot(int slot) {
        TileVat boss = owner(); return boss == null ? ItemStack.EMPTY : boss.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        TileVat boss = owner(); return boss == null ? ItemStack.EMPTY : boss.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        TileVat boss = owner(); return boss == null ? ItemStack.EMPTY : boss.removeStackFromSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        TileVat boss = owner(); if (boss != null) boss.setInventorySlotContents(slot, stack);
    }

    @Override
    public String getName() { TileVat boss = owner(); return boss == null ? "container.vat" : boss.getName(); }

    @Override
    public boolean hasCustomName() { return false; }

    @Override
    public ITextComponent getDisplayName() { return new TextComponentTranslation(getName()); }

    @Override
    public int getInventoryStackLimit() {
        TileVat boss = owner(); return boss == null ? 64 : boss.getInventoryStackLimit();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) { return false; }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        TileVat boss = owner(); return boss != null && boss.isItemValidForSlot(slot, item);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing face) {
        TileVat boss = owner(); return boss == null ? new int[0] : boss.getSlotsForFace(face);
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, EnumFacing face) {
        TileVat boss = owner(); return boss != null && boss.canInsertItem(slot, item, face);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing face) {
        TileVat boss = owner(); return boss != null && boss.canExtractItem(slot, item, face);
    }

    @Override
    public int getField(int id) { TileVat boss = owner(); return boss == null ? 0 : boss.getField(id); }

    @Override
    public void setField(int id, int value) { TileVat boss = owner(); if (boss != null) boss.setField(id, value); }

    @Override
    public int getFieldCount() { TileVat boss = owner(); return boss == null ? 0 : boss.getFieldCount(); }

    @Override
    public void clear() { TileVat boss = owner(); if (boss != null) boss.clear(); }

    @Override
    public boolean isConnectable(final EnumFacing face) {
        return true;
    }

    @Override
    public boolean canInputFrom(final EnumFacing face) {
        return true;
    }

    @Override
    public boolean canOutputTo(final EnumFacing face) {
        return false;
    }

    @Override
    public void setSuction(final Aspect aspect, final int amount) {}

    @Override
    public Aspect getSuctionType(final EnumFacing face) {
        final TileVat boss = this.getBoss(-1);
        if (boss != null) {
            return boss.getSuctionType(face);
        }
        return null;
    }

    @Override
    public int getSuctionAmount(final EnumFacing face) {
        final TileVat boss = this.getBoss(-1);
        if (boss != null) {
            return boss.getSuctionAmount(face);
        }
        return 0;
    }

    @Override
    public int takeEssentia(final Aspect aspect, final int amount, final EnumFacing face) {
        return 0;
    }

    @Override
    public int addEssentia(final Aspect aspect, final int amount, final EnumFacing face) {
        final TileVat boss = this.getBoss(-1);
        if (boss != null) {
            return boss.addEssentia(aspect, amount, face);
        }
        return 0;
    }

    @Override
    public Aspect getEssentiaType(final EnumFacing face) {
        return null;
    }

    @Override
    public int getEssentiaAmount(final EnumFacing face) {
        return 0;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Override
    public boolean renderExtendedTube() {
        return false;
    }
}
