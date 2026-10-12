package com.kentington.thaumichorizons.common.lib;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

public final class BaubleInventory {
    private BaubleInventory() {}
    public static List<ItemStack> contents(EntityPlayer player) {
        IBaublesItemHandler handler=BaublesApi.getBaublesHandler(player);
        List<ItemStack> items=new ArrayList<>();
        for(int i=0;i<handler.getSlots();i++) items.add(handler.getStackInSlot(i));
        return items;
    }
    public static void clear(EntityPlayer player) {
        IBaublesItemHandler handler=BaublesApi.getBaublesHandler(player);
        for(int i=0;i<handler.getSlots();i++) handler.setStackInSlot(i,ItemStack.EMPTY);
    }
}
