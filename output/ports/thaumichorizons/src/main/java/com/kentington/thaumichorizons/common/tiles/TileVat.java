//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import com.kentington.thaumichorizons.common.lib.THSoundBridge;
import net.minecraft.util.math.BlockPos;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.IEntityInfusedStats;
import com.kentington.thaumichorizons.common.lib.CreatureInfusionRecipe;
import com.kentington.thaumichorizons.common.lib.EntityInfusionProperties;
import com.kentington.thaumichorizons.common.lib.SelfInfusionRecipe;
import com.kentington.thaumichorizons.common.lib.networking.PacketFXEssentiaBubble;
import com.kentington.thaumichorizons.common.lib.networking.PacketFXInfusionDone;
import com.kentington.thaumichorizons.common.lib.networking.PacketHandler;
import com.kentington.thaumichorizons.common.lib.networking.PacketInfusionFX;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.lib.network.fx.PacketFXBlockZap;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TilePedestal;

public class TileVat extends TileThaumcraft implements IAspectContainer, IEssentiaTransport, ISidedInventory, ITickable {

    // Timers and progress values (in ticks)
    private static final int CLONE_PROGRESS = 800;
    private static final int COUNT_DELAY = 10;
    private static final int HEAL_ASPECT_COOLDOWN = 40;
    private static final int LIFE_ASPECT_COOLDOWN = 50;
    private static final int EFFIGY_PROGRESS = 80;
    private static final int BLOOD_SAMPLE_INITIAL_PROGRESS = 40;
    private static final int INFUSER_SOUND_INTERVAL = 65;
    private static final int CRAFTCOUNT_WIND_DOWN_MAX = 50;
    private static final int INSTABILITY_EFFECT_CHANCE = 200;
    private static final int INSTABILITY_CRAFT_CHANCE = 500;
    private static final int INSTABILITY_INCREASE_CHANCE_BASE = 100;
    private static final int MAX_INSTABILITY = 25;

    // Healing amounts
    private static final float STRONG_HEAL_AMOUNT = 8.0f;
    private static final float WEAK_HEAL_AMOUNT = 4.0f;

    // Essentia costs
    private static final int EFFIGY_ESSENTIA_AMOUNT = 8;
    private static final int BLOOD_SAMPLE_LIFE_COST = 4;
    private static final int VIS_DRAIN_AMOUNT = 100;

    // Instability damage divisor
    private static final float INSTABILITY_DAMAGE_DIVISOR = 10.0f;

    // Pedestal search radius
    private static final int PEDESTAL_SEARCH_HORIZONTAL = 12;
    private static final int PEDESTAL_SEARCH_Y_DOWN = 5;
    private static final int PEDESTAL_SEARCH_Y_UP = 10;
    private static final int PEDESTAL_SYMMETRY_RANGE = 8;

    // Self-infusion slots
    private static final int SELF_INFUSION_SLOTS = 12;
    private static final float SELF_INFUSION_INITIAL_HEALTH = 20.0f;

    // Particle count for infuser effects
    private static final int INFUSER_PARTICLE_COUNT = 25;

    public int mode;
    AspectList myEssentia;
    AspectList essentiaDemanded;
    Aspect currentlySucking;
    public ItemStack sample;
    public ItemStack nutrients;
    public int progress;
    private EntityLivingBase entityContained;
    public int[] selfInfusions;
    public float selfInfusionHealth;
    private final ArrayList<BlockPos> pedestals;
    private final int dangerCount;
    public boolean checkSurroundings;
    public int symmetry;
    public int instability;
    private ArrayList<ItemStack> recipeIngredients;
    private Object recipeOutput;
    private String recipePlayer;
    private String recipeOutputLabel;
    private int recipeInstability;
    private final int recipeXP;
    public int recipeType;
    int itemCount;
    public int count;
    public int craftCount;
    public float startUp;
    ArrayList<ItemStack> ingredients;
    public HashMap<String, SourceFX> sourceFX;
    private NBTTagCompound entityNBTObj = null;
    private Boolean firstTick = true;
    private boolean disassembling;

    public TileVat() {
        this.mode = 0;
        this.myEssentia = new AspectList();
        this.essentiaDemanded = new AspectList();
        this.currentlySucking = null;
        this.sample = ItemStack.EMPTY;
        this.nutrients = ItemStack.EMPTY;
        this.entityContained = null;
        this.selfInfusions = new int[SELF_INFUSION_SLOTS];
        this.selfInfusionHealth = SELF_INFUSION_INITIAL_HEALTH;
        this.pedestals = new ArrayList<>();
        this.dangerCount = 0;
        this.checkSurroundings = true;
        this.symmetry = 0;
        this.instability = 0;
        this.recipeIngredients = null;
        this.recipeOutput = null;
        this.recipePlayer = null;
        this.recipeOutputLabel = "";
        this.recipeInstability = 0;
        this.recipeXP = 0;
        this.recipeType = 0;
        this.itemCount = 0;
        this.count = 0;
        this.craftCount = 0;
        this.ingredients = new ArrayList<>();
        this.sourceFX = new HashMap<>();
    }

