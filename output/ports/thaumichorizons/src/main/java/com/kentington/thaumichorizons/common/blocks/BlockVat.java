//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileVat;
import com.kentington.thaumichorizons.common.tiles.TileVatConnector;
import com.kentington.thaumichorizons.common.tiles.TileVatSlave;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.TileThaumcraft;

public class BlockVat extends LegacyBlockContainer {
    // Preserve the 1.7.10 component metadata (0,4,5,6,7,10) in chunk saves.
    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 10);


    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconGlassTL;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconGlassT;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconGlassTR;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconGlassBL;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconGlassB;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconGlassBR;

    public BlockVat() {
        super(Material.WOOD);
        this.setHardness(3.0f);
        this.setResistance(15.0f);
        this.setLightLevel(8.0F / 15.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, 0));
        this.setTranslationKey("ThaumicHorizons_vat");
    }

    public BlockVat(final Material m) {
        super(m);
        this.setHardness(3.0f);
        this.setResistance(15.0f);
        this.setLightLevel(8.0F / 15.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, 0));
        this.setTranslationKey("ThaumicHorizons_vatSolid");
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new net.minecraftforge.common.property.ExtendedBlockState(this, new net.minecraft.block.properties.IProperty[]{TYPE}, new net.minecraftforge.common.property.IUnlistedProperty[]{com.kentington.thaumichorizons.common.lib.BlockRenderContext.WORLD, com.kentington.thaumichorizons.common.lib.BlockRenderContext.POSITION});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        // Vanilla metadata has 4 bits. All original vat variants are <= 10.
        return getDefaultState().withProperty(TYPE, meta >= 0 && meta <= 10 ? meta : 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (meta == 7) return new TileVat();
        if (meta == 4) return new TileVatConnector();
        return new TileVatSlave();
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return createNewTileEntity(world, getMetaFromState(state));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileVat) {
                ((TileVat) tile).killMe();
            } else if (tile instanceof TileVatSlave) {
                ((TileVatSlave) tile).killMyBoss(getMetaFromState(state));
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand, EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileVat) && !(tile instanceof TileVatSlave)) return false;
        if (world.isRemote) return true; // server owns teleport, NBT and inventory changes
        if (tile instanceof TileVat) return ((TileVat) tile).activate(player, true);
        return ((TileVatSlave) tile).activate(player);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        // The native baked adapter invokes the original multipart renderer.
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.TRANSLUCENT; }

    @SideOnly(Side.CLIENT)
    public void registerVatSprites(TextureMap atlas) {
        iconGlassTL = atlas.registerSprite(new ResourceLocation("thaumichorizons", "blocks/vattopleft"));
        iconGlassT = atlas.registerSprite(new ResourceLocation("thaumichorizons", "blocks/vattop"));
        iconGlassTR = atlas.registerSprite(new ResourceLocation("thaumichorizons", "blocks/vattopright"));
        iconGlassBL = atlas.registerSprite(new ResourceLocation("thaumichorizons", "blocks/vatbottomleft"));
        iconGlassB = atlas.registerSprite(new ResourceLocation("thaumichorizons", "blocks/vatbottom"));
        iconGlassBR = atlas.registerSprite(new ResourceLocation("thaumichorizons", "blocks/vatbottomright"));
    }

    @Override
    public net.minecraft.block.state.IBlockState getExtendedState(net.minecraft.block.state.IBlockState state, net.minecraft.world.IBlockAccess world, net.minecraft.util.math.BlockPos pos) {
        return ((net.minecraftforge.common.property.IExtendedBlockState)state)
                .withProperty(com.kentington.thaumichorizons.common.lib.BlockRenderContext.WORLD,world).withProperty(com.kentington.thaumichorizons.common.lib.BlockRenderContext.POSITION,pos);
    }

}
