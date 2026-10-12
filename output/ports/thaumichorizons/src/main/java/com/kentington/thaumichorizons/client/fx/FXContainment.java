package com.kentington.thaumichorizons.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ITCParticle;
import thaumcraft.client.fx.ParticleEngine;

/** Sixteen-frame original containment texture, with balanced TC particle batch handling. */
public class FXContainment extends Particle implements ITCParticle {
    public static final ResourceLocation portaltex = new ResourceLocation("thaumcraft", "textures/misc/eldritch_portal.png");

    public FXContainment(World world,double x,double y,double z) {
        super(world,x,y,z,0,0,0);
        particleRed=particleGreen=particleBlue=1.0f;
        particleAlpha=0.5f;particleScale=1.0f;particleMaxAge=40;
        motionX=motionY=motionZ=0;particleGravity=0;canCollide=false;
    }
    @Override public void renderParticle(BufferBuilder buffer,Entity camera,float partialTicks,
                                         float rotX,float rotXZ,float rotZ,float rotYZ,float rotXY) {
        // ParticleEngine has a shared BufferBuilder open. Flush before switching texture.
        Tessellator tess=Tessellator.getInstance();
        tess.draw();
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
        try {
            Minecraft.getMinecraft().getTextureManager().bindTexture(portaltex);
            buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.POSITION_TEX_COLOR);
            int frame=(int)((System.nanoTime()/50000000L)%16);
            float u=frame/16f, uEnd=u+1f/16f, size=3f*particleScale;
            float x=(float)(prevPosX+(posX-prevPosX)*partialTicks-Particle.interpPosX);
            float y=(float)(prevPosY+(posY-prevPosY)*partialTicks-Particle.interpPosY);
            float z=(float)(prevPosZ+(posZ-prevPosZ)*partialTicks-Particle.interpPosZ);
            vertex(buffer,x-rotX*size-rotXY*size,y-rotXZ*size,z-rotZ*size-rotYZ*size,u,0);
            vertex(buffer,x-rotX*size+rotXY*size,y+rotXZ*size,z-rotZ*size+rotYZ*size,uEnd,0);
            vertex(buffer,x+rotX*size+rotXY*size,y+rotXZ*size,z+rotZ*size+rotYZ*size,uEnd,1);
            vertex(buffer,x+rotX*size-rotXY*size,y-rotXZ*size,z+rotZ*size-rotYZ*size,u,1);
            tess.draw();
        } finally {
            GL11.glDepthMask(false);
            GL11.glPopMatrix();
            // TC layer 3 uses alpha blending and its second particle atlas.
            Minecraft.getMinecraft().getTextureManager().bindTexture(ParticleEngine.particleTexture2);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        }
    }
    private void vertex(BufferBuilder b,double x,double y,double z,float u,float v){
        b.pos(x,y,z).tex(u,v).color(particleRed,particleGreen,particleBlue,0.1f).endVertex();
    }
    @Override public int getFXLayer(){return 0;}
    @Override public int getTCParticleLayer(){return 3;}
    @Override public void onUpdate(){
        if(particleAge++>=particleMaxAge){setExpired();return;}
        particleScale/=1.15f;
    }
}
