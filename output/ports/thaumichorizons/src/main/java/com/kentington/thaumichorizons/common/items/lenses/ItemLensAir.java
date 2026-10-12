//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items.lenses;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import com.kentington.thaumichorizons.client.renderer.LegacyTessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.opengl.GL11;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLensAir extends Item implements ILens  , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    ResourceLocation tex;
    ResourceLocation tex2;
    TextureAtlasSprite icon;

    public ItemLensAir() {
        this.tex = new ResourceLocation("thaumichorizons", "textures/fx/ripple.png");
        this.tex2 = new ResourceLocation("thaumichorizons", "textures/fx/vortex.png");
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public String lensName() {
        return "LensAir";
    }

    @SideOnly(Side.CLIENT)
    public void handleRender(final Minecraft mc, final float partialTicks) {
        if (mc.gameSettings.thirdPersonView > 0) {
            return;
        }
        final EntityPlayer player = mc.player;
        final List<Entity> critters = (List<Entity>) player.world.getEntitiesWithinAABBExcludingEntity(
                mc.player,
                new AxisAlignedBB(
                        player.posX - 24.0,
                        player.posY - 24.0,
                        player.posZ - 24.0,
                        player.posX + 24.0,
                        player.posY + 24.0,
                        player.posZ + 24.0));
        for (final Entity ent : critters) {
            if (ent instanceof EntityLivingBase) {
                Vec3d look = player.getLookVec();
                if (look.y == -1.0) {
                    look = new Vec3d(-0.1 * Math.sin(Math.toRadians(player.rotationYaw)), -0.999,
                            0.1 * Math.cos(Math.toRadians(player.rotationYaw)));
                } else if (look.y == 1.0) {
                    look = new Vec3d(-0.1 * Math.sin(Math.toRadians(player.rotationYaw)), 0.999,
                            0.1 * Math.cos(Math.toRadians(player.rotationYaw)));
                }
                final Vec3d playerPos = player.getPositionEyes(partialTicks);
                final Vec3d entityPos = ent.getPositionVector().add(
                        (ent.prevPosX - ent.posX) * (1 - partialTicks),
                        (ent.prevPosY - ent.posY) * (1 - partialTicks) + ent.height / 2.0,
                        (ent.prevPosZ - ent.posZ) * (1 - partialTicks));
                final Vec3d pos = entityPos.subtract(playerPos);
                final double scale = pos.length();
                final double dot = pos.dotProduct(look);
                if (dot >= 0.0) {
                    continue;
                }
                final Vec3d proj = new Vec3d(look.x * dot, look.y * dot, look.z * dot);
                final Vec3d rej = new Vec3d(
                        pos.x - proj.x,
                        pos.y - proj.y,
                        pos.z - proj.z);
                Vec3d right = look.crossProduct(new Vec3d(1.0E-4, -1.0, 1.0E-4));
                right = right.normalize();
                Vec3d up = right.crossProduct(look);
                up = up.normalize();
                double vert = rej.dotProduct(up);
                double horiz = rej.dotProduct(right);
                final ScaledResolution var5 = new ScaledResolution(mc);
                final int var6 = var5.getScaledWidth();
                final int var7 = var5.getScaledHeight();
                final float minScreen = Math.min(var6, var7);
                final double hScale = proj.length() * Math.tan(Math.toRadians(mc.gameSettings.fovSetting) / 2.0)
                        * 2.0;
                horiz /= hScale;
                vert = vert / hScale / var7 * var6;
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.005f);
                final float minEnt = Math.min(ent.width, ent.height);
                double size = minEnt * (minScreen / scale);
                final double xCenter = var6 * (1.0 + horiz) / 2.0;
                final double yCenter = var7 * (1.0 - vert) / 2.0;
                final LegacyTessellator t = LegacyTessellator.getInstance();
                if (((EntityLivingBase) ent).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD) {
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.tex);
                    final float sizeOffset = ent.ticksExisted % 16;
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f - (float) scale / 80.0f);
                    int numRipples = (int) (48.0 / scale + 1.0);
                    if (numRipples > 4) {
                        numRipples = 4;
                    }
                    for (int i = 0; i < numRipples; ++i) {
                        final double ripSize = size * ((i * 16 / (numRipples + 1) + sizeOffset) % 16.0f) / 12.0;
                        t.startDrawingQuads();
                        t.addVertexWithUV(xCenter - ripSize, yCenter + ripSize, 1.0, 0.0, 1.0);
                        t.addVertexWithUV(xCenter + ripSize, yCenter + ripSize, 1.0, 1.0, 1.0);
                        t.addVertexWithUV(xCenter + ripSize, yCenter - ripSize, 1.0, 1.0, 0.0);
                        t.addVertexWithUV(xCenter - ripSize, yCenter - ripSize, 1.0, 0.0, 0.0);
                        t.draw();
                    }
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                } else {
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.1f - (float) scale / 240.0f);
                    size *= 2.0;
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.tex2);
                    final double angle = Math.toRadians(ent.ticksExisted % 360);
                    final double sin = Math.sin(angle);
                    final double cos = Math.cos(angle);
                    GL11.glPushMatrix();
                    t.startDrawingQuads();
                    t.addVertexWithUV(xCenter - size * sin, yCenter + size * cos, 1.0, 0.0, 0.0);
                    t.addVertexWithUV(xCenter + size * cos, yCenter + size * sin, 1.0, 1.0, 0.0);
                    t.addVertexWithUV(xCenter + size * sin, yCenter - size * cos, 1.0, 1.0, 1.0);
                    t.addVertexWithUV(xCenter - size * cos, yCenter - size * sin, 1.0, 0.0, 1.0);
                    t.draw();
                    GL11.glPopMatrix();
                    final double sin2 = Math.sin(-angle);
                    final double cos2 = Math.cos(-angle);
                    GL11.glPushMatrix();
                    t.startDrawingQuads();
                    t.addVertexWithUV(xCenter - size * sin2 / 2.0, yCenter + size * cos2 / 2.0, 1.0, 0.0, 0.0);
                    t.addVertexWithUV(xCenter + size * cos2 / 2.0, yCenter + size * sin2 / 2.0, 1.0, 1.0, 0.0);
                    t.addVertexWithUV(xCenter + size * sin2 / 2.0, yCenter - size * cos2 / 2.0, 1.0, 1.0, 1.0);
                    t.addVertexWithUV(xCenter - size * cos2 / 2.0, yCenter - size * sin2 / 2.0, 1.0, 0.0, 1.0);
                    t.draw();
                    GL11.glPopMatrix();
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.LensAir";
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:lensair", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public void handleRemoval(final EntityPlayer p) {}
}
