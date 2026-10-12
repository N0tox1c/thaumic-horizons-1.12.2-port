//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrystalDeep extends LegacyBlockBreakable {

    public TextureAtlasSprite icon;

    public BlockCrystalDeep() {
        super("glass", Material.GLASS, false);
        this.setCreativeTab(ThaumicHorizons.tabTH);
        this.setTranslationKey("ThaumicHorizons_deepcrystal");
        this.setHardness(2.0f);
        this.setStepSound(net.minecraft.block.SoundType.GLASS);
        this.setLightLevel(1.0f);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap par1IconRegister) {
        this.icon = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalDeep", "blocks"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(final int par1, final int par2) {
        return this.icon;
    }

    public int getRenderBlockPass() {
        return 1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

}
