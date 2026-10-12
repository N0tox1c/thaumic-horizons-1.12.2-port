//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import com.kentington.thaumichorizons.common.lib.THSoundBridge;
import net.minecraft.util.math.BlockPos;


import static com.kentington.thaumichorizons.common.lib.PocketPlaneData.pocketPlaneMAXID;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityGolemTH;
import com.kentington.thaumichorizons.common.lib.PocketPlaneData;
import com.kentington.thaumichorizons.common.lib.VortexTeleporter;

import net.minecraftforge.fml.common.FMLCommonHandler;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockAiry;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TileVortex extends TileThaumcraft implements IWandable, IAspectContainer, net.minecraft.util.ITickable {

    private static final int MAX_COUNT = 2400;
    public int count;
    public int beams;
    public int dimensionID;
    public int returnID;
    public AspectList aspects;
    boolean ateDevices;
    public boolean collapsing;
    public boolean createdDimension;
    public boolean cheat;
    public ArrayList<ItemStack> items;
    private boolean soundStarted = false;
    // true once the first S35 description packet has been received on the client
    public boolean clientSynced = false;
    private int lastSyncedBeams = -1;

    public TileVortex() {
        this.aspects = new AspectList();
        this.ateDevices = false;
        this.collapsing = false;
        this.createdDimension = false;
        this.cheat = false;
        this.items = new ArrayList<>();
    }

    @Override
    public void update() {

        // Send one packet per tick when beams changes, so same-tick re/deHungrify
        // pairs don't produce two packets with an intermediate wrong value.
        if (!this.world.isRemote && this.beams != this.lastSyncedBeams) {
            this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
            this.lastSyncedBeams = this.beams;
        }

        if (this.world != null && this.world.isRemote && !soundStarted) {
            ThaumicHorizons.proxy.playVortexSound(this);
            soundStarted = true;
        }

        if (this.collapsing) {
            ++this.count;
            if (this.count > 25) {
                if (this.createdDimension) {
                    WorldServer wsd = DimensionManager.getWorld(ThaumicHorizons.dimensionPocketId);
                    if (wsd != null) {
                        wsd.setBlockToAir(new net.minecraft.util.math.BlockPos(0, 129, this.dimensionID * 256));
                    }
                }
                this.world.setBlockToAir(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
                if (this.world.isRemote)
                    Thaumcraft.proxy.burst(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), 4.0f);
                BlockAiry.explodify(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
            }
            return;
        }
        if (this.count < 50) {
            ++this.count;
        } else {
            if (!this.ateDevices) {
                if (!this.cheat && this.world.provider.getDimension() != ThaumicHorizons.dimensionPocketId) {
                    for (int dx = -2; dx <= 2; ++dx) {
                        for (int dy = -2; dy <= 2; ++dy) {
                            for (int dz = -2; dz <= 2; ++dz) {
                                if (dx != 0 || dy != 0 || dz != 0) {
                                    this.world.setBlockToAir(new BlockPos(this.pos.getX() + dx, this.pos.getY() + dy, this.pos.getZ() + dz));
                                    if (this.world.isRemote) Thaumcraft.proxy.burst(
                                            this.world,
                                            this.pos.getX() + dx,
                                            this.pos.getY() + dy,
                                            this.pos.getZ() + dz,
                                            4.0f);
                                }
                            }
                        }
                    }
                }
                this.ateDevices = true;
            }
            if (this.beams < 6 && !this.cheat
                    && this.world.provider.getDimension() != ThaumicHorizons.dimensionPocketId) {
                this.handleHungryNode();
            } else if (!this.createdDimension) {
                this.handlePocketPlaneStuff();
            }
            if (!this.cheat) {
                this.count += Math.max(0, 6 - this.beams);
            }
            if (this.count > MAX_COUNT && !this.cheat
                    && this.world.provider.getDimension() != ThaumicHorizons.dimensionPocketId) {
                this.collapsing = true;
                this.count = 0;
            }
            if (this.createdDimension) {
                final List<EntityPlayerMP> ents = this.world.getEntitiesWithinAABB(
                        EntityPlayerMP.class,
                        new AxisAlignedBB(
                                this.pos.getX(),
                                this.pos.getY(),
                                this.pos.getZ(),
                                this.pos.getX() + 1,
                                this.pos.getY() + 1,
                                this.pos.getZ() + 1));
                if (!ents.isEmpty()) {
                    for (final EntityPlayerMP player : ents) {
                        if (!player.isRiding() && !player.isBeingRidden()) {
                            final MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
                            if (player.timeUntilPortal > 0) {
                                player.timeUntilPortal = 10;
                            } else if (player.dimension != ThaumicHorizons.dimensionPocketId) {
                                player.timeUntilPortal = 200;
                                mServer.getPlayerList().transferPlayerToDimension(
                                        player,
                                        ThaumicHorizons.dimensionPocketId,
                                        new VortexTeleporter(
                                                mServer.getWorld(ThaumicHorizons.dimensionPocketId),
                                                this.dimensionID));
                            } else {
                                player.timeUntilPortal = 200;
                                mServer.getPlayerList().transferPlayerToDimension(
                                        player,
                                        this.returnID,
                                        new VortexTeleporter(
                                                mServer.getWorld(this.returnID),
                                                this.dimensionID));
                            }
                        }
                    }
                }
            }
        }
    }

    void handlePocketPlaneStuff() {
        final List<Entity> ents = this.world.getEntitiesWithinAABB(
                Entity.class,
                new AxisAlignedBB(
                        this.pos.getX(),
                        this.pos.getY(),
                        this.pos.getZ(),
                        this.pos.getX() + 1,
                        this.pos.getY() + 1,
                        this.pos.getZ() + 1).expand(1.0, 1.0, 1.0));
        if (ents != null && !ents.isEmpty()) {
            for (final Entity eo : ents) {
                if (eo instanceof EntityItem) { EntityItem item = (EntityItem) eo;
                    if (ThaumicHorizons.enablePocket && item.getItem().getItem() == ConfigItems.itemEldritchObject
                            && item.getItem().getItemDamage() == 3) {
                        this.world.createExplosion(
                                null,
                                this.pos.getX() + this.world.rand.nextFloat(),
                                this.pos.getY() + this.world.rand.nextFloat(),
                                this.pos.getZ() + this.world.rand.nextFloat(),
                                1.0f,
                                false);
                        this.createDimension(item);
                        item.setDead();
                    } else {
                        this.handleVoidCrafting(item);
                    }
                }
            }
        }
    }

    void handleVoidCrafting(final EntityItem item) {
        if (item.getItem().getItem() == ConfigItems.itemResource && item.getItem().getItemDamage() == 16) {
            this.items.add(new ItemStack(ThaumicHorizons.itemVoidPutty, item.getItem().getCount()));
            item.setDead();
        } else if (item.getItem().getItem() == ConfigItems.itemResource
                && item.getItem().getItemDamage() == 14) {
                    for (int i = 0; i < item.getItem().getCount(); ++i) {
                        this.spawnWisps();
                    }
                    item.setDead();
                } else
            if (item.getItem().getItem() == ThaumicHorizons.itemCrystalWand) {
                final ItemStack theWand = new ItemStack(ThaumicHorizons.itemWandCastingDisposable);
                final ItemWandCasting wand = (ItemWandCasting) theWand.getItem();
                wand.setRod(theWand, ThaumicHorizons.ROD_CRYSTAL);
                wand.setCap(theWand, ThaumicHorizons.CAP_CRYSTAL);
                wand.storeVis(theWand, Aspect.EARTH, 25000);
                wand.storeVis(theWand, Aspect.AIR, 25000);
                wand.storeVis(theWand, Aspect.FIRE, 25000);
                wand.storeVis(theWand, Aspect.WATER, 25000);
                wand.storeVis(theWand, Aspect.ORDER, 25000);
                wand.storeVis(theWand, Aspect.ENTROPY, 25000);
                this.items.add(theWand);
                item.setDead();
            } else
                if (item.getItem().getItem() == ThaumicHorizons.itemGolemPowder && item.getOwner() != null) {
                    if (!this.world.isRemote) {
                        for (int i = 0; i < item.getItem().getCount(); ++i) {
                            final EntityGolemTH golem = new EntityGolemTH(this.world);
                            golem.setOwner(item.getOwner());
                            golem.loadGolem(
                                    this.pos.getX() + 0.5,
                                    this.pos.getY(),
                                    this.pos.getZ() + 0.5,
                                    null,
                                    0,
                                    -420,
                                    false,
                                    false,
                                    false);
                            THSoundBridge.play(this.world, 
                                    this.pos.getX() + 0.5,
                                    this.pos.getY() + 0.5,
                                    this.pos.getZ() + 0.5,
                                    "thaumcraft:wand",
                                    1.0f,
                                    1.0f);
                            golem.setHomePosAndDistance(new net.minecraft.util.math.BlockPos((int) golem.posX, (int) golem.posY, (int) golem.posZ), 32);
                            this.world.spawnEntity(golem);
                            this.world.setEntityState(golem, (byte) 7);
                        }
                    }
                    item.setDead();
                }
    }

    void spawnWisps() {
        if (this.world.isRemote) {
            return;
        }
        for (int wisps = this.world.rand.nextInt(4) + 1, i = 0; i < wisps; ++i) {
            final EntityWisp wisp = new EntityWisp(this.world);
            wisp.setPosition(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
            if (this.aspects.size() > 0 && this.aspects.getAspects()[0] != null) {
                wisp.setWispType(this.aspects.getAspects()[this.world.rand.nextInt(this.aspects.size())].getTag());
            }
            this.world.spawnEntity(wisp);
        }
    }

    void createDimension(final EntityItem pearl) {
        final MinecraftServer server = net.minecraftforge.fml.common.FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null) return;
        final PocketPlaneData data = new PocketPlaneData();
        String name = "";
        if (pearl != null && pearl.getItem().hasTagCompound()) {
            name = pearl.getItem().getDisplayName();
        } else if (pearl != null) {
            name = pearl.getOwner() + I18n.translateToLocal("thaumichorizons.pocketplane");
        }
        data.name = name;
        this.dimensionID = pocketPlaneMAXID;
        if (DimensionManager.getWorld(ThaumicHorizons.dimensionPocketId) == null) {
            DimensionManager.initDimension(ThaumicHorizons.dimensionPocketId);
        }
        if (!this.world.isRemote) {
            this.returnID = this.world.provider.getDimension();
            World world = server.getWorld(ThaumicHorizons.dimensionPocketId);
            PocketPlaneData.generatePocketPlane(
                    this.aspects,
                    data,
                    world,
                    this.pos.getX(),
                    this.pos.getY(),
                    this.pos.getZ(),
                    this.returnID);
            this.createdDimension = true;
            this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
        }
        this.markDirty();
    }

    void handleHungryNode() {
        if (this.world.isRemote && this.beams < 6) {
            for (int a = 0; a < Thaumcraft.proxy.particleCount(1); ++a) {
                int tx = this.pos.getX() + this.world.rand.nextInt(16) - this.world.rand.nextInt(16);
                int ty = this.pos.getY() + this.world.rand.nextInt(16) - this.world.rand.nextInt(16);
                int tz = this.pos.getZ() + this.world.rand.nextInt(16) - this.world.rand.nextInt(16);
                if (ty > this.world.getHeight(new BlockPos(tx, 0, tz)).getY()) {
                    ty = this.world.getHeight(new BlockPos(tx, 0, tz)).getY();
                }
                final Vec3d v1 = new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
                final Vec3d v2 = new Vec3d(tx + 0.5, ty + 0.5, tz + 0.5);
                final RayTraceResult mop = ThaumcraftApiHelper
                        .rayTraceIgnoringSource(this.world, v1, v2, true, false, false);
                if (mop != null && this.getDistanceTo(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ()) < 256.0) {
                    tx = mop.getBlockPos().getX();
                    ty = mop.getBlockPos().getY();
                    tz = mop.getBlockPos().getZ();
                    final Block bi = this.world.getBlockState(new BlockPos(tx, ty, tz)).getBlock();
                    final int md = this.world.getBlockState(new BlockPos(tx, ty, tz)).getBlock().getMetaFromState(this.world.getBlockState(new BlockPos(tx, ty, tz)));
                    if (!this.world.isAirBlock(new BlockPos(tx, ty, tz))) {
                        Thaumcraft.proxy
                                .hungryNodeFX(this.world, new BlockPos(tx, ty, tz), this.pos, this.world.getBlockState(new BlockPos(tx, ty, tz)));
                    }
                }
            }
        }
        final List ents = this.world.getEntitiesWithinAABB(
                Entity.class,
                new AxisAlignedBB(
                        this.pos.getX(),
                        this.pos.getY(),
                        this.pos.getZ(),
                        this.pos.getX() + 1,
                        this.pos.getY() + 1,
                        this.pos.getZ() + 1).expand(15.0, 15.0, 15.0));
        if (ents != null && ents.size() > 0) {
            for (final Object ent : ents) {
                final Entity eo = (Entity) ent;
                if (!(eo instanceof EntityPlayer) || !((EntityPlayer) eo).capabilities.disableDamage) {
                    if (eo.isEntityAlive() && !eo.isEntityInvulnerable(DamageSource.OUT_OF_WORLD)) {
                        final double d = this.getDistanceTo(eo.posX, eo.posY, eo.posZ);
                        if (d < 2.0) {
                            if (eo instanceof EntityFallingBlock) {
                                eo.setDead();
                            } else {
                                eo.attackEntityFrom(DamageSource.OUT_OF_WORLD, 3.0f - this.beams / 2.0f);
                            }
                        }
                    }
                    final double dx = (this.pos.getX() + 0.5 - eo.posX) / 15.0;
                    final double dy = (this.pos.getY() + 0.5 - eo.posY) / 15.0;
                    final double dz = (this.pos.getZ() + 0.5 - eo.posZ) / 15.0;
                    final double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    double var7 = 1.0 - dist;
                    final double modifier = 2.0 - this.beams / 3.0;
                    if (var7 <= 0.0) {
                        continue;
                    }
                    var7 *= var7;
                    eo.motionX += dx / dist * var7 * 0.15 * modifier;
                    eo.motionY += dy / dist * var7 * 0.25 * modifier;
                    eo.motionZ += dz / dist * var7 * 0.15 * modifier;
                }
            }
        }
        for (int i = 0; i < 3; ++i) {
            if (!this.world.isRemote
                    && (this.beams <= 0 || this.world.rand.nextInt(Math.max(1, this.beams)) == 0)) {
                int tx2 = this.pos.getX() + this.world.rand.nextInt(16) - this.world.rand.nextInt(16);
                int ty2 = this.pos.getY() + this.world.rand.nextInt(16) - this.world.rand.nextInt(16);
                int tz2 = this.pos.getZ() + this.world.rand.nextInt(16) - this.world.rand.nextInt(16);
                if (ty2 > this.world.getHeight(new BlockPos(tx2, 0, tz2)).getY()) {
                    ty2 = this.world.getHeight(new BlockPos(tx2, 0, tz2)).getY();
                }
                final Vec3d v3 = new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
                final Vec3d v4 = new Vec3d(tx2 + 0.5, ty2 + 0.5, tz2 + 0.5);
                final RayTraceResult mop2 = ThaumcraftApiHelper
                        .rayTraceIgnoringSource(this.world, v3, v4, true, false, false);
                if (mop2 != null && this.getDistanceTo(mop2.getBlockPos().getX(), mop2.getBlockPos().getY(), mop2.getBlockPos().getZ()) < 256.0) {
                    tx2 = mop2.getBlockPos().getX();
                    ty2 = mop2.getBlockPos().getY();
                    tz2 = mop2.getBlockPos().getZ();
                    final Block bi2 = this.world.getBlockState(new BlockPos(tx2, ty2, tz2)).getBlock();
                    final int md2 = this.world.getBlockState(new BlockPos(tx2, ty2, tz2)).getBlock().getMetaFromState(this.world.getBlockState(new BlockPos(tx2, ty2, tz2)));
                    if (!this.world.isAirBlock(new BlockPos(tx2, ty2, tz2))) {
                        final float h = this.world.getBlockState(new BlockPos(tx2, ty2, tz2)).getBlockHardness(this.world, new BlockPos(tx2, ty2, tz2));
                        if (h >= 0.0f && h < 10.0f) {
                            this.world.destroyBlock(new BlockPos(tx2, ty2, tz2), true);
                        }
                    }
                }
            }
        }
    }

    public double getDistanceTo(final double par1, final double par3, final double par5) {
        final double var7 = this.pos.getX() + 0.5 - par1;
        final double var8 = this.pos.getY() + 0.5 - par3;
        final double var9 = this.pos.getZ() + 0.5 - par5;
        return var7 * var7 + var8 * var8 + var9 * var9;
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setInteger("count", this.count);
        nbttagcompound.setInteger("beams", this.beams);
        nbttagcompound.setInteger("dimensionID", this.dimensionID);
        nbttagcompound.setInteger("returnID", this.returnID);
        nbttagcompound.setBoolean("ateDevices", this.ateDevices);
        nbttagcompound.setBoolean("collapsing", this.collapsing);
        nbttagcompound.setBoolean("createdDimension", this.createdDimension);
        nbttagcompound.setBoolean("cheat", this.cheat);
        final NBTTagList tlist = new NBTTagList();
        nbttagcompound.setTag("aspects", tlist);
        for (final Aspect aspect : this.aspects.getAspects()) {
            if (aspect != null) {
                final NBTTagCompound f = new NBTTagCompound();
                f.setString("key", aspect.getTag());
                f.setInteger("amount", this.aspects.getAmount(aspect));
                tlist.appendTag(f);
            }
        }
        final NBTTagList itemz = new NBTTagList();
        for (final ItemStack item : this.items) {
            final NBTTagCompound itemTag = new NBTTagCompound();
            item.writeToNBT(itemTag);
            itemz.appendTag(itemTag);
        }
        nbttagcompound.setTag("items", itemz);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.count = nbttagcompound.getInteger("count");
        this.beams = nbttagcompound.getInteger("beams");
        this.dimensionID = nbttagcompound.getInteger("dimensionID");
        this.returnID = nbttagcompound.getInteger("returnID");
        this.ateDevices = nbttagcompound.getBoolean("ateDevices");
        this.collapsing = nbttagcompound.getBoolean("collapsing");
        this.createdDimension = nbttagcompound.getBoolean("createdDimension");
        this.cheat = nbttagcompound.getBoolean("cheat");
        final AspectList al = new AspectList();
        final NBTTagList tlist = nbttagcompound.getTagList("aspects", 10);
        for (int j = 0; j < tlist.tagCount(); ++j) {
            final NBTTagCompound rs = tlist.getCompoundTagAt(j);
            if (rs.hasKey("key")) {
                al.add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
            }
        }
        this.aspects = al.copy();
        this.items.clear();
        final NBTTagList itemz = nbttagcompound.getTagList("items", 10);
        for (int i = 0; i < itemz.tagCount(); ++i) {
            final ItemStack item = new ItemStack(itemz.getCompoundTagAt(i));
            this.items.add(item);
        }
    }

    @Override
    public int onWandRightClick(final World world, final ItemStack wandstack, final EntityPlayer player, final int x,
            final int y, final int z, final int side, final int md) {
        if (!this.world.isRemote && !this.items.isEmpty()) {
            final ItemStack item = this.items.get(0);
            final EntityItem key = new EntityItem(this.world);
            key.setItem(item);
            key.setPosition(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
            this.world.spawnEntity(key);
            this.items.remove(0);
            THSoundBridge.playLegacy(player.world, 
                    x + 0.5,
                    y + 0.5,
                    z + 0.5,
                    "thaumcraft:wand",
                    0.5f,
                    0.9f + player.world.rand.nextFloat() * 0.2f,
                    false);
            player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
            this.markDirty();
        }
        THSoundBridge.playLegacy(player.world, 
                x + 0.5,
                y + 0.5,
                z + 0.5,
                "thaumcraft:wand",
                0.5f,
                0.9f + player.world.rand.nextFloat() * 0.2f,
                false);
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

    @Override
    public AspectList getAspects() {
        if (this.aspects.getAspects()[0] != null) {
            return this.aspects;
        }
        return new AspectList();
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

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeCustomNBT(nbt);
        return new SPacketUpdateTileEntity(this.pos, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readCustomNBT(pkt.getNbtCompound());
        this.clientSynced = true;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        stopSound();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        stopSound();
    }

    private void stopSound() {
        if (this.world != null && this.world.isRemote) {
            ThaumicHorizons.proxy.stopVortexSound(this);
        }
        soundStarted = false;
    }
}
