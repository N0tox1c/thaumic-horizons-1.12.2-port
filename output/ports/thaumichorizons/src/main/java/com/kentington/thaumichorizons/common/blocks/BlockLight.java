//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileLight;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.config.Config;

public class BlockLight extends LegacyBlockContainer {

    public TextureAtlasSprite blankIcon;

    public BlockLight() {
        super(Config.airyMaterial);
        this.setTranslationKey("ThaumicHorizons_light");
        this.setStepSound(new net.minecraft.block.SoundType(0f,1f,net.minecraft.init.SoundEvents.BLOCK_CLOTH_BREAK,net.minecraft.init.SoundEvents.BLOCK_CLOTH_STEP,net.minecraft.init.SoundEvents.BLOCK_CLOTH_PLACE,net.minecraft.init.SoundEvents.BLOCK_CLOTH_HIT,net.minecraft.init.SoundEvents.BLOCK_CLOTH_FALL));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap ir) {
        this.blankIcon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumcraft:blank", "blocks"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(final int side, final int meta) {
        return this.blankIcon;
    }

    public float getBlockHardness(final World world, final int x, final int y, final int z) {
        return 0.0f;
    }

    public float getExplosionResistance(final Entity par1Entity, final World world, final int x, final int y,
            final int z, final double explosionX, final double explosionY, final double explosionZ) {
        return 0.0f;
    }

    public int getLightValue(final IBlockAccess world, final int x, final int y, final int z) {
        return 14;
    }

    public void setBlockBoundsBasedOnState(final IBlockAccess ba, final int x, final int y, final int z) {
        this.setBlockBounds(0.3f, 0.3f, 0.3f, 0.7f, 0.7f, 0.7f);
    }

    public boolean getBlocksMovement(final IBlockAccess world, final int x, final int y, final int z) {
        return false;
    }

    public int getRenderType() {
        return ThaumicHorizons.blockLightRI;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public Item getItemDropped(final int par1, final Random par2Random, final int par3) {
        return Item.getItemById(0);
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileLight();
    }

    public TileEntity createNewTileEntity(final World var1, final int md) {
        return new TileLight();
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        return null;
    }

    @Override
    public net.minecraft.item.Item getItemDropped(net.minecraft.block.state.IBlockState state,Random random,int fortune) {
        return getItemDropped(getMetaFromState(state),random,fortune);
    }

    @Override
    public float getBlockHardness(net.minecraft.block.state.IBlockState state,World world,net.minecraft.util.math.BlockPos pos) {
        return getBlockHardness(world,pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public int getLightValue(net.minecraft.block.state.IBlockState state,IBlockAccess world,net.minecraft.util.math.BlockPos pos) {
        return getLightValue(world,pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean isPassable(IBlockAccess world,net.minecraft.util.math.BlockPos pos) {
        return getBlocksMovement(world,pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public net.minecraft.util.math.AxisAlignedBB getCollisionBoundingBox(net.minecraft.block.state.IBlockState state,IBlockAccess world,net.minecraft.util.math.BlockPos pos) {
        if (!(world instanceof World)) return getBoundingBox(state,world,pos);
        net.minecraft.util.math.AxisAlignedBB box=getCollisionBoundingBoxFromPool((World)world,pos.getX(), pos.getY(), pos.getZ());
        return box==null ? null : box.offset(-pos.getX(),-pos.getY(),-pos.getZ());
    }
}
