//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import com.kentington.thaumichorizons.common.lib.WandInteraction;


import java.awt.Color;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.visnet.TileVisNode;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.EntityUtils;

public class TileVisDynamo extends TileVisNode implements IAspectContainer, IWandable {

    AspectList primalsActuallyProvided;
    AspectList primalsProvided;
    public boolean provideAer;
    public boolean provideAqua;
    public boolean provideIgnis;
    public boolean provideOrdo;
    public boolean providePerditio;
    public boolean provideTerra;
    public int ticksProvided;
    public float rise;
    public float rotation;
    public float rotation2;
    public Entity drainEntity;
    public RayTraceResult drainCollision;
    public int drainColor;
    public Color targetColor;
    public Color color;

    public TileVisDynamo() {
        this.primalsActuallyProvided = new AspectList();
        this.primalsProvided = new AspectList();
        this.provideAer = true;
        this.provideAqua = true;
        this.provideIgnis = true;
        this.provideOrdo = true;
        this.providePerditio = true;
        this.provideTerra = true;
        this.ticksProvided = 0;
        this.rise = 0.0f;
        this.rotation = 0.0f;
        this.rotation2 = 0.0f;
        this.drainEntity = null;
        this.drainCollision = null;
        this.drainColor = 16777215;
        this.targetColor = new Color(16777215);
        this.color = new Color(16777215);
    }

