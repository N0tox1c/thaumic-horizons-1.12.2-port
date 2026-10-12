//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileVisDynamo;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.items.wands.ItemWandCasting;

public class BlockVisDynamo extends LegacyBlockContainer {

    TextureAtlasSprite icon;

    public BlockVisDynamo(final Material p_i45386_1_) {
        super(p_i45386_1_);
        this.setHardness(0.7f);
        this.setResistance(1.0f);
        this.setLightLevel(0.5f);
        this.setTranslationKey("ThaumicHorizons_visDynamo");
        this.setBlockTextureName("ThaumicHorizons:visDynamo");
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
        return this.createTileEntity(p_149915_1_, p_149915_2_);
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileVisDynamo();
    }

    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
            final int p_149727_6_, final float p_149727_7_, final float p_149727_8_, final float p_149727_9_) {
        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemWandCasting) {
            return false;
        }
        player.openGui(ThaumicHorizons.instance, 1, world, x, y, z);
        return true;
    }

    public void breakBlock(final World p_149749_1_, final int p_149749_2_, final int p_149749_3_, final int p_149749_4_,
            final Block p_149749_5_, final int p_149749_6_) {
        ((TileVisDynamo) p_149749_1_.getTileEntity(new BlockPos(p_149749_2_, p_149749_3_, p_149749_4_))).killMe();
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return ThaumicHorizons.blockVisDynamoRI;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("gold_block", "blocks"));
    }

    public TextureAtlasSprite getIcon(final int par1, final int par2) {
        return this.icon;
    }

    @Override
    public boolean onBlockActivated(World world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState state, EntityPlayer player, net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing face, float hitX, float hitY, float hitZ) {
        return onBlockActivated(world,pos.getX(), pos.getY(), pos.getZ(),player,face.getIndex(),hitX,hitY,hitZ);
    }

    @Override
    public void breakBlock(World world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState state) {
        breakBlock(world,pos.getX(), pos.getY(), pos.getZ(),state.getBlock(),getMetaFromState(state));
    }
}
