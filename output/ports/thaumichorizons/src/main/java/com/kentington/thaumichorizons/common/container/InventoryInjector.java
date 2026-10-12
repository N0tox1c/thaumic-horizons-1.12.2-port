package com.kentington.thaumichorizons.common.container;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

/** Seven injection-syringe slots using ItemStack.EMPTY and 1.12 inventory methods. */
public class InventoryInjector extends InventoryBasic {
    private final Container owner;

    public InventoryInjector(Container owner) {
        super("container.injector", false, 7);
        this.owner = owner;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index >= 0 && index < getSizeInventory() && !stack.isEmpty()
                && stack.getItem() == ThaumicHorizons.itemSyringeInjection;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (owner != null) owner.onCraftMatrixChanged(this);
    }
}
