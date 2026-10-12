//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ItemWandCastingDisposable extends ItemWandCasting {

    public ItemStack wand;

    public ItemWandCastingDisposable() {
        this.maxStackSize = 1;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(ThaumicHorizons.tabTH);
        final ItemStack w1 = new ItemStack(this);
        this.setCap(w1, ThaumicHorizons.CAP_CRYSTAL);
        this.setRod(w1, ThaumicHorizons.ROD_CRYSTAL);
        for (final Aspect aspect : Aspect.getPrimalAspects()) {
            this.storeVis(w1, aspect, 25000);
        }
        this.wand = w1;
    }

    @Override public int getVisCapacity(ItemStack stack) { return 25000; }
    @Override public boolean canRecharge(ItemStack stack) { return false; }

    public AspectList getAspectsWithRoom(final ItemStack wandstack) {
        return new AspectList();
    }

    @Override public void getSubItems(CreativeTabs tab, net.minecraft.util.NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) items.add(wand.copy());
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(this.wand.copy());
    }
}
