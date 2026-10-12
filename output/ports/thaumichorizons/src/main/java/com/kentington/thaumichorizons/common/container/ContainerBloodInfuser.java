package com.kentington.thaumichorizons.common.container;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileBloodInfuser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.capabilities.IPlayerKnowledge;
import thaumcraft.common.lib.capabilities.PlayerKnowledgeProvider;

/** Original 1 input + 9 outputs + 36 player slots, adapted to Forge 1.12.2. */
public class ContainerBloodInfuser extends Container {
    private final TileBloodInfuser tile;
    private final EntityPlayer player;
    private final AspectList aspectsKnown;

    public ContainerBloodInfuser(EntityPlayer p, TileBloodInfuser tileEntity) {
        player=p;
        tile=tileEntity;
        IPlayerKnowledge knowledge = p.getCapability(PlayerKnowledgeProvider.PLAYER_KNOWLEDGE, null);
        aspectsKnown = knowledge == null ? new AspectList() : knowledge.getAspectsDiscovered().copy();
        InventoryPlayer inv=p.inventory;
        addSlotToContainer(new SlotRestricted(tile,0,16,37,new ItemStack(ThaumicHorizons.itemSyringeHuman)));
        for (int x=0; x<3; ++x) {
            for(int y=0; y<3; ++y) {
                addSlotToContainer(new Slot(tile,x*3+y+1,108+x*18,19+y*18) {
                    @Override public boolean isItemValid(ItemStack stack) {return false;}
                });
            }
        }
        for(int i=0;i<3;++i) for(int j=0;j<9;++j)
            addSlotToContainer(new Slot(inv,j+i*9+9,8+j*18,137+i*18));
        for(int i=0;i<9;++i) addSlotToContainer(new Slot(inv,i,8+i*18,195));
    }

    @Override public boolean canInteractWith(EntityPlayer p) {
        return tile.isUsableByPlayer(p);
    }

    @Override public ItemStack transferStackInSlot(EntityPlayer p,int slotIndex) {
        if(slotIndex<0||slotIndex>=inventorySlots.size())return ItemStack.EMPTY;
        Slot slot=inventorySlots.get(slotIndex);
        if(slot==null||!slot.getHasStack())return ItemStack.EMPTY;
        ItemStack source=slot.getStack();
        ItemStack original=source.copy();
        if(slotIndex<10) {
            if(!mergeItemStack(source,10,46,true))return ItemStack.EMPTY;
        } else if(source.getItem()==ThaumicHorizons.itemSyringeHuman) {
            if(!mergeItemStack(source,0,1,false))return ItemStack.EMPTY;
        } else return ItemStack.EMPTY;
        if(source.isEmpty())slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();
        if(source.getCount()==original.getCount())return ItemStack.EMPTY;
        slot.onTake(p,source);
        return original;
    }

    @Override public boolean enchantItem(EntityPlayer p,int button) {
        if(p!=player||!canInteractWith(p))return false;
        Aspect[] known=aspectsKnown.getAspectsSorted();
        if(button<0||button>2) {
            int index=button<0 ? -button-1 : button-3;
            if(index<0||index>=known.length)return false;
            Aspect aspect=known[index];
            if(aspect==null)return false;
            if(button<0) {
                if(tile.aspectsSelected.visSize()>=8)return false;
                tile.aspectsSelected.add(aspect,1);
            } else {
                if(tile.aspectsSelected.getAmount(aspect)<=0)return false;
                tile.aspectsSelected.remove(aspect,1);
            }
        } else {
            tile.mode=button;
        }
        tile.markDirty();
        if(tile.getWorld()!=null) {
            net.minecraft.block.state.IBlockState state=tile.getWorld().getBlockState(tile.getPos());
            tile.getWorld().notifyBlockUpdate(tile.getPos(),state,state,3);
        }
        return true;
    }
}
