//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import com.kentington.thaumichorizons.client.renderer.obj.AdvancedModelLoader;
import com.kentington.thaumichorizons.client.renderer.obj.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.kentington.thaumichorizons.client.renderer.model.ModelQuarterBlock;
import com.kentington.thaumichorizons.common.tiles.TileVisDynamo;

import thaumcraft.client.lib.UtilsFX;

public class TileVisDynamoRender extends TileEntitySpecialRenderer {
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

    public TileVisDynamoRender() {
        this.model = AdvancedModelLoader.loadModel(TileVisDynamoRender.SCANNER);
        this.base = new ModelQuarterBlock();
    }

    public void renderTileEntityAt(final TileEntity te, final double x, final double y, final double z, final float f) {
        final TileVisDynamo tco = (TileVisDynamo) te;
        if (tco.rise >= 0.3f && tco.ticksProvided > 0) {
            GL11.glPushMatrix();
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            final long nt = System.nanoTime();
            UtilsFX.bindTexture(TileVisDynamoRender.tx3);
            final int frames = com.kentington.thaumichorizons.client.renderer.LegacyFX.getTextureAnimationSize(TileVisDynamoRender.tx3);
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
                    tco.color.getRGB());
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
            GL11.glPopMatrix();
        }
        if (tco.drainEntity != null && tco.drainCollision != null) {
            final Entity drainEntity = tco.drainEntity;
            if (drainEntity instanceof EntityPlayer && !((EntityPlayer) drainEntity).isHandActive()) {
                tco.drainEntity = null;
                tco.drainCollision = null;
            } else {
                final RayTraceResult drainCollision = tco.drainCollision;
                GL11.glPushMatrix();
                float f2 = 0.0f;
                final int iiud = ((EntityPlayer) drainEntity).getItemInUseMaxCount();
                if (drainEntity instanceof EntityPlayer) {
                    f2 = MathHelper.sin(iiud / 10.0f) * 10.0f;
                }
                Vec3d vec3 = new Vec3d(-0.1, -0.1, 0.5);
                vec3 = vec3.rotatePitch(
                        -(drainEntity.prevRotationPitch
                                + (drainEntity.rotationPitch - drainEntity.prevRotationPitch) * f) * (float) Math.PI
                                / 180.0f);
                vec3 = vec3.rotateYaw(
                        -(drainEntity.prevRotationYaw + (drainEntity.rotationYaw - drainEntity.prevRotationYaw) * f)
                                * (float) Math.PI
                                / 180.0f);
                vec3 = vec3.rotateYaw(-f2 * 0.01f);
                vec3 = vec3.rotatePitch(-f2 * 0.015f);
                final double d3 = drainEntity.prevPosX + (drainEntity.posX - drainEntity.prevPosX) * f + vec3.x;
                final double d4 = drainEntity.prevPosY + (drainEntity.posY - drainEntity.prevPosY) * f + vec3.y;
                final double d5 = drainEntity.prevPosZ + (drainEntity.posZ - drainEntity.prevPosZ) * f + vec3.z;
                final double d6 = (drainEntity == Minecraft.getMinecraft().player) ? 0.0
                        : drainEntity.getEyeHeight();
                UtilsFX.drawFloatyLine(
                        drainCollision.getBlockPos().getX() + 0.5,
                        drainCollision.getBlockPos().getY() + 0.5,
                        drainCollision.getBlockPos().getZ() + 0.5,
                        d3,
                        d4 + d6,
                        d5,
                        f,
                        tco.color.getRGB(),
                        "textures/misc/wispy.png",
                        -0.02f,
                        Math.min(iiud, 10) / 10.0f);
                GL11.glPopMatrix();
            }
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        UtilsFX.bindTexture(new net.minecraft.util.ResourceLocation("thaumichorizons".toLowerCase(java.util.Locale.ROOT), TileVisDynamoRender.tx2));
        this.base.render();
        GL11.glTranslatef(0.5f, 0.2f + tco.rise, 0.5f);
        GL11.glRotatef(tco.rotation, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(tco.rotation2, 1.0f, 0.0f, 0.0f);
        UtilsFX.bindTexture(new net.minecraft.util.ResourceLocation("thaumichorizons".toLowerCase(java.util.Locale.ROOT), TileVisDynamoRender.tx1));
        GL11.glScalef(0.36f, 0.36f, 0.36f);
        this.model.renderAll();
        GL11.glRotatef(-2.0f * tco.rotation2, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(0.5f, 1.0f, 0.5f);
        this.model.renderAll();
        GL11.glPopMatrix();
    }

    static {
        SCANNER = new ResourceLocation("thaumcraft", "textures/models/scanner.obj");
        TileVisDynamoRender.tx1 = "textures/models/goldring.png";
        TileVisDynamoRender.tx2 = "textures/models/dynamobase.png";
        TileVisDynamoRender.tx3 = "textures/items/lightningringv.png";
    }
}
