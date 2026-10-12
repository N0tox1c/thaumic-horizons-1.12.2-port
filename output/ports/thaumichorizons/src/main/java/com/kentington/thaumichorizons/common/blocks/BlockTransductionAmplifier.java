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
import com.kentington.thaumichorizons.common.tiles.TileTransductionAmplifier;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.tiles.TileNodeEnergized;

public class BlockTransductionAmplifier extends LegacyBlockContainer {

    TextureAtlasSprite icon;

    public BlockTransductionAmplifier() {
        super(Material.ROCK);
        this.setTranslationKey("ThaumicHorizons_transductionAmplifier");
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public TileEntity createNewTileEntity(final World world, final int md) {
        return this.createTileEntity(world, md);
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileTransductionAmplifier();
    }

    public boolean canPlaceBlockAt(final World p_149742_1_, final int p_149742_2_, final int p_149742_3_,
            final int p_149742_4_) {
        return p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_ - 1, p_149742_3_, p_149742_4_)) instanceof TileNodeEnergized
                || p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_ + 1, p_149742_3_, p_149742_4_)) instanceof TileNodeEnergized
                || p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_, p_149742_4_ - 1)) instanceof TileNodeEnergized
                || p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_, p_149742_4_ + 1)) instanceof TileNodeEnergized
                || p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_ - 1, p_149742_4_)) instanceof TileNodeEnergized
                || p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_ + 1, p_149742_4_)) instanceof TileNodeEnergized;
    }

    public boolean canPlaceBlockOnSide(final World p_149742_1_, final int p_149742_2_, final int p_149742_3_,
            final int p_149742_4_, final int p_149742_5_) {
        final EnumFacing dir = EnumFacing.byIndex(p_149742_5_);
        return (dir == EnumFacing.NORTH
                && p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_, p_149742_4_ + 1)) instanceof TileNodeEnergized)
                || (dir == EnumFacing.SOUTH && p_149742_1_
                        .getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_, p_149742_4_ - 1)) instanceof TileNodeEnergized)
                || (dir == EnumFacing.WEST && p_149742_1_
                        .getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_ + 1, p_149742_3_, p_149742_4_)) instanceof TileNodeEnergized)
                || (dir == EnumFacing.EAST && p_149742_1_
                        .getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_ - 1, p_149742_3_, p_149742_4_)) instanceof TileNodeEnergized);
    }

    public int onBlockPlaced(final World p_149742_1_, final int p_149742_2_, final int p_149742_3_,
            final int p_149742_4_, final int p_149660_5_, final float p_149660_6_, final float p_149660_7_,
            final float p_149660_8_, final int p_149660_9_) {
        if (p_149660_5_ == 0
                && p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_ + 1, p_149742_4_)) instanceof TileNodeEnergized) {
            return 0;
        }
        if (p_149660_5_ == 1
                && p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_ - 1, p_149742_4_)) instanceof TileNodeEnergized) {
            return 1;
        }
        if (p_149660_5_ == 2
                && p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_, p_149742_4_ + 1)) instanceof TileNodeEnergized) {
            return 2;
        }
        if (p_149660_5_ == 3
                && p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_, p_149742_4_ - 1)) instanceof TileNodeEnergized) {
            return 3;
        }
        if (p_149660_5_ == 4
                && p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_ + 1, p_149742_3_, p_149742_4_)) instanceof TileNodeEnergized) {
            return 4;
        }
        if (p_149660_5_ == 5
                && p_149742_1_.getTileEntity(new net.minecraft.util.math.BlockPos(p_149742_2_ - 1, p_149742_3_, p_149742_4_)) instanceof TileNodeEnergized) {
            return 5;
        }
        return -1;
    }

    public void killMe(final World world, final int x, final int y, final int z, final boolean drop) {
        if (((TileTransductionAmplifier) world.getTileEntity(new BlockPos(x, y, z))).activated) {
            ((TileTransductionAmplifier) world.getTileEntity(new BlockPos(x, y, z))).unBoostNode(x, y, z);
        }
        if (drop) {
            this.dropBlockAsItem(world, x, y, z, 0, 0);
        }
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
        return ThaumicHorizons.blockTransducerRI;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("iron_block", "blocks"));
    }

    public TextureAtlasSprite getIcon(final int par1, final int par2) {
        return this.icon;
    }

    public void onBlockPreDestroy(final World world, final int x, final int y, final int z, final int md) {
        this.killMe(world, x, y, z, false);
    }

    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block nbid) {
        final TileTransductionAmplifier tile = (TileTransductionAmplifier) world.getTileEntity(new BlockPos(x, y, z));
        if (tile.activated && !com.kentington.thaumichorizons.common.lib.WorldAccess.isBlockIndirectlyGettingPowered(world, x, y, z)) {
            tile.shouldActivate = false;
        } else if (!tile.activated && com.kentington.thaumichorizons.common.lib.WorldAccess.isBlockIndirectlyGettingPowered(world, x, y, z)) {
            tile.shouldActivate = true;
        }
    }

    public boolean isSideSolid(final IBlockAccess world, final int x, final int y, final int z,
            final EnumFacing side) {
        switch (world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z)))) {
    case 0: {
                return side == EnumFacing.DOWN;
            }
    case 1: {
                return side == EnumFacing.UP;
            }
    case 2: {
                return side == EnumFacing.NORTH;
            }
    case 3: {
                return side == EnumFacing.SOUTH;
            }
    case 4: {
                return side == EnumFacing.WEST;
            }
    case 5: {
                return side == EnumFacing.EAST;
            }
    default: {
                return false;
            }
}
    }

    @Override
    public void neighborChanged(net.minecraft.block.state.IBlockState state,World world,net.minecraft.util.math.BlockPos pos,Block neighbor,net.minecraft.util.math.BlockPos fromPos) {
        onNeighborBlockChange(world,pos.getX(), pos.getY(), pos.getZ(),neighbor);
    }

    @Override
    public boolean canPlaceBlockAt(World world,net.minecraft.util.math.BlockPos pos) {
        return canPlaceBlockAt(world,pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean canPlaceBlockOnSide(World world,net.minecraft.util.math.BlockPos pos,net.minecraft.util.EnumFacing side) {
        return canPlaceBlockOnSide(world,pos.getX(), pos.getY(), pos.getZ(),side.getIndex());
    }

    @Override
    public net.minecraft.block.state.IBlockState getStateForPlacement(World world,net.minecraft.util.math.BlockPos pos,net.minecraft.util.EnumFacing side,float hitX,float hitY,float hitZ,int meta,net.minecraft.entity.EntityLivingBase placer) {
        return getStateFromMeta(onBlockPlaced(world,pos.getX(), pos.getY(), pos.getZ(),side.getIndex(),hitX,hitY,hitZ,meta));
    }

    @Override
    public boolean isSideSolid(net.minecraft.block.state.IBlockState state,IBlockAccess world,net.minecraft.util.math.BlockPos pos,net.minecraft.util.EnumFacing face) {
        return isSideSolid(world,pos.getX(), pos.getY(), pos.getZ(),face);
    }

    @Override
    public void breakBlock(World world,net.minecraft.util.math.BlockPos pos,net.minecraft.block.state.IBlockState state) {
        if (!world.isRemote) onBlockPreDestroy(world,pos.getX(), pos.getY(), pos.getZ(),getMetaFromState(state));
        super.breakBlock(world,pos,state);
    }
}
