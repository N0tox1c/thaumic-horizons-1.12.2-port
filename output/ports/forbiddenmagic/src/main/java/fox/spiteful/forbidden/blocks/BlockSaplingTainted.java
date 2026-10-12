package fox.spiteful.forbidden.blocks;
import net.minecraft.util.math.BlockPos;


import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.WorldGenTaintedTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class BlockSaplingTainted extends BlockSapling
{
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite icon;

    public BlockSaplingTainted()
    {
        super();
        setCreativeTab(Forbidden.tab);
        setSoundType(ConfigBlocks.blockTaint.getSoundType());
        try {
            Field mat = ReflectionHelper.findField(Block.class, "blockMaterial", "field_149764_J");
            mat.set(this, Config.taintMaterial);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Grow the tree
     */
    public void func_149878_d(World world, int x, int y, int z, Random rand)
    {
        if(!world.isRemote)
        {
            int meta = world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));

            world.setBlockToAir(new BlockPos(x, y, z));
            WorldGenTaintedTree tree = new WorldGenTaintedTree(true);
            if (!tree.generate(world, rand, x, y, z))
            {
                world.setBlockState(new BlockPos(x, y, z), this.getStateFromMeta(meta), 2);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, net.minecraft.util.NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, 0));
    }

    public Item getItemDropped(int wut, Random random, int yeah)
    {
        return Item.getItemFromBlock(this);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(TextureMap register)
    {
        icon = register.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:sapling_taint"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(int side, int meta)
    {
        return icon;
    }

    @Override public Item getItemDropped(net.minecraft.block.state.IBlockState state, Random rand, int fortune) { return getItemDropped(getMetaFromState(state),rand,fortune); }
    @Override public void generateTree(World world, BlockPos pos, net.minecraft.block.state.IBlockState state, Random rand) { func_149878_d(world,pos.getX(),pos.getY(),pos.getZ(),rand); }
}