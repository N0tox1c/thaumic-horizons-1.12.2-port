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
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileVortexStabilizer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockVortexStabilizer extends LegacyBlockContainer {

    TextureAtlasSprite icon;

    public BlockVortexStabilizer() {
        super(Material.IRON);
        this.setTranslationKey("ThaumicHorizons_vortexStabilizer");
        this.setHardness(10.0f);
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public int onBlockPlaced(final World p_149742_1_, final int p_149742_2_, final int p_149742_3_,
            final int p_149742_4_, final int p_149660_5_, final float p_149660_6_, final float p_149660_7_,
            final float p_149660_8_, final int p_149660_9_) {
        return p_149660_5_;
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileVortexStabilizer();
    }

    public void killMe(final World world, final int x, final int y, final int z, final boolean drop) {
        if (((TileVortexStabilizer) world.getTileEntity(new BlockPos(x, y, z))).hasTarget) {
            ((TileVortexStabilizer) world.getTileEntity(new BlockPos(x, y, z))).reHungrifyTarget();
        }
        if (drop) {
            this.dropBlockAsItem(world, x, y, z, 0, 0);
        }
    }

    public void onBlockPreDestroy(final World world, final int x, final int y, final int z, final int md) {
        this.killMe(world, x, y, z, false);
    }

    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block nbid) {
        if (world.isRemote) return;

        final TileVortexStabilizer tile = (TileVortexStabilizer) world.getTileEntity(new BlockPos(x, y, z));
        tile.redstoned = com.kentington.thaumichorizons.common.lib.WorldAccess.isBlockIndirectlyGettingPowered(world, x, y, z);
        if ((!tile.redstoned && !com.kentington.thaumichorizons.common.lib.WorldAccess.isBlockIndirectlyGettingPowered(world, x, y, z))
                || (tile.redstoned && com.kentington.thaumichorizons.common.lib.WorldAccess.isBlockIndirectlyGettingPowered(world, x, y, z))) {
            tile.markDirty();
            com.kentington.thaumichorizons.common.lib.WorldAccess.markBlockForUpdate(tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
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
        return ThaumicHorizons.blockVortexStabilizerRI;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("iron_block", "blocks"));
    }

    public TextureAtlasSprite getIcon(final int par1, final int par2) {
        return this.icon;
    }

    public TileEntity createNewTileEntity(final World world, final int md) {
        return this.createTileEntity(world, md);
    }

    @Override
    public void neighborChanged(net.minecraft.block.state.IBlockState state,World world,net.minecraft.util.math.BlockPos pos,Block neighbor,net.minecraft.util.math.BlockPos fromPos) {
        onNeighborBlockChange(world,pos.getX(), pos.getY(), pos.getZ(),neighbor);
    }

    @Override
    public net.minecraft.block.state.IBlockState getStateForPlacement(World world,net.minecraft.util.math.BlockPos pos,net.minecraft.util.EnumFacing side,float hitX,float hitY,float hitZ,int meta,net.minecraft.entity.EntityLivingBase placer) {
        return getStateFromMeta(onBlockPlaced(world,pos.getX(), pos.getY(), pos.getZ(),side.getIndex(),hitX,hitY,hitZ,meta));
    }

    @Override
    public void breakBlock(World world,net.minecraft.util.math.BlockPos pos,net.minecraft.block.state.IBlockState state) {
        if (!world.isRemote) onBlockPreDestroy(world,pos.getX(), pos.getY(), pos.getZ(),getMetaFromState(state));
        super.breakBlock(world,pos,state);
    }
}
