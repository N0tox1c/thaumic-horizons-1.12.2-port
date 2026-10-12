//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import com.kentington.thaumichorizons.client.renderer.LegacyTessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.opengl.GL11;

import com.kentington.thaumichorizons.common.tiles.TilePortalTH;

import thaumcraft.client.lib.UtilsFX;

public class TilePortalTHRender extends TileEntitySpecialRenderer {
    @Override
    public void render(net.minecraft.tileentity.TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        renderTileEntityAt(tile, x, y, z, partialTicks);
    }


    public static final ResourceLocation portaltex;

    public void renderTileEntityAt(final TileEntity te, final double x, final double y, final double z, final float f) {
        if (te.getWorld() != null) {
            this.renderPortal((TilePortalTH) te, x, y, z, f);
        }
    }

    private void renderPortal(final TilePortalTH te, final double x, final double y, final double z, final float f) {
        final long nt = System.nanoTime();
        final long time = nt / 50000000L;
        final int c = (int) Math.min(30.0f, te.opencount + f);
        final int e = (int) Math.min(5.0f, te.opencount + f);
        final float scale = e / 5.0f;
        final float scaley = c / 30.0f;
        UtilsFX.bindTexture(TilePortalTHRender.portaltex);
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0f, 0.0f, 1.0f, 1.0f);
        if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer) {
            final EntityPlayer player = ((EntityPlayer) (Minecraft.getMinecraft().getRenderViewEntity()));

            final LegacyTessellator tessellator = LegacyTessellator.getInstance();
            final float arX = ActiveRenderInfo.getRotationX();
            final float arZ = ActiveRenderInfo.getRotationZ();
            final float arYZ = ActiveRenderInfo.getRotationYZ();
            final float arXY = ActiveRenderInfo.getRotationXY();
            final float arXZ = ActiveRenderInfo.getRotationXZ();
            final double iPX = player.prevPosX + (player.posX - player.prevPosX) * f;
            final double iPY = player.prevPosY + (player.posY - player.prevPosY) * f;
            final double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
            tessellator.startDrawingQuads();
            tessellator.setBrightness(220);
            switch (te.dimension) {
    case -1: {
                    tessellator.setColorRGBA_F(1.0f, 0.1f, 0.1f, 1.0f);
                } break;
    case 0: {
                    tessellator.setColorRGBA_F(0.1f, 0.8f, 0.8f, 1.0f);
                } break;
    case 1: {
                    tessellator.setColorRGBA_F(0.25f, 0.25f, 0.25f, 1.0f);
                } break;
    default: {
                    tessellator.setColorRGBA_F(0.5f, 0.5f, 0.1f, 1.0f);
                } break;
}
            final double px = x + 0.5;
            final double py = y + 0.5;
            final double pz = z + 0.5;
            final Vec3d v1 = new Vec3d(-arX - arYZ, -arXZ, -arZ - arXY);
            final Vec3d v2 = new Vec3d(-arX + arYZ, arXZ, -arZ + arXY);
            final Vec3d v3 = new Vec3d(arX + arYZ, arXZ, arZ + arXY);
            final Vec3d v4 = new Vec3d(arX - arYZ, -arXZ, arZ - arXY);
            final int frame = (int) time % 16;
            final float f2 = frame / 16.0f;
            final float f3 = f2 + 0.0625f;
            final float f4 = 0.0f;
            final float f5 = 1.0f;
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            tessellator
                    .addVertexWithUV(px + v1.x * scale, py + v1.y * scaley, pz + v1.z * scale, f2, f5);
            tessellator
                    .addVertexWithUV(px + v2.x * scale, py + v2.y * scaley, pz + v2.z * scale, f3, f5);
            tessellator
                    .addVertexWithUV(px + v3.x * scale, py + v3.y * scaley, pz + v3.z * scale, f3, f4);
            tessellator
                    .addVertexWithUV(px + v4.x * scale, py + v4.y * scaley, pz + v4.z * scale, f2, f4);
            tessellator.draw();
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    static {
        portaltex = new ResourceLocation("thaumichorizons", "textures/misc/vortex.png");
    }
}
