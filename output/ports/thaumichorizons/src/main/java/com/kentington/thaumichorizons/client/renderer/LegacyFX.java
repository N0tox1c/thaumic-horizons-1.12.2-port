package com.kentington.thaumichorizons.client.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Original animated TC4 quads, with native buffers and retained sheet layout. */
@SideOnly(Side.CLIENT)
public final class LegacyFX {
    public static final ResourceLocation NODE_TEXTURE = new ResourceLocation("thaumcraft", "textures/misc/nodes.png");
    private LegacyFX() {}

    public static int getTextureAnimationSize(String texture) {
        ResourceLocation id = new ResourceLocation("thaumcraft", texture);
        try (IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(id)) {
            BufferedImage image = ImageIO.read(resource.getInputStream());
            if (image == null || image.getWidth() % image.getHeight() != 0)
                throw new IllegalArgumentException("Invalid horizontal animation sheet: " + id);
            return image.getWidth() / image.getHeight();
        } catch (IOException error) {
            throw new IllegalStateException("Cannot read original animation sheet " + id, error);
        }
    }

    public static double[] sheetUV(int frames, int row, int frame, boolean grid) {
        if (frames <= 0 || row < 0 || (grid && row >= frames))
            throw new IllegalArgumentException("Invalid animation coordinates");
        int index = Math.floorMod(frame, frames);
        return new double[]{index / (double) frames, (index + 1) / (double) frames,
                grid ? row / (double) frames : 0, grid ? (row + 1) / (double) frames : 1};
    }

    private static void quad(float scale, float alpha, int color, double[] uv) {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        float red = (color >> 16 & 255) / 255.0f;
        float green = (color >> 8 & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;
        buffer.pos(-scale, -scale, 0).tex(uv[0], uv[3]).color(red, green, blue, alpha).endVertex();
        buffer.pos(scale, -scale, 0).tex(uv[1], uv[3]).color(red, green, blue, alpha).endVertex();
        buffer.pos(scale, scale, 0).tex(uv[1], uv[2]).color(red, green, blue, alpha).endVertex();
        buffer.pos(-scale, scale, 0).tex(uv[0], uv[2]).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    private static void facing(double x, double y, double z, float angle, float scale, float alpha,
                               int frames, int row, int frame, float partialTicks, int color, boolean grid) {
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x - manager.viewerPosX, y - manager.viewerPosY, z - manager.viewerPosZ);
        GlStateManager.rotate(-manager.playerViewY, 0, 1, 0);
        GlStateManager.rotate(manager.playerViewX * (manager.options.thirdPersonView == 2 ? -1 : 1), 1, 0, 0);
        GlStateManager.rotate((float)Math.toDegrees(angle), 0, 0, 1);
        quad(scale, alpha, color, sheetUV(frames, row, frame, grid));
        GlStateManager.popMatrix();
    }

    public static void renderFacingStrip(double x,double y,double z,float angle,float scale,float alpha,
                                          int frames,int row,int frame,float partialTicks,int color) {
        facing(x,y,z,angle,scale,alpha,frames,row,frame,partialTicks,color,true);
    }
    public static void renderFacingQuad(double x,double y,double z,float angle,float scale,float alpha,
                                        int frames,int frame,float partialTicks,int color) {
        facing(x,y,z,angle,scale,alpha,frames,0,frame,partialTicks,color,false);
    }
    public static void renderAnimatedQuad(float scale,float alpha,int frames,int frame,float angle,int color) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate((float)Math.toDegrees(angle),0,0,1);
        quad(scale,alpha,color,sheetUV(frames,0,frame,false));
        GlStateManager.popMatrix();
    }
}
