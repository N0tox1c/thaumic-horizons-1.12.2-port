package com.kentington.thaumichorizons.common.lib;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Original item sprite selectors, consumed by the native baked item models. */
public interface OriginalItemAppearance {
    @SideOnly(Side.CLIENT) TextureAtlasSprite getIconFromDamage(int metadata);
    @SideOnly(Side.CLIENT) default TextureAtlasSprite getOriginalIcon(ItemStack stack,int pass) {
        return getIconFromDamage(stack.getMetadata());
    }
    @SideOnly(Side.CLIENT) default int getOriginalRenderPasses() { return 1; }
}
