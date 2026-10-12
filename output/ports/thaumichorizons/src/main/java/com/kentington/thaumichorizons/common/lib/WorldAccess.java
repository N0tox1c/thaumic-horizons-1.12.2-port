package com.kentington.thaumichorizons.common.lib;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public final class WorldAccess {
    private WorldAccess() {}
    public static int getBlockMetadata(IBlockAccess world,int x,int y,int z) {
        IBlockState state=world.getBlockState(new BlockPos(x,y,z)); return state.getBlock().getMetaFromState(state);
    }
    public static boolean setBlockMetadataWithNotify(World world,int x,int y,int z,int meta,int flags) {
        BlockPos pos=new BlockPos(x,y,z); IBlockState old=world.getBlockState(pos);
        return world.setBlockState(pos,old.getBlock().getStateFromMeta(meta),flags);
    }
    public static void markBlockForUpdate(World world,int x,int y,int z) {
        BlockPos pos=new BlockPos(x,y,z); IBlockState state=world.getBlockState(pos);
        world.notifyBlockUpdate(pos,state,state,3);
    }
    public static boolean isBlockIndirectlyGettingPowered(World world,int x,int y,int z) {
        return world.isBlockPowered(new BlockPos(x,y,z));
    }
    public static void notifyBlocksOfNeighborChange(World world,int x,int y,int z,net.minecraft.block.Block block) {
        BlockPos pos=new BlockPos(x,y,z);
        world.notifyNeighborsOfStateChange(pos,block==null?world.getBlockState(pos).getBlock():block,false);
    }
}
