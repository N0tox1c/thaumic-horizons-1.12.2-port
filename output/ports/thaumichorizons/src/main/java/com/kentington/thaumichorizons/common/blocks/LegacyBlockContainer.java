package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class LegacyBlockContainer extends LegacyBlock implements ITileEntityProvider {
    public LegacyBlockContainer(Material material) { super(material); }
    @Override public boolean hasTileEntity(IBlockState state) { return true; }
    @Override public TileEntity createTileEntity(World world,IBlockState state) {
        return createNewTileEntity(world,getMetaFromState(state));
    }
}
