//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;


import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.common.registry.VillagerRegistry;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.InventoryUtils;

public class TileSoulforge extends TileThaumcraft implements ISoulReceiver, IEssentiaTransport, IAspectContainer, net.minecraft.util.ITickable {

    int progress;
    static final int PROGRESS_MAX = 9600;
    public int souls;
    static final int MAX_SOULS = 16;
    int essentia;
    static final int ESSENTIA_MAX = 4000;
    // Threshold for drawing more essentia (3 MIND aspects * 1000 each)
    static final int ESSENTIA_DRAW_THRESHOLD = 3000;
    static final int ESSENTIA_PER_ASPECT = 1000;
    static final int SUCTION_PRESSURE = 128;
    static final double PLAYER_REACH_SQ = 64.0;
    public float rota;
    public int forging;

    public TileSoulforge() {
        this.progress = 0;
        this.souls = 0;
        this.essentia = 0;
        this.forging = 3;
    }

    public boolean activate(final World world, final EntityPlayer player) {
        if (player.getHeldItemMainhand().isEmpty() || player.getHeldItemMainhand().getItemDamage() != 0
                || player.getHeldItemMainhand().getItem() != Item.getItemFromBlock(ConfigBlocks.blockJar)) {
            return false;
        }
        if (this.souls > 0) {
            final ItemStack soul = new ItemStack(ThaumicHorizons.blockJar);
            soul.setTagCompound(new NBTTagCompound());
            soul.getTagCompound().setBoolean("isSoul", true);
            final java.util.List<VillagerRegistry.VillagerProfession> professions =
                    new java.util.ArrayList<>(net.minecraftforge.fml.common.registry.ForgeRegistries.VILLAGER_PROFESSIONS.getValuesCollection());
            final VillagerRegistry.VillagerProfession profession = professions.get(world.rand.nextInt(professions.size()));
            com.kentington.thaumichorizons.common.lib.VillagerSoulData.write(soul.getTagCompound(), profession);
            final EntityVillager dummyVillager = new EntityVillager(this.world);
            dummyVillager.setProfession(profession);
            soul.getTagCompound().setString("jarredCritterName", dummyVillager.getName());
            player.inventory.decrStackSize(
                    InventoryUtils.isPlayerCarrying(player, new ItemStack(ConfigBlocks.blockJar, 1, 0)),
                    1);
            if (!player.inventory.addItemStackToInventory(soul)) {
                player.entityDropItem(soul, 1.0f);
            }
            --this.souls;
            return true;
        }
        return false;
    }

    @Override
    public void addSoulBits(final int bits) {
        this.progress += bits;
        this.essentia -= bits;
        while (this.progress >= PROGRESS_MAX) {
            this.progress -= PROGRESS_MAX;
            ++this.souls;
        }
        this.forging = 3;
        this.markDirty();
        this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
    }

    @Override
    public boolean canAcceptSouls() {
        return this.essentia > 0 && this.souls < MAX_SOULS;
    }

    @Override
    public void update() {
        if (this.essentia < ESSENTIA_DRAW_THRESHOLD) {
            this.drawEssentia();
        }
        if (this.forging > 0) {
            ++this.rota;
            if (this.rota > 360.0f) {
                this.rota -= 360.0f;
            }
            --this.forging;
        }
    }

    public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
        return this.world.getTileEntity(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())) == this
                && p_70300_1_.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= PLAYER_REACH_SQ;
    }

    void drawEssentia() {
        final EnumFacing dir = EnumFacing.UP;
        final TileEntity te = ThaumcraftApiHelper
                .getConnectableTile(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), dir);
        if (te != null) {
            final EnumFacing opposite = EnumFacing.DOWN;
            final IEssentiaTransport ic = (IEssentiaTransport) te;
            if (!ic.canOutputTo(opposite)) {
                return;
            }
            Aspect ta = null;
            if (ic.getEssentiaAmount(opposite) > 0 && ic.getSuctionAmount(opposite) < this.getSuctionAmount(dir)
                    && this.getSuctionAmount(dir) >= ic.getMinimumSuction()) {
                ta = ic.getEssentiaType(opposite);
            }
            if (ta != null && ta.getTag().equals(Aspect.MIND.getTag()) && ic.takeEssentia(ta, 1, opposite) == 1) {
                this.addEssentia(ta, 1, dir);
            }
        }
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("souls", this.souls);
        nbttagcompound.setInteger("progress", this.progress);
        nbttagcompound.setInteger("essentia", this.essentia);
        nbttagcompound.setInteger("forging", this.forging);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        this.souls = nbttagcompound.getInteger("souls");
        this.progress = nbttagcompound.getInteger("progress");
        this.essentia = nbttagcompound.getInteger("essentia");
        this.forging = nbttagcompound.getInteger("forging");
    }

    @Override
    public AspectList getAspects() {
        if (this.essentia <= 0) {
            return null;
        }
        return new AspectList().add(Aspect.MIND, (int) Math.ceil(this.essentia / (float) ESSENTIA_PER_ASPECT));
    }

    @Override
    public void setAspects(final AspectList aspects) {
        this.essentia = aspects.getAmount(Aspect.MIND) * ESSENTIA_PER_ASPECT;
    }

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
        return tag.getTag().equals(Aspect.MIND.getTag()) && this.essentia / ESSENTIA_PER_ASPECT >= amount;
    }

    @Override
    public boolean doesContainerContain(final AspectList ot) {
        return false;
    }

    @Override
    public int containerContains(final Aspect tag) {
        if (tag.getTag().equals(Aspect.MIND.getTag())) {
            return this.essentia / ESSENTIA_PER_ASPECT;
        }
        return 0;
    }

    @Override
    public boolean isConnectable(final EnumFacing face) {
        return face == EnumFacing.UP;
    }

    @Override
    public boolean canInputFrom(final EnumFacing face) {
        return face == EnumFacing.UP;
    }

    @Override
    public boolean canOutputTo(final EnumFacing face) {
        return false;
    }

    @Override
    public void setSuction(final Aspect aspect, final int amount) {}

    @Override
    public Aspect getSuctionType(final EnumFacing face) {
        return Aspect.MIND;
    }

    @Override
    public int getSuctionAmount(final EnumFacing face) {
        return (this.essentia < ESSENTIA_DRAW_THRESHOLD) ? SUCTION_PRESSURE : 0;
    }

    @Override
    public int takeEssentia(final Aspect aspect, final int amount, final EnumFacing face) {
        return 0;
    }

    @Override
    public int addEssentia(final Aspect aspect, final int amount, final EnumFacing face) {
        if (this.essentia < ESSENTIA_DRAW_THRESHOLD) {
            this.essentia += ESSENTIA_PER_ASPECT;
            this.markDirty();
            this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
            return 1;
        }
        return 0;
    }

    @Override
    public Aspect getEssentiaType(final EnumFacing face) {
        return Aspect.MIND;
    }

    @Override
    public int getEssentiaAmount(final EnumFacing face) {
        return this.essentia / ESSENTIA_PER_ASPECT;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Override
    public boolean renderExtendedTube() {
        return false;
    }
}
