//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import com.kentington.thaumichorizons.common.lib.EntityInfusionProperties;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketToggleInvisibleToServer
        implements IMessage, IMessageHandler<PacketToggleInvisibleToServer, IMessage> {

    private int playerid;
    private int dim;

    public PacketToggleInvisibleToServer() {}

    public PacketToggleInvisibleToServer(final EntityPlayer player, final int dim) {
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

    public IMessage onMessage(final PacketToggleInvisibleToServer message, final MessageContext ctx) {
        ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> applyServer(message, ctx));
        return null;
    }

    private void applyServer(final PacketToggleInvisibleToServer message, final MessageContext ctx) {
        if (!PacketHandler
                .selfInfusionSecurityCheck(ctx, "toggle chamelon skin (i.e. invisible)", message.playerid, 10)) {
            return;
        }
        final EntityPlayerMP player = ctx.getServerHandler().player;
        EntityInfusionProperties ieep = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player);
        ieep.toggleInvisible = !ieep.toggleInvisible;
        if (ieep.toggleInvisible) {
            player.removePotionEffect(net.minecraft.init.MobEffects.INVISIBILITY);
            player.setInvisible(false);
        } else {
            final PotionEffect effect = new PotionEffect(net.minecraft.init.MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, true, true);
            effect.setCurativeItems(new ArrayList<>());
            player.addPotionEffect(effect);
            player.setInvisible(true);
        }
        return;
    }
}
