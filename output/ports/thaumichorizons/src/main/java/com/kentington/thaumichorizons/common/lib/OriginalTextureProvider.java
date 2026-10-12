package com.kentington.thaumichorizons.common.lib;

/** Typed atlas registration. The client signature is stripped on a dedicated server. */
public interface OriginalTextureProvider {
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    void registerIcons(net.minecraft.client.renderer.texture.TextureMap atlas);
}
