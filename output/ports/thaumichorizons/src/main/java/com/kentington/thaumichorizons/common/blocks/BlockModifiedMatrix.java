//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileVat;
import com.kentington.thaumichorizons.common.tiles.TileVatMatrix;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockModifiedMatrix extends LegacyBlockContainer {

    TextureAtlasSprite icon;

    public BlockModifiedMatrix() {
        super(Material.ROCK);
        this.setHardness(2.0f);
        this.setResistance(2.0f);
        this.setTranslationKey("ThaumicHorizons_modMatrix");
        this.setBlockTextureName("ThaumicHorizons:modMatrix");
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public TileEntity createNewTileEntity(final World world, final int md) {
        return this.createTileEntity(world, md);
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileVatMatrix();
    }

    public boolean canPlaceBlockAt(final World world, final int x, final int y, final int z) {
        return world.getTileEntity(new net.minecraft.util.math.BlockPos(x, y - 1, z)) instanceof TileVat;
    }

    public void breakBlock(final World p_149749_1_, final int p_149749_2_, final int p_149749_3_, final int p_149749_4_,
            final Block p_149749_5_, final int p_149749_6_) {
        final TileVat tile = (TileVat) p_149749_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149749_2_, p_149749_3_ - 1, p_149749_4_));
        if (tile != null && tile.mode == 2) {
            tile.killSubject();
        }
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return ThaumicHorizons.blockVatMatrixRI;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumcraft:arcane_stone", "blocks"));
    }

    public TextureAtlasSprite getIcon(final int par1, final int par2) {
        return this.icon;
    }

    @Override
    public void breakBlock(World world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState state) {
        breakBlock(world,pos.getX(), pos.getY(), pos.getZ(),state.getBlock(),getMetaFromState(state));
    }

    @Override
    public boolean canPlaceBlockAt(World world,net.minecraft.util.math.BlockPos pos) {
        return canPlaceBlockAt(world,pos.getX(), pos.getY(), pos.getZ());
    }
}
