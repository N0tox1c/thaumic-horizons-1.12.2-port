package com.kentington.thaumichorizons.common.lib;

public interface OriginalItemColor {
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    int getColorFromItemStack(net.minecraft.item.ItemStack stack, int layer);
}
