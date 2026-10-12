//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kentington.thaumichorizons.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;
import com.kentington.thaumichorizons.client.lib.RenderEventHandler;
import com.kentington.thaumichorizons.common.blocks.BlockAlchemite;
import com.kentington.thaumichorizons.common.blocks.BlockBloodInfuser;
import com.kentington.thaumichorizons.common.blocks.BlockBone;
import com.kentington.thaumichorizons.common.blocks.BlockBrain;
import com.kentington.thaumichorizons.common.blocks.BlockChocolate;
import com.kentington.thaumichorizons.common.blocks.BlockCloud;
import com.kentington.thaumichorizons.common.blocks.BlockCloudItem;
import com.kentington.thaumichorizons.common.blocks.BlockCrystal;
import com.kentington.thaumichorizons.common.blocks.BlockCrystalDeep;
import com.kentington.thaumichorizons.common.blocks.BlockCrystalItem;
import com.kentington.thaumichorizons.common.blocks.BlockDust;
import com.kentington.thaumichorizons.common.blocks.BlockEssentiaDynamo;
import com.kentington.thaumichorizons.common.blocks.BlockEvanescent;
import com.kentington.thaumichorizons.common.blocks.BlockEyes;
import com.kentington.thaumichorizons.common.blocks.BlockFlesh;
import com.kentington.thaumichorizons.common.blocks.BlockGatewayPortal;
import com.kentington.thaumichorizons.common.blocks.BlockInspiratron;
import com.kentington.thaumichorizons.common.blocks.BlockLeviathan;
import com.kentington.thaumichorizons.common.blocks.BlockLight;
import com.kentington.thaumichorizons.common.blocks.BlockLightSolar;
import com.kentington.thaumichorizons.common.blocks.BlockModifiedMatrix;
import com.kentington.thaumichorizons.common.blocks.BlockNodeMonitor;
import com.kentington.thaumichorizons.common.blocks.BlockPortalTH;
import com.kentington.thaumichorizons.common.blocks.BlockRecombinator;
import com.kentington.thaumichorizons.common.blocks.BlockSlot;
import com.kentington.thaumichorizons.common.blocks.BlockSoulBeacon;
import com.kentington.thaumichorizons.common.blocks.BlockSoulExtractor;
import com.kentington.thaumichorizons.common.blocks.BlockSoulJar;
import com.kentington.thaumichorizons.common.blocks.BlockSoulJarItem;
import com.kentington.thaumichorizons.common.blocks.BlockSoulforge;
import com.kentington.thaumichorizons.common.blocks.BlockSpike;
import com.kentington.thaumichorizons.common.blocks.BlockSyntheticNode;
import com.kentington.thaumichorizons.common.blocks.BlockTransductionAmplifier;
import com.kentington.thaumichorizons.common.blocks.BlockVat;
import com.kentington.thaumichorizons.common.blocks.BlockVatInterior;
import com.kentington.thaumichorizons.common.blocks.BlockVatSolid;
import com.kentington.thaumichorizons.common.blocks.BlockVisDynamo;
import com.kentington.thaumichorizons.common.blocks.BlockVoid;
import com.kentington.thaumichorizons.common.blocks.BlockVortex;
import com.kentington.thaumichorizons.common.blocks.BlockVortexItem;
import com.kentington.thaumichorizons.common.blocks.BlockVortexStabilizer;
import com.kentington.thaumichorizons.common.blocks.MaterialPortalTH;
import com.kentington.thaumichorizons.common.entities.EntityAlchemitePrimed;
import com.kentington.thaumichorizons.common.entities.EntityBlastPhial;
import com.kentington.thaumichorizons.common.entities.EntityBoatGreatwood;
import com.kentington.thaumichorizons.common.entities.EntityBoatThaumium;
import com.kentington.thaumichorizons.common.entities.EntityChocolateCow;
import com.kentington.thaumichorizons.common.entities.EntityChromaticSheep;
import com.kentington.thaumichorizons.common.entities.EntityEggIncubated;
import com.kentington.thaumichorizons.common.entities.EntityEndersteed;
import com.kentington.thaumichorizons.common.entities.EntityFamiliar;
import com.kentington.thaumichorizons.common.entities.EntityGoldChicken;
import com.kentington.thaumichorizons.common.entities.EntityGolemTH;
import com.kentington.thaumichorizons.common.entities.EntityGravekeeper;
import com.kentington.thaumichorizons.common.entities.EntityGuardianPanther;
import com.kentington.thaumichorizons.common.entities.EntityHorseUndead;
import com.kentington.thaumichorizons.common.entities.EntityHorseUndeadS;
import com.kentington.thaumichorizons.common.entities.EntityLightningBoltFinite;
import com.kentington.thaumichorizons.common.entities.EntityLunarWolf;
import com.kentington.thaumichorizons.common.entities.EntityMeatSlime;
import com.kentington.thaumichorizons.common.entities.EntityMedSlime;
import com.kentington.thaumichorizons.common.entities.EntityMercurialSlime;
import com.kentington.thaumichorizons.common.entities.EntityNetherHound;
import com.kentington.thaumichorizons.common.entities.EntityNightmare;
import com.kentington.thaumichorizons.common.entities.EntityOrePig;
import com.kentington.thaumichorizons.common.entities.EntityScholarChicken;
import com.kentington.thaumichorizons.common.entities.EntitySeawolf;
import com.kentington.thaumichorizons.common.entities.EntitySelfShearingSheep;
import com.kentington.thaumichorizons.common.entities.EntitySheeder;
import com.kentington.thaumichorizons.common.entities.EntitySoul;
import com.kentington.thaumichorizons.common.entities.EntitySyringe;
import com.kentington.thaumichorizons.common.entities.EntityTaintPig;
import com.kentington.thaumichorizons.common.entities.EntityVoltSlime;
import com.kentington.thaumichorizons.common.entities.ItemSpawnerEgg;
import com.kentington.thaumichorizons.common.handlers.BehaviorDispenseBoat;
import com.kentington.thaumichorizons.common.items.ItemAmuletMirror;
import com.kentington.thaumichorizons.common.items.ItemBarChocolate;
import com.kentington.thaumichorizons.common.items.ItemBoatGreatwood;
import com.kentington.thaumichorizons.common.items.ItemBoatThaumium;
import com.kentington.thaumichorizons.common.items.ItemBucketChocolate;
import com.kentington.thaumichorizons.common.items.ItemCorpseEffigy;
import com.kentington.thaumichorizons.common.items.ItemCrystalWand;
import com.kentington.thaumichorizons.common.items.ItemDummy;
import com.kentington.thaumichorizons.common.items.ItemDummyVat;
import com.kentington.thaumichorizons.common.items.ItemEggIncubated;
import com.kentington.thaumichorizons.common.items.ItemFocusAnimation;
import com.kentington.thaumichorizons.common.items.ItemFocusContainment;
import com.kentington.thaumichorizons.common.items.ItemFocusDisintegration;
import com.kentington.thaumichorizons.common.items.ItemFocusIllumination;
import com.kentington.thaumichorizons.common.items.ItemFocusLiquefaction;
import com.kentington.thaumichorizons.common.items.ItemGoldEgg;
import com.kentington.thaumichorizons.common.items.ItemGolemBellTH;
import com.kentington.thaumichorizons.common.items.ItemGolemPlacer;
import com.kentington.thaumichorizons.common.items.ItemGolemPowder;
import com.kentington.thaumichorizons.common.items.ItemIceCream;
import com.kentington.thaumichorizons.common.items.ItemInfusionCheat;
import com.kentington.thaumichorizons.common.items.ItemInfusionSelfCheat;
import com.kentington.thaumichorizons.common.items.ItemInjector;
import com.kentington.thaumichorizons.common.items.ItemInkEgg;
import com.kentington.thaumichorizons.common.items.ItemKeystone;
import com.kentington.thaumichorizons.common.items.ItemMeat;
import com.kentington.thaumichorizons.common.items.ItemMeatCooked;
import com.kentington.thaumichorizons.common.items.ItemMeatNugget;
import com.kentington.thaumichorizons.common.items.ItemNodeCheat;
import com.kentington.thaumichorizons.common.items.ItemNutrients;
import com.kentington.thaumichorizons.common.items.ItemPlanarConduit;
import com.kentington.thaumichorizons.common.items.ItemSuicidePill;
import com.kentington.thaumichorizons.common.items.ItemSyringeBlood;
import com.kentington.thaumichorizons.common.items.ItemSyringeBloodSample;
import com.kentington.thaumichorizons.common.items.ItemSyringeEmpty;
import com.kentington.thaumichorizons.common.items.ItemSyringeInjection;
import com.kentington.thaumichorizons.common.items.ItemVoidPutty;
import com.kentington.thaumichorizons.common.items.ItemWandCastingDisposable;
import com.kentington.thaumichorizons.common.items.crafting.RecipeVoidPuttyRepair;
import com.kentington.thaumichorizons.common.items.crafting.RecipesFocusIlluminationDyes;
import com.kentington.thaumichorizons.common.items.lenses.ItemLensAir;
import com.kentington.thaumichorizons.common.items.lenses.ItemLensCase;
import com.kentington.thaumichorizons.common.items.lenses.ItemLensEarth;
import com.kentington.thaumichorizons.common.items.lenses.ItemLensFire;
import com.kentington.thaumichorizons.common.items.lenses.ItemLensOrderEntropy;
import com.kentington.thaumichorizons.common.items.lenses.ItemLensWater;
import com.kentington.thaumichorizons.common.lib.CreativeTabTH;
import com.kentington.thaumichorizons.common.lib.CreatureInfusionRecipe;
import com.kentington.thaumichorizons.common.lib.EventHandlerEntity;
import com.kentington.thaumichorizons.common.lib.EventHandlerWorld;
import com.kentington.thaumichorizons.common.lib.PageFormatText;
import com.kentington.thaumichorizons.common.lib.SelfInfusionRecipe;
import com.kentington.thaumichorizons.common.lib.WorldProviderPocketPlane;
import com.kentington.thaumichorizons.common.lib.networking.PacketHandler;
import com.kentington.thaumichorizons.common.lib.potion.PotionShock;
import com.kentington.thaumichorizons.common.lib.potion.PotionSynthesis;
import com.kentington.thaumichorizons.common.lib.potion.PotionVacuum;
import com.kentington.thaumichorizons.common.lib.potion.PotionVisBoost;
import com.kentington.thaumichorizons.common.lib.potion.PotionVisRegen;
import com.kentington.thaumichorizons.common.tiles.TileBloodInfuser;
import com.kentington.thaumichorizons.common.tiles.TileCloud;
import com.kentington.thaumichorizons.common.tiles.TileEssentiaDynamo;
import com.kentington.thaumichorizons.common.tiles.TileInspiratron;
import com.kentington.thaumichorizons.common.tiles.TileLight;
import com.kentington.thaumichorizons.common.tiles.TileNodeMonitor;
import com.kentington.thaumichorizons.common.tiles.TileRecombinator;
import com.kentington.thaumichorizons.common.tiles.TileSlot;
import com.kentington.thaumichorizons.common.tiles.TileSoulBeacon;
import com.kentington.thaumichorizons.common.tiles.TileSoulExtractor;
import com.kentington.thaumichorizons.common.tiles.TileSoulJar;
import com.kentington.thaumichorizons.common.tiles.TileSoulforge;
import com.kentington.thaumichorizons.common.tiles.TileSpike;
import com.kentington.thaumichorizons.common.tiles.TileSyntheticNode;
import com.kentington.thaumichorizons.common.tiles.TileTransductionAmplifier;
import com.kentington.thaumichorizons.common.tiles.TileVat;
import com.kentington.thaumichorizons.common.tiles.TileVatConnector;
import com.kentington.thaumichorizons.common.tiles.TileVatMatrix;
import com.kentington.thaumichorizons.common.tiles.TileVatSlave;
import com.kentington.thaumichorizons.common.tiles.TileVisDynamo;
import com.kentington.thaumichorizons.common.tiles.TileVortex;
import com.kentington.thaumichorizons.common.tiles.TileVortexStabilizer;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.api.wands.WandTriggerRegistry;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.config.research.ConfigResearch;
import thaumcraft.common.entities.monster.EntityPech;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.lib.utils.Utils;

@Mod(
        modid = ThaumicHorizons.MODID,
        name = "Thaumic Horizons",
        version = ThaumicHorizons.VERSION,
        dependencies = "required-after:forge@[14.23.5.2847,);required-after:thaumcraft@[4.2.3.4,)",
        guiFactory = "com.kentington.thaumichorizons.common.lib.THGuiFactory")
public class ThaumicHorizons {

    public static final String MODID = "thaumichorizons";
    public static final String VERSION = "0.0.1-fullsource-port";

    @Instance(ThaumicHorizons.MODID)
    public static ThaumicHorizons instance;

    @SidedProxy(
            clientSide = "com.kentington.thaumichorizons.client.ClientProxy",
            serverSide = "com.kentington.thaumichorizons.common.CommonProxy")
    public static CommonProxy proxy;

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public RenderEventHandler renderEventHandler;
    public EventHandlerEntity eventHandlerEntity;
    public EventHandlerWorld eventHandlerWorld;
    public static Configuration config;
    public static final Logger log = LogManager.getLogger("THAUMICHORIZONS");
    public static int dimensionPocketId = 69;
    public static int providerID = 69;
    public static int blockJarRI = -1;
    public static int blockSyntheticNodeRI = -1;
    public static int blockNodeMonRI = -1;
    public static int blockVisDynamoRI = -1;
    public static int blockEssentiaDynamoRI = -1;
    public static int blockBloodInfuserRI = -1;
    public static int blockSoulSieveRI = -1;
    public static int blockInspiratronRI = -1;
    public static int blockSoulforgeRI = -1;
    public static int blockVatRI = -1;
    public static int blockVatSolidRI = -1;
    public static int blockVatInteriorRI = -1;
    public static int blockVatMatrixRI = -1;
    public static int blockSoulBeaconRI = -1;
    public static int blockLightRI = -1;
    public static int blockLightSolarRI = -1;
    public static int blockTransducerRI = -1;
    public static int blockRecombinatorRI = -1;
    public static int blockVortexStabilizerRI = -1;
    public static int blockVortexRI = -1;
    public static int blockSpikeRI = -1;
    public static int blockSlotRI = -1;
    public static int potionVisBoostID = -1;
    public static int potionVisRegenID = -1;
    public static int potionVacuumID = -1;
    public static int potionShockID = -1;
    public static int potionSynthesisID = -1;
    public static Item itemSpawnerEgg;
    public static Item itemEggIncubated;
    public static Item itemLensFire;
    public static Item itemLensWater;
    public static Item itemLensEarth;
    public static Item itemLensAir;
    public static Item itemLensOrderEntropy;
    public static Item itemLensCase;
    public static Item itemFocusContainment;
    public static Item itemFocusLiquefaction;
    public static Item itemFocusDisintegration;
    public static Item itemFocusIllumination;
    public static Item itemFocusAnimation;
    public static Item blockSoulJarItem;
    public static Item itemAmuletMirror;
    public static Item itemSyringeEmpty;
    public static Item itemSyringeHuman;
    public static Item itemSyringeBloodSample;
    public static Item itemSyringeInjection;
    public static Item itemCorpseEffigy;
    public static Item itemNutrients;
    public static Item itemDummy;
    public static Item itemBarChocolate;
    public static Item itemIceCream;
    public static Item itemBucketChocolate;
    public static Item itemInkEgg;
    public static Item itemGoldEgg;
    public static Item itemInjector;
    public static Item itemInfusionCheat;
    public static Item itemInfusionSelfCheat;
    public static Item itemSuicidePill;
    public static Item itemGolemPowder;
    public static Item itemGolemPlacer;
    public static Item itemGolemBellTH;
    public static Item itemBoatGreatwood;
    public static Item itemBoatThaumium;
    public static Item itemDummyVat;
    public static Item itemPlanarConduit;
    public static Item itemKeystone;
    public static Item itemVoidPutty;
    public static Item itemCrystalWand;
    public static Item itemWandCastingDisposable;
    public static Item itemMeat;
    public static Item itemMeatCooked;
    public static Item itemMeatNugget;
    public static Item itemNodeCheat;
    public static Block blockSynthNode;
    public static Block blockAlchemite;
    public static Block blockVisDynamo;
    public static Block blockEssentiaDynamo;
    public static Block blockNodeMonitor;
    public static Block blockSoulSieve;
    public static Block blockInspiratron;
    public static Block blockSoulforge;
    public static Block blockJar;
    public static Block blockBloodInfuser;
    public static Block blockVat;
    public static Block blockVatSolid;
    public static Block blockVatInterior;
    public static Block blockSoulBeacon;
    public static Block blockModifiedMatrix;
    public static Block blockChocolate;
    public static Block blockEvanescent;
    public static Block blockLight;
    public static Block blockLightSolar;
    public static Block blockTransducer;
    public static Block blockRecombinator;
    public static Block blockVortexStabilizer;
    public static Block blockVortex;
    public static Block blockVoid;
    public static Block blockCloud;
    public static Block blockCloudGlowing;
    public static Block blockBrain;
    public static Block blockDust;
    public static Block blockEye;
    public static Block blockBone;
    public static Block blockFlesh;
    public static Block blockSpike;
    public static Block blockSpikeWood;
    public static Block blockSpikeTooth;
    public static Block blockCrystal;
    public static Block blockCrystalDeep;
    public static Block blockLeviathan;
    public static Block blockSlot;
    public static Block blockPortal;
    public static Block blockGateway;
    public static final Material portal;
    public static ShapedArcaneRecipe recipeSyringe;
    public static ShapedArcaneRecipe recipeLensCase;
    public static ShapedArcaneRecipe recipeGreatwoodBoat;
    public static ShapedArcaneRecipe recipeThaumiumBoat;
    public static ShapedArcaneRecipe recipeConduit;
    public static ShapedArcaneRecipe recipeCrystalWand;
    public static ShapedArcaneRecipe recipeTransducer;
    public static ShapelessArcaneRecipe recipeBlastPhial;
    public static CrucibleRecipe recipeAlcheponicsWheat;
    public static CrucibleRecipe recipeAlcheponicsCarrot;
    public static CrucibleRecipe recipeAlcheponicsPotato;
    public static CrucibleRecipe recipeAlcheponicsMelon;
    public static CrucibleRecipe recipeAlcheponicsPumpkin;
    public static CrucibleRecipe recipeAlchIncubation;
    public static CrucibleRecipe recipeMeatGrowthPork;
    public static CrucibleRecipe recipeMeatGrowthBeef;
    public static CrucibleRecipe recipeMeatGrowthChicken;
    public static CrucibleRecipe recipeLeatherBeef;
    public static CrucibleRecipe recipeLeatherPork;
    public static CrucibleRecipe recipeLeatherZombie;
    public static CrucibleRecipe shardTransFire;
    public static CrucibleRecipe shardTransWater;
    public static CrucibleRecipe shardTransAir;
    public static CrucibleRecipe shardTransEarth;
    public static CrucibleRecipe shardTransOrder;
    public static CrucibleRecipe shardTransEntropy;
    public static CrucibleRecipe recipeAlchemite;
    public static CrucibleRecipe recipeNutrients;
    public static CrucibleRecipe recipeFocusIllumination;
    public static CrucibleRecipe recipeGolemPowder;
    public static InfusionRecipe recipeSilverwoodSapling;
    public static InfusionRecipe recipeEssentiaBrew;
    public static InfusionRecipe recipeEtherealShard;
    public static InfusionRecipe recipeVisDynamo;
    public static InfusionRecipe recipeEssentiaDynamo;
    public static InfusionRecipe recipeNodeMonitor;
    public static InfusionRecipe recipeSoulSieve;
    public static InfusionRecipe recipeInspiratron;
    public static InfusionRecipe recipeSoulforge;
    public static InfusionRecipe recipeCorpseEffigy;
    public static InfusionRecipe recipeSoulBeacon;
    public static InfusionRecipe recipeFocusContainment;
    public static InfusionRecipe recipeFocusLiquefaction;
    public static InfusionRecipe recipeFocusDisintegration;
    public static InfusionRecipe recipeFocusAnimation;
    public static InfusionRecipe recipeAmuletMirror;
    public static InfusionRecipe recipeBloodInfuser;
    public static InfusionRecipe recipeInjector;
    public static InfusionRecipe recipeModifiedMatrix;
    public static InfusionRecipe recipeLensFire;
    public static InfusionRecipe recipeLensWater;
    public static InfusionRecipe recipeLensAir;
    public static InfusionRecipe recipeLensEarth;
    public static InfusionRecipe recipeLensOrderEntropy;
    public static InfusionRecipe recipeEnchantClay;
    public static InfusionRecipe recipeKeystone;
    public static InfusionRecipe recipeKeystone1;

    public static InfusionRecipe recipeVortexStabilizer;
    public static InfusionRecipe recipeRecombinator;
    public static InfusionRecipe recipeSlot;
    public static WandRod ROD_CRYSTAL;
    public static WandCap CAP_CRYSTAL;
    public static CreativeTabs tabTH;
    public static HashMap<Item, Integer> incarnationItems;
    public static ArrayList<CreatureInfusionRecipe> critterRecipes;
    public static ArrayList<SelfInfusionRecipe> selfRecipes;
    public static ArrayList<Class> classBanList;
    private static boolean useAlternateBell;
    public static boolean enablePocket;
    public static int warpedTumorValue;

