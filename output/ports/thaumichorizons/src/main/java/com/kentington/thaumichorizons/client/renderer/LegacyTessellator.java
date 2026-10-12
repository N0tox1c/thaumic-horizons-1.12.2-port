package com.kentington.thaumichorizons.client.renderer;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

/** Translates the original per-vertex attributes to an explicit 1.12 buffer format. */
public final class LegacyTessellator {
    private static final LegacyTessellator INSTANCE = new LegacyTessellator();
    private final List<double[]> vertices = new ArrayList<>();
    private boolean drawing, texture, color, light, normal;
    private int mode, brightness;
    private int red=255, green=255, blue=255, alpha=255;
    private double u, v, offsetX, offsetY, offsetZ;
    private float normalX, normalY, normalZ;

    public static LegacyTessellator getInstance() { return INSTANCE; }
    public void startDrawingQuads() { startDrawing(GL11.GL_QUADS); }
    public void startDrawing(int mode) {
        if (drawing) throw new IllegalStateException("Already tessellating");
        drawing=true; this.mode=mode; vertices.clear();
        texture=color=light=normal=false;
        red=green=blue=alpha=255;
        brightness=0; u=v=0; normalX=normalY=normalZ=0;
    }
    public void setColorRGBA(int r,int g,int b,int a) {
        red=clamp(r); green=clamp(g); blue=clamp(b); alpha=clamp(a); color=true;
    }
    private static int clamp(int value) { return Math.max(0,Math.min(255,value)); }
    public void setColorRGBA_F(float r,float g,float b,float a) {
        setColorRGBA((int)(r*255),(int)(g*255),(int)(b*255),(int)(a*255));
    }
    public void setColorOpaque_F(float r,float g,float b) { setColorRGBA_F(r,g,b,1); }
    public void setColorOpaque(int r,int g,int b) { setColorRGBA(r,g,b,255); }
    public void setColorOpaque_I(int rgb) { setColorRGBA_I(rgb,255); }
    public void setColorRGBA_I(int rgb,int a) { setColorRGBA(rgb>>16&255,rgb>>8&255,rgb&255,a); }
    public void setBrightness(int value) { brightness=value; light=true; }
    public void setNormal(float x,float y,float z) { normalX=x;normalY=y;normalZ=z;normal=true; }
    public void setTextureUV(double u,double v) { this.u=u;this.v=v;texture=true; }
    public void setTranslation(double x,double y,double z) { offsetX=x;offsetY=y;offsetZ=z; }
    public void addTranslation(float x,float y,float z) { offsetX+=x;offsetY+=y;offsetZ+=z; }
    public void addVertexWithUV(double x,double y,double z,double u,double v) {
        setTextureUV(u,v); addVertex(x,y,z);
    }
    public void addVertex(double x,double y,double z) {
        if (!drawing) throw new IllegalStateException("Not tessellating");
        vertices.add(new double[]{x+offsetX,y+offsetY,z+offsetZ,u,v,
                red,green,blue,alpha,brightness&65535,brightness>>>16,normalX,normalY,normalZ});
    }
    public int draw() {
        BufferBuilder buffer=Tessellator.getInstance().getBuffer();
        int bytes=writeTo(buffer);
        Tessellator.getInstance().draw();
        return bytes;
    }
    /** Emits the real target buffer, also usable by renderer contract tests without OpenGL. */
    public int writeTo(BufferBuilder buffer) {
        if (!drawing) throw new IllegalStateException("Not tessellating");
        VertexFormat format=new VertexFormat().addElement(DefaultVertexFormats.POSITION_3F);
        if (texture) format.addElement(DefaultVertexFormats.TEX_2F);
        if (color) format.addElement(DefaultVertexFormats.COLOR_4UB);
        if (light) format.addElement(DefaultVertexFormats.TEX_2S);
        if (normal) format.addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
        buffer.begin(mode,format);
        for (double[] vertex:vertices) {
            buffer.pos(vertex[0],vertex[1],vertex[2]);
            if (texture) buffer.tex(vertex[3],vertex[4]);
            if (color) buffer.color((int)vertex[5],(int)vertex[6],(int)vertex[7],(int)vertex[8]);
            // Native SHORT UV storage writes the second argument first. Keep
            // the original packed brightness: low block light, high sky light.
            if (light) buffer.lightmap((int)vertex[10],(int)vertex[9]);
            if (normal) buffer.normal((float)vertex[11],(float)vertex[12],(float)vertex[13]);
            buffer.endVertex();
        }
        int bytes=vertices.size()*format.getSize();
        vertices.clear(); drawing=false;
        return bytes;
    }
}
