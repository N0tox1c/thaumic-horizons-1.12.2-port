//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionVisBoost extends Potion {

    public static PotionVisBoost instance;
    private static final int statusIconIndex = 4;
    static final ResourceLocation rl;

    public PotionVisBoost(final boolean isBad, final int color) {
        super(isBad, color);
        this.setIconIndex(statusIconIndex, 0);
    }

    public static void init() {
        PotionVisBoost.instance.setPotionName("potion.visboost");
        PotionVisBoost.instance.setIconIndex(4, 0);
        PotionVisBoost.instance.setEffectiveness(0.25);
    }

    public boolean isBadEffect() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().renderEngine.bindTexture(PotionVisBoost.rl);
        return super.getStatusIconIndex();
    }

    public void performEffect(final EntityLivingBase target, final int par2) {}

    static {
        PotionVisBoost.instance = null;
        rl = new ResourceLocation("thaumichorizons", "textures/misc/potions.png");
    }
}
