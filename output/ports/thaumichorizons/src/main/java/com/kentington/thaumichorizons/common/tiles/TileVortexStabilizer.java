//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

import com.kentington.thaumichorizons.client.fx.FXSonic;
import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.Thaumcraft;

public class TileVortexStabilizer extends TileThaumcraft implements IWandable , net.minecraft.util.ITickable {

    public boolean hasTarget;
    public int prevType;
    public int xTarget;
    public int yTarget;
    public int zTarget;
    public TileEntity target;
    public int direction;
    boolean fireOnce;
    public boolean redstoned;
    public EnumFacing dir;
    public Object theBeam;
    @SideOnly(Side.CLIENT)
    private FXSonic[] sonicFX;

    public TileVortexStabilizer() {
        this.xTarget = Integer.MAX_VALUE;
        this.yTarget = Integer.MAX_VALUE;
        this.zTarget = Integer.MAX_VALUE;
        this.target = null;
        this.fireOnce = false;
        this.theBeam = null;
        this.sonicFX = null;
    }

    public void update() {
        if (!this.fireOnce) {
            ThaumicHorizons.blockVortexStabilizer.neighborChanged(
                    this.world.getBlockState(this.pos), this.world, this.pos,
                    ThaumicHorizons.blockVortexStabilizer, this.pos);
            this.direction = (byte) this.getBlockMetadata();
            this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
            this.dir = EnumFacing.byIndex(this.direction);
            if (this.target == null) {
                this.target = this.world.getTileEntity(new BlockPos(this.xTarget, this.yTarget, this.zTarget));
            }
            this.fireOnce = true;
        }
        if (this.world.getWorldTime() % 5L == 0L) {
            RayTraceResult mop = null;
            if (this.redstoned) {
                mop = this.world.rayTraceBlocks(
                        new Vec3d(
                                this.pos.getX() + this.dir.getXOffset() + 0.75,
                                this.pos.getY() + this.dir.getYOffset() + 0.75,
                                this.pos.getZ() + this.dir.getZOffset() + 0.75),
                        new Vec3d(
                                this.pos.getX() + this.dir.getXOffset() * 10 + 0.5,
                                this.pos.getY() + this.dir.getYOffset() * 10 + 0.5,
                                this.pos.getZ() + this.dir.getZOffset() * 10 + 0.5));
            }
            if (mop != null) {
                if (mop.getBlockPos().getX() != this.xTarget || mop.getBlockPos().getY() != this.yTarget || mop.getBlockPos().getZ() != this.zTarget) {
                    if (this.hasTarget) {
                        this.reHungrifyTarget();
                        this.hasTarget = false;
                    }
                    // Separate if (not else if) so a new valid target is acquired in the same tick,
                    // preventing a window where beams is decremented but not yet restored.
                    final TileEntity newTE = this.world.getTileEntity(new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ()));
                    if (newTE instanceof INode) {
                        this.hasTarget = true;
                        this.target = newTE;
                        this.prevType = ((INode) newTE).getNodeType().ordinal();
                        this.deHungrifyTarget();
                    } else if (newTE instanceof TileVortex) {
                        this.hasTarget = true;
                        this.target = newTE;
                        this.deHungrifyTarget();
                    }
                    this.xTarget = mop.getBlockPos().getX();
                    this.yTarget = mop.getBlockPos().getY();
                    this.zTarget = mop.getBlockPos().getZ();
                    this.markDirty();
                    this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
                }
            } else {
                if (this.hasTarget) {
                    this.reHungrifyTarget();
                    this.hasTarget = false;
                    this.markDirty();
                    this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
                }
                this.xTarget = this.pos.getX() + this.dir.getXOffset() * 10;
                this.yTarget = this.pos.getY() + this.dir.getYOffset() * 10;
                this.zTarget = this.pos.getZ() + this.dir.getZOffset() * 10;
                this.target = null;
            }
        }
        if (this.world.isRemote) this.updateClientEffects();
    }

    @SideOnly(Side.CLIENT)
    private void updateClientEffects() {
        if (this.redstoned
                && ThaumicHorizons.proxy.readyToRender()
                && this.xTarget != Integer.MAX_VALUE
                && this.yTarget != Integer.MAX_VALUE
                && this.zTarget != Integer.MAX_VALUE) {
            if (this.sonicFX == null) {
                this.sonicFX = new FXSonic[3];
            }
            for (int i = 0; i < 3; ++i) {
                if (this.sonicFX[i] == null || !this.sonicFX[i].isAlive()) {
                    this.sonicFX[i] = new FXSonic(
                            Thaumcraft.proxy.getClientWorld(),
                            this.xTarget + 0.5,
                            this.yTarget + 0.5,
                            this.zTarget + 0.5,
                            10,
                            this.direction);
                    ThaumicHorizons.proxy.addEffect(this.sonicFX[i]);
                    break;
                }
            }
            this.theBeam = Thaumcraft.proxy.beamBore(
                    this.world,
                    this.pos.getX() + 0.5,
                    this.pos.getY() + 0.5,
                    this.pos.getZ() + 0.5,
                    this.xTarget + 0.5 - this.dir.getXOffset(),
                    this.yTarget + 0.5 - this.dir.getYOffset(),
                    this.zTarget + 0.5 - this.dir.getZOffset(),
                    1,
                    33023,
                    false,
                    2.0f,
                    this.theBeam,
                    1);
        } else if (this.sonicFX != null) {
            for (int i = 0; i < 3; ++i) {
                if (this.sonicFX[i] != null) {
                    this.sonicFX[i].setExpired();
                    this.sonicFX[i] = null;
                }
            }
        }
    }

    public void reHungrifyTarget() {
        if (this.target instanceof INode) {
            if (!isNodeTargetedByOtherStabilizer()) {
                ((INode) this.target).setNodeType(NodeType.values()[this.prevType]);
            }
        } else if (this.target instanceof TileVortex) {
            --((TileVortex) this.target).beams;
        }
        if (this.target != null) {
            this.target.markDirty();
        }
    }

    private boolean isNodeTargetedByOtherStabilizer() {
        // A stabilizer can only reach a node from up to 10 blocks away in one of 6 directions,
        // so check those 60 positions instead of scanning all loaded tile entities.
        int tx = this.target.getPos().getX();
        int ty = this.target.getPos().getY();
        int tz = this.target.getPos().getZ();
        for (EnumFacing d : EnumFacing.values()) {
            for (int dist = 1; dist <= 10; dist++) {
                TileEntity te = this.world
                        .getTileEntity(new net.minecraft.util.math.BlockPos(tx - d.getXOffset() * dist, ty - d.getYOffset() * dist, tz - d.getZOffset() * dist));
                if (te instanceof TileVortexStabilizer && te != this) {
                    TileVortexStabilizer other = (TileVortexStabilizer) te;
                    if (other.hasTarget && other.target == this.target) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    void deHungrifyTarget() {
        if (this.target instanceof INode) {
            ((INode) this.target).setNodeType(NodeType.NORMAL);
        } else if (this.target instanceof TileVortex) {
            ++((TileVortex) this.target).beams;
        }
        if (this.target != null) {
            this.target.markDirty();
        }
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setInteger("xT", this.xTarget);
        nbttagcompound.setInteger("yT", this.yTarget);
        nbttagcompound.setInteger("zT", this.zTarget);
        nbttagcompound.setInteger("direction", this.direction);
        nbttagcompound.setBoolean("hasTarget", this.hasTarget);
        nbttagcompound.setBoolean("active", this.redstoned);
        nbttagcompound.setInteger("prevType", this.prevType);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.xTarget = nbttagcompound.getInteger("xT");
        this.yTarget = nbttagcompound.getInteger("yT");
        this.zTarget = nbttagcompound.getInteger("zT");
        this.direction = nbttagcompound.getInteger("direction");
        this.hasTarget = nbttagcompound.getBoolean("hasTarget");
        this.redstoned = nbttagcompound.getBoolean("active");
        this.prevType = nbttagcompound.getInteger("prevType");
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileVortexStabilizer.INFINITE_EXTENT_AABB;
    }

    @Override
    public int onWandRightClick(final World world, final ItemStack wandstack, final EntityPlayer player, final int x,
            final int y, final int z, final int side, final int md) {
        this.dir = EnumFacing.byIndex(side);
        com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(world, x, y, z, this.direction = side, 3);
        com.kentington.thaumichorizons.common.lib.THSoundBridge.playLegacy(player.world, x + 0.5, y + 0.5, z + 0.5, "thaumcraft:tool", 0.5f, 0.9f + player.world.rand.nextFloat() * 0.2f, false);
        player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
        this.markDirty();
        return 0;
    }

    @Override
    public ItemStack onWandRightClick(final World world, final ItemStack wandstack, final EntityPlayer player) {
        return null;
    }

    @Override
    public void onUsingWandTick(final ItemStack wandstack, final EntityPlayer player, final int count) {}

    @Override
    public void onWandStoppedUsing(final ItemStack wandstack, final World world, final EntityPlayer player,
            final int count) {}
}
