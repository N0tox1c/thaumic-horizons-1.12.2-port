//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;
import net.minecraft.util.math.BlockPos;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityItemInvulnerable;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemCrystalEssence;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.utils.BlockUtils;

public class ItemFocusDisintegration extends ItemFocusBasic  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider, com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    public static FocusUpgradeType enervation;
    private static final AspectList cost;
    private static final AspectList costCritter;
    static HashMap<String, Long> soundDelay;
    static HashMap<String, Object> beam;
    static HashMap<String, Float> breakcount;
    static HashMap<String, Integer> lastX;
    static HashMap<String, Integer> lastY;
    static HashMap<String, Integer> lastZ;

    public ItemFocusDisintegration() {
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:focus_disintegration", "items"));
    }

    @Override
    public String getSortingHelper(final ItemStack itemstack) {
        return "DS" + super.getSortingHelper(itemstack);
    }

    public int getFocusColor() {
        return 7930047;
    }

    public boolean isVisCostPerTick() {
        return false;
    }

    @Override
    public ItemStack onFocusRightClick(final ItemStack itemstack, final World world, final EntityPlayer p,
            final RayTraceResult mop) {
        com.kentington.thaumichorizons.common.lib.WandInteraction.startUsing(p, itemstack);
        return itemstack;
    }

    @Override
    public void onUsingFocusTick(ItemStack stack, final EntityPlayer p, final int count) {
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();
        final int size = 2 + wand.getFocusEnlarge(stack) * 8;
        if (!wand.consumeAllVis(stack, p, this.getVisCost(stack), false, true)) {
            p.stopActiveHand();
        } else {
            String pp = "SP|" + p.getName();
            if (!p.world.isRemote) {
                pp = "MP|" + p.getName();
            }
            ItemFocusDisintegration.soundDelay.putIfAbsent(pp, 0L);
            ItemFocusDisintegration.breakcount.putIfAbsent(pp, 0.0f);
            ItemFocusDisintegration.lastX.putIfAbsent(pp, 0);
            ItemFocusDisintegration.lastY.putIfAbsent(pp, 0);
            ItemFocusDisintegration.lastZ.putIfAbsent(pp, 0);
            final RayTraceResult mop = com.kentington.thaumichorizons.common.lib.WandInteraction.rayTrace(p.world, p, true);
            final Entity ent = getPointedEntity(p.world, p, 10.0);
            final Vec3d v = p.getLookVec();
            double tx = p.posX + v.x * 10.0;
            double ty = p.posY + v.y * 10.0;
            double tz = p.posZ + v.z * 10.0;
            byte impact = 0;
            if ((ent == null || (ent instanceof EntityItem && (this.getAspects(
                    ((EntityItem) ent).getItem().getItem(),
                    ((EntityItem) ent).getItem().getItemDamage()) == null
                    || this.getAspects(
                            ((EntityItem) ent).getItem().getItem(),
                            ((EntityItem) ent).getItem().getItemDamage()).size() == 0)))
                    && mop != null) {
                tx = mop.hitVec.x;
                ty = mop.hitVec.y;
                tz = mop.hitVec.z;
                impact = 5;
                if (!p.world.isRemote && ItemFocusDisintegration.soundDelay.get(pp) < System.currentTimeMillis()) {
                    com.kentington.thaumichorizons.common.lib.THSoundBridge.play(p.world, tx, ty, tz, "thaumcraft:zap", 0.3f, 1.0f);
                    ItemFocusDisintegration.soundDelay.put(pp, System.currentTimeMillis() + 100L);
                }
            } else if (ent != null) {
                tx = ent.posX;
                ty = ent.posY;
                tz = ent.posZ;
                impact = 5;
                if (!p.world.isRemote && ItemFocusDisintegration.soundDelay.get(pp) < System.currentTimeMillis()) {
                    com.kentington.thaumichorizons.common.lib.THSoundBridge.play(p.world, tx, ty, tz, "thaumcraft:zap", 0.3f, 1.0f);
                    ItemFocusDisintegration.soundDelay.put(pp, System.currentTimeMillis() + 100L);
                }
            }
            if (p.world.isRemote) {
                ItemFocusDisintegration.beam.put(
                        pp,
                        Thaumcraft.proxy.beamCont(
                                p.world,
                                p,
                                tx,
                                ty,
                                tz,
                                0,
                                7930047,
                                false,
                                (impact > 0) ? ((float) size) : 0.0f,
                                ItemFocusDisintegration.beam.get(pp),
                                5));
            }
            if ((ent == null || (!(ent instanceof EntityLiving) && (!(ent instanceof EntityItem)
                    || ((EntityItem) ent).getItem().getItem() == ConfigItems.itemCrystalEssence
                    || this.getAspects(
                            ((EntityItem) ent).getItem().getItem(),
                            ((EntityItem) ent).getItem().getItemDamage()) == null
                    || this.getAspects(
                            ((EntityItem) ent).getItem().getItem(),
                            ((EntityItem) ent).getItem().getItemDamage()).size() == 0)))
                    && mop != null
                    && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
                final Block bi = p.world.getBlockState(new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ())).getBlock();
                final int md = p.world.getBlockState(new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ())).getBlock().getMetaFromState(p.world.getBlockState(new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ())));
                if (Item.getItemFromBlock(bi) != null && this.getAspects(Item.getItemFromBlock(bi), md) != null) {
                    final int pot = wand.getFocusPotency(stack);
                    final float speed = 0.05f + pot * 0.015f;
                    final float hardness = p.world.getBlockState(mop.getBlockPos()).getBlockHardness(p.world, mop.getBlockPos());
                    if (hardness == -1.0f) {
                        return;
                    }
                    if (ItemFocusDisintegration.lastX.get(pp) == mop.getBlockPos().getX()
                            && ItemFocusDisintegration.lastY.get(pp) == mop.getBlockPos().getY()
                            && ItemFocusDisintegration.lastZ.get(pp) == mop.getBlockPos().getZ()) {
                        final float bc = ItemFocusDisintegration.breakcount.get(pp);
                        if (p.world.isRemote && bc > 0.0f && bi != null) {
                            final int gt = (int) (bc / hardness * 9.0f);
                            ThaumicHorizons.proxy.disintegrateFX(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ(), p, 15, size > 2);
                        }
                        if (p.world.isRemote) {
                            if (bc >= hardness) {
                                p.world.playEvent(2001, new net.minecraft.util.math.BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ()), 0);
                                ItemFocusDisintegration.breakcount.put(pp, 0.0f);
                            } else {
                                ItemFocusDisintegration.breakcount.put(pp, bc + speed);
                            }
                        } else if (bc >= hardness) {
                            this.processBlock(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ(), wand, stack, p, pp);
                            if (size > 2) {
                                for (int x = -1; x < 2; ++x) {
                                    for (int y = -1; y < 2; ++y) {
                                        for (int z = -1; z < 2; ++z) {
                                            if (x != 0 || y != 0 || z != 0) {
                                                this.processBlock(
                                                        mop.getBlockPos().getX() + x,
                                                        mop.getBlockPos().getY() + y,
                                                        mop.getBlockPos().getZ() + z,
                                                        wand,
                                                        stack,
                                                        p,
                                                        pp);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            ItemFocusDisintegration.breakcount.put(pp, bc + speed);
                        }
                    } else {
                        ItemFocusDisintegration.lastX.put(pp, mop.getBlockPos().getX());
                        ItemFocusDisintegration.lastY.put(pp, mop.getBlockPos().getY());
                        ItemFocusDisintegration.lastZ.put(pp, mop.getBlockPos().getZ());
                        ItemFocusDisintegration.breakcount.put(pp, 0.0f);
                    }
                }
            } else if (ent instanceof EntityLiving
                    && wand.consumeAllVis(stack, p, ItemFocusDisintegration.costCritter, !p.world.isRemote, true)) {
                        if (!p.world.isRemote && this.getUpgradeLevel(wand.getFocusItem(stack), ItemFocusDisintegration.enervation) > 0) {
                            ((EntityLiving) ent).addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.SLOWNESS, 40, 0));
                        }
                        ThaumicHorizons.proxy.disintegrateFX(ent.posX - 0.5, ent.posY, ent.posZ - 0.5, p, 5, false);
                        if (!p.world.isRemote) ent.attackEntityFrom(
                                DamageSourceThaumcraft.dissolve,
                                0.5f + 0.25f * wand.getFocusPotency(stack)
                                        + 0.5f * this.getUpgradeLevel(
                                                wand.getFocusItem(stack),
                                                ItemFocusDisintegration.enervation));
                    } else
                if (ent instanceof EntityItem
                        && this.getAspects(
                                ((EntityItem) ent).getItem().getItem(),
                                ((EntityItem) ent).getItem().getItemDamage()) != null
                        && this.getAspects(
                                ((EntityItem) ent).getItem().getItem(),
                                ((EntityItem) ent).getItem().getItemDamage()).size() > 0) {
                                    final int num = ((EntityItem) ent).getItem().getCount();
                                    if (wand.consumeAllVis(stack, p, this.getVisCost(stack), !p.world.isRemote, true)) {
                                        ThaumicHorizons.proxy.disintegrateFX(
                                                ent.posX - 0.5,
                                                ent.posY,
                                                ent.posZ - 0.5,
                                                p,
                                                5 * num,
                                                false);
                                        final AspectList aspects = this.getAspects(
                                                ((EntityItem) ent).getItem().getItem(),
                                                ((EntityItem) ent).getItem().getItemDamage());
                                        if (aspects != null && !p.world.isRemote) {
                                            if (!p.world.isRemote) {
                                                for (final Aspect asp : aspects.getAspects()) {
                                                    stack = new ItemStack(
                                                            ConfigItems.itemCrystalEssence,
                                                            aspects.getAmount(asp) * num);
                                                    ((ItemCrystalEssence) stack.getItem())
                                                            .setAspects(stack, new AspectList().add(asp, 1));
                                                    p.world.spawnEntity(
                                                            new EntityItemInvulnerable(
                                                                    p.world,
                                                                    ent.posX,
                                                                    ent.posY,
                                                                    ent.posZ,
                                                                    stack));
                                                }
                                            } else {
                                                ThaumicHorizons.proxy.disintegrateExplodeFX(
                                                        p.world,
                                                        ent.posX,
                                                        ent.posY,
                                                        ent.posZ);
                                            }
                                        }
                                        if (!p.world.isRemote) ent.setDead();
                                    }
                                } else {
                                    ItemFocusDisintegration.lastX.put(pp, Integer.MAX_VALUE);
                                    ItemFocusDisintegration.lastY.put(pp, Integer.MAX_VALUE);
                                    ItemFocusDisintegration.lastZ.put(pp, Integer.MAX_VALUE);
                                    ItemFocusDisintegration.breakcount.put(pp, 0.0f);
                                }
        }
    }

    public void onPlayerStoppedUsing(final ItemStack stack, final World world, final EntityPlayer p, final int count) {
        String pp = "R" + p.getName();
        if (!p.world.isRemote) {
            pp = "S" + p.getName();
        }
        ItemFocusDisintegration.soundDelay.putIfAbsent(pp, 0L);
        ItemFocusDisintegration.breakcount.putIfAbsent(pp, 0.0f);
        ItemFocusDisintegration.lastX.putIfAbsent(pp, 0);
        ItemFocusDisintegration.lastY.putIfAbsent(pp, 0);
        ItemFocusDisintegration.lastZ.putIfAbsent(pp, 0);
        ItemFocusDisintegration.beam.put(pp, null);
        ItemFocusDisintegration.lastX.put(pp, Integer.MAX_VALUE);
        ItemFocusDisintegration.lastY.put(pp, Integer.MAX_VALUE);
        ItemFocusDisintegration.lastZ.put(pp, Integer.MAX_VALUE);
        ItemFocusDisintegration.breakcount.put(pp, 0.0f);
    }

    @Override
    public WandFocusAnimation getAnimation(final ItemStack focusstack) {
        return WandFocusAnimation.CHARGE;
    }

    @Override
    public int getFocusColor(final ItemStack focusstack) {
        return 7930047;
    }

    @Override
    public AspectList getVisCost(final ItemStack focusstack) {
        return ItemFocusDisintegration.cost;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(final ItemStack focusstack, final int rank) {
        switch (rank) {
    case 1: case 4: case 2: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency };
            } 
    case 3: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency,
                        FocusUpgradeType.enlarge };
            } 
    case 5: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency,
                        ItemFocusDisintegration.enervation };
            } 
    default: {
                return null;
            }
}
    }

    public String getItemStackDisplayName(final ItemStack stack) {
        return I18n.translateToLocal("item.focusDisintegration.name");
    }

    public static Entity getPointedEntity(final World world, final EntityLivingBase entityplayer, final double range) {
        Entity pointedEntity = null;
        final Vec3d vec3d = new Vec3d(
                entityplayer.posX,
                entityplayer.posY + entityplayer.getEyeHeight(),
                entityplayer.posZ);
        final Vec3d vec3d2 = entityplayer.getLookVec();
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        final float f1 = 1.1f;
        final List list = world.getEntitiesWithinAABBExcludingEntity(
                entityplayer,
                entityplayer.getEntityBoundingBox().expand(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range)
                        .expand(f1, f1, f1));
        double d2 = 0.0;
        for (Object o : list) {
            final Entity entity = (Entity) o;
            if (!(entity instanceof EntityItem)
                    || ((EntityItem) entity).getItem().getItem() != ConfigItems.itemCrystalEssence) {
                if (world.rayTraceBlocks(
                        new Vec3d(
                                entityplayer.posX,
                                entityplayer.posY + entityplayer.getEyeHeight(),
                                entityplayer.posZ),
                        new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ),
                        false) == null) {
                    final float f2 = Math.max(0.8f, entity.getCollisionBorderSize());
                    final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f2, f2, f2);
                    final RayTraceResult movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d3);
                    if (axisalignedbb.contains(vec3d)) {
                        if (0.0 < d2 || d2 == 0.0) {
                            pointedEntity = entity;
                            d2 = 0.0;
                        }
                    } else if (movingobjectposition != null) {
                        final double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
                        if (d3 < d2 || d2 == 0.0) {
                            pointedEntity = entity;
                            d2 = d3;
                        }
                    }
                }
            }
        }
        return pointedEntity;
    }

    void processBlock(final int x, final int y, final int z, final ItemWandCasting wand, ItemStack stack,
            final EntityPlayer p, final String pp) {
        BlockPos pos = new BlockPos(x, y, z);
        final Block bi = p.world.getBlockState(pos).getBlock();
        if (p.world.getBlockState(pos).getBlockHardness(p.world, pos) == -1.0f
                || !com.kentington.thaumichorizons.common.lib.BlockInteraction.canBreak(p.world, p, pos)) return;
        final int md = p.world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(p.world.getBlockState(new BlockPos(x, y, z)));
        if (Item.getItemFromBlock(bi) != null && this.getAspects(Item.getItemFromBlock(bi), md) != null
                && this.getAspects(Item.getItemFromBlock(bi), md).size() > 0
                && wand.consumeAllVis(stack, p, this.getVisCost(stack), true, true)) {
            final AspectList aspects = this.getAspects(
                    Item.getItemFromBlock(p.world.getBlockState(new BlockPos(x, y, z)).getBlock()),
                    p.world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(p.world.getBlockState(new BlockPos(x, y, z))));
            if (aspects != null) {
                for (final Aspect asp : aspects.getAspects()) {
                    stack = new ItemStack(ConfigItems.itemCrystalEssence, aspects.getAmount(asp));
                    ((ItemCrystalEssence) stack.getItem()).setAspects(stack, new AspectList().add(asp, 1));
                    p.world.spawnEntity(
                            new EntityItemInvulnerable(p.world, x + 0.5, y + 0.5, z + 0.5, stack));
                }
                ThaumicHorizons.proxy.disintegrateExplodeFX(p.world, x + 0.5, y + 0.5, z + 0.5);
            }
            p.world.setBlockToAir(new BlockPos(x, y, z));
        }
        ItemFocusDisintegration.lastX.put(pp, Integer.MAX_VALUE);
        ItemFocusDisintegration.lastY.put(pp, Integer.MAX_VALUE);
        ItemFocusDisintegration.lastZ.put(pp, Integer.MAX_VALUE);
        ItemFocusDisintegration.breakcount.put(pp, 0.0f);
        p.world.notifyBlockUpdate(new BlockPos(x, y, z), p.world.getBlockState(new BlockPos(x, y, z)), p.world.getBlockState(new BlockPos(x, y, z)), 3);
    }

    private AspectList getAspects(final Item block, final int meta) {
        final ItemStack tmpStack = new ItemStack(block, 1, meta);
        AspectList tmp = ThaumcraftCraftingManager.getObjectTags(tmpStack);
        tmp = ThaumcraftCraftingManager.getBonusTags(tmpStack, tmp);
        if (tmp == null || tmp.size() == 0) {
            tmp = ThaumcraftApi.objectTags.get(Arrays.asList(block, 32767));
            if (meta == 32767 && tmp == null) {
                int index = 0;
                do {
                    tmp = ThaumcraftApi.objectTags.get(Arrays.asList(block, index));
                } while (++index < 16 && tmp == null);
            }
        }
        return tmp;
    }

    static {
        ItemFocusDisintegration.enervation = new FocusUpgradeType(
                FocusUpgradeType.types.length,
                new ResourceLocation("thaumichorizons", "textures/foci/enervation.png"),
                "focus.upgrade.enervation.name",
                "focus.upgrade.enervation.text",
                new AspectList().add(Aspect.DEATH, 8));
        cost = new AspectList().add(Aspect.FIRE, 30).add(Aspect.ENTROPY, 30).add(Aspect.EARTH, 30);
        costCritter = new AspectList().add(Aspect.FIRE, 12).add(Aspect.ENTROPY, 12).add(Aspect.EARTH, 12);
        ItemFocusDisintegration.soundDelay = new HashMap<>();
        ItemFocusDisintegration.beam = new HashMap<>();
        ItemFocusDisintegration.breakcount = new HashMap<>();
        ItemFocusDisintegration.lastX = new HashMap<>();
        ItemFocusDisintegration.lastY = new HashMap<>();
        ItemFocusDisintegration.lastZ = new HashMap<>();
    }
    @Override @SideOnly(Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getIconFromDamage(int metadata) {
        return this.icon;
    }

}
