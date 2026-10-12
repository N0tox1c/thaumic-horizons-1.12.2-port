//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;
import net.minecraft.util.math.BlockPos;


import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;


import com.kentington.thaumichorizons.common.tiles.TileVat;
import com.kentington.thaumichorizons.common.tiles.TileVatSlave;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TilePedestal;

public class PacketInfusionFX implements IMessage, IMessageHandler<PacketInfusionFX, IMessage> {

    private int x;
    private int y;
    private int z;
    private byte dx;
    private byte dy;
    private byte dz;
    private int color;

    public PacketInfusionFX() {}

    public PacketInfusionFX(final int x, final int y, final int z, final byte dx, final byte dy, final byte dz,
            final int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.color = color;
    }

    public void toBytes(final ByteBuf buffer) {
        buffer.writeInt(this.x);
        buffer.writeInt(this.y);
        buffer.writeInt(this.z);
        buffer.writeInt(this.color);
        buffer.writeByte(this.dx);
        buffer.writeByte(this.dy);
        buffer.writeByte(this.dz);
    }

    public void fromBytes(final ByteBuf buffer) {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.color = buffer.readInt();
        this.dx = buffer.readByte();
        this.dy = buffer.readByte();
        this.dz = buffer.readByte();
    }

    public IMessage onMessage(final PacketInfusionFX message, final MessageContext ctx) {
        // SimpleNetworkWrapper handlers run on Netty threads in 1.12.2.
        com.kentington.thaumichorizons.common.ThaumicHorizons.proxy.scheduleClientPacket(() -> applyClientFx(message));
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void applyClientFx(final PacketInfusionFX message) {
        final net.minecraft.world.World clientWorld = Thaumcraft.proxy.getClientWorld();
        if (clientWorld == null) return;
        final int tx = message.x - message.dx;
        final int ty = message.y - message.dy;
        final int tz = message.z - message.dz;
        final String key = tx + ":" + ty + ":" + tz + ":" + message.color;
        final TileEntity tile = clientWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
        if (tile instanceof TileVatSlave) {
            int count = 15;
            if (clientWorld.getTileEntity(new BlockPos(tx, ty, tz)) != null
                    && clientWorld.getTileEntity(new BlockPos(tx, ty, tz)) instanceof TilePedestal) {
                count = 60;
            }
            final TileVat is = ((TileVatSlave) tile).getBoss(-1);
            if (is == null) {
                return;
            }
            if (is.sourceFX.containsKey(key)) {
                final TileVat.SourceFX sf = is.sourceFX.get(key);
                sf.ticks = count;
                is.sourceFX.put(key, sf);
            } else {
                final HashMap<String, TileVat.SourceFX> sourceFX = is.sourceFX;
                sourceFX.put(key, new TileVat.SourceFX(new BlockPos(tx, ty, tz), count, message.color));
            }
        }
    }
}
