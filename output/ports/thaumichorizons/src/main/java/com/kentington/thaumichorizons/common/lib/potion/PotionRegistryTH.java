package com.kentington.thaumichorizons.common.lib.potion;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Forge 1.12.2 potion IDs come from the registry, never from 1.7.10 configuration. */
@Mod.EventBusSubscriber(modid = ThaumicHorizons.MODID)
public final class PotionRegistryTH {
    private PotionRegistryTH() {}

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        PotionVisBoost.instance = new PotionVisBoost(true, 6697847);
        PotionVisBoost.instance.setRegistryName("thaumichorizons", "vis_boost");
        PotionVisBoost.init();

        PotionVisRegen.instance = new PotionVisRegen(true, 5701759);
        PotionVisRegen.instance.setRegistryName("thaumichorizons", "vis_regen");
        PotionVisRegen.init();

        PotionVacuum.instance = new PotionVacuum(true, 4210752);
        PotionVacuum.instance.setRegistryName("thaumichorizons", "vacuum");
        PotionVacuum.init();

        PotionShock.instance = new PotionShock(true, 11513861);
        PotionShock.instance.setRegistryName("thaumichorizons", "shock");
        PotionShock.init();

        PotionSynthesis.instance = new PotionSynthesis(false, 3253280);
        PotionSynthesis.instance.setRegistryName("thaumichorizons", "synthesis");
        PotionSynthesis.init();

        event.getRegistry().registerAll(
                PotionVisBoost.instance, PotionVisRegen.instance, PotionVacuum.instance,
                PotionShock.instance, PotionSynthesis.instance);
    }
}
