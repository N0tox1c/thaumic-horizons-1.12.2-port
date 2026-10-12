package com.kentington.thaumichorizons.client.fx;

import com.kentington.thaumichorizons.client.renderer.obj.AdvancedModelLoader;
import com.kentington.thaumichorizons.client.renderer.obj.IModelCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ITCParticle;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;

/** Original hemispherical sonic ripple OBJ: keeps rotations, 15 frames and additive blending. */
public class FXSonic extends Particle implements ITCParticle {
    public float yaw,pitch;
    public static IModelCustom model;
    private static final ResourceLocation MODEL=new ResourceLocation("thaumcraft","textures/models/hemis.obj");
    public FXSonic(World world,double x,double y,double z,int age,int direction){
        super(world,x,y,z,0,0,0);
        this.particleRed=0;this.particleGreen=this.particleBlue=1;
        this.particleGravity=0;this.motionX=this.motionY=this.motionZ=0;
        this.particleMaxAge=Math.max(1,age)+rand.nextInt(Math.max(1,age/2));
        this.canCollide=false;this.setSize(0.01f,0.01f);this.particleScale=1;
        switch(direction){
            case 0:pitch=90;break;case 1:pitch=-90;break;
            case 2:yaw=180;break;case 3:yaw=0;break;
            case 4:yaw=90;break;case 5:yaw=270;break;
            default:break;
        }
        prevPosX=posX;prevPosY=posY;prevPosZ=posZ;
    }
    @Override public void renderParticle(BufferBuilder buffer,Entity camera,float tick,
                                         float rotX,float rotXZ,float rotZ,float rotYZ,float rotXY){
        Tessellator.getInstance().draw();
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE);
        float previousLightX=OpenGlHelper.lastBrightnessX, previousLightY=OpenGlHelper.lastBrightnessY;
        try {
            if(model==null)model=AdvancedModelLoader.loadModel(MODEL);
            float fade=(particleAge+tick)/(float)Math.max(1,particleMaxAge);
            double x=prevPosX+(posX-prevPosX)*tick-Particle.interpPosX;
            double y=prevPosY+(posY-prevPosY)*tick-Particle.interpPosY;
            double z=prevPosZ+(posZ-prevPosZ)*tick-Particle.interpPosZ;
            GL11.glTranslated(x,y,z);
            int frame=Math.min(15,Math.max(1,(int)(14*fade)+1));
            UtilsFX.bindTexture("textures/models/ripple"+frame+".png");
            int light=220;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,light%65536,light/65536);
            GL11.glRotatef(-yaw,0,1,0);GL11.glRotatef(pitch,1,0,0);
            GL11.glScaled(0.5,0.5,-0.5);
            GL11.glColor4f(0,0.5f,0.5f,1);
            model.renderAll();
        } finally {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,previousLightX,previousLightY);
            GL11.glColor4f(1,1,1,1);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
            Minecraft.getMinecraft().getTextureManager().bindTexture(ParticleEngine.particleTexture2);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE);
            buffer.begin(GL11.GL_QUADS,DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        }
    }
    @Override public int getFXLayer(){return 0;}
    @Override public int getTCParticleLayer(){return 2;}
    @Override public void onUpdate(){
        prevPosX=posX;prevPosY=posY;prevPosZ=posZ;
        if(particleAge++>=particleMaxAge)setExpired();
    }
}
