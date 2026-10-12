//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.util.math.BlockPos;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileSoulJar;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSoulJar extends LegacyBlockContainer {

    public TextureAtlasSprite iconJarBottom;
    public TextureAtlasSprite iconJarSide;
    public TextureAtlasSprite iconJarTop;

    public BlockSoulJar() {
        super(Material.GLASS);
        this.setHardness(0.3f);
        this.setResistance(1.0f);
        this.setTranslationKey("ThaumicHorizons_soulJar");
        this.setBlockTextureName("ThaumicHorizons:soulJar");
        this.setCreativeTab(ThaumicHorizons.tabTH);
        this.setLightLevel(0.66f);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap ir) {
        this.iconJarSide = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumcraft:jar_side", "blocks"));
        this.iconJarTop = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumcraft:jar_top", "blocks"));
        this.iconJarBottom = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumcraft:jar_bottom", "blocks"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(final int side, final int meta) {
        return (side == 1) ? this.iconJarTop : ((side == 0) ? this.iconJarBottom : this.iconJarSide);
    }

    public int getRenderBlockPass() {
        return 1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return ThaumicHorizons.blockJarRI;
    }

    public void setBlockBoundsBasedOnState(final IBlockAccess world, final int i, final int j, final int k) {
        this.setBlockBounds(0.1875f, 0.0f, 0.1875f, 0.8125f, 0.75f, 0.8125f);
        super.setBlockBoundsBasedOnState(world, i, j, k);
    }

    public void addCollisionBoxesToList(final World world, final int i, final int j, final int k,
            final AxisAlignedBB axisalignedbb, final List arraylist, final Entity par7Entity) {
        addCollisionBoxToList(new BlockPos(i, j, k), axisalignedbb, arraylist, FULL_BLOCK_AABB);
    }

    public TileEntity createNewTileEntity(final World world, final int md) {
        return this.createTileEntity(world, md);
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileSoulJar();
    }

    public void onBlockHarvested(final World par1World, final int par2, final int par3, final int par4, final int par5,
            final EntityPlayer par6EntityPlayer) {
        BlockPos pos = new BlockPos(par2, par3, par4);
        // Survival harvest receives the pre-removal tile in harvestBlock; creative does not.
        if (!par1World.isRemote && par6EntityPlayer.capabilities.isCreativeMode) {
            ItemStack drop = jarDrop(par1World.getTileEntity(pos));
            if (!drop.isEmpty()) spawnAsEntity(par1World, pos, drop);
        }
        super.onBlockHarvested(par1World, pos, getStateFromMeta(par5), par6EntityPlayer);
    }

    public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata,
            final int fortune) {
        final ArrayList<ItemStack> drops = new ArrayList<>();
        final TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileSoulJar) {
            final ItemStack drop = new ItemStack(this);
            drop.setTagCompound(((TileSoulJar) te).jarTag.copy());
            drops.add(drop);
        }
        return drops;
    }

    @Override
    public net.minecraft.block.state.IBlockState getExtendedState(net.minecraft.block.state.IBlockState state, net.minecraft.world.IBlockAccess world, net.minecraft.util.math.BlockPos pos) {
        return ((net.minecraftforge.common.property.IExtendedBlockState)state)
                .withProperty(com.kentington.thaumichorizons.common.lib.BlockRenderContext.WORLD,world).withProperty(com.kentington.thaumichorizons.common.lib.BlockRenderContext.POSITION,pos);
    }

    @Override
    protected net.minecraft.block.state.BlockStateContainer createBlockState() {
        return new net.minecraftforge.common.property.ExtendedBlockState(this,new net.minecraft.block.properties.IProperty[]{META},
                new net.minecraftforge.common.property.IUnlistedProperty[]{com.kentington.thaumichorizons.common.lib.BlockRenderContext.WORLD,com.kentington.thaumichorizons.common.lib.BlockRenderContext.POSITION});
    }
    @Override
    public net.minecraft.util.EnumBlockRenderType getRenderType(net.minecraft.block.state.IBlockState state) {
        return net.minecraft.util.EnumBlockRenderType.MODEL;
    }

    @Override
    public void onBlockHarvested(World world,net.minecraft.util.math.BlockPos pos,net.minecraft.block.state.IBlockState state,EntityPlayer player) {
        onBlockHarvested(world,pos.getX(), pos.getY(), pos.getZ(),getMetaFromState(state),player);
    }

    private ItemStack jarDrop(TileEntity tile) {
        if (!(tile instanceof TileSoulJar) || !((TileSoulJar) tile).drop) return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(((TileSoulJar) tile).jarTag.copy());
        return stack;
    }

    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos,
            net.minecraft.block.state.IBlockState state, int fortune) {
        ItemStack drop = jarDrop(world.getTileEntity(pos));
        if (!drop.isEmpty()) drops.add(drop);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, net.minecraft.block.state.IBlockState state,
            TileEntity tile, ItemStack tool) {
        player.addStat(net.minecraft.stats.StatList.getBlockStats(this));
        player.addExhaustion(0.005F);
        if (world.isRemote) return;
        net.minecraft.util.NonNullList<ItemStack> drops = net.minecraft.util.NonNullList.create();
        ItemStack drop = jarDrop(tile);
        if (!drop.isEmpty()) drops.add(drop);
        int fortune = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, tool);
        float chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(drops, world, pos, state, fortune, 1F, false, player);
        for (ItemStack stack : drops) if (world.rand.nextFloat() <= chance) spawnAsEntity(world, pos, stack);
    }

    @Override
    public void addCollisionBoxToList(net.minecraft.block.state.IBlockState state, World world, BlockPos pos,
            AxisAlignedBB box, List<AxisAlignedBB> boxes, Entity entity, boolean actualState) {
        addCollisionBoxToList(pos, box, boxes, FULL_BLOCK_AABB);
    }
}
