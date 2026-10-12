package com.kentington.thaumichorizons.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thaumcraft.common.config.ConfigItems;
import com.kentington.thaumichorizons.common.items.crafting.RecipesFocusIlluminationDyes;
import com.kentington.thaumichorizons.common.items.crafting.RecipeVoidPuttyRepair;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Full original 1.7 registrations, migrated to the Forge 1.12 registry events. */
@Mod.EventBusSubscriber(modid = ThaumicHorizons.MODID)
public final class ThaumicHorizonsRegistries {
    private static final List<Block> BLOCKS = new ArrayList<>();
    private static final List<Item> ITEMS = new ArrayList<>();
    private static final List<Item> BLOCK_ITEMS = new ArrayList<>();
    private static IRecipe chocolateBarRecipe;
    private ThaumicHorizonsRegistries() {}

    private static ResourceLocation id(String original) {
        String path = original.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_.-]+", "_");
        while (path.startsWith("_")) path = path.substring(1);
        while (path.endsWith("_")) path = path.substring(0, path.length()-1);
        return new ResourceLocation("thaumichorizons", path);
    }

    public static void enqueueItem(Item item, String legacyName) {
        item.setRegistryName(id(legacyName));
        ITEMS.add(item);
    }

    public static void enqueueBlock(Block block, String legacyName) {
        enqueueBlock(block, new ItemBlock(block), legacyName);
    }

    /** Retain original custom itemBlock field, important for jar soul metadata/NBT. */
    public static void enqueueBlock(Block block, ItemBlock item, String legacyName) {
        block.setRegistryName(id(legacyName));
        item.setRegistryName(block.getRegistryName());
        BLOCKS.add(block);
        BLOCK_ITEMS.add(item);
    }

    public static void enqueueBlock(Block block, java.util.function.Function<Block, ? extends ItemBlock> factory, String legacyName) {
        enqueueBlock(block, factory.apply(block), legacyName);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : BLOCKS) event.getRegistry().register(block);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : BLOCK_ITEMS) event.getRegistry().register(item);
        for (Item item : ITEMS) event.getRegistry().register(item);
        net.minecraftforge.oredict.OreDictionary.registerOre("dyeBlack", ThaumicHorizons.itemInkEgg);
    }
    /** Original six triple-meat combinations plus the two special TH crafting recipes. */
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        // TC4 Port represents edible nuggets as itemNuggetEdible metadata 0..3,
        // not the four removed 1.7.10 ConfigItems.itemNugget* fields.
        Item meat = ThaumicHorizons.itemMeatNugget;
        ItemStack chicken = new ItemStack(ConfigItems.itemNuggetEdible, 1, 0);
        ItemStack beef = new ItemStack(ConfigItems.itemNuggetEdible, 1, 1);
        ItemStack pork = new ItemStack(ConfigItems.itemNuggetEdible, 1, 2);
        ItemStack fish = new ItemStack(ConfigItems.itemNuggetEdible, 1, 3);

        registerTripleMeat(event, "triple_meat_chicken_pork", meat, chicken, pork);
        registerTripleMeat(event, "triple_meat_chicken_beef", meat, chicken, beef);
        registerTripleMeat(event, "triple_meat_chicken_fish", meat, chicken, fish);
        registerTripleMeat(event, "triple_meat_pork_beef", meat, pork, beef);
        registerTripleMeat(event, "triple_meat_pork_fish", meat, pork, fish);
        registerTripleMeat(event, "triple_meat_beef_fish", meat, beef, fish);

        // Original chocolate processing chain: bucket -> bar -> block -> nine bars.
        ResourceLocation chocolateBarId = new ResourceLocation("thaumichorizons", "chocolate_bar");
        chocolateBarRecipe = new ShapedOreRecipe(chocolateBarId,
                new ItemStack(ThaumicHorizons.itemBarChocolate),
                "M", 'M', new ItemStack(ThaumicHorizons.itemBucketChocolate));
        chocolateBarRecipe.setRegistryName(chocolateBarId);
        event.getRegistry().register(chocolateBarRecipe);

        ResourceLocation chocolateBlockId = new ResourceLocation("thaumichorizons", "chocolate_block");
        IRecipe chocolateBlock = new ShapelessOreRecipe(chocolateBlockId,
                new ItemStack(ThaumicHorizons.blockChocolate),
                new ItemStack(ThaumicHorizons.itemBarChocolate),
                new ItemStack(ThaumicHorizons.itemBarChocolate),
                new ItemStack(ThaumicHorizons.itemBarChocolate),
                new ItemStack(ThaumicHorizons.itemBarChocolate),
                new ItemStack(ThaumicHorizons.itemBarChocolate),
                new ItemStack(ThaumicHorizons.itemBarChocolate),
                new ItemStack(ThaumicHorizons.itemBarChocolate),
                new ItemStack(ThaumicHorizons.itemBarChocolate),
                new ItemStack(ThaumicHorizons.itemBarChocolate));
        chocolateBlock.setRegistryName(chocolateBlockId);
        event.getRegistry().register(chocolateBlock);

        ResourceLocation chocolateUnblockId = new ResourceLocation("thaumichorizons", "chocolate_bars_from_block");
        IRecipe chocolateUnblock = new ShapelessOreRecipe(chocolateUnblockId,
                new ItemStack(ThaumicHorizons.itemBarChocolate, 9),
                new ItemStack(ThaumicHorizons.blockChocolate));
        chocolateUnblock.setRegistryName(chocolateUnblockId);
        event.getRegistry().register(chocolateUnblock);

        IRecipe dye = new RecipesFocusIlluminationDyes();
        dye.setRegistryName(new ResourceLocation("thaumichorizons", "focus_illumination_dyes"));
        event.getRegistry().register(dye);
        IRecipe repair = new RecipeVoidPuttyRepair();
        repair.setRegistryName(new ResourceLocation("thaumichorizons", "void_putty_repair"));
        event.getRegistry().register(repair);
    }

    /** The exact registered crafting recipe is also displayed in Thaumonomicon research. */
    public static IRecipe getChocolateBarRecipe() {
        if (chocolateBarRecipe == null) {
            throw new IllegalStateException("Chocolate recipe not registered before Thaumic Horizons research");
        }
        return chocolateBarRecipe;
    }

    private static void registerTripleMeat(RegistryEvent.Register<IRecipe> event, String id,
                                           Item first, ItemStack second, ItemStack third) {
        ResourceLocation name = new ResourceLocation("thaumichorizons", id);
        IRecipe recipe = new ShapelessOreRecipe(name,
                new ItemStack(ConfigItems.itemTripleMeatTreat), Items.SUGAR, first, second, third);
        recipe.setRegistryName(name);
        event.getRegistry().register(recipe);
    }

}