    public ThaumicHorizons() {}

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            this.intitializeConfig(event.getSuggestedConfigurationFile());
        } catch (Exception var8) {
            log.error("Thaumic Horizons could not load its configuration file");
        } finally {
            if (config != null && config.hasChanged()) {
                config.save();
            }
        }

        this.eventHandlerEntity = new EventHandlerEntity();
        this.eventHandlerWorld = new EventHandlerWorld();
        com.kentington.thaumichorizons.common.lib.InfusionCapability.register();
        MinecraftForge.EVENT_BUS.register(new com.kentington.thaumichorizons.common.lib.InfusionCapability());
        PacketHandler.init();
        MinecraftForge.EVENT_BUS.register(this.eventHandlerEntity);
        MinecraftForge.EVENT_BUS.register(this.eventHandlerWorld);
        FMLCommonHandler.instance().bus().register(this.eventHandlerEntity);
        DimensionType pocketType = DimensionType.register("TH Pocket", "_th_pocket", providerID, WorldProviderPocketPlane.class, true);
        DimensionManager.registerDimension(dimensionPocketId, pocketType);
        ROD_CRYSTAL = new WandRod(
                "disposable",
                250,
                new ItemStack(Blocks.BEDROCK),
                99,
                new ResourceLocation("thaumichorizons", "textures/models/crystalRod.png"));
        CAP_CRYSTAL = new WandCap("disposable", 0.9F, new ItemStack(Blocks.BEDROCK), 99);
        CAP_CRYSTAL.setTexture(new ResourceLocation("thaumichorizons", "textures/models/crystalCap.png"));
        ROD_CRYSTAL.setGlowing(true);
        blockSynthNode = new BlockSyntheticNode();
        blockAlchemite = new BlockAlchemite();
        blockVisDynamo = new BlockVisDynamo(Material.IRON);
        blockEssentiaDynamo = new BlockEssentiaDynamo(Material.IRON);
        blockNodeMonitor = new BlockNodeMonitor();
        blockSoulSieve = new BlockSoulExtractor();
        blockInspiratron = new BlockInspiratron();
        blockSoulforge = new BlockSoulforge();
        blockJar = new BlockSoulJar();
        blockBloodInfuser = new BlockBloodInfuser();
        blockVat = new BlockVat();
        blockVatSolid = new BlockVatSolid();
        blockVatInterior = new BlockVatInterior();
        blockSoulBeacon = new BlockSoulBeacon();
        blockModifiedMatrix = new BlockModifiedMatrix();
        blockChocolate = new BlockChocolate();
        blockEvanescent = new BlockEvanescent();
        blockLight = new BlockLight();
        blockLightSolar = new BlockLightSolar();
        blockTransducer = new BlockTransductionAmplifier();
        blockRecombinator = new BlockRecombinator();
        blockVortexStabilizer = new BlockVortexStabilizer();
        blockVortex = new BlockVortex();
        blockVoid = new BlockVoid();
        blockCloud = new BlockCloud(false);
        blockCloudGlowing = new BlockCloud(true);
        blockBrain = new BlockBrain();
        blockDust = new BlockDust();
        blockEye = new BlockEyes();
        blockBone = new BlockBone();
        blockFlesh = new BlockFlesh();
        blockSpike = new BlockSpike(0, Material.IRON, "spike");
        blockSpikeWood = new BlockSpike(1, Material.WOOD, "spikeWood");
        blockSpikeTooth = new BlockSpike(2, Material.ROCK, "spikeTooth");
        blockCrystal = new BlockCrystal();
        blockCrystalDeep = new BlockCrystalDeep();
        blockLeviathan = new BlockLeviathan();
        blockSlot = new BlockSlot();
        blockPortal = new BlockPortalTH();
        blockGateway = new BlockGatewayPortal();
        itemSpawnerEgg = new ItemSpawnerEgg();
        itemEggIncubated = new ItemEggIncubated();
        itemLensCase = new ItemLensCase();
        itemLensFire = new ItemLensFire();
        itemLensWater = new ItemLensWater();
        itemLensAir = new ItemLensAir();
        itemLensEarth = new ItemLensEarth();
        itemLensOrderEntropy = new ItemLensOrderEntropy();
        itemFocusContainment = new ItemFocusContainment();
        itemFocusLiquefaction = new ItemFocusLiquefaction();
        itemFocusDisintegration = new ItemFocusDisintegration();
        itemFocusIllumination = new ItemFocusIllumination();
        itemFocusAnimation = new ItemFocusAnimation();
        blockSoulJarItem = new BlockSoulJarItem(blockJar);
        itemAmuletMirror = new ItemAmuletMirror();
        itemSyringeInjection = new ItemSyringeInjection();
        itemSyringeHuman = new ItemSyringeBlood();
        itemSyringeEmpty = new ItemSyringeEmpty();
        itemSyringeBloodSample = new ItemSyringeBloodSample();
        itemInjector = new ItemInjector();
        itemCorpseEffigy = new ItemCorpseEffigy();
        itemNutrients = new ItemNutrients();
        itemDummy = new ItemDummy();
        itemBarChocolate = new ItemBarChocolate();
        itemIceCream = new ItemIceCream();
        itemBucketChocolate = new ItemBucketChocolate();
        itemInkEgg = new ItemInkEgg();
        itemGoldEgg = new ItemGoldEgg();
        itemInfusionCheat = new ItemInfusionCheat();
        itemInfusionSelfCheat = new ItemInfusionSelfCheat();
        itemSuicidePill = new ItemSuicidePill();
        itemGolemPowder = new ItemGolemPowder();
        itemGolemPlacer = new ItemGolemPlacer();
        itemGolemBellTH = new ItemGolemBellTH();
        itemBoatGreatwood = new ItemBoatGreatwood();
        itemBoatThaumium = new ItemBoatThaumium();
        itemDummyVat = new ItemDummyVat();
        itemPlanarConduit = new ItemPlanarConduit();
        itemKeystone = new ItemKeystone();
        itemWandCastingDisposable = new ItemWandCastingDisposable();
        itemCrystalWand = new ItemCrystalWand();
        itemVoidPutty = new ItemVoidPutty();
        itemMeat = new ItemMeat();
        itemMeatCooked = new ItemMeatCooked();
        itemMeatNugget = new ItemMeatNugget();
        itemNodeCheat = new ItemNodeCheat();
        ThaumicHorizonsRegistries.enqueueBlock(blockSynthNode, "synthNode");
        ThaumicHorizonsRegistries.enqueueBlock(blockAlchemite, "alchemite");
        ThaumicHorizonsRegistries.enqueueBlock(blockVisDynamo, "visDynamo");
        ThaumicHorizonsRegistries.enqueueBlock(blockEssentiaDynamo, "essentiaDynamo");
        ThaumicHorizonsRegistries.enqueueBlock(blockNodeMonitor, "nodeMonitor");
        ThaumicHorizonsRegistries.enqueueBlock(blockSoulSieve, "soulSieve");
        ThaumicHorizonsRegistries.enqueueBlock(blockInspiratron, "inspiratron");
        ThaumicHorizonsRegistries.enqueueBlock(blockSoulforge, "soulforge");
        ThaumicHorizonsRegistries.enqueueBlock(blockJar, (net.minecraft.item.ItemBlock) blockSoulJarItem, "soulJar");
        ThaumicHorizonsRegistries.enqueueBlock(blockBloodInfuser, "bloodInfuser");
        ThaumicHorizonsRegistries.enqueueBlock(blockVat, "vat");
        ThaumicHorizonsRegistries.enqueueBlock(blockVatSolid, "vatSolid");
        ThaumicHorizonsRegistries.enqueueBlock(blockVatInterior, "vatInterior");
        ThaumicHorizonsRegistries.enqueueBlock(blockSoulBeacon, "soulBeacon");
        ThaumicHorizonsRegistries.enqueueBlock(blockModifiedMatrix, "modMatrix");
        ThaumicHorizonsRegistries.enqueueBlock(blockChocolate, "chocolate");
        ThaumicHorizonsRegistries.enqueueBlock(blockEvanescent, "evanescent");
        ThaumicHorizonsRegistries.enqueueBlock(blockLight, "light");
        ThaumicHorizonsRegistries.enqueueBlock(blockLightSolar, "lightSolar");
        ThaumicHorizonsRegistries.enqueueBlock(blockTransducer, "transductionAmplifier");
        ThaumicHorizonsRegistries.enqueueBlock(blockRecombinator, "recombinator");
        ThaumicHorizonsRegistries.enqueueBlock(blockVortexStabilizer, "vortexStabilizer");
        ThaumicHorizonsRegistries.enqueueBlock(blockVortex, BlockVortexItem::new, "vortexTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockVoid, "voidTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockCloud, BlockCloudItem::new, "cloudTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockCloudGlowing, BlockCloudItem::new, "cloudGlowingTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockBrain, "brainTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockDust, "dustTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockEye, "eyeTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockBone, "boneTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockFlesh, "fleshTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockSpike, "spikeTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockSpikeWood, "spikeWoodTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockSpikeTooth, "spikeToothTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockCrystal, BlockCrystalItem::new, "crystalTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockCrystalDeep, "crystalDeep");
        ThaumicHorizonsRegistries.enqueueBlock(blockLeviathan, "leviathanTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockSlot, "slotTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockPortal, "portalTH");
        ThaumicHorizonsRegistries.enqueueBlock(blockGateway, "gatewayTH");
        ThaumicHorizonsRegistries.enqueueItem(itemSpawnerEgg, "spawnerEgg");
        ThaumicHorizonsRegistries.enqueueItem(itemEggIncubated, "eggIncubated");
        ThaumicHorizonsRegistries.enqueueItem(itemLensFire, "lensFire");
        ThaumicHorizonsRegistries.enqueueItem(itemLensWater, "lensWater");
        ThaumicHorizonsRegistries.enqueueItem(itemLensAir, "lensAir");
        ThaumicHorizonsRegistries.enqueueItem(itemLensEarth, "lensEarth");
        ThaumicHorizonsRegistries.enqueueItem(itemLensOrderEntropy, "lensOrderEntropy");
        ThaumicHorizonsRegistries.enqueueItem(itemLensCase, "lensCase");
        ThaumicHorizonsRegistries.enqueueItem(itemFocusContainment, "focusContainment");
        ThaumicHorizonsRegistries.enqueueItem(itemFocusLiquefaction, "focusLiquefaction");
        ThaumicHorizonsRegistries.enqueueItem(itemFocusDisintegration, "focusDisintegration");
        ThaumicHorizonsRegistries.enqueueItem(itemFocusIllumination, "focusIllumination");
        ThaumicHorizonsRegistries.enqueueItem(itemFocusAnimation, "focusAnimation");
        ThaumicHorizonsRegistries.enqueueItem(itemAmuletMirror, "amuletMirror");
        ThaumicHorizonsRegistries.enqueueItem(itemSyringeInjection, "syringeInjection");
        ThaumicHorizonsRegistries.enqueueItem(itemSyringeEmpty, "syringeEmpty");
        ThaumicHorizonsRegistries.enqueueItem(itemSyringeHuman, "syringeBlood");
        ThaumicHorizonsRegistries.enqueueItem(itemSyringeBloodSample, "syringeBloodSample");
        ThaumicHorizonsRegistries.enqueueItem(itemInjector, "injector");
        ThaumicHorizonsRegistries.enqueueItem(itemCorpseEffigy, "corpseEffigy");
        ThaumicHorizonsRegistries.enqueueItem(itemNutrients, "nutrients");
        ThaumicHorizonsRegistries.enqueueItem(itemDummy, "dummy");
        ThaumicHorizonsRegistries.enqueueItem(itemBarChocolate, "barChocolate");
        ThaumicHorizonsRegistries.enqueueItem(itemBucketChocolate, "bucketChocolate");
        ThaumicHorizonsRegistries.enqueueItem(itemIceCream, "iceCream");
        ThaumicHorizonsRegistries.enqueueItem(itemInkEgg, "inkEgg");
        ThaumicHorizonsRegistries.enqueueItem(itemGoldEgg, "goldEgg");
        ThaumicHorizonsRegistries.enqueueItem(itemInfusionCheat, "infusionCheat");
        ThaumicHorizonsRegistries.enqueueItem(itemInfusionSelfCheat, "infusionSelfCheat");
        ThaumicHorizonsRegistries.enqueueItem(itemSuicidePill, "suicidePill");
        ThaumicHorizonsRegistries.enqueueItem(itemGolemPowder, "golemPowder");
        ThaumicHorizonsRegistries.enqueueItem(itemGolemPlacer, "golemPlacer");

        ThaumicHorizonsRegistries.enqueueItem(itemGolemBellTH, "Golemancy Bell TH");
        ConfigItems.itemGolemBell = itemGolemBellTH;

        ThaumicHorizonsRegistries.enqueueItem(itemBoatGreatwood, "boatGreatwood");
        ThaumicHorizonsRegistries.enqueueItem(itemBoatThaumium, "boatThaumium");
        ThaumicHorizonsRegistries.enqueueItem(itemDummyVat, "dummyVat");
        ThaumicHorizonsRegistries.enqueueItem(itemKeystone, "keystoneTH");
        ThaumicHorizonsRegistries.enqueueItem(itemPlanarConduit, "planarConduit");
        ThaumicHorizonsRegistries.enqueueItem(itemWandCastingDisposable, "wandCastingDisposable");
        ThaumicHorizonsRegistries.enqueueItem(itemCrystalWand, "crystalWand");
        ThaumicHorizonsRegistries.enqueueItem(itemVoidPutty, "voidPutty");
        ThaumicHorizonsRegistries.enqueueItem(itemMeat, "meatTH");
        ThaumicHorizonsRegistries.enqueueItem(itemMeatCooked, "meatCookedTH");
        ThaumicHorizonsRegistries.enqueueItem(itemMeatNugget, "meatNuggetTH");
        ThaumicHorizonsRegistries.enqueueItem(itemNodeCheat, "nodeCheat");
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "primedalchemite"), EntityAlchemitePrimed.class, "PrimedAlchemite", 0, this, 64, 20, false);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "egg"), EntityEggIncubated.class, "Egg", 1, this, 64, 20, true);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "chromaticsheep"), EntityChromaticSheep.class, "ChromaticSheep", 2, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("ChromaticSheep", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "selfshearingsheep"), EntitySelfShearingSheep.class, "SelfShearingSheep", 3, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("SelfShearingSheep", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "guardianpanther"), EntityGuardianPanther.class, "GuardianPanther", 4, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("GuardianPanther", 16761600, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "gravekeeper"), EntityGravekeeper.class, "Gravekeeper", 5, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("Gravekeeper", 16776960, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "familiar"), EntityFamiliar.class, "Familiar", 6, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("Familiar", 0, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "chocolatecow"), EntityChocolateCow.class, "ChocolateCow", 7, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("ChocolateCow", 8667136, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "scholarchicken"), EntityScholarChicken.class, "ScholarChicken", 9, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("ScholarChicken", 0, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "goldchicken"), EntityGoldChicken.class, "GoldChicken", 10, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("GoldChicken", 13873152, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "orepig"), EntityOrePig.class, "OrePig", 11, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("OrePig", 9671571, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "tainteaterpig"), EntityTaintPig.class, "TaintEaterPig", 12, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("TaintEaterPig", 3801179, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "endersteed"), EntityEndersteed.class, "Endersteed", 13, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("Endersteed", 10813695, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "seawolf"), EntitySeawolf.class, "Seawolf", 14, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("Seawolf", ' ', 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "netherhound"), EntityNetherHound.class, "NetherHound", 15, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("NetherHound", 16714752, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "lunarwolf"), EntityLunarWolf.class, "LunarWolf", 16, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("LunarWolf", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "syringe"), EntitySyringe.class, "Syringe", 17, this, 64, 20, false);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "blastphial"), EntityBlastPhial.class, "BlastPhial", 18, this, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "golemth"), EntityGolemTH.class, "GolemTH", 19, this, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "horsezombie"), EntityHorseUndead.class, "HorseZombie", 20, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("HorseZombie", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "horseskeleton"), EntityHorseUndeadS.class, "HorseSkeleton", 21, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("HorseSkeleton", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "nightmareth"), EntityNightmare.class, "NightmareTH", 22, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("NightmareTH", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "boatgreatwood"), EntityBoatGreatwood.class, "BoatGreatwood", 23, this, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "boatthaumium"), EntityBoatThaumium.class, "BoatThaumium", 24, this, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "meatslime"), EntityMeatSlime.class, "MeatSlime", 25, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("MeatSlime", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "mercurialslime"), EntityMercurialSlime.class, "MercurialSlime", 26, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("MercurialSlime", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "voltslime"), EntityVoltSlime.class, "VoltSlime", 27, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("VoltSlime", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "medslime"), EntityMedSlime.class, "MedSlime", 28, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("MedSlime", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "sheeder"), EntitySheeder.class, "Sheeder", 29, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("Sheeder", 16777215, 10813695);
        EntityRegistry.registerModEntity(new ResourceLocation("thaumichorizons", "lostsoul"), EntitySoul.class, "LostSoul", 30, this, 64, 3, true);
        ItemSpawnerEgg.addMapping("LostSoul", 16777215, 10813695);
        EntityRegistry
                .registerModEntity(new ResourceLocation("thaumichorizons", "lightningboltfinite"), EntityLightningBoltFinite.class, "LightningBoltFinite", 31, this, 64, 20, false);
        GameRegistry.registerTileEntity(TileSyntheticNode.class, new ResourceLocation("thaumichorizons", "tilesynthnode"));
        GameRegistry.registerTileEntity(TileVisDynamo.class, new ResourceLocation("thaumichorizons", "tilevisdynamo"));
        GameRegistry.registerTileEntity(TileEssentiaDynamo.class, new ResourceLocation("thaumichorizons", "tileessentiadynamoth"));
        GameRegistry.registerTileEntity(TileNodeMonitor.class, new ResourceLocation("thaumichorizons", "tilenodemonitor"));
        GameRegistry.registerTileEntity(TileSoulExtractor.class, new ResourceLocation("thaumichorizons", "tilesoulsieve"));
        GameRegistry.registerTileEntity(TileInspiratron.class, new ResourceLocation("thaumichorizons", "tileinspiratron"));
        GameRegistry.registerTileEntity(TileSoulforge.class, new ResourceLocation("thaumichorizons", "tilesoulforge"));
        GameRegistry.registerTileEntity(TileSoulJar.class, new ResourceLocation("thaumichorizons", "tilesouljar"));
        GameRegistry.registerTileEntity(TileBloodInfuser.class, new ResourceLocation("thaumichorizons", "tilebloodinfuser"));
        GameRegistry.registerTileEntity(TileVat.class, new ResourceLocation("thaumichorizons", "tilevat"));
        GameRegistry.registerTileEntity(TileVatSlave.class, new ResourceLocation("thaumichorizons", "tilevatslave"));
        GameRegistry.registerTileEntity(TileVatConnector.class, new ResourceLocation("thaumichorizons", "tilevatconnector"));
        GameRegistry.registerTileEntity(TileSoulBeacon.class, new ResourceLocation("thaumichorizons", "tilesoulbeacon"));
        GameRegistry.registerTileEntity(TileVatMatrix.class, new ResourceLocation("thaumichorizons", "tilevatmatrix"));
        GameRegistry.registerTileEntity(TileLight.class, new ResourceLocation("thaumichorizons", "tilelight"));
        GameRegistry.registerTileEntity(TileTransductionAmplifier.class, new ResourceLocation("thaumichorizons", "tiletransductionamplifier"));
        GameRegistry.registerTileEntity(TileRecombinator.class, new ResourceLocation("thaumichorizons", "tilerecombinator"));
        GameRegistry.registerTileEntity(TileVortexStabilizer.class, new ResourceLocation("thaumichorizons", "tilevortexstabilizer"));
        GameRegistry.registerTileEntity(TileVortex.class, new ResourceLocation("thaumichorizons", "vortexth"));
        GameRegistry.registerTileEntity(TileSpike.class, new ResourceLocation("thaumichorizons", "spiketh"));
        GameRegistry.registerTileEntity(TileCloud.class, new ResourceLocation("thaumichorizons", "cloudth"));
        GameRegistry.registerTileEntity(TileSlot.class, new ResourceLocation("thaumichorizons", "keystonereceptacleth"));
        proxy.registerHandlers();
        proxy.registerRenderers();
        proxy.registerDisplayInformation();
        FMLCommonHandler.instance().bus().register(instance);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerKeyBindings();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        // Dynamic recipes are registered during RegistryEvent.Register<IRecipe>.
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(itemMeat), new ItemStack(itemMeatCooked), 1.0F);
        ThaumcraftApi.addSmeltingBonus(new ItemStack(itemMeat), new ItemStack(itemMeatNugget));
        ResearchCategories.registerCategory(
                "ThaumicHorizons",
                new ResourceLocation("thaumichorizons", "textures/misc/vat.png"),
                new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png"));
        ResearchItem alcheponics;
        ResearchPage alcheponics1;
        ResearchPage alcheponics2;
        if (useAlternateBell) {
            alcheponics = new ResearchItem(
                    "alternateGolemBell",
                    "ThaumicHorizons",
                    (new AspectList()).add(Aspect.ORDER, 2).add(Aspect.ENERGY, 2).add(Aspect.CRYSTAL, 3),
                    -2,
                    0,
                    1,
                    new ItemStack(itemGolemBellTH));
            alcheponics1 = new ResearchPage("alternateGolemBell1");
            alcheponics2 = new ResearchPage(
                    ThaumcraftApi.addArcaneCraftingRecipe(
                            "GOLEMBELL",
                            new ItemStack(itemGolemBellTH),
                            (new AspectList()).add(Aspect.ORDER, 5),
                            "QQ ",
                            "QQ ",
                            "  S",
                            'S',
                            "stickWood",
                            'Q',
                            Items.QUARTZ));
            alcheponics.setPages(alcheponics1, alcheponics2);
            alcheponics.setSiblings("GOLEMBELL");
            alcheponics.setConcealed();
            ResearchCategories.addResearch(alcheponics);
        }

        recipeAlcheponicsWheat = ThaumcraftApi.addCrucibleRecipe(
                "alcheponics",
                new ItemStack(Items.WHEAT, 2),
                new ItemStack(Items.WHEAT_SEEDS),
                (new AspectList()).add(Aspect.LIGHT, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2));
        recipeAlcheponicsCarrot = ThaumcraftApi.addCrucibleRecipe(
                "alcheponics",
                new ItemStack(Items.CARROT, 2),
                new ItemStack(Items.CARROT),
                (new AspectList()).add(Aspect.LIGHT, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2));
        recipeAlcheponicsPotato = ThaumcraftApi.addCrucibleRecipe(
                "alcheponics",
                new ItemStack(Items.POTATO, 2),
                new ItemStack(Items.POTATO),
                (new AspectList()).add(Aspect.LIGHT, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2));
        recipeAlcheponicsMelon = ThaumcraftApi.addCrucibleRecipe(
                "alcheponics",
                new ItemStack(Blocks.MELON_BLOCK, 1),
                new ItemStack(Items.MELON_SEEDS),
                (new AspectList()).add(Aspect.LIGHT, 3).add(Aspect.EARTH, 3).add(Aspect.WATER, 3));
        recipeAlcheponicsPumpkin = ThaumcraftApi.addCrucibleRecipe(
                "alcheponics",
                new ItemStack(Blocks.PUMPKIN, 1),
                new ItemStack(Items.PUMPKIN_SEEDS),
                (new AspectList()).add(Aspect.LIGHT, 3).add(Aspect.EARTH, 3).add(Aspect.WATER, 3));
        alcheponics = new ResearchItem(
                "alcheponics",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.CROP, 5).add(Aspect.PLANT, 5).add(Aspect.LIGHT, 5),
                0,
                0,
                1,
                new ItemStack(Items.WHEAT));
        alcheponics1 = new ResearchPage("alcheponics1");
        alcheponics2 = new ResearchPage(recipeAlcheponicsWheat);
        ResearchPage alcheponicsE = new ResearchPage("alcheponics2");
        ResearchPage alcheponics3 = new ResearchPage(recipeAlcheponicsCarrot);
        ResearchPage alcheponics4 = new ResearchPage(recipeAlcheponicsPotato);
        ResearchPage alcheponics5 = new ResearchPage(recipeAlcheponicsMelon);
        ResearchPage alcheponics6 = new ResearchPage(recipeAlcheponicsPumpkin);
        alcheponics.setPages(
                alcheponics1,
                alcheponics2,
                alcheponicsE,
                alcheponics3,
                alcheponics4,
                alcheponics5,
                alcheponics6);
        ResearchCategories.addResearch(alcheponics);
        recipeAlchIncubation = ThaumcraftApi.addCrucibleRecipe(
                "alchIncubation",
                new ItemStack(itemEggIncubated),
                new ItemStack(Items.EGG),
                (new AspectList()).add(Aspect.FIRE, 2).add(Aspect.LIFE, 2));
        ResearchItem alchIncubation = new ResearchItem(
                "alchIncubation",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.BEAST, 4).add(Aspect.LIFE, 2).add(Aspect.FIRE, 2),
                2,
                0,
                1,
                new ItemStack(itemEggIncubated));
        ResearchPage alchIncubation1 = new ResearchPage("alchIncubation1");
        ResearchPage alchIncubation2 = new ResearchPage(recipeAlchIncubation);
        alchIncubation.setPages(alchIncubation1, alchIncubation2);
        alchIncubation.setParents("alcheponics");
        alchIncubation.setSecondary();
        ResearchCategories.addResearch(alchIncubation);
        recipeMeatGrowthPork = ThaumcraftApi.addCrucibleRecipe(
                "meatGrowth",
                new ItemStack(Items.PORKCHOP, 2),
                new ItemStack(Items.PORKCHOP),
                (new AspectList()).add(Aspect.LIFE, 3));
        recipeMeatGrowthBeef = ThaumcraftApi.addCrucibleRecipe(
                "meatGrowth",
                new ItemStack(Items.BEEF, 2),
                new ItemStack(Items.BEEF),
                (new AspectList()).add(Aspect.LIFE, 3));
        recipeMeatGrowthChicken = ThaumcraftApi.addCrucibleRecipe(
                "meatGrowth",
                new ItemStack(Items.CHICKEN, 2),
                new ItemStack(Items.CHICKEN),
                (new AspectList()).add(Aspect.LIFE, 3));
        ResearchItem meatGrowth = new ResearchItem(
                "meatGrowth",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.FLESH, 6).add(Aspect.LIFE, 4).add(Aspect.BEAST, 4).add(Aspect.CRAFT, 4),
                4,
                0,
                2,
                new ItemStack(Items.BEEF));
        ResearchPage meatGrowth1 = new ResearchPage("meatGrowth1");
        CrucibleRecipe[] meats = new CrucibleRecipe[] { recipeMeatGrowthPork, recipeMeatGrowthBeef,
                recipeMeatGrowthChicken };
        ResearchPage meatGrowth2 = new ResearchPage(meats);
        meatGrowth.setPages(meatGrowth1, meatGrowth2);
        meatGrowth.setParents("alchIncubation");
        ResearchCategories.addResearch(meatGrowth);
        recipeLeatherBeef = ThaumcraftApi.addCrucibleRecipe(
                "fleshLeather",
                new ItemStack(Items.LEATHER),
                new ItemStack(Items.BEEF),
                (new AspectList()).add(Aspect.CRAFT, 1).add(Aspect.CLOTH, 1));
        recipeLeatherPork = ThaumcraftApi.addCrucibleRecipe(
                "fleshLeather",
                new ItemStack(Items.LEATHER),
                new ItemStack(Items.PORKCHOP),
                (new AspectList()).add(Aspect.CRAFT, 1).add(Aspect.CLOTH, 1));
        recipeLeatherZombie = ThaumcraftApi.addCrucibleRecipe(
                "fleshLeather",
                new ItemStack(Items.LEATHER),
                new ItemStack(Items.ROTTEN_FLESH),
                (new AspectList()).add(Aspect.CRAFT, 1).add(Aspect.CLOTH, 1).add(Aspect.ORDER, 1));
        ResearchItem fleshLeather = new ResearchItem(
                "fleshLeather",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.FLESH, 4).add(Aspect.CLOTH, 6).add(Aspect.CRAFT, 4),
                6,
                -1,
                1,
                new ItemStack(Items.LEATHER));
        ResearchPage fleshLeather1 = new ResearchPage("fleshLeather1");
        CrucibleRecipe[] leathers = new CrucibleRecipe[] { recipeLeatherBeef, recipeLeatherPork, recipeLeatherZombie };
        ResearchPage fleshLeather2 = new ResearchPage(leathers);
        fleshLeather.setPages(fleshLeather1, fleshLeather2);
        fleshLeather.setParents("meatGrowth");
        fleshLeather.setSecondary();
        ResearchCategories.addResearch(fleshLeather);
        shardTransFire = ThaumcraftApi.addCrucibleRecipe(
                "shardTrans",
                new ItemStack(ConfigItems.itemShard, 1, 1),
                new ItemStack(ConfigItems.itemShard, 1, 6),
                (new AspectList()).add(Aspect.FIRE, 2).add(Aspect.EXCHANGE, 1));
        shardTransWater = ThaumcraftApi.addCrucibleRecipe(
                "shardTrans",
                new ItemStack(ConfigItems.itemShard, 1, 2),
                new ItemStack(ConfigItems.itemShard, 1, 6),
                (new AspectList()).add(Aspect.WATER, 2).add(Aspect.EXCHANGE, 1));
        shardTransAir = ThaumcraftApi.addCrucibleRecipe(
                "shardTrans",
                new ItemStack(ConfigItems.itemShard, 1, 0),
                new ItemStack(ConfigItems.itemShard, 1, 6),
                (new AspectList()).add(Aspect.AIR, 2).add(Aspect.EXCHANGE, 1));
        shardTransEarth = ThaumcraftApi.addCrucibleRecipe(
                "shardTrans",
                new ItemStack(ConfigItems.itemShard, 1, 3),
                new ItemStack(ConfigItems.itemShard, 1, 6),
                (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.EXCHANGE, 1));
        shardTransOrder = ThaumcraftApi.addCrucibleRecipe(
                "shardTrans",
                new ItemStack(ConfigItems.itemShard, 1, 4),
                new ItemStack(ConfigItems.itemShard, 1, 6),
                (new AspectList()).add(Aspect.ORDER, 2).add(Aspect.EXCHANGE, 1));
        shardTransEntropy = ThaumcraftApi.addCrucibleRecipe(
                "shardTrans",
                new ItemStack(ConfigItems.itemShard, 1, 5),
                new ItemStack(ConfigItems.itemShard, 1, 6),
                (new AspectList()).add(Aspect.ENTROPY, 2).add(Aspect.EXCHANGE, 1));
        ResearchItem shardTrans = new ResearchItem(
                "shardTrans",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.EXCHANGE, 4).add(Aspect.CRYSTAL, 4).add(Aspect.FIRE, 2)
                        .add(Aspect.WATER, 2).add(Aspect.EARTH, 2).add(Aspect.AIR, 2).add(Aspect.ORDER, 2)
                        .add(Aspect.ENTROPY, 2),
                1,
                2,
                2,
                new ItemStack(ConfigItems.itemShard, 1, 6));
        ResearchPage shardTrans1 = new ResearchPage("shardTrans1");
        CrucibleRecipe[] shardTranses = new CrucibleRecipe[] { shardTransFire, shardTransWater, shardTransAir,
                shardTransEarth, shardTransOrder, shardTransEntropy };
        ResearchPage shardTrans2 = new ResearchPage(shardTranses);
        shardTrans.setPages(shardTrans1, shardTrans2);
        shardTrans.setSecondary();
        ResearchCategories.addResearch(shardTrans);
        recipeEtherealShard = ThaumcraftApi.addInfusionCraftingRecipe(
                "etherealShard",
                new ItemStack(blockSynthNode),
                7,
                (new AspectList()).add(Aspect.AURA, 8).add(Aspect.ENERGY, 32).add(Aspect.VOID, 32),
                new ItemStack(ConfigItems.itemShard, 1, 6),
                new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 1),
                        new ItemStack(ConfigItems.itemResource, 1, 1), new ItemStack(ConfigItems.itemResource, 1, 1),
                        new ItemStack(ConfigItems.itemResource, 1, 1) });
        ResearchItem etherealShard = new ResearchItem(
                "etherealShard",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.AURA, 8).add(Aspect.ENERGY, 16).add(Aspect.VOID, 16).add(Aspect.MAGIC, 8),
                1,
                4,
                3,
                new ItemStack(blockSynthNode));
        ResearchPage etherealShard1 = new ResearchPage("etherealShard1");
        ResearchPage etherealShard2 = new ResearchPage(recipeEtherealShard);
        ResearchPage etherealShard3 = new ResearchPage("etherealShard2");
        etherealShard.setPages(etherealShard1, etherealShard2, etherealShard3);
        etherealShard.setParents("shardTrans");
        etherealShard.setParentsHidden("INFUSION", "VISPOWER");
        etherealShard.setConcealed();
        ResearchCategories.addResearch(etherealShard);
        recipeAlchemite = ThaumcraftApi.addCrucibleRecipe(
                "alchemite",
                new ItemStack(blockAlchemite),
                new ItemStack(Blocks.TNT),
                (new AspectList()).add(Aspect.ENERGY, 4).add(Aspect.CRYSTAL, 4).add(Aspect.EXCHANGE, 4));
        ResearchItem alchemite = new ResearchItem(
                "alchemite",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.FIRE, 2).add(Aspect.ENTROPY, 2).add(Aspect.ENERGY, 4)
                        .add(Aspect.CRYSTAL, 4).add(Aspect.EXCHANGE, 2),
                3,
                6,
                2,
                new ItemStack(blockAlchemite));
        ResearchPage alchemite1 = new ResearchPage("alchemite1");
        ResearchPage alchemite2 = new ResearchPage(recipeAlchemite);
        alchemite.setPages(alchemite1, alchemite2);
        alchemite.setParentsHidden("ESSENTIACRYSTAL");
        ResearchCategories.addResearch(alchemite);
        recipeVisDynamo = ThaumcraftApi.addInfusionCraftingRecipe(
                "visDynamo",
                new ItemStack(blockVisDynamo),
                5,
                (new AspectList()).add(Aspect.AURA, 4).add(Aspect.ENERGY, 16).add(Aspect.EXCHANGE, 16)
                        .add(Aspect.MECHANISM, 16),
                new ItemStack(Blocks.PISTON),
                new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(Items.GOLD_INGOT),
                        new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), new ItemStack(Items.GOLD_INGOT) });
        ResearchItem visDynamo = new ResearchItem(
                "visDynamo",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.AURA, 5).add(Aspect.MECHANISM, 5).add(Aspect.ENERGY, 5),
                3,
                2,
                2,
                new ItemStack(blockVisDynamo));
        ResearchPage visDynamo1 = new ResearchPage("visDynamo1");
        ResearchPage visDynamo2 = new ResearchPage(recipeVisDynamo);
        visDynamo.setPages(visDynamo1, visDynamo2);
        visDynamo.setParentsHidden("INFUSION", "VISPOWER");
        ResearchCategories.addResearch(visDynamo);
        recipeEssentiaDynamo = ThaumcraftApi.addInfusionCraftingRecipe(
                "essentiaDynamo",
                new ItemStack(blockEssentiaDynamo),
                5,
                (new AspectList()).add(Aspect.AURA, 4).add(Aspect.ENERGY, 16).add(Aspect.EXCHANGE, 16)
                        .add(Aspect.MECHANISM, 16).add(Aspect.WATER, 32),
                new ItemStack(Blocks.PISTON),
                new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 2),
                        new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9),
                        new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                        new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigBlocks.blockTube, 1, 2) });
        ResearchItem essentiaDynamo = new ResearchItem(
                "essentiaDynamo",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.AURA, 5).add(Aspect.MECHANISM, 5).add(Aspect.ENERGY, 5)
                        .add(Aspect.WATER, 5),
                3,
                4,
                2,
                new ItemStack(blockEssentiaDynamo));
        ResearchPage essentiaDynamo1 = new ResearchPage("essentiaDynamo1");
        ResearchPage essentiaDynamo2 = new ResearchPage(recipeEssentiaDynamo);
        ResearchPage essentiaDynamo3 = new ResearchPage("essentiaDynamo2");
        essentiaDynamo.setPages(essentiaDynamo1, essentiaDynamo2, essentiaDynamo3);
        essentiaDynamo.setParentsHidden("INFUSION", "VISPOWER", "THAUMIUM");
        essentiaDynamo.setParents("visDynamo");
        essentiaDynamo.setSecondary();
        essentiaDynamo.setConcealed();
        ResearchCategories.addResearch(essentiaDynamo);
        recipeNodeMonitor = ThaumcraftApi.addInfusionCraftingRecipe(
                "nodeMonitor",
                new ItemStack(blockNodeMonitor),
                2,
                (new AspectList()).add(Aspect.AURA, 2).add(Aspect.SENSES, 8).add(Aspect.MECHANISM, 8),
                new ItemStack(ConfigItems.itemThaumometer),
                new ItemStack[] { new ItemStack(ConfigItems.itemZombieBrain), new ItemStack(Items.COMPARATOR),
                        new ItemStack(ConfigItems.itemShard, 1, 0) });
        ResearchItem nodeMonitor = new ResearchItem(
                "nodeMonitor",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.AURA, 3).add(Aspect.MECHANISM, 3).add(Aspect.SENSES, 3),
                1,
                6,
                1,
                new ItemStack(blockNodeMonitor));
        ResearchPage nodeMonitor1 = new ResearchPage("nodeMonitor1");
        ResearchPage nodeMonitor2 = new ResearchPage(recipeNodeMonitor);
        nodeMonitor.setParents("INFUSION");
        nodeMonitor.setPages(nodeMonitor1, nodeMonitor2);
        ResearchCategories.addResearch(nodeMonitor);
        recipeSoulSieve = ThaumcraftApi.addInfusionCraftingRecipe(
                "soulExtractor",
                new ItemStack(blockSoulSieve),
                4,
                (new AspectList()).add(Aspect.SOUL, 16).add(Aspect.MECHANISM, 8).add(Aspect.EXCHANGE, 8),
                new ItemStack(Blocks.HOPPER),
                new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 6),
                        new ItemStack(ConfigItems.itemResource, 1, 7), new ItemStack(Blocks.PISTON),
                        new ItemStack(ConfigItems.itemResource, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 7),
                        new ItemStack(Blocks.PISTON) });
        ResearchItem soulSieve = new ResearchItem(
                "soulExtractor",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SOUL, 4).add(Aspect.TRAP, 4).add(Aspect.EARTH, 4).add(Aspect.EXCHANGE, 4),
                5,
                6,
                1,
                new ItemStack(blockSoulSieve));
        ResearchPage soulSieve1 = new ResearchPage("soulExtractor1");
        ResearchPage soulSieve2 = new ResearchPage(recipeSoulSieve);
        ResearchPage soulSieve3 = new ResearchPage("soulExtractor2");
        soulSieve.setPages(soulSieve1, soulSieve2, soulSieve3);
        soulSieve.setItemTriggers(new ItemStack(Blocks.SOUL_SAND));
        soulSieve.setParents("JARBRAIN", "ENCHFABRIC");
        soulSieve.setHidden();
        ResearchCategories.addResearch(soulSieve);
        ThaumcraftApi.addWarpToResearch("soulExtractor", 3);
        recipeInspiratron = ThaumcraftApi.addInfusionCraftingRecipe(
                "soulKnowledge",
                new ItemStack(blockInspiratron),
                5,
                (new AspectList()).add(Aspect.HEAL, 8).add(Aspect.MIND, 16).add(Aspect.ORDER, 8).add(Aspect.SOUL, 8),
                new ItemStack(ConfigBlocks.blockJar, 1, 1),
                new ItemStack[] { new ItemStack(ConfigItems.itemInkwell), new ItemStack(ConfigItems.itemResource, 1, 2),
                        new ItemStack(Items.GOLDEN_APPLE), new ItemStack(ConfigItems.itemResource, 1, 2) });
        ResearchItem inspiratron = new ResearchItem(
                "soulKnowledge",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SOUL, 4).add(Aspect.HEAL, 4).add(Aspect.MIND, 4),
                7,
                6,
                2,
                new ItemStack(blockInspiratron));
        ResearchPage inspiratron1 = new ResearchPage("soulKnowledge1");
        ResearchPage inspiratron2 = new ResearchPage(recipeInspiratron);
        ResearchPage inspiratron3 = new ResearchPage("soulKnowledge2");
        inspiratron.setPages(inspiratron1, inspiratron2, inspiratron3);
        inspiratron.setParents("soulExtractor");
        ResearchCategories.addResearch(inspiratron);
        inspiratron.setConcealed();
        ThaumcraftApi.addWarpToResearch("soulKnowledge", 2);
        recipeSoulforge = ThaumcraftApi.addInfusionCraftingRecipe(
                "soulAssembler",
                new ItemStack(blockSoulforge),
                6,
                (new AspectList()).add(Aspect.SOUL, 16).add(Aspect.CRAFT, 16).add(Aspect.EXCHANGE, 16)
                        .add(Aspect.MIND, 32).add(Aspect.HEAL, 16),
                new ItemStack(ConfigBlocks.blockJar, 1, 0),
                new ItemStack[] { new ItemStack(ConfigBlocks.blockJar, 1, 1), new ItemStack(Items.GOLDEN_APPLE),
                        new ItemStack(ConfigItems.itemResource, 1, 8), new ItemStack(ConfigBlocks.blockJar, 1, 1),
                        new ItemStack(Items.GOLDEN_APPLE), new ItemStack(ConfigItems.itemResource, 1, 8) });
        ResearchItem soulforge = new ResearchItem(
                "soulAssembler",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SOUL, 4).add(Aspect.HEAL, 4).add(Aspect.MIND, 4),
                9,
                6,
                3,
                new ItemStack(blockSoulforge));
        ResearchPage soulforge1 = new ResearchPage("soulAssembler1");
        ResearchPage soulforge2 = new ResearchPage(recipeSoulforge);
        ResearchPage soulforge3 = new ResearchPage("soulAssembler2");
        ResearchPage soulforge4 = new ResearchPage("soulAssembler3");
        soulforge.setPages(soulforge1, soulforge2, soulforge3, soulforge4);
        soulforge.setParents("soulKnowledge");
        ResearchCategories.addResearch(soulforge);
        soulforge.setConcealed();
        ThaumcraftApi.addWarpToResearch("soulAssembler", 2);
        recipeFocusContainment = ThaumcraftApi.addInfusionCraftingRecipe(
                "focusContainment",
                new ItemStack(itemFocusContainment),
                6,
                (new AspectList()).add(Aspect.VOID, 32).add(Aspect.TRAP, 32).add(Aspect.TRAVEL, 8),
                new ItemStack(ConfigItems.focusPortableHole),
                new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 6), new ItemStack(Blocks.WEB),
                        new ItemStack(ConfigItems.itemResource, 1, 6), new ItemStack(Blocks.IRON_BARS),
                        new ItemStack(ConfigItems.itemResource, 1, 6), new ItemStack(Blocks.WEB),
                        new ItemStack(ConfigItems.itemResource, 1, 6), new ItemStack(Blocks.IRON_BARS) });
        ResearchItem focusContainment = new ResearchItem(
                "focusContainment",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.TRAP, 6).add(Aspect.VOID, 6).add(Aspect.TRAVEL, 2),
                8,
                -3,
                3,
                new ItemStack(itemFocusContainment));
        ResearchPage focusContainment1 = new ResearchPage("focusContainment1");
        ResearchPage focusContainment2 = new ResearchPage(recipeFocusContainment);
        ResearchPage focusContainment3 = new ResearchPage("focusContainment2");
        focusContainment.setPages(focusContainment1, focusContainment2, focusContainment3);
        focusContainment.setParentsHidden("DISTILESSENTIA", "FOCUSPORTABLEHOLE");
        focusContainment.setConcealed();
        ResearchCategories.addResearch(focusContainment);
        recipeAmuletMirror = ThaumcraftApi.addInfusionCraftingRecipe(
                "mirrorAmulet",
                new ItemStack(itemAmuletMirror),
                6,
                (new AspectList()).add(Aspect.DEATH, 16).add(Aspect.ENTROPY, 32).add(Aspect.VOID, 32),
                new ItemStack(ConfigItems.itemEldritchObject, 1, 3),
                new ItemStack[] { new ItemStack(ConfigItems.itemBaubleBlanks, 1, 0), new ItemStack(Items.MAP),
                        new ItemStack(ConfigBlocks.blockMirror) });
        ResearchItem amuletMirror = new ResearchItem(
                "mirrorAmulet",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.DEATH, 5).add(Aspect.ENTROPY, 5).add(Aspect.VOID, 5),
                17,
                4,
                2,
                new ItemStack(itemAmuletMirror));
        ResearchPage amuletMirror1 = new ResearchPage("mirrorAmulet1");
        ResearchPage amuletMirror2 = new ResearchPage(recipeAmuletMirror);
        amuletMirror.setPages(amuletMirror1, amuletMirror2);
        amuletMirror.setParents("cloneSelf");
        amuletMirror.setParentsHidden("MIRROR");
        amuletMirror.setConcealed();
        ResearchCategories.addResearch(amuletMirror);
        recipeSyringe = ThaumcraftApi.addArcaneCraftingRecipe(
                "essentiaInjection",
                new ItemStack(itemSyringeEmpty, 1, 2),
                (new AspectList()).add(Aspect.WATER, 1).add(Aspect.ORDER, 1),
                "P",
                "N",
                'P',
                new ItemStack(ConfigItems.itemEssence, 1, 0),
                'N',
                "nuggetIron");
        recipeBloodInfuser = ThaumcraftApi.addInfusionCraftingRecipe(
                "essentiaInjection",
                new ItemStack(blockBloodInfuser),
                4,
                (new AspectList()).add(Aspect.MAN, 8).add(Aspect.EXCHANGE, 8).add(Aspect.LIFE, 16)
                        .add(Aspect.MAGIC, 16),
                new ItemStack(ConfigBlocks.blockMetalDevice, 1, 0),
                new ItemStack[] { new ItemStack(Items.COMPARATOR), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9),
                        new ItemStack(ConfigBlocks.blockTube, 1, 4),
                        new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9) });
        ResearchItem bloodInfusion = new ResearchItem(
                "essentiaInjection",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.LIFE, 4).add(Aspect.MAN, 2).add(Aspect.EXCHANGE, 2).add(Aspect.MAGIC, 2),
                6,
                1,
                3,
                new ItemStack(itemSyringeHuman));
        ResearchPage bloodInfusion1 = new ResearchPage("essentiaInjection1");
        ResearchPage bloodInfusion2 = new ResearchPage(recipeSyringe);
        ResearchPage bloodInfusion3 = new ResearchPage("essentiaInjection2");
        ResearchPage bloodInfusion4 = new ResearchPage(recipeBloodInfuser);
        ResearchPage bloodInfusion5 = new ResearchPage("essentiaInjection3");
        ResearchPage bloodInfusion6 = new ResearchPage("essentiaInjection4");
        bloodInfusion.setPages(
                bloodInfusion1,
                bloodInfusion2,
                bloodInfusion3,
                bloodInfusion4,
                bloodInfusion5,
                bloodInfusion6);
        bloodInfusion.setParents("meatGrowth");
        bloodInfusion.setParentsHidden("THAUMATORIUM", "INFUSION");
        ResearchCategories.addResearch(bloodInfusion);
        ThaumcraftApi.addWarpToResearch("essentiaInjection", 1);
        recipeBlastPhial = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
                "blastPhial",
                new ItemStack(itemSyringeHuman, 4, 1),
                (new AspectList()).add(Aspect.FIRE, 1).add(Aspect.WATER, 1).add(Aspect.ENTROPY, 1),
                new ItemStack(ConfigItems.itemEssence, 1, 0),
                new ItemStack(ConfigItems.itemEssence, 1, 0),
                new ItemStack(Items.GUNPOWDER),
                new ItemStack(ConfigItems.itemEssence, 1, 0),
                new ItemStack(ConfigItems.itemEssence, 1, 0),
                new ItemStack(itemSyringeHuman, 1, 0));
        ResearchItem blastPhial = new ResearchItem(
                "blastPhial",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.LIFE, 2).add(Aspect.FIRE, 2).add(Aspect.ENTROPY, 2),
                6,
                3,
                1,
                new ItemStack(itemSyringeHuman, 1, 1));
        ResearchPage blastPhial1 = new ResearchPage("blastPhial1");
        ResearchPage blastPhial2 = new ResearchPage(recipeBlastPhial);
        blastPhial.setPages(blastPhial1, blastPhial2);
        blastPhial.setParents("essentiaInjection");
        blastPhial.setSecondary();
        blastPhial.setConcealed();
        ResearchCategories.addResearch(blastPhial);
        ResearchItem injector = new ResearchItem(
                "injector",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.WEAPON, 3).add(Aspect.MECHANISM, 3).add(Aspect.FLIGHT, 3),
                8,
                3,
                1,
                new ItemStack(itemInjector));
        recipeInjector = ThaumcraftApi.addInfusionCraftingRecipe(
                "injector",
                new ItemStack(itemInjector),
                2,
                (new AspectList()).add(Aspect.WEAPON, 8).add(Aspect.MECHANISM, 8).add(Aspect.FLIGHT, 8),
                new ItemStack(ConfigItems.itemBowBone),
                new ItemStack[] { new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), new ItemStack(Items.FEATHER),
                        new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemShard, 1, 0) });
        ResearchPage injector1 = new ResearchPage("injector1");
        ResearchPage injector2 = new ResearchPage(recipeInjector);
        injector.setPages(injector1, injector2);
        injector.setParents("blastPhial", "BONEBOW");
        injector.setConcealed();
        ResearchCategories.addResearch(injector);
        ResearchItem curativeVat = new ResearchItem(
                "healingVat",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.LIFE, 2).add(Aspect.HEAL, 4).add(Aspect.MECHANISM, 2)
                        .add(Aspect.WATER, 2),
                8,
                0,
                3,
                new ResourceLocation("thaumichorizons", "textures/misc/vat.png"));
        ConfigResearch.recipes.put(
                "CurativeVat",
                Arrays.asList(
                        (new AspectList()).add(Aspect.WATER, 50).add(Aspect.EARTH, 50).add(Aspect.ORDER, 50),
                        3,
                        4,
                        3,
                        Arrays.asList(
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.WATER),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.WATER),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(Blocks.GLASS),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6),
                                new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6))));
        ResearchPage curativeVat1 = new ResearchPage("healingVat1");
        ResearchPage curativeVat2 = new ResearchPage((List) ConfigResearch.recipes.get("CurativeVat"));
        ResearchPage curativeVat3 = new ResearchPage("healingVat2");
        ResearchPage curativeVatContainment = new ResearchPage("focusContainment", "healingVatContainment");
        curativeVat.setPages(curativeVat1, curativeVat2, curativeVat3, curativeVatContainment);
        curativeVat.setParents("essentiaInjection", "fleshLeather");
        curativeVat.setSpecial();
        ResearchCategories.addResearch(curativeVat);
        ResearchItem incarnationVat = new ResearchItem(
                "incarnationVat",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.LIFE, 3).add(Aspect.CRAFT, 3).add(Aspect.BEAST, 3)
                        .add(Aspect.MECHANISM, 2),
                10,
                1,
                2,
                new ItemStack(Items.SPAWN_EGG));
        recipeNutrients = ThaumcraftApi.addCrucibleRecipe(
                "incarnationVat",
                new ItemStack(itemNutrients),
                new ItemStack(Items.SUGAR),
                (new AspectList()).add(Aspect.HUNGER, 2));
        ResearchPage incarnationVat1 = new ResearchPage("incarnationVat1");
        ResearchPage incarnationVat2 = new ResearchPage(recipeNutrients);
        ResearchPage incarnationVat3 = new ResearchPage("incarnationVat2");
        incarnationVat.setPages(incarnationVat1, incarnationVat2, incarnationVat3);
        incarnationVat.setParents("healingVat");
        incarnationVat.setSecondary();
        incarnationVat.setConcealed();
        ResearchCategories.addResearch(incarnationVat);
        ResearchItem infusionVat = new ResearchItem(
                "infusionVat",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAGIC, 5).add(Aspect.LIFE, 5).add(Aspect.CRAFT, 5).add(Aspect.BEAST, 5),
                10,
                -1,
                3,
                new ItemStack(blockModifiedMatrix));
        recipeModifiedMatrix = ThaumcraftApi.addInfusionCraftingRecipe(
                "infusionVat",
                new ItemStack(blockModifiedMatrix),
                10,
                (new AspectList()).add(Aspect.LIFE, 32).add(Aspect.CRAFT, 32).add(Aspect.MAGIC, 32)
                        .add(Aspect.BEAST, 32),
                new ItemStack(ConfigBlocks.blockStoneDevice, 1, 2),
                new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(itemSyringeHuman),
                        new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(itemSyringeHuman) });
        ResearchPage infusionVat1 = new ResearchPage("infusionVat1");
        ResearchPage infusionVat2 = new ResearchPage(recipeModifiedMatrix);
        ResearchPage infusionVat3 = new ResearchPage("infusionVat2");
        ResearchPage infusionVat4 = new ResearchPage("infusionVat3");
        ResearchPage infusionVatClone = new ResearchPage("incarnationVat", "infusionVatClone");
        infusionVat.setPages(infusionVat1, infusionVat2, infusionVat3, infusionVat4, infusionVatClone);
        infusionVat.setParents("healingVat", "INFUSION", "focusContainment");
        infusionVat.setSiblings("generalInfusion");
        infusionVat.setConcealed();
        ResearchCategories.addResearch(infusionVat);
        ResearchItem cloneVillager = new ResearchItem(
                "cloneVillager",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SOUL, 8).add(Aspect.MAN, 8).add(Aspect.HEAL, 8),
                11,
                4,
                3,
                new ResourceLocation("thaumichorizons", "textures/misc/villager.png"));
        recipeCorpseEffigy = ThaumcraftApi.addInfusionCraftingRecipe(
                "cloneVillager",
                new ItemStack(itemCorpseEffigy),
                3,
                (new AspectList()).add(Aspect.CRAFT, 8).add(Aspect.MAN, 8),
                new ItemStack(ConfigItems.itemZombieBrain),
                new ItemStack[] { new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.BONE),
                        new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.BONE), new ItemStack(Items.ROTTEN_FLESH),
                        new ItemStack(Items.BONE), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.BONE) });
        ResearchPage cloneVillager1 = new ResearchPage("cloneVillager1");
        ResearchPage cloneVillager2 = new ResearchPage(recipeCorpseEffigy);
        ResearchPage cloneVillager3 = new ResearchPage("cloneVillager2");
        cloneVillager.setPages(cloneVillager1, cloneVillager2, cloneVillager3);
        cloneVillager.setParents("incarnationVat", "soulAssembler");
        cloneVillager.setSiblings("nightmare");
        cloneVillager.setConcealed();
        ResearchCategories.addResearch(cloneVillager);
        ThaumcraftApi.addWarpToResearch("cloneVillager", 3);
        ResearchItem nightmare = new ResearchItem(
                "nightmare",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SOUL, 1),
                13,
                4,
                1,
                new ResourceLocation("thaumichorizons", "textures/misc/skull.png"));
        ResearchPage nightmare1 = new ResearchPage("nightmare1");
        ResearchPage nightmare2 = new ResearchPage("nightmare2");
        nightmare.setPages(nightmare1, nightmare2);
        nightmare.setRound();
        nightmare.setParents("cloneVillager");
        nightmare.setConcealed();
        ResearchCategories.addResearch(nightmare);
        ResearchItem cloneSelf = new ResearchItem(
                "cloneSelf",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SOUL, 4).add(Aspect.TRAP, 4).add(Aspect.MAN, 4).add(Aspect.HEAL, 4),
                15,
                4,
                2,
                new ResourceLocation("thaumichorizons", "textures/misc/steve.png"));
        recipeSoulBeacon = ThaumcraftApi.addInfusionCraftingRecipe(
                "cloneSelf",
                new ItemStack(blockSoulBeacon),
                8,
                (new AspectList()).add(Aspect.SOUL, 16).add(Aspect.TRAP, 32).add(Aspect.TRAVEL, 32)
                        .add(Aspect.DEATH, 32),
                new ItemStack(ConfigItems.itemEldritchObject, 1, 3),
                new ItemStack[] { new ItemStack(ConfigBlocks.blockJar, 1, 0),
                        new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.ENDER_EYE),
                        new ItemStack(Blocks.BEACON), new ItemStack(Items.ENDER_EYE),
                        new ItemStack(ConfigItems.itemResource, 1, 16) });
        ResearchPage cloneSelf1 = new ResearchPage("cloneSelf1");
        ResearchPage cloneSelf2 = new ResearchPage(recipeSoulBeacon);
        ResearchPage cloneSelf3 = new ResearchPage("cloneSelf2");
        cloneSelf.setPages(cloneSelf1, cloneSelf2, cloneSelf3);
        cloneSelf.setConcealed();
        cloneSelf.setParents("nightmare", "PRIMPEARL");
        ResearchCategories.addResearch(cloneSelf);
        ThaumcraftApi.addWarpToResearch("cloneSelf", 5);
        ResearchItem lens = new ResearchItem(
                "lensFire",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SENSES, 3).add(Aspect.LIGHT, 3).add(Aspect.ENERGY, 2),
                2,
                -2,
                2,
                new ItemStack(itemLensFire));
        recipeLensFire = ThaumcraftApi.addInfusionCraftingRecipe(
                "lensFire",
                new ItemStack(itemLensFire),
                1,
                (new AspectList()).add(Aspect.LIGHT, 20).add(Aspect.ENERGY, 12).add(Aspect.SENSES, 20),
                new ItemStack(Blocks.GLASS_PANE),
                new ItemStack[] { new ItemStack(Items.GOLDEN_CARROT), new ItemStack(ConfigItems.itemShard, 1, 1),
                        new ItemStack(Items.GOLDEN_CARROT), new ItemStack(ConfigItems.itemShard, 1, 1) });
        ResearchPage lens1 = new ResearchPage("lensFire1");
        ResearchPage lens2 = new ResearchPage(recipeLensFire);
        lens.setPages(lens1, lens2);
        lens.setParents("GOGGLES", "INFUSION");
        ResearchCategories.addResearch(lens);
        ResearchItem lensWater = new ResearchItem(
                "lensWater",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SENSES, 4).add(Aspect.EXCHANGE, 2),
                1,
                -4,
                1,
                new ItemStack(itemLensWater));
        recipeLensWater = ThaumcraftApi.addInfusionCraftingRecipe(
                "lensWater",
                new ItemStack(itemLensWater),
                1,
                (new AspectList()).add(Aspect.SENSES, 24).add(Aspect.EXCHANGE, 12).add(Aspect.WATER, 12),
                new ItemStack(Blocks.GLASS_PANE),
                new ItemStack[] { new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 1),
                        new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(Blocks.NOTEBLOCK),
                        new ItemStack(ConfigItems.itemShard, 1, 2) });
        ResearchPage lensWater1 = new ResearchPage("lensWater1");
        ResearchPage lensWater2 = new ResearchPage(recipeLensWater);
        lensWater.setPages(lensWater1, lensWater2);
        lensWater.setParents("lensFire", "ARCANEEAR");
        lensWater.setConcealed();
        ResearchCategories.addResearch(lensWater);
        ResearchItem lensEarth = new ResearchItem(
                "lensEarth",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SENSES, 4).add(Aspect.VOID, 2).add(Aspect.LIGHT, 2),
                3,
                -4,
                1,
                new ItemStack(itemLensEarth));
        recipeLensEarth = ThaumcraftApi.addInfusionCraftingRecipe(
                "lensEarth",
                new ItemStack(itemLensEarth),
                1,
                (new AspectList()).add(Aspect.SENSES, 20).add(Aspect.VOID, 12).add(Aspect.LIGHT, 12),
                new ItemStack(Blocks.GLASS_PANE),
                new ItemStack[] { new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(ConfigItems.itemShard, 1, 3),
                        new ItemStack(Items.ENDER_EYE), new ItemStack(ConfigItems.itemShard, 1, 3) });
        ResearchPage lensEarth1 = new ResearchPage("lensEarth1");
        ResearchPage lensEarth2 = new ResearchPage(recipeLensEarth);
        lensEarth.setPages(lensEarth1, lensEarth2);
        lensEarth.setParents("lensFire");
        lensEarth.setConcealed();
        ResearchCategories.addResearch(lensEarth);
        ResearchItem lensAir = new ResearchItem(
                "lensAir",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SENSES, 4).add(Aspect.AURA, 2).add(Aspect.LIFE, 2).add(Aspect.UNDEAD, 2),
                -1,
                -4,
                1,
                new ItemStack(itemLensAir));
        recipeLensAir = ThaumcraftApi.addInfusionCraftingRecipe(
                "lensAir",
                new ItemStack(itemLensAir),
                1,
                (new AspectList()).add(Aspect.SENSES, 18).add(Aspect.AURA, 4).add(Aspect.LIFE, 12)
                        .add(Aspect.UNDEAD, 12),
                new ItemStack(Blocks.GLASS_PANE),
                new ItemStack[] { new ItemStack(Items.BONE), new ItemStack(ConfigItems.itemShard, 1, 0),
                        new ItemStack(Items.EGG), new ItemStack(ConfigItems.itemShard, 1, 0) });
        ResearchPage lensAir1 = new ResearchPage("lensAir1");
        ResearchPage lensAir2 = new ResearchPage(recipeLensAir);
        lensAir.setPages(lensAir1, lensAir2);
        lensAir.setParents("lensFire");
        lensAir.setConcealed();
        ResearchCategories.addResearch(lensAir);
        ResearchItem lensOrderEntropy = new ResearchItem(
                "lensOrderEntropy",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SENSES, 4).add(Aspect.MIND, 4).add(Aspect.MAGIC, 2),
                5,
                -4,
                1,
                new ItemStack(itemLensOrderEntropy));
        recipeLensOrderEntropy = ThaumcraftApi.addInfusionCraftingRecipe(
                "lensOrderEntropy",
                new ItemStack(itemLensOrderEntropy),
                1,
                (new AspectList()).add(Aspect.SENSES, 24).add(Aspect.MIND, 12).add(Aspect.MAGIC, 12),
                new ItemStack(Blocks.GLASS_PANE),
                new ItemStack[] { new ItemStack(ConfigItems.itemInkwell), new ItemStack(ConfigItems.itemShard, 1, 5),
                        new ItemStack(Items.BOOK), new ItemStack(ConfigItems.itemShard, 1, 4) });
        ResearchPage lensOrderEntropy1 = new ResearchPage("lensOrderEntropy1");
        ResearchPage lensOrderEntropy2 = new ResearchPage(recipeLensOrderEntropy);
        lensOrderEntropy.setPages(lensOrderEntropy1, lensOrderEntropy2);
        lensOrderEntropy.setParents("lensFire");
        lensOrderEntropy.setConcealed();
        ResearchCategories.addResearch(lensOrderEntropy);
        ResearchItem lensCase = new ResearchItem(
                "lensCase",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.CRYSTAL, 2).add(Aspect.CLOTH, 4).add(Aspect.VOID, 2),
                2,
                -6,
                1,
                new ItemStack(itemLensCase));
        recipeLensCase = ThaumcraftApi.addArcaneCraftingRecipe(
                "lensCase",
                new ItemStack(itemLensCase),
                (new AspectList()).add(Aspect.EARTH, 10).add(Aspect.ORDER, 10).add(Aspect.ENTROPY, 10),
                "LGL",
                "LBL",
                "LLL",
                'B',
                new ItemStack(ConfigItems.itemBaubleBlanks, 1, 2),
                'L',
                Items.LEATHER,
                'G',
                Items.IRON_INGOT);
        ResearchPage lensCase1 = new ResearchPage("lensCase1");
        ResearchPage lensCase2 = new ResearchPage(recipeLensCase);
        lensCase.setPages(lensCase1, lensCase2);
        lensCase.setSecondary();
        lensCase.setParents("lensAir", "lensWater", "lensEarth", "lensOrderEntropy", "FOCUSPOUCH");
        lensCase.setConcealed();
        ResearchCategories.addResearch(lensCase);
        ResearchItem focusLiquefaction = new ResearchItem(
                "focusLiquefaction",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.FIRE, 3).add(Aspect.METAL, 3).add(Aspect.EXCHANGE, 2),
                1,
                8,
                2,
                new ItemStack(itemFocusLiquefaction));
        recipeFocusLiquefaction = ThaumcraftApi.addInfusionCraftingRecipe(
                "focusLiquefaction",
                new ItemStack(itemFocusLiquefaction),
                3,
                (new AspectList()).add(Aspect.FIRE, 12).add(Aspect.METAL, 8).add(Aspect.EXCHANGE, 4),
                new ItemStack(Items.DIAMOND),
                new ItemStack[] { new ItemStack(ConfigItems.focusFire),
                        new ItemStack(ConfigItems.itemResource, 1, 14),
                        new ItemStack(ConfigItems.focusExcavation) });
        ResearchPage focusLiquefaction1 = new ResearchPage("focusLiquefaction1");
        ResearchPage focusLiquefaction2 = new ResearchPage(recipeFocusLiquefaction);
        focusLiquefaction.setPages(focusLiquefaction1, focusLiquefaction2);
        focusLiquefaction.setParents("focusIllumination", "FOCUSEXCAVATION");
        focusLiquefaction.setConcealed();
        ResearchCategories.addResearch(focusLiquefaction);
        ResearchItem focusDisintegration = new ResearchItem(
                "focusDisintegration",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAGIC, 3).add(Aspect.CRYSTAL, 2).add(Aspect.ENTROPY, 3),
                3,
                8,
                2,
                new ItemStack(itemFocusDisintegration));
        recipeFocusDisintegration = ThaumcraftApi.addInfusionCraftingRecipe(
                "focusDisintegration",
                new ItemStack(itemFocusDisintegration),
                7,
                (new AspectList()).add(Aspect.ENTROPY, 32).add(Aspect.MAGIC, 24).add(Aspect.CRYSTAL, 32)
                        .add(Aspect.EXCHANGE, 16),
                new ItemStack(ConfigItems.focusExcavation),
                new ItemStack[] { new ItemStack(ConfigItems.itemBucketDeath),
                        new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigItems.itemBucketDeath),
                        new ItemStack(ConfigItems.itemResource, 1, 0) });
        ResearchPage focusDisintegration1 = new ResearchPage("focusDisintegration1");
        ResearchPage focusDisintegration2 = new ResearchPage(recipeFocusDisintegration);
        focusDisintegration.setPages(focusDisintegration1, focusDisintegration2);
        focusDisintegration.setParents("focusLiquefaction", "alchemite", "LIQUIDDEATH");
        focusDisintegration.setConcealed();
        ResearchCategories.addResearch(focusDisintegration);
        ResearchItem focusIllumination = new ResearchItem(
                "focusIllumination",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.LIGHT, 2).add(Aspect.ENERGY, 2).add(Aspect.MAGIC, 1),
                -1,
                6,
                1,
                new ItemStack(itemFocusIllumination, 1, 11));
        recipeFocusIllumination = ThaumcraftApi.addCrucibleRecipe(
                "focusIllumination",
                new ItemStack(itemFocusIllumination, 1, 11),
                new ItemStack(Blocks.GLOWSTONE),
                (new AspectList()).add(Aspect.LIGHT, 12).add(Aspect.ENERGY, 8).add(Aspect.FIRE, 8));
        ResearchPage focusIllumination1 = new ResearchPage("focusIllumination1");
        ResearchPage focusIllumination2 = new ResearchPage(recipeFocusIllumination);
        focusIllumination.setPages(focusIllumination1, focusIllumination2);
        focusIllumination.setParents("FOCUSFIRE");
        focusIllumination.setConcealed();
        ResearchCategories.addResearch(focusIllumination);
        ResearchItem focusAnimation = new ResearchItem(
                "focusAnimation",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAN, 5).add(Aspect.MOTION, 5).add(Aspect.SOUL, 5).add(Aspect.CRAFT, 3),
                12,
                -1,
                3,
                new ItemStack(itemFocusAnimation));
        recipeFocusAnimation = ThaumcraftApi.addInfusionCraftingRecipe(
                "focusAnimation",
                new ItemStack(itemFocusAnimation),
                10,
                (new AspectList()).add(Aspect.MAN, 64).add(Aspect.MOTION, 64).add(Aspect.SOUL, 64)
                        .add(Aspect.CRAFT, 64),
                new ItemStack(ConfigItems.itemEldritchObject, 1, 3),
                new ItemStack[] { new ItemStack(ConfigItems.itemGolemCore, 1, 100), new ItemStack(itemSyringeHuman),
                        new ItemStack(ConfigItems.itemGolemUpgrade, 1, 0),
                        new ItemStack(ConfigItems.itemGolemUpgrade, 1, 2),
                        new ItemStack(ConfigItems.itemGolemUpgrade, 1, 4),
                        new ItemStack(ConfigItems.itemGolemCore, 1, 100), new ItemStack(itemSyringeHuman),
                        new ItemStack(ConfigItems.itemGolemUpgrade, 1, 1),
                        new ItemStack(ConfigItems.itemGolemUpgrade, 1, 3),
                        new ItemStack(ConfigItems.itemGolemUpgrade, 1, 5) });
        ResearchPage focusAnimation1 = new ResearchPage("focusAnimation1");
        ResearchPage focusAnimation2 = new ResearchPage(recipeFocusAnimation);
        ResearchPage focusAnimation3 = new ResearchPage("focusAnimation2");
        focusAnimation.setPages(focusAnimation1, focusAnimation2, focusAnimation3);
        focusAnimation.setParents("PRIMPEARL", "golemPowder");
        focusAnimation.setConcealed();
        ResearchCategories.addResearch(focusAnimation);
        ConfigResearch.recipes.put(
                "AdvancedGolemTH",
                ThaumcraftApi.addInfusionCraftingRecipe(
                        "focusAnimation",
                        new Object[] { "advanced", new NBTTagByte((byte) 1) },
                        3,
                        (new AspectList()).add(Aspect.MIND, 8).add(Aspect.SENSES, 8).add(Aspect.LIFE, 8),
                        new ItemStack(itemGolemPlacer, 1, 32767),
                        new ItemStack[] { new ItemStack(Items.REDSTONE), new ItemStack(Items.GLOWSTONE_DUST),
                                new ItemStack(Items.GUNPOWDER), new ItemStack(ConfigBlocks.blockJar, 1, 0),
                                new ItemStack(ConfigItems.itemZombieBrain) }));
        ResearchItem boatGreatwood = new ResearchItem(
                "greatwoodBoat",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.WATER, 1).add(Aspect.TRAVEL, 1).add(Aspect.CRAFT, 1),
                5,
                8,
                1,
                new ItemStack(itemBoatGreatwood));
        recipeGreatwoodBoat = ThaumcraftApi.addArcaneCraftingRecipe(
                "greatwoodBoat",
                new ItemStack(itemBoatGreatwood),
                (new AspectList()).add(Aspect.ORDER, 5).add(Aspect.EARTH, 5).add(Aspect.WATER, 5),
                "G G",
                "GGG",
                'G',
                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0));
        ResearchPage boatGreatwood1 = new ResearchPage("greatwoodBoat1");
        ResearchPage boatGreatwood2 = new ResearchPage(recipeGreatwoodBoat);
        boatGreatwood.setPages(boatGreatwood1, boatGreatwood2);
        boatGreatwood.setSecondary();
        ResearchCategories.addResearch(boatGreatwood);
        ResearchItem boatThaumium = new ResearchItem(
                "thaumiumBoat",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.FIRE, 2).add(Aspect.METAL, 2).add(Aspect.MAGIC, 2),
                7,
                8,
                1,
                new ItemStack(itemBoatThaumium));
        recipeThaumiumBoat = ThaumcraftApi.addArcaneCraftingRecipe(
                "thaumiumBoat",
                new ItemStack(itemBoatThaumium),
                (new AspectList()).add(Aspect.FIRE, 10).add(Aspect.WATER, 10).add(Aspect.ORDER, 10)
                        .add(Aspect.EARTH, 10),
                "STS",
                "TBT",
                "STS",
                'S',
                new ItemStack(ConfigItems.itemShard, 1, 2),
                'T',
                new ItemStack(ConfigItems.itemResource, 1, 2),
                'B',
                new ItemStack(itemBoatGreatwood));
        ResearchPage boatThaumium1 = new ResearchPage("thaumiumBoat1");
        ResearchPage boatThaumium2 = new ResearchPage(recipeThaumiumBoat);
        boatThaumium.setPages(boatThaumium1, boatThaumium2);
        boatThaumium.setParents("greatwoodBoat", "THAUMIUM");
        ResearchCategories.addResearch(boatThaumium);
        ResearchItem golemPowder = new ResearchItem(
                "golemPowder",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAN, 3).add(Aspect.SOUL, 3).add(Aspect.MOTION, 3).add(Aspect.CRAFT, 3),
                12,
                1,
                3,
                new ItemStack(itemGolemPowder));
        recipeGolemPowder = ThaumcraftApi.addCrucibleRecipe(
                "golemPowder",
                new ItemStack(itemGolemPowder),
                new ItemStack(ConfigItems.itemResource, 1, 14),
                (new AspectList()).add(Aspect.MAN, 4).add(Aspect.SOUL, 4).add(Aspect.MOTION, 4));
        ResearchPage golemPowder1 = new ResearchPage("golemPowder1");
        ResearchPage golemPowder2 = new ResearchPage(recipeGolemPowder);
        ResearchPage golemPowder3 = new ResearchPage("golemPowder2");
        ResearchPage golemPowder4 = new ResearchPage("golemPowder3");
        golemPowder.setPages(golemPowder1, golemPowder2, golemPowder3, golemPowder4);
        golemPowder.setParents("GOLEMTHAUMIUM", "ADVANCEDGOLEM", "incarnationVat");
        golemPowder.setConcealed();
        ResearchCategories.addResearch(golemPowder);
        ResearchItem planarTheory = new ResearchItem(
                "planarTheory",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.VOID, 5).add(Aspect.MAGIC, 5).add(Aspect.ELDRITCH, 5),
                10,
                8,
                3,
                new ItemStack(itemPlanarConduit));
        recipeConduit = ThaumcraftApi.addArcaneCraftingRecipe(
                "planarTheory",
                new ItemStack(itemPlanarConduit),
                (new AspectList()).add(Aspect.FIRE, 50).add(Aspect.WATER, 50).add(Aspect.ORDER, 50)
                        .add(Aspect.ENTROPY, 50).add(Aspect.EARTH, 50).add(Aspect.AIR, 50),
                "VEV",
                "EPE",
                "VEV",
                'V',
                new ItemStack(ConfigItems.itemResource, 1, 16),
                'E',
                new ItemStack(ConfigItems.itemWispEssence, 1, 32767),
                'P',
                new ItemStack(Items.ENDER_PEARL));
        ResearchPage planarTheory1 = new ResearchPage("planarTheory1");
        ResearchPage planarTheory2 = new ResearchPage(recipeConduit);
        ResearchPage planarTheory3 = new ResearchPage("planarTheory2");
        planarTheory.setPages(planarTheory1, planarTheory2, planarTheory3);
        planarTheory.setParents("VISPOWER", "VOIDMETAL");
        planarTheory.setConcealed();
        ResearchCategories.addResearch(planarTheory);
        ResearchItem transductionAmplifier = new ResearchItem(
                "transductionAmplifier",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.AURA, 4).add(Aspect.ENERGY, 4).add(Aspect.VOID, 3),
                12,
                6,
                3,
                new ItemStack(blockTransducer));
        recipeTransducer = ThaumcraftApi.addArcaneCraftingRecipe(
                "transductionAmplifier",
                new ItemStack(blockTransducer),
                (new AspectList()).add(Aspect.AIR, 10).add(Aspect.FIRE, 10).add(Aspect.ORDER, 10),
                " C ",
                "ATA",
                "RNR",
                'C',
                new ItemStack(itemPlanarConduit),
                'A',
                new ItemStack(ConfigItems.itemResource, 1, 6),
                'T',
                new ItemStack(ConfigBlocks.blockStoneDevice, 1, 11),
                'R',
                new ItemStack(Blocks.REDSTONE_BLOCK),
                'N',
                new ItemStack(ConfigItems.itemResource, 1, 1));
        ResearchPage transductionAmplifier1 = new ResearchPage("transductionAmplifier1");
        ResearchPage transductionAmplifier2 = new ResearchPage(recipeTransducer);
        ResearchPage transductionAmplifier3 = new ResearchPage("transductionAmplifier2");
        transductionAmplifier.setPages(transductionAmplifier1, transductionAmplifier2, transductionAmplifier3);
        transductionAmplifier.setParents("planarTheory");
        transductionAmplifier.setConcealed();
        ResearchCategories.addResearch(transductionAmplifier);
        ResearchItem vortexStabilizer = new ResearchItem(
                "vortexStabilizer",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.AURA, 3).add(Aspect.HUNGER, 3).add(Aspect.TRAP, 3),
                12,
                10,
                2,
                new ItemStack(blockVortexStabilizer));
        recipeVortexStabilizer = ThaumcraftApi.addInfusionCraftingRecipe(
                "vortexStabilizer",
                new ItemStack(blockVortexStabilizer),
                6,
                (new AspectList()).add(Aspect.AURA, 8).add(Aspect.TRAP, 32).add(Aspect.HUNGER, 32)
                        .add(Aspect.ENERGY, 16),
                new ItemStack(ConfigBlocks.blockStoneDevice, 1, 9),
                new ItemStack[] { new ItemStack(itemPlanarConduit), new ItemStack(ConfigItems.itemResource, 1, 2),
                        new ItemStack(ConfigItems.itemResource, 1, 6), new ItemStack(ConfigItems.itemResource, 1, 2) });
        ResearchPage vortexStabilizer1 = new ResearchPage("vortexStabilizer1");
        ResearchPage vortexStabilizer2 = new ResearchPage(recipeVortexStabilizer);
        vortexStabilizer.setPages(vortexStabilizer1, vortexStabilizer2);
        vortexStabilizer.setParents("planarTheory");
        vortexStabilizer.setConcealed();
        ResearchCategories.addResearch(vortexStabilizer);
        ResearchItem recombinator = new ResearchItem(
                "recombinator",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.AURA, 4).add(Aspect.EXCHANGE, 4).add(Aspect.CRAFT, 4),
                12,
                8,
                3,
                new ItemStack(blockRecombinator));
        recipeRecombinator = ThaumcraftApi.addInfusionCraftingRecipe(
                "recombinator",
                new ItemStack(blockRecombinator),
                9,
                (new AspectList()).add(Aspect.EXCHANGE, 32).add(Aspect.AURA, 20).add(Aspect.CRAFT, 32)
                        .add(Aspect.ENERGY, 32),
                new ItemStack(ConfigItems.itemEldritchObject, 1, 3),
                new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 8), new ItemStack(itemPlanarConduit),
                        new ItemStack(ConfigItems.itemResource, 1, 8),
                        new ItemStack(ConfigBlocks.blockStoneDevice, 1, 11),
                        new ItemStack(ConfigItems.itemResource, 1, 8), new ItemStack(itemPlanarConduit),
                        new ItemStack(ConfigItems.itemResource, 1, 8),
                        new ItemStack(ConfigBlocks.blockStoneDevice, 1, 9) });
        ResearchPage recombinator1 = new ResearchPage("recombinator1");
        ResearchPage recombinator2 = new ResearchPage(recipeRecombinator);
        ResearchPage recombinator3 = new ResearchPage("recombinator2");
        ResearchPage recombinator4 = new ResearchPage("recombinator3");
        recombinator.setPages(recombinator1, recombinator2, recombinator3, recombinator4);
        recombinator.setParents("planarTheory", "PRIMPEARL");
        recombinator.setConcealed();
        ResearchCategories.addResearch(recombinator);
        ResearchItem newHorizons = new ResearchItem(
                "planarRift",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.VOID, 4).add(Aspect.MAGIC, 4).add(Aspect.ELDRITCH, 2),
                14,
                8,
                2,
                new ResourceLocation("thaumichorizons", "textures/misc/rift.png"));
        ResearchPage newHorizons1 = new ResearchPage("planarRift1");
        ResearchPage newHorizons2 = new ResearchPage("planarRift2");
        ResearchPage newHorizons3 = new ResearchPage("planarRift3");
        newHorizons.setPages(newHorizons1, newHorizons2, newHorizons3);
        newHorizons.setParents("vortexStabilizer", "transductionAmplifier");
        newHorizons.setConcealed();
        ThaumcraftApi.addWarpToResearch("planarRift", 3);
        newHorizons.setSpecial();
        ResearchCategories.addResearch(newHorizons);
        ResearchItem wispSpawn = new ResearchItem(
                "wispSpawn",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAGIC, 2).add(Aspect.CRYSTAL, 2).add(Aspect.LIFE, 2),
                15,
                6,
                1,
                new ResourceLocation("thaumichorizons", "textures/misc/wisp.png"));
        ResearchPage wispSpawn1 = new ResearchPage("wispSpawn1");
        wispSpawn.setPages(wispSpawn1);
        wispSpawn.setParents("planarRift");
        wispSpawn.setConcealed();
        ResearchCategories.addResearch(wispSpawn);
        ResearchItem crystalWand = new ResearchItem(
                "crystalWand",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.CRYSTAL, 4).add(Aspect.ENERGY, 4),
                15,
                10,
                2,
                ((ItemWandCastingDisposable) itemWandCastingDisposable).wand);
        recipeCrystalWand = ThaumcraftApi.addArcaneCraftingRecipe(
                "crystalWand",
                new ItemStack(itemCrystalWand),
                (new AspectList()).add(Aspect.AIR, 25).add(Aspect.EARTH, 25).add(Aspect.FIRE, 25).add(Aspect.WATER, 25)
                        .add(Aspect.ORDER, 25).add(Aspect.ENTROPY, 25),
                "  B",
                " C ",
                "B  ",
                'C',
                new ItemStack(ConfigBlocks.blockCrystal, 1, 6),
                'B',
                new ItemStack(ConfigItems.itemShard, 1, 6));
        ResearchPage crystalWand1 = new ResearchPage("crystalWand1");
        ResearchPage crystalWand2 = new ResearchPage(recipeCrystalWand);
        crystalWand.setPages(crystalWand1, crystalWand2);
        crystalWand.setParents("planarRift");
        crystalWand.setConcealed();
        ResearchCategories.addResearch(crystalWand);
        ResearchItem voidPutty = new ResearchItem(
                "voidPutty",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.EXCHANGE, 3).add(Aspect.CRAFT, 3).add(Aspect.METAL, 3),
                16,
                9,
                2,
                new ItemStack(itemVoidPutty));
        ResearchPage voidPutty1 = new ResearchPage("voidPutty1");
        ItemStack damaged = new ItemStack(ConfigItems.itemSwordElemental, 1, 32767);
        ArrayList<ItemStack> voidComponents = new ArrayList<>();
        voidComponents.add(damaged);
        voidComponents.add(new ItemStack(itemVoidPutty));
        ResearchPage voidPutty2 = new ResearchPage(
                new ShapelessOreRecipe(new ResourceLocation("thaumichorizons", "void_putty_preview"),
                        new ItemStack(ConfigItems.itemSwordElemental), damaged, new ItemStack(itemVoidPutty)));
        voidPutty.setPages(voidPutty1, voidPutty2);
        voidPutty.setParents("planarRift");
        voidPutty.setConcealed();
        ResearchCategories.addResearch(voidPutty);
        ResearchItem voidGolem = new ResearchItem(
                "voidGolem",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.VOID, 2).add(Aspect.MOTION, 2).add(Aspect.SOUL, 2).add(Aspect.MAN, 2),
                16,
                7,
                1,
                new ResourceLocation("thaumichorizons", "textures/misc/voidgolem.png"));
        ResearchPage voidGolem1 = new ResearchPage("voidGolem1");
        voidGolem.setPages(voidGolem1);
        voidGolem.setParentsHidden("golemPowder");
        voidGolem.setParents("wispSpawn");
        voidGolem.setConcealed();
        ThaumcraftApi.addWarpToResearch("voidGolem", 1);
        ResearchCategories.addResearch(voidGolem);
        ResearchPage infusion1;
        ResearchPage infusion3;
        ResearchPage infusion4;
        ResearchPage infusion5;
        ResearchPage infusion7;
        ResearchPage infusion8;
        ResearchPage infusion10;
        ResearchPage infusion11;
        ResearchPage infusion12;
        ResearchPage infusion13b;
        if (enablePocket) {
            ResearchItem dummyTag = new ResearchItem(
                    "pocketPlane",
                    "ThaumicHorizons",
                    (new AspectList()).add(Aspect.TRAVEL, 24).add(Aspect.VOID, 21).add(Aspect.CRAFT, 18)
                            .add(Aspect.AURA, 15).add(Aspect.MECHANISM, 12).add(Aspect.ELDRITCH, 6)
                            .add(Aspect.MECHANISM, 9).add(Aspect.MAGIC, 6).add(Aspect.MIND, 3),
                    18,
                    8,
                    4,
                    new ResourceLocation("thaumichorizons", "textures/misc/pocketplane.png"));

            if (Loader.isModLoaded("gregtech") && !Loader.isModLoaded("gregapi")
                    && com.kentington.thaumichorizons.common.lib.OreIngredients.available("gemExquisiteGlass", "lensInfusedOrder", "lensGlass", "plateDenseVoid", "screwTitanium", "plateDenseEnderium", "plateDenseInfusedGold")) {
                recipeKeystone = ThaumcraftApi.addInfusionCraftingRecipe(
                        "pocketPlane",
                        new ItemStack(itemKeystone),
                        12,
                        (new AspectList()).add(Aspect.TRAVEL, 128).add(Aspect.VOID, 64).add(Aspect.EXCHANGE, 48)
                                .add(Aspect.AURA, 32).add(Aspect.MIND, 16).add(Aspect.ELDRITCH, 64),
                        new ItemStack(itemPlanarConduit),
                        new ItemStack[] { new ItemStack(ConfigItems.itemShard, 1, 6),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("gemExquisiteGlass"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("lensInfusedOrder"),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("gemExquisiteGlass"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("lensInfusedOrder"),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("gemExquisiteGlass"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("lensInfusedOrder"),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("gemExquisiteGlass"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("lensInfusedOrder") });
                recipeKeystone1 = ThaumcraftApi.addInfusionCraftingRecipe(
                        "planarKeystone",
                        new ItemStack(itemKeystone),
                        9,
                        (new AspectList()).add(Aspect.TRAVEL, 64).add(Aspect.VOID, 48).add(Aspect.MECHANISM, 32)
                                .add(Aspect.ELDRITCH, 32),
                        new ItemStack(Items.ENDER_EYE),
                        new ItemStack[] { new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(ConfigItems.itemResource, 1, 3),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("lensGlass"),
                                new ItemStack(ConfigItems.itemResource, 1, 3),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(ConfigItems.itemResource, 1, 3),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("lensGlass"),
                                new ItemStack(ConfigItems.itemResource, 1, 3) });
                recipeSlot = ThaumcraftApi.addInfusionCraftingRecipe(
                        "planarKeystone",
                        new ItemStack(blockSlot),
                        12,
                        (new AspectList()).add(Aspect.VOID, 48).add(Aspect.TRAVEL, 64).add(Aspect.AIR, 64)
                                .add(Aspect.MECHANISM, 32).add(Aspect.ELDRITCH, 64).add(Aspect.MAGIC, 16),
                        new ItemStack(itemPlanarConduit),
                        new ItemStack[] { com.kentington.thaumichorizons.common.lib.OreIngredients.get("plateDenseVoid"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("screwTitanium"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("plateDenseEnderium"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("plateDenseVoid"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("plateDenseInfusedGold"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("screwTitanium"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("plateDenseVoid"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("screwTitanium"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("plateDenseInfusedGold"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("plateDenseVoid"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("plateDenseEnderium"),
                                com.kentington.thaumichorizons.common.lib.OreIngredients.get("screwTitanium") });
            } else {
                recipeKeystone = ThaumcraftApi.addInfusionCraftingRecipe(
                        "pocketPlane",
                        new ItemStack(itemKeystone),
                        6,
                        (new AspectList()).add(Aspect.TRAVEL, 32).add(Aspect.VOID, 32).add(Aspect.EXCHANGE, 16),
                        new ItemStack(itemPlanarConduit),
                        new ItemStack[] { new ItemStack(Items.QUARTZ), new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(Items.QUARTZ), new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(Items.QUARTZ), new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(Items.QUARTZ), new ItemStack(ConfigItems.itemShard, 1, 6) });
                recipeKeystone1 = ThaumcraftApi.addInfusionCraftingRecipe(
                        "planarKeystone",
                        new ItemStack(itemKeystone),
                        6,
                        (new AspectList()).add(Aspect.TRAVEL, 24).add(Aspect.VOID, 24).add(Aspect.ELDRITCH, 12),
                        new ItemStack(Items.DIAMOND),
                        new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 3),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(ConfigItems.itemResource, 1, 3),
                                new ItemStack(ConfigItems.itemShard, 1, 6) });
                recipeSlot = ThaumcraftApi.addInfusionCraftingRecipe(
                        "planarKeystone",
                        new ItemStack(blockSlot),
                        8,
                        (new AspectList()).add(Aspect.VOID, 32).add(Aspect.TRAVEL, 24).add(Aspect.MECHANISM, 24)
                                .add(Aspect.ELDRITCH, 24),
                        new ItemStack(itemPlanarConduit),
                        new ItemStack[] { new ItemStack(Items.GOLD_INGOT),
                                new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.GOLD_INGOT),
                                new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.GOLD_INGOT),
                                new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.GOLD_INGOT),
                                new ItemStack(ConfigItems.itemResource, 1, 16) });
            }
            ThaumcraftApi.addWarpToResearch("pocketPlane", 4);
            ThaumcraftApi.addWarpToResearch("planarKeystone", 5);

            ResearchPage dummyStack = new ResearchPage("pocketPlane1");
            ResearchPage dummyInputStack = new ResearchPage("pocketPlane2");
            ResearchPage generalInfusion = new ResearchPage("pocketPlane3");
            infusion1 = new ResearchPage(recipeKeystone);
            dummyTag.setPages(dummyStack, dummyInputStack, generalInfusion, infusion1);
            dummyTag.setParents("planarRift", "PRIMPEARL");
            dummyTag.setConcealed();
            ResearchCategories.addResearch(dummyTag);
            ResearchItem infusion2 = new ResearchItem(
                    "planarClouds",
                    "ThaumicHorizons",
                    new AspectList(),
                    20,
                    6,
                    1,
                    new ItemStack(blockCloud));
            infusion3 = new ResearchPage("planarClouds1");
            infusion4 = new ResearchPage("planarClouds2");
            infusion5 = new ResearchPage("planarClouds3");
            infusion2.setPages(infusion3, infusion4, infusion5);
            infusion2.setItemTriggers(new ItemStack(blockCloud, 1, 32767), new ItemStack(blockCloudGlowing, 1, 32767));
            infusion2.setLost();
            infusion2.setRound();
            infusion2.setParents("pocketPlane");
            ResearchCategories.addResearch(infusion2);
            ResearchItem infusion6 = new ResearchItem(
                    "leviathan",
                    "ThaumicHorizons",
                    new AspectList(),
                    20,
                    10,
                    1,
                    new ItemStack(blockLeviathan));
            infusion7 = new ResearchPage("leviathan1");
            infusion8 = new ResearchPage("leviathan2");
            infusion6.setPages(infusion7, infusion8);
            infusion6.setItemTriggers(new ItemStack(blockLeviathan));
            infusion6.setLost();
            infusion6.setRound();
            infusion6.setParents("pocketPlane");
            ResearchCategories.addResearch(infusion6);
            ResearchItem infusion9 = new ResearchItem(
                    "planarKeystone",
                    "ThaumicHorizons",
                    (new AspectList()).add(Aspect.TRAVEL, 15).add(Aspect.VOID, 12).add(Aspect.AURA, 9)
                            .add(Aspect.ELDRITCH, 6).add(Aspect.MAGIC, 3),
                    20,
                    8,
                    4,
                    new ItemStack(blockSlot));
            infusion10 = new ResearchPage("planarKeystone1");
            infusion11 = new ResearchPage(recipeKeystone1);
            infusion12 = new ResearchPage("planarKeystone2");
            ResearchPage instilledLoyalty = new ResearchPage(recipeSlot);
            ItemStack infusion13 = new ItemStack(ConfigBlocks.blockHole, 1, 15);
            ConfigResearch.recipes.put(
                    "PlanarGateway",
                    Arrays.asList(
                            (new AspectList()).add(Aspect.AIR, 250).add(Aspect.FIRE, 250).add(Aspect.WATER, 250)
                                    .add(Aspect.EARTH, 250).add(Aspect.ENTROPY, 250).add(Aspect.ORDER, 250),
                            5,
                            5,
                            1,
                            Arrays.asList(
                                    new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    new ItemStack(blockSlot),
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    infusion13,
                                    infusion13,
                                    infusion13,
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    infusion13,
                                    infusion13,
                                    infusion13,
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    infusion13,
                                    infusion13,
                                    infusion13,
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11),
                                    new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3))));
            infusion13b = new ResearchPage((List) ConfigResearch.recipes.get("PlanarGateway"));
            infusion9.setPages(infusion10, infusion11, infusion12, instilledLoyalty, infusion13b);
            infusion9.setParents("pocketPlane");
            infusion9.setConcealed();
            ResearchCategories.addResearch(infusion9);
        }
        ResearchItem generalInfusion1 = new ResearchItem(
                "generalInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.BEAST, 1),
                12,
                -3,
                1,
                new ItemStack(itemInfusionCheat, 1, 5));
        infusion1 = new ResearchPage("generalInfusion1");
        ItemStack dummyStack1 = new ItemStack(itemInfusionCheat, 1, 1);
        ItemStack dummyInputStack1 = new ItemStack(itemDummy, 1, 7);
        ResearchPage infusion21 = new ResearchPage(
                new InfusionRecipe(
                        "generalInfusion",
                        dummyStack1,
                        3,
                        (new AspectList()).add(Aspect.MOTION, 16).add(Aspect.MECHANISM, 8).add(Aspect.FLIGHT, 4),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.POTIONITEM, 1, 8226),
                                new ItemStack(ConfigItems.itemResource, 1, 3),
                                new ItemStack(ConfigItems.itemResource, 1, 3) }));
        infusion3 = new ResearchPage("generalInfusion2");
        dummyStack1 = new ItemStack(itemInfusionCheat, 1, 2);
        infusion4 = new ResearchPage(
                new InfusionRecipe(
                        "generalInfusion",
                        dummyStack1,
                        3,
                        (new AspectList()).add(Aspect.WEAPON, 12).add(Aspect.METAL, 8).add(Aspect.MAGIC, 8),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(ConfigItems.itemSwordThaumium),
                                new ItemStack(ConfigItems.itemSwordThaumium) }));
        infusion5 = new ResearchPage("generalInfusion3");
        dummyStack1 = new ItemStack(itemInfusionCheat, 1, 3);
        ResearchPage infusion61 = new ResearchPage(
                new InfusionRecipe(
                        "generalInfusion",
                        dummyStack1,
                        4,
                        (new AspectList()).add(Aspect.HEAL, 8).add(Aspect.LIFE, 16).add(Aspect.MIND, 4),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.SPECKLED_MELON), new ItemStack(itemSyringeHuman),
                                new ItemStack(Items.GOLDEN_APPLE), new ItemStack(itemSyringeHuman) }));
        infusion7 = new ResearchPage("generalInfusion4");
        dummyStack1 = new ItemStack(itemInfusionCheat, 1, 4);
        infusion8 = new ResearchPage(
                new InfusionRecipe(
                        "generalInfusion",
                        dummyStack1,
                        4,
                        (new AspectList()).add(Aspect.ARMOR, 16).add(Aspect.CRYSTAL, 16).add(Aspect.BEAST, 8),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.LEATHER), new ItemStack(Items.DIAMOND),
                                new ItemStack(Items.DIAMOND) }));
        ResearchPage infusion91 = new ResearchPage("generalInfusion5");
        dummyStack1 = new ItemStack(itemInfusionCheat, 1, 5);
        infusion10 = new ResearchPage(
                new InfusionRecipe(
                        "generalInfusion",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.ELDRITCH, 16).add(Aspect.TRAVEL, 16).add(Aspect.FLESH, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.EGG),
                                new ItemStack(Items.EGG) }));
        infusion11 = new ResearchPage("generalInfusion6");
        dummyStack1 = new ItemStack(itemInfusionCheat, 1, 6);
        infusion12 = new ResearchPage(
                new InfusionRecipe(
                        "generalInfusion",
                        dummyStack1,
                        5,
                        (new AspectList()).add(Aspect.ENERGY, 16).add(Aspect.WEAPON, 12).add(Aspect.WEATHER, 4),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.QUARTZ), new ItemStack(ConfigItems.itemShard, 1, 0),
                                new ItemStack(Items.QUARTZ), new ItemStack(Items.REDSTONE) }));
        generalInfusion1.setRound();
        generalInfusion1.setConcealed();
        generalInfusion1.setPages(
                infusion1,
                infusion21,
                infusion3,
                infusion4,
                infusion5,
                infusion61,
                infusion7,
                infusion8,
                infusion91,
                infusion10,
                infusion11,
                infusion12);
        generalInfusion1.setParents("infusionVat");
        ResearchCategories.addResearch(generalInfusion1);
        ResearchItem instilledLoyalty1 = new ResearchItem(
                "instilledLoyalty",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MIND, 4).add(Aspect.BEAST, 3).add(Aspect.EXCHANGE, 3),
                15,
                -1,
                2,
                new ItemStack(itemInfusionCheat, 1, 7));
        ResearchPage infusion131 = new ResearchPage("generalInfusion7");
        infusion13b = new ResearchPage("generalInfusion7b");
        dummyStack1 = new ItemStack(itemInfusionCheat, 1, 7);
        ResearchPage infusion14 = new ResearchPage(
                new InfusionRecipe(
                        "generalInfusion",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.MIND, 24).add(Aspect.BEAST, 12).add(Aspect.EXCHANGE, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.SADDLE), new ItemStack(ConfigItems.itemZombieBrain),
                                new ItemStack(Items.GOLDEN_APPLE) }));
        instilledLoyalty1.setPages(infusion131, infusion14, infusion13b);
        instilledLoyalty1.setParents("generalInfusion");
        instilledLoyalty1.setConcealed();
        ResearchCategories.addResearch(instilledLoyalty1);
        ResearchItem runicHide = new ResearchItem(
                "runicCreature",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.ENERGY, 4).add(Aspect.ARMOR, 4).add(Aspect.MAGIC, 4),
                15,
                -3,
                2,
                new ResourceLocation("thaumcraft", "textures/misc/r_runicupg.png"));
        ResearchPage infusion15 = new ResearchPage("generalInfusion8");
        dummyStack1 = new ItemStack(itemInfusionCheat, 1, 8);
        ResearchPage infusion16 = new ResearchPage(
                new InfusionRecipe(
                        "runicCreature",
                        dummyStack1,
                        7,
                        (new AspectList()).add(Aspect.ARMOR, 16).add(Aspect.MAGIC, 32).add(Aspect.ENERGY, 16)
                                .add(Aspect.FLESH, 8),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.DIAMOND), new ItemStack(ConfigItems.itemResource, 1, 6),
                                new ItemStack(ConfigItems.itemResource, 1, 7),
                                new ItemStack(ConfigItems.itemResource, 1, 1),
                                new ItemStack(ConfigItems.itemInkwell) }));
        runicHide.setPages(infusion15, infusion16);
        runicHide.setParents("generalInfusion", "RUNICAUGMENTATION");
        runicHide.setSecondary();
        runicHide.setConcealed();
        ResearchCategories.addResearch(runicHide);
        ResearchItem eldritchFangs = new ResearchItem(
                "eldritchFangs",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.ELDRITCH, 4).add(Aspect.WEAPON, 4).add(Aspect.POISON, 4),
                15,
                -2,
                2,
                new ItemStack(itemInfusionCheat, 1, 9));
        ResearchPage infusion17 = new ResearchPage("generalInfusion9");
        dummyStack1 = new ItemStack(itemInfusionCheat, 1, 9);
        ResearchPage infusion18 = new ResearchPage(
                new InfusionRecipe(
                        "eldritchFangs",
                        dummyStack1,
                        8,
                        (new AspectList()).add(Aspect.ELDRITCH, 8).add(Aspect.WEAPON, 16).add(Aspect.METAL, 16)
                                .add(Aspect.POISON, 32),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 16),
                                new ItemStack(ConfigBlocks.blockCustomPlant, 1, 5),
                                new ItemStack(ConfigItems.itemResource, 1, 16),
                                new ItemStack(ConfigBlocks.blockCustomPlant, 1, 5) }));
        eldritchFangs.setPages(infusion17, infusion18);
        eldritchFangs.setConcealed();
        eldritchFangs.setParents("generalInfusion", "VOIDMETAL");
        ResearchCategories.addResearch(eldritchFangs);
        ResearchItem portability = new ResearchItem(
                "portability",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.VOID, 4).add(Aspect.TRAP, 4),
                15,
                -4,
                2,
                new ItemStack(itemInfusionCheat, 1, 10));
        ResearchPage portability1 = new ResearchPage("portability1");
        dummyStack1 = new ItemStack(itemInfusionCheat, 1, 10);
        ResearchPage portability2 = new ResearchPage(
                new InfusionRecipe(
                        "portability",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.VOID, 16).add(Aspect.TRAP, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockJar), new ItemStack(Items.ENDER_PEARL) }));
        portability.setPages(portability1, portability2);
        portability.setConcealed();
        portability.setParents("generalInfusion");
        ResearchCategories.addResearch(portability);
        ResearchItem sheepInfusion = new ResearchItem(
                "sheepInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.CLOTH, 3).add(Aspect.SENSES, 3),
                10,
                -5,
                1,
                new ItemStack(itemDummy, 1, 2));
        ResearchPage sheepInfusion1 = new ResearchPage("sheepInfusion1");
        NBTTagCompound dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.ChromaticSheep.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 2);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 2);
        ResearchPage sheepInfusion2 = new ResearchPage(
                new InfusionRecipe(
                        "sheepInfusion",
                        dummyStack1,
                        2,
                        (new AspectList()).add(Aspect.SENSES, 8).add(Aspect.EXCHANGE, 4),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.DYE, 1, 1), new ItemStack(Items.DYE, 1, 4),
                                new ItemStack(Items.DYE, 1, 11) }));
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.SelfShearingSheep.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 2);
        dummyStack1.setTagCompound(dummyTag1);
        ResearchPage sheepInfusion3 = new ResearchPage(
                new InfusionRecipe(
                        "sheepInfusion",
                        dummyStack1,
                        2,
                        (new AspectList()).add(Aspect.TOOL, 8).add(Aspect.CLOTH, 4),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.SHEARS), new ItemStack(Items.COMPARATOR) }));
        sheepInfusion.setParents("generalInfusion");
        sheepInfusion.setPages(sheepInfusion1, sheepInfusion2, sheepInfusion3);
        sheepInfusion.setSecondary();
        sheepInfusion.setConcealed();
        ResearchCategories.addResearch(sheepInfusion);
        ResearchItem catInfusion = new ResearchItem(
                "catInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.MAGIC, 3).add(Aspect.UNDEAD, 3),
                13,
                -5,
                1,
                new ItemStack(itemDummy, 1, 4));
        ResearchPage catInfusion1 = new ResearchPage("catInfusion1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.GuardianPanther.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 4);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 4);
        ResearchPage catInfusion2 = new ResearchPage(
                new InfusionRecipe(
                        "catInfusion",
                        dummyStack1,
                        2,
                        (new AspectList()).add(Aspect.BEAST, 8).add(Aspect.WEAPON, 8),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.POTIONITEM, 1, 8233), new ItemStack(Items.SADDLE) }));
        ResearchPage catInfusion3 = new ResearchPage("catInfusion2");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.Familiar.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 4);
        dummyStack1.setTagCompound(dummyTag1);
        ResearchPage catInfusion4 = new ResearchPage(
                new InfusionRecipe(
                        "catInfusion",
                        dummyStack1,
                        4,
                        (new AspectList()).add(Aspect.TRAP, 8).add(Aspect.AURA, 4).add(Aspect.ENERGY, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(Blocks.WOOL, 1, 15),
                                new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(Items.GOLD_NUGGET) }));
        ResearchPage catInfusion5 = new ResearchPage("catInfusion3");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.Gravekeeper.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 4);
        dummyStack1.setTagCompound(dummyTag1);
        ResearchPage catInfusion6 = new ResearchPage(
                new InfusionRecipe(
                        "catInfusion",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.ORDER, 16).add(Aspect.DEATH, 8).add(Aspect.LIGHT, 24),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.BONE), new ItemStack(Blocks.GOLD_BLOCK),
                                new ItemStack(ConfigItems.itemResource, 1, 6),
                                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1) }));
        catInfusion.setParents("generalInfusion");
        catInfusion.setConcealed();
        catInfusion.setPages(catInfusion1, catInfusion2, catInfusion3, catInfusion4, catInfusion5, catInfusion6);
        ResearchCategories.addResearch(catInfusion);
        ResearchItem cowInfusion = new ResearchItem(
                "cowInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.SLIME, 5).add(Aspect.EXCHANGE, 5),
                9,
                -5,
                1,
                new ItemStack(itemDummy, 1, 0));
        ResearchPage cowInfusion1 = new ResearchPage("cowInfusion1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.ChocolateCow.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 0);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 0);
        ResearchPage cowInfusion2 = new ResearchPage(
                new InfusionRecipe(
                        "cowInfusion",
                        dummyStack1,
                        1,
                        (new AspectList()).add(Aspect.HUNGER, 4).add(Aspect.EXCHANGE, 2),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.DYE, 1, 3), new ItemStack(Items.SUGAR),
                                new ItemStack(Items.DYE, 1, 3), new ItemStack(Items.SUGAR) }));
        IRecipe chocolate = ThaumicHorizonsRegistries.getChocolateBarRecipe();
        ResearchPage cowInfusion3 = new ResearchPage(chocolate);
        ResearchPage cowInfusion4 = new ResearchPage(
                ThaumcraftApi.addShapelessArcaneCraftingRecipe(
                        "cowInfusion",
                        new ItemStack(itemIceCream),
                        (new AspectList()).add(Aspect.WATER, 1),
                        new ItemStack(itemBucketChocolate),
                        new ItemStack(Items.WHEAT)));
        ResearchPage cowInfusion5 = new ResearchPage("cowInfusion2");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.MushroomCow.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 0);
        dummyStack1.setTagCompound(dummyTag1);
        ResearchPage cowInfusion6 = new ResearchPage(
                new InfusionRecipe(
                        "cowInfusion",
                        dummyStack1,
                        2,
                        (new AspectList()).add(Aspect.PLANT, 8).add(Aspect.BEAST, 4),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM),
                                new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM) }));
        cowInfusion.setParents("generalInfusion");
        cowInfusion.setConcealed();
        cowInfusion.setPages(cowInfusion1, cowInfusion2, cowInfusion3, cowInfusion4, cowInfusion5, cowInfusion6);
        ResearchCategories.addResearch(cowInfusion);
        ResearchItem chickenInfusion = new ResearchItem(
                "chickenInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.SLIME, 5).add(Aspect.GREED, 3)
                        .add(Aspect.DARKNESS, 3),
                11,
                -5,
                1,
                new ItemStack(itemDummy, 1, 3));
        ResearchPage chickenInfusion1 = new ResearchPage("chickenInfusion1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.ScholarChicken.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 3);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 3);
        ResearchPage chickenInfusion2 = new ResearchPage(
                new InfusionRecipe(
                        "chickenInfusion",
                        dummyStack1,
                        2,
                        (new AspectList()).add(Aspect.DARKNESS, 4).add(Aspect.SLIME, 4).add(Aspect.EXCHANGE, 2),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.DYE, 1, 0), new ItemStack(Items.SHEARS) }));
        ResearchPage chickenInfusion3 = new ResearchPage("chickenInfusion2");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.GoldChicken.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 3);
        dummyStack1.setTagCompound(dummyTag1);
        ResearchPage chickenInfusion4 = new ResearchPage(
                new InfusionRecipe(
                        "chickenInfusion",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.GREED, 8).add(Aspect.METAL, 8).add(Aspect.CRAFT, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.GOLDEN_APPLE),
                                new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), new ItemStack(Items.GOLDEN_APPLE),
                                new ItemStack(ConfigBlocks.blockMetalDevice, 1, 0) }));
        GameRegistry.addSmelting(itemGoldEgg, new ItemStack(Items.GOLD_NUGGET), 1.0F);
        ResearchPage chickenInfusion6 = new ResearchPage(new ItemStack(itemGoldEgg));
        chickenInfusion.setParents("generalInfusion");
        chickenInfusion.setSecondary();
        chickenInfusion.setConcealed();
        chickenInfusion
                .setPages(chickenInfusion1, chickenInfusion2, chickenInfusion3, chickenInfusion4, chickenInfusion6);
        ResearchCategories.addResearch(chickenInfusion);
        ResearchItem pigInfusion = new ResearchItem(
                "pigInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.HUNGER, 5).add(Aspect.METAL, 3).add(Aspect.TAINT, 3)
                        .add(Aspect.HEAL, 3),
                12,
                -5,
                1,
                new ItemStack(itemDummy, 1, 1));
        ResearchPage pigInfusion1 = new ResearchPage("pigInfusion1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.OrePig.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 1);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 1);
        ResearchPage pigInfusion2 = new ResearchPage(
                new InfusionRecipe(
                        "pigInfusion",
                        dummyStack1,
                        4,
                        (new AspectList()).add(Aspect.EARTH, 8).add(Aspect.HUNGER, 8).add(Aspect.METAL, 16)
                                .add(Aspect.FIRE, 8),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Blocks.FURNACE), new ItemStack(ConfigItems.itemShard, 1, 1),
                                new ItemStack(ConfigItems.itemShard, 1, 5),
                                new ItemStack(ConfigItems.itemShard, 1, 3) }));
        ResearchPage pigInfusion3 = new ResearchPage("pigInfusion2");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.TaintEaterPig.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 1);
        dummyStack1.setTagCompound(dummyTag1);
        ResearchPage pigInfusion4 = new ResearchPage(
                new InfusionRecipe(
                        "pigInfusion",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.TAINT, 6).add(Aspect.HEAL, 8).add(Aspect.PLANT, 8)
                                .add(Aspect.HUNGER, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockCustomPlant, 1, 4),
                                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1),
                                new ItemStack(ConfigBlocks.blockJar, 1, 3),
                                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1) }));
        pigInfusion.setParents("generalInfusion");
        pigInfusion.setConcealed();
        pigInfusion.setPages(pigInfusion1, pigInfusion2, pigInfusion3, pigInfusion4);
        ResearchCategories.addResearch(pigInfusion);
        ResearchItem dogInfusion = new ResearchItem(
                "dogInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.LIGHT, 3).add(Aspect.FIRE, 3).add(Aspect.WATER, 3)
                        .add(Aspect.MAGIC, 3),
                14,
                -5,
                1,
                new ItemStack(itemDummy, 1, 5));
        ResearchPage dogInfusion1 = new ResearchPage("dogInfusion1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.Seawolf.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 5);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 5);
        ResearchPage dogInfusion2 = new ResearchPage(
                new InfusionRecipe(
                        "dogInfusion",
                        dummyStack1,
                        2,
                        (new AspectList()).add(Aspect.WATER, 8).add(Aspect.AIR, 8).add(Aspect.EXCHANGE, 4)
                                .add(Aspect.MOTION, 8),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.FISH, 1, 32767),
                                new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(Items.FISH, 1, 32767),
                                new ItemStack(Items.POTIONITEM, 1, 8269) }));
        ResearchPage dogInfusion3 = new ResearchPage("dogInfusion2");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.LunarWolf.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 5);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 5);
        ResearchPage dogInfusion4 = new ResearchPage(
                new InfusionRecipe(
                        "dogInfusion",
                        dummyStack1,
                        4,
                        (new AspectList()).add(Aspect.LIGHT, 8).add(Aspect.VOID, 8).add(Aspect.MAGIC, 16)
                                .add(Aspect.EXCHANGE, 4),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 10),
                                new ItemStack(ConfigItems.itemResource, 1, 14),
                                new ItemStack(ConfigItems.itemResource, 1, 14) }));
        ResearchPage dogInfusion5 = new ResearchPage("dogInfusion3");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.NetherHound.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 5);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 5);
        ResearchPage dogInfusion6 = new ResearchPage(
                new InfusionRecipe(
                        "dogInfusion",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.FIRE, 24).add(Aspect.WEAPON, 8).add(Aspect.BEAST, 8),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.BLAZE_ROD),
                                new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(Items.BLAZE_ROD) }));
        dogInfusion.setParents("generalInfusion");
        dogInfusion.setConcealed();
        dogInfusion.setPages(dogInfusion1, dogInfusion2, dogInfusion3, dogInfusion4, dogInfusion5, dogInfusion6);
        ResearchCategories.addResearch(dogInfusion);
        ResearchItem horseInfusion = new ResearchItem(
                "horseInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.BEAST, 4).add(Aspect.TRAVEL, 5).add(Aspect.ELDRITCH, 4),
                15,
                -5,
                2,
                new ItemStack(itemDummy, 1, 6));
        ResearchPage horseInfusion1 = new ResearchPage("horseInfusion2");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.Endersteed.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 6);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 6);
        ResearchPage horseInfusion2 = new ResearchPage(
                new InfusionRecipe(
                        "horseInfusion",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.TRAVEL, 16).add(Aspect.VOID, 24).add(Aspect.ELDRITCH, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.EGG),
                                new ItemStack(Items.ENDER_PEARL), new ItemStack(ConfigItems.itemResource, 1, 14) }));
        ResearchPage horseInfusion3 = new ResearchPage("horseInfusion3");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.HorseZombie.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 6);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 6);
        ResearchPage horseInfusion4 = new ResearchPage(
                new InfusionRecipe(
                        "horseInfusion",
                        dummyStack1,
                        2,
                        (new AspectList()).add(Aspect.DEATH, 8).add(Aspect.UNDEAD, 8),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.ROTTEN_FLESH),
                                new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.ROTTEN_FLESH) }));
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.HorseSkeleton.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 6);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 6);
        ResearchPage horseInfusion5 = new ResearchPage(
                new InfusionRecipe(
                        "horseInfusion",
                        dummyStack1,
                        2,
                        (new AspectList()).add(Aspect.DEATH, 8).add(Aspect.UNDEAD, 8),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.BONE), new ItemStack(Items.BONE),
                                new ItemStack(Items.BONE), new ItemStack(Items.BONE) }));
        horseInfusion.setParents("generalInfusion");
        horseInfusion.setConcealed();
        horseInfusion.setPages(horseInfusion1, horseInfusion2, horseInfusion3, horseInfusion4, horseInfusion5);
        ResearchCategories.addResearch(horseInfusion);
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.Endersteed.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 6);
        dummyStack1.setTagCompound(dummyTag1);
        ResearchItem nightmareInfusion = new ResearchItem(
                "nightmareInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.FIRE, 4).add(Aspect.VOID, 3).add(Aspect.TRAVEL, 3),
                17,
                -7,
                2,
                dummyStack1);
        ResearchPage nightmareInfusion1 = new ResearchPage("nightmareInfusion1");
        dummyInputStack1 = dummyStack1;
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.Nightmare.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 6);
        dummyStack1.setTagCompound(dummyTag1);
        ResearchPage nightmareInfusion2 = new ResearchPage(
                new InfusionRecipe(
                        "nightmareInfusion",
                        dummyStack1,
                        8,
                        (new AspectList()).add(Aspect.FIRE, 32).add(Aspect.TRAVEL, 16).add(Aspect.VOID, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.NETHER_STAR), new ItemStack(Blocks.OBSIDIAN),
                                new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(ConfigBlocks.blockLifter),
                                new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(Blocks.OBSIDIAN) }));
        ResearchPage nightmareInfusion3 = new ResearchPage("nightmareInfusion2");
        nightmareInfusion.setParents("horseInfusion", "LEVITATOR");
        nightmareInfusion.setConcealed();
        nightmareInfusion.setPages(nightmareInfusion1, nightmareInfusion2, nightmareInfusion3);
        ResearchCategories.addResearch(nightmareInfusion);
        dummyStack1 = new ItemStack(itemDummy, 1, 2);
        ResearchItem sheederInfusion = new ResearchItem(
                "sheederInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.CLOTH, 4).add(Aspect.BEAST, 3).add(Aspect.EXCHANGE, 3),
                10,
                -7,
                2,
                dummyStack1);
        ResearchPage sheederInfusion1 = new ResearchPage("sheederInfusion1");
        dummyInputStack1 = dummyStack1;
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.Sheeder.name");
        dummyStack1 = new ItemStack(itemDummy, 1, 2);
        dummyStack1.setTagCompound(dummyTag1);
        ResearchPage sheederInfusion2 = new ResearchPage(
                new InfusionRecipe(
                        "sheederInfusion",
                        dummyStack1,
                        5,
                        (new AspectList()).add(Aspect.CLOTH, 32).add(Aspect.EXCHANGE, 8).add(Aspect.POISON, 8),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Blocks.WEB), new ItemStack(Items.SPIDER_EYE),
                                new ItemStack(Blocks.WEB), new ItemStack(Items.SPIDER_EYE) }));
        dummyStack1 = new ItemStack(itemDummy, 1, 8);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 8);
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "entity.ThaumicHorizons.Sheeder.name");
        sheederInfusion.setParents("sheepInfusion");
        dummyStack1.setTagCompound(dummyTag1);
        ResearchPage sheederInfusion3 = new ResearchPage(
                new InfusionRecipe(
                        "sheederInfusion",
                        dummyStack1,
                        5,
                        (new AspectList()).add(Aspect.CLOTH, 32).add(Aspect.EXCHANGE, 8).add(Aspect.MIND, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(ConfigItems.itemZombieBrain),
                                new ItemStack(Blocks.WOOL, 1, 32767), new ItemStack(Blocks.WOOL, 1, 32767),
                                new ItemStack(Blocks.WOOL, 1, 32767) }));
        sheederInfusion.setConcealed();
        sheederInfusion.setPages(sheederInfusion1, sheederInfusion2, sheederInfusion3);
        ResearchCategories.addResearch(sheederInfusion);
        ResearchItem selfInfusion = new ResearchItem(
                "selfInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAN, 6).add(Aspect.MAGIC, 6).add(Aspect.CRAFT, 6),
                14,
                1,
                3,
                new ResourceLocation("thaumichorizons", "textures/items/humaninfusion.png"));
        ResearchPage selfInfusion1 = new ResearchPage("selfInfusion1");
        ResearchPage selfInfusion2 = new ResearchPage("selfInfusion2");
        ResearchPage selfInfusion3 = new ResearchPage("selfInfusion3");
        selfInfusion.setPages(selfInfusion1, selfInfusion2, selfInfusion3);
        selfInfusion.setParents("generalInfusion", "cloneSelf");
        selfInfusion.setConcealed();
        selfInfusion.setSiblings("humanInfusion");
        ResearchCategories.addResearch(selfInfusion);
        ResearchItem humanInfusion = new ResearchItem(
                "humanInfusion",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAN, 1),
                16,
                1,
                1,
                new ResourceLocation("thaumichorizons", "textures/items/quicksilverlimbs.png"));
        ResearchPage humanInfusion1 = new ResearchPage("humanInfusion1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "selfInfusions.quicksilver");
        dummyStack1 = new ItemStack(itemDummy, 1, 15);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 15);
        ResearchPage humanInfusion2 = new ResearchPage(
                new InfusionRecipe(
                        "humanInfusion",
                        dummyStack1,
                        4,
                        (new AspectList()).add(Aspect.MOTION, 64).add(Aspect.MECHANISM, 32).add(Aspect.FLIGHT, 32),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.POTIONITEM, 1, 8226),
                                new ItemStack(ConfigItems.itemResource, 1, 3),
                                new ItemStack(ConfigItems.itemResource, 1, 16),
                                new ItemStack(Items.POTIONITEM, 1, 8226), new ItemStack(ConfigItems.itemResource, 1, 3),
                                new ItemStack(ConfigItems.itemResource, 1, 16) }));
        ResearchPage humanInfusion3 = new ResearchPage("humanInfusion3");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "selfInfusions.awakeBlood");
        dummyStack1 = new ItemStack(itemDummy, 1, 15);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 15);
        ResearchPage humanInfusion4 = new ResearchPage(
                new InfusionRecipe(
                        "humanInfusion",
                        dummyStack1,
                        5,
                        (new AspectList()).add(Aspect.HEAL, 32).add(Aspect.LIFE, 64).add(Aspect.MIND, 32),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.SPECKLED_MELON),
                                new ItemStack(Items.SPECKLED_MELON), new ItemStack(ConfigItems.itemZombieBrain),
                                new ItemStack(Items.SPECKLED_MELON), new ItemStack(Items.SPECKLED_MELON) }));
        ResearchPage humanInfusion5 = new ResearchPage("humanInfusion4");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "selfInfusions.diamondSkin");
        dummyStack1 = new ItemStack(itemDummy, 1, 15);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 15);
        ResearchPage humanInfusion6 = new ResearchPage(
                new InfusionRecipe(
                        "humanInfusion",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.ARMOR, 32).add(Aspect.CRYSTAL, 32).add(Aspect.MAN, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.LEATHER_HELMET), new ItemStack(Items.DIAMOND),
                                new ItemStack(Items.LEATHER_CHESTPLATE), new ItemStack(Blocks.DIAMOND_BLOCK),
                                new ItemStack(Items.LEATHER_LEGGINGS), new ItemStack(Items.DIAMOND),
                                new ItemStack(Items.LEATHER_BOOTS), new ItemStack(Blocks.DIAMOND_BLOCK) }));
        humanInfusion.setParents("selfInfusion");
        humanInfusion.setConcealed();
        humanInfusion.setPages(
                humanInfusion1,
                humanInfusion2,
                humanInfusion3,
                humanInfusion4,
                humanInfusion5,
                humanInfusion6);
        ResearchCategories.addResearch(humanInfusion);
        ResearchItem morphicFingers = new ResearchItem(
                "morphicFingers",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAN, 3).add(Aspect.CRAFT, 3).add(Aspect.TOOL, 3).add(Aspect.EXCHANGE, 2),
                19,
                2,
                2,
                new ResourceLocation("thaumichorizons", "textures/items/morphicfingers.png"));
        ResearchPage morphicFingers1 = new ResearchPage("morphicFingers1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "selfInfusions.morphic");
        dummyStack1 = new ItemStack(itemDummy, 1, 15);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 15);
        ResearchPage morphicFingers2 = new ResearchPage(
                new InfusionRecipe(
                        "morphicFingers",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.TOOL, 32).add(Aspect.CRAFT, 32).add(Aspect.MAN, 32)
                                .add(Aspect.EXCHANGE, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockTable, 1, 15),
                                new ItemStack(ConfigItems.itemResource, 1, 16),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(ConfigItems.itemResource, 1, 16) }));
        morphicFingers.setParents("humanInfusion");
        morphicFingers.setConcealed();
        morphicFingers.setPages(morphicFingers1, morphicFingers2);
        ResearchCategories.addResearch(morphicFingers);
        ResearchItem silverHeart = new ResearchItem(
                "silverHeart",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAN, 3).add(Aspect.ORDER, 3).add(Aspect.HEAL, 3),
                19,
                -1,
                2,
                new ResourceLocation("thaumichorizons", "textures/items/silverwoodheart.png"));
        ResearchPage silverHeart1 = new ResearchPage("silverHeart1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "selfInfusions.silverHeart");
        dummyStack1 = new ItemStack(itemDummy, 1, 15);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 15);
        ResearchPage silverHeart2 = new ResearchPage(
                new InfusionRecipe(
                        "silverHeart",
                        dummyStack1,
                        7,
                        (new AspectList()).add(Aspect.ORDER, 32).add(Aspect.HEAL, 32).add(Aspect.EXCHANGE, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockCustomPlant, 1, 1),
                                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1),
                                new ItemStack(ConfigBlocks.blockCustomPlant, 1, 2),
                                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1) }));
        ResearchPage silverHeart3 = new ResearchPage("silverHeart2");
        silverHeart.setParents("humanInfusion");
        silverHeart.setConcealed();
        silverHeart.setPages(silverHeart1, silverHeart2, silverHeart3);
        ResearchCategories.addResearch(silverHeart);
        ResearchItem synthSkin = new ResearchItem(
                "synthSkin",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAN, 3).add(Aspect.PLANT, 3).add(Aspect.HUNGER, 3),
                19,
                0,
                2,
                new ResourceLocation("thaumichorizons", "textures/items/synthskin.png"));
        ResearchPage synthSkin1 = new ResearchPage("synthSkin1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "selfInfusions.synthSkin");
        dummyStack1 = new ItemStack(itemDummy, 1, 15);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 15);
        ResearchPage synthSkin2 = new ResearchPage(
                new InfusionRecipe(
                        "synthSkin",
                        dummyStack1,
                        6,
                        (new AspectList()).add(Aspect.PLANT, 32).add(Aspect.HUNGER, 32).add(Aspect.LIGHT, 32)
                                .add(Aspect.MAN, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Blocks.LEAVES, 1, 32767),
                                new ItemStack(Blocks.SAPLING, 1, 32767), new ItemStack(Blocks.RED_FLOWER, 1, 32767),
                                new ItemStack(Blocks.VINE) }));
        synthSkin.setParents("humanInfusion");
        synthSkin.setConcealed();
        synthSkin.setPages(synthSkin1, synthSkin2);
        ResearchCategories.addResearch(synthSkin);
        ResearchItem amphibious = new ResearchItem(
                "amphibious",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAN, 3).add(Aspect.AIR, 3).add(Aspect.WATER, 3),
                19,
                1,
                2,
                new ItemStack(Items.FISH));
        ResearchPage amphibious1 = new ResearchPage("amphibious1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "selfInfusions.amphibious");
        dummyStack1 = new ItemStack(itemDummy, 1, 15);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 15);
        ResearchPage amphibious2 = new ResearchPage(
                new InfusionRecipe(
                        "amphibious",
                        dummyStack1,
                        7,
                        (new AspectList()).add(Aspect.WATER, 64).add(Aspect.AIR, 64).add(Aspect.LIFE, 32)
                                .add(Aspect.EXCHANGE, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.FISH, 1, 32767), new ItemStack(Items.POTIONITEM, 1, 8269),
                                new ItemStack(Items.FISH, 1, 32767), new ItemStack(Items.POTIONITEM, 1, 8269) }));
        amphibious.setParents("humanInfusion");
        amphibious.setConcealed();
        amphibious.setPages(amphibious1, amphibious2);
        ResearchCategories.addResearch(amphibious);
        ResearchItem warpedTumor = new ResearchItem(
                "warpedTumor",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAN, 15).add(Aspect.TAINT, 12).add(Aspect.ELDRITCH, 9)
                        .add(Aspect.FLESH, 6).add(Aspect.EXCHANGE, 3),
                19,
                4,
                3,
                new ResourceLocation("thaumichorizons", "textures/items/warpedtumor.png"));
        ResearchPage warpedTumor1 = new PageFormatText("warpedTumor1", ThaumicHorizons.warpedTumorValue);
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "selfInfusions.warpedTumor");
        dummyStack1 = new ItemStack(itemDummy, 1, 15);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 15);
        ResearchPage warpedTumor2 = new ResearchPage(
                new InfusionRecipe(
                        "warpedTumor",
                        dummyStack1,
                        12,
                        (new AspectList()).add(Aspect.TAINT, 64).add(Aspect.ELDRITCH, 48).add(Aspect.TRAP, 32)
                                .add(Aspect.FLESH, 24).add(Aspect.EXCHANGE, 16),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockTaint, 1, 2),
                                new ItemStack(ConfigItems.itemSanitySoap, 1, 0), new ItemStack(Items.NETHER_STAR, 1, 0),
                                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0),
                                new ItemStack(Items.NETHER_STAR, 1, 0),
                                new ItemStack(ConfigItems.itemBathSalts, 1, 0) }));
        warpedTumor.setParents("humanInfusion");
        warpedTumor.setConcealed();
        warpedTumor.setPages(warpedTumor1, warpedTumor2);
        ResearchCategories.addResearch(warpedTumor);
        ResearchItem spiderClimb = new ResearchItem(
                "spiderClimb",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.MAN, 3).add(Aspect.BEAST, 3).add(Aspect.MOTION, 3).add(Aspect.SLIME, 3),
                19,
                3,
                2,
                new ResourceLocation("thaumichorizons", "textures/items/spiderclimb.png"));
        ResearchPage spiderClimb1 = new ResearchPage("spiderClimb1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "selfInfusions.spiderClimb");
        dummyStack1 = new ItemStack(itemDummy, 1, 15);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 15);
        ResearchPage spiderClimb2 = new ResearchPage(
                new InfusionRecipe(
                        "spiderClimb",
                        dummyStack1,
                        8,
                        (new AspectList()).add(Aspect.BEAST, 32).add(Aspect.MOTION, 48).add(Aspect.SLIME, 32),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Blocks.WEB), new ItemStack(ConfigItems.itemResource, 1, 7),
                                new ItemStack(Blocks.WEB), new ItemStack(Blocks.LADDER), new ItemStack(Blocks.WEB),
                                new ItemStack(ConfigItems.itemResource, 1, 7), new ItemStack(Blocks.WEB),
                                new ItemStack(Blocks.LADDER) }));
        spiderClimb.setParents("humanInfusion");
        spiderClimb.setConcealed();
        spiderClimb.setPages(spiderClimb1, spiderClimb2);
        ResearchCategories.addResearch(spiderClimb);
        ResearchItem chameleonSkin = new ResearchItem(
                "chameleonSkin",
                "ThaumicHorizons",
                (new AspectList()).add(Aspect.SENSES, 4).add(Aspect.EXCHANGE, 4).add(Aspect.VOID, 4),
                19,
                -2,
                2,
                new ResourceLocation("thaumichorizons", "textures/items/chameleonskin.png"));
        ResearchPage chameleonSkin1 = new ResearchPage("chameleonSkin1");
        dummyTag1 = new NBTTagCompound();
        dummyTag1.setString("infName", "selfInfusions.chameleonSkin");
        dummyStack1 = new ItemStack(itemDummy, 1, 15);
        dummyStack1.setTagCompound(dummyTag1);
        dummyInputStack1 = new ItemStack(itemDummy, 1, 15);
        ResearchPage chameleonSkin2 = new ResearchPage(
                new InfusionRecipe(
                        "chameleonSkin",
                        dummyStack1,
                        7,
                        (new AspectList()).add(Aspect.SENSES, 48).add(Aspect.EXCHANGE, 32).add(Aspect.VOID, 32),
                        dummyInputStack1,
                        new ItemStack[] { new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.DYE, 1, 1),
                                new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.DYE, 1, 11),
                                new ItemStack(Items.DYE, 1, 0), new ItemStack(Items.DYE, 1, 15) }));
        chameleonSkin.setParents("humanInfusion");
        chameleonSkin.setConcealed();
        chameleonSkin.setPages(chameleonSkin1, chameleonSkin2);
        ResearchCategories.addResearch(chameleonSkin);
        incarnationItems.put(Items.BEEF, 92);
        incarnationItems.put(Items.PORKCHOP, 90);
        incarnationItems.put(Items.CHICKEN, 93);
        WandTriggerRegistry.registerWandBlockTrigger(proxy.wandManager, 0, Blocks.GLASS, -1, "ThaumicHorizons");
        WandTriggerRegistry
                .registerWandBlockTrigger(proxy.wandManager, 0, ConfigBlocks.blockWoodenDevice, 6, "ThaumicHorizons");
        WandTriggerRegistry
                .registerWandBlockTrigger(proxy.wandManager, 0, ConfigBlocks.blockMetalDevice, 9, "ThaumicHorizons");
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "pigInfusion",
                        11,
                        4,
                        (new AspectList()).add(Aspect.EARTH, 8).add(Aspect.HUNGER, 8).add(Aspect.METAL, 16)
                                .add(Aspect.FIRE, 8),
                        EntityPig.class,
                        new ItemStack[] { new ItemStack(Blocks.FURNACE), new ItemStack(ConfigItems.itemShard, 1, 1),
                                new ItemStack(ConfigItems.itemShard, 1, 5),
                                new ItemStack(ConfigItems.itemShard, 1, 3) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "pigInfusion",
                        12,
                        6,
                        (new AspectList()).add(Aspect.TAINT, 6).add(Aspect.HEAL, 8).add(Aspect.PLANT, 8)
                                .add(Aspect.HUNGER, 16),
                        EntityPig.class,
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockCustomPlant, 1, 4),
                                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1),
                                new ItemStack(ConfigBlocks.blockJar, 1, 3),
                                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "chickenInfusion",
                        9,
                        2,
                        (new AspectList()).add(Aspect.DARKNESS, 4).add(Aspect.SLIME, 4).add(Aspect.EXCHANGE, 2),
                        EntityChicken.class,
                        new ItemStack[] { new ItemStack(Items.DYE, 1, 0), new ItemStack(Items.SHEARS) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "chickenInfusion",
                        10,
                        6,
                        (new AspectList()).add(Aspect.GREED, 8).add(Aspect.METAL, 8).add(Aspect.CRAFT, 16),
                        EntityChicken.class,
                        new ItemStack[] { new ItemStack(Items.GOLDEN_APPLE),
                                new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), new ItemStack(Items.GOLDEN_APPLE),
                                new ItemStack(ConfigBlocks.blockMetalDevice, 1, 0) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "cowInfusion",
                        new ResourceLocation("minecraft", "mooshroom"),
                        2,
                        (new AspectList()).add(Aspect.PLANT, 8).add(Aspect.BEAST, 4),
                        EntityCow.class,
                        new ItemStack[] { new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM),
                                new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "cowInfusion",
                        7,
                        2,
                        (new AspectList()).add(Aspect.HUNGER, 4).add(Aspect.EXCHANGE, 2),
                        EntityCow.class,
                        new ItemStack[] { new ItemStack(Items.DYE, 1, 3), new ItemStack(Items.SUGAR),
                                new ItemStack(Items.DYE, 1, 3), new ItemStack(Items.SUGAR) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "sheepInfusion",
                        2,
                        2,
                        (new AspectList()).add(Aspect.SENSES, 8).add(Aspect.EXCHANGE, 4),
                        EntitySheep.class,
                        new ItemStack[] { new ItemStack(Items.DYE, 1, 1), new ItemStack(Items.DYE, 1, 4),
                                new ItemStack(Items.DYE, 1, 11) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "sheepInfusion",
                        3,
                        2,
                        (new AspectList()).add(Aspect.TOOL, 8).add(Aspect.CLOTH, 4),
                        EntitySheep.class,
                        new ItemStack[] { new ItemStack(Items.SHEARS), new ItemStack(Items.COMPARATOR) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "sheederInfusion",
                        29,
                        5,
                        (new AspectList()).add(Aspect.CLOTH, 32).add(Aspect.EXCHANGE, 8).add(Aspect.POISON, 8),
                        EntitySheep.class,
                        new ItemStack[] { new ItemStack(Blocks.WEB), new ItemStack(Items.SPIDER_EYE),
                                new ItemStack(Blocks.WEB), new ItemStack(Items.SPIDER_EYE) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "sheederInfusion",
                        29,
                        5,
                        (new AspectList()).add(Aspect.CLOTH, 32).add(Aspect.EXCHANGE, 8).add(Aspect.MIND, 16),
                        EntitySpider.class,
                        new ItemStack[] { new ItemStack(ConfigItems.itemZombieBrain),
                                new ItemStack(Blocks.WOOL, 1, 32767), new ItemStack(Blocks.WOOL, 1, 32767),
                                new ItemStack(Blocks.WOOL, 1, 32767) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "catInfusion",
                        4,
                        2,
                        (new AspectList()).add(Aspect.BEAST, 8).add(Aspect.WEAPON, 8),
                        EntityOcelot.class,
                        new ItemStack[] { new ItemStack(Items.POTIONITEM, 1, 8233), new ItemStack(Items.SADDLE) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "catInfusion",
                        5,
                        6,
                        (new AspectList()).add(Aspect.ORDER, 16).add(Aspect.DEATH, 8).add(Aspect.LIGHT, 24),
                        EntityOcelot.class,
                        new ItemStack[] { new ItemStack(Items.BONE), new ItemStack(Blocks.GOLD_BLOCK),
                                new ItemStack(ConfigItems.itemResource, 1, 6),
                                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "catInfusion",
                        6,
                        4,
                        (new AspectList()).add(Aspect.TRAP, 8).add(Aspect.AURA, 4).add(Aspect.ENERGY, 16),
                        EntityOcelot.class,
                        new ItemStack[] { new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(Blocks.WOOL, 1, 15),
                                new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(Items.GOLD_NUGGET) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "dogInfusion",
                        14,
                        2,
                        (new AspectList()).add(Aspect.WATER, 8).add(Aspect.AIR, 8).add(Aspect.EXCHANGE, 4)
                                .add(Aspect.MOTION, 8),
                        EntityWolf.class,
                        new ItemStack[] { new ItemStack(Items.FISH, 1, 32767),
                                new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(Items.FISH, 1, 32767),
                                new ItemStack(Items.POTIONITEM, 1, 8269) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "dogInfusion",
                        16,
                        4,
                        (new AspectList()).add(Aspect.LIGHT, 8).add(Aspect.VOID, 8).add(Aspect.MAGIC, 16)
                                .add(Aspect.EXCHANGE, 4),
                        EntityWolf.class,
                        new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 14),
                                new ItemStack(ConfigItems.itemResource, 1, 10),
                                new ItemStack(ConfigItems.itemResource, 1, 14) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "dogInfusion",
                        15,
                        6,
                        (new AspectList()).add(Aspect.FIRE, 24).add(Aspect.WEAPON, 8).add(Aspect.BEAST, 8),
                        EntityWolf.class,
                        new ItemStack[] { new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.BLAZE_ROD),
                                new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(Items.BLAZE_ROD) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "horseInfusion",
                        13,
                        6,
                        (new AspectList()).add(Aspect.ELDRITCH, 16).add(Aspect.TRAVEL, 16).add(Aspect.VOID, 24),
                        EntityHorse.class,
                        new ItemStack[] { new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.EGG),
                                new ItemStack(Items.ENDER_PEARL), new ItemStack(ConfigItems.itemResource, 1, 14) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "horseInfusion",
                        20,
                        2,
                        (new AspectList()).add(Aspect.DEATH, 8).add(Aspect.UNDEAD, 8),
                        EntityHorse.class,
                        new ItemStack[] { new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.ROTTEN_FLESH),
                                new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.ROTTEN_FLESH) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "horseInfusion",
                        21,
                        2,
                        (new AspectList()).add(Aspect.DEATH, 8).add(Aspect.UNDEAD, 8),
                        EntityHorse.class,
                        new ItemStack[] { new ItemStack(Items.BONE), new ItemStack(Items.BONE),
                                new ItemStack(Items.BONE), new ItemStack(Items.BONE) },
                        0));
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "nightmareInfusion",
                        22,
                        8,
                        (new AspectList()).add(Aspect.FIRE, 32).add(Aspect.TRAVEL, 16).add(Aspect.VOID, 16),
                        EntityEndersteed.class,
                        new ItemStack[] { new ItemStack(Items.NETHER_STAR), new ItemStack(Blocks.OBSIDIAN),
                                new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(ConfigBlocks.blockLifter),
                                new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(Blocks.OBSIDIAN) },
                        0));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("quickLimbs", 1);
        tag.setInteger("generic.movementSpeed", 3);
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "infusionVat",
                        tag,
                        3,
                        (new AspectList()).add(Aspect.MOTION, 16).add(Aspect.MECHANISM, 8).add(Aspect.FLIGHT, 4),
                        null,
                        new ItemStack[] { new ItemStack(Items.POTIONITEM, 1, 8226),
                                new ItemStack(ConfigItems.itemResource, 1, 3),
                                new ItemStack(ConfigItems.itemResource, 1, 3) },
                        1));
        tag = new NBTTagCompound();
        tag.setInteger("generic.attackDamage", 2);
        tag.setInteger("thaumClaws", 2);
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "infusionVat",
                        tag,
                        3,
                        (new AspectList()).add(Aspect.WEAPON, 12).add(Aspect.METAL, 8).add(Aspect.MAGIC, 8),
                        null,
                        new ItemStack[] { new ItemStack(ConfigItems.itemSwordThaumium),
                                new ItemStack(ConfigItems.itemSwordThaumium) },
                        2));
        tag = new NBTTagCompound();
        tag.setInteger("awakeBlood", 3);
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "infusionVat",
                        tag,
                        4,
                        (new AspectList()).add(Aspect.HEAL, 8).add(Aspect.LIFE, 16).add(Aspect.MIND, 4),
                        null,
                        new ItemStack[] { new ItemStack(Items.SPECKLED_MELON), new ItemStack(itemSyringeHuman),
                                new ItemStack(Items.GOLDEN_APPLE), new ItemStack(itemSyringeHuman) },
                        3));
        tag = new NBTTagCompound();
        tag.setInteger("diamondSkin", 4);
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "infusionVat",
                        tag,
                        4,
                        (new AspectList()).add(Aspect.ARMOR, 16).add(Aspect.CRYSTAL, 16).add(Aspect.BEAST, 8),
                        null,
                        new ItemStack[] { new ItemStack(Items.LEATHER), new ItemStack(Items.DIAMOND),
                                new ItemStack(Items.DIAMOND) },
                        4));
        tag = new NBTTagCompound();
        tag.setInteger("enderHeart", 5);
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "infusionVat",
                        tag,
                        6,
                        (new AspectList()).add(Aspect.ELDRITCH, 16).add(Aspect.TRAVEL, 16).add(Aspect.FLESH, 16),
                        null,
                        new ItemStack[] { new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.EGG),
                                new ItemStack(Items.EGG) },
                        5));
        tag = new NBTTagCompound();
        tag.setInteger("shockSkin", 6);
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "infusionVat",
                        tag,
                        5,
                        (new AspectList()).add(Aspect.ENERGY, 16).add(Aspect.WEAPON, 12).add(Aspect.WEATHER, 4),
                        null,
                        new ItemStack[] { new ItemStack(Items.QUARTZ), new ItemStack(ConfigItems.itemShard, 1, 0),
                                new ItemStack(Items.QUARTZ), new ItemStack(Items.REDSTONE) },
                        6));
        tag = new NBTTagCompound();
        tag.setInteger("instilledLoyalty", 7);
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "infusionVat",
                        tag,
                        6,
                        (new AspectList()).add(Aspect.MIND, 4).add(Aspect.BEAST, 3).add(Aspect.EXCHANGE, 3),
                        null,
                        new ItemStack[] { new ItemStack(Items.SADDLE), new ItemStack(ConfigItems.itemZombieBrain),
                                new ItemStack(Items.GOLDEN_APPLE) },
                        7));
        tag = new NBTTagCompound();
        tag.setInteger("runicCreature", 8);
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "runicHide",
                        tag,
                        7,
                        (new AspectList()).add(Aspect.ARMOR, 16).add(Aspect.MAGIC, 32).add(Aspect.ENERGY, 16)
                                .add(Aspect.FLESH, 8),
                        null,
                        new ItemStack[] { new ItemStack(Items.DIAMOND), new ItemStack(ConfigItems.itemResource, 1, 6),
                                new ItemStack(ConfigItems.itemResource, 1, 7),
                                new ItemStack(ConfigItems.itemResource, 1, 1), new ItemStack(ConfigItems.itemInkwell) },
                        8));
        tag = new NBTTagCompound();
        tag.setInteger("eldritchFangs", 9);
        tag.setInteger("generic.attackDamage", 1);
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "eldritchFangs",
                        tag,
                        8,
                        (new AspectList()).add(Aspect.ELDRITCH, 8).add(Aspect.WEAPON, 16).add(Aspect.METAL, 16)
                                .add(Aspect.POISON, 32),
                        null,
                        new ItemStack[] { new ItemStack(ConfigItems.itemResource, 1, 16),
                                new ItemStack(ConfigBlocks.blockCustomPlant, 1, 5),
                                new ItemStack(ConfigItems.itemResource, 1, 16),
                                new ItemStack(ConfigBlocks.blockCustomPlant, 1, 5) },
                        9));
        tag = new NBTTagCompound();
        tag.setInteger("portability", 10);
        critterRecipes.add(
                new CreatureInfusionRecipe(
                        "infusionVat",
                        tag,
                        6,
                        (new AspectList()).add(Aspect.VOID, 16).add(Aspect.TRAP, 16),
                        null,
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockJar), new ItemStack(Items.ENDER_PEARL) },
                        10));
        classBanList.add(EntityGhast.class);
        classBanList.add(EntityEnderman.class);
        classBanList.add(EntityWitch.class);
        classBanList.add(EntityBlaze.class);
        classBanList.add(EntityMagmaCube.class);
        classBanList.add(EntityPech.class);
        classBanList.add(ITaintedMob.class);
        classBanList.add(EntityWisp.class);
        selfRecipes.add(
                new SelfInfusionRecipe(
                        "selfInfusion",
                        4,
                        (new AspectList()).add(Aspect.MOTION, 64).add(Aspect.MECHANISM, 32).add(Aspect.FLIGHT, 32),
                        new ItemStack[] { new ItemStack(Items.POTIONITEM, 1, 8226),
                                new ItemStack(ConfigItems.itemResource, 1, 3),
                                new ItemStack(ConfigItems.itemResource, 1, 16),
                                new ItemStack(Items.POTIONITEM, 1, 8226), new ItemStack(ConfigItems.itemResource, 1, 3),
                                new ItemStack(ConfigItems.itemResource, 1, 16) },
                        1));
        selfRecipes.add(
                new SelfInfusionRecipe(
                        "selfInfusion",
                        5,
                        (new AspectList()).add(Aspect.HEAL, 32).add(Aspect.LIFE, 64).add(Aspect.MIND, 32),
                        new ItemStack[] { new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.SPECKLED_MELON),
                                new ItemStack(Items.SPECKLED_MELON), new ItemStack(ConfigItems.itemZombieBrain),
                                new ItemStack(Items.SPECKLED_MELON), new ItemStack(Items.SPECKLED_MELON) },
                        3));
        selfRecipes.add(
                new SelfInfusionRecipe(
                        "selfInfusion",
                        6,
                        (new AspectList()).add(Aspect.ARMOR, 32).add(Aspect.CRYSTAL, 32).add(Aspect.MAN, 16),
                        new ItemStack[] { new ItemStack(Items.LEATHER_HELMET), new ItemStack(Items.DIAMOND),
                                new ItemStack(Items.LEATHER_CHESTPLATE), new ItemStack(Blocks.DIAMOND_BLOCK),
                                new ItemStack(Items.LEATHER_LEGGINGS), new ItemStack(Items.DIAMOND),
                                new ItemStack(Items.LEATHER_BOOTS), new ItemStack(Blocks.DIAMOND_BLOCK) },
                        4));
        selfRecipes.add(
                new SelfInfusionRecipe(
                        "morphicFingers",
                        6,
                        (new AspectList()).add(Aspect.TOOL, 32).add(Aspect.CRAFT, 32).add(Aspect.MAN, 32)
                                .add(Aspect.EXCHANGE, 16),
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockTable, 1, 15),
                                new ItemStack(ConfigItems.itemResource, 1, 16),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(ConfigItems.itemResource, 1, 16) },
                        2));
        selfRecipes.add(
                new SelfInfusionRecipe(
                        "silverHeart",
                        7,
                        (new AspectList()).add(Aspect.ORDER, 32).add(Aspect.HEAL, 32).add(Aspect.EXCHANGE, 16),
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockCustomPlant, 1, 1),
                                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1),
                                new ItemStack(ConfigBlocks.blockCustomPlant, 1, 2),
                                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1) },
                        5));
        selfRecipes.add(
                new SelfInfusionRecipe(
                        "synthSkin",
                        6,
                        (new AspectList()).add(Aspect.PLANT, 32).add(Aspect.HUNGER, 32).add(Aspect.LIGHT, 32)
                                .add(Aspect.MAN, 16),
                        new ItemStack[] { new ItemStack(Blocks.LEAVES, 1, 32767),
                                new ItemStack(Blocks.SAPLING, 1, 32767), new ItemStack(Blocks.RED_FLOWER, 1, 32767),
                                new ItemStack(Blocks.VINE) },
                        6));
        selfRecipes.add(
                new SelfInfusionRecipe(
                        "amphibious",
                        7,
                        (new AspectList()).add(Aspect.WATER, 64).add(Aspect.AIR, 64).add(Aspect.LIFE, 32)
                                .add(Aspect.EXCHANGE, 16),
                        new ItemStack[] { new ItemStack(Items.FISH, 1, 32767), new ItemStack(Items.POTIONITEM, 1, 8269),
                                new ItemStack(Items.FISH, 1, 32767), new ItemStack(Items.POTIONITEM, 1, 8269) },
                        7));
        selfRecipes.add(
                new SelfInfusionRecipe(
                        "warpedTumor",
                        12,
                        (new AspectList()).add(Aspect.TAINT, 64).add(Aspect.ELDRITCH, 48).add(Aspect.TRAP, 32)
                                .add(Aspect.FLESH, 24).add(Aspect.EXCHANGE, 16),
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockTaint, 1, 2),
                                new ItemStack(ConfigItems.itemBathSalts, 1, 0), new ItemStack(Items.NETHER_STAR, 1, 0),
                                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0),
                                new ItemStack(Items.NETHER_STAR, 1, 0),
                                new ItemStack(ConfigItems.itemSanitySoap, 1, 0) },
                        8));
        selfRecipes.add(
                new SelfInfusionRecipe(
                        "spiderClimb",
                        8,
                        (new AspectList()).add(Aspect.BEAST, 32).add(Aspect.MOTION, 48).add(Aspect.SLIME, 32),
                        new ItemStack[] { new ItemStack(Blocks.WEB), new ItemStack(ConfigItems.itemResource, 1, 7),
                                new ItemStack(Blocks.WEB), new ItemStack(Blocks.LADDER), new ItemStack(Blocks.WEB),
                                new ItemStack(ConfigItems.itemResource, 1, 7), new ItemStack(Blocks.WEB),
                                new ItemStack(Blocks.LADDER) },
                        9));
        selfRecipes.add(
                new SelfInfusionRecipe(
                        "chameleonSkin",
                        7,
                        (new AspectList()).add(Aspect.SENSES, 48).add(Aspect.EXCHANGE, 32).add(Aspect.VOID, 32),
                        new ItemStack[] { new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.DYE, 1, 1),
                                new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.DYE, 1, 11),
                                new ItemStack(Items.DYE, 1, 0), new ItemStack(Items.DYE, 1, 15) },
                        10));
        addAspectsToAllTheThings();
        registerDispenserBehavior();
    }

    public static CreatureInfusionRecipe getCreatureInfusion(EntityLivingBase entityContained,
            ArrayList<ItemStack> components, EntityPlayer player) {
        Iterator<CreatureInfusionRecipe> var3 = critterRecipes.iterator();

        CreatureInfusionRecipe recipe;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            recipe = var3.next();
        } while (!recipe.matches(components, entityContained.getClass(), player.world, player));

        return recipe;
    }

    public static SelfInfusionRecipe getSelfInfusion(ArrayList<ItemStack> components, EntityPlayer player) {
        Iterator<SelfInfusionRecipe> var2 = selfRecipes.iterator();

        SelfInfusionRecipe recipe;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            recipe = var2.next();
        } while (!recipe.matches(components, player.world, player));

        return recipe;
    }

    // The five Thaumic Horizons potions are registered by PotionRegistryTH during
    // Forge's RegistryEvent.Register<Potion>. Legacy numeric config values below
    // are retained only for reading old configuration files; they are NOT IDs.

    static void addAspectsToAllTheThings() {
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.ChromaticSheep",
                (new AspectList()).add(Aspect.BEAST, 2).add(Aspect.EARTH, 2).add(Aspect.SENSES, 2)
                        .add(Aspect.EXCHANGE, 2));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.SelfShearingSheep",
                (new AspectList()).add(Aspect.BEAST, 2).add(Aspect.EARTH, 2).add(Aspect.TOOL, 2).add(Aspect.CLOTH, 2));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.GuardianPanther",
                (new AspectList()).add(Aspect.BEAST, 4).add(Aspect.WEAPON, 4).add(Aspect.ENTROPY, 4));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.Familiar",
                (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.MAGIC, 3).add(Aspect.AURA, 3));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.Gravekeeper",
                (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.DEATH, 3).add(Aspect.ORDER, 3));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.ChocolateCow",
                (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.EARTH, 3).add(Aspect.SENSES, 3));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.ScholarChicken",
                (new AspectList()).add(Aspect.BEAST, 2).add(Aspect.FLIGHT, 2).add(Aspect.AIR, 1).add(Aspect.DARKNESS, 2)
                        .add(Aspect.MIND, 2));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.GoldChicken",
                (new AspectList()).add(Aspect.BEAST, 2).add(Aspect.FLIGHT, 2).add(Aspect.AIR, 1).add(Aspect.GREED, 3));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.OrePig",
                (new AspectList()).add(Aspect.BEAST, 2).add(Aspect.EARTH, 2).add(Aspect.METAL, 4));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.TaintEaterPig",
                (new AspectList()).add(Aspect.BEAST, 2).add(Aspect.EARTH, 2).add(Aspect.HEAL, 2).add(Aspect.ORDER, 2));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.Seawolf",
                (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.EARTH, 3).add(Aspect.WATER, 3));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.LunarWolf",
                (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.EARTH, 3).add(Aspect.MAGIC, 3));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.NetherHound",
                (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.EARTH, 3).add(Aspect.FIRE, 3));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.GolemTH",
                (new AspectList()).add(Aspect.AIR, 2).add(Aspect.EARTH, 2).add(Aspect.MAGIC, 2));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.Endersteed",
                (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.ELDRITCH, 4));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.NightmareTH",
                (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.ELDRITCH, 4).add(Aspect.FIRE, 4)
                        .add(Aspect.TRAVEL, 4));
        ThaumcraftApi.registerEntityTag(
                "ThaumicHorizons.Sheeder",
                (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.CLOTH, 4));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemEggIncubated),
                (new AspectList()).add(Aspect.SLIME, 2).add(Aspect.LIFE, 2).add(Aspect.BEAST, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemFocusIllumination, 1, 32767),
                (new AspectList()).add(Aspect.LIGHT, 5).add(Aspect.ENERGY, 5));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemSyringeEmpty),
                (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.METAL, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemSyringeBloodSample),
                (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.METAL, 1).add(Aspect.BEAST, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemSyringeHuman, 1, 32767),
                (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.METAL, 1).add(Aspect.MAN, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemSyringeInjection, 1, 32767),
                (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.METAL, 1).add(Aspect.MAN, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(itemNutrients), (new AspectList()).add(Aspect.HUNGER, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemBarChocolate),
                (new AspectList()).add(Aspect.HUNGER, 2).add(Aspect.LIFE, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemIceCream),
                (new AspectList()).add(Aspect.HUNGER, 3).add(Aspect.LIFE, 1).add(Aspect.COLD, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemBucketChocolate),
                (new AspectList(new ItemStack(Items.BUCKET))).add(Aspect.HUNGER, 2).add(Aspect.LIFE, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemInkEgg),
                (new AspectList()).add(Aspect.SLIME, 2).add(Aspect.DARKNESS, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemGoldEgg),
                (new AspectList()).add(Aspect.SLIME, 2).add(Aspect.GREED, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemAmuletMirror),
                (new AspectList()).add(Aspect.DEATH, 8).add(Aspect.VOID, 6).add(Aspect.GREED, 6).add(Aspect.TRAVEL, 6));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemFocusAnimation),
                (new AspectList()).add(Aspect.SOUL, 8).add(Aspect.MAN, 8).add(Aspect.MOTION, 8).add(Aspect.CRAFT, 8));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemVoidPutty),
                (new AspectList()).add(Aspect.CRAFT, 4).add(Aspect.ELDRITCH, 4));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemCrystalWand),
                (new AspectList()).add(Aspect.CRYSTAL, 6).add(Aspect.MAGIC, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(itemMeat), (new AspectList()).add(Aspect.FLESH, 4));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(itemMeatCooked),
                (new AspectList()).add(Aspect.FLESH, 3).add(Aspect.FIRE, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(itemMeatNugget), (new AspectList()).add(Aspect.FLESH, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockAlchemite),
                (new AspectList()).add(Aspect.ENERGY, 4).add(Aspect.ENTROPY, 4).add(Aspect.EXCHANGE, 2)
                        .add(Aspect.MAGIC, 2).add(Aspect.CRYSTAL, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockVat),
                (new AspectList()).add(Aspect.MECHANISM, 1).add(Aspect.HEAL, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockVatSolid),
                (new AspectList()).add(Aspect.MECHANISM, 1).add(Aspect.HEAL, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockChocolate),
                (new AspectList()).add(Aspect.HUNGER, 5).add(Aspect.LIFE, 5));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockSoulBeacon),
                (new AspectList()).add(Aspect.SOUL, 8).add(Aspect.TRAVEL, 8).add(Aspect.TRAP, 8).add(Aspect.LIGHT, 8));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockBrain),
                (new AspectList()).add(Aspect.MIND, 4).add(Aspect.FLESH, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(blockCloud), (new AspectList()).add(Aspect.WEATHER, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockVoid),
                (new AspectList()).add(Aspect.VOID, 4).add(Aspect.TRAP, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(blockDust), (new AspectList()).add(Aspect.ENTROPY, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockEye),
                (new AspectList()).add(Aspect.SENSES, 2).add(Aspect.BEAST, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(blockBone), (new AspectList()).add(Aspect.DEATH, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(blockFlesh), (new AspectList()).add(Aspect.FLESH, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(blockCrystal), (new AspectList()).add(Aspect.CRYSTAL, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockCrystalDeep),
                (new AspectList()).add(Aspect.CRYSTAL, 2).add(Aspect.LIGHT, 2).add(Aspect.EXCHANGE, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockVortex),
                (new AspectList()).add(Aspect.AURA, 16).add(Aspect.VOID, 16));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockLeviathan),
                (new AspectList()).add(Aspect.ELDRITCH, 8).add(Aspect.DEATH, 4));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockPortal),
                (new AspectList()).add(Aspect.TRAVEL, 4).add(Aspect.VOID, 4));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockSpike),
                (new AspectList()).add(Aspect.WEAPON, 3).add(Aspect.METAL, 3));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockSpikeWood),
                (new AspectList()).add(Aspect.WEAPON, 3).add(Aspect.TREE, 3));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(blockSpikeTooth),
                (new AspectList()).add(Aspect.WEAPON, 3).add(Aspect.BEAST, 3));
    }

    private static void registerDispenserBehavior() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY
                .putObject(Item.getItemFromBlock(blockAlchemite), new BehaviorDefaultDispenseItem() {

                    @Override
                    protected ItemStack dispenseStack(IBlockSource dispenser, ItemStack dispensedItem) {
                        EnumFacing enumfacing = dispenser.getBlockState().getValue(BlockDispenser.FACING);
                        World world = dispenser.getWorld();
                        int x = dispenser.getBlockPos().getX() + enumfacing.getXOffset();
                        int y = dispenser.getBlockPos().getY() + enumfacing.getYOffset();
                        int z = dispenser.getBlockPos().getZ() + enumfacing.getZOffset();

                        EntityAlchemitePrimed primedAlchemite = new EntityAlchemitePrimed(
                                world,
                                x + 0.5F,
                                y + 0.5F,
                                z + 0.5F,
                                null);
                        world.spawnEntity(primedAlchemite);
                        com.kentington.thaumichorizons.common.lib.THSoundBridge.play(world, x + 0.5, y + 0.5, z + 0.5, "game.tnt.primed", 1.0F, 1.0F);

                        dispensedItem.shrink(1);
                        return dispensedItem;
                    }
                });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(itemSyringeInjection, new BehaviorDefaultDispenseItem() {

            @Override
            public ItemStack dispenseStack(IBlockSource dispenser, ItemStack stack) {
                if (!(stack.getItem() instanceof ItemSyringeInjection)) {
                    return super.dispenseStack(dispenser, stack);
                }

                World world = dispenser.getWorld();
                EnumFacing facing = dispenser.getBlockState().getValue(BlockDispenser.FACING);

                double spawnX = dispenser.getX() + facing.getXOffset();
                double spawnY = dispenser.getY() + facing.getYOffset();
                double spawnZ = dispenser.getZ() + facing.getZOffset();

                IProjectile projectile = createProjectile(
                        (ItemSyringeInjection) stack.getItem(),
                        stack,
                        world,
                        spawnX,
                        spawnY,
                        spawnZ);

                projectile.shoot(
                        facing.getXOffset(),
                        facing.getYOffset() + 0.1F,
                        facing.getZOffset(),
                        1.1F, // velocity
                        6F // inaccuracy
                );

                world.spawnEntity((Entity) projectile);

                stack.shrink(1);
                return stack;
            }

            private IProjectile createProjectile(ItemSyringeInjection item, ItemStack stack, World world, double x,
                    double y, double z) {
                if (stack.getItemDamage() != 0) {
                    return new EntityBlastPhial(world, x, y, z, stack);
                } else {
                    return new EntitySyringe(world, x, y, z, stack.getTagCompound());
                }
            }

            @Override
            protected void playDispenseSound(IBlockSource source) {
                source.getWorld().playEvent(1002, new net.minecraft.util.math.BlockPos(source.getBlockPos().getX(), source.getBlockPos().getY(), source.getBlockPos().getZ()), 0);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(itemEggIncubated, new BehaviorProjectileDispense() {

            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stack) {
                return new EntityEggIncubated(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(
                itemBoatGreatwood,
                new BehaviorDispenseBoat(Collections.singleton(Material.WATER), EntityBoatGreatwood::new));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(
                itemBoatThaumium,
                new BehaviorDispenseBoat(Sets.newHashSet(Material.WATER, Material.LAVA), EntityBoatThaumium::new));
    }


    private void intitializeConfig(File suggestedConfigurationFile) {
        config = new Configuration(suggestedConfigurationFile);
        config.load();
        this.syncConfig();
    }

    private void syncConfig() {
        Property dimPocket = config.get("general", "pocket_plane_dim", 69);
        dimensionPocketId = dimPocket.getInt();
        Property alternateBell = config.get("general", "alternateGolemBell", true);
        alternateBell.setComment("Enable alternate golemancer\'s bell recipe (in case of mod conflict).");
        alternateBell.setRequiresMcRestart(true);
        useAlternateBell = alternateBell.getBoolean();
        Property enablePocketPlane = config.get("general", "enablePocketPlane", true);
        enablePocketPlane.setComment("Enable pocket plane content (currently incomplete - many aspects will not generate the cool stuff they are supposed to). World backups are highly suggested.");
        enablePocketPlane.setRequiresMcRestart(true);
        enablePocket = enablePocketPlane.getBoolean();
        Property warpedTumorValueProperty = config.get("general", "warpedTumorValue", 50);
        warpedTumorValueProperty.setComment("Points of warp held by warped tumor.");
        warpedTumorValueProperty.setRequiresMcRestart(true);
        warpedTumorValue = warpedTumorValueProperty.getInt();

        Property visboostPotionProperty = config.get("potions", "visboostPotionID", 24);
        visboostPotionProperty.setComment("Potion ID for the VisBoost Potion");
        visboostPotionProperty.setRequiresMcRestart(true);
        potionVisBoostID = visboostPotionProperty.getInt();

        Property visregenPotionProperty = config.get("potions", "visregenPotionID", 25);
        visregenPotionProperty.setComment("Potion ID for the VisRegen Potion");
        visregenPotionProperty.setRequiresMcRestart(true);
        potionVisRegenID = visregenPotionProperty.getInt();

        Property vacuumPotionProperty = config.get("potions", "vacuumPotionID", 26);
        vacuumPotionProperty.setComment("Potion ID for the Vacuum");
        vacuumPotionProperty.setRequiresMcRestart(true);
        potionVacuumID = vacuumPotionProperty.getInt();

        Property shockPotionProperty = config.get("potions", "shockPotionID", 27);
        shockPotionProperty.setComment("Potion ID for the Shock Potion");
        shockPotionProperty.setRequiresMcRestart(true);
        potionShockID = shockPotionProperty.getInt();

        Property synthesisPotionProperty = config.get("potions", "synthesisPotionID", 28);
        synthesisPotionProperty.setComment("Potion ID for the Sythesis Potion");
        synthesisPotionProperty.setRequiresMcRestart(true);
        potionSynthesisID = synthesisPotionProperty.getInt();

        config.save();
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals("ThaumicHorizons")) {
            this.syncConfig();
        }
    }

    static {
        portal = new MaterialPortalTH(MapColor.AIR);
        tabTH = new CreativeTabTH(CreativeTabs.getNextID(), "thaumichorizons");
        incarnationItems = new HashMap<>();
        critterRecipes = new ArrayList<>();
        selfRecipes = new ArrayList<>();
        classBanList = new ArrayList<>();
    }
}
