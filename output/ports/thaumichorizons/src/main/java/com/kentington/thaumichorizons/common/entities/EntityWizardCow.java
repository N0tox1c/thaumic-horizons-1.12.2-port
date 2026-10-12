//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;

import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.lib.networking.PacketGetCowData;
import com.kentington.thaumichorizons.common.lib.networking.PacketHandler;

import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.blocks.BlockJarItem;

import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class EntityWizardCow extends EntityCow implements IEntityAdditionalSpawnData {

    AspectList aspects;
    public AspectList essentia;
    public int nodeMod;
    public int nodeType;
    public boolean hasNode;

    public EntityWizardCow(final World p_i1683_1_) {
        super(p_i1683_1_);
        this.aspects = new AspectList();
        this.essentia = new AspectList();
        if (p_i1683_1_.isRemote) {
            PacketHandler.INSTANCE.sendToServer(new PacketGetCowData(this.getEntityId()));
        }
    }

    @Override
    public boolean processInteract(final EntityPlayer player, final net.minecraft.util.EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (!held.isEmpty() && held.getItem() instanceof BlockJarItem) {
            BlockJarItem jar = (BlockJarItem) held.getItem();
            if (this.hasNode && held.getItemDamage() != 2) {
                if (this.world.isRemote) return true;
                AspectList existing = jar.getAspects(held);
                for (Aspect aspect : this.essentia.getAspects()) {
                    int amount = existing == null ? 0 : existing.getAmount(aspect);
                    if (existing != null && existing.size() > 0 && amount == 0) continue;
                    int transferred = Math.min(this.essentia.getAmount(aspect), 64 - amount);
                    if (transferred <= 0) continue;
                    ItemStack filled = held.copy();
                    filled.setCount(1);
                    jar.setAspects(filled, new AspectList().add(aspect, amount + transferred));
                    this.essentia.remove(aspect, transferred);
                    held.shrink(1);
                    if (held.isEmpty()) player.setHeldItem(hand, filled);
                    else if (!player.inventory.addItemStackToInventory(filled)) player.dropItem(filled, false);
                    player.inventory.markDirty();
                    return true;
                }
                return false;
            }
            if (!this.hasNode && held.getItemDamage() == 2) {
                AspectList nodeAspects = jar.getAspects(held);
                NodeType type = jar.getNodeType(held);
                if (nodeAspects == null || type == null) return false;
                if (this.world.isRemote) return true;
                this.aspects = new AspectList().add(nodeAspects);
                this.hasNode = true;
                NodeModifier mod = jar.getNodeModifier(held);
                this.nodeMod = mod == NodeModifier.BRIGHT ? 1 : mod == NodeModifier.PALE ? -1 : mod == NodeModifier.FADING ? -2 : 0;
                this.nodeType = type == NodeType.UNSTABLE ? 2 : type == NodeType.DARK ? 3 : type == NodeType.TAINTED ? 4
                        : type == NodeType.HUNGRY ? 5 : type == NodeType.PURE ? 6 : 1;
                ItemStack empty = new ItemStack(ConfigBlocks.blockJar);
                held.shrink(1);
                if (held.isEmpty()) player.setHeldItem(hand, empty);
                else if (!player.inventory.addItemStackToInventory(empty)) player.dropItem(empty, false);
                player.inventory.markDirty();
                return true;
            }
        }
        return super.processInteract(player, hand);
    }

    public NodeType getNodeType() {
        switch (this.nodeType) {
    case 2: {
                return NodeType.UNSTABLE;
            }
    case 3: {
                return NodeType.DARK;
            }
    case 4: {
                return NodeType.TAINTED;
            }
    case 5: {
                return NodeType.HUNGRY;
            }
    case 6: {
                return NodeType.PURE;
            }
    default: {
                return NodeType.NORMAL;
            }
}
    }

    public NodeModifier getNodeMod() {
        switch (this.nodeMod) {
    case 1: {
                return NodeModifier.BRIGHT;
            }
    case -1: {
                return NodeModifier.PALE;
            }
    case -2: {
                return NodeModifier.FADING;
            }
    default: {
                return null;
            }
}
    }

    public void updateAITasks() {
        super.updateAITasks();
        if (this.hasNode) {
            for (final Aspect asp : this.aspects.getAspects()) {
                final int divisor = this.aspects.getAmount(asp) * (3 + this.nodeMod);
                if (this.essentia.getAmount(asp) < 64 && divisor > 0 && this.ticksExisted % (150000 / divisor) == 0) {
                    this.essentia.add(asp, 1);
                }
            }
        }
    }

    public AspectList getEssentia() {
        return this.essentia;
    }

    public AspectList getAspects() {
        return this.aspects;
    }

    public void writeEntityToNBT(final NBTTagCompound p_70014_1_) {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setBoolean("hasNode", this.hasNode);
        p_70014_1_.setInteger("nodeMod", this.nodeMod);
        p_70014_1_.setInteger("nodeType", this.nodeType);
        final NBTTagCompound aspectTag = new NBTTagCompound();
        this.aspects.writeToNBT(aspectTag);
        p_70014_1_.setTag("aspects", aspectTag);
        final NBTTagCompound essentiaTag = new NBTTagCompound();
        this.essentia.writeToNBT(essentiaTag);
        p_70014_1_.setTag("essentia", essentiaTag);
    }

    public void readEntityFromNBT(final NBTTagCompound p_70037_1_) {
        super.readEntityFromNBT(p_70037_1_);
        this.hasNode = p_70037_1_.getBoolean("hasNode");
        this.nodeMod = p_70037_1_.getInteger("nodeMod");
        this.nodeType = p_70037_1_.getInteger("nodeType");
        this.aspects.readFromNBT(p_70037_1_.getCompoundTag("aspects"));
        this.essentia.readFromNBT(p_70037_1_.getCompoundTag("essentia"));
    }

    public void writeSpawnData(final ByteBuf buffer) {
        if (this.aspects.size() > 0 && this.aspects.getAspects()[0] != null) {
            buffer.writeBoolean(true);
            buffer.writeInt(this.aspects.size());
            for (final Aspect asp : this.aspects.getAspects()) {
                final String tag = asp.getTag();
                buffer.writeInt(tag.length());
                buffer.writeBytes(tag.getBytes());
                buffer.writeInt(this.aspects.getAmount(asp));
            }
        } else {
            buffer.writeBoolean(false);
        }
    }

    public void readSpawnData(final ByteBuf buffer) {
        if (buffer.readBoolean()) {
            for (int numAspects = buffer.readInt(), i = 0; i < numAspects; ++i) {
                final int length = buffer.readInt();
                final byte[] bytes = new byte[length];
                final char[] chars = new char[length];
                buffer.readBytes(bytes);
                final int j = 0;
                while (i < bytes.length) {
                    chars[j] = (char) bytes[j];
                    ++i;
                }
                final String tag = String.copyValueOf(chars);
                this.aspects.add(Aspect.getAspect(tag), buffer.readInt());
            }
        }
    }
}
