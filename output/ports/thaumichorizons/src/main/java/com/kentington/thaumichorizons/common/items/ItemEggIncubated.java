//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityEggIncubated;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEggIncubated extends ItemEgg  implements com.kentington.thaumichorizons.common.lib.OriginalItemAppearance , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider {

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon;

    public ItemEggIncubated() {
        this.maxStackSize = 16;
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public ItemStack onItemRightClick(final ItemStack p_77659_1_, final World p_77659_2_,
            final EntityPlayer p_77659_3_) {
        if (!p_77659_3_.capabilities.isCreativeMode) {
            p_77659_1_.shrink(1);
        }
        com.kentington.thaumichorizons.common.lib.THSoundBridge.playAtEntity(p_77659_2_, p_77659_3_, "random.bow", 0.5f, 0.4f / (ItemEggIncubated.itemRand.nextFloat() * 0.4f + 0.8f));
        if (!p_77659_2_.isRemote) {
            p_77659_2_.spawnEntity(new EntityEggIncubated(p_77659_2_, p_77659_3_));
        }
        return p_77659_1_;
    }

    @Override
    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, net.minecraft.util.EnumHand hand) {
        return new net.minecraft.util.ActionResult<>(net.minecraft.util.EnumActionResult.SUCCESS,
                onItemRightClick(player.getHeldItem(hand),world,player));
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:eggincubated", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.eggIncubated";
    }
}
