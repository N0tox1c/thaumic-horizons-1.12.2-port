//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.config.ConfigBlocks;

public class TileSoulJar extends TileThaumcraft implements IWandable , net.minecraft.util.ITickable {

    public NBTTagCompound jarTag;
    public Entity entity;
    public boolean drop;
    ResourceLocation texture;

    public TileSoulJar() {
        this.jarTag = new NBTTagCompound();
        this.entity = null;
        this.drop = true;
        this.texture = new ResourceLocation("thaumcraft", "textures/models/jar.png");
    }

    public void update() {
        if (this.entity == null && this.jarTag.hasKey("id") && !this.jarTag.getBoolean("isSoul")) {
            this.entity = EntityList.createEntityFromNBT(this.jarTag, this.world);
        }
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        NBTTagCompound updated = nbttagcompound.getCompoundTag("jarTag").copy();
        if (!updated.equals(this.jarTag)) this.entity = null;
        this.jarTag = updated;
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setTag("jarTag", this.jarTag);
    }

    @Override
    public int onWandRightClick(final World world, final ItemStack wandstack, final EntityPlayer player, final int x,
            final int y, final int z, final int side, final int md) {
        if (this.jarTag.getBoolean("isSoul") || !this.jarTag.hasKey("id")) {
            return 0;
        }
        if (!world.isRemote) {
            final Entity ent = EntityList.createEntityFromNBT(this.jarTag, world);
            if (ent == null) {
                return 0;
            }
            if (ent instanceof EntityTameable && ((EntityTameable) ent).getOwner() == null) {
                ((EntityTameable) ent).setOwnerId(player.getUniqueID());
            }
            ent.setLocationAndAngles(x + 0.5, y + 0.1, z + 0.5, 0.0f, 0.0f);
            if (!world.spawnEntity(ent)) return 0;
            this.drop = false;
            world.setBlockToAir(new BlockPos(x, y, z));
            this.markDirty();
        }
        world.playEvent(2001, new net.minecraft.util.math.BlockPos(x, y, z), Block.getStateId(ConfigBlocks.blockJar.getStateFromMeta(15)));
        com.kentington.thaumichorizons.common.lib.THSoundBridge.playLegacy(player.world, x + 0.5, y + 0.5, z + 0.5, "random.glass", 1.0f, 0.9f + player.world.rand.nextFloat() * 0.2f, false);
        player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
        return 0;
    }

    public ResourceLocation getTexture() {
        return this.texture;
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
