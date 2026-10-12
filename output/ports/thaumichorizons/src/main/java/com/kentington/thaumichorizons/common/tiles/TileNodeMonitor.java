//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;


import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.blocks.BlockNodeMonitor;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.INode;

public class TileNodeMonitor extends TileThaumcraft  implements net.minecraft.util.ITickable {

    public byte direction;
    public boolean activated;
    boolean lastActivated;
    public int rotation;
    public boolean switchy;

    public TileNodeMonitor() {
        this.direction = -1;
        this.rotation = 0;
        this.switchy = false;
        this.activated = false;
        this.lastActivated = false;
    }

    public void update() {
        if (!this.activated) {
            this.switchy = false;
            ++this.rotation;
            if (this.rotation > 360) {
                this.rotation -= 360;
            }
        } else if (this.world.isRemote && Minecraft.getMinecraft().player.ticksExisted % 15 == 0) {
            this.switchy = !this.switchy;
        }
        if (this.direction == -1) {
            this.direction = (byte) this.getBlockMetadata();
        }
        final EnumFacing dir = EnumFacing.byIndex(this.direction);
        if (dir == EnumFacing.UP
                && this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ())) instanceof INode) {
            this.activated = this.aspectCritical(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ());
        } else if (dir == EnumFacing.DOWN
                && this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ())) instanceof INode) {
                    this.activated = this.aspectCritical(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
                } else
            if (dir == EnumFacing.NORTH
                    && this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1)) instanceof INode) {
                        this.activated = this.aspectCritical(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1);
                    } else
                if (dir == EnumFacing.SOUTH
                        && this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1)) instanceof INode) {
                            this.activated = this.aspectCritical(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1);
                        } else
                    if (dir == EnumFacing.WEST && this.world
                            .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ())) instanceof INode) {
                                this.activated = this.aspectCritical(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ());
                            } else
                        if (dir == EnumFacing.EAST && this.world
                                .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ())) instanceof INode) {
                                    this.activated = this.aspectCritical(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ());
                                } else {
                                    ((BlockNodeMonitor) ThaumicHorizons.blockNodeMonitor)
                                            .killMe(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
                                }
        if (this.activated != this.lastActivated) {
            com.kentington.thaumichorizons.common.lib.WorldAccess.notifyBlocksOfNeighborChange(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), ThaumicHorizons.blockNodeMonitor);
        }
        this.lastActivated = this.activated;
    }

    private boolean aspectCritical(final int x, final int y, final int z) {
        final TileEntity node = this.world.getTileEntity(new BlockPos(x, y, z));
        if (node instanceof INode) {
            for (final Aspect asp : ((INode) node).getAspects().getAspects()) {
                if (((INode) node).getAspects().getAmount(asp) <= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setBoolean("active", this.activated);
        nbttagcompound.setBoolean("lastactive", this.lastActivated);
        nbttagcompound.setByte("dir", this.direction);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.activated = nbttagcompound.getBoolean("active");
        this.lastActivated = nbttagcompound.getBoolean("lastactive");
        this.direction = nbttagcompound.getByte("dir");
    }
}
