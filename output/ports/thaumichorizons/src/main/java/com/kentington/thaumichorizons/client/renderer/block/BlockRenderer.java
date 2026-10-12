package com.kentington.thaumichorizons.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockRenderer extends NativeBlockRenderer {
    public static final float W3=3/16F,W5=5/16F,W11=11/16F,W12=12/16F,W13=13/16F,W14=14/16F;
    protected static int setBrightness(IBlockAccess world,int x,int y,int z,Block block) {
        return world.getCombinedLight(new BlockPos(x,y,z),block.getDefaultState().getLightValue());
    }
    protected static void drawFaces(NativeBlockMesh mesh,Block block,TextureAtlasSprite bottom,TextureAtlasSprite top,
            TextureAtlasSprite north,TextureAtlasSprite south,TextureAtlasSprite west,TextureAtlasSprite east,boolean inventory) {
        mesh.renderFaceYNeg(block,0,0,0,bottom); mesh.renderFaceYPos(block,0,0,0,top);
        mesh.renderFaceZNeg(block,0,0,0,north); mesh.renderFaceZPos(block,0,0,0,south);
        mesh.renderFaceXNeg(block,0,0,0,west); mesh.renderFaceXPos(block,0,0,0,east);
    }
}
