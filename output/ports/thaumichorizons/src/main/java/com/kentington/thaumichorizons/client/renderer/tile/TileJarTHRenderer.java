//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.tile;
import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.kentington.thaumichorizons.common.tiles.TileSoulJar;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelJar;

@SideOnly(Side.CLIENT)
public class TileJarTHRenderer extends TileEntitySpecialRenderer {
    @Override
    public void render(net.minecraft.tileentity.TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        renderTileEntityAt(tile, x, y, z, partialTicks);
    }


    @SuppressWarnings("FieldCanBeLocal") // An unfinished feature
    private final ModelJar model;
    static String tx3;

    public TileJarTHRenderer() {
        this.model = new ModelJar();
    }

    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z,
            final float f) {
        if (!(tile instanceof TileSoulJar)) {
            return;
        }
            final TileSoulJar th = ((TileSoulJar) (tile));

        if (th.jarTag != null && th.jarTag.getBoolean("isSoul")) {
            final long nt = System.nanoTime();
            UtilsFX.bindTexture(new net.minecraft.util.ResourceLocation("thaumichorizons".toLowerCase(java.util.Locale.ROOT), TileJarTHRenderer.tx3));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            com.kentington.thaumichorizons.client.renderer.LegacyFX.renderFacingQuad(
                    tile.getPos().getX() + 0.5,
                    tile.getPos().getY() + 0.4,
                    tile.getPos().getZ() + 0.5,
                    0.0f,
                    0.1f,
                    0.9f,
                    16,
                    (int) (nt / 40000000L % 16L),
                    f,
                    16777215);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            return;
        }
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.01f, (float) z + 0.5f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.bindTexture(th.getTexture());
        if (th.entity != null) {
            final float f2 = 0.25f;
            GL11.glScalef(f2, f2, f2);
            th.entity.setLocationAndAngles(th.getPos().getX() + 0.5, th.getPos().getY() + 0.5, th.getPos().getZ() + 0.5, 0.0f, 0.0f);
            Render render;
            render = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(th.entity);
            if (render != null) {
                render.doRender(th.entity, 0.0, 0.0, 0.0, 0.0f, f);
            }
        }
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    static {
        TileJarTHRenderer.tx3 = "textures/misc/soul.png";
    }
}
