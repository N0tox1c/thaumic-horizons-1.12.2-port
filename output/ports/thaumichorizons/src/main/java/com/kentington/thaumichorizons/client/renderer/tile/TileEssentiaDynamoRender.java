//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import com.kentington.thaumichorizons.client.renderer.obj.AdvancedModelLoader;
import com.kentington.thaumichorizons.client.renderer.obj.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.kentington.thaumichorizons.client.fx.FXEssentiaTrail;
import com.kentington.thaumichorizons.client.renderer.model.ModelQuarterBlock;
import com.kentington.thaumichorizons.common.tiles.TileEssentiaDynamo;

import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;

public class TileEssentiaDynamoRender extends TileEntitySpecialRenderer {
    @Override
    public void render(net.minecraft.tileentity.TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        renderTileEntityAt(tile, x, y, z, partialTicks);
    }


    private final IModelCustom model;
    private static final ResourceLocation SCANNER;
    static String tx1;
    static String tx2;
    static String tx3;
    private final ModelQuarterBlock base;

    public TileEssentiaDynamoRender() {
        this.model = AdvancedModelLoader.loadModel(TileEssentiaDynamoRender.SCANNER);
        this.base = new ModelQuarterBlock();
    }

    public void renderTileEntityAt(final TileEntity te, final double x, final double y, final double z, final float f) {
        final TileEssentiaDynamo tco = (TileEssentiaDynamo) te;
        if (tco.essentia != null && tco.ticksProvided > 0) {
            final FXEssentiaTrail fb = new FXEssentiaTrail(
                    tco.getWorld(),
                    tco.getPos().getX() + 0.5,
                    tco.getPos().getY(),
                    tco.getPos().getZ() + 0.5,
                    tco.getPos().getX() + 0.5,
                    tco.getPos().getY() + 0.5,
                    tco.getPos().getZ() + 0.5,
                    Minecraft.getMinecraft().player.ticksExisted,
                    tco.essentia.getColor(),
                    0.3f);
            fb.setNoClip(true);
            ParticleEngine.instance.addEffect(tco.getWorld(), fb);
        }
        if (tco.rise >= 0.3f && tco.ticksProvided > 0) {
            GL11.glPushMatrix();
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            final long nt = System.nanoTime();
            UtilsFX.bindTexture(TileEssentiaDynamoRender.tx3);
            final int frames = com.kentington.thaumichorizons.client.renderer.LegacyFX.getTextureAnimationSize(TileEssentiaDynamoRender.tx3);
            final int i = (int) ((nt / 40000000L + x) % frames);
            com.kentington.thaumichorizons.client.renderer.LegacyFX.renderFacingQuad(
                    tco.getPos().getX() + 0.5,
                    tco.getPos().getY() + 0.5f,
                    tco.getPos().getZ() + 0.5,
                    0.0f,
                    0.2f,
                    0.9f,
                    frames,
                    i,
                    f,
                    tco.essentia.getColor());
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
            GL11.glPopMatrix();
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        UtilsFX.bindTexture(new net.minecraft.util.ResourceLocation("thaumichorizons".toLowerCase(java.util.Locale.ROOT), TileEssentiaDynamoRender.tx2));
        this.base.render();
        GL11.glTranslatef(0.5f, 0.2f + tco.rise, 0.5f);
        GL11.glRotatef(tco.rotation, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(tco.rotation2, 1.0f, 0.0f, 0.0f);
        UtilsFX.bindTexture(new net.minecraft.util.ResourceLocation("thaumichorizons".toLowerCase(java.util.Locale.ROOT), TileEssentiaDynamoRender.tx1));
        GL11.glScalef(0.36f, 0.36f, 0.36f);
        this.model.renderAll();
        GL11.glRotatef(-2.0f * tco.rotation2, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(0.5f, 1.0f, 0.5f);
        this.model.renderAll();
        GL11.glPopMatrix();
    }

    static {
        SCANNER = new ResourceLocation("thaumcraft", "textures/models/scanner.obj");
        TileEssentiaDynamoRender.tx1 = "textures/models/thaumiumring.png";
        TileEssentiaDynamoRender.tx2 = "textures/models/dynamoessentiabase.png";
        TileEssentiaDynamoRender.tx3 = "textures/items/lightningringv.png";
    }
}
