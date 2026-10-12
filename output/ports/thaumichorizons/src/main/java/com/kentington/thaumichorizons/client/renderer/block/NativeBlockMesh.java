package com.kentington.thaumichorizons.client.renderer.block;

import java.util.*;
import org.lwjgl.util.vector.Vector3f;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

/** Emits native baked quads from the original renderer's bounds and face textures. */
public final class NativeBlockMesh {
    private final List<BakedQuad> quads=new ArrayList<>();
    private Vector3f min=new Vector3f(0,0,0), max=new Vector3f(16,16,16);
    public boolean enableAO;
    private int color=0xffffffff;
    public void setRenderBounds(double x1,double y1,double z1,double x2,double y2,double z2) {
        min=new Vector3f((float)x1*16,(float)y1*16,(float)z1*16);
        max=new Vector3f((float)x2*16,(float)y2*16,(float)z2*16);
    }
    public void setColor(int r,int g,int b,int a) { color=(a<<24)|(b<<16)|(g<<8)|r; }
    private void face(EnumFacing side,TextureAtlasSprite texture) {
        if(texture==null) throw new IllegalStateException("Unstitched texture for "+side);
        float[] uv;
        switch(side.getAxis()) {
            case X: uv=new float[]{min.z,16-max.y,max.z,16-min.y}; break;
            case Y: uv=new float[]{min.x,min.z,max.x,max.z}; break;
            default: uv=new float[]{min.x,16-max.y,max.x,16-min.y};
        }
        BlockPartFace part=new BlockPartFace(null,-1,"",new BlockFaceUV(uv,0));
        BakedQuad baked=new FaceBakery().makeBakedQuad(min,max,part,texture,side,ModelRotation.X0_Y0,null,false,true);
        int[] data=baked.getVertexData().clone(); int stride=baked.getFormat().getIntegerSize();
        for(int i=0;i<4;i++) data[i*stride+3]=color;
        quads.add(new BakedQuad(data,-1,side,texture,true,baked.getFormat()));
    }
    public void renderFaceXNeg(Block b,double x,double y,double z,TextureAtlasSprite t) { face(EnumFacing.WEST,t); }
    public void renderFaceXPos(Block b,double x,double y,double z,TextureAtlasSprite t) { face(EnumFacing.EAST,t); }
    public void renderFaceYNeg(Block b,double x,double y,double z,TextureAtlasSprite t) { face(EnumFacing.DOWN,t); }
    public void renderFaceYPos(Block b,double x,double y,double z,TextureAtlasSprite t) { face(EnumFacing.UP,t); }
    public void renderFaceZNeg(Block b,double x,double y,double z,TextureAtlasSprite t) { face(EnumFacing.NORTH,t); }
    public void renderFaceZPos(Block b,double x,double y,double z,TextureAtlasSprite t) { face(EnumFacing.SOUTH,t); }
    public List<BakedQuad> quads() { return Collections.unmodifiableList(quads); }
    public void drawInventory() {
        if(quads.isEmpty()) return;
        Tessellator tess=Tessellator.getInstance(); BufferBuilder buffer=tess.getBuffer();
        buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.ITEM);
        for(BakedQuad quad:quads) buffer.addVertexData(quad.getVertexData());
        tess.draw();
    }
}
