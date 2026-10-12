//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;

public class RenderVoltSlime extends RenderSlime {

    private static final ResourceLocation slimeTextures;

    protected ResourceLocation getEntityTexture(final EntitySlime p_110775_1_) {
        return RenderVoltSlime.slimeTextures;
    }

    public RenderVoltSlime(net.minecraft.client.renderer.entity.RenderManager manager) {
        super(manager);
        this.shadowSize = 0.25f;
    }

    static {
        slimeTextures = new ResourceLocation("thaumichorizons", "textures/entity/voltslime.png");
    }
}
