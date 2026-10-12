package com.kentington.thaumichorizons.client.fx;

import java.awt.Color;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.client.fx.ITCParticle;

/** Original bubbling essentia particle, adapted to Thaumcraft 4 Port's batched particles. */
public class FXEssentiaBubble extends Particle implements ITCParticle {
    private final int count;
    private int delay;
    public int particle = 24;

    public FXEssentiaBubble(World world, double x, double y, double z, int count,
                            int color, float scale, int delay) {
        super(world, x, y, z, 0, 0, 0);
        this.count = count;
        this.delay = delay;
        this.particleScale = (MathHelper.sin(count / 2.0f) * 0.1f + 1.0f) * scale;
        this.particleMaxAge = 20 + this.rand.nextInt(20);
        this.motionY = 0.025f + MathHelper.sin(count / 3.0f) * 0.002f;
        this.motionX = this.motionZ = 0;
        Color c = new Color(color);
        float mr = c.getRed() / 255.0f * 0.2f;
        float mg = c.getGreen() / 255.0f * 0.2f;
        float mb = c.getBlue() / 255.0f * 0.2f;
        this.particleRed = c.getRed() / 255.0f - mr + this.rand.nextFloat() * mr;
        this.particleGreen = c.getGreen() / 255.0f - mg + this.rand.nextFloat() * mg;
        this.particleBlue = c.getBlue() / 255.0f - mb + this.rand.nextFloat() * mb;
        this.particleGravity = 0.2f;
        this.canCollide = true;
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity camera, float partialTicks,
                               float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
        if (this.delay > 0) return;
        float u1 = 0.5625f, u2 = 0.625f, v1 = 0.0625f, v2 = 0.125f;
        float oscillation = MathHelper.sin((this.particleAge - this.count) / 5.0f) * 0.25f + 1;
        float scale = 0.1f * this.particleScale * oscillation;
        float x = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - Particle.interpPosX);
        float y = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - Particle.interpPosY);
        float z = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - Particle.interpPosZ);
        vertex(buffer, x - rotX*scale - rotXY*scale, y - rotXZ*scale, z - rotZ*scale - rotYZ*scale, u1,v2);
        vertex(buffer, x - rotX*scale + rotXY*scale, y + rotXZ*scale, z - rotZ*scale + rotYZ*scale, u2,v2);
        vertex(buffer, x + rotX*scale + rotXY*scale, y + rotXZ*scale, z + rotZ*scale + rotYZ*scale, u2,v1);
        vertex(buffer, x + rotX*scale - rotXY*scale, y - rotXZ*scale, z + rotZ*scale - rotYZ*scale, u1,v1);
    }
    private void vertex(BufferBuilder b, double x,double y,double z,float u,float v) {
        b.pos(x,y,z).tex(u,v).color(this.particleRed,this.particleGreen,this.particleBlue,0.5f)
            .lightmap(240,240).endVertex();
    }
    @Override public int getFXLayer() { return 0; }
    @Override public int getTCParticleLayer() { return 0; }
    @Override public int getBrightnessForRender(float ticks) { return 0xF000F0; }
    @Override public void onUpdate() {
        this.prevPosX=this.posX; this.prevPosY=this.posY; this.prevPosZ=this.posZ;
        if (this.delay > 0) { --this.delay; return; }
        if (this.particleAge++ >= this.particleMaxAge) { this.setExpired(); return; }
        this.motionY += 0.00125;
        this.particleScale *= 1.05f;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionY *= 0.985;
    }
    public void setNoClip(boolean noClip) { this.canCollide = !noClip; }
}
