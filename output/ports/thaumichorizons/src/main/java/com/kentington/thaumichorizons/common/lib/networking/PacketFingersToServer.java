//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketFingersToServer implements IMessage, IMessageHandler<PacketFingersToServer, IMessage> {

    private int playerid;
    private int dim;

    public PacketFingersToServer() {}

    public PacketFingersToServer(final EntityPlayer player, final int dim) {
        this.playerid = player.getEntityId();
        this.dim = dim;
    }

    public void toBytes(final ByteBuf buffer) {
        buffer.writeInt(this.playerid);
        buffer.writeInt(this.dim);
    }

    public void fromBytes(final ByteBuf buffer) {
        this.playerid = buffer.readInt();
        this.dim = buffer.readInt();
    }

    public IMessage onMessage(final PacketFingersToServer message, final MessageContext ctx) {
        ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> applyServer(message, ctx));
        return null;
    }

    private void applyServer(final PacketFingersToServer message, final MessageContext ctx) {
        if (!PacketHandler.selfInfusionSecurityCheck(ctx, "open workbench", message.playerid, 2)) {
            return;
        }
        final EntityPlayer player = ctx.getServerHandler().player;
        if (player.dimension != message.dim) return;
        final World world = player.world;
        player.openGui(
                ThaumicHorizons.instance,
                9,
                player.world,
                (int) player.posX,
                (int) player.posY,
                (int) player.posZ);
        return;
    }
}
