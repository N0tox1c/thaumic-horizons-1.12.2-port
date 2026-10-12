//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.Thaumcraft;

public class TileSoulBeacon extends TileThaumcraft {

    @SideOnly(Side.CLIENT)
    private float field_146014_j;

    @SideOnly(Side.CLIENT)
    private long field_146016_i;

    public boolean activate(final EntityPlayer p) {
        p.getEntityData().setBoolean("soulBeacon", true);
        p.getEntityData().setIntArray("soulBeaconCoords", new int[] { this.pos.getX(), this.pos.getY(), this.pos.getZ() });
        p.getEntityData().setInteger("soulBeaconDim", this.world.provider.getDimension());
        p.sendMessage(
                new TextComponentString(
                        TextFormatting.ITALIC + ""
                                + TextFormatting.GRAY
                                + I18n.translateToLocal("thaumichorizons.setBeacon")));
        Thaumcraft.proxy.arcLightning(
                this.world,
                p.posX,
                p.posY + p.getEyeHeight(),
                p.posZ,
                this.pos.getX() + 0.5,
                this.pos.getY() + 0.75,
                this.pos.getZ() + 0.5,
                0.05f,
                1.0f,
                0.05f,
                0.5f);
        com.kentington.thaumichorizons.common.lib.THSoundBridge.play(this.world, this.pos.getX() + 0.5, this.pos.getY() + 0.75, this.pos.getZ() + 0.5, "thaumcraft:zap", 1.0f, 1.0f);
        return true;
    }

    @SideOnly(Side.CLIENT)
    public float func_146002_i() {
        final int i = (int) (this.world.getTotalWorldTime() - this.field_146016_i);
        this.field_146016_i = this.world.getTotalWorldTime();
        if (i > 1) {
            this.field_146014_j -= i / 40.0f;
            if (this.field_146014_j < 0.0f) {
                this.field_146014_j = 0.0f;
            }
        }
        this.field_146014_j += 0.025f;
        if (this.field_146014_j > 1.0f) {
            this.field_146014_j = 1.0f;
        }
        return this.field_146014_j;
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileSoulBeacon.INFINITE_EXTENT_AABB;
    }
}
