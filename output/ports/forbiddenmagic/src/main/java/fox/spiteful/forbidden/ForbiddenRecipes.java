package fox.spiteful.forbidden;

import java.util.Random;

import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import fox.spiteful.forbidden.compat.Compat;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.wands.WandCap;

import fox.spiteful.forbidden.blocks.ForbiddenBlocks;
import fox.spiteful.forbidden.enchantments.DarkEnchantments;
import fox.spiteful.forbidden.items.ForbiddenItems;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class ForbiddenRecipes {
    public static void addRecipes() {
        ForbiddenResearch.recipes.put("TaintShovel", ThaumcraftApi.addInfusionCraftingRecipe("TAINTSHOVEL", new ItemStack(ForbiddenItems.taintShovel), 5, (new AspectList()).add(Aspect.HEAL, 8).add(Aspect.CRYSTAL, 24).add(Aspect.MINE, 12), ItemApi.getItem("itemShovelThaumium", 0), new ItemStack[] { ItemApi.getItem("itemShard", 4), ItemApi.getItem("itemShard", 4), new ItemStack(Items.DIAMOND), ItemApi.getBlock("blockMagicalLog", 1) }));
        ForbiddenResearch.recipes.put("TaintPick", ThaumcraftApi.addInfusionCraftingRecipe("TAINTPICK", new ItemStack(ForbiddenItems.taintPickaxe), 1, (new AspectList()).add(Aspect.ENTROPY, 8).add(Aspect.MINE, 8).add(Aspect.TAINT, 12), ItemApi.getItem("itemPickThaumium", 0), new ItemStack[] { ItemApi.getItem("itemShard", 5), ItemApi.getItem("itemShard", 5), new ItemStack(Items.DIAMOND), ItemApi.getBlock("blockMagicalLog", 0) }));
        ForbiddenResearch.recipes.put("WandRodTainted", ThaumcraftApi.addInfusionCraftingRecipe("ROD_tainted", new ItemStack(ForbiddenItems.wandCore, 1, 0), 5, (new AspectList()).add(Aspect.TAINT, 24).add(Aspect.MAGIC, 12).add(Aspect.ENTROPY, 12), ItemApi.getItem("itemResource", 12), new ItemStack[]{ItemApi.getItem("itemResource", 14), new ItemStack(ForbiddenItems.deadlyShards, 1, 2), new ItemStack(ForbiddenItems.deadlyShards, 1, 2), new ItemStack(ForbiddenItems.deadlyShards, 1, 2), new ItemStack(ForbiddenItems.deadlyShards, 1, 2), new ItemStack(ForbiddenItems.deadlyShards, 1, 2), new ItemStack(ForbiddenItems.deadlyShards, 1, 2)}));
        ForbiddenResearch.recipes.put("TaintTree", ThaumcraftApi.addCrucibleRecipe("TAINTTREE", new ItemStack(ForbiddenBlocks.taintSapling, 1, 0), new ItemStack(Blocks.SAPLING, 1, 0), (new AspectList()).merge(Aspect.TAINT, 10).add(Aspect.POISON, 4)));
        GameRegistry.addSmelting(ForbiddenBlocks.taintLog, new ItemStack(ForbiddenItems.taintCoal, 1), 0.15F);
        OreDictionary.registerOre("resourceTaint", new ItemStack(ConfigItems.itemResource, 1, 11));
        OreDictionary.registerOre("resourceTaint", new ItemStack(ForbiddenItems.deadlyShards, 1, 2));
        ForbiddenResearch.recipes.put("TaintStone", ThaumcraftApi.addArcaneCraftingRecipe("TAINTSTONE", new ItemStack(ForbiddenBlocks.taintStone, 9, 0), (new AspectList()).add(Aspect.ENTROPY, 2).add(Aspect.ORDER, 1), new Object[]{"SSS", "SXS", "SSS", Character.valueOf('S'), new ItemStack(Blocks.STONE, 1), Character.valueOf('X'), "resourceTaint"}));

        ForbiddenResearch.recipes.put("SkullAxe", ThaumcraftApi.addInfusionCraftingRecipe("SKULLAXE", new ItemStack(ForbiddenItems.skullAxe), 1, (new AspectList()).add(DarkAspects.WRATH, 8).add(Aspect.WEAPON, 8).add(DarkAspects.NETHER, 8), ItemApi.getItem("itemAxeThaumium", 0), new ItemStack[] { new ItemStack(ForbiddenItems.deadlyShards, 1, 0), new ItemStack(ForbiddenItems.deadlyShards, 1, 0), new ItemStack(Items.DIAMOND), new ItemStack(Items.SKULL, 1, 1) }));
        if(!Config.noLust)
            ForbiddenResearch.recipes.put("SubCollar", ThaumcraftApi.addInfusionCraftingRecipe("SUBCOLLAR", new ItemStack(ForbiddenItems.subCollar), 1, (new AspectList()).add(DarkAspects.LUST, 8).add(Aspect.TRAP, 8).add(DarkAspects.NETHER, 4).add(Aspect.FLESH, 4), new ItemStack(ConfigItems.itemAmuletVis, 1, 1), new ItemStack[] { new ItemStack(ForbiddenItems.deadlyShards, 1, 4), new ItemStack(ForbiddenItems.deadlyShards, 1, 4), new ItemStack(ForbiddenItems.deadlyShards, 1, 4), new ItemStack(Items.LEAD, 1), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 2) }));
        if(Config.gluttony == 0) {
            ForbiddenResearch.recipes.put("ArcaneCake", ThaumcraftApi.addInfusionCraftingRecipe("ARCANECAKE", new ItemStack(ForbiddenItems.arcaneCakeItem), 3, (new AspectList()).add(DarkAspects.GLUTTONY, 12).add(Aspect.HUNGER, 24).add(Aspect.CRAFT, 24), new ItemStack(Items.CAKE), new ItemStack[]{ItemApi.getItem("itemResource", 14), new ItemStack(Items.EGG), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.EGG), new ItemStack(ForbiddenItems.gluttonyShard), new ItemStack(ForbiddenItems.gluttonyShard)}));
            ForbiddenResearch.recipes.put("RingFood", ThaumcraftApi.addArcaneCraftingRecipe("RINGFOOD", new ItemStack(ForbiddenItems.ringFood), (new AspectList()).add(Aspect.ENTROPY, 10).add(Aspect.WATER, 20).add(Aspect.EARTH, 20), new Object[]{" D ", "SXS", " S ", Character.valueOf('S'), new ItemStack(ForbiddenItems.gluttonyShard, 1), Character.valueOf('X'), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 1), Character.valueOf('D'), new ItemStack(Items.DIAMOND)}));
        }
        else if(Config.gluttony == 2){
            ForbiddenResearch.recipes.put("ArcaneCake", ThaumcraftApi.addInfusionCraftingRecipe("ARCANECAKE", new ItemStack(ForbiddenItems.arcaneCakeItem), 3, (new AspectList()).add(DarkAspects.GLUTTONY, 12).add(Aspect.HUNGER, 24).add(Aspect.CRAFT, 24), new ItemStack(Items.CAKE), new ItemStack[]{ItemApi.getItem("itemResource", 14), new ItemStack(ForbiddenBlocks.starBlock), new ItemStack(ForbiddenBlocks.starBlock), new ItemStack(Items.EGG), new ItemStack(ForbiddenItems.gluttonyShard), new ItemStack(ForbiddenItems.gluttonyShard)}));
            ForbiddenResearch.recipes.put("RingFood", ThaumcraftApi.addArcaneCraftingRecipe("RINGFOOD", new ItemStack(ForbiddenItems.ringFood), (new AspectList()).add(Aspect.ENTROPY, 10).add(Aspect.WATER, 20).add(Aspect.EARTH, 20), new Object[]{" D ", "SXS", " S ", Character.valueOf('S'), new ItemStack(ForbiddenItems.gluttonyShard, 1), Character.valueOf('X'), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 1), Character.valueOf('D'), new ItemStack(ForbiddenBlocks.starBlock)}));
        }
        ForbiddenResearch.recipes.put("FocusBlink", ThaumcraftApi.addInfusionCraftingRecipe("FOCUSBLINK", new ItemStack(ForbiddenItems.blinkFocus), 3, (new AspectList()).add(Aspect.TRAVEL, 25).add(DarkAspects.NETHER, 10).add(DarkAspects.SLOTH, 10).add(Aspect.ENTROPY, 25), new ItemStack(Items.ENDER_PEARL), new ItemStack[]{new ItemStack(Items.QUARTZ), new ItemStack(ForbiddenItems.deadlyShards, 1, 5), new ItemStack(Items.QUARTZ), new ItemStack(ForbiddenItems.deadlyShards, 1, 5), new ItemStack(Items.QUARTZ), new ItemStack(ForbiddenItems.deadlyShards, 1, 5)}));
        ForbiddenResearch.recipes.put("MorphPick", ThaumcraftApi.addInfusionCraftingRecipe("MORPHTOOLS", new ItemStack(ForbiddenItems.morphPickaxe), 6, (new AspectList()).add(Aspect.EXCHANGE, 32).add(DarkAspects.ENVY, 16).add(Aspect.TOOL, 16), ItemApi.getItem("itemPickThaumium", 0), new ItemStack[] { new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(Items.DIAMOND), ItemApi.getItem("itemResource", 3), ItemApi.getBlock("blockMagicalLog", 1) }));
        ForbiddenResearch.recipes.put("MorphSword", ThaumcraftApi.addInfusionCraftingRecipe("MORPHTOOLS", new ItemStack(ForbiddenItems.morphSword), 6, (new AspectList()).add(Aspect.EXCHANGE, 32).add(DarkAspects.ENVY, 16).add(Aspect.WEAPON, 16), ItemApi.getItem("itemSwordThaumium", 0), new ItemStack[] { new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(Items.DIAMOND), ItemApi.getItem("itemResource", 3), ItemApi.getBlock("blockMagicalLog", 1) }));
        ForbiddenResearch.recipes.put("MorphShovel", ThaumcraftApi.addInfusionCraftingRecipe("MORPHTOOLS", new ItemStack(ForbiddenItems.morphShovel), 6, (new AspectList()).add(Aspect.EXCHANGE, 32).add(DarkAspects.ENVY, 16).add(Aspect.TOOL, 16), ItemApi.getItem("itemShovelThaumium", 0), new ItemStack[] { new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(Items.DIAMOND), ItemApi.getItem("itemResource", 3), ItemApi.getBlock("blockMagicalLog", 1) }));
        ForbiddenResearch.recipes.put("MorphAxe", ThaumcraftApi.addInfusionCraftingRecipe("MORPHTOOLS", new ItemStack(ForbiddenItems.morphAxe), 6, (new AspectList()).add(Aspect.EXCHANGE, 32).add(DarkAspects.ENVY, 16).add(Aspect.TOOL, 16), ItemApi.getItem("itemAxeThaumium", 0), new ItemStack[] { new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(Items.DIAMOND), ItemApi.getItem("itemResource", 3), ItemApi.getBlock("blockMagicalLog", 1) }));
        ForbiddenResearch.recipes.put("WandRodInfernal", ThaumcraftApi.addInfusionCraftingRecipe("ROD_infernal", new ItemStack(ForbiddenItems.wandCore, 1, 1), 5, (new AspectList()).add(DarkAspects.NETHER, 32).add(Aspect.MAGIC, 12).add(DarkAspects.PRIDE, 12), new ItemStack(Items.BLAZE_ROD), new ItemStack[] { ItemApi.getItem("itemResource", 14), new ItemStack(ForbiddenItems.deadlyShards, 1, 3), new ItemStack(ForbiddenItems.deadlyShards, 1, 3), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Items.SKULL, 1, 1), new ItemStack(Items.QUARTZ), new ItemStack(Items.BLAZE_POWDER) }));
        if (Config.wrathCage) {
            ForbiddenResearch.recipes.put("WrathCage", ThaumcraftApi.addInfusionCraftingRecipe("WRATHCAGE", new ItemStack(ForbiddenBlocks.wrathCage, 1, 0), 10, (new AspectList()).add(DarkAspects.WRATH, 32).add(Aspect.MAGIC, 32).add(Aspect.BEAST, 32).add(Aspect.MECHANISM, 16), ItemApi.getBlock("blockCosmeticSolid", 4), new ItemStack[] { new ItemStack(ForbiddenItems.deadlyShards, 1, 0), new ItemStack(ForbiddenItems.deadlyShards, 1, 0), new ItemStack(ForbiddenItems.deadlyShards, 1, 0), new ItemStack(ForbiddenItems.deadlyShards, 1, 0), new ItemStack(Items.DIAMOND, 1, 0), new ItemStack(Items.DIAMOND, 1, 0), new ItemStack(Items.DIAMOND, 1, 0), new ItemStack(Items.DIAMOND, 1, 0), ItemApi.getBlock("blockJar", 0), ItemApi.getBlock("blockJar", 0), ItemApi.getBlock("blockJar", 0) }));
            ForbiddenResearch.recipes.put("MobCrystal", ThaumcraftApi.addCrucibleRecipe("WRATHCAGE", new ItemStack(ForbiddenItems.mobCrystal, 1, 0), new ItemStack(Items.DIAMOND, 1), (new AspectList()).merge(Aspect.MIND, 10).merge(Aspect.ENERGY, 10)));
        }

        ForbiddenResearch.recipes.put("Fork", ThaumcraftApi.addInfusionCraftingRecipe("FORK", new ItemStack(ForbiddenItems.fork, 1, 0), 1, (new AspectList()).add(DarkAspects.NETHER, 8).add(Aspect.MECHANISM, 8).add(Aspect.ENERGY, 8), ItemApi.getItem("itemSwordThaumium", 0), new ItemStack[] { new ItemStack(Items.QUARTZ), new ItemStack(Items.QUARTZ), new ItemStack(Items.QUARTZ), new ItemStack(Items.REDSTONE) }));

        if (Config.emeraldTrans)
            ForbiddenResearch.recipes.put("TransEmerald", ThaumcraftApi.addCrucibleRecipe("TRANSEMERALD", new ItemStack(ForbiddenItems.resource, 4, 0), "nuggetEmerald", (new AspectList()).merge(Aspect.CRYSTAL, 2).merge(Aspect.GREED, 2)));
        ForbiddenResearch.recipes.put("BlackFlower", ThaumcraftApi.addCrucibleRecipe("BLACKFLOWER", new ItemStack(ForbiddenBlocks.roseBush, 1, 0), new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), (new AspectList()).merge(Aspect.DARKNESS, 8).merge(Aspect.LIFE, 5)));

        if(thaumcraft.common.config.Config.researchDifficulty != -1)
            ForbiddenResearch.recipes.put("Crystalwell", ThaumcraftApi.addShapelessArcaneCraftingRecipe("CRYSTALWELL", new ItemStack(ForbiddenItems.crystalwell, 1, 0), (new AspectList()).add(Aspect.WATER, 1).add(Aspect.ORDER, 1), new Object[] { new ItemStack(ConfigItems.itemInkwell, 1, 32767), "dyeBlack", new ItemStack(ConfigItems.itemShard, 1, 32767), new ItemStack(ConfigItems.itemShard, 1, 32767) }));
        else
            ForbiddenResearch.recipes.put("Crystalwell", ThaumcraftApi.addShapelessArcaneCraftingRecipe("CRYSTALWELL", new ItemStack(ForbiddenItems.crystalwell, 1, 0), (new AspectList()).add(Aspect.WATER, 1).add(Aspect.ORDER, 1), new Object[] { new ItemStack(ConfigItems.itemInkwell, 1, 32767), "dyeBlack", new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 6) }));

        ForbiddenResearch.recipes.put("Primewell", ThaumcraftApi.addShapelessArcaneCraftingRecipe("PRIMEWELL", new ItemStack(ForbiddenItems.primewell, 1, 0), (new AspectList()).add(Aspect.WATER, 50).add(Aspect.EARTH, 50).add(Aspect.FIRE, 50).add(Aspect.AIR, 50).add(Aspect.ORDER, 50).add(Aspect.ENTROPY, 50), new Object[] { new ItemStack(Items.FEATHER, 1, 0), new ItemStack(ConfigItems.itemEldritchObject, 1, 3), new ItemStack(Items.GLASS_BOTTLE, 1, 0) }));

        if(Config.enchanting) {
            if (Config.greedyEnch)
                ForbiddenResearch.recipes.put("Greedy", ThaumcraftApi.addInfusionEnchantmentRecipe("GREEDY", DarkEnchantments.greedy, 4, (new AspectList()).add(DarkAspects.NETHER, 16).add(Aspect.WEAPON, 8).add(Aspect.GREED, 16), new ItemStack[]{new ItemStack(Items.GOLDEN_SWORD), new ItemStack(Items.DIAMOND), new ItemStack(ForbiddenItems.deadlyShards, 1, 6), new ItemStack(ForbiddenItems.deadlyShards, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 14)}));
            ForbiddenResearch.recipes.put("Consuming", ThaumcraftApi.addInfusionEnchantmentRecipe("CONSUMING", DarkEnchantments.consuming, 3, (new AspectList()).add(Aspect.VOID, 8).add(Aspect.TOOL, 8).add(Aspect.HUNGER, 8), new ItemStack[]{new ItemStack(Items.IRON_PICKAXE), new ItemStack(Items.LAVA_BUCKET), ItemApi.getItem("itemResource", 14)}));
            ForbiddenResearch.recipes.put("Wrath", ThaumcraftApi.addInfusionEnchantmentRecipe("WRATH", DarkEnchantments.wrath, 8, (new AspectList()).add(DarkAspects.WRATH, 16).add(Aspect.WEAPON, 16).add(DarkAspects.NETHER, 8), new ItemStack[]{ItemApi.getItem("itemResource", 14), new ItemStack(Items.DIAMOND_SWORD, 1, 0), new ItemStack(ForbiddenItems.deadlyShards, 1, 0), new ItemStack(ForbiddenItems.deadlyShards, 1, 0), new ItemStack(ForbiddenItems.deadlyShards, 1, 0)}));
            ForbiddenResearch.recipes.put("Educational", ThaumcraftApi.addInfusionEnchantmentRecipe("EDUCATIONAL", DarkEnchantments.educational, 3, (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.WEAPON, 4).add(Aspect.MIND, 8), new ItemStack[]{ItemApi.getItem("itemZombieBrain", 0), new ItemStack(Items.BOOK), ItemApi.getItem("itemResource", 14)}));
            ForbiddenResearch.recipes.put("Corrupting", ThaumcraftApi.addInfusionEnchantmentRecipe("CORRUPTING", DarkEnchantments.corrupting, 4, (new AspectList()).add(DarkAspects.NETHER, 16).add(Aspect.EXCHANGE, 16).add(Aspect.CRYSTAL, 8), new ItemStack[]{new ItemStack(Items.NETHER_WART), new ItemStack(Blocks.SOUL_SAND), ItemApi.getItem("itemResource", 14)}));

            ForbiddenResearch.recipes.put("Cluster", ThaumcraftApi.addInfusionEnchantmentRecipe("CLUSTER", DarkEnchantments.cluster, 3, (new AspectList()).add(Aspect.FIRE, 4).add(Aspect.METAL, 4).add(Aspect.GREED, 4), new ItemStack[]{ItemApi.getItem("itemPickElemental", 0), ItemApi.getItem("itemResource", 14)}));
            ForbiddenResearch.recipes.put("Impact", ThaumcraftApi.addInfusionEnchantmentRecipe("IMPACT", DarkEnchantments.impact, 4, (new AspectList()).add(Aspect.ENTROPY, 16).add(Aspect.MINE, 16), new ItemStack[]{ItemApi.getItem("itemResource", 14), new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(ConfigItems.itemShovelElemental, 1, 0)}));
            ForbiddenResearch.recipes.put("Voidtouched", ThaumcraftApi.addInfusionEnchantmentRecipe("VOIDTOUCHED", DarkEnchantments.voidtouched, 8, (new AspectList()).add(Aspect.VOID, 16).add(Aspect.DARKNESS, 16).add(DarkAspects.ENVY, 24).add(Aspect.ELDRITCH, 16), new ItemStack[]{ItemApi.getItem("itemResource", 14), ItemApi.getItem("itemResource", 16), ItemApi.getItem("itemResource", 16), ItemApi.getItem("itemResource", 16), new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(ForbiddenItems.deadlyShards, 1, 1), new ItemStack(ForbiddenItems.deadlyShards, 1, 1)}));
        }

        ThaumcraftApi.addSmeltingBonus(new ItemStack(Items.EMERALD), new ItemStack(ForbiddenItems.resource, 0, 0));

        GameRegistry.registerFuelHandler(new IFuelHandler() {
            Random randy = new Random();
            @Override
            public int getBurnTime(ItemStack fuel) {

                if(fuel.getItem() == ForbiddenItems.taintCoal) {
                    return 1 + randy.nextInt(2400);
                }
                return 0;
            }
        });

        OreDictionary.registerOre("logWood", new ItemStack(ConfigBlocks.blockMagicalLog, 1, OreDictionary.WILDCARD_VALUE));

    }
}