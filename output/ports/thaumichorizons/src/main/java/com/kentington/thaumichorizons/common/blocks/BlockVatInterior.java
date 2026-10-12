//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileVatSlave;

public class BlockVatInterior extends LegacyBlockContainer {

    public BlockVatInterior() {
        super(Material.WATER);
        this.setHardness(3.0f);
        this.setResistance(15.0f);
        this.setLightLevel(8.0F / 15.0F);
        this.setTranslationKey("ThaumicHorizons_vatInterior");

    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileVatSlave();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileVatSlave) ((TileVatSlave) tile).killMyBoss(0);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE; // Internal volume is drawn by the vat slave renderer.
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand, EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileVatSlave)) return false;
        if (world.isRemote) return true;
        return ((TileVatSlave) tile).activate(player);
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        entity.setAir(300);
    }

    @Override
    public Boolean isEntityInsideMaterial(net.minecraft.world.IBlockAccess world, BlockPos pos,
            IBlockState state, Entity entity, double yToTest, Material material, boolean testingHead) {
        // Preserve the original still-water volume. Forge's explicit hook avoids
        // vanilla reading BlockLiquid.LEVEL from this non-BlockLiquid tile block.
        double surface = pos.getY() + 8.0D / 9.0D;
        return material == Material.WATER && (testingHead ? yToTest < surface : yToTest >= surface);
    }

}
