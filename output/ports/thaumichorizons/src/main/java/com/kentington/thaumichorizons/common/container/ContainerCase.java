package com.kentington.thaumichorizons.common.container;

import com.kentington.thaumichorizons.common.items.lenses.ILens;
import com.kentington.thaumichorizons.common.items.lenses.ItemLensCase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.container.SlotLimitedByClass;

/** The original 18-slot lens case. The open case cannot be moved into itself. */
public class ContainerCase extends Container {
    private static final int CASE_SLOTS = 18;
    private final World world;
    private final EntityPlayer owner;
    private final ItemStack pouch;
    private final int selectedHotbarIndex;
    private final int protectedSlot;
    public final InventoryCase input;

    public ContainerCase(InventoryPlayer inventory, World world, int x, int y, int z) {
        this.world = world;
        this.owner = inventory.player;
        this.selectedHotbarIndex = inventory.currentItem;
        this.protectedSlot = CASE_SLOTS + 27 + selectedHotbarIndex;
        this.pouch = inventory.getCurrentItem();
        this.input = new InventoryCase(this);
        if (!pouch.isEmpty() && pouch.getItem() instanceof ItemLensCase) {
            ItemStack[] stored = ((ItemLensCase) pouch.getItem()).getInventory(pouch);
            for (int i = 0; i < CASE_SLOTS; i++) input.setInventorySlotContents(i, stored[i]);
        }
        for (int i = 0; i < CASE_SLOTS; i++) {
            addSlotToContainer(new SlotLimitedByClass(ILens.class, input, i,
                    37 + (i % 6) * 18, 51 + (i / 6) * 18, 1));
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(inventory, col + row * 9 + 9,
                        8 + col * 18, 151 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            if (col == selectedHotbarIndex) {
                addSlotToContainer(new Slot(inventory, col, 8 + col * 18, 209) {
                    @Override public boolean canTakeStack(EntityPlayer player) { return false; }
                    @Override public boolean isItemValid(ItemStack stack) { return false; }
                });
            } else {
                addSlotToContainer(new Slot(inventory, col, 8 + col * 18, 209));
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player == owner && !player.isDead && player.world == world
                && player.inventory.currentItem == selectedHotbarIndex
                && !pouch.isEmpty() && pouch.getItem() instanceof ItemLensCase
                && player.inventory.getCurrentItem() == pouch;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        if (!canInteractWith(player) || index == protectedSlot || index < 0 || index >= inventorySlots.size())
            return ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
        ItemStack moved = slot.getStack();
        ItemStack original = moved.copy();
        if (index < CASE_SLOTS) {
            if (!mergeItemStack(moved, CASE_SLOTS, inventorySlots.size(), true)) return ItemStack.EMPTY;
        } else {
            if (!(moved.getItem() instanceof ILens) || !mergeItemStack(moved, 0, CASE_SLOTS, false))
                return ItemStack.EMPTY;
        }
        if (moved.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();
        if (moved.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, moved);
        return original;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (!canInteractWith(player) || slotId == protectedSlot
                || (clickType == ClickType.SWAP && dragType == selectedHotbarIndex))
            return ItemStack.EMPTY;
        return super.slotClick(slotId, dragType, clickType, player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (world.isRemote || pouch.isEmpty() || !(pouch.getItem() instanceof ItemLensCase)) return;
        ItemStack[] stored = new ItemStack[CASE_SLOTS];
        for (int i = 0; i < CASE_SLOTS; i++) stored[i] = input.getStackInSlot(i);
        ((ItemLensCase) pouch.getItem()).setInventory(pouch, stored);
        owner.inventory.markDirty();
    }
}
