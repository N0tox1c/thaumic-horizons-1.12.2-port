package fox.spiteful.forbidden.items;

import java.util.Arrays;

import fox.spiteful.forbidden.compat.Compat;
import fox.spiteful.forbidden.items.baubles.*;
import fox.spiteful.forbidden.items.scribes.*;
import fox.spiteful.forbidden.items.tools.*;
import fox.spiteful.forbidden.Config;
import fox.spiteful.forbidden.items.wands.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import net.minecraftforge.event.RegistryEvent;

public class ForbiddenItems {
    private static final List<Item> PENDING = new ArrayList<>();

    private static void enqueue(Item item, String legacyId) {
        item.setRegistryName("forbiddenmagic", legacyId.toLowerCase(Locale.ROOT));
        PENDING.add(item);
    }

    /** Forge 1.12 RegistryEvent.Register<Item>, called after the preInit creation pass. */
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : PENDING) event.getRegistry().register(item);
    }

    public static List<Item> allRegisteredItems() {
        return Collections.unmodifiableList(PENDING);
    }

    public static Item deadlyShards;
    public static Item gluttonyShard;
    public static Item skullAxe;
    public static Item arcaneCakeItem;
    public static Item taintShovel;
    public static Item wandCore;
    public static Item wandCap;
    public static Item resource;
    public static Item taintPickaxe;
    public static Item mobCrystal;
    public static Item fork;
    public static Item morphPickaxe;
    public static Item morphSword;
    public static Item morphShovel;
    public static Item morphAxe;
    public static Item crystalwell;
    public static Item primewell;
    public static Item ridingCrop;
    public static Item subCollar;
    public static Item ringFood;
    public static Item taintFruit;
    public static Item taintCoal;

    public static Item blinkFocus;

    public static Item bloodRapier;
    public static Item bloodwell;
    public static Item bloodOrb;
    public static Item boundwell;

    public static Item dragonslayer;

    public static WandRod WAND_ROD_TAINTED;
    public static WandRod WAND_ROD_INFERNAL;
    public static WandRod WAND_ROD_NEUTRONIUM;
    public static WandRod WAND_ROD_BLOOD;
    public static WandRod WAND_ROD_WITCHWOOD;
    public static WandRod WAND_ROD_LIVINGWOOD;
    public static WandRod WAND_ROD_DREAMWOOD;
    public static WandRod WAND_ROD_EQUIVALENT;
    public static WandRod WAND_ROD_PROFANE;
    public static WandRod WAND_ROD_PROFANED;
    public static StaffRod STAFF_ROD_BLOOD;
    public static StaffRod STAFF_ROD_NEUTRONIUM;
    public static StaffRod STAFF_ROD_WITCHWOOD;
    public static StaffRod STAFF_ROD_DREAMWOOD;
    public static WandCap WAND_CAP_ORICHALCUM;
    public static WandCap WAND_CAP_ALCHEMICAL;
    public static WandCap WAND_CAP_VINTEUM;
    public static WandCap WAND_CAP_MANASTEEL;
    public static WandCap WAND_CAP_ELEMENTIUM;
    public static WandCap WAND_CAP_TERRASTEEL;

    public static void addItems() {
        deadlyShards = new ItemDeadlyShard().setTranslationKey("NetherShard");
        enqueue(deadlyShards, "NetherShard");

        gluttonyShard = new ItemGluttonyShard().setTranslationKey("GluttonyShard");
        enqueue(gluttonyShard, "GluttonyShard");

        taintShovel = new ItemTaintShovel(ThaumcraftApi.toolMatElemental).setTranslationKey("TaintShovel");
        enqueue(taintShovel, "TaintShovel");

        taintPickaxe = new ItemTaintPickaxe(ThaumcraftApi.toolMatElemental).setTranslationKey("TaintPickaxe");
        enqueue(taintPickaxe, "TaintPickaxe");

        arcaneCakeItem = new ItemArcaneCake().setTranslationKey("ArcaneCake");
        enqueue(arcaneCakeItem, "ArcaneCakeItem");

        skullAxe = new ItemSkullAxe(ThaumcraftApi.toolMatElemental).setTranslationKey("SkullAxe");
        enqueue(skullAxe, "SkullAxe");

        morphPickaxe = new ItemMorphPickaxe(ThaumcraftApi.toolMatElemental).setTranslationKey("MorphPickaxe");
        enqueue(morphPickaxe, "MorphPickaxe");

        morphSword = new ItemMorphSword(ThaumcraftApi.toolMatElemental).setTranslationKey("MorphSword");
        enqueue(morphSword, "MorphSword");

        morphShovel = new ItemMorphShovel(ThaumcraftApi.toolMatElemental).setTranslationKey("MorphShovel");
        enqueue(morphShovel, "MorphShovel");

        morphAxe = new ItemMorphAxe(ThaumcraftApi.toolMatElemental).setTranslationKey("MorphAxe");
        enqueue(morphAxe, "MorphAxe");

        if (Config.wrathCage) {
            mobCrystal = new ItemMobCrystal().setTranslationKey("MobCrystal");
            enqueue(mobCrystal, "MobCrystal");
        }

        fork = new ItemDiabolistFork(ThaumcraftApi.toolMatThaumium).setTranslationKey("DiabolistFork");
        if (Config.spork)
            fork.setTranslationKey("DiabolistSpork");
        enqueue(fork, "DiabolistFork");

        wandCore = new ItemWandCores().setTranslationKey("WandCores");
        enqueue(wandCore, "WandCores");
        wandCap = new ItemWandCaps().setTranslationKey("WandCaps");
        enqueue(wandCap, "WandCaps");
        WAND_ROD_TAINTED = new WandRod("tainted", 150, new ItemStack(wandCore, 1, 0), 12, new TaintedWandUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_tainted.png"));
        WAND_ROD_INFERNAL = new WandRod("infernal", 150, new ItemStack(wandCore, 1, 1), 12, new InfernalWandUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_infernal.png"));
        WAND_ROD_NEUTRONIUM = new WandRod("neutronium", 9001, new ItemStack(Blocks.BEDROCK, 1), 1000, new CreativeWandUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_neutronium.png"));
        WAND_ROD_NEUTRONIUM.setGlowing(true);
        WAND_ROD_BLOOD = new WandRod("blood", 100, new ItemStack(wandCore, 1, 3), 12, new BloodWandUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_blood.png"));
        WAND_ROD_WITCHWOOD = new WandRod("witchwood", 100, new ItemStack(wandCore, 1, 4), 12, new ManaWandUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_witchwood.png"));
        WAND_ROD_LIVINGWOOD = new WandRod("livingwood", 100, new ItemStack(wandCore, 1, 7), 12, new YandereWandUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_livingwood.png"));
        WAND_ROD_DREAMWOOD = new WandRod("dreamwood", 100, new ItemStack(wandCore, 1, 11), 12, new YandereWandUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_dreamwood.png"));
        WAND_ROD_EQUIVALENT = new WandRod("equivalent", 100, new ItemStack(wandCore, 1, 11), 12, new ResourceLocation("forbidden", "textures/models/wand_rod_equivalent.png"));
        WAND_ROD_PROFANE = new WandRod("profane", 50, new ItemStack(wandCore, 1, 5), 12, new ProfaneWandUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_profane.png"));
        WAND_ROD_PROFANED = new WandRod("profaned", 50, new ItemStack(Blocks.BEDROCK, 1), 1000, new ResourceLocation("forbidden", "textures/models/wand_rod_profaned.png"));
        STAFF_ROD_BLOOD = new StaffRod("blood", 50, new ItemStack(wandCore, 1, 9), 24, new BloodStaffUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_blood.png"));
        STAFF_ROD_NEUTRONIUM = new StaffRod("neutronium", 9002, new ItemStack(Blocks.BEDROCK, 1), 1000, new CreativeWandUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_neutronium.png"));
        STAFF_ROD_NEUTRONIUM.setGlowing(true);
        STAFF_ROD_WITCHWOOD = new StaffRod("witchwood", 50, new ItemStack(wandCore, 1, 10), 24, new ManaStaffUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_witchwood.png"));
        STAFF_ROD_DREAMWOOD = new StaffRod("dreamwood", 250, new ItemStack(wandCore, 1, 13), 25, new YandereWandUpdate(), new ResourceLocation("forbidden", "textures/models/wand_rod_dreamwood.png"));
        STAFF_ROD_DREAMWOOD.setRunes(true);
        WAND_CAP_ORICHALCUM = new DarkWandCap("orichalcum", 0.0F, new ItemStack(Blocks.COMMAND_BLOCK, 1), 1000, new ResourceLocation("forbidden", "textures/models/wand_cap_orichalcum.png"));
        WAND_CAP_ALCHEMICAL = new DarkWandCap("alchemical", 0.9F, Arrays.asList(new Aspect[] { Aspect.WATER }), 0.8F, new ItemStack(wandCap, 1, 0), 7, new ResourceLocation("forbidden", "textures/models/wand_cap_alchemical.png"));
        WAND_CAP_VINTEUM = new DarkWandCap("vinteum", 0.9F, new ItemStack(wandCap, 1, 1), 6, new ResourceLocation("forbidden", "textures/models/wand_cap_vinteum.png"));
        WAND_CAP_MANASTEEL = new DarkWandCap("manasteel", 0.9F, new ItemStack(wandCap, 1, 3), 6, new ResourceLocation("forbidden", "textures/models/wand_cap_manasteel.png"));
        WAND_CAP_ELEMENTIUM = new DarkWandCap("elementium", 0.8F, new ItemStack(wandCap, 1, 5), 9, new ResourceLocation("forbidden", "textures/models/wand_cap_elementium.png"));
        WAND_CAP_TERRASTEEL = new DarkWandCap("terrasteel", 1.8F, new ItemStack(wandCap, 1, 2), 1, new ResourceLocation("forbidden", "textures/models/wand_cap_terrasteel.png"));


        resource = new ItemResource().setTranslationKey("FMResource");
        enqueue(resource, "FMResource");
        OreDictionary.registerOre("nuggetEmerald", new ItemStack(resource, 1, 0));
        OreDictionary.registerOre("dyeBlack", new ItemStack(resource, 1, 1));

        if (Compat.bm) {
            try {
                bloodwell = new ItemBloodwell().setTranslationKey("Bloodwell");
                enqueue(bloodwell, "Bloodwell");

                bloodOrb = new ItemDivineOrb().setTranslationKey("EldritchOrb");
                enqueue(bloodOrb, "EldritchOrb");

                boundwell = new ItemBoundwell().setTranslationKey("Boundwell");
                enqueue(boundwell, "Boundwell");

                bloodRapier = new ItemBloodRapier().setTranslationKey("BloodRapier");
                enqueue(bloodRapier, "BloodRapier");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        crystalwell = new ItemCrystalwell().setTranslationKey("Crystalwell");
        enqueue(crystalwell, "Crystalwell");

        primewell = new ItemPrimewell().setTranslationKey("Primewell");
        enqueue(primewell, "Primewell");

        ridingCrop = new ItemRidingCrop(ToolMaterial.WOOD).setTranslationKey("RidingCrop");
        enqueue(ridingCrop, "RidingCrop");

        if(!Config.noLust) {
            subCollar = new ItemSubCollar().setTranslationKey("SubCollar");
            enqueue(subCollar, "SubCollar");

        }

        ringFood = new ItemRingNutrition().setTranslationKey("RingNutrition");
        enqueue(ringFood, "RingNutrition");

        taintFruit = new ItemFruitTainted().setTranslationKey("TaintFruit");
        enqueue(taintFruit, "TaintFruit");

        taintCoal = new ItemTaintCoal().setTranslationKey("TaintCoal");
        enqueue(taintCoal, "TaintCoal");

        blinkFocus = new ItemFocusBlink().setTranslationKey("BlinkFocus");
        enqueue(blinkFocus, "BlinkFocus");

    }

    public static FocusUpgradeType getUpgrade(int id, ResourceLocation icon, String name, String text, AspectList aspects){
        if (id >= FocusUpgradeType.types.length) {
            FocusUpgradeType[] temp = new FocusUpgradeType[id+1];
            System.arraycopy(FocusUpgradeType.types, 0, temp, 0, FocusUpgradeType.types.length);
            FocusUpgradeType.types = temp;
        }
        return new FocusUpgradeType(id, icon, name, text, aspects);
    }
}