//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

import com.kentington.thaumichorizons.common.items.lenses.LensManager;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class PacketRemoveNightvision implements IMessage, IMessageHandler<PacketRemoveNightvision, IMessage> {

    public IMessage onMessage(final PacketRemoveNightvision message, final MessageContext ctx) {
        com.kentington.thaumichorizons.common.ThaumicHorizons.proxy.scheduleClientPacket(() -> applyClient(message));
        return null;
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    private void applyClient(final PacketRemoveNightvision message) {
        if (com.kentington.thaumichorizons.common.items.lenses.ItemLensFire.isEffectGrantedByLens(
                Minecraft.getMinecraft().player.getActivePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION))) {
            Minecraft.getMinecraft().player.removePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION);
        }
        LensManager.nightVisionOffTime = Minecraft.getSystemTime() + 100L;
        return;
    }

    public void fromBytes(final ByteBuf buf) {}

    public void toBytes(final ByteBuf buf) {}
}
