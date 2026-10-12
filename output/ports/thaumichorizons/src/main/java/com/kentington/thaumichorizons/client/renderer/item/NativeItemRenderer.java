package com.kentington.thaumichorizons.client.renderer.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

/** Native TEISR entry point; retains the original per-context model animation code. */
public abstract class NativeItemRenderer extends TileEntityItemStackRenderer {
    public enum ItemRenderType { ENTITY, EQUIPPED, EQUIPPED_FIRST_PERSON, INVENTORY }
    public enum ItemRendererHelper { ENTITY_ROTATION, ENTITY_BOBBING, EQUIPPED_BLOCK, BLOCK_3D, INVENTORY_BLOCK }
    private ItemRenderType context=ItemRenderType.INVENTORY;
    public void setPerspective(TransformType type) {
        switch(type) {
            case GROUND: context=ItemRenderType.ENTITY; break;
            case FIRST_PERSON_LEFT_HAND: case FIRST_PERSON_RIGHT_HAND: context=ItemRenderType.EQUIPPED_FIRST_PERSON; break;
            case THIRD_PERSON_LEFT_HAND: case THIRD_PERSON_RIGHT_HAND: context=ItemRenderType.EQUIPPED; break;
            default: context=ItemRenderType.INVENTORY;
        }
    }
    public abstract boolean handleRenderType(ItemStack item, ItemRenderType type);
    public boolean handlesPerspective(ItemStack item) { return handleRenderType(item,context); }
    public abstract boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper);
    public abstract void renderItem(ItemRenderType type, ItemStack item, Object... data);
    @Override public void renderByItem(ItemStack stack, float partialTicks) {
        GlStateManager.pushMatrix();
        try {
            // RenderItem shifts the native model origin from the center to its corner.
            GlStateManager.translate(.5F,.5F,.5F);
            renderItem(context,stack);
        } finally { GlStateManager.popMatrix(); }
    }
}
