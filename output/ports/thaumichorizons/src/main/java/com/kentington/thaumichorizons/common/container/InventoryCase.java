package com.kentington.thaumichorizons.common.container;

import com.kentington.thaumichorizons.common.items.lenses.ILens;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

/** Eighteen lens-only single-item slots, backed by the vanilla 1.12 inventory contract. */
public class InventoryCase extends InventoryBasic {
    private final Container owner;

    public InventoryCase(Container owner) {
        super("container.lenscase", false, 18);
        this.owner = owner;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index >= 0 && index < getSizeInventory() && !stack.isEmpty()
                && stack.getItem() instanceof ILens;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (owner != null) owner.onCraftMatrixChanged(this);
    }
}
