package com.kentington.thaumichorizons.common.container;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.items.ItemInjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/** The seven-chamber injector's original rotating magazine editor. */
public class ContainerInjector extends Container {
    private static final int AMMO_SLOTS = 7;
    private final EntityPlayer player;
    private final ItemStack injector;
    private final int selectedHotbarIndex;
    private final int protectedSlot;
    public final InventoryInjector ammoInv;

    public ContainerInjector(EntityPlayer player) {
        this.player = player;
        this.selectedHotbarIndex = player.inventory.currentItem;
        this.protectedSlot = AMMO_SLOTS + 27 + selectedHotbarIndex;
        this.injector = player.inventory.getCurrentItem();
        this.ammoInv = new InventoryInjector(this);
        if (!injector.isEmpty() && injector.getItem() instanceof ItemInjector) {
            for (int i = 0; i < AMMO_SLOTS; i++) {
                ammoInv.setInventorySlotContents(i, ((ItemInjector) injector.getItem()).getAmmo(injector, i));
            }
        }
        final int[] xs = {73, 99, 107, 92, 64, 45, 49};
        final int[] ys = {10, 20, 47, 70, 72, 51, 24};
        for (int i = 0; i < AMMO_SLOTS; i++) {
            addSlotToContainer(new SlotRestricted(ammoInv, i, xs[i], ys[i],
                    new ItemStack(ThaumicHorizons.itemSyringeInjection)));
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9,
                        8 + col * 18, 108 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            if (col == selectedHotbarIndex) {
                addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 166) {
                    @Override public boolean canTakeStack(EntityPlayer p) { return false; }
                    @Override public boolean isItemValid(ItemStack stack) { return false; }
                });
            } else {
                addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 166));
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p) {
        return p == player && !p.isDead && p.inventory.currentItem == selectedHotbarIndex
                && !injector.isEmpty() && injector.getItem() instanceof ItemInjector
                && p.inventory.getCurrentItem() == injector;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p, int index) {
        if (!canInteractWith(p) || index == protectedSlot || index < 0 || index >= inventorySlots.size())
            return ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
        ItemStack moved = slot.getStack();
        ItemStack original = moved.copy();
        if (index < AMMO_SLOTS) {
            if (!mergeItemStack(moved, AMMO_SLOTS, inventorySlots.size(), true)) return ItemStack.EMPTY;
        } else {
            if (moved.getItem() != ThaumicHorizons.itemSyringeInjection
                    || !mergeItemStack(moved, 0, AMMO_SLOTS, false)) return ItemStack.EMPTY;
        }
        if (moved.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();
        if (moved.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(p, moved);
        return original;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer p) {
        if (!canInteractWith(p) || slotId == protectedSlot
                || (clickType == ClickType.SWAP && dragType == selectedHotbarIndex))
            return ItemStack.EMPTY;
        // Server-side slot validation remains in SlotRestricted and InventoryInjector.
        return super.slotClick(slotId, dragType, clickType, p);
    }

    @Override
    public void onContainerClosed(EntityPlayer p) {
        super.onContainerClosed(p);
        if (player.world.isRemote || injector.isEmpty() || !(injector.getItem() instanceof ItemInjector)) return;
        NBTTagList ammo = new NBTTagList();
        for (int i = 0; i < AMMO_SLOTS; i++) {
            ItemStack shot = ammoInv.getStackInSlot(i);
            NBTTagCompound entry = new NBTTagCompound();
            if (!shot.isEmpty()) shot.writeToNBT(entry);
            // The old format stores exactly seven ordered compounds without Slot tags.
            ammo.appendTag(entry);
        }
        NBTTagCompound tag = injector.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();
        // Do not replace the whole tag: preserve rotationTarget, usetime and enchantments.
        tag.setTag("ammo", ammo);
        injector.setTagCompound(tag);
        player.inventory.markDirty();
    }
}
