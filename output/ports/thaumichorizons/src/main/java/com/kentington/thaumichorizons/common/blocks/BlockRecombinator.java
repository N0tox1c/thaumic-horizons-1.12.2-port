//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileRecombinator;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRecombinator extends LegacyBlockContainer {

    TextureAtlasSprite icon;

    public BlockRecombinator() {
        super(Material.ROCK);
        this.setTranslationKey("ThaumicHorizons_recombinator");
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public TileEntity createNewTileEntity(final World world, final int md) {
        return this.createTileEntity(world, md);
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileRecombinator();
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
        return ThaumicHorizons.blockRecombinatorRI;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("iron_block", "blocks"));
    }

    public TextureAtlasSprite getIcon(final int par1, final int par2) {
        return this.icon;
    }

    public boolean isSideSolid(final IBlockAccess world, final int x, final int y, final int z,
            final EnumFacing side) {
        return side == EnumFacing.UP;
    }

    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block nbid) {
        final TileRecombinator tile = (TileRecombinator) world.getTileEntity(new BlockPos(x, y, z));
        if (tile.activated && !com.kentington.thaumichorizons.common.lib.WorldAccess.isBlockIndirectlyGettingPowered(world, x, y, z)) {
            tile.shouldActivate = false;
        } else if (!tile.activated && com.kentington.thaumichorizons.common.lib.WorldAccess.isBlockIndirectlyGettingPowered(world, x, y, z)) {
            tile.shouldActivate = true;
        }
    }

    @Override
    public void neighborChanged(net.minecraft.block.state.IBlockState state,World world,net.minecraft.util.math.BlockPos pos,Block neighbor,net.minecraft.util.math.BlockPos fromPos) {
        onNeighborBlockChange(world,pos.getX(), pos.getY(), pos.getZ(),neighbor);
    }

    @Override
    public boolean isSideSolid(net.minecraft.block.state.IBlockState state,IBlockAccess world,net.minecraft.util.math.BlockPos pos,net.minecraft.util.EnumFacing face) {
        return isSideSolid(world,pos.getX(), pos.getY(), pos.getZ(),face);
    }
}
