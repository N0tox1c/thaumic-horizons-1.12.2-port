package com.kentington.thaumichorizons.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.common.tiles.TileMagicWorkbench;

/**
 * Virtual 11-slot arcane workbench, not placed as a world tile.
 * Slots 0..8: crafting ingredients, 9: ephemeral output, 10: wand.
 * TC4's TileMagicWorkbench implements 1.12's inventory contract and
 * recipe-change notification. Do not reimplement its inventory with null stacks.
 */
public class InventoryFingers extends TileMagicWorkbench {
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return player != null;
    }

    /** The GUI is virtual, so we never persist a tile or access a world position. */
    @Override
    public void markDirty() {
        // The inherited inventoryChanged() still refreshes Container listeners.
    }

    public ItemStack removeOnClose(int slot) {
        if (slot < 0 || slot >= 11 || slot == 9) return ItemStack.EMPTY;
        return this.removeStackFromSlot(slot);
    }
}
