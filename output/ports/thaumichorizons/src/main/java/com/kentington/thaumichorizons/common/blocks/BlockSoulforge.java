//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileSoulforge;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSoulforge extends LegacyBlockContainer {

    public TextureAtlasSprite iconLiquid;
    public TextureAtlasSprite iconJarBottom;
    public TextureAtlasSprite iconJarSide;
    public TextureAtlasSprite iconJarTop;

    public BlockSoulforge() {
        super(Material.GLASS);
        this.setHardness(0.7f);
        this.setResistance(1.0f);
        this.setTranslationKey("ThaumicHorizons_soulforge");
        this.setBlockTextureName("ThaumicHorizons:soulforge");
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public TileEntity createNewTileEntity(final World world, final int md) {
        return this.createTileEntity(world, md);
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileSoulforge();
    }

    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
            final int p_149727_6_, final float p_149727_7_, final float p_149727_8_, final float p_149727_9_) {
        final TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        return tile instanceof TileSoulforge && ((TileSoulforge) tile).activate(world, player);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap ir) {
        this.iconJarSide = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:jar_side", "blocks"));
        this.iconJarTop = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:jar_top", "blocks"));
        this.iconJarBottom = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:jar_bottom", "blocks"));
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
        return ThaumicHorizons.blockSoulforgeRI;
    }

    @Override
    public boolean onBlockActivated(World world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState state, EntityPlayer player, net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing face, float hitX, float hitY, float hitZ) {
        return onBlockActivated(world,pos.getX(), pos.getY(), pos.getZ(),player,face.getIndex(),hitX,hitY,hitZ);
    }
}
