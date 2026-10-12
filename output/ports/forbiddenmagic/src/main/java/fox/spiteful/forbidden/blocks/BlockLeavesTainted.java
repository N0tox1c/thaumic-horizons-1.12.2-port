package fox.spiteful.forbidden.blocks;
import net.minecraft.util.math.BlockPos;


import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.items.ForbiddenItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;

import java.lang.reflect.Field;
import java.util.Random;

public class BlockLeavesTainted extends BlockLeaves
{
    int[] decayThingy;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite opaqueIcon;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite transparentIcon;

    public BlockLeavesTainted()
    {
        super();
        setCreativeTab(Forbidden.tab);
        setLightOpacity(0);
        setHardness(0.2F);
        setSoundType(ConfigBlocks.blockTaint.getSoundType());
        try {
            Field mat = ReflectionHelper.findField(Block.class, "blockMaterial", "field_149764_J");
            mat.set(this, Config.taintMaterial);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Item getItemDropped(int i, Random random, int j)
    {
        return Item.getItemFromBlock(ForbiddenBlocks.taintSapling);
    }

    /**
    * Chance of sapling dropping
     */
    protected int func_150123_b(int p_150123_1_)
    {
        return 60;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess iba, int x, int y, int z, int side)
    {
        return !Blocks.LEAVES.getDefaultState().isOpaqueCube() || super.shouldSideBeRendered(getDefaultState(), iba, new BlockPos(x,y,z), net.minecraft.util.EnumFacing.byIndex(side));
    }
    public boolean isOpaqueCube()
    {
        return Blocks.LEAVES.getDefaultState().isOpaqueCube();
    }
    public TextureAtlasSprite getIcon(int side, int meta)
    {
        return !Blocks.LEAVES.getDefaultState().isOpaqueCube() ? transparentIcon : opaqueIcon;
    }
    public String[] func_150125_e()
    {
        return new String[0];
    }
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(TextureMap register)
    {
        opaqueIcon = register.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:taint_leaves_opaque"));
        transparentIcon = register.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:taint_leaves"));
    }

    protected void func_150124_c(World world, int x, int y, int z, int meta, int wat)
    {
        if (world.rand.nextInt(20) == 0)
        {
            spawnAsEntity(world,new BlockPos(x,y,z),new ItemStack(ForbiddenItems.taintFruit));
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (!world.isRemote)
        {
            int l = world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));

            if ((l & 8) != 0 && (l & 4) == 0)
            {
                byte b0 = 4;
                int i1 = b0 + 1;
                byte b1 = 32;
                int j1 = b1 * b1;
                int k1 = b1 / 2;

                if (this.decayThingy == null)
                {
                    this.decayThingy = new int[b1 * b1 * b1];
                }

                int l1;

                if (world.isAreaLoaded(new net.minecraft.util.math.BlockPos(x - i1, y - i1, z - i1), new net.minecraft.util.math.BlockPos(x + i1, y + i1, z + i1)))
                {
                    int i2;
                    int j2;

                    for (l1 = -b0; l1 <= b0; ++l1)
                    {
                        for (i2 = -b0; i2 <= b0; ++i2)
                        {
                            for (j2 = -b0; j2 <= b0; ++j2)
                            {
                                Block block = world.getBlockState(new BlockPos(x + l1, y + i2, z + j2)).getBlock();

                                if (block != ForbiddenBlocks.taintLog)
                                {
                                    if (block.isLeaves(world.getBlockState(new net.minecraft.util.math.BlockPos(x + l1, y + i2, z + j2)), world, new net.minecraft.util.math.BlockPos(x + l1, y + i2, z + j2)))
                                    {
                                        this.decayThingy[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                    }
                                    else
                                    {
                                        this.decayThingy[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                    }
                                }
                                else
                                {
                                    this.decayThingy[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                }
                            }
                        }
                    }

                    for (l1 = 1; l1 <= 4; ++l1)
                    {
                        for (i2 = -b0; i2 <= b0; ++i2)
                        {
                            for (j2 = -b0; j2 <= b0; ++j2)
                            {
                                for (int k2 = -b0; k2 <= b0; ++k2)
                                {
                                    if (this.decayThingy[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == l1 - 1)
                                    {
                                        if (this.decayThingy[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
                                        {
                                            this.decayThingy[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.decayThingy[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
                                        {
                                            this.decayThingy[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.decayThingy[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2)
                                        {
                                            this.decayThingy[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.decayThingy[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2)
                                        {
                                            this.decayThingy[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.decayThingy[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] == -2)
                                        {
                                            this.decayThingy[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] = l1;
                                        }

                                        if (this.decayThingy[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2)
                                        {
                                            this.decayThingy[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] = l1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                l1 = this.decayThingy[k1 * j1 + k1 * b1 + k1];

                if (l1 >= 0)
                {
                    world.setBlockState(new net.minecraft.util.math.BlockPos(x, y, z), world.getBlockState(new net.minecraft.util.math.BlockPos(x, y, z)).getBlock().getStateFromMeta(l & -9), 4);
                }
                else
                {
                    this.removeLeaves(world, x, y, z);
                }
            }
        }
    }

    private void removeLeaves(World world, int x, int y, int z)
    {
        this.dropBlockAsItem(world,new BlockPos(x,y,z),world.getBlockState(new BlockPos(x,y,z)),0);
        world.setBlockToAir(new BlockPos(x, y, z));
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return 0xFFFFFF;
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int metadata)
    {
        return 0xFFFFFF;
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return 0xFFFFFF;
    }

    public boolean isLeaves(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    @Override public void updateTick(World world, BlockPos pos, net.minecraft.block.state.IBlockState state, Random rand) { updateTick(world,pos.getX(),pos.getY(),pos.getZ(),rand); }
    @Override public Item getItemDropped(net.minecraft.block.state.IBlockState state, Random rand, int fortune) { return getItemDropped(getMetaFromState(state),rand,fortune); }
    @Override public net.minecraft.block.BlockPlanks.EnumType getWoodType(int meta) { return net.minecraft.block.BlockPlanks.EnumType.OAK; }
    @Override protected int getSaplingDropChance(net.minecraft.block.state.IBlockState state) { return 60; }
    @Override protected void dropApple(World world, BlockPos pos, net.minecraft.block.state.IBlockState state, int chance) { func_150124_c(world,pos.getX(),pos.getY(),pos.getZ(),getMetaFromState(state),chance); }
    @Override public boolean isOpaqueCube(net.minecraft.block.state.IBlockState state) { return isOpaqueCube(); }
    @Override public boolean isLeaves(net.minecraft.block.state.IBlockState state, IBlockAccess world, BlockPos pos) { return isLeaves(world,pos.getX(),pos.getY(),pos.getZ()); }
    @Override protected net.minecraft.block.state.BlockStateContainer createBlockState() { return new net.minecraft.block.state.BlockStateContainer(this,DECAYABLE,CHECK_DECAY); }
    @Override public net.minecraft.block.state.IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(DECAYABLE,(meta&4)==0).withProperty(CHECK_DECAY,(meta&8)!=0); }
    @Override public int getMetaFromState(net.minecraft.block.state.IBlockState state) { return (state.getValue(DECAYABLE)?0:4)|(state.getValue(CHECK_DECAY)?8:0); }
    @Override public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) { return true; }
    @Override public java.util.List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) { return java.util.Collections.singletonList(new ItemStack(this,1,0)); }
}