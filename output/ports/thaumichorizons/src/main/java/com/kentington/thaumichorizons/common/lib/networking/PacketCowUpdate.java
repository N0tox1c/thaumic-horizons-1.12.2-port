//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import com.kentington.thaumichorizons.common.entities.EntityWizardCow;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.aspects.AspectList;

public class PacketCowUpdate implements IMessage, IMessageHandler<PacketCowUpdate, IMessage> {

    int[] aspects;
    int[] amounts;
    int type;
    int mod;
    int id;

    public PacketCowUpdate() {}

    public PacketCowUpdate(final int[] aspects, final int[] amounts, final int type, final int mod, final int id) {
        this.aspects = aspects;
        this.amounts = amounts;
        this.type = type;
        this.mod = mod;
        this.id = id;
    }

    public IMessage onMessage(final PacketCowUpdate message, final MessageContext ctx) {
        com.kentington.thaumichorizons.common.ThaumicHorizons.proxy.scheduleClientPacket(() -> applyClient(message));
        return null;
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    private void applyClient(final PacketCowUpdate message) {
        final Entity ent = Minecraft.getMinecraft().world.getEntityByID(message.id);
        if (ent instanceof EntityWizardCow) { EntityWizardCow cow = (EntityWizardCow) ent;
            cow.nodeMod = message.mod;
            cow.nodeType = message.type;
            cow.essentia = new AspectList();
            for (int i = 0; i < message.aspects.length; ++i) {
                cow.essentia.add(PacketGetCowData.sorted[message.aspects[i]], message.amounts[i]);
            }
        }
        return;
    }

    public void fromBytes(final ByteBuf buf) {
        this.id = buf.readInt();
        this.type = buf.readInt();
        this.mod = buf.readInt();
        int count = buf.readInt();
        if (count < 0 || count > PacketGetCowData.sorted.length || buf.readableBytes() < count * 8)
            throw new io.netty.handler.codec.DecoderException("Invalid cow aspect count: " + count);
        this.aspects = new int[count];
        this.amounts = new int[count];
        for (int i = 0; i < count; ++i) {
            this.aspects[i] = buf.readInt();
            this.amounts[i] = buf.readInt();
            if (this.aspects[i] < 0 || this.aspects[i] >= PacketGetCowData.sorted.length || this.amounts[i] < 0)
                throw new io.netty.handler.codec.DecoderException("Invalid cow aspect entry");
        }
    }

    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeInt(this.type);
        buf.writeInt(this.mod);
        buf.writeInt(this.aspects.length);
        for (int i = 0; i < this.aspects.length; ++i) {
            buf.writeInt(this.aspects[i]);
            buf.writeInt(this.amounts[i]);
        }
    }
}
