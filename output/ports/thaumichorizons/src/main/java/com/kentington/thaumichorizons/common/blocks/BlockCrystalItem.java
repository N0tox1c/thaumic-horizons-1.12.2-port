//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockCrystalItem extends ItemBlock {

    public BlockCrystalItem(final Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    public int getMetadata(final int par1) {
        return par1;
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return super.getTranslationKey() + "." + par1ItemStack.getItemDamage();
    }
}
