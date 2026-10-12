package fox.spiteful.forbidden.compat;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
/** Coordinate-call adapter; all reads/writes go through native 1.12 block states. */
public final class LegacyWorldAccess {
    private LegacyWorldAccess() {}
    public static Block getBlock(IBlockAccess world,int x,int y,int z){return world.getBlockState(new BlockPos(x,y,z)).getBlock();}
    public static int getBlockMetadata(IBlockAccess world,int x,int y,int z){IBlockState state=world.getBlockState(new BlockPos(x,y,z));return state.getBlock().getMetaFromState(state);}
    public static TileEntity getTileEntity(IBlockAccess world,int x,int y,int z){return world.getTileEntity(new BlockPos(x,y,z));}
    public static boolean blockExists(World world,int x,int y,int z){return world.isBlockLoaded(new BlockPos(x,y,z));}
    public static boolean setBlockToAir(World world,int x,int y,int z){return world.setBlockToAir(new BlockPos(x,y,z));}
    public static boolean setBlock(World world,int x,int y,int z,Block block){return world.setBlockState(new BlockPos(x,y,z),block.getDefaultState(),3);}
    public static boolean setBlock(World world,int x,int y,int z,Block block,int meta,int flags){return world.setBlockState(new BlockPos(x,y,z),block.getStateFromMeta(meta),flags);}
    public static int getIndirectPowerLevelTo(World world,int x,int y,int z,int side){return world.getRedstonePower(new BlockPos(x,y,z),EnumFacing.byIndex(side));}
}
