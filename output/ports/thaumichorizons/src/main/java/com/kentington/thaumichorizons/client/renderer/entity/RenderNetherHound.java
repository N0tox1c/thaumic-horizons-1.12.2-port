//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

import com.kentington.thaumichorizons.common.entities.EntityNetherHound;

public class RenderNetherHound extends RenderWolf {

    ResourceLocation wolfTex;

    public RenderNetherHound(net.minecraft.client.renderer.entity.RenderManager manager) {
        super(manager);
        this.shadowSize = 0.5f;
        this.wolfTex = new ResourceLocation("thaumichorizons", "textures/entity/netherhound.png");
    }

    @Override protected ResourceLocation getEntityTexture(EntityWolf wolf) {
        return this.wolfTex;
    }
}
