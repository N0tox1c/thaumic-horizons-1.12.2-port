//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import com.kentington.thaumichorizons.common.lib.BaubleInventory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class PacketNoMoreItems implements IMessage, IMessageHandler<PacketNoMoreItems, IMessage> {

    public IMessage onMessage(final PacketNoMoreItems message, final MessageContext ctx) {
        com.kentington.thaumichorizons.common.ThaumicHorizons.proxy.scheduleClientPacket(() -> applyClient(message));
        return null;
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    private void applyClient(final PacketNoMoreItems message) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        player.inventory.clear();
        BaubleInventory.clear(player);
        return;
    }

    public void fromBytes(final ByteBuf buf) {}

    public void toBytes(final ByteBuf buf) {}
}
