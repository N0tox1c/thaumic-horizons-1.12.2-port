package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import com.kentington.thaumichorizons.client.renderer.model.ModelGolemTH;
import com.kentington.thaumichorizons.common.entities.EntityGolemTH;
import thaumcraft.client.renderers.entity.RenderGolemBase;
import thaumcraft.common.entities.golems.EntityGolemBase;

/** Native TC4 layers render cores, upgrades, decorations, damage and carried items
 * against TH's original model and its custom material texture. */
public class RenderGolemTH extends RenderGolemBase {
    private static final ResourceLocation VOID = new ResourceLocation("thaumichorizons", "textures/models/golem_void.png");

    public RenderGolemTH(RenderManager manager) {
        super(manager, new ModelGolemTH(false), new ModelGolemTH(false));
    }

    @Override protected ResourceLocation getEntityTexture(EntityGolemBase entity) {
        EntityGolemTH golem = (EntityGolemTH) entity;
        if (golem.texture == null && golem.blocky != null && golem.blocky != Blocks.AIR) golem.loadTexture();
        return golem.texture == null ? VOID : golem.texture;
    }
}
