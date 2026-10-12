package com.kentington.thaumichorizons.client.fx;

import java.awt.Color;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.client.fx.ITCParticle;

/** Essentia particle seeking a target; protects against division by zero at arrival. */
public class FXEssentiaTrail extends Particle implements ITCParticle {
    private final double targetX, targetY, targetZ;
    private final int count;
    public int particle = 24;

    public FXEssentiaTrail(World world, double x,double y,double z,double tx,double ty,double tz,
                           int count,int color,float scale) {
        super(world,x,y,z,0,0,0);
        this.count=count; this.targetX=tx;this.targetY=ty;this.targetZ=tz;
        this.particleScale=(MathHelper.sin(count/2.0f)*0.1f+1)*scale;
        this.particleMaxAge=20+rand.nextInt(20);
        this.motionX=MathHelper.sin(count/4.0f)*0.003f+rand.nextGaussian()*0.002;
        this.motionY=0.1f+MathHelper.sin(count/3.0f)*0.002f;
        this.motionZ=MathHelper.sin(count/2.0f)*0.003f+rand.nextGaussian()*0.002;
        Color c=new Color(color);
        float mr=c.getRed()/255.0f*0.2f, mg=c.getGreen()/255.0f*0.2f, mb=c.getBlue()/255.0f*0.2f;
        this.particleRed=c.getRed()/255.0f-mr+rand.nextFloat()*mr;
        this.particleGreen=c.getGreen()/255.0f-mg+rand.nextFloat()*mg;
        this.particleBlue=c.getBlue()/255.0f-mb+rand.nextFloat()*mb;
        this.particleGravity=0.2f;this.canCollide=true;
    }
    @Override public void renderParticle(BufferBuilder b,Entity camera,float tick,float rotX,float rotXZ,float rotZ,float rotYZ,float rotXY) {
        float size=0.1f*this.particleScale*(MathHelper.sin((this.particleAge-this.count)/5.0f)*0.25f+1);
        float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*tick-Particle.interpPosX);
        float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*tick-Particle.interpPosY);
        float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*tick-Particle.interpPosZ);
        vertex(b,x-rotX*size-rotXY*size,y-rotXZ*size,z-rotZ*size-rotYZ*size,0.5625f,0.125f);
        vertex(b,x-rotX*size+rotXY*size,y+rotXZ*size,z-rotZ*size+rotYZ*size,0.625f,0.125f);
        vertex(b,x+rotX*size+rotXY*size,y+rotXZ*size,z+rotZ*size+rotYZ*size,0.625f,0.0625f);
        vertex(b,x+rotX*size-rotXY*size,y-rotXZ*size,z+rotZ*size-rotYZ*size,0.5625f,0.0625f);
    }
    private void vertex(BufferBuilder b,double x,double y,double z,float u,float v) {
        b.pos(x,y,z).tex(u,v).color(particleRed,particleGreen,particleBlue,0.5f).lightmap(240,240).endVertex();
    }
    @Override public int getFXLayer() {return 0;}
    @Override public int getTCParticleLayer(){return 0;}
    @Override public int getBrightnessForRender(float tick){return 0xF000F0;}
    @Override public void onUpdate() {
        prevPosX=posX;prevPosY=posY;prevPosZ=posZ;
        if(particleAge++>=particleMaxAge){setExpired();return;}
        motionY+=0.01*particleGravity;
        if(canCollide) pushFromOpaqueBlock();
        move(motionX,motionY,motionZ);
        motionX*=0.985;motionY*=0.985;motionZ*=0.985;
        motionX=MathHelper.clamp(motionX,-0.05,0.05);
        motionY=MathHelper.clamp(motionY,-0.05,0.05);
        motionZ=MathHelper.clamp(motionZ,-0.05,0.05);
        double dx=targetX-posX,dy=targetY-posY,dz=targetZ-posZ;
        double distance=Math.sqrt(dx*dx+dy*dy+dz*dz);
        if(distance<2.0)particleScale*=0.98f;
        if(particleScale<0.2f||distance<1.0e-5){setExpired();return;}
        double acceleration=0.01/Math.min(1.0,distance);
        motionX+=dx/distance*acceleration;
        motionY+=dy/distance*acceleration;
        motionZ+=dz/distance*acceleration;
    }
    public void setGravity(float gravity){this.particleGravity=gravity;}
    private void pushFromOpaqueBlock(){
        BlockPos p=new BlockPos(posX,posY,posZ);
        if(world.isAirBlock(p)||!world.isBlockNormalCube(p,true))return;
        double dx=posX-p.getX(),dy=posY-p.getY(),dz=posZ-p.getZ();
        double[] distances={dx,1-dx,dy,1-dy,dz,1-dz};
        int nearest=-1;double best=Double.MAX_VALUE;
        for(int side=0;side<6;side++){
            BlockPos adjacent=p.add(side==0?-1:side==1?1:0,side==2?-1:side==3?1:0,side==4?-1:side==5?1:0);
            // The original 1.7 algorithm checks the six neighbouring cells.
            if(world.isBlockNormalCube(adjacent,true))continue;
            if(distances[side]<best){best=distances[side];nearest=side;}
        }
        if(nearest<0)return;
        float outward=rand.nextFloat()*0.05f+0.025f;
        float jitter=(rand.nextFloat()-rand.nextFloat())*0.1f;
        switch(nearest){
            case 0:motionX=-outward;motionY=jitter;motionZ=jitter;break;
            case 1:motionX=outward;motionY=jitter;motionZ=jitter;break;
            case 2:motionY=-outward;motionX=jitter;motionZ=jitter;break;
            case 3:motionY=outward;motionX=jitter;motionZ=jitter;break;
            case 4:motionZ=-outward;motionX=jitter;motionY=jitter;break;
            case 5:motionZ=outward;motionX=jitter;motionY=jitter;break;
            default:break;
        }
    }
    public void setNoClip(boolean noClip) { this.canCollide = !noClip; }
}
