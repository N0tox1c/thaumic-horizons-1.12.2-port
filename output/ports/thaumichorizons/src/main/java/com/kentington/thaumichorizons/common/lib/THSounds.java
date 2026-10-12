package com.kentington.thaumichorizons.common.lib;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=ThaumicHorizons.MODID)
public final class THSounds {
    public static final SoundEvent VORTEX=new SoundEvent(new ResourceLocation("thaumichorizons","vortex"))
            .setRegistryName("thaumichorizons","vortex");
    private THSounds() {}
    @SubscribeEvent public static void register(RegistryEvent.Register<SoundEvent> event) { event.getRegistry().register(VORTEX); }
}
