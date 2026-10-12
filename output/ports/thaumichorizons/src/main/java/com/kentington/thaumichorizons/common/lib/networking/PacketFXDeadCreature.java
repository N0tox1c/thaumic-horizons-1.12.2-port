//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.world.World;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class PacketFXDeadCreature implements IMessage, IMessageHandler<PacketFXDeadCreature, IMessage> {

    double x;
    double y;
    double z;

    public PacketFXDeadCreature() {}

    public PacketFXDeadCreature(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public IMessage onMessage(final PacketFXDeadCreature message, final MessageContext ctx) {
        com.kentington.thaumichorizons.common.ThaumicHorizons.proxy.scheduleClientPacket(() -> applyClient(message));
        return null;
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    private void applyClient(final PacketFXDeadCreature message) {
        final World world = Minecraft.getMinecraft().world;
        for (int i = 0; i < 36; ++i) {
            final com.kentington.thaumichorizons.client.fx.FXDeadCreature fb =
                    new com.kentington.thaumichorizons.client.fx.FXDeadCreature(world, message.x, message.y, message.z);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
        }
        com.kentington.thaumichorizons.common.lib.THSoundBridge.play(world, message.x + 0.5, message.y, message.z + 0.5, "thaumcraft:gore", 2.0f, 1.0f);
        return;
    }

    public void fromBytes(final ByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public void toBytes(final ByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
    }
}
