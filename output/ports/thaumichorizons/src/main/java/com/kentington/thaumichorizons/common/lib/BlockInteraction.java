package com.kentington.thaumichorizons.common.lib;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;

public final class BlockInteraction {
    private BlockInteraction() {}
    /** Forge returns negative XP when a break is cancelled; transformations do not award that XP. */
    public static boolean canBreak(net.minecraft.world.World world, net.minecraft.entity.player.EntityPlayer player, BlockPos pos) {
        if (world.isRemote || !(player instanceof net.minecraft.entity.player.EntityPlayerMP)
                || !world.isBlockModifiable(player, pos)) return false;
        net.minecraft.entity.player.EntityPlayerMP serverPlayer = (net.minecraft.entity.player.EntityPlayerMP) player;
        return net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(world,
                serverPlayer.interactionManager.getGameType(), serverPlayer, pos) >= 0;
    }
    /** Directionless heat tests every exposed block face; native Forge has no UNKNOWN facing. */
    public static boolean isFlammable(Block block,IBlockAccess world,int x,int y,int z,EnumFacing face) {
        BlockPos pos=new BlockPos(x,y,z);
        if (face!=null) return block.isFlammable(world,pos,face);
        for (EnumFacing side:EnumFacing.VALUES) if (block.isFlammable(world,pos,side)) return true;
        return false;
    }
}
