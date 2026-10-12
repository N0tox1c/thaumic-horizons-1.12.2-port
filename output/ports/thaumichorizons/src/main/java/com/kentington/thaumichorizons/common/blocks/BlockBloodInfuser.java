package com.kentington.thaumichorizons.common.blocks;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileBloodInfuser;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Original Blood Infuser block port. Original 10 inventory drops and GUI ID 5
 * retained; full TESR/model and Container GUI remain independent work items.
 */
public class BlockBloodInfuser extends LegacyBlockContainer {
    public BlockBloodInfuser() {
        super(Material.IRON);
        setHardness(1.0F);
        setResistance(1.0F);
        setTranslationKey("ThaumicHorizons_bloodInfuser");
        setCreativeTab(ThaumicHorizons.tabTH);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileBloodInfuser();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
            EntityPlayer player, EnumHand hand, EnumFacing facing,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(ThaumicHorizons.instance, 5, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (!world.isRemote && te instanceof TileBloodInfuser) {
            TileBloodInfuser tile = (TileBloodInfuser) te;
            dropInventoryItem(world, pos, tile.syringe);
            for (ItemStack stack : tile.output) dropInventoryItem(world, pos, stack);
            tile.clear(); // no duplicate item drops when chunk/tiles unload
        }
        super.breakBlock(world, pos, state);
    }

    private static void dropInventoryItem(World world, BlockPos pos, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        world.spawnEntity(new EntityItem(world, pos.getX() + 0.5,
                pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        // MODEL is an interim static render; keep the original special renderer sources.
        return EnumBlockRenderType.MODEL;
    }

}
