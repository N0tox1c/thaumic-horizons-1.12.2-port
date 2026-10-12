//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.kentington.thaumichorizons.client.renderer.model.ModelRecombinator;
import com.kentington.thaumichorizons.common.tiles.TileRecombinator;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileNode;

@SideOnly(Side.CLIENT)
public class TileRecombinatorRender extends TileEntitySpecialRenderer {
    @Override
    public void render(net.minecraft.tileentity.TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        renderTileEntityAt(tile, x, y, z, partialTicks);
    }


    static String tx1;
    static String tx2;
    private final ModelRecombinator base;

    public TileRecombinatorRender() {
        this.base = new ModelRecombinator();
    }

    public void renderTileEntityAt(final TileEntity te, final double x, final double y, final double z, final float f) {
        final TileRecombinator tco = (TileRecombinator) te;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y - 0.5f, (float) z + 0.5f);
        UtilsFX.bindTexture(new net.minecraft.util.ResourceLocation("thaumichorizons".toLowerCase(java.util.Locale.ROOT), TileRecombinatorRender.tx1));
        final float sin = (float) Math.sin(tco.count / 8.0f);
        this.base.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, sin * 2.0f);
        if (tco.activated) {
            final long nt = System.nanoTime();
            final int frames = com.kentington.thaumichorizons.client.renderer.LegacyFX.getTextureAnimationSize(TileRecombinatorRender.tx2);
            final int i = (int) ((nt / 40000000L + x) % frames);
            UtilsFX.bindTexture(new net.minecraft.util.ResourceLocation("thaumcraft".toLowerCase(java.util.Locale.ROOT), TileRecombinatorRender.tx2));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glScalef(1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            com.kentington.thaumichorizons.client.renderer.LegacyFX.renderFacingQuad(
                    tco.getPos().getX() + 0.5,
                    tco.getPos().getY() + 1.5 + 0.2 * sin,
                    tco.getPos().getZ() + 0.5,
                    0.0f,
                    1.5f,
                    0.9f,
                    frames,
                    i,
                    f,
                    16777215);
            GL11.glPopMatrix();
            if (te.getWorld().getTileEntity(new net.minecraft.util.math.BlockPos(te.getPos().getX(), te.getPos().getY() - 1, te.getPos().getZ())) instanceof TileNode) {
                Thaumcraft.proxy.beamBore(
                        Minecraft.getMinecraft().world,
                        te.getPos().getX() + 0.5,
                        te.getPos().getY() + 0.2,
                        te.getPos().getZ() + 0.5,
                        te.getPos().getX() + 0.5,
                        te.getPos().getY() - 0.5,
                        te.getPos().getZ() + 0.5,
                        1,
                        16777215,
                        false,
                        0.1f,
                        null,
                        3);
            }
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glPopMatrix();
    }

    static {
        TileRecombinatorRender.tx1 = "textures/models/recombinator.png";
        TileRecombinatorRender.tx2 = "textures/items/lightningringv.png";
    }
}
