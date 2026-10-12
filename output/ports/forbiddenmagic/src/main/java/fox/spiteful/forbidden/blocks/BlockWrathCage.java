package fox.spiteful.forbidden.blocks;

import java.util.Random;
import fox.spiteful.forbidden.Config;
import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.items.ForbiddenItems;
import fox.spiteful.forbidden.tiles.TileEntityWrathCage;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/** Forge 1.12.2 block interaction and crystal exchange for the original Wrath Cage. */
public class BlockWrathCage extends BlockContainer {
    public BlockWrathCage() {
        super(Material.IRON);
        setCreativeTab(Forbidden.tab);
        setHardness(5.0f);
        setResistance(2000.0f);
        setSoundType(SoundType.METAL);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) { return new TileEntityWrathCage(); }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) return false;
        ItemStack held = player.getHeldItem(hand);
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileEntityWrathCage)) return false;
        TileEntityWrathCage cage = (TileEntityWrathCage) tile;
        if (!held.isEmpty() && held.getItem() == ForbiddenItems.mobCrystal) {
            NBTTagCompound tag = held.getTagCompound();
            if (tag == null || !tag.hasKey("mob") || tag.getString("mob").isEmpty()) return false;
            if (!world.isRemote) {
                ItemStack old = ItemStack.EMPTY;
                if (cage.getSpawnerLogic().isMobSet()) {
                    old = new ItemStack(ForbiddenItems.mobCrystal);
                    old.setTagCompound(new NBTTagCompound());
                    old.getTagCompound().setString("mob", cage.getSpawnerLogic().getEntityNameToSpawn());
                }
                cage.getSpawnerLogic().setMobID(tag.getString("mob"));
                cage.getSpawnerLogic().mobIsSet(true);
                cage.checkAspect();
                // A mob crystal is unstackable; exchange instead of dropping a duplicate.
                if (!player.capabilities.isCreativeMode) player.setHeldItem(hand, old);
                else if (!old.isEmpty() && !player.inventory.addItemStackToInventory(old)) player.dropItem(old, false);
                cage.sync();
            }
            return true;
        }
        if (!held.isEmpty() && held.getItem() == ForbiddenItems.fork && Config.wrathCost > 0) {
            if (!world.isRemote) {
                cage.mode = (byte)((cage.mode + 1) % 3);
                cage.sync();
            }
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (!world.isRemote && tile instanceof TileEntityWrathCage) {
            TileEntityWrathCage cage = (TileEntityWrathCage) tile;
            if (cage.getSpawnerLogic().isMobSet() && ForbiddenItems.mobCrystal != null) {
                ItemStack crystal = new ItemStack(ForbiddenItems.mobCrystal);
                crystal.setTagCompound(new NBTTagCompound());
                crystal.getTagCompound().setString("mob", cage.getSpawnerLogic().getEntityNameToSpawn());
                EntityItem dropped = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5,
                        pos.getZ() + 0.5, crystal);
                dropped.setDefaultPickupDelay();
                world.spawnEntity(dropped);
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world,
                                  BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }
}
