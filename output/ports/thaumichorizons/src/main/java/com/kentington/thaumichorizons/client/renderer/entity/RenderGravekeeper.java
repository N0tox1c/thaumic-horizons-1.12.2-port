//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;

public class RenderGravekeeper extends RenderOcelot {

    ResourceLocation rl;

    public RenderGravekeeper(net.minecraft.client.renderer.entity.RenderManager manager) {
        super(manager);
        this.shadowSize = 0.5f;
        this.rl = new ResourceLocation("thaumichorizons", "textures/entity/gravekeeper.png");
    }

    protected ResourceLocation getEntityTexture(final EntityOcelot p_110775_1_) {
        return this.rl;
    }
}
