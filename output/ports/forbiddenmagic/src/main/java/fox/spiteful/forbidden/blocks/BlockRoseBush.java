package fox.spiteful.forbidden.blocks;
import net.minecraft.util.math.BlockPos;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fox.spiteful.forbidden.Forbidden;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

import java.util.Random;

public class BlockRoseBush extends BlockBush implements IGrowable
{

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite top;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite bottom;

    public BlockRoseBush()
    {
        super(Material.PLANTS);
        this.setHardness(0.0F);
        this.setSoundType(net.minecraft.block.SoundType.PLANT);
        this.setTranslationKey("blackRoseBush");
        this.setCreativeTab(Forbidden.tab);
        this.setTickRandomly(true);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 1;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        // Both halves use getBoundingBox.
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return super.canPlaceBlockAt(world, new BlockPos(x,y,z)) && world.isAirBlock(new BlockPos(x, y + 1, z));
    }

    /**
     * checks if the block can stay, if not drop as item
     */
    protected void checkAndDropBlock(World world, int x, int y, int z)
    {
        int meta = world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));
        if(meta == 0){
            if(world.getBlockState(new BlockPos(x, y - 1, z)).getBlock() != this){
                world.setBlockToAir(new BlockPos(x, y, z));
            }
        }
        else if(meta == 1){
            if(world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() != this){
                spawnAsEntity(world,new BlockPos(x,y,z),new ItemStack(this));
                world.setBlockToAir(new BlockPos(x, y, z));
            }
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        if (world.getBlockState(new BlockPos(x, y, z)).getBlock() != this) return super.canBlockStay(world, new BlockPos(x,y,z), world.getBlockState(new BlockPos(x,y,z))); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        if(world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z))) == 0)
            return world.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == this;
        Block block = world.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
        return block.canSustainPlant(world.getBlockState(new net.minecraft.util.math.BlockPos(x, y - 1, z)), world, new net.minecraft.util.math.BlockPos(x, y - 1, z), EnumFacing.UP, this);
    }

    public Item getItemDropped(int metadata, Random rand, int fortune)
    {
        if (metadata != 1)
        {
            return null;
        }
        else
        {
            return Item.getItemFromBlock(this);
        }
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(int side, int meta)
    {
        if(meta == 1)
            return bottom;
        else
            return top;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
        world.setBlockState(new BlockPos(x, y, z), this.getStateFromMeta(1), 2);
        world.setBlockState(new BlockPos(x, y + 1, z), this.getStateFromMeta(0), 2);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(TextureMap register)
    {
        this.top = register.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:umbral_rose_top"));
        this.bottom = register.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:umbral_rose_bottom"));
    }

    @Override
    /* Whether the plant isn't fully grown?  Who knows?  Aside from Mojang. */
    public boolean canGrow(World world, BlockPos pos, net.minecraft.block.state.IBlockState state, boolean isClient)
    {
        return true;
    }

    @Override
    /* Whether it can accept bonemeal?  Who knows?  Aside from Mojang. */
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, net.minecraft.block.state.IBlockState state)
    {
        return true;
    }

    @Override
    /* What happens when the block is bonemealed?  Maybe?  Fuck Obfuscation. */
    public void grow(World world, Random rand, BlockPos pos, net.minecraft.block.state.IBlockState state)
    {
        spreadFlowers(world, pos.getX(), pos.getY(), pos.getZ(), rand);
    }

    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote && rand.nextInt(10) <= 2)
            spreadFlowers(world, x, y, z, rand);
    }

    public void spreadFlowers(World world, int x, int y, int z, Random rand) {
        int i1;
        int j1;
        int k1;

        i1 = x + rand.nextInt(3) - 1;
        j1 = y + rand.nextInt(2) - rand.nextInt(2);
        k1 = z + rand.nextInt(3) - 1;

        for (int l1 = 0; l1 < 8; ++l1) {
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
            world.setBlockState(new BlockPos(i1, j1, k1), ForbiddenBlocks.blackFlower.getStateFromMeta(0), 2);
        }
    }


    @Override public boolean canPlaceBlockAt(World world, BlockPos pos) { return canPlaceBlockAt(world,pos.getX(),pos.getY(),pos.getZ()); }
    @Override public boolean canBlockStay(World world, BlockPos pos, net.minecraft.block.state.IBlockState state) { return canBlockStay(world,pos.getX(),pos.getY(),pos.getZ()); }
    @Override public void updateTick(World world, BlockPos pos, net.minecraft.block.state.IBlockState state, Random rand) { updateTick(world,pos.getX(),pos.getY(),pos.getZ(),rand); }
    @Override public Item getItemDropped(net.minecraft.block.state.IBlockState state, Random rand, int fortune) { return getItemDropped(getMetaFromState(state),rand,fortune); }
    @Override public net.minecraft.util.math.AxisAlignedBB getBoundingBox(net.minecraft.block.state.IBlockState state, IBlockAccess world, BlockPos pos) { return FULL_BLOCK_AABB; }
    @Override public void onBlockPlacedBy(World world, BlockPos pos, net.minecraft.block.state.IBlockState state, EntityLivingBase player, ItemStack stack) { onBlockPlacedBy(world,pos.getX(),pos.getY(),pos.getZ(),player,stack); }
    private static final net.minecraft.block.properties.PropertyInteger HALF_META=net.minecraft.block.properties.PropertyInteger.create("half_meta",0,1);
    @Override protected net.minecraft.block.state.BlockStateContainer createBlockState() { return new net.minecraft.block.state.BlockStateContainer(this,HALF_META); }
    @Override public net.minecraft.block.state.IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(HALF_META,meta&1); }
    @Override public int getMetaFromState(net.minecraft.block.state.IBlockState state) { return state.getValue(HALF_META); }
    @Override public void neighborChanged(net.minecraft.block.state.IBlockState state, World world, BlockPos pos, Block block, BlockPos from) { checkAndDropBlock(world,pos.getX(),pos.getY(),pos.getZ()); }
}