package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class LegacyBlockBreakable extends LegacyBlock {
    private final boolean ignoreSimilarity;
    public LegacyBlockBreakable(String texture,Material material,boolean ignoreSimilarity) {
        super(material); setBlockTextureName(texture); this.ignoreSimilarity=ignoreSimilarity;
    }
    @Override public boolean isOpaqueCube() { return false; }
    @Override public boolean shouldSideBeRendered(IBlockState state,IBlockAccess world,BlockPos pos,EnumFacing face) {
        if (!ignoreSimilarity && world.getBlockState(pos.offset(face)).getBlock()==this) return false;
        return super.shouldSideBeRendered(state,world,pos,face);
    }
}
