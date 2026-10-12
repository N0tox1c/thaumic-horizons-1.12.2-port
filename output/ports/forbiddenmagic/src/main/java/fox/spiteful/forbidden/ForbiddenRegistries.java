package fox.spiteful.forbidden;

import fox.spiteful.forbidden.blocks.ForbiddenBlocks;
import fox.spiteful.forbidden.items.ForbiddenItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.enchantment.Enchantment;
import fox.spiteful.forbidden.enchantments.DarkEnchantments;
import fox.spiteful.forbidden.Config;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Registration hook for ALL original ForbiddenMagic blocks and items. */
@Mod.EventBusSubscriber(modid = "forbiddenmagic")
public final class ForbiddenRegistries {
    private ForbiddenRegistries() {}

    @SubscribeEvent
    public static void onBlocks(RegistryEvent.Register<Block> event) {
        ForbiddenBlocks.registerBlocks(event);
    }

    @SubscribeEvent
    public static void onItems(RegistryEvent.Register<Item> event) {
        ForbiddenBlocks.registerBlockItems(event);
        ForbiddenItems.registerItems(event);
    }

    @SubscribeEvent
    public static void onEnchantments(RegistryEvent.Register<Enchantment> event) {
        if (!Config.enchanting) return;
        Enchantment[] all = { DarkEnchantments.cluster, DarkEnchantments.wrath,
                DarkEnchantments.greedy, DarkEnchantments.consuming,
                DarkEnchantments.educational, DarkEnchantments.corrupting,
                DarkEnchantments.voidtouched, DarkEnchantments.impact };
        for (Enchantment enchantment : all) {
            if (enchantment == null) throw new IllegalStateException("Original FM enchantment not initialized before registry event");
            event.getRegistry().register(enchantment);
        }
    }

    @SubscribeEvent
    public static void onRecipes(RegistryEvent.Register<IRecipe> event) {
        ForbiddenVanillaRecipes.register(event);
        fox.spiteful.forbidden.compat.ForbiddenBotaniaRecipes.register(event);
    }
}
