//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;

public class RenderChocolateCow extends RenderCow {

    private static final ResourceLocation cowTextures;

    public RenderChocolateCow(net.minecraft.client.renderer.entity.RenderManager manager) {
        super(manager);
        this.shadowSize = 0.7f;
    }

    protected ResourceLocation getEntityTexture(final EntityCow p_110775_1_) {
        return RenderChocolateCow.cowTextures;
    }

    static {
        cowTextures = new ResourceLocation("thaumichorizons", "textures/entity/chocolatecow.png");
    }
}
