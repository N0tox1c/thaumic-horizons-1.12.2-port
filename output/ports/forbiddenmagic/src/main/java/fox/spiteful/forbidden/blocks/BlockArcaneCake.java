package fox.spiteful.forbidden.blocks;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Port of Forbidden Magic's self-regenerating twelve-slice cake, not a vanilla
 * 7-bite BlockCake substitute. Metadata 0..11 and its hunger/saturation are kept.
 */
public class BlockArcaneCake extends Block {
    public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 11);
    private static final float INSET = 1F / 16F;

    public BlockArcaneCake() {
        super(Material.CAKE);
        setDefaultState(blockState.getBaseState().withProperty(BITES, 0));
        setTickRandomly(true);
        setHardness(0.5F);
        setSoundType(net.minecraft.block.SoundType.CLOTH);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty<?>[]{BITES});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BITES, Math.max(0, Math.min(11, meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BITES);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        int bites = state.getValue(BITES);
        // The old 1.7.10 expression inverted minX/maxX at 11 bites.
        float minX = Math.min(14F / 16F, (1F + bites) / 12F);
        return new AxisAlignedBB(minX, 0D, INSET, 15D / 16D, 8D / 16D, 15D / 16D);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
            EntityPlayer player, EnumHand hand, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        eatCakeSlice(world, pos, state, player);
        return true;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        eatCakeSlice(world, pos, world.getBlockState(pos), player);
    }

    private void eatCakeSlice(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (world.isRemote || !player.canEat(false)) return;
        player.getFoodStats().addStats(2, 1.0F);
        int next = state.getValue(BITES) + 1;
        if (next >= 12) world.setBlockToAir(pos);
        else world.setBlockState(pos, state.withProperty(BITES, next), 2);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote) return;
        int bites = state.getValue(BITES);
        if (bites > 0) world.setBlockState(pos, state.withProperty(BITES, bites - 1), 2);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && canBlockStay(world, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos,
            Block neighbor, BlockPos fromPos) {
        if (!canBlockStay(world, pos)) world.setBlockToAir(pos);
    }

    public boolean canBlockStay(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).getMaterial().isSolid();
    }

    @Override
    public int quantityDropped(Random random) { return 0; }

    public Item getItemDropped(IBlockState state, Random random, int fortune) { return Items.AIR; }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target,
            World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }
}
