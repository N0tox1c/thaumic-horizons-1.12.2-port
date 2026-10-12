//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.world.IBlockAccess;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrystal extends LegacyBlockBreakable {

    public TextureAtlasSprite[] icon;

    public BlockCrystal() {
        super("thaumichorizons:blockCrystalRed", Material.GLASS, false);
        this.icon = new TextureAtlasSprite[16];
        this.setCreativeTab(ThaumicHorizons.tabTH);
        this.setHardness(1.75f);
        this.setResistance(0.5f);
        this.setStepSound(net.minecraft.block.SoundType.GLASS);
        this.setLightOpacity(1);
        this.setTranslationKey("ThaumicHorizons_crystal");
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap par1IconRegister) {
        this.icon[0] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalWhite", "blocks"));
        this.icon[1] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalOrange", "blocks"));
        this.icon[2] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalMagenta", "blocks"));
        this.icon[3] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalLightBlue", "blocks"));
        this.icon[4] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalYellow", "blocks"));
        this.icon[5] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalLime", "blocks"));
        this.icon[6] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalPink", "blocks"));
        this.icon[7] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalGray", "blocks"));
        this.icon[8] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalLightGray", "blocks"));
        this.icon[9] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalCyan", "blocks"));
        this.icon[10] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalPurple", "blocks"));
        this.icon[11] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalBlue", "blocks"));
        this.icon[12] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalBrown", "blocks"));
        this.icon[13] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalGreen", "blocks"));
        this.icon[14] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalRed", "blocks"));
        this.icon[15] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:blockCrystalBlack", "blocks"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(final int par1, final int par2) {
        return this.icon[par2];
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int var4 = 0; var4 < 16; ++var4) {
            par3List.add(new ItemStack(this, 1, var4));
        }
    }

    public int damageDropped(final int par1) {
        return par1;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public MapColor getMapColor(final int p_149728_1_) {
        return MapColor.getBlockColor(net.minecraft.item.EnumDyeColor.byMetadata(p_149728_1_));
    }

    @Override
    public int damageDropped(net.minecraft.block.state.IBlockState state) {
        return damageDropped(getMetaFromState(state));
    }

    @Override
    public net.minecraft.block.material.MapColor getMapColor(net.minecraft.block.state.IBlockState state,IBlockAccess world,net.minecraft.util.math.BlockPos pos) {
        return getMapColor(getMetaFromState(state));
    }

    @Override
    public void getSubBlocks(CreativeTabs tab,net.minecraft.util.NonNullList<ItemStack> items) {
        getSubBlocks(net.minecraft.item.Item.getItemFromBlock(this),tab,items);
    }
}
