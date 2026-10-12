//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.kentington.thaumichorizons.common.entities.EntityLunarWolf;

import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.tile.TileNodeRenderer;

public class RenderLunarWolf extends RenderWolf {

    ResourceLocation wolfTex;

    public RenderLunarWolf(net.minecraft.client.renderer.entity.RenderManager manager) {
        super(manager);
        this.wolfTex = new ResourceLocation("thaumichorizons", "textures/entity/lunarwolf.png");
    }

    public void doRender(final net.minecraft.entity.passive.EntityWolf p_76986_1_, final double p_76986_2_, final double p_76986_4_,
            final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        if (p_76986_1_.world.getWorldTime() % 24000L < 12000L) {
            return;
        }
        final float scale = p_76986_1_.world.getCurrentMoonPhaseFactor() * 3.0f;
        GL11.glPushMatrix();
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_CULL_FACE);
        final int i = p_76986_1_.ticksExisted % 32;
        UtilsFX.bindTexture(com.kentington.thaumichorizons.client.renderer.LegacyFX.NODE_TEXTURE);
        com.kentington.thaumichorizons.client.renderer.LegacyFX.renderFacingStrip(
                p_76986_1_.posX,
                p_76986_1_.posY + p_76986_1_.height / 1.75,
                p_76986_1_.posZ,
                0.0f,
                scale,
                0.75f,
                32,
                1,
                i,
                p_76986_9_,
                11197951);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
        GL11.glPopMatrix();
    }

    @Override protected ResourceLocation getEntityTexture(EntityWolf wolf) {
        return this.wolfTex;
    }
}
