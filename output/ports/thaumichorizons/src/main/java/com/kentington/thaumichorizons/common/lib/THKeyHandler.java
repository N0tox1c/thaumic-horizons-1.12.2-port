//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib;

import java.util.ArrayList;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import org.lwjgl.input.Keyboard;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.lib.networking.PacketFingersToServer;
import com.kentington.thaumichorizons.common.lib.networking.PacketHandler;
import com.kentington.thaumichorizons.common.lib.networking.PacketLensChangeToServer;
import com.kentington.thaumichorizons.common.lib.networking.PacketToggleClimbToServer;
import com.kentington.thaumichorizons.common.lib.networking.PacketToggleInvisibleToServer;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.nodes.IRevealer;

public class THKeyHandler {

    public KeyBinding keyV;
    public KeyBinding keyM;
    public KeyBinding keyC;
    public KeyBinding keyX;
    private boolean keyPressedM;
    public static long lastPressM;
    private boolean keyPressedC;
    public static long lastPressC;
    private boolean keyPressedX;
    public static long lastPressX;
    private boolean keyPressedV;
    public static boolean radialActive;
    public static boolean radialLock;
    public static long lastPressV;

    public THKeyHandler() {
        this.keyV = new KeyBinding("Change Arcane Lens", Keyboard.KEY_NONE, "Thaumcraft");
        this.keyM = new KeyBinding("Activate Morphic Fingers", Keyboard.KEY_NONE, "Thaumcraft");
        this.keyC = new KeyBinding("Toggle Spider Climb", Keyboard.KEY_NONE, "Thaumcraft");
        this.keyX = new KeyBinding("Toggle Chameleon Skin", Keyboard.KEY_NONE, "Thaumcraft");
        this.keyPressedM = false;
        this.keyPressedC = false;
        this.keyPressedX = false;
        this.keyPressedV = false;
        ClientRegistry.registerKeyBinding(this.keyV);
        ClientRegistry.registerKeyBinding(this.keyM);
        ClientRegistry.registerKeyBinding(this.keyC);
        ClientRegistry.registerKeyBinding(this.keyX);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void playerTick(final TickEvent.PlayerTickEvent event) {
        if (event.side == Side.SERVER) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            if (this.keyV.isKeyDown()) {
                if (FMLClientHandler.instance().getClient().inGameHasFocus) {
                    final EntityPlayer player = event.player;
                    if (player != null) {
                        if (!this.keyPressedV) {
                            THKeyHandler.lastPressV = System.currentTimeMillis();
                            THKeyHandler.radialLock = false;
                        }
                        if (!THKeyHandler.radialLock && player.inventory.armorItemInSlot(3) != null
                                && player.inventory.armorItemInSlot(3).getItem() instanceof IRevealer) {
                            if (player.isSneaking()) {
                                PacketHandler.INSTANCE.sendToServer(new PacketLensChangeToServer(player, "REMOVE"));
                            } else {
                                THKeyHandler.radialActive = true;
                            }
                        }
                    }
                    this.keyPressedV = true;
                }
            } else {
                THKeyHandler.radialActive = false;
                if (this.keyPressedV) {
                    THKeyHandler.lastPressV = System.currentTimeMillis();
                }
                this.keyPressedV = false;
            }
            if (this.keyM.isKeyDown()) {
                if (FMLClientHandler.instance().getClient().inGameHasFocus) {
                    final EntityPlayer player = event.player;
                    if (player != null) {
                        if (!this.keyPressedM) {
                            THKeyHandler.lastPressM = System.currentTimeMillis();
                        }
                        if (((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player))
                                .hasPlayerInfusion(2) && !this.keyPressedM) {
                            player.openGui(
                                    ThaumicHorizons.instance,
                                    9,
                                    player.world,
                                    (int) player.posX,
                                    (int) player.posY,
                                    (int) player.posZ);
                            PacketHandler.INSTANCE.sendToServer(new PacketFingersToServer(player, player.dimension));
                        }
                    }
                    this.keyPressedM = true;
                }
            } else {
                if (this.keyPressedM) {
                    THKeyHandler.lastPressM = System.currentTimeMillis();
                }
                this.keyPressedM = false;
            }
            if (this.keyC.isKeyDown()) {
                if (FMLClientHandler.instance().getClient().inGameHasFocus) {
                    final EntityPlayer player = event.player;
                    if (player != null) {
                        if (!this.keyPressedC) {
                            THKeyHandler.lastPressC = System.currentTimeMillis();
                        }
                        if (((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player))
                                .hasPlayerInfusion(9) && !this.keyPressedC) {
                            ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player)).toggleClimb = !((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player)).toggleClimb;
                            if (((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player)).toggleClimb) {
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.GRAY
                                                        + "Spider Climb disabled."));
                            } else {
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.GRAY
                                                        + "Spider Climb enabled."));
                            }
                            PacketHandler.INSTANCE
                                    .sendToServer(new PacketToggleClimbToServer(player, player.dimension));
                        }
                    }
                    this.keyPressedC = true;
                }
            } else {
                if (this.keyPressedC) {
                    THKeyHandler.lastPressC = System.currentTimeMillis();
                }
                this.keyPressedC = false;
            }
            if (this.keyX.isKeyDown()) {
                if (FMLClientHandler.instance().getClient().inGameHasFocus) {
                    final EntityPlayer player = event.player;
                    if (player != null) {
                        if (!this.keyPressedX) {
                            THKeyHandler.lastPressX = System.currentTimeMillis();
                        }
                        if (((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player))
                                .hasPlayerInfusion(10) && !this.keyPressedX) {
                            ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player)).toggleInvisible = !((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player)).toggleInvisible;
                            if (((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player)).toggleInvisible) {
                                player.removePotionEffect(net.minecraft.init.MobEffects.INVISIBILITY);
                                player.setInvisible(false);
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.GRAY
                                                        + "Chameleon Skin disabled."));
                            } else {
                                final PotionEffect effect = new PotionEffect(
                                        net.minecraft.init.MobEffects.INVISIBILITY,
                                        Integer.MAX_VALUE,
                                        0,
                                        true, true);
                                effect.setCurativeItems(new ArrayList<>());
                                player.addPotionEffect(effect);
                                player.setInvisible(true);
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.GRAY
                                                        + "Chameleon Skin enabled."));
                            }
                            PacketHandler.INSTANCE
                                    .sendToServer(new PacketToggleInvisibleToServer(player, player.dimension));
                        }
                    }
                    this.keyPressedX = true;
                }
            } else {
                if (this.keyPressedX) {
                    THKeyHandler.lastPressX = System.currentTimeMillis();
                }
                this.keyPressedX = false;
            }
        }
    }

    static {
        THKeyHandler.lastPressM = 0L;
        THKeyHandler.lastPressC = 0L;
        THKeyHandler.lastPressX = 0L;
        THKeyHandler.radialActive = false;
        THKeyHandler.radialLock = false;
        THKeyHandler.lastPressV = 0L;
    }
}
