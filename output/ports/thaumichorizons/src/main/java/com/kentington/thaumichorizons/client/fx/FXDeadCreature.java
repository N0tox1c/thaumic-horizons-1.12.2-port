package com.kentington.thaumichorizons.client.fx;

import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.world.World;

/** Native spell animation with the original red gore tint, velocities and no collision. */
public class FXDeadCreature extends ParticleSpell {
    public FXDeadCreature(World world, double x, double y, double z) {
        super(world, x, y, z, 0, 0, 0);
        setRBGColorF(0.8f, 0.2f, 0.2f);
        motionX = (world.rand.nextFloat() - 0.5f) * 0.4f;
        motionY = (world.rand.nextFloat() - 0.5f) * 0.2f;
        motionZ = (world.rand.nextFloat() - 0.5f) * 0.4f;
        canCollide = false;
    }
}
