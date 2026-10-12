package fox.spiteful.forbidden;
import net.minecraft.init.Enchantments;
import net.minecraft.util.math.BlockPos;


import java.util.*;

import fox.spiteful.forbidden.compat.BloodNetwork;
import baubles.api.BaublesApi;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import fox.spiteful.forbidden.blocks.ForbiddenBlocks;
import fox.spiteful.forbidden.compat.Compat;
import fox.spiteful.forbidden.items.tools.ItemRidingCrop;
import fox.spiteful.forbidden.items.baubles.ItemSubCollar;
import fox.spiteful.forbidden.items.tools.ItemTaintPickaxe;
import fox.spiteful.forbidden.potions.DarkPotions;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.init.Biomes;
import net.minecraft.init.MobEffects;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.Utils;

import fox.spiteful.forbidden.enchantments.DarkEnchantments;
import fox.spiteful.forbidden.items.ForbiddenItems;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FMEventHandler {
    Random randy = new Random();
    final Aspect[] primals = { Aspect.ENTROPY, Aspect.ORDER, Aspect.FIRE, Aspect.WATER, Aspect.EARTH, Aspect.AIR };
    HashMap<String, Integer> lastLP = new HashMap<String, Integer>();


    private static boolean isExtremeHills(String biomePath) {
        return biomePath.equals("extreme_hills") || biomePath.equals("smaller_extreme_hills")
                || biomePath.equals("mutated_extreme_hills") || biomePath.equals("extreme_hills_with_trees")
                || biomePath.equals("mutated_extreme_hills_with_trees");
    }

    @SubscribeEvent
    public void onPlayerBreaking(BreakSpeed event) {
        if (!event.getEntityPlayer().getHeldItemMainhand().isEmpty()) {
            ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
            if (stack.getItem() instanceof ItemTaintPickaxe) {
                World world = event.getEntityPlayer().world;
                int x = event.getPos().getX();
                int y = event.getPos().getY();
                int z = event.getPos().getZ();
                float hardness = event.getState().getBlockHardness(world, event.getPos());
                if (hardness == 0.0F)
                    event.setNewSpeed(0.0F);
                else if (hardness < 5.0F)
                    event.setNewSpeed(0.1F);
                else if (hardness < 20.0F)
                    event.setNewSpeed(hardness / 2.0F);
                else
                    event.setNewSpeed(5.0F + hardness);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntityLiving().world.isRemote) return;
        if (event.getSource().getDamageType().equals("player") && event.getEntityLiving() instanceof ITaintedMob)
        {
            PotionEffect effect = event.getEntityLiving().getActivePotionEffect(MobEffects.WEAKNESS);
            if (effect != null)
            {
                if (effect.getAmplifier() >= 2) {
                    double rand = Math.random();
                    if (rand < 0.50d) {
                        addDrop(event, new ItemStack(ForbiddenItems.resource, 1, 3));
                    }
                }
            }
        }

        if (Config.silverfishEmeralds && event.getEntityLiving() instanceof EntitySilverfish) {
            if (event.getEntityLiving().world.getBiome(new BlockPos(event.getEntityLiving())).getRegistryName() != null
                    && isExtremeHills(event.getEntityLiving().world.getBiome(new BlockPos(event.getEntityLiving())).getRegistryName().getPath())) {
                if (event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer && randy.nextInt(30) <= (2 + event.getLootingLevel() * 2))
                    addDrop(event, new ItemStack(ForbiddenItems.resource, 1, 0));
            } else if (event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer && !(event.getSource().getTrueSource() instanceof FakePlayer) && randy.nextInt(70) <= (1 + event.getLootingLevel() * 3)) {
                addDrop(event, new ItemStack(ForbiddenItems.resource, 1, 0));
            }

        }

        if (Config.greedyEnch && (event.getEntityLiving() instanceof EntityVillager || event.getEntityLiving() instanceof IMob) && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            ItemStack equip = player.getHeldItemMainhand();
            if (equip != null && EnchantmentHelper.getEnchantmentLevel(DarkEnchantments.greedy, equip) > 0 && event.getLootingLevel() <= 0) {
                if (event.getEntityLiving() instanceof EntityVillager)
                    addDrop(event, new ItemStack(Items.EMERALD, 1));
                else if (randy.nextInt(35) < 3)
                    addDrop(event, new ItemStack(ForbiddenItems.resource, 1, 0));
            }
        }

        if (event.getEntityLiving() instanceof AbstractSkeleton && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
            ItemStack weap = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (weap != null && weap.getItem() == ForbiddenItems.skullAxe && randy.nextInt(26) <= (3 + EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, weap))) {
                addDrop(event, new ItemStack(Items.SKULL, 1, (event.getEntityLiving() instanceof EntityWitherSkeleton ? 1 : 0)));
            }
        }

        if (event.getEntityLiving().getClass() == EntityZombie.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
            ItemStack weap = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (weap != null && weap.getItem() == ForbiddenItems.skullAxe && randy.nextInt(26) <= (2 + 2 * EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, weap))) {
                addDrop(event, new ItemStack(Items.SKULL, 1, 2));
            }
        }

        if (event.getEntityLiving().getClass() == EntityCreeper.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
            ItemStack weap = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (weap != null && weap.getItem() == ForbiddenItems.skullAxe && randy.nextInt(26) <= (2 + 2 * EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, weap))) {
                addDrop(event, new ItemStack(Items.SKULL, 1, 4));
            }
        }

        if (event.getEntityLiving() instanceof EntityPlayer && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
            ItemStack weap = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (weap != null && weap.getItem() == ForbiddenItems.skullAxe && randy.nextInt(11) <= (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, weap))) {
                ItemStack head = new ItemStack(Items.SKULL, 1, 3);
                NBTTagCompound nametag = new NBTTagCompound();
                nametag.setString("SkullOwner", ((EntityPlayer) event.getEntityLiving()).getName());
                head.setTagCompound(nametag);
                addDrop(event, head);
            }
        }

        if (event.getEntityLiving().world.provider.getDimension() != -1)
            return;

        if (!event.isRecentlyHit() || event.getSource().getTrueSource() == null || !(event.getSource().getTrueSource() instanceof EntityPlayer) || event.getSource().getTrueSource() instanceof FakePlayer) {
            if (randy.nextInt(30) < 4) {
                addDrop(event, new ItemStack(ForbiddenItems.deadlyShards, 1 + randy.nextInt(3), 5));
            }
        }

        if (event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer && !(event.getSource().getTrueSource() instanceof FakePlayer)) {
            if (event.getEntityLiving() instanceof IMob) {
                int wrath = 2;
                int greed = 0;
                ItemStack heldItem = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
                if (heldItem != null) {
                    if (heldItem.getItem() instanceof ItemTool || heldItem.getItem() instanceof ItemSword) {
                        for (AttributeModifier modifier : heldItem.getAttributeModifiers(EntityEquipmentSlot.MAINHAND)
                                .get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
                            wrath += (int) modifier.getAmount();
                        }
                    }

                    wrath += EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, heldItem);
                    wrath += EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, heldItem);

                    if (!event.getEntityLiving().isImmuneToFire()) {
                        wrath += EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, heldItem);
                        wrath += EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, heldItem);
                    }

                    if (event.getEntityLiving().isEntityUndead()) {
                        wrath += EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, heldItem);
                    }

                    if (event.getEntityLiving().getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
                        wrath += EnchantmentHelper.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, heldItem);
                    }

                    if(heldItem.getItem() instanceof ItemWandCasting){
                        wrath += ((ItemWandCasting)(heldItem.getItem())).getFocusPotency(heldItem);
                        greed += ((ItemWandCasting)(heldItem.getItem())).getFocusTreasure(heldItem);
                    }

                    greed += EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, heldItem);
                }

                if (randy.nextInt(61) <= wrath) {
                    addDrop(event, new ItemStack(ForbiddenItems.deadlyShards, 1, 0));
                }

                if (!event.getEntityLiving().isNonBoss()) {
                    addDrop(event, new ItemStack(ForbiddenItems.deadlyShards, 2 + randy.nextInt(1 + event.getLootingLevel()), 3));
                }

                if(greed > 0 && randy.nextInt(20) <= greed){
                    addDrop(event, new ItemStack(ForbiddenItems.deadlyShards, 1, 6));
                }
            }

            if (event.getEntityLiving() instanceof EntityPigZombie && !(event.getSource().getTrueSource() instanceof FakePlayer)) {
                if (event.getEntityLiving().getHeldItemMainhand() != null && event.getEntityLiving().getHeldItemMainhand().getItem() == ForbiddenItems.deadlyShards && event.getEntityLiving().getHeldItemMainhand().getItemDamage() == 1) {
                    addDrop(event, new ItemStack(ForbiddenItems.deadlyShards, 1, 1));
                }
            }
        }
    }

    public void addDrop(LivingDropsEvent event, ItemStack drop) {
        EntityItem entityitem = new EntityItem(event.getEntityLiving().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, drop);
        entityitem.setPickupDelay(10);
        event.getDrops().add(entityitem);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntityLiving().world.isRemote) return;
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer && ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand() != null) {
            ItemStack equip = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (equip.getItem() == ForbiddenItems.fork) {
                String name = null;
                try {
                    name = EntityList.getKey(event.getEntityLiving()) == null ? null : EntityList.getKey(event.getEntityLiving()).toString();
                } catch (Exception e) {
                    try {
                        name = event.getEntityLiving().getName();
                    } catch (Exception ee) {
                        return;
                    }
                } finally {
                    if (name == null)
                        return;
                }

                if (Config.spawnerMobs.containsKey(name) || (Config.wrathCrazy && event.getEntityLiving().isNonBoss()))
                    imprintCrystal((EntityPlayer) (event.getSource().getTrueSource()), name);
            }

            if (EnchantmentHelper.getEnchantmentLevel(DarkEnchantments.educational, equip) > 0 && event.getEntityLiving() instanceof EntityLiving && EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, equip) == 0) {
                try {
                    int learning = 3 * XPReflectionHelper.getXP(((EntityLiving) event.getEntityLiving())) * EnchantmentHelper.getEnchantmentLevel(DarkEnchantments.educational, equip);
                    while (learning > 0) {
                        int xp = EntityXPOrb.getXPSplit(learning);
                        learning -= xp;
                        event.getEntityLiving().world.spawnEntity(new EntityXPOrb(event.getEntityLiving().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, xp));
                    }
                } catch (Throwable e) {
                    LogHandler.log(Level.ERROR, "Failed to educate!");
                }
            }
        }
    }

    public void imprintCrystal(EntityPlayer player, String mob) {
        for (int x = 0; x < player.inventory.getSizeInventory(); ++x) {
            ItemStack item = player.inventory.getStackInSlot(x);
            if (!item.isEmpty() && item.getItem() == ForbiddenItems.mobCrystal && (!item.hasTagCompound() || !item.getTagCompound().hasKey("mob"))) {
                if (!item.hasTagCompound())
                    item.setTagCompound(new NBTTagCompound());
                item.getTagCompound().setString("mob", mob);
                return;
            }
        }
    }

    @SubscribeEvent
    public void onHarvest(HarvestDropsEvent event) {
        if (event.getWorld().isRemote) return;
        EntityPlayer player = event.getHarvester();
        if (player != null && player.inventory.getCurrentItem() != null) {
            ItemStack equip = player.inventory.getCurrentItem();
            if (equip.getItem() == ForbiddenItems.taintShovel && event.getState().getMaterial() == thaumcraft.common.config.Config.taintMaterial && event.getDrops().size() == 0 && event.getState().getBlock() != ForbiddenBlocks.taintLeaves) {
                int fortune = EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, player.getHeldItemMainhand());
                int chance = 1 + (2 * fortune);
                if (event.isSilkTouching())
                    chance += 6;

                if (randy.nextInt(31) <= chance) {
                    event.getDrops().add(new ItemStack(ForbiddenItems.deadlyShards, 1, 2));
                }
            }
            if (EnchantmentHelper.getEnchantmentLevel(DarkEnchantments.cluster, equip) > 0 && EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, player.getHeldItemMainhand()) == 0) {
                if (event.getDrops() != null && event.getDrops().size() > 0) {
                    int cluster = EnchantmentHelper.getEnchantmentLevel(DarkEnchantments.cluster, equip);
                    float chance = 0.2F + (float) cluster * 0.075F;

                    for (int a = 0; a < event.getDrops().size(); ++a) {
                        ItemStack is = (ItemStack) event.getDrops().get(a);
                        ItemStack smr = Utils.findSpecialMiningResult(is, chance, event.getWorld().rand);
                        if (!is.isItemEqual(smr)) {
                            event.getDrops().set(a, smr);
                            if (!event.getWorld().isRemote) {
                                event.getWorld().playSound(null, event.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.2F, 0.7F + event.getWorld().rand.nextFloat() * 0.2F);
                            }
                        }
                    }
                }
            }
            if (EnchantmentHelper.getEnchantmentLevel(DarkEnchantments.consuming, equip) > 0) {
                event.getDrops().removeIf(drop -> drop != null && !drop.isEmpty() && isGarbage(drop));
            }
            if (EnchantmentHelper.getEnchantmentLevel(DarkEnchantments.corrupting, equip) > 0 && event.getState().getBlock() == net.minecraft.block.Block.getBlockFromItem(Config.thaumcraftOre.getItem()) && randy.nextInt(3) == 1) {
                ArrayList<ItemStack> moarDrops = new ArrayList<ItemStack>();
                ArrayList<ItemStack> deadDrops = new ArrayList<ItemStack>();
                Iterator current = event.getDrops().iterator();
                while (current.hasNext()) {
                    ItemStack curDrop = (ItemStack) current.next();
                    if (curDrop != null && curDrop.getItem() == Config.thaumcraftShard.getItem()) {
                        int x = curDrop.getCount();
                        for (int i = 0; i < x; i++) {
                            moarDrops.add(getSinShard());
                        }
                        deadDrops.add(curDrop);
                    }
                }
                current = deadDrops.iterator();
                while (current.hasNext()) {
                    event.getDrops().remove((ItemStack) (current.next()));
                }
                current = moarDrops.iterator();
                while (current.hasNext()) {
                    event.getDrops().add((ItemStack) (current.next()));
                }
            }
            if(player.world.provider.getDimension() == -1 && event.getState().getBlock() == Blocks.QUARTZ_ORE){
                int fortune = EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, player.getHeldItemMainhand());
                if(fortune > 0 && randy.nextInt(60) <= fortune){
                    event.getDrops().add(new ItemStack(ForbiddenItems.deadlyShards, 1, 6));
                }
            }
        }
    }

    private boolean isGarbage(ItemStack drop) {
        for(int id : OreDictionary.getOreIDs(drop)) {
            for(String ore : Config.trash) {
                if(OreDictionary.getOreName(id).equals(ore))
                    return true;
            }
        }
        return false;
    }

    /** Original consumption-triggered drop and Ring of Nutrition effects. */
    @SubscribeEvent
    public void onEat(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (player.world.isRemote) return;
        final ItemStack eaten = event.getItem();
        if (eaten.isEmpty() || !(eaten.getItem() instanceof ItemFood)) return;

        // In the Nether, original Forbidden Magic gives one Gluttony Shard
        // with a 20% chance when food is actually finished (not merely used).
        if (player.world.provider.getDimension() == -1 && randy.nextInt(10) < 2) {
            EntityItem drop = player.dropItem(new ItemStack(ForbiddenItems.gluttonyShard), false);
            if (drop != null) {
                drop.motionY += randy.nextFloat() * 0.05F;
                drop.motionX += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
                drop.motionZ += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
            }
        }
        // Baubles 1.12 API checks equipped items, independent of changed
        // ring-slot indices. One ring or two still gives a single bonus.
        if (BaublesApi.isBaubleEquipped(player, ForbiddenItems.ringFood) >= 0) {
            player.getFoodStats().addStats(2, 2.0F);
        }
    }

    public ItemStack getSinShard() {
        int x = Config.noLust ? randy.nextInt(6) : randy.nextInt(7);
        if (Config.noLust && x >= 4) ++x;
        switch (x) {
        case 0:
        case 1:
        case 3:
        case 5:
        case 6:
            return new ItemStack(ForbiddenItems.deadlyShards, 1, x);
        case 2:
            return new ItemStack(ForbiddenItems.gluttonyShard, 1, 0);
        case 4:
            if (Config.noLust)
                return getSinShard();
            else
                return new ItemStack(ForbiddenItems.deadlyShards, 1, 4);
        }
        return null;
    }

    @SubscribeEvent
    public void onSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (!event.getWorld().isRemote && event.getEntityLiving().world.provider.getDimension() == -1
                && event.getEntityLiving() instanceof EntityPigZombie && randy.nextInt(175) == 1)
            event.getEntityLiving().setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ForbiddenItems.deadlyShards, 1, 1));
    }

    @SubscribeEvent
    public void onGetHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().world.isRemote) return;
        if (event.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer) event.getEntityLiving()).getHeldItemMainhand() != null && ((EntityPlayer) event.getEntityLiving()).getHeldItemMainhand().getItem() instanceof ItemWandCasting) {

            ItemStack wand = ((EntityPlayer) event.getEntityLiving()).getHeldItemMainhand();
            if (event.getSource().getDamageType().equals("taint") && ((ItemWandCasting) wand.getItem()).getRod(wand).getTag().equals("tainted")) {

                for (int x = 0; x < 3; x++) {
                    ((ItemWandCasting) wand.getItem()).addVis(wand, primals[randy.nextInt(6)], 1, true);
                }

            }

            if ((event.getSource().getDamageType().equals("wither") || event.getSource().isFireDamage()) && ((ItemWandCasting) wand.getItem()).getRod(wand).getTag().equals("infernal")) {

                event.setCanceled(true);
            }
        }

        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
            ItemStack equip = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (equip != null && equip.getItem() instanceof ItemWandCasting) {
                if (((ItemWandCasting) equip.getItem()).getCap(equip).getTag().equals("alchemical") && ((ItemWandCasting) equip.getItem()).getRod(equip).getTag().startsWith("blood")) {
                    event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 60, 2));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFeelPain(LivingHurtEvent event){
        if (event.getEntityLiving().world.isRemote) return;
        if(!Config.noLust && event.getAmount() > 0 && event.getEntityLiving() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            ItemStack amulet = BaublesApi.getBaublesHandler(player).getStackInSlot(0);
            if(amulet != null && amulet.getItem() == ForbiddenItems.subCollar){
                int doses = 3 * (int)event.getAmount();
                if(event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer){
                    EntityPlayer dom = (EntityPlayer)event.getSource().getTrueSource();
                    int chance = 1;
                    if(dom.getHeldItemMainhand() != null && dom.getHeldItemMainhand().getItem() instanceof ItemRidingCrop) {
                        doses += 3;
                        chance += 3;
                    }
                    if(player.world.provider.getDimension() == -1 && randy.nextInt(30) < chance){
                        EntityItem ent = player.dropItem(new ItemStack(ForbiddenItems.deadlyShards, 1, 4), false);
                        if (ent != null) {
                            ent.motionY += player.world.rand.nextFloat() * 0.05F;
                            ent.motionX += (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.1F;
                            ent.motionZ += (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.1F;
                        }
                    }
                }
                for(int x = 0;x < doses; x++){
                    ((ItemSubCollar)ForbiddenItems.subCollar).addVis(amulet, primals[randy.nextInt(6)], 1, true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ItemWandCasting) {
            if (((ItemWandCasting) event.getItemStack().getItem()).getRod(event.getItemStack()).getTag().startsWith("blood") && !(event.getItemStack().hasTagCompound() ? event.getItemStack().getTagCompound().getString("ownerName") : "").equals("")) {
                event.getToolTip().add(I18n.translateToLocal("tooltip.currentowner") + " " + (event.getItemStack().hasTagCompound() ? event.getItemStack().getTagCompound().getString("ownerName") : ""));
            } else if (((ItemWandCasting) event.getItemStack().getItem()).getRod(event.getItemStack()).getTag().startsWith("neutronium")) {
                event.getToolTip().add(I18n.translateToLocal("tooltip.creativeonly"));
            } else if (((ItemWandCasting) event.getItemStack().getItem()).getRod(event.getItemStack()).getTag().startsWith("profane")) {
                event.getToolTip().add("Contract: " + (event.getItemStack().hasTagCompound() ? event.getItemStack().getTagCompound().getInteger("contract") : 0));
            }
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingUpdateEvent event){
        if(Compat.bm && event.getEntityLiving() != null && event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().world.isRemote){
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            String name = player.getName();
            if(player.isPotionActive(DarkPotions.bloodSeal)){
                try {
                    if (lastLP.containsKey(name)) {
                        if (BloodNetwork.getCurrentEssence(name) > lastLP.get(name))
                            BloodNetwork.setCurrentEssence(name, lastLP.get(name));
                        else
                            lastLP.put(name, BloodNetwork.getCurrentEssence(name));
                    }
                    else
                        lastLP.put(name, BloodNetwork.getCurrentEssence(name));
                }
                catch(Throwable e){
                    Compat.bm = false;
                }
            }
            else if(lastLP.containsKey(name))
                lastLP.remove(name);
        }
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event) {
        if (event.player.world.isRemote) return;
        if(!event.crafting.isEmpty() && event.crafting.getItem() == ForbiddenItems.primewell) {
            for(int x = 0; x < 9; ++x) {
                ItemStack item = event.craftMatrix.getStackInSlot(x);
                if(!item.isEmpty() && item.getItem() == ConfigItems.itemEldritchObject) {
                    item.grow(1);
                    event.craftMatrix.setInventorySlotContents(x, item);
                }
            }
        }
        if(Compat.bm && !event.crafting.isEmpty() && event.crafting.getItem() == ForbiddenItems.bloodOrb){
            EntityItem ent = event.player.dropItem(new ItemStack(ConfigItems.itemEldritchObject, 1, 3), false);
            if (ent != null) ent.motionY += randy.nextFloat() * 0.05F;
            if (ent != null) ent.motionX += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
            if (ent != null) ent.motionZ += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
        }
    }

}