    public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
        return this.world.getTileEntity(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())) == this
                && p_70300_1_.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public AspectList getAspects() {
        if (this.primalsProvided.getAspects().length > 0 && this.primalsProvided.getAspects()[0] != null) {
            return this.primalsProvided;
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
        return amount;
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
    public int getRange() {
        return 0;
    }

    @Override
    public boolean isSource() {
        return this.ticksProvided > 0;
    }

    @Override
    public int consumeVis(final Aspect aspect, final int amount) {
        final int drain = Math.min(this.primalsActuallyProvided.getAmount(aspect), amount);
        if (drain > 0) {
            this.primalsActuallyProvided.reduce(aspect, drain);
        }
        return drain;
    }

    @Override
    public void update() {
        super.update();
        if (this.ticksProvided > 0) {
            if (this.rise < 0.3f) {
                this.rise += 0.02f;
            } else {
                this.rotation2 += 2.0f;
                if (this.rotation2 >= 360.0f) {
                    this.rotation2 -= 360.0f;
                }
            }
            this.rotation += 2.0f;
            if (this.rotation >= 360.0f) {
                this.rotation -= 360.0f;
            }
            --this.ticksProvided;
            if (this.ticksProvided == 0) {
                this.primalsProvided = new AspectList();
                this.markDirty();
            }
            this.primalsActuallyProvided = this.primalsProvided.copy();
        } else if (this.ticksProvided == 0) {
            --this.ticksProvided;
            if (!this.world.isRemote && VisNetHandler.sources.get(this.world.provider.getDimension()) != null) {
                VisNetHandler.sources.get(this.world.provider.getDimension()).remove(
                        new WorldCoordinates(
                                this.pos.getX(),
                                this.pos.getY(),
                                this.pos.getZ(),
                                this.world.provider.getDimension()));
            }
            if (!this.world.isRemote) this.removeThisNode();
        } else if (this.ticksProvided < 0 && (this.rise > 0.0f || this.rotation2 != 0.0f)) {
            if (this.rotation2 > 0.0f) {
                this.rotation2 -= 8.0f;
                if (this.rotation2 < 0.0f) {
                    this.rotation2 = 0.0f;
                }
            } else if (this.rise > 0.0f) {
                this.rise -= 0.02f;
            }
        }
    }

    public void killMe() {
        if (!this.world.isRemote && VisNetHandler.sources.get(this.world.provider.getDimension()) != null) {
            VisNetHandler.sources.get(this.world.provider.getDimension()).remove(
                    new WorldCoordinates(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.world.provider.getDimension()));
        }
        this.removeThisNode();
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setInteger("ticks", this.ticksProvided);
        nbttagcompound.setBoolean("aer", this.provideAer);
        nbttagcompound.setBoolean("aqua", this.provideAqua);
        nbttagcompound.setBoolean("ignis", this.provideIgnis);
        nbttagcompound.setBoolean("ordo", this.provideOrdo);
        nbttagcompound.setBoolean("perditio", this.providePerditio);
        nbttagcompound.setBoolean("terra", this.provideTerra);
        final NBTTagList tlist = new NBTTagList();
        nbttagcompound.setTag("AspectsProvided", tlist);
        for (final Aspect aspect : this.primalsProvided.getAspects()) {
            if (aspect != null) {
                final NBTTagCompound f = new NBTTagCompound();
                f.setString("key", aspect.getTag());
                f.setInteger("amount", this.primalsProvided.getAmount(aspect));
                tlist.appendTag(f);
            }
        }
        if (this.drainEntity != null && this.drainEntity instanceof EntityPlayer) {
            nbttagcompound.setString("drainer", this.drainEntity.getName());
        }
        nbttagcompound.setInteger("draincolor", this.drainColor);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.ticksProvided = nbttagcompound.getInteger("ticks");
        this.provideAer = nbttagcompound.getBoolean("aer");
        this.provideAqua = nbttagcompound.getBoolean("aqua");
        this.provideIgnis = nbttagcompound.getBoolean("ignis");
        this.provideOrdo = nbttagcompound.getBoolean("ordo");
        this.providePerditio = nbttagcompound.getBoolean("perditio");
        this.provideTerra = nbttagcompound.getBoolean("terra");
        final AspectList al = new AspectList();
        final NBTTagList tlist = nbttagcompound.getTagList("AspectsProvided", 10);
        for (int j = 0; j < tlist.tagCount(); ++j) {
            final NBTTagCompound rs = tlist.getCompoundTagAt(j);
            if (rs.hasKey("key")) {
                al.add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
            }
        }
        this.primalsProvided = al.copy();
        final String de = nbttagcompound.getString("drainer");
        if (de != null && de.length() > 0 && this.getWorld() != null) {
            this.drainEntity = this.getWorld().getPlayerEntityByName(de);
            if (this.drainEntity != null) {
                this.drainCollision = new RayTraceResult(new Vec3d(this.drainEntity.posX, this.drainEntity.posY, this.drainEntity.posZ), EnumFacing.DOWN, this.pos);
            }
        }
        this.drainColor = nbttagcompound.getInteger("draincolor");
    }

    @Override
    public int onWandRightClick(final World paramWorld, final ItemStack paramItemStack,
            final EntityPlayer paramEntityPlayer, final int paramInt1, final int paramInt2, final int paramInt3,
            final int paramInt4, final int paramInt5) {
        return -1;
    }

    @Override
    public ItemStack onWandRightClick(final World paramWorld, final ItemStack wandstack, final EntityPlayer player) {
        WandInteraction.startUsing(player, wandstack);
        final ItemWandCasting wand = (ItemWandCasting) wandstack.getItem();
        wand.setObjectInUse(wandstack, this.pos.getX(), this.pos.getY(), this.pos.getZ());
        if (!this.world.isRemote && (this.provideAer || this.provideAqua
                || this.provideIgnis
                || this.provideOrdo
                || this.providePerditio
                || this.provideTerra)) {
            VisNetHandler.sources.computeIfAbsent(this.world.provider.getDimension(), k -> new HashMap<>());
            if (VisNetHandler.sources.get(this.world.provider.getDimension()).get(
                    new WorldCoordinates(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.world.provider.getDimension()))
                    == null) {
                VisNetHandler.sources.get(this.world.provider.getDimension()).put(
                        new WorldCoordinates(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.world.provider.getDimension()),
                        new WeakReference<>(this));
            } else if (VisNetHandler.sources.get(this.world.provider.getDimension()).get(
                    new WorldCoordinates(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.world.provider.getDimension()))
                    .get() == null) {
                        VisNetHandler.sources.get(this.world.provider.getDimension()).remove(
                                new WorldCoordinates(
                                        this.pos.getX(),
                                        this.pos.getY(),
                                        this.pos.getZ(),
                                        this.world.provider.getDimension()));
                        VisNetHandler.sources.get(this.world.provider.getDimension()).put(
                                new WorldCoordinates(
                                        this.pos.getX(),
                                        this.pos.getY(),
                                        this.pos.getZ(),
                                        this.world.provider.getDimension()),
                                new WeakReference<>(this));
                    }
        }
        return wandstack;
    }

    @Override
    public void onUsingWandTick(final ItemStack wandstack, final EntityPlayer player, final int count) {
        final boolean mfu = false;
        final ItemWandCasting wand = (ItemWandCasting) wandstack.getItem();
        final RayTraceResult movingobjectposition = WandInteraction.rayTrace(this.world, player, true);
        if (movingobjectposition == null
                || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK) {
            player.stopActiveHand();
            return;
        } else {
            final int i = movingobjectposition.getBlockPos().getX();
            final int j = movingobjectposition.getBlockPos().getY();
            final int k = movingobjectposition.getBlockPos().getZ();
            if (i != this.pos.getX() || j != this.pos.getY() || k != this.pos.getZ()) {
                player.stopActiveHand();
            return;
            }
        }
        if (!this.world.isRemote && count % 5 == 0) {
            boolean success = false;
            AspectList aspectsToProvide = new AspectList();
            this.primalsProvided = new AspectList();
            if (this.provideAer) {
                aspectsToProvide = aspectsToProvide.add(Aspect.AIR, 25);
            }
            if (this.provideAqua) {
                aspectsToProvide = aspectsToProvide.add(Aspect.WATER, 25);
            }
            if (this.provideIgnis) {
                aspectsToProvide = aspectsToProvide.add(Aspect.FIRE, 25);
            }
            if (this.provideOrdo) {
                aspectsToProvide = aspectsToProvide.add(Aspect.ORDER, 25);
            }
            if (this.providePerditio) {
                aspectsToProvide = aspectsToProvide.add(Aspect.ENTROPY, 25);
            }
            if (this.provideTerra) {
                aspectsToProvide = aspectsToProvide.add(Aspect.EARTH, 25);
            }
            for (final Aspect asp : aspectsToProvide.getAspects()) {
                if (wand.consumeVis(wandstack, player, asp, 40, false)) {
                    this.primalsProvided = this.primalsProvided.add(asp, 5);
                    success = true;
                }
            }
            if (success) {
                this.ticksProvided = 10;
                this.drainEntity = player;
                this.drainCollision = movingobjectposition;
                int r = 0;
                int g = 0;
                int b = 0;
                int num = 0;
                for (final Aspect asp2 : this.primalsProvided.getAspects()) {
                    final Color col = new Color(asp2.getColor());
                    r += col.getRed();
                    g += col.getGreen();
                    b += col.getBlue();
                    ++num;
                }
                r /= num;
                g /= num;
                b /= num;
                this.drainColor = new Color(r, g, b).getRGB();
                this.targetColor = new Color(this.drainColor);
                if (!this.world.isRemote) {
                    this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
                    this.markDirty();
                }
            } else {
                this.drainEntity = null;
                this.drainCollision = null;
            }
        }
        if (player.world.isRemote) {
            final int r2 = this.targetColor.getRed();
            final int g2 = this.targetColor.getGreen();
            final int b2 = this.targetColor.getBlue();
            final int r3 = this.color.getRed() * 4;
            final int g3 = this.color.getGreen() * 4;
            final int b3 = this.color.getBlue() * 4;
            this.color = new Color((r2 + r3) / 5, (g2 + g3) / 5, (b2 + b3) / 5);
        }
    }

    @Override
    public void onWandStoppedUsing(final ItemStack paramItemStack, final World paramWorld,
            final EntityPlayer paramEntityPlayer, final int paramInt) {}
}
