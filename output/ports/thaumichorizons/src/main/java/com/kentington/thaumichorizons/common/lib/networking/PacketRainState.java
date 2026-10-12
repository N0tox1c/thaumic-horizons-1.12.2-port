package com.kentington.thaumichorizons.common.lib.networking;

import com.kentington.thaumichorizons.common.tiles.TileCloud;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class PacketRainState implements IMessage, IMessageHandler<PacketRainState, IMessage> {

    public PacketRainState(boolean raining) {
        this.raining = raining;
    }

    public PacketRainState() {
        raining = false;
    }

    private boolean raining;

    @Override
    public void fromBytes(ByteBuf buf) {
        raining = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(raining);
    }

    @Override
    public IMessage onMessage(final PacketRainState message, final MessageContext ctx) {
        com.kentington.thaumichorizons.common.ThaumicHorizons.proxy.scheduleClientPacket(() -> applyClient(message));
        return null;
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    private void applyClient(final PacketRainState message) {
        TileCloud.raining = message.raining;
        return;
    }
}
