//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;


import java.util.Random;

import net.minecraft.block.BlockBreakable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.lib.GatewayTeleporter;
import com.kentington.thaumichorizons.common.lib.PocketPlaneData;
import com.kentington.thaumichorizons.common.tiles.TileSlot;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPortalTH extends LegacyBlockBreakable {

    public BlockPortalTH() {
        super("portal", ThaumicHorizons.portal, false);
        this.setTickRandomly(true);
        this.setBlockUnbreakable();
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World p_149668_1_, final int p_149668_2_,
            final int p_149668_3_, final int p_149668_4_) {
        return null;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World p_149734_1_, final int p_149734_2_, final int p_149734_3_,
            final int p_149734_4_, final Random p_149734_5_) {
        for (int l = 0; l < 4; ++l) {
            double d0 = p_149734_2_ + p_149734_5_.nextFloat();
            final double d2 = p_149734_3_ + p_149734_5_.nextFloat();
            double d3 = p_149734_4_ + p_149734_5_.nextFloat();
            double d4 = 0.0;
            double d5 = 0.0;
            double d6 = 0.0;
            final int i1 = p_149734_5_.nextInt(2) * 2 - 1;
            d4 = (p_149734_5_.nextFloat() - 0.5) * 0.5;
            d5 = (p_149734_5_.nextFloat() - 0.5) * 0.5;
            d6 = (p_149734_5_.nextFloat() - 0.5) * 0.5;
            if (p_149734_1_.getBlockState(new net.minecraft.util.math.BlockPos(p_149734_2_ - 1, p_149734_3_, p_149734_4_)).getBlock() != this
                    && p_149734_1_.getBlockState(new net.minecraft.util.math.BlockPos(p_149734_2_ + 1, p_149734_3_, p_149734_4_)).getBlock() != this) {
                d0 = p_149734_2_ + 0.5 + 0.25 * i1;
                d4 = p_149734_5_.nextFloat() * 2.0f * i1;
            } else {
                d3 = p_149734_4_ + 0.5 + 0.25 * i1;
                d6 = p_149734_5_.nextFloat() * 2.0f * i1;
            }
            p_149734_1_.spawnParticle(net.minecraft.util.EnumParticleTypes.PORTAL, d0, d2, d3, d4, d5, d6);
        }
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(final World p_149694_1_, final int p_149694_2_, final int p_149694_3_, final int p_149694_4_) {
        return null;
    }

    public int quantityDropped(final Random p_149745_1_) {
        return 0;
    }

    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z,
            final Entity player) {
        if (!world.isRemote && !player.isRiding() && !player.isBeingRidden() && player instanceof EntityPlayerMP) {
            final EntityPlayerMP playerMP = ((EntityPlayerMP) (player));

            if (playerMP.timeUntilPortal > 0) {
                playerMP.timeUntilPortal = 10;
                return;
            }
            playerMP.timeUntilPortal = 10;
            int targetX = 0;
            int targetY = 0;
            int targetZ = 0;
            int targetDim = 0;
            if (world.provider.getDimension() == ThaumicHorizons.dimensionPocketId) {
                final int planeNum = (z + 128) / 256;
                final int which = world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));
                final PocketPlaneData plane = PocketPlaneData.planes.get(planeNum);
                final int[] portal;

                // Note the order ACBD
                switch (which) {
    case 0: portal = plane.portalA; break;
    case 1: portal = plane.portalC; break;
    case 2: portal = plane.portalB; break;
    case 3: portal = plane.portalD; break;
    default: {
                        return;
                    }
}

                targetX = portal[0];
                targetY = portal[1] - 2;
                targetZ = portal[2];
                if (portal.length > 3) targetDim = portal[3];

                final MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
                final WorldServer targetWorld = mServer.getWorld(targetDim);
                float exitYaw = playerMP.rotationYaw;
                // face perpendicular to portal plane, away from portal into the world
                if (targetWorld.getTileEntity(new net.minecraft.util.math.BlockPos(targetX, targetY + 2, targetZ)) instanceof TileSlot) {
            final TileSlot slotTE = ((TileSlot) (targetWorld.getTileEntity(new net.minecraft.util.math.BlockPos(targetX, targetY + 2, targetZ))));

                    exitYaw = slotTE.xAligned ? 0.0f : 270.0f;
                }
                if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(playerMP, targetDim)) return;
                playerMP.getServer().getPlayerList().transferPlayerToDimension(
                        playerMP,
                        targetDim,
                        new GatewayTeleporter(
                                targetWorld,
                                targetX,
                                targetY,
                                targetZ,
                                exitYaw));
            } else {
                int slotY = y;
                int slotX = x;
                int slotZ = z;
                do {
                    ++slotY;
                } while (world.getBlockState(new BlockPos(slotX, slotY, slotZ)).getBlock() == ThaumicHorizons.blockPortal);
                if (world.getBlockState(new BlockPos(slotX, slotY, slotZ)).getBlock() == ThaumicHorizons.blockGateway) {
                    if (world.getBlockState(new BlockPos(slotX + 1, slotY, slotZ)).getBlock() == ThaumicHorizons.blockSlot) {
                        ++slotX;
                    } else if (world.getBlockState(new BlockPos(slotX - 1, slotY, slotZ)).getBlock() == ThaumicHorizons.blockSlot) {
                        --slotX;
                    } else if (world.getBlockState(new BlockPos(slotX, slotY, slotZ + 1)).getBlock() == ThaumicHorizons.blockSlot) {
                        ++slotZ;
                    } else if (world.getBlockState(new BlockPos(slotX, slotY, slotZ - 1)).getBlock() == ThaumicHorizons.blockSlot) {
                        --slotZ;
                    }
                }
                final TileEntity te = world.getTileEntity(new BlockPos(slotX, slotY, slotZ));
                if (te instanceof TileSlot) { TileSlot tco = (TileSlot) te;
                    targetY = 128;
                    float targetYaw = 0.0f;
                    switch (tco.which) {
    case 1: {
                            targetZ = tco.pocketID * 256 + PocketPlaneData.planes.get(tco.pocketID).radius;
                            targetYaw = 180.0f;
                        } break;
    case 2: {
                            targetZ = tco.pocketID * 256 - PocketPlaneData.planes.get(tco.pocketID).radius;
                        } break;
    case 3: {
                            targetZ = tco.pocketID * 256;
                            targetX = PocketPlaneData.planes.get(tco.pocketID).radius;
                            targetYaw = 90.0f;
                        } break;
    case 4: {
                            targetZ = tco.pocketID * 256;
                            targetX = -PocketPlaneData.planes.get(tco.pocketID).radius;
                            targetYaw = 270.0f;
                        } break;
}
                    final MinecraftServer mServer2 = FMLCommonHandler.instance().getMinecraftServerInstance();
                    if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(playerMP, ThaumicHorizons.dimensionPocketId)) return;
                    playerMP.getServer().getPlayerList().transferPlayerToDimension(
                            playerMP,
                            ThaumicHorizons.dimensionPocketId,
                            new GatewayTeleporter(
                                    mServer2.getWorld(ThaumicHorizons.dimensionPocketId),
                                    targetX,
                                    targetY,
                                    targetZ,
                                    targetYaw));
                }
            }
        }
    }

    @Override
    public void randomDisplayTick(net.minecraft.block.state.IBlockState state,World world,net.minecraft.util.math.BlockPos pos,Random random) {
        randomDisplayTick(world,pos.getX(), pos.getY(), pos.getZ(),random);
    }

    @Override
    public void onEntityCollision(World world,net.minecraft.util.math.BlockPos pos,net.minecraft.block.state.IBlockState state,Entity entity) {
        onEntityCollidedWithBlock(world,pos.getX(), pos.getY(), pos.getZ(),entity);
    }

    @Override
    public net.minecraft.util.math.AxisAlignedBB getCollisionBoundingBox(net.minecraft.block.state.IBlockState state,IBlockAccess world,net.minecraft.util.math.BlockPos pos) {
        if (!(world instanceof World)) return getBoundingBox(state,world,pos);
        net.minecraft.util.math.AxisAlignedBB box=getCollisionBoundingBoxFromPool((World)world,pos.getX(), pos.getY(), pos.getZ());
        return box==null ? null : box.offset(-pos.getX(),-pos.getY(),-pos.getZ());
    }
}
