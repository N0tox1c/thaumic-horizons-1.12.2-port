//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityAlchemitePrimed;

public class RenderAlchemitePrimed extends Render<EntityAlchemitePrimed> {

    private final BlockRendererDispatcher blockRenderer;

    public RenderAlchemitePrimed() {
        super(Minecraft.getMinecraft().getRenderManager());
        this.blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        this.shadowSize = 0.5f;
    }

    protected ResourceLocation getEntityTexture(final EntityAlchemitePrimed p_110775_1_) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    public void doRenderStuff(final EntityAlchemitePrimed entity, final double p_76986_2_, final double p_76986_4_,
            final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
        if (entity.fuse - p_76986_9_ + 1.0f < 10.0f) {
            float f2 = 1.0f - (entity.fuse - p_76986_9_ + 1.0f) / 10.0f;
            if (f2 < 0.0f) {
                f2 = 0.0f;
            }
            if (f2 > 1.0f) {
                f2 = 1.0f;
            }
            f2 *= f2;
            f2 *= f2;
            final float f3 = 1.0f + f2 * 0.3f;
            GL11.glScalef(f3, f3, f3);
        }
        float f2 = (1.0f - (entity.fuse - p_76986_9_ + 1.0f) / 100.0f) * 0.8f;
        this.bindEntityTexture(entity);
        GL11.glTranslatef(-0.5f,-0.5f,-0.5f);
        this.blockRenderer.renderBlockBrightness(ThaumicHorizons.blockAlchemite.getDefaultState(), entity.getBrightness());
        if (entity.fuse / 5 % 2 == 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, f2);
            this.blockRenderer.renderBlockBrightness(ThaumicHorizons.blockAlchemite.getDefaultState(), 1.0f);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        GL11.glPopMatrix();
    }

    public void doRender(final EntityAlchemitePrimed p_76986_1_, final double p_76986_2_, final double p_76986_4_,
            final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRenderStuff(
                (EntityAlchemitePrimed) p_76986_1_,
                p_76986_2_,
                p_76986_4_,
                p_76986_6_,
                p_76986_8_,
                p_76986_9_);
    }
}
