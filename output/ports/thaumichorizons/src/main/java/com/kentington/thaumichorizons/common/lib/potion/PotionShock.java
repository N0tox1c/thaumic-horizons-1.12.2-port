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

public class PotionShock extends Potion {

    public static PotionShock instance;
    private static final int statusIconIndex = 0;
    static final ResourceLocation rl;

    public PotionShock(final boolean isBad, final int color) {
        super(isBad, color);
        this.setIconIndex(statusIconIndex, 0);
    }

    public static void init() {
        PotionShock.instance.setPotionName("potion.shock");
        PotionShock.instance.setIconIndex(0, 0);
        PotionShock.instance.setEffectiveness(0.25);
    }

    public boolean isBadEffect() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().renderEngine.bindTexture(PotionShock.rl);
        return super.getStatusIconIndex();
    }

    public void performEffect(final EntityLivingBase player, final int par2) {}

    static {
        PotionShock.instance = null;
        rl = new ResourceLocation("thaumichorizons", "textures/misc/potions.png");
    }
}
