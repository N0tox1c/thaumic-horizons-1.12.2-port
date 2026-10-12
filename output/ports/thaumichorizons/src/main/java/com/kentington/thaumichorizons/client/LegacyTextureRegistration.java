package com.kentington.thaumichorizons.client;

import java.util.Locale;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Connects the retained original icon selectors to the native block/item atlas. */
public final class LegacyTextureRegistration {
    public static ResourceLocation sprite(String name,String directory) {
        int split=name.indexOf(':');
        String domain=split<0?"minecraft":name.substring(0,split).toLowerCase(Locale.ROOT);
        String path=split<0?name:name.substring(split+1);
        return new ResourceLocation(domain,directory+"/"+path);
    }
    @SubscribeEvent public void stitch(TextureStitchEvent.Pre event) {
        for(Block block:Block.REGISTRY) register(block,event.getMap());
        for(Item item:Item.REGISTRY) register(item,event.getMap());
    }
    private void register(Object object,TextureMap atlas) {
        if (object instanceof com.kentington.thaumichorizons.common.lib.OriginalTextureProvider)
            ((com.kentington.thaumichorizons.common.lib.OriginalTextureProvider)object).registerIcons(atlas);
    }
    @SubscribeEvent public void colors(net.minecraftforge.client.event.ColorHandlerEvent.Item event) {
        for (Item item : Item.REGISTRY) {
            if (item instanceof com.kentington.thaumichorizons.common.lib.OriginalItemColor) {
                com.kentington.thaumichorizons.common.lib.OriginalItemColor provider =
                        (com.kentington.thaumichorizons.common.lib.OriginalItemColor)item;
                event.getItemColors().registerItemColorHandler(provider::getColorFromItemStack, item);
            }
        }
    }
}
