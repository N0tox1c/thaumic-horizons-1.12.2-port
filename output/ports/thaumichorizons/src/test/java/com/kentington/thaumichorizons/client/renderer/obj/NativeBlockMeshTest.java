package com.kentington.thaumichorizons.client.renderer.obj;

import com.kentington.thaumichorizons.client.renderer.block.NativeBlockMesh;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.junit.Test;
import static org.junit.Assert.*;

public class NativeBlockMeshTest {
    private TextureAtlasSprite texture() {
        TextureAtlasSprite texture=new TextureAtlasSprite("test") {};
        texture.setIconWidth(16); texture.setIconHeight(16); texture.initSprite(256,256,0,0,false);
        return texture;
    }
    @Test public void keepsJarBoundsAndAlphaInNativeQuad() {
        NativeBlockMesh mesh=new NativeBlockMesh();
        mesh.setRenderBounds(3/16D,0,3/16D,13/16D,12/16D,13/16D);
        mesh.setColor(255,255,255,192);
        mesh.renderFaceXNeg(null,120,45,-36,texture());
        BakedQuad quad=mesh.quads().get(0); int[] data=quad.getVertexData(); int stride=quad.getFormat().getIntegerSize();
        for(int i=0;i<4;i++) {
            assertEquals(3/16F,Float.intBitsToFloat(data[i*stride]),0);
            assertEquals(192,(data[i*stride+3]>>>24));
            float y=Float.intBitsToFloat(data[i*stride+1]); assertTrue(y==0||y==12/16F);
        }
    }
    @Test public void keepsEachFaceAndUpdatedBoundsSeparate() {
        NativeBlockMesh mesh=new NativeBlockMesh(); TextureAtlasSprite texture=texture();
        mesh.renderFaceYPos(null,0,0,0,texture);
        mesh.setRenderBounds(0,0,.25,1,1,1);
        mesh.renderFaceZNeg(null,0,0,0,texture);
        assertEquals(2,mesh.quads().size());
        assertEquals(net.minecraft.util.EnumFacing.UP,mesh.quads().get(0).getFace());
        int[] data=mesh.quads().get(1).getVertexData();int stride=mesh.quads().get(1).getFormat().getIntegerSize();
        for(int i=0;i<4;i++) assertEquals(.25F,Float.intBitsToFloat(data[i*stride+2]),0);
    }
}
