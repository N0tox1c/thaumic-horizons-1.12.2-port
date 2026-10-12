//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;


import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.blocks.BlockTransductionAmplifier;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockZap;
import thaumcraft.common.tiles.TileNode;
import thaumcraft.common.tiles.TileNodeConverter;
import thaumcraft.common.tiles.TileNodeEnergized;

public class TileTransductionAmplifier extends TileThaumcraft  implements net.minecraft.util.ITickable {

    public int count;
    public byte direction;
    public boolean activated;
    public boolean shouldActivate;
    boolean lastActivated;
    boolean fireOnce;

    public TileTransductionAmplifier() {
        this.count = -1;
        this.direction = -1;
        this.activated = false;
        this.shouldActivate = false;
        this.fireOnce = false;
    }

    public void update() {

        if (this.world.provider.getDimension() == ThaumicHorizons.dimensionPocketId) {
            this.world.createExplosion(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), 1.0f, false);
        }

        if (!this.fireOnce) {
            this.direction = (byte) this.getBlockMetadata();
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
            if (this.direction == -1) {
                this.direction = (byte) this.getBlockMetadata();
            }
            final EnumFacing dir = EnumFacing.byIndex(this.direction);
            if (dir == EnumFacing.UP && this.world
                    .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ())) instanceof TileNodeEnergized) {
                this.boostNode(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ());
            } else if (dir == EnumFacing.DOWN && this.world
                    .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ())) instanceof TileNodeEnergized) {
                        this.boostNode(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
                    } else
                if (dir == EnumFacing.NORTH && this.world
                        .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1)) instanceof TileNodeEnergized) {
                            this.boostNode(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1);
                        } else
                    if (dir == EnumFacing.SOUTH && this.world
                            .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1)) instanceof TileNodeEnergized) {
                                this.boostNode(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1);
                            } else
                        if (dir == EnumFacing.WEST && this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ())) instanceof TileNodeEnergized) {
                                    this.boostNode(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ());
                                } else
                            if (dir == EnumFacing.EAST && this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ())) instanceof TileNodeEnergized) {
                                        this.boostNode(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ());
                                    } else {
                                        ((BlockTransductionAmplifier) ThaumicHorizons.blockTransducer)
                                                .killMe(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), true);
                                        this.world.setBlockToAir(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
                                    }
            this.activated = true;
            this.markDirty();
            this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
        } else if (!this.shouldActivate && this.activated) {
            if (this.direction == -1) {
                this.direction = (byte) this.getBlockMetadata();
            }
            final EnumFacing dir = EnumFacing.byIndex(this.direction);
            if (dir == EnumFacing.UP && this.world
                    .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ())) instanceof TileNodeEnergized) {
                this.unboostNode(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ());
            } else if (dir == EnumFacing.DOWN && this.world
                    .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ())) instanceof TileNodeEnergized) {
                        this.unboostNode(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
                    } else
                if (dir == EnumFacing.NORTH && this.world
                        .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1)) instanceof TileNodeEnergized) {
                            this.unboostNode(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1);
                        } else
                    if (dir == EnumFacing.SOUTH && this.world
                            .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1)) instanceof TileNodeEnergized) {
                                this.unboostNode(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1);
                            } else
                        if (dir == EnumFacing.WEST && this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ())) instanceof TileNodeEnergized) {
                                    this.unboostNode(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ());
                                } else
                            if (dir == EnumFacing.EAST && this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ())) instanceof TileNodeEnergized) {
                                        this.unboostNode(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ());
                                    } else {
                                        ((BlockTransductionAmplifier) ThaumicHorizons.blockTransducer)
                                                .killMe(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), true);
                                        this.world.setBlockToAir(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
                                    }
            this.activated = false;
            this.markDirty();
            this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
        }
        if (!this.world.isRemote && this.activated && this.count % 10 == 0) {
            int ecks = this.pos.getX();
            final int why = this.pos.getY();
            int zee = this.pos.getZ();
            final EnumFacing dir2 = EnumFacing.byIndex(this.direction);
            if (dir2 == EnumFacing.NORTH && (this.world
                    .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1)) instanceof TileNodeEnergized
                    || this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1)) instanceof TileNode)) {
                ++zee;
            } else if (dir2 == EnumFacing.SOUTH && (this.world
                    .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1)) instanceof TileNodeEnergized
                    || this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1)) instanceof TileNode)) {
                        --zee;
                    } else
                if (dir2 == EnumFacing.WEST && (this.world
                        .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ())) instanceof TileNodeEnergized
                        || this.world
                                .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ())) instanceof TileNode)) {
                                    ++ecks;
                                } else
                    if (dir2 == EnumFacing.EAST && (this.world
                            .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ())) instanceof TileNodeEnergized
                            || this.world
                                    .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ())) instanceof TileNode)) {
                                        --ecks;
                                    }
            int transducers = 0;
            if (this.world.getBlockState(new BlockPos(ecks + 1, why, zee)).getBlock() == ThaumicHorizons.blockTransducer
                    && ((TileTransductionAmplifier) this.world.getTileEntity(new net.minecraft.util.math.BlockPos(ecks + 1, why, zee))).activated) {
                ++transducers;
            }
            if (this.world.getBlockState(new BlockPos(ecks - 1, why, zee)).getBlock() == ThaumicHorizons.blockTransducer
                    && ((TileTransductionAmplifier) this.world.getTileEntity(new net.minecraft.util.math.BlockPos(ecks - 1, why, zee))).activated) {
                ++transducers;
            }
            if (this.world.getBlockState(new BlockPos(ecks, why, zee + 1)).getBlock() == ThaumicHorizons.blockTransducer
                    && ((TileTransductionAmplifier) this.world.getTileEntity(new net.minecraft.util.math.BlockPos(ecks, why, zee + 1))).activated) {
                ++transducers;
            }
            if (this.world.getBlockState(new BlockPos(ecks, why, zee - 1)).getBlock() == ThaumicHorizons.blockTransducer
                    && ((TileTransductionAmplifier) this.world.getTileEntity(new net.minecraft.util.math.BlockPos(ecks, why, zee - 1))).activated) {
                ++transducers;
            }
            if (transducers > 3 && this.count % 50 == 0) {
                this.unboostNode(ecks, why, zee);
                ((TileNodeConverter) this.world.getTileEntity(new net.minecraft.util.math.BlockPos(ecks, why + 1, zee))).status = -1;
                final AspectList aspects = ((TileNodeEnergized) this.world.getTileEntity(new BlockPos(ecks, why, zee)))
                        .getAuraBase().copy();
                this.world.setBlockState(new BlockPos(ecks, why, zee), ThaumicHorizons.blockVortex.getStateFromMeta(0), 3);
                ((TileVortex) this.world.getTileEntity(new BlockPos(ecks, why, zee))).aspects = aspects;
            }
            if (transducers > 2 && this.world.rand.nextInt(4) == 2 && this.count % 50 == 0) {
                final int dx = this.world.rand.nextInt(16) - 8;
                final int dy = this.world.rand.nextInt(16) - 8;
                final int dz = this.world.rand.nextInt(16) - 8;
                if (this.world.isAirBlock(new BlockPos(this.pos.getX() + dx, this.pos.getY() + dy, this.pos.getZ() + dz))) {
                    PacketHandler.INSTANCE.sendToAllAround(
                            new PacketFXBlockZap(
                                    this.pos.getX() + 0.5f,
                                    this.pos.getY() + 0.5f,
                                    this.pos.getZ() + 0.5f,
                                    this.pos.getX() + 0.5f + dx,
                                    this.pos.getY() + 0.5f + dy,
                                    this.pos.getZ() + 0.5f + dz),
                            new NetworkRegistry.TargetPoint(
                                    this.world.provider.getDimension(),
                                    this.pos.getX(),
                                    this.pos.getY(),
                                    this.pos.getZ(),
                                    32.0));
                    if (dy > 0) {
                        this.world.setBlockState(new BlockPos(this.pos.getX() + dx, this.pos.getY() + dy, this.pos.getZ() + dz), ConfigBlocks.blockFluxGas.getDefaultState(), 3);
                    } else {
                        this.world.setBlockState(new BlockPos(this.pos.getX() + dx, this.pos.getY() + dy, this.pos.getZ() + dz), ConfigBlocks.blockFluxGoo.getDefaultState(), 3);
                    }
                }
            }
            if (transducers > 1 && this.count % 50 == 0) {
                final List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(
                        EntityLivingBase.class,
                        new AxisAlignedBB(
                                this.pos.getX(),
                                this.pos.getY(),
                                this.pos.getZ(),
                                this.pos.getX() + 1,
                                this.pos.getY() + 1,
                                this.pos.getZ() + 1).expand(10.0, 10.0, 10.0));
                if (targets != null && !targets.isEmpty()) {
                    for (final Entity target : targets) {
                        PacketHandler.INSTANCE.sendToAllAround(
                                new PacketFXBlockZap(
                                        this.pos.getX() + 0.5f,
                                        this.pos.getY() + 0.5f,
                                        this.pos.getZ() + 0.5f,
                                        (float) target.posX,
                                        (float) target.posY + target.height / 2.0f,
                                        (float) target.posZ),
                                new NetworkRegistry.TargetPoint(
                                        this.world.provider.getDimension(),
                                        this.pos.getX(),
                                        this.pos.getY(),
                                        this.pos.getZ(),
                                        32.0));
                        target.attackEntityFrom(DamageSource.MAGIC, (float) (4 + this.world.rand.nextInt(1)));
                    }
                }
            }
            if (this.world.getTileEntity(new BlockPos(ecks, why, zee)) instanceof TileNode) {
                for (int i = 0; i < transducers; ++i) {
                    this.unboostNode(ecks, why, zee);
                }
                this.activated = false;
                this.markDirty();
                this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
            }
        }
    }

    void boostNode(final int x, final int y, final int z) {
        final TileEntity tyle = this.world.getTileEntity(new BlockPos(x, y, z));
        if (tyle instanceof TileNodeEnergized) { TileNodeEnergized node = (TileNodeEnergized) tyle;
            final AspectList baseVis = node.getAuraBase();
            for (final Aspect asp : baseVis.getAspects()) {
                baseVis.add(asp, 10);
            }
            node.setAspects(baseVis);
            node.setupNode();
        }
    }

    public void unBoostNode(final int x, final int y, final int z) {
        final EnumFacing dir = EnumFacing.byIndex(this.direction);
        if (dir == EnumFacing.UP && this.world
                .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ())) instanceof TileNodeEnergized) {
            this.unboostNode(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ());
        } else if (dir == EnumFacing.DOWN && this.world
                .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ())) instanceof TileNodeEnergized) {
                    this.unboostNode(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
                } else
            if (dir == EnumFacing.NORTH && this.world
                    .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1)) instanceof TileNodeEnergized) {
                        this.unboostNode(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1);
                    } else
                if (dir == EnumFacing.SOUTH && this.world
                        .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1)) instanceof TileNodeEnergized) {
                            this.unboostNode(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1);
                        } else
                    if (dir == EnumFacing.WEST && this.world
                            .getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ())) instanceof TileNodeEnergized) {
                                this.unboostNode(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ());
                            } else
                        if (dir == EnumFacing.EAST && this.world.getTileEntity(new net.minecraft.util.math.BlockPos(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ())) instanceof TileNodeEnergized) {
                                    this.unboostNode(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ());
                                }
    }

    void unboostNode(final int x, final int y, final int z) {
        final TileEntity tyle = this.world.getTileEntity(new BlockPos(x, y, z));
        if (tyle instanceof TileNodeEnergized) { TileNodeEnergized node = (TileNodeEnergized) tyle;
            final AspectList baseVis = node.getAuraBase();
            for (final Aspect asp : baseVis.getAspects()) {
                baseVis.remove(asp, 10);
            }
            node.setAspects(baseVis);
            node.setupNode();
        } else if (tyle instanceof TileNode) { TileNode node2 = (TileNode) tyle;
            final AspectList baseVis = node2.getAspectsBase();
            for (final Aspect asp : baseVis.getAspects()) {
                baseVis.remove(asp, 10);
            }
            node2.setAspects(baseVis);
        }
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setBoolean("active", this.activated);
        nbttagcompound.setByte("dir", this.direction);
        nbttagcompound.setBoolean("shouldactivate", this.shouldActivate);
        nbttagcompound.setInteger("count", this.count);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.activated = nbttagcompound.getBoolean("active");
        this.direction = nbttagcompound.getByte("dir");
        this.shouldActivate = nbttagcompound.getBoolean("shouldactivate");
        this.count = nbttagcompound.getInteger("count");
    }
}
