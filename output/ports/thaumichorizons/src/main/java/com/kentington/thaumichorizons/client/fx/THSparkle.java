package com.kentington.thaumichorizons.client.fx;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.particles.FXSparkle;

/** Uses the normal Thaumcraft 4 particle renderer without exposing protected Particle fields. */
@SideOnly(Side.CLIENT)
public final class THSparkle extends FXSparkle {
    public THSparkle(World world, double x, double y, double z, float scale, int type, float speed) {
        super(world, x, y, z, scale, type, speed);
    }

    public void setMotion(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    public void disableCollision() {
        this.canCollide = false;
    }
}
