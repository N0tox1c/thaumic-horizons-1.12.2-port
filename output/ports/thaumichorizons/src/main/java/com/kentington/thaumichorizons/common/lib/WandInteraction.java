package com.kentington.thaumichorizons.common.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/** Hand and reach handling for the TC4 wand callbacks. */
public final class WandInteraction {
    private WandInteraction() {}

    public static void startUsing(EntityPlayer player, ItemStack stack) {
        for (EnumHand hand : EnumHand.values()) {
            if (player.getHeldItem(hand) == stack) {
                player.setActiveHand(hand);
                return;
            }
        }
        throw new IllegalArgumentException("Wand callback stack is not held by the player");
    }

    public static RayTraceResult rayTrace(World world, EntityPlayer player, boolean liquids) {
        double reach = player instanceof EntityPlayerMP
                ? ((EntityPlayerMP) player).interactionManager.getBlockReachDistance() : 5.0;
        Vec3d eyes = player.getPositionEyes(1);
        return world.rayTraceBlocks(eyes, eyes.add(player.getLook(1).scale(reach)), liquids, !liquids, false);
    }
}
