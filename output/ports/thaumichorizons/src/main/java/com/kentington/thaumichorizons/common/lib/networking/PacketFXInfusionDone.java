//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import net.minecraft.client.Minecraft;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;

public class PacketFXInfusionDone implements IMessage, IMessageHandler<PacketFXInfusionDone, IMessage> {

    int x;
    int y;
    int z;

    public PacketFXInfusionDone() {}

    public PacketFXInfusionDone(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public IMessage onMessage(final PacketFXInfusionDone message, final MessageContext ctx) {
        com.kentington.thaumichorizons.common.ThaumicHorizons.proxy.scheduleClientPacket(() -> applyClient(message));
        return null;
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    private void applyClient(final PacketFXInfusionDone message) {
        Thaumcraft.proxy.blockSparkle(Minecraft.getMinecraft().world, message.x, message.y, message.z, -9999, 20);
        Thaumcraft.proxy
                .blockSparkle(Minecraft.getMinecraft().world, message.x, message.y - 1, message.z, -9999, 20);
        return;
    }

    public void fromBytes(final ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }
}
