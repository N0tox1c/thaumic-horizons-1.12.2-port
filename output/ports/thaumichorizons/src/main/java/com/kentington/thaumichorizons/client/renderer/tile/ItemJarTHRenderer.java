//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.tile;

import net.minecraft.client.Minecraft;
import com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


import org.lwjgl.opengl.GL11;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileSoulJar;

import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.ConfigBlocks;

public class ItemJarTHRenderer extends NativeItemRenderer {

    static String tx3;

    public boolean handleRenderType(final ItemStack item, final NativeItemRenderer.ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(final NativeItemRenderer.ItemRenderType type, final ItemStack item,
            final NativeItemRenderer.ItemRendererHelper helper) {
        return helper != NativeItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK;
    }

    public void renderItem(final NativeItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        if (item.getItem() == Item.getItemFromBlock(ThaumicHorizons.blockJar)) {
            final float short1 = 240.0f;
            final float short2 = 240.0f;
            GL11.glTranslated(0.5, 0.0, 0.5);
            if (type == NativeItemRenderer.ItemRenderType.ENTITY) {
                GL11.glTranslatef(-0.5f, -0.25f, -0.5f);
            } else if (type == NativeItemRenderer.ItemRenderType.EQUIPPED && Minecraft.getMinecraft().player != null) {
                GL11.glTranslatef(0.0f, 0.0f, -0.5f);
            }
            if (item.hasTagCompound() && item.getTagCompound().getBoolean("isSoul")) {
                final long nt = System.nanoTime();
                UtilsFX.bindTexture(new net.minecraft.util.ResourceLocation("thaumichorizons".toLowerCase(java.util.Locale.ROOT), ItemJarTHRenderer.tx3));
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glPushMatrix();
                GL11.glTranslated(0.0, 0.4, 0.0);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                com.kentington.thaumichorizons.client.renderer.LegacyFX.renderAnimatedQuad(0.3f, 0.9f, 16, (int) (nt / 40000000L % 16L), 0.0f, 16777215);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_BLEND);
            } else if (item.hasTagCompound()) {
                final TileSoulJar th = new TileSoulJar();
                GL11.glPushMatrix();
                GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
                GL11.glTranslatef(-0.5f, 0.0f, -0.5f);
                final EntityLivingBase viewer = Minecraft.getMinecraft().player;
                th.entity = EntityList.createEntityFromNBT(item.getTagCompound(), viewer.world);
                TileEntityRendererDispatcher.instance.render(th, 0.0, 0.0, 0.0, 0.0f);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                Minecraft.getMinecraft().entityRenderer.disableLightmap();
                GL11.glPopMatrix();
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0f, 0.5f, 0.0f);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ConfigBlocks.blockJar), net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.NONE);
            GL11.glPopMatrix();
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    static {
        ItemJarTHRenderer.tx3 = "textures/misc/soul.png";
    }
}
