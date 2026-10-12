//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import com.kentington.thaumichorizons.common.lib.EntityInfusionProperties;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketPlayerInfusionSync implements IMessage, IMessageHandler<PacketPlayerInfusionSync, IMessage> {

    int[] infusions;
    String name;
    boolean toggleClimb;
    boolean toggleInvisible;

    public PacketPlayerInfusionSync() {
        this.infusions = new int[EntityInfusionProperties.NUM_INFUSIONS];
        this.name = "";
        this.toggleClimb = false;
        this.toggleInvisible = false;
    }

    public PacketPlayerInfusionSync(final String name, final int[] infusions, final boolean toggleClimb,
            final boolean toggleInvisible) {
        this.infusions = new int[EntityInfusionProperties.NUM_INFUSIONS];
        this.name = "";
        this.name = name;
        this.infusions = infusions;
        this.toggleClimb = toggleClimb;
        this.toggleInvisible = toggleInvisible;
    }

    public IMessage onMessage(final PacketPlayerInfusionSync message, final MessageContext ctx) {
        com.kentington.thaumichorizons.common.ThaumicHorizons.proxy.scheduleClientPacket(() -> applyClient(message));
        return null;
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    private void applyClient(final PacketPlayerInfusionSync message) {
        if (Minecraft.getMinecraft().world != null
                && Minecraft.getMinecraft().world.getPlayerEntityByName(message.name) != null
                && com.kentington.thaumichorizons.common.lib.InfusionCapability.get(Minecraft.getMinecraft().world.getPlayerEntityByName(message.name)) != null) {
            EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByName(message.name);
            EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player);
            prop.playerInfusions = message.infusions;
            if (prop.toggleClimb != message.toggleClimb) {
                prop.toggleClimb = message.toggleClimb;
            }
            if (prop.toggleInvisible != message.toggleInvisible) {
                prop.toggleInvisible = message.toggleInvisible;
                if (prop.toggleInvisible) {
                    player.removePotionEffect(net.minecraft.init.MobEffects.INVISIBILITY);
                    player.setInvisible(false);
                } else {
                    final PotionEffect effect = new PotionEffect(net.minecraft.init.MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, true, true);
                    effect.setCurativeItems(new ArrayList<>());
                    player.addPotionEffect(effect);
                    player.setInvisible(true);
                }
            }
        }
        return;
    }

    public void fromBytes(final ByteBuf buf) {
        final int length = buf.readInt();
        final byte[] bites = new byte[length];
        final char[] chars = new char[length];
        buf.readBytes(bites);
        for (int i = 0; i < length; ++i) {
            chars[i] = (char) bites[i];
        }
        this.name = String.copyValueOf(chars);
        for (int i = 0; i < EntityInfusionProperties.NUM_INFUSIONS; ++i) {
            this.infusions[i] = buf.readInt();
        }
        this.toggleClimb = buf.readBoolean();
        this.toggleInvisible = buf.readBoolean();
    }

    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.name.length());
        final byte[] bites = new byte[this.name.length()];
        final char[] chars = this.name.toCharArray();
        for (int i = 0; i < this.name.length(); ++i) {
            bites[i] = (byte) chars[i];
        }
        buf.writeBytes(bites);
        for (int i = 0; i < EntityInfusionProperties.NUM_INFUSIONS; ++i) {
            buf.writeInt(this.infusions[i]);
        }
        buf.writeBoolean(this.toggleClimb);
        buf.writeBoolean(this.toggleInvisible);
    }
}
