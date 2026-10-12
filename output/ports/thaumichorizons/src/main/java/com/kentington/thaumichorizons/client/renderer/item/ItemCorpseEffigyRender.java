//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


import org.lwjgl.opengl.GL11;

import thaumcraft.client.lib.UtilsFX;

public class ItemCorpseEffigyRender extends NativeItemRenderer {

    private final ModelBiped corpse;
    private final String tx1;

    public ItemCorpseEffigyRender() {
        this.corpse = new ModelBiped();
        this.tx1 = "textures/models/corpseeffigy.png";
    }

    public boolean handleRenderType(final ItemStack item, final NativeItemRenderer.ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(final NativeItemRenderer.ItemRenderType type, final ItemStack item,
            final NativeItemRenderer.ItemRendererHelper helper) {
        return helper != NativeItemRenderer.ItemRendererHelper.BLOCK_3D;
    }

    public void renderItem(final NativeItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        GL11.glPushMatrix();
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        if (type != NativeItemRenderer.ItemRenderType.INVENTORY) {
            if (type == NativeItemRenderer.ItemRenderType.ENTITY) {
                GL11.glTranslated(0.0, -3.0, 0.0);
            } else {
                GL11.glScalef(0.5f, 0.5f, 0.5f);
                GL11.glTranslated(-1.0, -4.0, -1.0);
            }
        } else {
            GL11.glTranslated(0.0, -0.9, 0.0);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
        }
        UtilsFX.bindTexture(new ResourceLocation("thaumichorizons", this.tx1));
        this.corpse.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.125f);
        GL11.glPopMatrix();
    }
}
