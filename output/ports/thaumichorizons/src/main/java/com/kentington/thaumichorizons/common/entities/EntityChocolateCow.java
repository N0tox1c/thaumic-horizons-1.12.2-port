//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;

import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

public class EntityChocolateCow extends EntityCow {

    public EntityChocolateCow(final World p_i1683_1_) {
        super(p_i1683_1_);
    }

    @Override
    public boolean processInteract(final EntityPlayer p_70085_1_, net.minecraft.util.EnumHand hand) {
        final ItemStack itemstack = p_70085_1_.getHeldItem(hand);
        if (!itemstack.isEmpty() && itemstack.getItem() == Items.BUCKET) {
            if (world.isRemote) return true;
            itemstack.shrink(1);
            if (itemstack.isEmpty()) {
                p_70085_1_.setHeldItem(hand, new ItemStack(ThaumicHorizons.itemBucketChocolate));
            } else
                if (!p_70085_1_.inventory.addItemStackToInventory(new ItemStack(ThaumicHorizons.itemBucketChocolate))) {
                    p_70085_1_.dropItem(
                            new ItemStack(ThaumicHorizons.itemBucketChocolate, 1, 0),
                            false);
                }
            return true;
        }
        return super.processInteract(p_70085_1_, hand);
    }
}
