package com.kentington.thaumichorizons.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.common.container.ContainerArcaneWorkbench;

/**
 * Thaumic Horizons Arcane Crafting Fingers: original 11-slot layout on top
 * of TC4 Port's tested recipe resolver and atomic crafting transaction.
 * Reusing the real TC4 container preserves vis checks, crafting remainders,
 * recipe unlocks, shift-click and drag protection instead of duplicating them.
 */
public class ContainerFingers extends ContainerArcaneWorkbench {
    public final InventoryFingers tileEntity;

    public ContainerFingers(InventoryPlayer inventory) {
        this(inventory, new InventoryFingers());
    }

    private ContainerFingers(InventoryPlayer inventory, InventoryFingers fingers) {
        super(inventory, fingers);
        this.tileEntity = fingers;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        // Fingers are a virtual/portable crafting interface, not a placed TileEntity.
        return player != null && this.tileEntity.isUsableByPlayer(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (player == null || player.world.isRemote) return;
        // Keep slot 9 (the craft preview) out of dropped items.
        for (int i = 0; i < 11; i++) {
            ItemStack stack = this.tileEntity.removeOnClose(i);
            if (!stack.isEmpty()) player.dropItem(stack, false);
        }
    }
}
