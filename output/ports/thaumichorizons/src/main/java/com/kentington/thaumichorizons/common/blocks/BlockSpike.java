//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileSpike;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpike extends LegacyBlockContainer {

    int type;
    TextureAtlasSprite icon;
    TextureAtlasSprite iconWood;
    TextureAtlasSprite iconTooth;

    public BlockSpike(final int spikeType, final Material mat, final String name) {
        super(mat);
        this.type = spikeType;
        this.setHardness(3.0f);
        this.setResistance(3.0f);
        this.setTranslationKey("ThaumicHorizons_" + name);
        this.setBlockTextureName("ThaumicHorizons:spike");
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 0;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return ThaumicHorizons.blockSpikeRI;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("iron_block", "blocks"));
        this.iconWood = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumcraft:greatwoodtop", "blocks"));
        this.iconTooth = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:bone", "blocks"));
    }

    public TextureAtlasSprite getIcon(final int par1, final int par2) {
        switch (this.type) {
    case 1: {
                return this.iconWood;
            }
    case 2: {
                return this.iconTooth;
            }
    default: {
                return this.icon;
            }
}
    }

    public void onEntityCollidedWithBlock(final World p_149670_1_, final int p_149670_2_, final int p_149670_3_,
            final int p_149670_4_, final Entity p_149670_5_) {
        p_149670_5_.attackEntityFrom(DamageSource.CACTUS, 1.0f);
    }

    public TileEntity createNewTileEntity(final World world, final int md) {
        return this.createTileEntity(world, md);
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileSpike((byte) metadata, (byte) this.type);
    }

    public boolean canPlaceBlockOnSide(final World p_149742_1_, final int p_149742_2_, final int p_149742_3_,
            final int p_149742_4_, final int p_149742_5_) {
        final EnumFacing dir = EnumFacing.byIndex(p_149742_5_);
        return (dir == EnumFacing.DOWN
                && p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_ + 1, p_149742_4_), EnumFacing.DOWN))
                || (dir == EnumFacing.UP
                        && p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_ - 1, p_149742_4_), EnumFacing.UP))
                || (dir == EnumFacing.NORTH
                        && p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_, p_149742_4_ + 1), EnumFacing.NORTH))
                || (dir == EnumFacing.SOUTH
                        && p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_, p_149742_4_ - 1), EnumFacing.SOUTH))
                || (dir == EnumFacing.WEST
                        && p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_ + 1, p_149742_3_, p_149742_4_), EnumFacing.WEST))
                || (dir == EnumFacing.EAST
                        && p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_ - 1, p_149742_3_, p_149742_4_), EnumFacing.EAST));
    }

    public boolean canPlaceBlockAt(final World p_149742_1_, final int p_149742_2_, final int p_149742_3_,
            final int p_149742_4_) {
        return p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_ - 1, p_149742_3_, p_149742_4_), EnumFacing.EAST)
                || p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_ + 1, p_149742_3_, p_149742_4_), EnumFacing.WEST)
                || p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_, p_149742_4_ - 1), EnumFacing.SOUTH)
                || p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_, p_149742_4_ + 1), EnumFacing.NORTH)
                || p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_ - 1, p_149742_4_), EnumFacing.UP)
                || p_149742_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149742_2_, p_149742_3_ + 1, p_149742_4_), EnumFacing.DOWN);
    }

    public int onBlockPlaced(final World p_149660_1_, final int p_149660_2_, final int p_149660_3_,
            final int p_149660_4_, final int p_149660_5_, final float p_149660_6_, final float p_149660_7_,
            final float p_149660_8_, final int p_149660_9_) {
        byte b0 = -1;
        if (p_149660_5_ == 0
                && p_149660_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149660_2_, p_149660_3_ + 1, p_149660_4_), EnumFacing.DOWN)) {
            b0 = 0;
        }
        if (p_149660_5_ == 1 && p_149660_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149660_2_, p_149660_3_ - 1, p_149660_4_), EnumFacing.UP)) {
            b0 = 1;
        }
        if (p_149660_5_ == 2
                && p_149660_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149660_2_, p_149660_3_, p_149660_4_ + 1), EnumFacing.NORTH)) {
            b0 = 2;
        }
        if (p_149660_5_ == 3
                && p_149660_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149660_2_, p_149660_3_, p_149660_4_ - 1), EnumFacing.SOUTH)) {
            b0 = 3;
        }
        if (p_149660_5_ == 4
                && p_149660_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149660_2_ + 1, p_149660_3_, p_149660_4_), EnumFacing.WEST)) {
            b0 = 4;
        }
        if (p_149660_5_ == 5
                && p_149660_1_.isSideSolid(new net.minecraft.util.math.BlockPos(p_149660_2_ - 1, p_149660_3_, p_149660_4_), EnumFacing.EAST)) {
            b0 = 5;
        }
        return b0;
    }

    public void onNeighborBlockChange(final World p_149695_1_, final int p_149695_2_, final int p_149695_3_,
            final int p_149695_4_, final Block p_149695_5_) {
        this.func_149820_e(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
    }

    private void func_149820_e(final World p_149820_1_, final int p_149820_2_, final int p_149820_3_,
            final int p_149820_4_) {
        if (!this.canPlaceBlockOnSide(
                p_149820_1_,
                p_149820_2_,
                p_149820_3_,
                p_149820_4_,
                com.kentington.thaumichorizons.common.lib.WorldAccess.getBlockMetadata(p_149820_1_, p_149820_2_, p_149820_3_, p_149820_4_))) {
            this.dropBlockAsItem(
                    p_149820_1_,
                    p_149820_2_,
                    p_149820_3_,
                    p_149820_4_,
                    com.kentington.thaumichorizons.common.lib.WorldAccess.getBlockMetadata(p_149820_1_, p_149820_2_, p_149820_3_, p_149820_4_),
                    0);
            p_149820_1_.setBlockToAir(new BlockPos(p_149820_2_, p_149820_3_, p_149820_4_));
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World p_149668_1_, final int p_149668_2_,
            final int p_149668_3_, final int p_149668_4_) {
        final float f = 0.0625f;
        final int md = com.kentington.thaumichorizons.common.lib.WorldAccess.getBlockMetadata(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
        if (md == 0) {
            return new AxisAlignedBB(
                    p_149668_2_,
                    p_149668_3_ + f,
                    p_149668_4_,
                    p_149668_2_ + 1,
                    p_149668_3_ + 1,
                    p_149668_4_ + 1);
        }
        if (md == 1) {
            return new AxisAlignedBB(
                    p_149668_2_,
                    p_149668_3_,
                    p_149668_4_,
                    p_149668_2_ + 1,
                    p_149668_3_ + 1 - f,
                    p_149668_4_ + 1);
        }
        if (md == 4) {
            return new AxisAlignedBB(
                    p_149668_2_ + f,
                    p_149668_3_,
                    p_149668_4_,
                    p_149668_2_ + 1,
                    p_149668_3_ + 1,
                    p_149668_4_ + 1);
        }
        if (md == 5) {
            return new AxisAlignedBB(
                    p_149668_2_,
                    p_149668_3_,
                    p_149668_4_,
                    p_149668_2_ + 1 - f,
                    p_149668_3_ + 1,
                    p_149668_4_ + 1);
        }
        if (md == 2) {
            return new AxisAlignedBB(
                    p_149668_2_,
                    p_149668_3_,
                    p_149668_4_ + f,
                    p_149668_2_ + 1,
                    p_149668_3_ + 1,
                    p_149668_4_ + 1);
        }
        if (md == 3) {
            return new AxisAlignedBB(
                    p_149668_2_,
                    p_149668_3_,
                    p_149668_4_,
                    p_149668_2_ + 1,
                    p_149668_3_ + 1,
                    p_149668_4_ + 1 - f);
        }
        return new AxisAlignedBB(
                p_149668_2_ + f,
                p_149668_3_,
                p_149668_4_ + f,
                p_149668_2_ + 1 - f,
                p_149668_3_ + 1 - f,
                p_149668_4_ + 1 - f);
    }

    @Override
    public void onEntityCollision(World world,net.minecraft.util.math.BlockPos pos,net.minecraft.block.state.IBlockState state,Entity entity) {
        onEntityCollidedWithBlock(world,pos.getX(), pos.getY(), pos.getZ(),entity);
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
    public net.minecraft.util.math.AxisAlignedBB getCollisionBoundingBox(net.minecraft.block.state.IBlockState state,IBlockAccess world,net.minecraft.util.math.BlockPos pos) {
        if (!(world instanceof World)) return getBoundingBox(state,world,pos);
        net.minecraft.util.math.AxisAlignedBB box=getCollisionBoundingBoxFromPool((World)world,pos.getX(), pos.getY(), pos.getZ());
        return box==null ? null : box.offset(-pos.getX(),-pos.getY(),-pos.getZ());
    }
}
