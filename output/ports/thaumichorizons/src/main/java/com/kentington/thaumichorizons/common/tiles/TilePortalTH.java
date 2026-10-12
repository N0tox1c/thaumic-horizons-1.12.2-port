//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;


import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.Thaumcraft;

public class TilePortalTH extends TileThaumcraft  implements net.minecraft.util.ITickable {

    public int opencount;
    private int count;
    public int dimension;
    public int pocket;

    public TilePortalTH() {
        this.opencount = -1;
        this.count = 0;
        this.dimension = 0;
        this.pocket = -1;
    }

    public boolean canUpdate() {
        return true;
    }

    public double getMaxRenderDistanceSquared() {
        return 9216.0;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(
                this.pos.getX() - 1,
                this.pos.getY() - 1,
                this.pos.getZ() - 1,
                this.pos.getX() + 2,
                this.pos.getY() + 2,
                this.pos.getZ() + 2);
    }

    public void update() {
        ++this.count;
        if (this.world.isRemote && (this.count % 250 == 0 || this.count == 0)) {
            com.kentington.thaumichorizons.common.lib.THSoundBridge.playLegacy(this.world, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, "thaumcraft:evilportal", 1.0f, 1.0f, false);
        }
        if (this.world.isRemote && this.opencount < 30) {
            ++this.opencount;
        }
        if (!this.world.isRemote && this.count % 5 == 0) {
            final List ents = this.world.getEntitiesWithinAABB(
                    EntityPlayerMP.class,
                    new AxisAlignedBB(
                            this.pos.getX(),
                            this.pos.getY(),
                            this.pos.getZ(),
                            this.pos.getX() + 1,
                            this.pos.getY() + 1,
                            this.pos.getZ() + 1).expand(0.5, 1.0, 0.5));
            if (ents.size() > 0) {
                for (final Object e : ents) {
                    final EntityPlayerMP player = (EntityPlayerMP) e;
                    if (!player.isRiding() && !player.isBeingRidden()) {
                        final MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
                        if (player.timeUntilPortal > 0) {
                            continue;
                        }
                        player.timeUntilPortal = 50;
                        final int oldDim = player.dimension;
                    }
                }
            }
        }

        if (this.count > 250) {
            com.kentington.thaumichorizons.common.lib.THSoundBridge.playLegacy(this.world, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, "thaumcraft:craftfail", 1.0f, 1.0f, false);
            Thaumcraft.proxy
                    .burst(this.getWorld(), this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f, 3.0f);
            this.world.setBlockToAir(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
        }
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setInteger("dimension", this.dimension);
        nbttagcompound.setInteger("count", this.count);
        nbttagcompound.setInteger("pocket", this.pocket);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.dimension = nbttagcompound.getInteger("dimension");
        this.count = nbttagcompound.getInteger("count");
        this.pocket = nbttagcompound.getInteger("pocket");
    }
}
