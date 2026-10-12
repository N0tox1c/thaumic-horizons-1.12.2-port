package fox.spiteful.forbidden.blocks;
import net.minecraft.util.math.BlockPos;


import java.util.List;
import java.util.Random;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBlackFlower extends BlockFlower {
    protected BlockBlackFlower() {
        super();
        float f = 0.2F;
        // Selection shape is provided by getBoundingBox.
        this.setCreativeTab(Forbidden.tab);
        setSoundType(net.minecraft.block.SoundType.PLANT);
        // Texture is assigned by the native model registry.
    }

    public void spreadFlowers(World world, int x, int y, int z, Random rand) {
        int l = 10;
        int i1;
        int j1;
        int k1;

        for (i1 = x - 2; i1 <= x + 2; ++i1) {
            for (j1 = z - 2; j1 <= z + 2; ++j1) {
                for (k1 = y - 1; k1 <= y + 1; ++k1) {
                    if (world.getBlockState(new BlockPos(i1, k1, j1)).getBlock() == this) {
                        --l;

                        if (l <= 0) {
                            return;
                        }
                    }
                }
            }
        }

        i1 = x + rand.nextInt(3) - 1;
        j1 = y + rand.nextInt(2) - rand.nextInt(2);
        k1 = z + rand.nextInt(3) - 1;

        for (int l1 = 0; l1 < 4; ++l1) {
            if (world.isAirBlock(new BlockPos(i1, j1, k1)) && this.canBlockStay(world, i1, j1, k1)) {
                x = i1;
                y = j1;
                z = k1;
            }

            i1 = x + rand.nextInt(3) - 1;
            j1 = y + rand.nextInt(2) - rand.nextInt(2);
            k1 = z + rand.nextInt(3) - 1;
        }

        if (world.isAirBlock(new BlockPos(i1, j1, k1)) && this.canBlockStay(world, i1, j1, k1)) {
            world.setBlockState(new BlockPos(i1, j1, k1), this.getStateFromMeta(0), 2);
        }
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(int p_149691_1_, int p_149691_2_) {
        return this.blockIcon;
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, new BlockPos(x,y,z)) && this.canBlockStay(world, x, y, z);
    }

    public boolean canBlockStay(World world, int x, int y, int z) {
        Block block = world.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
        return block.canSustainPlant(world.getBlockState(new net.minecraft.util.math.BlockPos(x, y - 1, z)), world, new net.minecraft.util.math.BlockPos(x, y - 1, z), EnumFacing.UP, this);
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, net.minecraft.util.NonNullList<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(TextureMap ir) {
        this.blockIcon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:flower_black"));
    }

    @Override public boolean canPlaceBlockAt(World world, BlockPos pos) { return canPlaceBlockAt(world,pos.getX(),pos.getY(),pos.getZ()); }
    @Override public boolean canBlockStay(World world, BlockPos pos, net.minecraft.block.state.IBlockState state) { return canBlockStay(world,pos.getX(),pos.getY(),pos.getZ()); }
    private TextureAtlasSprite blockIcon;
    @Override public EnumFlowerColor getBlockType() { return EnumFlowerColor.RED; }
    @Override public net.minecraft.util.math.AxisAlignedBB getBoundingBox(net.minecraft.block.state.IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos) { return new net.minecraft.util.math.AxisAlignedBB(0.3,0,0.3,0.7,0.4,0.7); }
}
