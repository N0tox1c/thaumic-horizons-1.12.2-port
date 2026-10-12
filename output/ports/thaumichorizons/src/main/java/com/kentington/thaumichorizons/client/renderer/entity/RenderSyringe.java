package com.kentington.thaumichorizons.client.renderer.entity;

import com.kentington.thaumichorizons.client.renderer.model.ModelSyringe;
import com.kentington.thaumichorizons.common.entities.EntitySyringe;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/** Native Forge 1.12 RenderManager-backed syringe renderer. */
public class RenderSyringe extends Render<EntitySyringe> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("thaumichorizons", "textures/models/syringe.png");
    private final ModelSyringe model = new ModelSyringe();

    public RenderSyringe(RenderManager manager) {
        super(manager);
        this.shadowSize = 0.0F;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySyringe entity) {
        return TEXTURE;
    }

    @Override
    public void doRender(EntitySyringe syringe, double x, double y, double z,
                         float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        try {
            GlStateManager.translate(x, y, z);
            float yaw = syringe.prevRotationYaw
                    + (syringe.rotationYaw - syringe.prevRotationYaw) * partialTicks;
            float pitch = syringe.prevRotationPitch
                    + (syringe.rotationPitch - syringe.prevRotationPitch) * partialTicks;
            GlStateManager.rotate(yaw + 90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-pitch, 0.0F, 0.0F, 1.0F);
            bindEntityTexture(syringe);
            model.render(syringe, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, null);
        } finally {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }
}
