//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBucketChocolate extends ItemBucketMilk  implements com.kentington.thaumichorizons.common.lib.OriginalItemAppearance , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider {

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon;

    public ItemBucketChocolate() {
        this.setMaxStackSize(1);
        this.setContainerItem(Items.BUCKET);
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public ItemStack onEaten(final ItemStack p_77654_1_, final World p_77654_2_, final EntityPlayer p_77654_3_) {
        if (!p_77654_3_.capabilities.isCreativeMode) {
            p_77654_1_.shrink(1);
        }
        if (!p_77654_2_.isRemote) {
            p_77654_3_.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
        }
        return (p_77654_1_.getCount() <= 0) ? new ItemStack(Items.BUCKET) : p_77654_1_;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:bucket_chocolatemilk", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.chocolateMilk";
    }
}
