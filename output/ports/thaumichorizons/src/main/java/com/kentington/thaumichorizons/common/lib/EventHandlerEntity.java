//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib;
import net.minecraft.util.math.BlockPos;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityGolemTH;
import com.kentington.thaumichorizons.common.entities.EntityMeatSlime;
import com.kentington.thaumichorizons.common.entities.EntityMercurialSlime;
import com.kentington.thaumichorizons.common.entities.EntityNightmare;
import com.kentington.thaumichorizons.common.entities.EntityVoltSlime;
import com.kentington.thaumichorizons.common.entities.ai.EntityAIAttackOnCollideTH;
import com.kentington.thaumichorizons.common.entities.ai.EntityAIFollowOwnerTH;
import com.kentington.thaumichorizons.common.entities.ai.EntityAIHurtByTargetTH;
import com.kentington.thaumichorizons.common.entities.ai.EntityAIOwnerHurtByTargetTH;
import com.kentington.thaumichorizons.common.entities.ai.EntityAIOwnerHurtTargetTH;
import com.kentington.thaumichorizons.common.entities.ai.EntityAISitTH;
import com.kentington.thaumichorizons.common.entities.ai.EntityAIWanderTH;
import com.kentington.thaumichorizons.common.items.ItemAmuletMirror;
import com.kentington.thaumichorizons.common.items.ItemFocusContainment;
import com.kentington.thaumichorizons.common.lib.networking.PacketFXContainment;
import com.kentington.thaumichorizons.common.lib.networking.PacketHandler;
import com.kentington.thaumichorizons.common.lib.networking.PacketNoMoreItems;
import com.kentington.thaumichorizons.common.lib.networking.PacketPlayerInfusionSync;
import com.kentington.thaumichorizons.common.tiles.TileSoulBeacon;
import com.kentington.thaumichorizons.common.tiles.TileVat;

import baubles.api.cap.IBaublesItemHandler;
import baubles.api.BaublesApi;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.EntityFollowingItem;
import thaumcraft.common.entities.EntityPermanentItem;
import thaumcraft.common.items.relics.ItemHandMirror;
import thaumcraft.common.lib.network.fx.PacketFXShield;
import thaumcraft.common.lib.utils.EntityUtils;

public class EventHandlerEntity {

    @SideOnly(Side.CLIENT)
    public static int clientNightmareID;

    @SideOnly(Side.CLIENT)
    public static int clientPlayerID;

