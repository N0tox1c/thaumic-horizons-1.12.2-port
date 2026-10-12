//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.lib.PocketPlaneData;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TileSlot extends TileThaumcraft {

    public boolean hasKeystone;
    public boolean portalOpen;
    public int pocketID;
    public int which;
    public boolean xAligned;

    public TileSlot() {
        this.hasKeystone = false;
        this.portalOpen = false;
        this.pocketID = 0;
        this.which = 0;
        this.xAligned = false;
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setInteger("pocketPlaneID", this.pocketID);
        nbttagcompound.setInteger("which", this.which);
        nbttagcompound.setBoolean("hasKeystone", this.hasKeystone);
        nbttagcompound.setBoolean("portalOpen", this.portalOpen);
        nbttagcompound.setBoolean("aligned", this.xAligned);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.pocketID = nbttagcompound.getInteger("pocketPlaneID");
        this.which = nbttagcompound.getInteger("which");
        this.hasKeystone = nbttagcompound.getBoolean("hasKeystone");
        this.portalOpen = nbttagcompound.getBoolean("portalOpen");
        this.xAligned = nbttagcompound.getBoolean("aligned");
    }

    public void destroyPortal() {
        if (this.world.isRemote) {
            return;
        }
        if (this.xAligned) {
            this.world.setBlockState(new BlockPos(this.pos.getX() - 2, this.pos.getY(), this.pos.getZ()), ConfigBlocks.blockMetalDevice.getStateFromMeta(3), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() + 2, this.pos.getY(), this.pos.getZ()), ConfigBlocks.blockMetalDevice.getStateFromMeta(3), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() - 2, this.pos.getY() - 4, this.pos.getZ()), ConfigBlocks.blockMetalDevice.getStateFromMeta(3), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() + 2, this.pos.getY() - 4, this.pos.getZ()), ConfigBlocks.blockMetalDevice.getStateFromMeta(3), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() - 1, this.pos.getY() - 4, this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 4, this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() + 1, this.pos.getY() - 4, this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() - 2, this.pos.getY() - 1, this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() - 2, this.pos.getY() - 2, this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() - 2, this.pos.getY() - 3, this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() + 2, this.pos.getY() - 1, this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() + 2, this.pos.getY() - 2, this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX() + 2, this.pos.getY() - 3, this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y >= -3; --y) {
                    this.world.setBlockToAir(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ()));
                }
            }
            for (int x = -2; x <= 2; ++x) {
                for (int y = 0; y >= -4; --y) {
                    this.world.notifyBlockUpdate(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ())), 3);
                }
            }
        } else {
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 2), ConfigBlocks.blockMetalDevice.getStateFromMeta(3), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 2), ConfigBlocks.blockMetalDevice.getStateFromMeta(3), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 4, this.pos.getZ() - 2), ConfigBlocks.blockMetalDevice.getStateFromMeta(3), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 4, this.pos.getZ() + 2), ConfigBlocks.blockMetalDevice.getStateFromMeta(3), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 4, this.pos.getZ() - 1), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 4, this.pos.getZ()), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 4, this.pos.getZ() + 1), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ() - 2), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 2, this.pos.getZ() - 2), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 3, this.pos.getZ() - 2), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ() + 2), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 2, this.pos.getZ() + 2), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 3, this.pos.getZ() + 2), ConfigBlocks.blockCosmeticSolid.getStateFromMeta(11), 1);
            for (int z = -1; z <= 1; ++z) {
                for (int y = -1; y >= -3; --y) {
                    this.world.setBlockToAir(new BlockPos(this.pos.getX(), this.pos.getY() + y, this.pos.getZ() + z));
                }
            }
            for (int z = -2; z <= 2; ++z) {
                for (int y = 0; y >= -4; --y) {
                    this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY() + y, this.pos.getZ() + z), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY() + y, this.pos.getZ() + z)), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY() + y, this.pos.getZ() + z)), 3);
                }
            }
        }
        PocketPlaneData.destroyPortal(this.pocketID, this.which);
        com.kentington.thaumichorizons.common.lib.THSoundBridge.play(this.world, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, "thaumcraft:craftfail", 1.0f, 1.0f);
        this.portalOpen = false;
        this.markDirty();
        this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
    }

    public void makePortal(final EntityPlayer player) {
        if (this.world.isRemote) {
            return;
        }
        final int portalNum = PocketPlaneData.firstAvailablePortal(this.pocketID);
        final ItemWandCasting wand = (ItemWandCasting) player.getHeldItemMainhand().getItem();
        if (portalNum != 0 && wand.consumeAllVisCrafting(
                player.getHeldItemMainhand(),
                player,
                new AspectList().add(Aspect.WATER, 250).add(Aspect.EARTH, 250).add(Aspect.ORDER, 250)
                        .add(Aspect.FIRE, 250).add(Aspect.AIR, 250).add(Aspect.ENTROPY, 250),
                false)) {
            boolean madePortal = false;
            if (this.checkPortalX()) {
                this.xAligned = true;
                int md = 0;
                for (int x = -2; x <= 2; ++x) {
                    for (int y = 0; y >= -4; --y) {
                        if (!this.world.isAirBlock(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ()))
                                && (x != 0 || y != 0)) {
                            this.world.setBlockState(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ()), ThaumicHorizons.blockGateway.getStateFromMeta(md), 3);
                            ++md;
                        } else if (x != 0 || y != 0) {
                            this.world.setBlockState(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ()), ThaumicHorizons.blockPortal.getStateFromMeta(0), 3);
                        }
                    }
                }
                madePortal = true;
            } else if (this.checkPortalZ()) {
                this.xAligned = false;
                int md = 0;
                for (int z = -2; z <= 2; ++z) {
                    for (int y = 0; y >= -4; --y) {
                        if (!this.world.isAirBlock(new BlockPos(this.pos.getX(), this.pos.getY() + y, this.pos.getZ() + z))
                                && (z != 0 || y != 0)) {
                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() + y, this.pos.getZ() + z), ThaumicHorizons.blockGateway.getStateFromMeta(md), 3);
                            ++md;
                        } else if (z != 0 || y != 0) {
                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() + y, this.pos.getZ() + z), ThaumicHorizons.blockPortal.getStateFromMeta(0), 3);
                        }
                    }
                }
                madePortal = true;
            }
            if (madePortal) {
                PocketPlaneData planeData = PocketPlaneData.planes.get(this.pocketID);
                int[] portal;
                switch (portalNum) {
    case 1: {
                        planeData.portalA = new int[4];
                        portal = planeData.portalA;
                    } break;
    case 2: {
                        planeData.portalB = new int[4];
                        portal = planeData.portalB;
                    } break;
    case 3: {
                        planeData.portalC = new int[4];
                        portal = planeData.portalC;
                    } break;
    case 4: {
                        planeData.portalD = new int[4];
                        portal = planeData.portalD;
                    } break;
    default: {
                        return;
                    }
}
                portal[0] = this.pos.getX();
                portal[1] = this.pos.getY();
                portal[2] = this.pos.getZ();
                portal[3] = player.dimension;

                PocketPlaneData.makePortal(this.pocketID, portalNum, this.pos.getX(), this.pos.getY(), this.pos.getZ());
                this.which = portalNum;
                this.portalOpen = true;
                wand.consumeAllVisCrafting(
                        player.getHeldItemMainhand(),
                        player,
                        new AspectList().add(Aspect.WATER, 250).add(Aspect.EARTH, 250).add(Aspect.ORDER, 250)
                                .add(Aspect.FIRE, 250).add(Aspect.AIR, 250).add(Aspect.ENTROPY, 250),
                        true);
                com.kentington.thaumichorizons.common.lib.THSoundBridge.play(this.world, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, "thaumcraft:wand", 1.0f, 1.0f);
                this.markDirty();
                this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
            }
        }
    }

    boolean checkPortalX() {
        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y >= -3; --y) {
                if (!this.world.isAirBlock(new BlockPos(this.pos.getX() + x, this.pos.getY() + y, this.pos.getZ()))) {
                    return false;
                }
            }
        }

        return checkBlocksX(this.pos.getX(), this.pos.getY(), this.pos.getZ());
    }

    boolean checkPortalZ() {
        for (int z = -1; z <= 1; ++z) {
            for (int y = -1; y >= -3; --y) {
                if (!this.world.isAirBlock(new BlockPos(this.pos.getX(), this.pos.getY() + y, this.pos.getZ() + z))) {
                    return false;
                }
            }
        }

        return checkBlocksZ(this.pos.getX(), this.pos.getY(), this.pos.getZ());
    }

    boolean checkBlocksX(int x, int y, int z) {
        boolean isValid = true;

        isValid &= checkCornerBlock(x - 2, y, z);
        isValid &= checkCornerBlock(x + 2, y, z);
        isValid &= checkCornerBlock(x - 2, y - 4, z);
        isValid &= checkCornerBlock(x + 2, y - 4, z);

        isValid &= checkEdgeBlock(x - 1, y, z);
        isValid &= checkEdgeBlock(x + 1, y, z);
        isValid &= checkEdgeBlock(x - 2, y - 1, z);
        isValid &= checkEdgeBlock(x + 2, y - 1, z);
        isValid &= checkEdgeBlock(x - 2, y - 2, z);
        isValid &= checkEdgeBlock(x + 2, y - 2, z);
        isValid &= checkEdgeBlock(x - 2, y - 3, z);
        isValid &= checkEdgeBlock(x + 2, y - 3, z);
        isValid &= checkEdgeBlock(x - 1, y - 4, z);
        isValid &= checkEdgeBlock(x, y - 4, z);
        isValid &= checkEdgeBlock(x + 1, y - 4, z);

        return isValid;
    }

    boolean checkBlocksZ(int x, int y, int z) {
        boolean isValid = true;

        isValid &= checkCornerBlock(x, y, z - 2);
        isValid &= checkCornerBlock(x, y, z + 2);
        isValid &= checkCornerBlock(x, y - 4, z - 2);
        isValid &= checkCornerBlock(x, y - 4, z + 2);

        isValid &= checkEdgeBlock(x, y, z - 1);
        isValid &= checkEdgeBlock(x, y, z + 1);
        isValid &= checkEdgeBlock(x, y - 1, z - 2);
        isValid &= checkEdgeBlock(x, y - 1, z + 2);
        isValid &= checkEdgeBlock(x, y - 2, z - 2);
        isValid &= checkEdgeBlock(x, y - 2, z + 2);
        isValid &= checkEdgeBlock(x, y - 3, z - 2);
        isValid &= checkEdgeBlock(x, y - 3, z + 2);
        isValid &= checkEdgeBlock(x, y - 4, z - 1);
        isValid &= checkEdgeBlock(x, y - 4, z);
        isValid &= checkEdgeBlock(x, y - 4, z + 1);

        return isValid;
    }

    boolean checkCornerBlock(final int x, final int y, final int z) {
        return this.world.getBlockState(new BlockPos(x, y, z)).getBlock() == ConfigBlocks.blockMetalDevice
                && this.world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(this.world.getBlockState(new BlockPos(x, y, z))) == 3;
    }

    boolean checkEdgeBlock(final int x, final int y, final int z) {
        return this.world.getBlockState(new BlockPos(x, y, z)).getBlock() == ConfigBlocks.blockCosmeticSolid
                && this.world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(this.world.getBlockState(new BlockPos(x, y, z))) == 11;
    }

    public void insertKeystone(final int pocketNum) {
        this.hasKeystone = true;
        this.pocketID = pocketNum;
        this.markDirty();
        this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
    }

    public int removeKeystone() {
        this.hasKeystone = false;
        this.markDirty();
        this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
        return this.pocketID;
    }
}
