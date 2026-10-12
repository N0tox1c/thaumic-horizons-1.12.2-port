//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items.lenses;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLensEarth extends Item implements ILens  , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    TextureAtlasSprite icon;

    public ItemLensEarth() {
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public String lensName() {
        return "LensEarth";
    }

    public void handleRender(final Minecraft mc, final float partialTicks) {}

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.LensEarth";
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:lensearth", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public void handleRemoval(final EntityPlayer p) {}
}
