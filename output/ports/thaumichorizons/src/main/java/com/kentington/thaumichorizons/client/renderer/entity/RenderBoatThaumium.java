//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import com.kentington.thaumichorizons.common.entities.EntityBoatThaumium;

public class RenderBoatThaumium extends RenderBoat {

    private static final ResourceLocation boatTextures;

    public RenderBoatThaumium(RenderManager manager) {
        super(manager);
        this.modelBoat = new ModelArcaneBoat();
    }

    @Override
    protected ResourceLocation getEntityTexture(final EntityBoat boat) {
        return boatTextures;
    }

    @Override
    public void setupTranslation(double x, double y, double z) {
        GlStateManager.translate(x, y + 0.55, z);
    }

    @Override
    public boolean isMultipass() { return false; }

    static {
        boatTextures = new ResourceLocation("thaumichorizons", "textures/entity/boatthaumium.png");
    }
}
