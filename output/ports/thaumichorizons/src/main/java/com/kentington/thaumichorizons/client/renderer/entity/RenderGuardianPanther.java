//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;

import org.lwjgl.opengl.GL11;

public class RenderGuardianPanther extends RenderOcelot {

    public RenderGuardianPanther(net.minecraft.client.renderer.entity.RenderManager manager) {
        super(manager);
        this.shadowSize = 0.5f;
    }

    protected void preRenderCallback(final EntityOcelot p_77041_1_, final float p_77041_2_) {
        super.preRenderCallback(p_77041_1_, p_77041_2_);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }
}
