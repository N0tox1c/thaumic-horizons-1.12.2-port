//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileVat;
import thaumcraft.common.lib.research.ResearchManager;

/** Original two vat inputs and 36 player inventory slots. */
public class ContainerVat extends Container {
    private final TileVat tile;
    private final boolean canIncarnate;
    private final int[] lastFields = new int[4];

    public ContainerVat(EntityPlayer player, TileVat tile) {
        this.tile = tile;
        this.canIncarnate = ResearchManager.isResearchComplete(player, "incarnationVat");
        if (this.canIncarnate) {
            addSlotToContainer(new SlotSample(tile, 0, 63, 32));
            addSlotToContainer(new SlotRestricted(tile, 1, 96, 32,
                    new ItemStack(ThaumicHorizons.itemNutrients)));
        }
        int firstPlayerSlotY = 127;
        for (int row=0;row<3;row++) for (int col=0;col<9;col++) {
            addSlotToContainer(new Slot(player.inventory,col+row*9+9,8+col*18,firstPlayerSlotY+row*18));
        }
        for (int col=0;col<9;col++) {
            addSlotToContainer(new Slot(player.inventory,col,8+col*18,185));
        }
        for (int i=0;i<lastFields.length;i++) lastFields[i]=tile.getField(i);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) { return tile.isUsableByPlayer(player); }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        if (slotIndex < 0 || slotIndex >= inventorySlots.size()) return ItemStack.EMPTY;
        Slot slot=inventorySlots.get(slotIndex);
        if (slot==null || !slot.getHasStack()) return ItemStack.EMPTY;
        ItemStack held=slot.getStack();
        ItemStack original=held.copy();
        final int playerStart=canIncarnate ? 2 : 0;
        if (slotIndex < playerStart) {
            if (!mergeItemStack(held,playerStart,inventorySlots.size(),true)) return ItemStack.EMPTY;
        } else {
            // Without research no vat slots exist in the container, even for shift-click.
            if (!canIncarnate) return ItemStack.EMPTY;
            boolean inserted=false;
            if (held.getItem()==ThaumicHorizons.itemNutrients) {
                inserted=mergeItemStack(held,1,2,false);
            }
            if (!inserted && inventorySlots.get(0).isItemValid(held)) {
                inserted=mergeItemStack(held,0,1,false);
            }
            if (!inserted) return ItemStack.EMPTY;
        }
        if (held.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();
        if (held.getCount()==original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player,held);
        return original;
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        for (int i=0;i<tile.getFieldCount();i++) listener.sendWindowProperty(this,i,tile.getField(i));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i=0;i<tile.getFieldCount();i++) {
            int current=tile.getField(i);
            if (current!=lastFields[i]) {
                for (IContainerListener listener:listeners) listener.sendWindowProperty(this,i,current);
                lastFields[i]=current;
            }
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id>=0 && id<tile.getFieldCount()) tile.setField(id,data);
    }
}
