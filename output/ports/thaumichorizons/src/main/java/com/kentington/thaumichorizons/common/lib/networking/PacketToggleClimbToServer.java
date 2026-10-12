//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.kentington.thaumichorizons.common.lib.EntityInfusionProperties;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketToggleClimbToServer implements IMessage, IMessageHandler<PacketToggleClimbToServer, IMessage> {

    private int playerid;
    private int dim;

    public PacketToggleClimbToServer() {}

    public PacketToggleClimbToServer(final EntityPlayer player, final int dim) {
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

    public IMessage onMessage(final PacketToggleClimbToServer message, final MessageContext ctx) {
        ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> applyServer(message, ctx));
        return null;
    }

    private void applyServer(final PacketToggleClimbToServer message, final MessageContext ctx) {
        if (!PacketHandler.selfInfusionSecurityCheck(ctx, "toggle spider climb", message.playerid, 9)) {
            return;
        }
        final EntityPlayer player = ctx.getServerHandler().player;
        if (player.dimension != message.dim) return;
        final World world = player.world;
        ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player)).toggleClimb = !((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player)).toggleClimb;
        return;
    }
}
