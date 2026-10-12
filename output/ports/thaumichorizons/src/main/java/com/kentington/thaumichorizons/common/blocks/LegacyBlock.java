package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import com.kentington.thaumichorizons.common.lib.BlockRenderContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Preserves original block metadata, bounds and texture selectors on native block states. */
public class LegacyBlock extends Block implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider {
    public static final PropertyInteger META=PropertyInteger.create("meta",0,15);
    private AxisAlignedBB defaultBounds=FULL_BLOCK_AABB;
    private final ThreadLocal<AxisAlignedBB> sampledBounds=new ThreadLocal<>();
    private String textureName;
    @SideOnly(Side.CLIENT)
    protected net.minecraft.client.renderer.texture.TextureAtlasSprite blockIcon;

    public LegacyBlock(Material material) { super(material); }
    @Override protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this,new net.minecraft.block.properties.IProperty[]{META},
                new IUnlistedProperty[]{BlockRenderContext.WORLD,BlockRenderContext.POSITION});
    }
    @Override public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(META,meta&15); }
    @Override public int getMetaFromState(IBlockState state) { return state.getValue(META); }
    @Override public IBlockState getExtendedState(IBlockState state,IBlockAccess world,BlockPos pos) {
        if (state instanceof IExtendedBlockState) return ((IExtendedBlockState)state)
                .withProperty(BlockRenderContext.WORLD,world).withProperty(BlockRenderContext.POSITION,pos);
        return state;
    }
    public void setBlockBounds(float x1,float y1,float z1,float x2,float y2,float z2) {
        AxisAlignedBB box=new AxisAlignedBB(x1,y1,z1,x2,y2,z2);
        if (sampledBounds.get()!=null) sampledBounds.set(box); else defaultBounds=box;
    }
    public void setBlockBoundsBasedOnState(IBlockAccess world,int x,int y,int z) {
        if (sampledBounds.get()==null) sampledBounds.set(defaultBounds);
    }
    @Override public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess world,BlockPos pos) {
        sampledBounds.set(defaultBounds);
        try {
            setBlockBoundsBasedOnState(world,pos.getX(),pos.getY(),pos.getZ());
            return sampledBounds.get();
        } finally { sampledBounds.remove(); }
    }
    public Block setBlockTextureName(String name) { textureName=name; return this; }
    public String getTextureName() { return textureName; }
    public void breakBlock(net.minecraft.world.World world,int x,int y,int z,Block block,int meta) {
        super.breakBlock(world,new BlockPos(x,y,z),block.getStateFromMeta(meta));
    }
    public void dropBlockAsItem(net.minecraft.world.World world,int x,int y,int z,int meta,int fortune) {
        dropBlockAsItem(world,new BlockPos(x,y,z),getStateFromMeta(meta),fortune);
    }
    public Block setStepSound(SoundType sound) { return setSoundType(sound); }
    public boolean isOpaqueCube() { return super.isOpaqueCube(getDefaultState()); }
    @Override public boolean isOpaqueCube(IBlockState state) { return isOpaqueCube(); }
    public boolean renderAsNormalBlock() { return super.isFullCube(getDefaultState()); }
    @Override public boolean isFullCube(IBlockState state) { return renderAsNormalBlock(); }
    public int getRenderType() { return 0; }
    @Override public net.minecraft.util.EnumBlockRenderType getRenderType(IBlockState state) {
        return getRenderType()<0 ? net.minecraft.util.EnumBlockRenderType.INVISIBLE : net.minecraft.util.EnumBlockRenderType.MODEL;
    }
    @SideOnly(Side.CLIENT) public int getRenderBlockPass() { return 0; }
    @SideOnly(Side.CLIENT) @Override public net.minecraft.util.BlockRenderLayer getRenderLayer() {
        return getRenderBlockPass()==1 ? net.minecraft.util.BlockRenderLayer.TRANSLUCENT : net.minecraft.util.BlockRenderLayer.SOLID;
    }
    @SideOnly(Side.CLIENT)
    @Override public void registerIcons(net.minecraft.client.renderer.texture.TextureMap atlas) { registerBlockIcons(atlas); }
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(net.minecraft.client.renderer.texture.TextureMap atlas) {
        if (textureName!=null) blockIcon=atlas.registerSprite(
                com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite(textureName,"blocks"));
    }
    @SideOnly(Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getIcon(int side,int meta) { return blockIcon; }
    @SideOnly(Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getIcon(IBlockAccess world,int x,int y,int z,int side) {
        return getIcon(side,getMetaFromState(world.getBlockState(new BlockPos(x,y,z))));
    }
}
