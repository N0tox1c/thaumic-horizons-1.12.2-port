//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

public class ItemCorpseEffigy extends Item  implements com.kentington.thaumichorizons.common.lib.OriginalItemAppearance , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider {

    public ItemCorpseEffigy() {
        this.setMaxStackSize(1);
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public void registerIcons(net.minecraft.client.renderer.texture.TextureMap atlas) {
        icon = atlas.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumcraft:brain", "items"));
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    private net.minecraft.client.renderer.texture.TextureAtlasSprite icon;

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getIconFromDamage(int meta) { return icon; }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.corpseEffigy";
    }
}
