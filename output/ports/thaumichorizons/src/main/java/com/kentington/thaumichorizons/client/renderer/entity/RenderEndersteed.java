//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.entity;

import java.util.Map;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Maps;

public class RenderEndersteed extends RenderHorse {

    ResourceLocation enderTex;
    ResourceLocation enderTexIron;
    ResourceLocation enderTexGold;
    ResourceLocation enderTexDiamond;
    private static final Map field_110852_a;

    public RenderEndersteed(net.minecraft.client.renderer.entity.RenderManager manager) {
        super(manager);
        this.shadowSize = 0.75f;
        this.enderTex = new ResourceLocation("thaumichorizons", "textures/entity/endersteed.png");
        this.enderTexIron = new ResourceLocation("thaumichorizons", "textures/entity/endersteediron.png");
        this.enderTexGold = new ResourceLocation("thaumichorizons", "textures/entity/endersteedgold.png");
        this.enderTexDiamond = new ResourceLocation("thaumichorizons", "textures/entity/endersteeddiamond.png");
    }

    protected ResourceLocation getEntityTexture(final EntityHorse p_110775_1_) {
        switch (p_110775_1_.getHorseArmorType().getOrdinal()) {
    case 1: {
                return this.enderTexIron;
            }
    case 2: {
                return this.enderTexGold;
            }
    case 3: {
                return this.enderTexDiamond;
            }
    default: {
                return this.enderTex;
            }
}
    }

    static {
        field_110852_a = Maps.newHashMap();
    }
}
