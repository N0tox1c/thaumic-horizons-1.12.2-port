package com.kentington.thaumichorizons.client;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.items.ItemSyringeInjection;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Native 1.12.2 item models for the original injection/sampling chain.
 * Existing original textures are reused, including metadata-specific phials.
 */
@Mod.EventBusSubscriber(modid = ThaumicHorizons.MODID, value = Side.CLIENT)
public final class SyringeModelRegistration {
    private SyringeModelRegistration() {}

    private static void model(Item item, int metadata, String path) {
        ModelLoader.setCustomModelResourceLocation(item, metadata,
                new ModelResourceLocation("thaumichorizons:" + path, "inventory"));
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        model(ThaumicHorizons.itemSyringeInjection, 0, "syringeinjection");
        model(ThaumicHorizons.itemSyringeInjection, 1, "syringeinjection_phial");
        model(ThaumicHorizons.itemSyringeEmpty, 0, "syringeempty");
        model(ThaumicHorizons.itemSyringeEmpty, 2, "syringeempty");
        model(ThaumicHorizons.itemSyringeHuman, 0, "syringeblood");
        model(ThaumicHorizons.itemSyringeHuman, 1, "phialblood");
        model(ThaumicHorizons.itemSyringeBloodSample, 0, "syringebloodsample");
        model(ThaumicHorizons.itemInjector, 0, "injector");
    }

    @SubscribeEvent
    public static void registerColors(ColorHandlerEvent.Item event) {
        event.getItemColors().registerItemColorHandler(
                (stack, tintIndex) -> tintIndex == 0
                        ? ((ItemSyringeInjection) stack.getItem()).getInjectionColor(stack)
                        : 0xFFFFFF,
                ThaumicHorizons.itemSyringeInjection);
    }
}
