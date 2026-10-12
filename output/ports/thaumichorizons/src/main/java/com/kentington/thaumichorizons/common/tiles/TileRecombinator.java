//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockZap;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileNode;

public class TileRecombinator extends TileThaumcraft  implements net.minecraft.util.ITickable {

    public int count;
    public boolean activated;
    public boolean shouldActivate;
    boolean fireOnce;

    public TileRecombinator() {
        this.count = -1;
        this.activated = false;
        this.shouldActivate = false;
        this.fireOnce = false;
    }

    public void update() {
        if (!this.fireOnce) {
            getBlockType().neighborChanged(world.getBlockState(pos), world, pos, getBlockType(), pos);
            this.fireOnce = true;
        }
        if (this.activated) {
            ++this.count;
        } else if (!this.activated && this.count > 0) {
            if (this.count > 50) {
                this.count = 50;
            }
            --this.count;
        }
        if (this.shouldActivate && !this.activated) {
            this.activated = true;
            this.markDirty();
            this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
        } else if (!this.shouldActivate && this.activated) {
            this.activated = false;
            this.markDirty();
            this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
        }
        if (!this.world.isRemote && this.activated
                && this.count > 50
                && this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ())) instanceof TileNode) {
            final TileNode tile = ((TileNode) (this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ()))));

            final int x = this.world.rand.nextInt(5) - this.world.rand.nextInt(5);
            final int y = this.world.rand.nextInt(5) - this.world.rand.nextInt(5) - 1;
            final int z = this.world.rand.nextInt(5) - this.world.rand.nextInt(5);
            if (x != 0 || y != -1 || z != 0) {
                final TileEntity te = this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ() + z));
                if (te instanceof TileNode && this.world.getBlockState(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ() + z)).getBlock()
                        == ConfigBlocks.blockAiry) {
            final TileNode nd = ((TileNode) (te));

                    if (te instanceof TileNode && ((TileNode) te).getLock() > 0) {
                        return;
                    }
                    if (nd.getAspects().size() == 0) {
                        return;
                    }
                    this.processCombos(nd, tile, x, y, z);
                }
            }
        }
    }

    void processCombos(final TileNode nd, final TileNode tile, final int x, final int y, final int z) {
        final AspectList possibleCombos = new AspectList();
        for (final Aspect asp : tile.getAspectsBase().getAspects()) {
            if (asp.isPrimal()) {
                for (final Aspect asp2 : nd.getAspectsBase().getAspects()) {
                    if (asp2.isPrimal() && ResearchManager.getCombinationResult(asp, asp2) != null) {
                        possibleCombos.add(ResearchManager.getCombinationResult(asp, asp2), 1);
                    }
                }
            }
        }
        if (possibleCombos.size() > 0 && possibleCombos.getAspects()[0] != null) {
            this.doMerge(possibleCombos, nd, tile, x, y, z);
            return;
        }
        for (final Aspect asp : tile.getAspectsBase().getAspects()) {
            if (asp.isPrimal()) {
                for (final Aspect asp2 : nd.getAspectsBase().getAspects()) {
                    if (ResearchManager.getCombinationResult(asp, asp2) != null) {
                        possibleCombos.add(ResearchManager.getCombinationResult(asp, asp2), 1);
                    }
                }
            }
        }
        if (possibleCombos.size() > 0 && possibleCombos.getAspects()[0] != null) {
            this.doMerge(possibleCombos, nd, tile, x, y, z);
            return;
        }
        for (final Aspect asp : tile.getAspectsBase().getAspects()) {
            for (final Aspect asp2 : nd.getAspectsBase().getAspects()) {
                if (asp2.isPrimal() && ResearchManager.getCombinationResult(asp, asp2) != null) {
                    possibleCombos.add(ResearchManager.getCombinationResult(asp, asp2), 1);
                }
            }
        }
        if (possibleCombos.size() > 0 && possibleCombos.getAspects()[0] != null) {
            this.doMerge(possibleCombos, nd, tile, x, y, z);
            return;
        }
        for (final Aspect asp : tile.getAspectsBase().getAspects()) {
            for (final Aspect asp2 : nd.getAspectsBase().getAspects()) {
                if (ResearchManager.getCombinationResult(asp, asp2) != null) {
                    possibleCombos.add(ResearchManager.getCombinationResult(asp, asp2), 1);
                }
            }
        }
        if (possibleCombos.size() > 0 && possibleCombos.getAspects()[0] != null) {
            this.doMerge(possibleCombos, nd, tile, x, y, z);
        }
    }

    public void doMerge(final AspectList possibleCombos, final TileNode nd, final TileNode tile, final int x,
            final int y, final int z) {
        final int which = this.world.rand.nextInt(possibleCombos.getAspects().length);
        final Aspect toAdd = possibleCombos.getAspects()[which];
        tile.getAspectsBase().add(toAdd, 1);
        tile.getAspects().add(toAdd, 1);
        Aspect aspA;
        Aspect aspB;
        if (tile.getAspectsBase().getAmount(toAdd.getComponents()[0]) > 0) {
            aspA = toAdd.getComponents()[0];
            aspB = toAdd.getComponents()[1];
        } else {
            aspA = toAdd.getComponents()[1];
            aspB = toAdd.getComponents()[0];
        }
        tile.getAspectsBase().remove(aspA, 1);
        tile.getAspects().remove(aspA, 1);
        nd.getAspects().remove(aspB, 1);
        if (this.world.rand.nextInt(3) == 0) {
            nd.setNodeVisBase(aspB, (short) (nd.getNodeVisBase(aspB) - 1));
        }
        this.world.notifyBlockUpdate(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ() + z), this.world.getBlockState(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ() + z)), this.world.getBlockState(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ() + z)), 3);
        nd.markDirty();
        this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ())), 3);
        tile.markDirty();
        PacketHandler.INSTANCE.sendToAllAround(
                new PacketFXBlockZap(
                        this.pos.getX() + x + 0.5f,
                        this.pos.getY() + y + 0.5f,
                        this.pos.getZ() + z + 0.5f,
                        this.pos.getX() + 0.5f,
                        this.pos.getY() + 0.5f,
                        this.pos.getZ() + 0.5f),
                new NetworkRegistry.TargetPoint(
                        this.world.provider.getDimension(),
                        this.pos.getX(),
                        this.pos.getY(),
                        this.pos.getZ(),
                        32.0));
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setBoolean("active", this.activated);
        nbttagcompound.setBoolean("shouldactivate", this.shouldActivate);
        nbttagcompound.setInteger("count", this.count);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.activated = nbttagcompound.getBoolean("active");
        this.shouldActivate = nbttagcompound.getBoolean("shouldactivate");
        this.count = nbttagcompound.getInteger("count");
    }
}
