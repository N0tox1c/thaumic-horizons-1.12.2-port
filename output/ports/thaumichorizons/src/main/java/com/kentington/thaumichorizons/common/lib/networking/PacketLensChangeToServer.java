//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.kentington.thaumichorizons.common.items.lenses.LensManager;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.nodes.IRevealer;

public class PacketLensChangeToServer implements IMessage, IMessageHandler<PacketLensChangeToServer, IMessage> {

    private int dim;
    private int playerid;
    private String lens;

    public PacketLensChangeToServer() {}

    public PacketLensChangeToServer(final EntityPlayer player, final String lens) {
        this.dim = player.world.provider.getDimension();
        this.playerid = player.getEntityId();
        this.lens = lens;
    }

    public void toBytes(final ByteBuf buffer) {
        buffer.writeInt(this.dim);
        buffer.writeInt(this.playerid);
        ByteBufUtils.writeUTF8String(buffer, this.lens);
    }

    public void fromBytes(final ByteBuf buffer) {
        this.dim = buffer.readInt();
        this.playerid = buffer.readInt();
        this.lens = ByteBufUtils.readUTF8String(buffer);
    }

    public IMessage onMessage(final PacketLensChangeToServer message, final MessageContext ctx) {
        ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> applyServer(message, ctx));
        return null;
    }

    private void applyServer(final PacketLensChangeToServer message, final MessageContext ctx) {
        final EntityPlayer player = ctx.getServerHandler().player;
        if (player.getEntityId() != message.playerid || player.dimension != message.dim) return;
        final World world = player.world;
        if (player instanceof EntityPlayer && ((EntityPlayer) player).inventory.armorItemInSlot(3) != null
                && ((EntityPlayer) player).inventory.armorItemInSlot(3).getItem() instanceof IRevealer) {
            LensManager.changeLens(
                    ((EntityPlayer) player).inventory.armorItemInSlot(3),
                    world,
                    (EntityPlayer) player,
                    message.lens);
        }
        return;
    }
}
