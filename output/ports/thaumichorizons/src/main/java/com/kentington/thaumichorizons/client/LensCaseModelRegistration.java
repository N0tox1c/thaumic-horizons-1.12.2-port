package com.kentington.thaumichorizons.client;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/** Register the original lenscase texture in the 1.12 item model system. */
@Mod.EventBusSubscriber(modid = ThaumicHorizons.MODID, value = Side.CLIENT)
public final class LensCaseModelRegistration {
    private LensCaseModelRegistration() { }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(ThaumicHorizons.itemLensCase, 0,
                new ModelResourceLocation("thaumichorizons:lenscase", "inventory"));
    }
}
