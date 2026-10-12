//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;

public class RenderScholarChicken extends RenderChicken {

    ResourceLocation rl;

    public RenderScholarChicken(net.minecraft.client.renderer.entity.RenderManager manager) {
        super(manager);
        this.shadowSize = 0.5f;
        this.rl = new ResourceLocation("thaumichorizons", "textures/entity/scholarchicken.png");
    }

    protected ResourceLocation getEntityTexture(final EntityChicken p_110775_1_) {
        return this.rl;
    }
}