    @SubscribeEvent
    public void EntityJoinWorld(final EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            this.applyInfusions((EntityLivingBase) event.getEntity());
        }
        if (event.getWorld().isRemote && event.getEntity() instanceof EntityNightmare
                && event.getEntity().getEntityId() == EventHandlerEntity.clientNightmareID) {
            Entity rider = event.getWorld().getEntityByID(EventHandlerEntity.clientPlayerID);
            if (rider != null) rider.startRiding(event.getEntity(), true);
            EventHandlerEntity.clientNightmareID = -2;
            EventHandlerEntity.clientPlayerID = -2;
        }
    }

    @SubscribeEvent
    public void Respawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
        // Exit Portal fix
        if (!event.player.world.isRemote) {
            this.applyInfusions(event.player);
        }
    }

    public void applyInfusions(final EntityLivingBase entity) {
        EntityInfusionProperties infusionProperties = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(entity);
        if (entity instanceof EntityPlayer) {
            final int[] infusions = infusionProperties.getPlayerInfusions();
            for (int infusion : infusions) {
                if (infusion != 0) {
                    if (infusion == 8 && !entity.world.isRemote) {
                        this.warpTumor(
                                (EntityPlayer) entity,
                                ThaumicHorizons.warpedTumorValue - infusionProperties.tumorWarpPermanent
                                        - infusionProperties.tumorWarp
                                        - infusionProperties.tumorWarpTemp);
                    }
                }
            }
            this.applyPlayerPotionInfusions((EntityPlayer) entity, infusions, infusionProperties.toggleInvisible);
            if (!entity.world.isRemote) {
                PacketHandler.INSTANCE.sendToAll(
                        new PacketPlayerInfusionSync(
                                entity.getName(),
                                infusions,
                                infusionProperties.toggleClimb,
                                infusionProperties.toggleInvisible));
            }
        } else {
            final int[] infusions = infusionProperties.getInfusions();
            for (int i = 0; i < EntityInfusionProperties.NUM_INFUSIONS; ++i) {
                if (infusions[i] != 0) {
                    if (infusions[i] == 1) {
                        final PotionEffect effect = new PotionEffect(net.minecraft.init.MobEffects.JUMP_BOOST, Integer.MAX_VALUE, 0, true, true);
                        effect.setCurativeItems(new ArrayList<>());
                        entity.addPotionEffect(effect);
                    } else if (infusions[i] == 3) {
                        final PotionEffect effect = new PotionEffect(
                                net.minecraft.init.MobEffects.REGENERATION,
                                Integer.MAX_VALUE,
                                0,
                                true, true);
                        effect.setCurativeItems(new ArrayList<>());
                        entity.addPotionEffect(effect);
                    } else if (infusions[i] == 4) {
                        final PotionEffect effect = new PotionEffect(net.minecraft.init.MobEffects.RESISTANCE, Integer.MAX_VALUE, 0, true, true);
                        effect.setCurativeItems(new ArrayList<>());
                        entity.addPotionEffect(effect);
                        if (entity.world.isRemote) {
                            ThaumicHorizons.instance.renderEventHandler.thingsThatSparkle.add(entity);
                        }
                    } else if (infusions[i] == 8 && !entity.getEntityData().hasKey("runicCharge")) {
                        entity.getEntityData().setInteger("runicCharge", 6);
                    } else if (infusions[i] == 7 && entity instanceof EntityLiving) {
                        this.applyNewAI((EntityLiving) entity);
                    }
                }
            }
        }
    }

    public void applyPlayerPotionInfusions(final EntityPlayer entity, final int[] infusions, final boolean toggled) {
        for (int infusion : infusions) {
            if (infusion == 1) {
                PotionEffect effect = new PotionEffect(net.minecraft.init.MobEffects.JUMP_BOOST, Integer.MAX_VALUE, 0, true, true);
                effect.setCurativeItems(new ArrayList<>());
                entity.addPotionEffect(effect);
                effect = new PotionEffect(net.minecraft.init.MobEffects.SPEED, Integer.MAX_VALUE, 0, true, true);
                effect.setCurativeItems(new ArrayList<>());
                entity.addPotionEffect(effect);
            } else if (infusion == 3) {
                final PotionEffect effect = new PotionEffect(net.minecraft.init.MobEffects.REGENERATION, Integer.MAX_VALUE, 0, true, true);
                effect.setCurativeItems(new ArrayList<>());
                entity.addPotionEffect(effect);
            } else if (infusion == 4) {
                final PotionEffect effect = new PotionEffect(net.minecraft.init.MobEffects.RESISTANCE, Integer.MAX_VALUE, 0, true, true);
                effect.setCurativeItems(new ArrayList<>());
                entity.addPotionEffect(effect);
            } else if (infusion == 10 && !toggled) {
                final PotionEffect effect = new PotionEffect(net.minecraft.init.MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, true, true);
                effect.setCurativeItems(new ArrayList<>());
                entity.addPotionEffect(effect);
                entity.setInvisible(true);
            }
        }
    }

    public void warpTumor(final EntityPlayer entity, int capacity) {
        if (entity.world.isRemote || capacity <= 0) {
            return;
        }
        final int warpPermanent = Thaumcraft.proxy.getPlayerKnowledge(entity).getWarpPerm();
        final int warp = Thaumcraft.proxy.getPlayerKnowledge(entity).getWarpSticky();
        final int tempWarp = Thaumcraft.proxy.getPlayerKnowledge(entity).getWarpTemp();
        if (warpPermanent > capacity) {
            Thaumcraft.proxy.getPlayerKnowledge(entity).addWarpPerm( -capacity);
            final EntityInfusionProperties entityInfusionProperties = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(entity);
            entityInfusionProperties.tumorWarpPermanent += capacity;
            capacity = 0;
        } else {
            capacity -= warpPermanent;
            Thaumcraft.proxy.getPlayerKnowledge(entity).addWarpPerm( -warpPermanent);
            final EntityInfusionProperties entityInfusionProperties2 = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(entity);
            entityInfusionProperties2.tumorWarpPermanent += warpPermanent;
            if (warp > capacity) {
                Thaumcraft.proxy.getPlayerKnowledge(entity).addWarpSticky( -capacity);
                final EntityInfusionProperties entityInfusionProperties3 = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(entity);
                entityInfusionProperties3.tumorWarp += capacity;
                capacity = 0;
            } else {
                capacity -= warp;
                Thaumcraft.proxy.getPlayerKnowledge(entity).addWarpSticky( -warp);
                final EntityInfusionProperties entityInfusionProperties4 = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(entity);
                entityInfusionProperties4.tumorWarp += warp;
                if (tempWarp > capacity) {
                    Thaumcraft.proxy.getPlayerKnowledge(entity).addWarpTemp( -capacity);
                    final EntityInfusionProperties entityInfusionProperties5 = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(entity);
                    entityInfusionProperties5.tumorWarpTemp += capacity;
                    capacity = 0;
                } else {
                    capacity -= tempWarp;
                    Thaumcraft.proxy.getPlayerKnowledge(entity).addWarpTemp( -tempWarp);
                    final EntityInfusionProperties entityInfusionProperties6 = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(entity);
                    entityInfusionProperties6.tumorWarpTemp += tempWarp;
                }
            }
        }
        thaumcraft.common.lib.research.ResearchManager.syncWarp(entity);
    }

    public void applyNewAI(final EntityLiving entity) {
        removeTasks(entity.tasks);
        removeTasks(entity.targetTasks);

        entity.tasks.addTask(1, new EntityAISwimming(entity));
        entity.tasks.addTask(2, new EntityAISitTH(entity));
        entity.tasks.addTask(3, new EntityAILeapAtTarget(entity, 0.4f));
        entity.tasks.addTask(4, new EntityAIAttackOnCollideTH(entity, 1.0, true));
        entity.tasks.addTask(5, new EntityAIFollowOwnerTH(entity, 1.0, 10.0f, 2.0f));
        if (entity instanceof EntityAnimal) {
            entity.tasks.addTask(6, new EntityAIMate((EntityAnimal) entity, 1.0));
        }
        entity.tasks.addTask(7, new EntityAIWanderTH(entity, 1.0));
        entity.tasks.addTask(9, new EntityAIWatchClosest(entity, EntityPlayer.class, 8.0f));
        entity.tasks.addTask(9, new EntityAILookIdle(entity));
        entity.targetTasks.addTask(1, new EntityAIOwnerHurtByTargetTH(entity));
        entity.targetTasks.addTask(2, new EntityAIOwnerHurtTargetTH(entity));
        entity.targetTasks.addTask(3, new EntityAIHurtByTargetTH(entity, true));
    }

    private void removeTasks(EntityAITasks entityAITasks) {
        final ArrayList<Object> taskEntries = new ArrayList<Object>(entityAITasks.taskEntries);
        for (Object task : taskEntries) {
            EntityAITasks.EntityAITaskEntry taskEntry = (EntityAITasks.EntityAITaskEntry) task;
            entityAITasks.removeTask(taskEntry.action);
        }
    }

    @SubscribeEvent
    public void livingTick(final LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof EntityPlayer) { EntityPlayer player = (EntityPlayer) event.getEntity();
            final String pp = "R" + player.getDisplayName();
            if (ItemFocusContainment.hitCritters.containsKey(pp)) {
                ItemFocusContainment.contain.put(pp, ItemFocusContainment.contain.get(pp) - 1.0f);
                if (ItemFocusContainment.contain.get(pp) > 0.0f) {
                    final Entity ent = ItemFocusContainment.hitCritters.get(pp);
                    ThaumicHorizons.proxy.containmentFX(
                            ent.posX,
                            ent.posY,
                            ent.posZ,
                            player,
                            ent,
                            (int) (ItemFocusContainment.contain.get(pp) / ((EntityLiving) ent).getHealth()) / 3 + 1);
                } else {
                    ItemFocusContainment.contain.remove(pp);
                    ItemFocusContainment.hitCritters.remove(pp);
                }
            }
            final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player);
            if (prop.hasPlayerInfusion(5) && (player.getActivePotionEffect(net.minecraft.init.MobEffects.POISON) != null
                    || player.getActivePotionEffect(net.minecraft.init.MobEffects.WITHER) != null
                    || player.getActivePotionEffect(Config.potionInfectiousVisExhaust) != null
                    || player.getActivePotionEffect(Config.potionVisExhaust) != null
                    || player.getActivePotionEffect(Config.potionThaumarhia) != null
                    || player.getActivePotionEffect(Config.potionFluxTaint) != null)) {

                final Collection activePotionEffects = event.getEntityLiving().getActivePotionEffects();
                final ArrayList<PotionEffect> toAdd = new ArrayList<>();

                for (Object activePotionEffect : activePotionEffects) {
                    final PotionEffect effect = (PotionEffect) activePotionEffect;
                    if (effect.getPotion() == net.minecraft.init.MobEffects.POISON || effect.getPotion() == net.minecraft.init.MobEffects.WITHER
                            || effect.getPotion() == Config.potionInfectiousVisExhaust
                            || effect.getPotion() == Config.potionFluxTaint
                            || effect.getPotion() == Config.potionVisExhaust
                            || effect.getPotion() == Config.potionThaumarhia) {
                        final Potion id = effect.getPotion();
                        final int amplifier = 0;
                        final int duration = effect.getDuration() - 1;
                        toAdd.add(new PotionEffect(id, duration, amplifier, false, true));
                    } else {
                        toAdd.add(effect);
                    }
                }
                event.getEntityLiving().clearActivePotions();
                for (final PotionEffect effect : toAdd) {
                    event.getEntityLiving().addPotionEffect(effect);
                }
            }
            if (prop.hasPlayerInfusion(6) && event.getEntity().ticksExisted % 200 == 0
                    && player.getFoodStats().needFood()
                    && player.world.isDaytime()
                    && player.world.canSeeSky(new net.minecraft.util.math.BlockPos(MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ)))) {
                player.getFoodStats().addStats(1, 0.0f);
            }
            if (prop.hasPlayerInfusion(7) && player.isInWater()) {
                player.setAir(300);
            }
            if (event.getEntityLiving().ticksExisted % 30 == 0) {
                if (prop.hasPlayerInfusion(8) && !event.getEntityLiving().world.isRemote) {
                    this.warpTumor(
                            (EntityPlayer) event.getEntityLiving(),
                            ThaumicHorizons.warpedTumorValue - prop.tumorWarpPermanent
                                    - prop.tumorWarp
                                    - prop.tumorWarpTemp);
                }
                this.applyPlayerPotionInfusions(player, prop.playerInfusions, prop.toggleInvisible);
            }
            if (prop.hasPlayerInfusion(9) && !prop.toggleClimb) {
                if (event.getEntityLiving().collidedHorizontally) {
                    event.getEntityLiving().motionY = 0.2;
                    if (event.getEntityLiving().isSneaking()) {
                        event.getEntityLiving().motionY = 0.0;
                    }
                    event.getEntity().fallDistance = 0.0f;
                } else {
                    final List listy = event.getEntityLiving().world.getCollisionBoxes(event.getEntityLiving(), 
                            new AxisAlignedBB(
                                    event.getEntityLiving().posX - event.getEntityLiving().width / 1.5,
                                    event.getEntityLiving().posY,
                                    event.getEntityLiving().posZ - event.getEntityLiving().width / 1.5,
                                    event.getEntityLiving().posX + event.getEntityLiving().width / 1.5,
                                    event.getEntityLiving().posY + event.getEntityLiving().height * 0.9,
                                    event.getEntityLiving().posZ + event.getEntityLiving().width / 1.5));
                    if (listy.size() > 0) {
                        if (event.getEntityLiving().isSneaking()) {
                            event.getEntityLiving().motionY = 0.0;
                        } else {
                            event.getEntityLiving().motionY = -0.15;
                        }
                        event.getEntity().fallDistance = 0.0f;
                    }
                }
            }
        }
        if (event.getEntityLiving().ticksExisted % 30 == 0) {
            final boolean shock = ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(event.getEntityLiving())).hasInfusion(6);
            if (shock && event.getEntityLiving().getRevengeTarget() != null) {
                event.getEntityLiving().getRevengeTarget().attackEntityFrom(DamageSource.MAGIC, 1.0f);
                Thaumcraft.proxy.arcLightning(
                        event.getEntityLiving().world,
                        event.getEntityLiving().posX,
                        event.getEntityLiving().posY + event.getEntityLiving().height / 2.0f,
                        event.getEntityLiving().posZ,
                        event.getEntityLiving().getRevengeTarget().posX,
                        event.getEntityLiving().getRevengeTarget().posY + event.getEntityLiving().getRevengeTarget().height / 2.0f,
                        event.getEntityLiving().getRevengeTarget().posZ,
                        0.2f,
                        0.8f,
                        0.8f,
                        1.0f);
                com.kentington.thaumichorizons.common.lib.THSoundBridge.playAtEntity(event.getEntityLiving().world, event.getEntityLiving(), "thaumcraft:zap", 1.0f, 1.0f + (event.getEntityLiving().world.rand.nextFloat()
                                - event.getEntityLiving().world.rand.nextFloat()) * 0.2f);
            } else if (shock && event.getEntityLiving() instanceof EntityLiving
                    && ((EntityLiving) event.getEntityLiving()).getAttackTarget() != null) {
                        ((EntityLiving) event.getEntityLiving()).getAttackTarget()
                                .attackEntityFrom(DamageSource.MAGIC, 1.0f);
                        Thaumcraft.proxy.arcLightning(
                                event.getEntityLiving().world,
                                event.getEntityLiving().posX,
                                event.getEntityLiving().posY + event.getEntityLiving().height / 2.0f,
                                event.getEntityLiving().posZ,
                                ((EntityLiving) event.getEntityLiving()).getAttackTarget().posX,
                                ((EntityLiving) event.getEntityLiving()).getAttackTarget().posY
                                        + ((EntityLiving) event.getEntityLiving()).getAttackTarget().height / 2.0f,
                                ((EntityLiving) event.getEntityLiving()).getAttackTarget().posZ,
                                0.2f,
                                0.8f,
                                0.8f,
                                1.0f);
                        com.kentington.thaumichorizons.common.lib.THSoundBridge.playAtEntity(event.getEntityLiving().world, event.getEntityLiving(), "thaumcraft:zap", 1.0f, 1.0f + (event.getEntityLiving().world.rand.nextFloat()
                                        - event.getEntityLiving().world.rand.nextFloat()) * 0.2f);
                    }
        }
        if (event.getEntityLiving() instanceof EntityVoltSlime && event.getEntityLiving().ticksExisted % 2 == 0
                && event.getEntityLiving().getRevengeTarget() != null) {
            event.getEntityLiving().getRevengeTarget().attackEntityFrom(DamageSource.MAGIC, 0.5f);
            Thaumcraft.proxy.arcLightning(
                    event.getEntityLiving().world,
                    event.getEntityLiving().posX,
                    event.getEntityLiving().posY + event.getEntityLiving().height / 2.0f,
                    event.getEntityLiving().posZ,
                    event.getEntityLiving().getRevengeTarget().posX,
                    event.getEntityLiving().getRevengeTarget().posY + event.getEntityLiving().getRevengeTarget().height / 2.0f,
                    event.getEntityLiving().getRevengeTarget().posZ,
                    0.2f,
                    0.8f,
                    0.8f,
                    1.0f);
            com.kentington.thaumichorizons.common.lib.THSoundBridge.playAtEntity(event.getEntityLiving().world, event.getEntityLiving(), "thaumcraft:zap", 1.0f, 1.0f + (event.getEntityLiving().world.rand.nextFloat() - event.getEntityLiving().world.rand.nextFloat())
                            * 0.2f);
        }
        if (event.getEntityLiving().ticksExisted % 100 == 0) {
            final boolean runic = ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(event.getEntityLiving())).hasInfusion(8);
            if (runic) {
                int charge = event.getEntityLiving().getEntityData().getInteger("runicCharge") + 1;
                if (charge > 6) {
                    charge = 6;
                }
                event.getEntityLiving().getEntityData().setInteger("runicCharge", charge);
            }
        }
        if (((EntityLivingBase) event.getEntity()).isPotionActive(com.kentington.thaumichorizons.common.lib.potion.PotionShock.instance)) {
            final EntityLivingBase player2 = (EntityLivingBase) event.getEntity();
            final List<EntityLivingBase> stuff = EntityUtils.getEntitiesInRange(
                    player2.world,
                    player2.posX,
                    player2.posY,
                    player2.posZ,
                    player2,
                    EntityLivingBase.class,
                    10.0);
            if (stuff != null && stuff.size() > 0) {
                final int boost = player2.getActivePotionEffect(com.kentington.thaumichorizons.common.lib.potion.PotionShock.instance)
                        .getAmplifier();
                for (final Entity e : stuff) {
                    final int r = player2.world.rand.nextInt(1000);
                    if (r < 20 * (1 + boost) && !e.isDead && e instanceof EntityLivingBase) {
                        if (player2 instanceof EntityPlayer) {
                            e.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player2), 1.0f);
                        } else {
                            e.attackEntityFrom(DamageSource.MAGIC, 1.0f);
                        }
                        Thaumcraft.proxy.arcLightning(
                                player2.world,
                                player2.posX,
                                player2.posY + player2.height / 2.0f,
                                player2.posZ,
                                e.posX,
                                e.posY + e.height / 2.0f,
                                e.posZ,
                                0.2f,
                                0.8f,
                                0.8f,
                                1.0f);
                        com.kentington.thaumichorizons.common.lib.THSoundBridge.playAtEntity(player2.world, player2, "thaumcraft:zap", 1.0f, 1.0f + (player2.world.rand.nextFloat() - player2.world.rand.nextFloat()) * 0.2f);
                    }
                }
            }
        }
        if (((EntityLivingBase) event.getEntity()).isPotionActive(com.kentington.thaumichorizons.common.lib.potion.PotionVisRegen.instance)) {
            final EntityLivingBase target = (EntityLivingBase) event.getEntity();
            final int boost2 = target.getActivePotionEffect(com.kentington.thaumichorizons.common.lib.potion.PotionVisRegen.instance)
                    .getAmplifier();
            if (event.getEntity().ticksExisted % Math.max(1, 20 - 3 * boost2) == 0) {
                Aspect aspect = null;
                switch (target.world.rand.nextInt(6)) {
    case 0: {
                        aspect = Aspect.AIR;
                    } break;
    case 1: {
                        aspect = Aspect.EARTH;
                    } break;
    case 2: {
                        aspect = Aspect.FIRE;
                    } break;
    case 3: {
                        aspect = Aspect.WATER;
                    } break;
    case 4: {
                        aspect = Aspect.ORDER;
                    } break;
    case 5: {
                        aspect = Aspect.ENTROPY;
                    } break;
}
                if (aspect != null) {
                    final EntityAspectOrb orb = new EntityAspectOrb(
                            target.world,
                            target.posX,
                            target.posY,
                            target.posZ,
                            aspect,
                            1);
                    target.world.spawnEntity(orb);
                }
            }
        }
        if (((EntityLivingBase) event.getEntity()).isPotionActive(com.kentington.thaumichorizons.common.lib.potion.PotionVacuum.instance)) {
            final EntityLivingBase player2 = (EntityLivingBase) event.getEntity();
            final int boost2 = player2.getActivePotionEffect(com.kentington.thaumichorizons.common.lib.potion.PotionVacuum.instance)
                    .getAmplifier();
            final List<EntityItem> stuff2 = EntityUtils.getEntitiesInRange(
                    player2.world,
                    player2.posX,
                    player2.posY,
                    player2.posZ,
                    player2,
                    EntityItem.class,
                    10.0 + 2 * boost2);
            if (stuff2 != null && stuff2.size() > 0) {
                for (final Entity e : stuff2) {
                    if ((!(e instanceof EntityFollowingItem) || ((EntityFollowingItem) e).target == null) && !e.isDead
                            && e instanceof EntityItem) {
                        double d6 = e.posX - player2.posX;
                        double d7 = e.posY - player2.posY + player2.height / 2.0f;
                        double d8 = e.posZ - player2.posZ;
                        final double d9 = MathHelper.sqrt(d6 * d6 + d7 * d7 + d8 * d8);
                        d6 /= d9;
                        d7 /= d9;
                        d8 /= d9;
                        final double d10 = 0.1;
                        e.motionX -= d6 * d10;
                        e.motionY -= d7 * d10;
                        e.motionZ -= d8 * d10;
                        if (e.motionX > 0.35) {
                            e.motionX = 0.35;
                        }
                        if (e.motionX < -0.35) {
                            e.motionX = -0.35;
                        }
                        if (e.motionY > 0.35) {
                            e.motionY = 0.35;
                        }
                        if (e.motionY < -0.35) {
                            e.motionY = -0.35;
                        }
                        if (e.motionZ > 0.35) {
                            e.motionZ = 0.35;
                        }
                        if (e.motionZ < -0.35) {
                            e.motionZ = -0.35;
                        }
                        Thaumcraft.proxy.spark(
                                (float) e.posX + (player2.world.rand.nextFloat() - player2.world.rand.nextFloat())
                                        * 0.125f,
                                (float) e.posY + (player2.world.rand.nextFloat() - player2.world.rand.nextFloat())
                                        * 0.125f,
                                (float) e.posZ + (player2.world.rand.nextFloat() - player2.world.rand.nextFloat())
                                        * 0.125f,
                                1.0f,
                                0.25f,
                                0.25f,
                                0.25f,
                                1.0f);
                    }
                }
            }
        }
        if (((EntityLivingBase) event.getEntity()).isPotionActive(com.kentington.thaumichorizons.common.lib.potion.PotionSynthesis.instance)) {
            final EntityLivingBase player2 = (EntityLivingBase) event.getEntity();
            final int boost2 = player2.getActivePotionEffect(com.kentington.thaumichorizons.common.lib.potion.PotionSynthesis.instance)
                    .getAmplifier();
            if (event.getEntity().ticksExisted % Math.max(1, 15 - 2 * boost2) == 0 && player2.world.isDaytime()
                    && player2.world.canSeeSky(new net.minecraft.util.math.BlockPos(MathHelper.floor(player2.posX), MathHelper.floor(player2.posY), MathHelper.floor(player2.posZ)))) {
                player2.heal(0.5f);
                if (player2 instanceof EntityPlayer) {
                    ((EntityPlayer) player2).getFoodStats().addStats(1, 0.5f);
                }
            }
        }
    }

    @SubscribeEvent
    public void livingDeath(final LivingDeathEvent event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (event.getEntityLiving() instanceof EntityMeatSlime
                    && ((EntityMeatSlime) event.getEntityLiving()).getSlimeSize() == 1) {
                switch (event.getEntityLiving().world.rand.nextInt(5)) {
    case 0: {
                        event.getEntityLiving().entityDropItem(new ItemStack(Items.BEEF), 0.0f);
                    } break;
    case 1: {
                        event.getEntityLiving().entityDropItem(new ItemStack(Items.PORKCHOP), 0.0f);
                    } break;
    case 2: {
                        event.getEntityLiving().entityDropItem(new ItemStack(Items.CHICKEN), 0.0f);
                    } break;
    case 3: {
                        event.getEntityLiving().entityDropItem(new ItemStack(Items.FISH), 0.0f);
                    } break;
    default: {
                        event.getEntityLiving().entityDropItem(new ItemStack(Items.ROTTEN_FLESH), 0.0f);
                    } break;
}
            } else if (event.getEntityLiving() instanceof EntityMercurialSlime
                    && ((EntityMercurialSlime) event.getEntityLiving()).getSlimeSize() == 1) {
                        event.getEntityLiving().entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 3), 0.0f);
                    } else
                if (event.getEntityLiving() instanceof EntityPlayer) {
                    final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(event.getEntity());
                    if (prop != null) {
                        if (prop.tumorWarpPermanent > 0 || prop.tumorWarp > 0 || prop.tumorWarpTemp > 0) {
                            Thaumcraft.proxy.getPlayerKnowledge((EntityPlayer) event.getEntity()).addWarpPerm( prop.tumorWarpPermanent);
                            Thaumcraft.proxy.getPlayerKnowledge((EntityPlayer) event.getEntity()).addWarpSticky( prop.tumorWarp);
                            Thaumcraft.proxy.getPlayerKnowledge((EntityPlayer) event.getEntity()).addWarpTemp( prop.tumorWarpTemp);
                        }
                        prop.resetPlayerInfusions();
                    }
                }
        }
    }

    @SubscribeEvent
    public void onPlayerHurt(final LivingHurtEvent event) {
        final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(event.getEntity());
        if (prop.hasPlayerInfusion(5) && event.getSource() == DamageSourceThaumcraft.taint) {
            event.setCanceled(true);
            event.setAmount(0.0f);
            return;
        }
        if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayer
                && event.getEntityLiving().getHealth() - event.getAmount() <= 0.0f) {
            final EntityPlayer player = ((EntityPlayer) (event.getEntity()));

            // Clear player infusions
            if (prop.tumorWarpPermanent > 0 || prop.tumorWarp > 0 || prop.tumorWarpTemp > 0) {
                Thaumcraft.proxy.getPlayerKnowledge((EntityPlayer) event.getEntity()).addWarpPerm( prop.tumorWarpPermanent);
                Thaumcraft.proxy.getPlayerKnowledge((EntityPlayer) event.getEntity()).addWarpSticky( prop.tumorWarp);
                Thaumcraft.proxy.getPlayerKnowledge((EntityPlayer) event.getEntity()).addWarpTemp( prop.tumorWarpTemp);
            }
            prop.resetPlayerInfusions();

            // Mirrored Amulet returning items
            ItemStack amulet = null;
            for (ItemStack bauble : BaubleInventory.contents(player)) {
                if (bauble != null && bauble.getItem() instanceof ItemAmuletMirror) {
                    amulet = bauble;
                    break;
                }
            }
            if (amulet != null) {
                boolean transportedSomething = false;
                for (int i = 0; i < player.inventory.armorInventory.size(); ++i) {
                    final ItemStack item = player.inventory.armorInventory.get(i);
                    if (!item.isEmpty() && ItemHandMirror.transport(amulet, item, player, player.world)) {
                        transportedSomething = true;
                        player.inventory.armorInventory.set(i, ItemStack.EMPTY);
                    }
                }
                for (int i = 0; i < player.inventory.mainInventory.size(); ++i) {
                    final ItemStack item = player.inventory.mainInventory.get(i);
                    if (!item.isEmpty() && ItemHandMirror.transport(amulet, item, player, player.world)) {
                        transportedSomething = true;
                        player.inventory.mainInventory.set(i, ItemStack.EMPTY);
                    }
                }
                IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
                int amuletIndex = 0;
                for (int i = 0; i < baubles.getSlots(); ++i) {
                    final ItemStack item = baubles.getStackInSlot(i);
                    if (item == amulet) {
                        amuletIndex = i;
                    } else if (!item.isEmpty() && ItemHandMirror.transport(amulet, item, player, player.world)) {
                        transportedSomething = true;
                        baubles.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }

                if (transportedSomething) {
                    baubles.setStackInSlot(amuletIndex, ItemStack.EMPTY);
    
                    PacketHandler.INSTANCE.sendToAllAround(
                            new PacketFXContainment(
                                    player.posX,
                                    player.posY + player.getEyeHeight(),
                                    player.posZ),
                            new NetworkRegistry.TargetPoint(
                                    player.world.provider.getDimension(),
                                    player.posX,
                                    player.posY,
                                    player.posZ,
                                    32.0));
                    com.kentington.thaumichorizons.common.lib.THSoundBridge.play(player.world, player.posX, player.posY + player.getEyeHeight(), player.posZ, "thaumcraft:craftfail", 1.0f, 1.0f);
                    player.inventory.markDirty();
                    final ItemStack droppedPearl = new ItemStack(ConfigItems.itemEldritchObject, 1, 3);
                    final EntityPermanentItem drop = new EntityPermanentItem(
                            player.world,
                            player.posX,
                            player.posY,
                            player.posZ,
                            droppedPearl);
                    player.world.spawnEntity(drop);
                }
            }

            // Return to Soul Beacon before death
            if (player.getEntityData().getBoolean("soulBeacon")) {
                final int dim = player.getEntityData().getInteger("soulBeaconDim");
                final World beaconWorld = player.getServer().getWorld(dim);
                final World playerWorld = player.world;
                final int x = player.getEntityData().getIntArray("soulBeaconCoords")[0];
                final int y = player.getEntityData().getIntArray("soulBeaconCoords")[1];
                final int z = player.getEntityData().getIntArray("soulBeaconCoords")[2];
                if (beaconWorld != null && beaconWorld.getTileEntity(new BlockPos(x, y, z)) instanceof TileSoulBeacon
                        && beaconWorld.getTileEntity(new net.minecraft.util.math.BlockPos(x, y - 1, z)) instanceof TileVat
                        && ((TileVat) (beaconWorld.getTileEntity(new net.minecraft.util.math.BlockPos(x, y - 1, z)))).mode == 4) {
            final TileVat vat = ((TileVat) (beaconWorld.getTileEntity(new net.minecraft.util.math.BlockPos(x, y - 1, z))));

                    event.setCanceled(true);
                    event.setAmount(0.0f);
                    playerWorld.createExplosion(
                            null,
                            player.posX,
                            player.posY + player.getEyeHeight(),
                            player.posZ,
                            0.0f,
                            false);
                    for (int i = 0; i < 25; ++i) {
                        final int xx = (int) player.posX + playerWorld.rand.nextInt(8) - playerWorld.rand.nextInt(8);
                        final int yy = (int) player.posY + playerWorld.rand.nextInt(8) - playerWorld.rand.nextInt(8);
                        final int zz = (int) player.posZ + playerWorld.rand.nextInt(8) - playerWorld.rand.nextInt(8);
                        if (playerWorld.isAirBlock(new BlockPos(xx, yy, zz))) {
                            if (yy <= (int) player.posY + 1) {
                                playerWorld.setBlockState(new net.minecraft.util.math.BlockPos(xx, yy, zz), (ConfigBlocks.blockFluxGoo).getStateFromMeta(8), 3);
                            } else {
                                playerWorld.setBlockState(new net.minecraft.util.math.BlockPos(xx, yy, zz), (ConfigBlocks.blockFluxGas).getStateFromMeta(8), 3);
                            }
                        }
                    }
                    player.inventory.dropAllItems();
                    for (ItemStack bauble : BaubleInventory.contents(player)) {
                        if (bauble == null) {
                            continue;
                        }
                        player.dropItem(bauble, true, false);
                    }
                    BaubleInventory.clear(player);
                    player.inventory.markDirty();
                    PacketHandler.INSTANCE.sendTo(new PacketNoMoreItems(), (EntityPlayerMP) player);
                    player.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
                    player.heal(Float.MAX_VALUE);
                    if (dim != player.world.provider.getDimension()) {
                        player.changeDimension(dim);
                    }
                    player.setPositionAndUpdate(x + 0.5, y - 2.5, z + 0.5);
                    Thaumcraft.proxy.blockSparkle(beaconWorld, x, y - 2, z, 16777215, 20);
                    Thaumcraft.proxy.blockSparkle(beaconWorld, x, y - 3, z, 16777215, 20);
                    com.kentington.thaumichorizons.common.lib.THSoundBridge.play(beaconWorld, x + 0.5, y + 0.5, z + 0.5, "thaumcraft:whispers", 1.0f, beaconWorld.rand.nextFloat());
                    this.applyPlayerInfusions(player, vat);
                    vat.selfInfusions = new int[12];
                    vat.mode = 0;
                    vat.setEntityContained(player);
                    beaconWorld.getTileEntity(new net.minecraft.util.math.BlockPos(x, y - 1, z)).markDirty();
                    player.world.notifyBlockUpdate(new BlockPos(x, y - 1, z), player.world.getBlockState(new BlockPos(x, y - 1, z)), player.world.getBlockState(new BlockPos(x, y - 1, z)), 3);
                }
            }
        }
    }

    void applyPlayerInfusions(final EntityPlayer player, final TileVat tile) {
        final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(player);
        for (int i = 0; i < tile.selfInfusions.length; ++i) {
            if (tile.selfInfusions[i] != 0) {
                prop.addPlayerInfusion(tile.selfInfusions[i]);
            }
        }
        this.applyInfusions(player);
    }

    @SubscribeEvent
    public void onAttack(final LivingAttackEvent event) {
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityLivingBase) {
            final boolean poison = ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(event.getEntityLiving())).hasInfusion(9);
            if (poison) {
                event.getEntityLiving().addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.POISON, 40, 0, false, true));
            }
        }
        if (!event.getSource().isProjectile()) {
            return;
        }
        final boolean ender = ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(event.getEntityLiving()))
                .hasInfusion(5);
        if (!ender) {
            return;
        }
        event.setCanceled(true);
        this.teleport(event.getEntityLiving());
    }

    public void teleport(final EntityLivingBase entity) {
        if (entity.world.isRemote) return;
        final EnderTeleportEvent event = new EnderTeleportEvent(
                entity,
                entity.posX + (entity.world.rand.nextDouble() - 0.5) * 64.0,
                entity.posY + (entity.world.rand.nextInt(64) - 32),
                entity.posZ + (entity.world.rand.nextDouble() - 0.5) * 64.0,
                0.0f);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return;
        }
        final double d3 = entity.posX;
        final double d4 = entity.posY;
        final double d5 = entity.posZ;
        entity.posX = event.getTargetX();
        entity.posY = event.getTargetY();
        entity.posZ = event.getTargetZ();
        boolean flag = false;
        final int i = MathHelper.floor(entity.posX);
        int j = MathHelper.floor(entity.posY);
        final int k = MathHelper.floor(entity.posZ);
        if (entity.world.isBlockLoaded(new net.minecraft.util.math.BlockPos(i, j, k))) {
            boolean flag2 = false;
            while (!flag2 && j > 0) {
                final Block block = entity.world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
                if (entity.world.getBlockState(new BlockPos(i, j - 1, k)).getMaterial().blocksMovement()) {
                    flag2 = true;
                } else {
                    --entity.posY;
                    --j;
                }
            }
            if (flag2) {
                entity.setPosition(entity.posX, entity.posY, entity.posZ);
                if (entity.world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty()
                        && !entity.world.containsAnyLiquid(entity.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }
        if (!flag) {
            entity.setPosition(d3, d4, d5);
            return;
        }
        final short short1 = 128;
        for (int l = 0; l < short1; ++l) {
            final double d6 = l / (short1 - 1.0);
            final float f = (entity.world.rand.nextFloat() - 0.5f) * 0.2f;
            final float f2 = (entity.world.rand.nextFloat() - 0.5f) * 0.2f;
            final float f3 = (entity.world.rand.nextFloat() - 0.5f) * 0.2f;
            final double d7 = d3 + (entity.posX - d3) * d6
                    + (entity.world.rand.nextDouble() - 0.5) * entity.width * 2.0;
            final double d8 = d4 + (entity.posY - d4) * d6 + entity.world.rand.nextDouble() * entity.height;
            final double d9 = d5 + (entity.posZ - d5) * d6
                    + (entity.world.rand.nextDouble() - 0.5) * entity.width * 2.0;
            entity.world.spawnParticle(net.minecraft.util.EnumParticleTypes.PORTAL, d7, d8, d9, f, f2, f3);
        }
        com.kentington.thaumichorizons.common.lib.THSoundBridge.play(entity.world, d3, d4, d5, "mob.endermen.portal", 1.0f, 1.0f);
        entity.playSound(com.kentington.thaumichorizons.common.lib.THSoundBridge.lookup("mob.endermen.portal"), 1.0f, 1.0f);
    }

    @SubscribeEvent
    public void entityHurt(final LivingHurtEvent event) {
        if (event.getEntityLiving().world.isRemote) return;
        final boolean runic = ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(event.getEntityLiving()))
                .hasInfusion(8);
        if (runic) {
            int charge = event.getEntityLiving().getEntityData().getInteger("runicCharge");
            if (charge <= 0 || event.getSource() == DamageSource.DROWN
                    || event.getSource() == DamageSource.WITHER
                    || event.getSource() == DamageSource.OUT_OF_WORLD
                    || event.getSource() == DamageSource.STARVE) {
                return;
            }
            int target = -1;
            if (event.getSource().getTrueSource() != null) {
                target = event.getSource().getTrueSource().getEntityId();
            }
            if (event.getSource() == DamageSource.FALL) {
                target = -2;
            }
            if (event.getSource() == DamageSource.FALLING_BLOCK) {
                target = -3;
            }
            thaumcraft.common.lib.network.PacketHandler.INSTANCE.sendToAllAround(
                    new PacketFXShield(event.getEntity().getEntityId(), target),
                    new NetworkRegistry.TargetPoint(
                            event.getEntity().world.provider.getDimension(),
                            event.getEntity().posX,
                            event.getEntity().posY,
                            event.getEntity().posZ,
                            64.0));
            if (charge > event.getAmount()) {
                charge -= (int) event.getAmount();
                event.setAmount(0.0f);
            } else {
                event.setAmount(event.getAmount() - charge);
                charge = 0;
            }
            event.getEntityLiving().getEntityData().setInteger("runicCharge", charge);
        }
    }

    @SubscribeEvent
    public void golemDies(final LivingDeathEvent event) {
        if (event.getEntity().world.isRemote) {
            return;
        }
        if (event.getEntity() instanceof EntityGolemTH) { EntityGolemTH golem = (EntityGolemTH) event.getEntity();
            golem.die();
        }
    }

    @SubscribeEvent
    public void sitStay(final EntityInteract event) {
        if (com.kentington.thaumichorizons.common.lib.InfusionCapability.get(event.getTarget()) != null) {
            final EntityInfusionProperties prop = (EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(event.getTarget());
            if (prop.hasInfusion(10) && event.getEntityPlayer().getHeldItemMainhand() != null
                    && event.getEntityPlayer().getHeldItemMainhand().getItem() == ConfigItems.itemWandCasting) {
                final ItemStack jar = new ItemStack(ThaumicHorizons.blockJar);
                final NBTTagCompound entityData = new NBTTagCompound();
                entityData.setString("id", EntityList.getEntityString(event.getTarget()));
                event.getTarget().writeToNBT(entityData);
                jar.setTagCompound(entityData);
                jar.getTagCompound().setString("jarredCritterName", event.getTarget().getName());
                jar.getTagCompound().setBoolean("isSoul", false);
                event.getTarget().entityDropItem(jar, 1.0f);
                if (!event.getTarget().world.isRemote) {
                    PacketHandler.INSTANCE.sendToAllAround(
                            new PacketFXContainment(
                                    event.getTarget().posX,
                                    event.getTarget().posY + event.getTarget().height / 2.0f,
                                    event.getTarget().posZ),
                            new NetworkRegistry.TargetPoint(
                                    event.getTarget().world.provider.getDimension(),
                                    event.getTarget().posX,
                                    event.getTarget().posY + event.getTarget().height / 2.0f,
                                    event.getTarget().posZ,
                                    32.0));
                }
                event.getTarget().world.removeEntity(event.getTarget());
            } else if (prop.hasInfusion(7) && event.getEntityPlayer().getName().equals(prop.getOwner())) {
                prop.setSitting(!prop.isSitting());
                if (event.getTarget().world.isRemote) {
                    if (prop.isSitting()) {
                        event.getEntityPlayer().sendMessage(
                                new TextComponentString(
                                        TextFormatting.ITALIC + ""
                                                + TextFormatting.GRAY
                                                + event.getTarget().getName()
                                                + " is waiting."));
                    } else {
                        event.getEntityPlayer().sendMessage(
                                new TextComponentString(
                                        TextFormatting.ITALIC + ""
                                                + TextFormatting.GRAY
                                                + event.getTarget().getName()
                                                + " will follow you."));
                    }
                }
            }
        }
    }
}