    public boolean activate(final EntityPlayer player, final boolean direct) {
        final ItemStack possibleJar = player.getHeldItemMainhand();
        if (possibleJar != null && Block.getBlockFromItem(possibleJar.getItem()) == ThaumicHorizons.blockJar
                && possibleJar.hasTagCompound()
                && !possibleJar.getTagCompound().getBoolean("isSoul")) {
            if (this.mode == 0 && this.getEntityContained() == null
                    && player.inventory.addItemStackToInventory(new ItemStack(ConfigBlocks.blockJar))) {
                this.setEntityContained(
                        (EntityLivingBase) EntityList.createEntityFromNBT(possibleJar.getTagCompound(), this.world));
                possibleJar.shrink(1);
                this.markDirty();
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                return true;
            }
        } else if (possibleJar != null && Block.getBlockFromItem(possibleJar.getItem()) == ConfigBlocks.blockJar
                && possibleJar.getItemDamage() == 0
                && this.getEntityContained() != null
                && !(this.getEntityContained() instanceof EntityPlayer)) {
                    if (this.mode == 0) {
                        return this.jarCritter(possibleJar, player);
                    }
                } else
            if (this.mode == 4 && possibleJar != null
                    && Block.getBlockFromItem(possibleJar.getItem()) == ThaumicHorizons.blockJar
                    && possibleJar.hasTagCompound()
                    && possibleJar.getTagCompound().getBoolean("isSoul")) {
                        if (this.selfInfusions[1] == 0
                                && player.inventory.addItemStackToInventory(new ItemStack(ConfigBlocks.blockJar))) {
                            THSoundBridge.play(this.world, 
                                    this.pos.getX() + 0.5,
                                    this.pos.getY() + 0.5,
                                    this.pos.getZ() + 0.5,
                                    "thaumcraft:whispers",
                                    1.0f,
                                    this.world.rand.nextFloat());
                            Thaumcraft.proxy.blockSparkle(
                                    this.world,
                                    this.pos.getX(),
                                    this.pos.getY() - 1,
                                    this.pos.getZ(),
                                    16777215,
                                    20);
                            Thaumcraft.proxy.blockSparkle(
                                    this.world,
                                    this.pos.getX(),
                                    this.pos.getY() - 2,
                                    this.pos.getZ(),
                                    16777215,
                                    20);
                            final EntityVillager villager = new EntityVillager(this.world);
                            com.kentington.thaumichorizons.common.lib.VillagerSoulData.restore(villager, possibleJar.getTagCompound());
                            this.setEntityContained(villager);
                            this.mode = 0;
                            this.markDirty();
                            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                            this.selfInfusions = new int[SELF_INFUSION_SLOTS];
                            possibleJar.shrink(1);
                        }
                    } else {
                        if (this.mode == 0 && direct && this.getEntityContained() == null) {
                            player.setPositionAndUpdate(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
                            this.setEntityContained(player);
                            this.markDirty();
                            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                            return true;
                        }
                        if (this.mode == 0 && this.getEntityContained() == player) {
                            if (this.world.getBlockState(this.pos.up()).getBlock()
                                    == ThaumicHorizons.blockSoulBeacon) {
                                player.setPositionAndUpdate(this.pos.getX() + 0.5, this.pos.getY() + 2.0, this.pos.getZ() + 0.5);
                            } else {
                                player.setPositionAndUpdate(this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5);
                            }
                            this.setEntityContained(null);
                            this.markDirty();
                            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                            return true;
                        }
                        player.openGui(
                                ThaumicHorizons.instance,
                                7,
                                this.world,
                                this.pos.getX(),
                                this.pos.getY(),
                                this.pos.getZ());
                        return true;
                    }
        return false;
    }

    private void loadContainedEntity(NBTTagCompound EntityNBT) {
        if (EntityNBT.getString("id") != null) {
            if (EntityNBT.getString("id").equals("PLAYER")) {
                this.setEntityContained(this.world.getPlayerEntityByName(EntityNBT.getString("playerName")));
            } else if (EntityNBT.hasKey("id")) {
                this.setEntityContained((EntityLivingBase) EntityList.createEntityFromNBT(EntityNBT, this.world));
            }
        }
    }

    public void update() {
        if (this.world == null) return;
        if (this.firstTick && this.entityNBTObj != null) {
            loadContainedEntity(entityNBTObj);
            this.firstTick = false;
        }
        if (this.getEntityContained() != null && this.getEntityContained().isBurning()) {
            this.getEntityContained().extinguish();
        }
        if (this.world.isRemote) {
            if (this.mode == 2) {
                this.doEffects();
            } else if (this.mode != 2 && this.startUp > 0.0f) {
                if (this.startUp > 0.0f) {
                    this.startUp -= this.startUp / 10.0f;
                }
                if (this.startUp < 0.001) {
                    this.startUp = 0.0f;
                }
            }
            if (this.mode == 1) {
                Thaumcraft.proxy.blockSparkle(this.world, this.pos.getX(), this.pos.getY() - 1, this.pos.getZ(), 14184241, 1);
                Thaumcraft.proxy.blockSparkle(this.world, this.pos.getX(), this.pos.getY() - 2, this.pos.getZ(), 14184241, 1);
            }
            return;
        }
        if (this.mode == 0) {
            this.essentiaDemanded = new AspectList();
            if (this.getEntityContained() != null) {
                if (this.getEntityContained().getHealth() < this.getEntityContained().getMaxHealth()) {
                    if (this.getEntityContained().getCreatureAttribute() != EnumCreatureAttribute.UNDEAD) {
                        if (this.myEssentia.getAmount(Aspect.HEAL) > 0 && this.progress <= 0) {
                            this.getEntityContained().heal(STRONG_HEAL_AMOUNT);
                            this.myEssentia.remove(Aspect.HEAL, 1);
                            this.markDirty();
                            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                            this.progress += HEAL_ASPECT_COOLDOWN;
                        }
                        if (this.getEntityContained().getHealth() < this.getEntityContained().getMaxHealth()
                                && this.essentiaDemanded.getAmount(Aspect.HEAL) < 1) {
                            this.essentiaDemanded.add(Aspect.HEAL, 1);
                        }
                        if (this.myEssentia.getAmount(Aspect.LIFE) > 0 && this.progress <= 0) {
                            this.getEntityContained().heal(WEAK_HEAL_AMOUNT);
                            this.myEssentia.remove(Aspect.LIFE, 1);
                            this.markDirty();
                            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                            this.progress += LIFE_ASPECT_COOLDOWN;
                        }
                        if (this.getEntityContained().getHealth() < this.getEntityContained().getMaxHealth()
                                && this.essentiaDemanded.getAmount(Aspect.LIFE) < 1) {
                            this.essentiaDemanded.add(Aspect.LIFE, 1);
                        }
                    } else {
                        if (this.myEssentia.getAmount(Aspect.UNDEAD) > 0 && this.progress <= 0) {
                            this.getEntityContained().heal(STRONG_HEAL_AMOUNT);
                            this.myEssentia.remove(Aspect.UNDEAD, 1);
                            this.markDirty();
                            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                            this.progress += HEAL_ASPECT_COOLDOWN;
                        }
                        if (this.getEntityContained().getHealth() < this.getEntityContained().getMaxHealth()
                                && this.essentiaDemanded.getAmount(Aspect.UNDEAD) < 1) {
                            this.essentiaDemanded.add(Aspect.UNDEAD, 1);
                        }
                        if (this.myEssentia.getAmount(Aspect.DEATH) > 0 && this.progress <= 0) {
                            this.getEntityContained().heal(WEAK_HEAL_AMOUNT);
                            this.myEssentia.remove(Aspect.DEATH, 1);
                            this.markDirty();
                            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                            this.progress += LIFE_ASPECT_COOLDOWN;
                        }
                        if (this.getEntityContained().getHealth() < this.getEntityContained().getMaxHealth()
                                && this.essentiaDemanded.getAmount(Aspect.DEATH) < 1) {
                            this.essentiaDemanded.add(Aspect.DEATH, 1);
                        }
                    }
                }
                if (this.hasNegativeEffect(this.getEntityContained())) {
                    if (this.getEntityContained().getCreatureAttribute() != EnumCreatureAttribute.UNDEAD) {
                        if (this.myEssentia.getAmount(Aspect.HEAL) > 0 && this.progress <= 0) {
                            this.removeNegativeEffects(this.getEntityContained());
                            this.myEssentia.remove(Aspect.HEAL, 1);
                            this.markDirty();
                            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                            this.progress += 50;
                        } else if (this.essentiaDemanded.getAmount(Aspect.HEAL) < 1) {
                            this.essentiaDemanded.add(Aspect.HEAL, 1);
                        }
                    } else if (this.myEssentia.getAmount(Aspect.UNDEAD) > 0 && this.progress <= 0) {
                        this.removeNegativeEffects(this.getEntityContained());
                        this.myEssentia.remove(Aspect.UNDEAD, 1);
                        this.markDirty();
                        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                        this.progress += 50;
                    } else if (this.essentiaDemanded.getAmount(Aspect.UNDEAD) < 1) {
                        this.essentiaDemanded.add(Aspect.UNDEAD, 1);
                    }
                }
                if (this.getEntityContained() instanceof EntityPlayer
                        && ((EntityPlayer) this.getEntityContained()).getFoodStats().needFood()) {
                    if (this.myEssentia.getAmount(Aspect.HUNGER) > 0 && this.progress <= 0) {
                        ((EntityPlayer) this.getEntityContained()).getFoodStats().addStats(4, 2.0f);
                        this.markDirty();
                        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                        this.progress += 50;
                    }
                    if (((EntityPlayer) this.getEntityContained()).getFoodStats().needFood()
                            && this.essentiaDemanded.getAmount(Aspect.HUNGER) < 1) {
                        this.essentiaDemanded.add(Aspect.HUNGER, 1);
                    }
                }
            } else if ((!this.sample.isEmpty()) && this.sample.getItem() == ThaumicHorizons.itemCorpseEffigy) {
                this.mode = 3;
                this.essentiaDemanded = new AspectList().add(Aspect.LIFE, EFFIGY_ESSENTIA_AMOUNT)
                        .add(Aspect.HEAL, EFFIGY_ESSENTIA_AMOUNT);
                this.progress = EFFIGY_PROGRESS;
                this.markDirty();
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
            } else if ((!this.sample.isEmpty()) && (!this.nutrients.isEmpty())) {
                this.mode = 1;
                this.essentiaDemanded = new AspectList().add(Aspect.LIFE, BLOOD_SAMPLE_LIFE_COST);
                if (this.sample.getItem() == ThaumicHorizons.itemSyringeBloodSample && this.sample.hasTagCompound()
                        && this.sample.getTagCompound().getCompoundTag("critter") != null
                        && this.sample.getTagCompound().getCompoundTag("critter").getCompoundTag("ForgeData") != null) {
                    final NBTTagCompound tlist = this.sample.getTagCompound().getCompoundTag("critter")
                            .getCompoundTag("CreatureInfusion").getCompoundTag("InfusionCosts");
                    if (tlist != null && tlist.hasKey("Aspects")) {
                        final NBTTagList aspex = tlist.getTagList("Aspects", 10);
                        for (int j = 0; j < aspex.tagCount(); ++j) {
                            final NBTTagCompound rs = aspex.getCompoundTagAt(j);
                            if (rs.hasKey("key")) {
                                this.essentiaDemanded
                                        .add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
                            }
                        }
                    }
                }
                this.progress = BLOOD_SAMPLE_INITIAL_PROGRESS;
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                this.markDirty();
            }
        } else if (this.mode == 1) {
            if (this.sample.isEmpty() && this.getEntityContained() == null) {
                this.progress = 0;
                this.mode = 0;
                this.essentiaDemanded = new AspectList();
                this.myEssentia = new AspectList();
                return;
            }
            if (this.getEntityContained() == null && this.myEssentia.getAmount(Aspect.LIFE) >= 4) {
                if (this.sample.getItem() == ThaumicHorizons.itemSyringeBloodSample) {
                    this.setEntityContained(
                            (EntityLivingBase) EntityList.createEntityFromNBT(
                                    this.sample.getTagCompound().getCompoundTag("critter"),
                                    this.world));
                    if (this.getEntityContained() instanceof EntityTameable) {
                        ((EntityTameable) this.getEntityContained()).setTamed(false);
                    }
                } else {
                    this.setEntityContained(
                            (EntityLivingBase) EntityList.createEntityByID(
                                    ThaumicHorizons.incarnationItems.get(this.sample.getItem()),
                                    this.world));
                }
                final ItemStack sample = this.sample;
                sample.shrink(1);
                if (this.sample.isEmpty()) {
                    this.sample = ItemStack.EMPTY;
                }
                final ItemStack nutrients = this.nutrients;
                nutrients.shrink(1);
                if (this.nutrients.isEmpty()) {
                    this.nutrients = ItemStack.EMPTY;
                }
                this.progress = CLONE_PROGRESS;
                this.essentiaDemanded = new AspectList();
                this.myEssentia = new AspectList();
                if (this.getEntityContained() == null) {
                    this.progress = 0;
                    this.mode = 0;
                    this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                    this.markDirty();
                    return;
                }
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                this.markDirty();
            } else if (this.progress <= 0) {
                this.mode = 0;
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                this.markDirty();
            }
        } else if (this.mode == 2) {
            ++this.count;
            if (this.checkSurroundings) {
                this.checkSurroundings = false;
                this.getSurroundings();
            } else if (this.count % COUNT_DELAY == 0) {
                this.craftCycle();
                this.markDirty();
            }
        } else if (this.mode == 3) {
            if (this.sample.isEmpty() || this.sample.getItem() != ThaumicHorizons.itemCorpseEffigy) {
                this.progress = 0;
                this.mode = 0;
                this.essentiaDemanded = new AspectList();
                this.myEssentia = new AspectList();
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                this.markDirty();
                return;
            }
            if (this.progress <= 0 && this.myEssentia.getAmount(Aspect.LIFE) >= 8
                    && this.myEssentia.getAmount(Aspect.HEAL) >= 8) {
                THSoundBridge.play(this.world, 
                        this.pos.getX() + 0.5,
                        this.pos.getY() + 0.5,
                        this.pos.getZ() + 0.5,
                        "thaumcraft:wand",
                        1.0f,
                        this.world.rand.nextFloat());
                Thaumcraft.proxy.blockSparkle(this.world, this.pos.getX(), this.pos.getY() - 2, this.pos.getZ(), 16720418, 20);
                Thaumcraft.proxy.blockSparkle(this.world, this.pos.getX(), this.pos.getY() - 1, this.pos.getZ(), 16720418, 20);
                this.mode = 4;
                this.selfInfusionHealth = SELF_INFUSION_INITIAL_HEALTH;
                this.sample = ItemStack.EMPTY;
                this.essentiaDemanded = new AspectList();
                this.myEssentia = new AspectList();
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                this.markDirty();
            }
        }
        if (this.mode != 2 && this.needsEssentia()) {
            this.tryDrawAllEssentia();
        }
        if (this.progress > 0) {
            --this.progress;
            this.markDirty();
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        }
    }

    boolean needsEssentia() {
        this.currentlySucking = null;
        if (this.progress > 0) {
            return false;
        }
        for (final Aspect asp : this.essentiaDemanded.getAspects()) {
            if (this.myEssentia.getAmount(asp) < this.essentiaDemanded.getAmount(asp)) {
                this.currentlySucking = asp;
                break;
            }
        }
        return this.currentlySucking != null;
    }

    boolean tryDrawAllEssentia() {
        boolean drew = false;
        TileEntity conn = this.world.getTileEntity(new BlockPos(this.pos.getX() - 1, this.pos.getY() - 3, this.pos.getZ()));
        if (conn instanceof TileVatConnector) {
            drew |= this.tryDrawEssentia((TileVatConnector) conn);
        }
        conn = this.world.getTileEntity(new BlockPos(this.pos.getX() + 1, this.pos.getY() - 3, this.pos.getZ()));
        if (conn instanceof TileVatConnector) {
            drew |= this.tryDrawEssentia((TileVatConnector) conn);
        }
        conn = this.world.getTileEntity(new BlockPos(this.pos.getX(), this.pos.getY() - 3, this.pos.getZ() - 1));
        if (conn instanceof TileVatConnector) {
            drew |= this.tryDrawEssentia((TileVatConnector) conn);
        }
        conn = this.world.getTileEntity(new BlockPos(this.pos.getX(), this.pos.getY() - 3, this.pos.getZ() + 1));
        if (conn instanceof TileVatConnector) {
            drew |= this.tryDrawEssentia((TileVatConnector) conn);
        }
        return drew;
    }

    boolean tryDrawEssentia(final TileVatConnector conn) {
        TileEntity te = null;
        IEssentiaTransport ic = null;
        for (final EnumFacing dir : EnumFacing.values()) {
            te = ThaumcraftApiHelper.getConnectableTile(this.world, conn.getPos().getX(), conn.getPos().getY(), conn.getPos().getZ(), dir);
            if (te != null) {
                ic = (IEssentiaTransport) te;
                if (ic.getEssentiaAmount(dir.getOpposite()) > 0
                        && ic.getSuctionAmount(dir.getOpposite()) < this.getSuctionAmount(null)
                        && this.getSuctionAmount(null) >= ic.getMinimumSuction()) {
                    for (final Aspect asp : this.essentiaDemanded.getAspects()) {
                        if (this.mode == 2 || this.myEssentia.getAmount(asp) < this.essentiaDemanded.getAmount(asp)) {
                            final int ess = ic.takeEssentia(asp, 1, dir.getOpposite());
                            if (ess > 0) {
                                this.addToContainer(asp, ess);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean hasNegativeEffect(final EntityLivingBase ent) {
        return ent.getActivePotionEffect(net.minecraft.init.MobEffects.POISON) != null || ent.getActivePotionEffect(net.minecraft.init.MobEffects.BLINDNESS) != null
                || ent.getActivePotionEffect(net.minecraft.init.MobEffects.HUNGER) != null
                || ent.getActivePotionEffect(net.minecraft.init.MobEffects.WEAKNESS) != null
                || ent.getActivePotionEffect(net.minecraft.init.MobEffects.WITHER) != null
                || ent.getActivePotionEffect(net.minecraft.init.MobEffects.NAUSEA) != null
                || ent.getActivePotionEffect(net.minecraft.init.MobEffects.MINING_FATIGUE) != null
                || ent.getActivePotionEffect(net.minecraft.init.MobEffects.SLOWNESS) != null
                || ent.getActivePotionEffect(Config.potionBlurredVision) != null
                || ent.getActivePotionEffect(Config.potionInfectiousVisExhaust) != null
                || ent.getActivePotionEffect(Config.potionFluxTaint) != null
                || ent.getActivePotionEffect(Config.potionThaumarhia) != null
                || ent.getActivePotionEffect(Config.potionVisExhaust) != null;
    }

    void removeNegativeEffects(final EntityLivingBase ent) {
        ent.removePotionEffect(Config.potionBlurredVision);
        ent.removePotionEffect(Config.potionInfectiousVisExhaust);
        ent.removePotionEffect(Config.potionFluxTaint);
        ent.removePotionEffect(Config.potionThaumarhia);
        ent.removePotionEffect(Config.potionVisExhaust);
        ent.removePotionEffect(net.minecraft.init.MobEffects.BLINDNESS);
        ent.removePotionEffect(net.minecraft.init.MobEffects.NAUSEA);
        ent.removePotionEffect(net.minecraft.init.MobEffects.MINING_FATIGUE);
        ent.removePotionEffect(net.minecraft.init.MobEffects.HUNGER);
        ent.removePotionEffect(net.minecraft.init.MobEffects.SLOWNESS);
        ent.removePotionEffect(net.minecraft.init.MobEffects.HUNGER);
        ent.removePotionEffect(net.minecraft.init.MobEffects.POISON);
        ent.removePotionEffect(net.minecraft.init.MobEffects.WEAKNESS);
        ent.removePotionEffect(net.minecraft.init.MobEffects.WITHER);
    }

    public boolean jarCritter(final ItemStack possibleJar, final EntityPlayer player) {
        final ItemStack jar = new ItemStack(ThaumicHorizons.blockJar);
        final NBTTagCompound entityData = new NBTTagCompound();
        entityData.setString("id", String.valueOf(EntityList.getKey(this.getEntityContained())));
        this.getEntityContained().writeToNBT(entityData);
        jar.setTagCompound(entityData);
        jar.getTagCompound().setString("jarredCritterName", this.getEntityContained().getName());
        jar.getTagCompound().setBoolean("isSoul", false);
        if (player.inventory.addItemStackToInventory(jar)) {
            possibleJar.shrink(1);
            this.setEntityContained(null);
            this.markDirty();
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
            return true;
        }
        return false;
    }

    public EntityLivingBase getEntity() {
        return this.getEntityContained();
    }

    public void startInfusion(final EntityPlayer player) {
        this.getSurroundings();
        final ArrayList<ItemStack> components = new ArrayList<>();
        for (final BlockPos cc : this.pedestals) {
            final TileEntity te = this.world.getTileEntity(new BlockPos(cc.getX(), cc.getY(), cc.getZ()));
            if (te instanceof TilePedestal) { TilePedestal ped = (TilePedestal) te;
                if (ped.getStackInSlot(0).isEmpty()) {
                    continue;
                }
                components.add(ped.getStackInSlot(0).copy());
            }
        }
        if (components.size() == 0) {
            return;
        }
        if (this.mode != 4) {
            final CreatureInfusionRecipe recipe = ThaumicHorizons
                    .getCreatureInfusion(this.getEntityContained(), components, player);
            if (recipe == null || (recipe.getID(null) != 0
                    && ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.getEntityContained()))
                            .hasInfusion(recipe.getID(null)))) {
                return;
            }
            if (recipe.getRecipeOutput() instanceof NBTTagCompound
                    && ((NBTTagCompound) recipe.getRecipeOutput()).getInteger("instilledLoyalty") != 0
                    && ((EntityLiving) this.entityContained).tasks.taskEntries.size() == 0) {
                return;
            }
            this.recipeType = 0;
            this.recipeIngredients = new ArrayList<>();
            for (final ItemStack ing : recipe.getComponents()) {
                this.recipeIngredients.add(ing.copy());
            }
            // Resolve the recipe output exactly once: array output may be synthesized
            // per call and instanceof binding in the GTNH source kept that object.
            final Object computedRecipeOutput = recipe.getRecipeOutput(this.getEntityContained().getClass());
            if (computedRecipeOutput instanceof Object[]) {
                final Object[] obj = (Object[]) computedRecipeOutput;
                this.recipeOutputLabel = (String) obj[0];
                this.recipeOutput = obj[1];
            } else {
                this.recipeOutput = computedRecipeOutput;
            }
            this.recipeInstability = recipe.getInstability(this.getEntityContained().getClass());
            this.essentiaDemanded = recipe.getAspects(this.getEntityContained().getClass()).copy();
            this.myEssentia = recipe.getAspects(this.getEntityContained().getClass()).copy();
        } else {
            final SelfInfusionRecipe recipe2 = ThaumicHorizons.getSelfInfusion(components, player);
            if (recipe2 == null) {
                return;
            }
            for (int selfInfusion : this.selfInfusions) {
                if (selfInfusion == recipe2.getID()) {
                    return;
                }
            }
            this.recipeType = 1;
            this.recipeIngredients = new ArrayList<>();
            for (final ItemStack ing : recipe2.getComponents()) {
                this.recipeIngredients.add(ing.copy());
            }
            this.recipeOutputLabel = "";
            this.recipeOutput = recipe2.getID();
            this.recipeInstability = recipe2.getInstability();
            this.myEssentia = recipe2.getAspects().copy();
            this.essentiaDemanded = recipe2.getAspects().copy();
        }
        this.recipePlayer = player.getName();
        this.instability = this.symmetry + this.recipeInstability;
        this.mode = 2;
        THSoundBridge.play(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), "thaumcraft:craftstart", 0.5f, 1.0f);
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        this.markDirty();
    }

    public boolean validLocation() {
        return true;
    }

    private void getSurroundings() {
        final ArrayList<BlockPos> stuff = new ArrayList<>();
        this.pedestals.clear();
        for (int xx = -PEDESTAL_SEARCH_HORIZONTAL; xx <= PEDESTAL_SEARCH_HORIZONTAL; ++xx) {
            for (int zz = -PEDESTAL_SEARCH_HORIZONTAL; zz <= PEDESTAL_SEARCH_HORIZONTAL; ++zz) {
                boolean skip = false;
                for (int yy = -PEDESTAL_SEARCH_Y_DOWN; yy <= PEDESTAL_SEARCH_Y_UP; ++yy) {
                    if (xx != 0 || zz != 0) {
                        final int x = this.pos.getX() + xx;
                        final int y = this.pos.getY() - yy;
                        final int z = this.pos.getZ() + zz;
                        final TileEntity te = this.world.getTileEntity(new BlockPos(x, y, z));
                        if (!skip && yy > 0
                                && Math.abs(xx) <= PEDESTAL_SYMMETRY_RANGE
                                && Math.abs(zz) <= PEDESTAL_SYMMETRY_RANGE
                                && te instanceof TilePedestal) {
                            this.pedestals.add(new BlockPos(x, y, z));
                            skip = true;
                        } else {
                            final Block bi = this.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                            if (bi == Blocks.SKULL || (bi instanceof IInfusionStabiliser
                                    && ((IInfusionStabiliser) bi).canStabaliseInfusion(this.getWorld(), x, y, z))) {
                                stuff.add(new BlockPos(x, y, z));
                            }
                        }
                    }
                }
            }
        }
        this.symmetry = 0;
        for (final BlockPos cc : this.pedestals) {
            boolean items = false;
            final int x2 = this.pos.getX() - cc.getX();
            final int z2 = this.pos.getZ() - cc.getZ();
            TileEntity te2 = this.world.getTileEntity(new BlockPos(cc.getX(), cc.getY(), cc.getZ()));
            if (te2 instanceof TilePedestal) {
                this.symmetry += 2;
                if (!((TilePedestal) te2).getStackInSlot(0).isEmpty()) {
                    ++this.symmetry;
                    items = true;
                }
            }
            final int xx2 = this.pos.getX() + x2;
            final int zz2 = this.pos.getZ() + z2;
            te2 = this.world.getTileEntity(new BlockPos(xx2, cc.getY(), zz2));
            if (te2 instanceof TilePedestal) {
                this.symmetry -= 2;
                if (((TilePedestal) te2).getStackInSlot(0).isEmpty() || !items) {
                    continue;
                }
                --this.symmetry;
            }
        }
        float sym = 0.0f;
        for (final BlockPos cc2 : stuff) {
            final boolean items2 = false;
            final int x = this.pos.getX() - cc2.getX();
            final int z3 = this.pos.getZ() - cc2.getZ();
            Block bi2 = this.world.getBlockState(new BlockPos(cc2.getX(), cc2.getY(), cc2.getZ())).getBlock();
            if (bi2 == Blocks.SKULL || (bi2 instanceof IInfusionStabiliser && ((IInfusionStabiliser) bi2)
                    .canStabaliseInfusion(this.getWorld(), cc2.getX(), cc2.getY(), cc2.getZ()))) {
                sym += 0.1f;
            }
            final int xx3 = this.pos.getX() + x;
            final int zz3 = this.pos.getZ() + z3;
            bi2 = this.world.getBlockState(new BlockPos(xx3, cc2.getY(), zz3)).getBlock();
            if (bi2 == Blocks.SKULL || (bi2 instanceof IInfusionStabiliser && ((IInfusionStabiliser) bi2)
                    .canStabaliseInfusion(this.getWorld(), xx3, cc2.getY(), zz3))) {
                sym -= 0.2f;
            }
        }
        this.symmetry += (int) sym;
    }

    private void doEffects() {
        if (this.mode == 2) {
            if (this.craftCount == 0) {
                THSoundBridge.playLegacy(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), "thaumcraft:infuserstart", 0.5f, 1.0f, false);
            } else if (this.craftCount % INFUSER_SOUND_INTERVAL == 0) {
                THSoundBridge.playLegacy(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), "thaumcraft:infuser", 0.5f, 1.0f, false);
            }
            ++this.craftCount;
            Thaumcraft.proxy.blockRunes(
                    this.world,
                    this.pos.getX(),
                    this.pos.getY() - 2,
                    this.pos.getZ(),
                    0.5f + this.world.rand.nextFloat() * 0.2f,
                    0.1f,
                    0.7f + this.world.rand.nextFloat() * 0.3f,
                    INFUSER_PARTICLE_COUNT,
                    -0.03f);
        } else if (this.craftCount > 0) {
            this.craftCount -= 2;
            if (this.craftCount < 0) {
                this.craftCount = 0;
            }
            if (this.craftCount > CRAFTCOUNT_WIND_DOWN_MAX) {
                this.craftCount = CRAFTCOUNT_WIND_DOWN_MAX;
            }
        }
        if (this.mode == 2 && this.startUp != 1.0f) {
            if (this.startUp < 1.0f) {
                this.startUp += Math.max(this.startUp / 10.0f, 0.001f);
            }
            if (this.startUp > 0.999) {
                this.startUp = 1.0f;
            }
        }
        for (final String fxk : this.sourceFX.keySet().toArray(new String[0])) {
            final SourceFX fx = this.sourceFX.get(fxk);
            if (fx.ticks <= 0) {
                this.sourceFX.remove(fxk);
            } else {
                if (fx.loc.getX() == this.pos.getX() && fx.loc.getY() == this.pos.getY() && fx.loc.getZ() == this.pos.getZ()) {
                    final Entity player = this.world.getEntityByID(fx.color);
                    if (player != null) {
                        for (int a = 0; a < Thaumcraft.proxy.particleCount(2); ++a) {
                            Thaumcraft.proxy.drawInfusionParticles4(
                                    this.world,
                                    player.posX + (this.world.rand.nextFloat() - this.world.rand.nextFloat())
                                            * player.width,
                                    player.getEntityBoundingBox().minY + this.world.rand.nextFloat() * player.height,
                                    player.posZ + (this.world.rand.nextFloat() - this.world.rand.nextFloat())
                                            * player.width,
                                    this.pos.getX(),
                                    this.pos.getY(),
                                    this.pos.getZ());
                        }
                    }
                } else {
                    final TileEntity tile = this.world.getTileEntity(fx.loc);
                    if (tile instanceof TilePedestal) {
                        final ItemStack is = ((TilePedestal) tile).getStackInSlot(0);
                        if (is != null) {
                            if (this.world.rand.nextInt(3) == 0) {
                                Thaumcraft.proxy.drawInfusionParticles3(
                                        this.world,
                                        fx.loc.getX() + this.world.rand.nextFloat(),
                                        fx.loc.getY() + this.world.rand.nextFloat() + 1.0f,
                                        fx.loc.getZ() + this.world.rand.nextFloat(),
                                        this.pos.getX(),
                                        this.pos.getY(),
                                        this.pos.getZ());
                            } else {
                                final Item bi = is.getItem();
                                final int md = is.getItemDamage();
                                if (bi instanceof ItemBlock) {
                                    for (int a2 = 0; a2 < Thaumcraft.proxy.particleCount(2); ++a2) {
                                        Thaumcraft.proxy.drawInfusionParticles2(
                                                this.world,
                                                fx.loc.getX() + this.world.rand.nextFloat(),
                                                fx.loc.getY() + this.world.rand.nextFloat() + 1.0f,
                                                fx.loc.getZ() + this.world.rand.nextFloat(),
                                                this.pos.getX(),
                                                this.pos.getY(),
                                                this.pos.getZ(),
                                                Block.getBlockFromItem(bi).getStateFromMeta(md));
                                    }
                                } else {
                                    for (int a2 = 0; a2 < Thaumcraft.proxy.particleCount(2); ++a2) {
                                        Thaumcraft.proxy.drawInfusionParticles1(
                                                this.world,
                                                fx.loc.getX() + 0.4f + this.world.rand.nextFloat() * 0.2f,
                                                fx.loc.getY() + 1.23f + this.world.rand.nextFloat() * 0.2f,
                                                fx.loc.getZ() + 0.4f + this.world.rand.nextFloat() * 0.2f,
                                                this.pos.getX(),
                                                this.pos.getY(),
                                                this.pos.getZ(),
                                                bi,
                                                md);
                                    }
                                }
                            }
                        }
                    } else {
                        fx.ticks = 0;
                    }
                }
                --fx.ticks;
                this.sourceFX.put(fxk, fx);
            }
        }
        if (this.mode == 2 && this.instability > 0
                && this.world.rand.nextInt(INSTABILITY_EFFECT_CHANCE) <= this.instability) {
            Thaumcraft.proxy.nodeBolt(
                    this.world,
                    this.pos.getX() + 0.5f,
                    this.pos.getY() + 0.5f,
                    this.pos.getZ() + 0.5f,
                    this.pos.getX() + 0.5f + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 2.0f,
                    this.pos.getY() + 0.5f + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 2.0f,
                    this.pos.getZ() + 0.5f + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 2.0f);
        }
    }

    public void craftCycle() {
        if (this.instability > 0 && this.world.rand.nextInt(INSTABILITY_CRAFT_CHANCE) <= this.instability) {
            switch (this.world.rand.nextInt(21)) {
    case 0: case 2: case 10: case 13: {
                    this.inEvEjectItem(0);
                } break;
    case 6: case 17: {
                    this.inEvEjectItem(1);
                } break;
    case 1: case 11: {
                    this.inEvEjectItem(2);
                } break;
    case 3: case 8: case 14: {
                    this.inEvZap(false);
                } break;
    case 5: case 16: {
                    this.inEvHarm(false);
                } break;
    case 12: {
                    this.inEvZap(true);
                } break;
    case 19: {
                    this.inEvEjectItem(3);
                } break;
    case 7: {
                    this.inEvEjectItem(4);
                } break;
    case 4: case 15: {
                    this.inEvEjectItem(5);
                } break;
    case 18: {
                    this.inEvHarm(true);
                } break;
    case 9: {
                    this.world.createExplosion(
                            null,
                            this.pos.getX() + 0.5f,
                            this.pos.getY() + 0.5f,
                            this.pos.getZ() + 0.5f,
                            1.5f + this.world.rand.nextFloat(),
                            false);
                } break;
    case 20: {
                    this.inEvWarp();
                } break;
}
        }
        if (this.instability > 0 && this.entityContained != null) {
            float visDrawn = 999.0f;
            if (!this.world.isRemote) {
                float temp = VisNetHandler.drainVis(
                        this.world,
                        this.pos.getX(),
                        this.pos.getY() + 1,
                        this.pos.getZ(),
                        Aspect.EARTH,
                        VIS_DRAIN_AMOUNT);
                if (temp < visDrawn) {
                    visDrawn = temp;
                }
                temp = VisNetHandler.drainVis(
                        this.world,
                        this.pos.getX(),
                        this.pos.getY() + 1,
                        this.pos.getZ(),
                        Aspect.WATER,
                        VIS_DRAIN_AMOUNT);
                if (temp < visDrawn) {
                    visDrawn = temp;
                }
                temp = VisNetHandler.drainVis(
                        this.world,
                        this.pos.getX(),
                        this.pos.getY() + 1,
                        this.pos.getZ(),
                        Aspect.ORDER,
                        VIS_DRAIN_AMOUNT);
                if (temp < visDrawn) {
                    visDrawn = temp;
                }
            }
            this.getEntityContained().setHealth(
                    this.getEntityContained().getHealth()
                            - this.instability / INSTABILITY_DAMAGE_DIVISOR / (5.0f + visDrawn));
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
            if (this.getEntityContained().getHealth() <= 0.0f) {
                this.killSubject();
                return;
            }
        } else if (this.instability > 0) {
            float visDrawn = 999.0f;
            if (!this.world.isRemote) {
                float temp = VisNetHandler.drainVis(
                        this.world,
                        this.pos.getX(),
                        this.pos.getY() + 1,
                        this.pos.getZ(),
                        Aspect.EARTH,
                        VIS_DRAIN_AMOUNT);
                if (temp < visDrawn) {
                    visDrawn = temp;
                }
                temp = VisNetHandler.drainVis(
                        this.world,
                        this.pos.getX(),
                        this.pos.getY() + 1,
                        this.pos.getZ(),
                        Aspect.WATER,
                        VIS_DRAIN_AMOUNT);
                if (temp < visDrawn) {
                    visDrawn = temp;
                }
                temp = VisNetHandler.drainVis(
                        this.world,
                        this.pos.getX(),
                        this.pos.getY() + 1,
                        this.pos.getZ(),
                        Aspect.ORDER,
                        VIS_DRAIN_AMOUNT);
                if (temp < visDrawn) {
                    visDrawn = temp;
                }
            }
            this.selfInfusionHealth -= this.instability / INSTABILITY_DAMAGE_DIVISOR / (5.0f + visDrawn);
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
            if (this.selfInfusionHealth <= 0.0f) {
                this.killSubject();
                return;
            }
        }
        if (this.essentiaDemanded.visSize() > 0) {
            final Aspect[] aspects = this.essentiaDemanded.getAspects();
            final int length = aspects.length;
            int i = 0;
            while (i < length) {
                final Aspect aspect = aspects[i];
                if (this.essentiaDemanded.getAmount(aspect) > 0) {
                    this.currentlySucking = aspect;
                    if (this.tryDrawAllEssentia()) {
                        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                        this.markDirty();
                        return;
                    }
                    if (this.world.rand.nextInt(INSTABILITY_INCREASE_CHANCE_BASE - this.recipeInstability * 3)
                            == 0) {
                        ++this.instability;
                    }
                    if (this.instability > MAX_INSTABILITY) {
                        this.instability = MAX_INSTABILITY;
                    }
                    this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                    this.markDirty();
                    break;
                } else {
                    ++i;
                }
            }
            this.checkSurroundings = true;
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
            this.markDirty();
            return;
        }
        if (this.recipeIngredients.size() > 0) {
            for (int a = 0; a < this.recipeIngredients.size(); ++a) {
                for (final BlockPos cc : this.pedestals) {
                    final TileEntity te = this.world.getTileEntity(new BlockPos(cc.getX(), cc.getY(), cc.getZ()));
                    if (te instanceof TilePedestal && !((TilePedestal) te).getStackInSlot(0).isEmpty()
                            && InfusionRecipe.areItemStacksEqual(
                                    ((TilePedestal) te).getStackInSlot(0),
                                    this.recipeIngredients.get(a),
                                    true)) {
                        if (this.itemCount == 0) {
                            this.itemCount = 5;
                            PacketHandler.INSTANCE.sendToAllAround(
                                    new PacketInfusionFX(
                                            this.pos.getX(),
                                            this.pos.getY() - 2,
                                            this.pos.getZ(),
                                            (byte) (this.pos.getX() - cc.getX()),
                                            (byte) (this.pos.getY() - cc.getY() - 2),
                                            (byte) (this.pos.getZ() - cc.getZ()),
                                            0),
                                    new NetworkRegistry.TargetPoint(
                                            this.getWorld().provider.getDimension(),
                                            this.pos.getX(),
                                            this.pos.getY(),
                                            this.pos.getZ(),
                                            32.0));
                        } else if (this.itemCount-- <= 1) {
                            final ItemStack is = ((TilePedestal) te).getStackInSlot(0).getItem()
                                    .getContainerItem(((TilePedestal) te).getStackInSlot(0));
                            ((TilePedestal) te).setInventorySlotContents(0, is.isEmpty() ? ItemStack.EMPTY : is.copy());
                            this.recipeIngredients.remove(a);
                        }
                        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
                        this.markDirty();
                        return;
                    }
                }
            }
            return;
        }
        this.instability = 0;
        this.craftingFinish(this.recipeOutput, this.recipeOutputLabel);
        this.recipeOutput = null;
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        this.markDirty();
    }

    private void inEvZap(final boolean all) {
        final List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                new AxisAlignedBB(
                        this.pos.getX(),
                        this.pos.getY(),
                        this.pos.getZ(),
                        this.pos.getX() + 1,
                        this.pos.getY() + 1,
                        this.pos.getZ() + 1).expand(10.0, 10.0, 10.0));
        if (targets != null && !targets.isEmpty()) {
            for (final Entity target : targets) {
                thaumcraft.common.lib.network.PacketHandler.INSTANCE.sendToAllAround(
                        new PacketFXBlockZap(
                                this.pos.getX() + 0.5f,
                                this.pos.getY() + 0.5f,
                                this.pos.getZ() + 0.5f,
                                (float) target.posX,
                                (float) target.posY + target.height / 2.0f,
                                (float) target.posZ),
                        new NetworkRegistry.TargetPoint(
                                this.world.provider.getDimension(),
                                this.pos.getX(),
                                this.pos.getY(),
                                this.pos.getZ(),
                                32.0));
                target.attackEntityFrom(DamageSource.MAGIC, (float) (4 + this.world.rand.nextInt(4)));
                if (!all) {
                    break;
                }
            }
        }
    }

    private void inEvHarm(final boolean all) {
        final List<EntityLivingBase> targets = (List<EntityLivingBase>) this.world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                new AxisAlignedBB(
                        this.pos.getX(),
                        this.pos.getY(),
                        this.pos.getZ(),
                        this.pos.getX() + 1,
                        this.pos.getY() + 1,
                        this.pos.getZ() + 1).expand(10.0, 10.0, 10.0));
        if (targets != null && targets.size() > 0) {
            for (final EntityLivingBase target : targets) {
                if (this.world.rand.nextBoolean()) {
                    final Potion taint = Potion.getPotionById(Config.potionTaintPoisonID);
                    if (taint != null) target.addPotionEffect(new PotionEffect(taint, 120, 0, false, true));
                } else {
                    final Potion visExhaust = Potion.getPotionById(Config.potionVisExhaustID);
                    if (visExhaust != null) {
                        final PotionEffect pe = new PotionEffect(visExhaust, 2400, 0, true, true);
                        pe.getCurativeItems().clear();
                        target.addPotionEffect(pe);
                    }
                }
                if (!all) {
                    break;
                }
            }
        }
    }

    private void inEvWarp() {
        final List<EntityPlayer> targets = (List<EntityPlayer>) this.world.getEntitiesWithinAABB(
                EntityPlayer.class,
                new AxisAlignedBB(
                        this.pos.getX(),
                        this.pos.getY(),
                        this.pos.getZ(),
                        this.pos.getX() + 1,
                        this.pos.getY() + 1,
                        this.pos.getZ() + 1).expand(10.0, 10.0, 10.0));
        if (targets != null && targets.size() > 0) {
            final EntityPlayer target = targets.get(this.world.rand.nextInt(targets.size()));
            if (this.world.rand.nextFloat() < 0.25f) {
                Thaumcraft.addStickyWarpToPlayer(target, 1);
            } else {
                Thaumcraft.addWarpToPlayer(target, 1 + this.world.rand.nextInt(5), true);
            }
        }
    }

    private void inEvEjectItem(final int type) {
        for (int q = 0; q < 50 && this.pedestals.size() > 0; ++q) {
            final BlockPos cc = this.pedestals.get(this.world.rand.nextInt(this.pedestals.size()));
            final TileEntity te = this.world.getTileEntity(new BlockPos(cc.getX(), cc.getY(), cc.getZ()));
            if (te instanceof TilePedestal && !((TilePedestal) te).getStackInSlot(0).isEmpty()) {
                if (type < 3 || type == 5) {
                    InventoryUtils.dropItems(this.world, cc.getX(), cc.getY(), cc.getZ());
                } else {
                    ((TilePedestal) te).setInventorySlotContents(0, ItemStack.EMPTY);
                }
                if (type == 1 || type == 3) {
                    this.world.setBlockState(new BlockPos(cc.getX(), cc.getY() + 1, cc.getZ()), ConfigBlocks.blockFluxGoo.getStateFromMeta(7), 3);
                    THSoundBridge.play(this.world, cc.getX(), cc.getY(), cc.getZ(), "game.neutral.swim", 0.3f, 1.0f);
                } else if (type == 2 || type == 4) {
                    this.world.setBlockState(new BlockPos(cc.getX(), cc.getY() + 1, cc.getZ()), ConfigBlocks.blockFluxGas.getStateFromMeta(7), 3);
                    THSoundBridge.play(this.world, cc.getX(), cc.getY(), cc.getZ(), "random.fizz", 0.3f, 1.0f);
                } else if (type == 5) {
                    this.world.createExplosion(null, cc.getX() + 0.5f, cc.getY() + 0.5f, cc.getZ() + 0.5f, 1.0f, false);
                }
                this.world.addBlockEvent(cc, ConfigBlocks.blockStoneDevice, 11, 0);
                thaumcraft.common.lib.network.PacketHandler.INSTANCE.sendToAllAround(
                        new PacketFXBlockZap(
                                this.pos.getX() + 0.5f,
                                this.pos.getY() + 0.5f,
                                this.pos.getZ() + 0.5f,
                                cc.getX() + 0.5f,
                                cc.getY() + 1.5f,
                                cc.getZ() + 0.5f),
                        new NetworkRegistry.TargetPoint(
                                this.world.provider.getDimension(),
                                this.pos.getX(),
                                this.pos.getY(),
                                this.pos.getZ(),
                                32.0));
                return;
            }
        }
    }

    public void craftingFinish(final Object out, final String label) {
        if (this.recipeType == 0) {
            if (out instanceof Integer || out instanceof net.minecraft.util.ResourceLocation) {
                net.minecraft.util.ResourceLocation entityId;
                if (out instanceof net.minecraft.util.ResourceLocation) {
                    entityId = (net.minecraft.util.ResourceLocation) out;
                } else if ((Integer)out < 0) {
                    entityId = EntityList.getKey(EntityList.getClassFromID(-(Integer)out));
                } else {
                    entityId = com.kentington.thaumichorizons.common.lib.InfusionEntityRegistry.idForLegacy((Integer)out);
                }
                Entity result = EntityList.createEntityByIDFromName(entityId, this.world);
                if (!(result instanceof EntityLivingBase)) throw new IllegalStateException("Invalid infusion entity " + entityId);
                EntityLivingBase created = (EntityLivingBase)result;
                created.copyLocationAndAnglesFrom(this.getEntityContained());
                com.kentington.thaumichorizons.common.lib.EntityTransformation.copyLivingState(this.getEntityContained(), created);
                if (created instanceof IEntityInfusedStats) {
                    ((IEntityInfusedStats) created).resetStats();
                }
                this.setEntityContained(created);
            } else if (out instanceof NBTBase) {
                final NBTTagCompound tagMods = (NBTTagCompound) out;
                final Multimap map = HashMultimap.create();
                if (tagMods.getDouble("generic.movementSpeed") > 0.0) {
                    map.put(
                            "generic.movementSpeed",
                            new AttributeModifier(
                                    "generic.movementSpeed",
                                    tagMods.getDouble("generic.movementSpeed") / 10.0,
                                    1));
                }
                if (tagMods.getDouble("generic.maxHealth") > 0.0) {
                    map.put(
                            "generic.maxHealth",
                            new AttributeModifier("generic.maxHealth", tagMods.getDouble("generic.maxHealth"), 1));
                }
                if (tagMods.getDouble("generic.attackDamage") > 0.0) {
                    map.put(
                            "generic.attackDamage",
                            new AttributeModifier(
                                    "generic.attackDamage",
                                    tagMods.getDouble("generic.attackDamage"),
                                    1));
                }
                if (map.size() > 0) {
                    this.getEntityContained().getAttributeMap().applyAttributeModifiers(map);
                }
                final Set<String> keys = (Set<String>) tagMods.getKeySet();
                for (final String s : keys) {
                    if (!s.startsWith("generic.")) {
                        ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.getEntityContained()))
                                .addInfusion(tagMods.getInteger(s));
                        if (tagMods.getInteger(s) != 7) {
                            continue;
                        }
                        ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.getEntityContained()))
                                .setOwner(this.recipePlayer);
                    }
                }
            }
            ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(this.getEntityContained()))
                    .addCost(this.myEssentia);
            if (this.entityContained instanceof EntityLiving) {
                ((EntityLiving) this.entityContained).enablePersistence();
            }
            this.mode = 0;
        } else {
            for (int i = 0; i < this.selfInfusions.length; ++i) {
                if (this.selfInfusions[i] == 0) {
                    this.selfInfusions[i] = (Integer) this.recipeOutput;
                    break;
                }
            }
            this.mode = 4;
        }
        PacketHandler.INSTANCE.sendToAllAround(
                new PacketFXInfusionDone(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ()),
                new NetworkRegistry.TargetPoint(
                        this.world.provider.getDimension(),
                        this.pos.getX(),
                        this.pos.getY(),
                        this.pos.getZ(),
                        32.0));
        this.essentiaDemanded = new AspectList();
        this.myEssentia = new AspectList();
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        this.markDirty();
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setInteger("mode", this.mode);
        nbttagcompound.setInteger("progress", this.progress);
        nbttagcompound.setShort("instability", (short) this.instability);
        if (this.currentlySucking != null) {
            nbttagcompound.setString("sucking", this.currentlySucking.getTag());
        } else {
            nbttagcompound.setString("sucking", "");
        }
        NBTTagList tlist = new NBTTagList();
        nbttagcompound.setTag("myEssentia", tlist);
        for (final Aspect aspect : this.myEssentia.getAspects()) {
            if (aspect != null) {
                final NBTTagCompound f = new NBTTagCompound();
                f.setString("key", aspect.getTag());
                f.setInteger("amount", this.myEssentia.getAmount(aspect));
                tlist.appendTag(f);
            }
        }
        tlist = new NBTTagList();
        nbttagcompound.setTag("essentiaDemanded", tlist);
        for (final Aspect aspect : this.essentiaDemanded.getAspects()) {
            if (aspect != null) {
                final NBTTagCompound f = new NBTTagCompound();
                f.setString("key", aspect.getTag());
                f.setInteger("amount", this.essentiaDemanded.getAmount(aspect));
                tlist.appendTag(f);
            }
        }
        final NBTTagCompound entityData = new NBTTagCompound();
        if (this.getEntityContained() != null && !(this.getEntityContained() instanceof EntityPlayer)) {
            entityData.setString("id", String.valueOf(EntityList.getKey(this.getEntityContained())));
            this.getEntityContained().writeToNBT(entityData);
        } else if (this.getEntityContained() != null) {
            entityData.setString("id", "PLAYER");
            entityData.setString("playerName", this.getEntityContained().getName());
        }
        nbttagcompound.setTag("entity", entityData);
        final NBTTagCompound item = new NBTTagCompound();
        if ((!this.sample.isEmpty())) {
            this.sample.writeToNBT(item);
        }
        nbttagcompound.setTag("sample", item);
        final NBTTagCompound itemtoo = new NBTTagCompound();
        if ((!this.nutrients.isEmpty())) {
            this.nutrients.writeToNBT(itemtoo);
        }
        nbttagcompound.setTag("nutrients", itemtoo);
        nbttagcompound.setIntArray("selfInfusions", this.selfInfusions);
        nbttagcompound.setFloat("selfInfusionHealth", this.selfInfusionHealth);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.mode = nbttagcompound.getInteger("mode");
        this.progress = nbttagcompound.getInteger("progress");
        this.instability = nbttagcompound.getShort("instability");
        this.currentlySucking = Aspect.getAspect(nbttagcompound.getString("sucking"));
        AspectList al = new AspectList();
        NBTTagList tlist = nbttagcompound.getTagList("myEssentia", 10);
        for (int j = 0; j < tlist.tagCount(); ++j) {
            final NBTTagCompound rs = tlist.getCompoundTagAt(j);
            if (rs.hasKey("key")) {
                al.add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
            }
        }
        this.myEssentia = al.copy();
        al = new AspectList();
        tlist = nbttagcompound.getTagList("essentiaDemanded", 10);
        for (int j = 0; j < tlist.tagCount(); ++j) {
            final NBTTagCompound rs = tlist.getCompoundTagAt(j);
            if (rs.hasKey("key")) {
                al.add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
            }
        }
        this.essentiaDemanded = al.copy();
        if (this.firstTick) {
            entityNBTObj = nbttagcompound.getCompoundTag("entity");
        } else {
            if (nbttagcompound.getCompoundTag("entity").getString("id").equals("PLAYER")) {
                this.setEntityContained(
                        this.world.getPlayerEntityByName(
                                nbttagcompound.getCompoundTag("entity").getString("playerName")));
            } else if (nbttagcompound.getCompoundTag("entity").hasKey("id")) {

                this.setEntityContained(
                        (EntityLivingBase) EntityList
                                .createEntityFromNBT(nbttagcompound.getCompoundTag("entity"), this.world));
            }
        }
        this.sample = readLegacyInventoryStack(nbttagcompound, "sample");
        this.nutrients = readLegacyInventoryStack(nbttagcompound, "nutrients");
        this.selfInfusions = nbttagcompound.getIntArray("selfInfusions");
        if (this.selfInfusions.length == 0) {
            this.selfInfusions = new int[SELF_INFUSION_SLOTS];
        }
        this.selfInfusionHealth = nbttagcompound.getFloat("selfInfusionHealth");
    }

    /** Compatible with old vat NBT keys and empty-compound slot encoding. */
    private static ItemStack readLegacyInventoryStack(NBTTagCompound root, String key) {
        NBTTagCompound tag = root.getCompoundTag(key);
        return tag.hasKey("id", 8) ? new ItemStack(tag) : ItemStack.EMPTY;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtCompound) {
        super.readFromNBT(nbtCompound);
        final NBTTagList nbttaglist = nbtCompound.getTagList("recipein", 10);
        this.recipeIngredients = new ArrayList<>();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound saved = nbttaglist.getCompoundTagAt(i);
            int index = saved.getByte("item") & 0xFF;
            // Match the original sparse slot index; bound NBT to prevent huge allocations.
            if (index > 127) continue;
            while (this.recipeIngredients.size() <= index) this.recipeIngredients.add(ItemStack.EMPTY);
            this.recipeIngredients.set(index, saved.hasKey("id", 8)
                    ? new ItemStack(saved) : ItemStack.EMPTY);
        }
        final String rot = nbtCompound.getString("rotype");
        if (rot != null && rot.equals("@")) {
            this.recipeOutput = nbtCompound.getInteger("recipeout");
        } else if ("entity".equals(rot)) {
            this.recipeOutput = new net.minecraft.util.ResourceLocation(nbtCompound.getString("recipeout"));
        } else if (rot != null) {
            this.recipeOutputLabel = rot;
            this.recipeOutput = nbtCompound.getTag("recipeout");
        }
        this.recipeInstability = nbtCompound.getInteger("recipeinst");
        this.recipeType = nbtCompound.getInteger("recipetype");
        this.recipePlayer = nbtCompound.getString("recipeplayer");
        if (this.recipePlayer.isEmpty()) {
            this.recipePlayer = null;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbtCompound) {
        super.writeToNBT(nbtCompound);
        if (this.recipeIngredients != null && this.recipeIngredients.size() > 0) {
            final NBTTagList nbttaglist = new NBTTagList();
            int count = 0;
            for (final ItemStack stack : this.recipeIngredients) {
                if (stack != null && !stack.isEmpty()) {
                    final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    nbttagcompound1.setByte("item", (byte) count);
                    stack.writeToNBT(nbttagcompound1);
                    nbttaglist.appendTag(nbttagcompound1);
                }
                ++count;
            }
            nbtCompound.setTag("recipein", nbttaglist);
        }
        if (this.recipeOutput != null && this.recipeOutput instanceof Integer) {
            nbtCompound.setString("rotype", "@");
        }
        if (this.recipeOutput instanceof net.minecraft.util.ResourceLocation) {
            nbtCompound.setString("rotype", "entity");
            nbtCompound.setString("recipeout", this.recipeOutput.toString());
        }
        if (this.recipeOutput != null && this.recipeOutput instanceof NBTBase) {
            nbtCompound.setString("rotype", this.recipeOutputLabel);
        }
        if (this.recipeOutput != null && this.recipeOutput instanceof Integer) {
            nbtCompound.setTag("recipeout", new NBTTagInt((Integer) this.recipeOutput));
        }
        if (this.recipeOutput != null && this.recipeOutput instanceof NBTBase) {
            nbtCompound.setTag("recipeout", (NBTBase) this.recipeOutput);
        }
        nbtCompound.setInteger("recipeinst", this.recipeInstability);
        nbtCompound.setInteger("recipetype", this.recipeType);
        nbtCompound.setInteger("recipexp", this.recipeXP);
        if (this.recipePlayer == null) {
            nbtCompound.setString("recipeplayer", "");
        } else {
            nbtCompound.setString("recipeplayer", this.recipePlayer);
        }
        return nbtCompound;
    }

    public boolean isValidInfusionTarget() {
        if (this.getEntityContained() != null
                && this.getEntityContained().getCreatureAttribute() != EnumCreatureAttribute.UNDEAD
                && !(this.getEntityContained() instanceof EntityPlayer)
                && !(this.getEntityContained() instanceof EntityGolem)
                && !(this.getEntityContained() instanceof EntityGolemBase)
                && !(this.getEntityContained() instanceof IMerchant)
                && !(this.getEntityContained() instanceof INpc)) {
            for (final Class clazz : ThaumicHorizons.classBanList) {
                if (this.getEntityContained().getClass().isAssignableFrom(clazz)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isDisassembling() { return disassembling; }

    public void killMe() {
        // 36 part break callbacks must not recursively disassemble/drop items.
        if (this.world == null || this.world.isRemote || disassembling) return;
        disassembling = true;
        try {
        if (this.entityContained != null) {
            this.killSubject();
        }
        if ((!this.sample.isEmpty())) {
            final EntityItem item = new EntityItem(
                    this.world,
                    this.pos.getX() + 0.5,
                    this.pos.getY() + 1.5,
                    this.pos.getZ() - 0.5,
                    this.sample);
            this.world.spawnEntity(item);
            this.sample = ItemStack.EMPTY;
        }
        if ((!this.nutrients.isEmpty())) {
            final EntityItem item = new EntityItem(
                    this.world,
                    this.pos.getX() + 0.5,
                    this.pos.getY() + 1.5,
                    this.pos.getZ() - 0.5,
                    this.nutrients);
            this.world.spawnEntity(item);
            this.nutrients = ItemStack.EMPTY;
        }
        for (int y = 0; y < 4; ++y) {
            for (int x = -1; x < 2; ++x) {
                for (int z = -1; z < 2; ++z) {
                    if (x != 0 || z != 0) {
                        if (y == 0 || y == 3) {
                            this.world.setBlockState(this.pos.add(x, -y, z),
                                ConfigBlocks.blockWoodenDevice.getStateFromMeta(6), 3);
                        } else {
                            this.world.setBlockState(this.pos.add(x, -y, z),
                                Blocks.GLASS.getStateFromMeta(0), 3);
                        }
                    } else if (y == 0 || y == 3) {
                        this.world.setBlockState(this.pos.add(x, -y, z),
                                ConfigBlocks.blockMetalDevice.getStateFromMeta(9), 3);
                    } else {
                        this.world.setBlockState(this.pos.add(x, -y, z),
                                Blocks.WATER.getStateFromMeta(0), 3);
                    }
                }
            }
        }
        } finally {
            disassembling = false;
        }
    }

    public void killSubject() {
        if (!this.world.isRemote
                && ((this.entityContained != null && !(this.entityContained instanceof EntityPlayer))
                        || this.recipeType == 1)) {
            this.world.createExplosion(null, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, 0.5f, false);
            for (int a = 0; a < 25; ++a) {
                final int xx = this.pos.getX() + this.world.rand.nextInt(8) - this.world.rand.nextInt(8);
                final int yy = this.pos.getY() + this.world.rand.nextInt(8) - this.world.rand.nextInt(8);
                final int zz = this.pos.getZ() + this.world.rand.nextInt(8) - this.world.rand.nextInt(8);
                if (this.world.isAirBlock(new BlockPos(xx, yy, zz))) {
                    if (yy < this.pos.getY()) {
                        this.world.setBlockState(new BlockPos(xx, yy, zz), ConfigBlocks.blockFluxGoo.getStateFromMeta(8), 3);
                    } else {
                        this.world.setBlockState(new BlockPos(xx, yy, zz), ConfigBlocks.blockFluxGas.getStateFromMeta(8), 3);
                    }
                }
            }
        }
        this.selfInfusions = new int[SELF_INFUSION_SLOTS];
        this.setEntityContained(null);
        this.mode = 0;
        this.currentlySucking = null;
        this.myEssentia = new AspectList();
        this.essentiaDemanded = new AspectList();
        this.progress = 0;
        this.symmetry = 0;
        this.instability = 0;
        this.craftCount = 0;
        this.count = 0;
        this.markDirty();
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
    }

    // ISidedInventory (Forge 1.12.2). Keep the original two slots and routing.
    @Override
    public int getSizeInventory() { return 2; }

    @Override
    public boolean isEmpty() { return this.sample.isEmpty() && this.nutrients.isEmpty(); }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == 0 ? this.sample : slot == 1 ? this.nutrients : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (amount <= 0 || slot < 0 || slot >= getSizeInventory()) return ItemStack.EMPTY;
        ItemStack held = getStackInSlot(slot);
        if (held.isEmpty()) return ItemStack.EMPTY;
        ItemStack taken;
        if (amount >= held.getCount()) {
            taken = held;
            setInventorySlotContents(slot, ItemStack.EMPTY);
        } else {
            taken = held.splitStack(amount);
            if (held.isEmpty()) setInventorySlotContents(slot, ItemStack.EMPTY);
            else markDirty();
        }
        return taken;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        if (slot < 0 || slot >= getSizeInventory()) return ItemStack.EMPTY;
        ItemStack taken = getStackInSlot(slot);
        if (!taken.isEmpty()) setInventorySlotContents(slot, ItemStack.EMPTY);
        return taken;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot < 0 || slot >= getSizeInventory()) return;
        ItemStack safe = stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack;
        if (!safe.isEmpty() && safe.getCount() > getInventoryStackLimit()) {
            safe = safe.copy();
            safe.setCount(getInventoryStackLimit());
        }
        if (slot == 0) this.sample = safe;
        else this.nutrients = safe;
        markDirty();
        if (this.world != null && !this.world.isRemote) {
            net.minecraft.block.state.IBlockState state = this.world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, state, state, 3);
        }
    }

    @Override
    public String getName() { return "container.vat"; }

    @Override
    public boolean hasCustomName() { return false; }

    @Override
    public ITextComponent getDisplayName() { return new TextComponentTranslation(getName()); }

    @Override
    public int getInventoryStackLimit() { return 64; }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world != null && world.getTileEntity(pos) == this
                && player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (slot == 0) {
            return stack.getItem() == ThaumicHorizons.itemSyringeBloodSample
                    || stack.getItem() == ThaumicHorizons.itemCorpseEffigy
                    || ThaumicHorizons.incarnationItems.containsKey(stack.getItem());
        }
        return slot == 1 && stack.getItem() == ThaumicHorizons.itemNutrients;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing face) { return new int[] {0, 1}; }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, EnumFacing face) {
        return isItemValidForSlot(slot, item);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing face) { return false; }

    // Forge 1.12.2 inventory field synchronization is intentionally handled
    // by ContainerVat, not by the deprecated ICrafting methods.
    @Override
    public int getField(int id) {
        if (id == 0) return this.mode;
        if (id == 1) return this.progress;
        if (id == 2) return Math.round(this.selfInfusionHealth * 100.0f);
        if (id == 3) return this.instability;
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        if (id == 0) this.mode = value;
        else if (id == 1) this.progress = value;
        else if (id == 2) this.selfInfusionHealth = value / 100.0f;
        else if (id == 3) this.instability = value;
    }

    @Override
    public int getFieldCount() { return 4; }

    @Override
    public void clear() {
        setInventorySlotContents(0, ItemStack.EMPTY);
        setInventorySlotContents(1, ItemStack.EMPTY);
    }

    @Override
    public boolean isConnectable(final EnumFacing face) {
        return this.mode == 1 && face == EnumFacing.UP;
    }

    @Override
    public boolean canInputFrom(final EnumFacing face) {
        return this.mode == 1 && face == EnumFacing.UP;
    }

    @Override
    public boolean canOutputTo(final EnumFacing face) {
        return false;
    }

    @Override
    public void setSuction(final Aspect aspect, final int amount) {}

    @Override
    public Aspect getSuctionType(final EnumFacing face) {
        if (this.mode != 2) {
            return null;
        }
        return this.currentlySucking;
    }

    @Override
    public int getSuctionAmount(final EnumFacing face) {
        return (this.essentiaDemanded.size() > 0) ? 128 : 0;
    }

    @Override
    public int takeEssentia(final Aspect aspect, final int amount, final EnumFacing face) {
        return 0;
    }

    @Override
    public int addEssentia(final Aspect aspect, final int amount, final EnumFacing face) {
        return this.canInputFrom(face) ? (amount - this.addToContainer(aspect, amount)) : 0;
    }

    @Override
    public Aspect getEssentiaType(final EnumFacing face) {
        return null;
    }

    @Override
    public int getEssentiaAmount(final EnumFacing face) {
        return 0;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Override
    public boolean renderExtendedTube() {
        return false;
    }

    @Override
    public AspectList getAspects() {
        if (this.mode != 2) {
            if (this.myEssentia.getAspects().length > 0 && this.myEssentia.getAspects()[0] != null) {
                return this.myEssentia;
            }
        } else {
            if (this.essentiaDemanded.getAspects().length > 0 && this.essentiaDemanded.getAspects()[0] != null) {
                return this.essentiaDemanded;
            }
        }
        return null;
    }

    @Override
    public void setAspects(final AspectList aspects) {}

    @Override
    public boolean doesContainerAccept(final Aspect tag) {
        return this.currentlySucking != null && tag.getTag().equals(this.currentlySucking.getTag());
    }

    @Override
    public int addToContainer(final Aspect tag, final int amount) {
        if (this.mode != 2) {
            this.myEssentia.add(tag, amount);
        } else {
            this.essentiaDemanded.reduce(tag, amount);
        }
        this.clientEssentiaFX(tag);
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        this.markDirty();
        return 0;
    }

    public void clientEssentiaFX(final Aspect tag) {
        PacketHandler.INSTANCE.sendToAllAround(
                new PacketFXEssentiaBubble(this.pos.getX() + 0.5, this.pos.getY() - 2, this.pos.getZ() + 0.5, tag.getColor()),
                new NetworkRegistry.TargetPoint(
                        this.getWorld().provider.getDimension(),
                        this.pos.getX(),
                        this.pos.getY(),
                        this.pos.getZ(),
                        32.0));
    }

    @Override
    public boolean takeFromContainer(final Aspect tag, final int amount) {
        return false;
    }

    @Override
    public boolean takeFromContainer(final AspectList ot) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(final Aspect tag, final int amount) {
        return this.containerContains(tag) >= amount;
    }

    @Override
    public boolean doesContainerContain(final AspectList ot) {
        return false;
    }

    @Override
    public int containerContains(final Aspect tag) {
        return this.myEssentia.getAmount(tag);
    }

    public EntityLivingBase getEntityContained() {
        return this.entityContained;
    }

    public void setEntityContained(final EntityLivingBase newEntity) {
        this.entityContained = newEntity;
        if (this.entityContained != null) {
            this.entityContained
                    .setLocationAndAngles(this.pos.getX() + 0.5, this.pos.getY() - 1.75, this.pos.getZ() + 0.5, 0.0f, 0.0f);
        }
    }

    public static class SourceFX {

        public BlockPos loc;
        public int ticks;
        public int color;
        public int entity;

        public SourceFX(final BlockPos loc, final int ticks, final int color) {
            this.loc = loc;
            this.ticks = ticks;
            this.color = color;
        }
    }
}
