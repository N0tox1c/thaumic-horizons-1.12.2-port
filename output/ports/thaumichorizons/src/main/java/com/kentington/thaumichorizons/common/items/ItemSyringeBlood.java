package com.kentington.thaumichorizons.common.items;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * 1.7.10 ItemSyringeBlood reworked to Forge 1.12.2 item API.
 * Metadata 0 = syringe, 1 = phial; original translation keys retained.
 * Sprite/mesh handling must be migrated at the client registry layer.
 */
public class ItemSyringeBlood extends Item {
    public ItemSyringeBlood() {
        setCreativeTab(ThaumicHorizons.tabTH);
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "item.syringeBlood." + stack.getMetadata();
    }
}
