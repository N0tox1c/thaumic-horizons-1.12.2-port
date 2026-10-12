package com.kentington.thaumichorizons.client.audio;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class VortexSound extends MovingSound {

    private final TileEntity tile;

    public VortexSound(TileEntity tile) {
        super(com.kentington.thaumichorizons.common.lib.THSounds.VORTEX, net.minecraft.util.SoundCategory.AMBIENT);
        this.tile = tile;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 1.0F;
    }

    // Let me know if update needs to be cached or if that's a good idea or not for performance.

    @Override
    public void update() {
        if (tile == null || tile.isInvalid() || tile.getWorld() == null) {
            this.donePlaying = true;
            return;
        }

        // follow tile
        this.xPosF = tile.getPos().getX() + 0.5F;
        this.yPosF = tile.getPos().getY() + 0.5F;
        this.zPosF = tile.getPos().getZ() + 0.5F;

        // distance-based volume
        EntityPlayer p = Minecraft.getMinecraft().player;

        if (p != null) {
            double dist = p.getDistance(this.xPosF, this.yPosF, this.zPosF);
            this.volume = Math.max(0.0F, 1.0F / (1.0F + (float) (dist / 8.0F)));
        }
    }

    public void markDone() {
        this.donePlaying = true;
    }

}
