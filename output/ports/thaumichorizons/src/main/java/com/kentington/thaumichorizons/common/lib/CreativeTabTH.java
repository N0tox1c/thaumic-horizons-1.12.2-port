//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class CreativeTabTH extends CreativeTabs {

    public CreativeTabTH(final int par1, final String par2Str) {
        super(par2Str);
    }

    @SideOnly(Side.CLIENT)
    public ItemStack createIcon() {
        return new ItemStack(ThaumicHorizons.itemDummyVat);
    }
}
