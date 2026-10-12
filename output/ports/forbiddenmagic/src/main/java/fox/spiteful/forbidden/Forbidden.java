package fox.spiteful.forbidden;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import fox.spiteful.forbidden.potions.DarkPotions;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import thaumcraft.api.wands.WandTriggerRegistry;

import fox.spiteful.forbidden.blocks.ForbiddenBlocks;
import fox.spiteful.forbidden.compat.Compat;
import fox.spiteful.forbidden.enchantments.DarkEnchantments;
import fox.spiteful.forbidden.items.ForbiddenItems;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Collection;

@Mod(modid = "forbiddenmagic", name = "Forbidden Magic", dependencies = "required-after:thaumcraft@[4.2.2.0,);after:thaumictinkerer;after:bloodmagic;after:botania;after:projecte")
public class Forbidden {
    @Instance("forbiddenmagic")
    public static Forbidden instance;
    public static CreativeTabs tab = new CreativeTabs("forbidden"){
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ForbiddenItems.fork);
        }
    };
    public static CreativeTabs crysTab;
    @SidedProxy(clientSide = "fox.spiteful.forbidden.client.ClientProxy", serverSide = "fox.spiteful.forbidden.CommonProxy")
    public static CommonProxy proxy;
    public static WandOverlord wandLord;
    public static FMEventHandler events;

    @EventHandler
    public void prelude(FMLPreInitializationEvent event) {
        instance = this;
        Config.configurate(event.getSuggestedConfigurationFile());
        if (Config.wrathCage)
            crysTab = new CreativeTabs("mobcrystal"){
                @Override
                public ItemStack createIcon() {
                    return new ItemStack(ForbiddenItems.mobCrystal);
                }
            };
        Compat.initiate();
        DarkAspects.initAspects();
        Config.spawnilify();
        ForbiddenItems.addItems();
        ForbiddenBlocks.addBlocks();
        DarkEnchantments.hex();
        // Potion registration is handled by DarkPotions.registerPotions(RegistryEvent).
        proxy.registerRenderInfo();
    }

    @EventHandler
    public void crescendo(FMLInitializationEvent event) {
        events = new FMEventHandler();
        MinecraftForge.EVENT_BUS.register(events);
        FMLCommonHandler.instance().bus().register(events);
        // Heretic profession and trades are registered through RegistryEvent.
    }

    @EventHandler
    public void outro(FMLPostInitializationEvent event) {
        DarkAspects.addAspects();
        ForbiddenRecipes.addRecipes();
        ForbiddenResearch.addResearch();
        Compat.compatify();

        wandLord = new WandOverlord();
        WandTriggerRegistry.registerWandBlockTrigger(wandLord, 1, Blocks.OBSIDIAN, 0);
        WandTriggerRegistry.registerWandBlockTrigger(wandLord, 1, Blocks.NETHERRACK, 0);
    }
}