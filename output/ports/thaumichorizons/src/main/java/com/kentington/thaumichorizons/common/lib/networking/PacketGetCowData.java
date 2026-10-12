//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.entities.EntityWizardCow;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class PacketGetCowData implements IMessage, IMessageHandler<PacketGetCowData, IMessage> {

    int id;
    static Aspect[] sorted;

    public PacketGetCowData() {}

    public PacketGetCowData(final int id) {
        this.id = id;
    }

    public IMessage onMessage(final PacketGetCowData message, final MessageContext ctx) {
        ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> applyServer(message, ctx));
        return null;
    }
    private void applyServer(PacketGetCowData message, MessageContext ctx) {
        final net.minecraft.entity.player.EntityPlayerMP player = ctx.getServerHandler().player;
        final World world = player.world;
        final Entity ent = world.getEntityByID(message.id);
        if (ent instanceof EntityWizardCow && player.getDistanceSq(ent) <= 64 * 64) { EntityWizardCow cow = (EntityWizardCow) ent;
            final AspectList ess = cow.getEssentia();
            final int mod = cow.nodeMod;
            final int type = cow.nodeType;
            final int[] types = new int[ess.size()];
            final int[] amounts = new int[ess.size()];
            int pointer = 0;
            for (final Aspect asp : ess.getAspects()) {
                amounts[pointer] = ess.getAmount(asp);
                for (int i = 0; i < PacketGetCowData.sorted.length; ++i) {
                    if (PacketGetCowData.sorted[i].getTag().equals(asp.getTag())) {
                        types[pointer] = i;
                        ++pointer;
                        break;
                    }
                }
            }
            PacketHandler.INSTANCE.sendTo(new PacketCowUpdate(types, amounts, type, mod, message.id), player);
        }
        return;
    }

    public void fromBytes(final ByteBuf buf) {
        this.id = buf.readInt();
    }

    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.id);
    }

    static {
        final AspectList list = new AspectList();
        for (String key : Aspect.aspects.keySet()) {
            list.add(Aspect.aspects.get(key), 1);
        }
        PacketGetCowData.sorted = list.getAspectsSorted();
    }
}
