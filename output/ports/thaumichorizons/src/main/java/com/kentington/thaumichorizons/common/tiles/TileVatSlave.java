//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;


import net.minecraft.entity.player.EntityPlayer;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;

public class TileVatSlave extends TileThaumcraft implements IAspectContainer {

    boolean bossFound;
    int bossX;
    int bossY;
    int bossZ;
    int renderMe;

    public boolean activate(final EntityPlayer player) {
        final TileVat boss = this.getBoss(-1);
        if (boss == null) {
            return false;
        }
        if (this.world.getBlockState(this.pos).getBlock().getMetaFromState(this.world.getBlockState(this.pos)) != 0) {
            return boss.activate(player, false);
        }
        return boss.activate(player, true);
    }

    /** Resolve the ORIGINAL vat layout (metadata 0, 4, 5, 6, 10). */
    public TileVat getBoss(final int mdOverride) {
        if (world == null) return null;
        if (bossFound) {
            net.minecraft.tileentity.TileEntity cached = world.getTileEntity(new BlockPos(bossX, bossY, bossZ));
            if (cached instanceof TileVat && !((TileVat)cached).isDisassembling()) return (TileVat) cached;
            bossFound = false; // a dismantled or unloaded structure is not a valid owner
        }
        int meta = mdOverride == -1 ? metadata(pos) : mdOverride;
        if (meta == 0) {
            TileVat vat = vatAt(pos.up());
            if (vat == null) vat = vatAt(pos.up(2));
            return remember(vat);
        }
        if (meta == 10) {
            for (int x=-1; x<=1; x++) for (int z=-1; z<=1; z++) {
                BlockPos neighbor = pos.add(x, 0, z);
                if (world.getBlockState(neighbor).getBlock()==ThaumicHorizons.blockVatInterior
                        && metadata(neighbor)==0) {
                    net.minecraft.tileentity.TileEntity te=world.getTileEntity(neighbor);
                    if (te instanceof TileVatSlave && te != this) {
                        TileVat vat=((TileVatSlave)te).getBoss(-1);
                        if (vat!=null) return remember(vat);
                    }
                }
            }
        } else if (meta == 4) {
            BlockPos neighbor = pos.up();
            if (world.getBlockState(neighbor).getBlock()==ThaumicHorizons.blockVat) {
                net.minecraft.tileentity.TileEntity te=world.getTileEntity(neighbor);
                if (te instanceof TileVatSlave && te!=this) return remember(((TileVatSlave)te).getBoss(-1));
            }
        } else if (meta == 5) {
            for (BlockPos neighbor : new BlockPos[] {pos.up(), pos.down()}) {
                if (world.getBlockState(neighbor).getBlock()==ThaumicHorizons.blockVat
                        && metadata(neighbor)==10) {
                    net.minecraft.tileentity.TileEntity te=world.getTileEntity(neighbor);
                    if (te instanceof TileVatSlave && te!=this) {
                        TileVat vat=((TileVatSlave)te).getBoss(-1);
                        if (vat!=null) return remember(vat);
                    }
                }
            }
        } else if (meta == 6) {
            return remember(vatAt(pos.up(3)));
        }
        return null;
    }

    private int metadata(BlockPos position) {
        net.minecraft.block.state.IBlockState state=world.getBlockState(position);
        return state.getBlock().getMetaFromState(state);
    }

    private TileVat vatAt(BlockPos position) {
        net.minecraft.tileentity.TileEntity te=world.getTileEntity(position);
        return te instanceof TileVat && !((TileVat)te).isDisassembling() ? (TileVat)te : null;
    }

    private TileVat remember(TileVat vat) {
        if (vat!=null && !vat.isDisassembling()) {
            bossX=vat.getPos().getX();
            bossY=vat.getPos().getY();
            bossZ=vat.getPos().getZ();
            bossFound=true;
        }
        return vat;
    }

    public void killMyBoss(final int mdOverride) {
        final TileVat boss = this.getBoss(mdOverride);
        if (boss != null) {
            boss.killMe();
        }
    }

    @Override
    public AspectList getAspects() {
        final TileVat boss = this.getBoss(-1);
        if (boss != null) {
            return boss.getAspects();
        }
        return null;
    }

    @Override
    public void setAspects(final AspectList aspects) {}

    @Override
    public boolean doesContainerAccept(final Aspect tag) {
        return false;
    }

    @Override
    public int addToContainer(final Aspect tag, final int amount) {
        return 0;
    }

    @Override
    public boolean takeFromContainer(final Aspect tag, final int amount) {
        return false;
    }

    @Override
    public boolean takeFromContainer(final AspectList ot) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(final Aspect tag, final int amount) {
        return false;
    }

    @Override
    public boolean doesContainerContain(final AspectList ot) {
        return false;
    }

    @Override
    public int containerContains(final Aspect tag) {
        return 0;
    }
}
