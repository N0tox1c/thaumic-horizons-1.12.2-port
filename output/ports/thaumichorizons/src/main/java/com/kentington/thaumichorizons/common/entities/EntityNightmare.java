//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.lib.NightmareTeleporter;
import com.kentington.thaumichorizons.common.lib.networking.PacketHandler;
import com.kentington.thaumichorizons.common.lib.networking.PacketMountNightmare;

public class EntityNightmare extends EntityEndersteed {

    NightmareTeleporter nightmareTeleporterOverworld;
    NightmareTeleporter nightmareTeleporterNether;

    public EntityNightmare(final World p_i1685_1_) {
        super(p_i1685_1_);
        this.isImmuneToFire = true;

    }

    @Override
    public void setJumpPower(final int p_110206_1_) {
        final double blocks = p_110206_1_ / 7.0;
        if (p_110206_1_ < 90 || (this.world.provider.getDimension() != 0 && this.world.provider.getDimension() != -1)) {
            this.teleportTo(
                    this.posX - blocks * Math.sin(Math.toRadians(this.rotationYaw)),
                    this.posY,
                    this.posZ + blocks * Math.cos(Math.toRadians(this.rotationYaw)));
        } else if (this.dimension == 0 || this.dimension == -1) {
            if (this.dimension == 0) {
                this.netherport(-1);
            } else {
                this.netherport(0);
            }
        }
    }

    private NightmareTeleporter teleporterFor(int dimension) {
        MinecraftServer server = this.world.getMinecraftServer();
        if (server == null) throw new IllegalStateException("Nightmare transfer requires a server world");
        WorldServer target = server.getWorld(dimension);
        if (target == null) throw new IllegalArgumentException("Unavailable Nightmare dimension: " + dimension);
        if (dimension == -1) {
            if (this.nightmareTeleporterNether == null) this.nightmareTeleporterNether = new NightmareTeleporter(target);
            return this.nightmareTeleporterNether;
        }
        if (dimension != 0) throw new IllegalArgumentException("Nightmare portal destination: " + dimension);
        if (this.nightmareTeleporterOverworld == null) this.nightmareTeleporterOverworld = new NightmareTeleporter(target);
        return this.nightmareTeleporterOverworld;
    }

    private void netherport(final int dim) {
        if (this.world.isRemote || !(this.getControllingPassenger() instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) this.getControllingPassenger();
        if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(player, dim)) return;
        World sourceWorld=this.world;
        double sourceX=this.posX, sourceY=this.posY+this.height/2.0F, sourceZ=this.posZ;
        player.dismountRidingEntity();
        Entity transferred = this.nightmareTravelToDimension(dim);
        if (transferred == null) {
            if (!this.isDead) player.startRiding(this, true);
            return;
        }
        // Entity.changeDimension owns the horse's single Forge travel hook.
        // Apply the original departure explosion only after that transfer succeeds.
        sourceWorld.newExplosion(this, sourceX, sourceY, sourceZ, 2.0f, true, true);
        this.playerTravelToDimension(player, dim);
        player.connection.setPlayerLocation(transferred.posX, transferred.posY, transferred.posZ,
                transferred.rotationYaw, transferred.rotationPitch);
        player.startRiding(transferred, true);
        PacketHandler.INSTANCE.sendTo(new PacketMountNightmare(transferred, player), player);
    }

    public Entity nightmareTravelToDimension(final int dimension) {
        if (this.world.isRemote || this.isDead) return null;
        return this.changeDimension(dimension, this.teleporterFor(dimension));
    }

    public EntityPlayerMP playerTravelToDimension(final EntityPlayerMP player, final int dimension) {
        player.getServer().getPlayerList().transferPlayerToDimension(player, dimension, this.teleporterFor(dimension));
        return player;
    }

    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        return I18n.translateToLocal("entity.ThaumicHorizons.NightmareTH.name");
    }

    public void onUpdate() {
        final AxisAlignedBB axisalignedbb = new AxisAlignedBB(
                this.getEntityBoundingBox().minX,
                this.getEntityBoundingBox().minY,
                this.getEntityBoundingBox().minZ,
                this.getEntityBoundingBox().maxX,
                this.getEntityBoundingBox().maxY,
                this.getEntityBoundingBox().maxZ);
        if (this.world.isMaterialInBB(axisalignedbb, Material.LAVA)) {
            this.motionY += 0.1;
            if (this.motionY > 0.25) {
                this.motionY = 0.25;
            }
            this.onGround = true;
            this.fallDistance = 0.0f;
        } else if (this.world.getBlockState(new BlockPos((int) this.posX, (int) Math.floor(this.posY - 1.0), (int) this.posZ)).getMaterial() == Material.LAVA) {
                    this.onGround = true;
                    this.fallDistance = 0.0f;
                    if (this.motionY < 0.0) {
                        this.motionY = 0.0;
                    }
                }
        super.onUpdate();
        final net.minecraft.block.state.IBlockState underfoot = this.world.getBlockState(new BlockPos((int) this.posX, (int) this.posY - 1, (int) this.posZ));
        final net.minecraft.block.state.IBlockState in = this.world.getBlockState(new BlockPos((int) this.posX, (int) this.posY, (int) this.posZ));
        final net.minecraft.block.state.IBlockState up = this.world.getBlockState(new BlockPos((int) this.posX, (int) this.posY + 1, (int) this.posZ));
        if (!this.world.isRemote && underfoot.getMaterial() == Material.GRASS) {
            this.world.setBlockState(new BlockPos((int) this.posX, (int) this.posY - 1, (int) this.posZ), Blocks.DIRT.getDefaultState(), 3);
            ThaumicHorizons.proxy
                    .smeltFX((int) this.posX, (int) this.posY - 1, (int) this.posZ, this.world, 10, false);
        }
        if (this.world.isRemote && underfoot.isNormalCube() && this.moveForward > 0.0f) {
            ThaumicHorizons.proxy
                    .smeltFX((int) this.posX, (int) this.posY - 1, (int) this.posZ, this.world, 3, false);
        }
        if (!this.world.isRemote && (in.getMaterial() == Material.LEAVES || in.getMaterial() == Material.WEB
                || in.getMaterial() == Material.VINE
                || in.getMaterial() == Material.PLANTS)) {
            this.world.setBlockToAir(new BlockPos((int) this.posX, (int) this.posY, (int) this.posZ));
            ThaumicHorizons.proxy.smeltFX((int) this.posX, (int) this.posY, (int) this.posZ, this.world, 15, false);
        }
        if (!this.world.isRemote && (up.getMaterial() == Material.LEAVES || up.getMaterial() == Material.WEB
                || up.getMaterial() == Material.VINE
                || up.getMaterial() == Material.PLANTS)) {
            this.world.setBlockToAir(new BlockPos((int) this.posX, (int) this.posY + 1, (int) this.posZ));
            ThaumicHorizons.proxy
                    .smeltFX((int) this.posX, (int) this.posY + 1, (int) this.posZ, this.world, 15, false);
        }
    }

    public void setInWeb() {
        this.isInWeb = false;
        this.fallDistance = 0.0f;
    }
}
