//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;
import net.minecraft.util.math.BlockPos;


import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.world.BlockEvent;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;

public class ItemFocusLiquefaction extends ItemFocusBasic  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider, com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    public static FocusUpgradeType nuggets;
    public static FocusUpgradeType purity;
    private static final AspectList cost;
    private static final AspectList costCritter;
    static HashMap<String, Long> soundDelay;
    static HashMap<String, Object> beam;
    static HashMap<String, Float> breakcount;
    static HashMap<String, Integer> lastX;
    static HashMap<String, Integer> lastY;
    static HashMap<String, Integer> lastZ;

    public ItemFocusLiquefaction() {
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:focus_liquefaction", "items"));
    }

    @Override
    public String getSortingHelper(final ItemStack itemstack) {
        return "BE" + super.getSortingHelper(itemstack);
    }

    public int getFocusColor() {
        return 16729156;
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
    public void onUsingFocusTick(final ItemStack stack, final EntityPlayer player, final int count) {
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();
        final int size = 2 + wand.getFocusEnlarge(stack) * 8;
        if (!wand.consumeAllVis(stack, player, this.getVisCost(stack), false, true)) {
            player.stopActiveHand();
        } else {
            String playerProfile = "SP|" + player.getName();
            if (!player.world.isRemote) {
                playerProfile = "MP|" + player.getName();
            }
            ItemFocusLiquefaction.soundDelay.putIfAbsent(playerProfile, 0L);
            ItemFocusLiquefaction.breakcount.putIfAbsent(playerProfile, 0.0f);
            ItemFocusLiquefaction.lastX.putIfAbsent(playerProfile, 0);
            ItemFocusLiquefaction.lastY.putIfAbsent(playerProfile, 0);
            ItemFocusLiquefaction.lastZ.putIfAbsent(playerProfile, 0);

            final RayTraceResult mop = com.kentington.thaumichorizons.common.lib.WandInteraction.rayTrace(player.world, player, true);
            final Entity ent = getPointedEntity(player.world, player, 10.0);
            final Vec3d v = player.getLookVec();
            double tx = player.posX + v.x * 10.0;
            double ty = player.posY + v.y * 10.0;
            double tz = player.posZ + v.z * 10.0;
            byte impact = 0;

            ItemStack droppedItem = null;
            ItemStack resultItem = null;

            if (ent instanceof EntityItem) {
                droppedItem = ((EntityItem) ent).getItem();
                if (droppedItem != null && droppedItem.getCount() > 0) {
                    ItemStack smeltingOutput = FurnaceRecipes.instance().getSmeltingResult(droppedItem);
                    if (!smeltingOutput.isEmpty()) {
                        resultItem = smeltingOutput.copy();
                    }
                }
            }

            if (ent instanceof EntityLiving) {
                tx = ent.posX;
                ty = ent.posY;
                tz = ent.posZ;
                impact = 5;
                if (!player.world.isRemote
                        && ItemFocusLiquefaction.soundDelay.get(playerProfile) < System.currentTimeMillis()) {
                    com.kentington.thaumichorizons.common.lib.THSoundBridge.play(player.world, tx, ty, tz, "fire.fire", 0.3f, 1.0f);
                    ItemFocusLiquefaction.soundDelay.put(playerProfile, System.currentTimeMillis() + 1200L);
                }
            } else if (resultItem == null && mop != null) {
                tx = mop.hitVec.x;
                ty = mop.hitVec.y;
                tz = mop.hitVec.z;
                impact = 5;
                if (!player.world.isRemote
                        && ItemFocusLiquefaction.soundDelay.get(playerProfile) < System.currentTimeMillis()) {
                    com.kentington.thaumichorizons.common.lib.THSoundBridge.play(player.world, tx, ty, tz, "fire.fire", 8.0f, 1.0f);
                    ItemFocusLiquefaction.soundDelay.put(playerProfile, System.currentTimeMillis() + 1200L);
                }
            } else {
                ItemFocusLiquefaction.soundDelay.put(playerProfile, 0L);
            }
            if (player.world.isRemote) {
                ItemFocusLiquefaction.beam.put(
                        playerProfile,
                        Thaumcraft.proxy.beamCont(
                                player.world,
                                player,
                                tx,
                                ty,
                                tz,
                                0,
                                16729156,
                                false,
                                (impact > 0) ? ((float) size) : 0.0f,
                                ItemFocusLiquefaction.beam.get(playerProfile),
                                5));
            }

            if (ent instanceof EntityLiving) {
                if (!player.world.isRemote && wand.consumeAllVis(stack, player, ItemFocusLiquefaction.costCritter, true, true)) {
                    ThaumicHorizons.proxy.smeltFX(ent.posX - 0.5, ent.posY, ent.posZ - 0.5, player.world, 5, false);
                    ent.attackEntityFrom(DamageSource.IN_FIRE, 1.0f + 0.5f * wand.getFocusPotency(stack));
                }
            } else if (resultItem != null) {
                final int quantity = ((EntityItem) ent).getItem().getCount();
                if (!player.world.isRemote && wand.consumeAllVis(stack, player, this.getVisCost(stack), true, true)) {
                    ThaumicHorizons.proxy
                            .smeltFX(ent.posX - 0.5, ent.posY, ent.posZ - 0.5, player.world, 5 * quantity, false);

                    resultItem.setCount( quantity);
                    ((EntityItem) ent).setItem(resultItem);
                }
            } else if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
                final Block block = player.world.getBlockState(new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ())).getBlock();
                final int meta = player.world.getBlockState(new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ())).getBlock().getMetaFromState(player.world.getBlockState(new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ())));
                final ItemStack resultBlock = FurnaceRecipes.instance()
                        .getSmeltingResult(new ItemStack(block, 1, meta));
                final int meltingBehaviour = this.isMeltableBlock(block, meta);
                final boolean flammable = com.kentington.thaumichorizons.common.lib.BlockInteraction.isFlammable(block, player.world, mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ(), null);
                if (meltingBehaviour > 0 || flammable || !resultBlock.isEmpty()) {
                    final int pot = wand.getFocusPotency(stack);
                    final float speed = 0.15f + pot * 0.05f;
                    float hardness = 2.0f;

                    if (meltingBehaviour == 6) {
                        hardness = 20.0f;
                    } else if (meltingBehaviour > 0 || com.kentington.thaumichorizons.common.lib.BlockInteraction.isFlammable(block, player.world, mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ(), EnumFacing.UP)) {
                                hardness = 0.25f;
                            }

                    if (ItemFocusLiquefaction.lastX.get(playerProfile) == mop.getBlockPos().getX()
                            && ItemFocusLiquefaction.lastY.get(playerProfile) == mop.getBlockPos().getY()
                            && ItemFocusLiquefaction.lastZ.get(playerProfile) == mop.getBlockPos().getZ()) {
                        final float breakcount = ItemFocusLiquefaction.breakcount.get(playerProfile);

                        if (player.world.isRemote) {
                            if (breakcount > 0.0f) {
                                ThaumicHorizons.proxy
                                        .smeltFX(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ(), player.world, 15, size > 2);
                            }
                            if (breakcount >= hardness) {
                                player.world.playEvent(2001, new net.minecraft.util.math.BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ()), 0);
                                ItemFocusLiquefaction.breakcount.put(playerProfile, 0.0f);
                            } else {
                                ItemFocusLiquefaction.breakcount.put(playerProfile, breakcount + speed);
                            }
                        } else if (breakcount >= hardness) {
                            this.processBlock(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ(), wand, stack, player, playerProfile);
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
                                                        player,
                                                        playerProfile);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            ItemFocusLiquefaction.breakcount.put(playerProfile, breakcount + speed);
                        }
                    } else {
                        ItemFocusLiquefaction.lastX.put(playerProfile, mop.getBlockPos().getX());
                        ItemFocusLiquefaction.lastY.put(playerProfile, mop.getBlockPos().getY());
                        ItemFocusLiquefaction.lastZ.put(playerProfile, mop.getBlockPos().getZ());
                        ItemFocusLiquefaction.breakcount.put(playerProfile, 0.0f);
                    }
                }
            } else {
                ItemFocusLiquefaction.lastX.put(playerProfile, Integer.MAX_VALUE);
                ItemFocusLiquefaction.lastY.put(playerProfile, Integer.MAX_VALUE);
                ItemFocusLiquefaction.lastZ.put(playerProfile, Integer.MAX_VALUE);
                ItemFocusLiquefaction.breakcount.put(playerProfile, 0.0f);
            }
        }
    }

    public void onPlayerStoppedUsing(final ItemStack stack, final World world, final EntityPlayer p, final int count) {
        String pp = "SP|" + p.getName();
        if (!p.world.isRemote) {
            pp = "MP|" + p.getName();
        }
        ItemFocusLiquefaction.soundDelay.putIfAbsent(pp, 0L);
        ItemFocusLiquefaction.breakcount.putIfAbsent(pp, 0.0f);
        ItemFocusLiquefaction.lastX.putIfAbsent(pp, 0);
        ItemFocusLiquefaction.lastY.putIfAbsent(pp, 0);
        ItemFocusLiquefaction.lastZ.putIfAbsent(pp, 0);
        ItemFocusLiquefaction.beam.put(pp, null);
        ItemFocusLiquefaction.lastX.put(pp, Integer.MAX_VALUE);
        ItemFocusLiquefaction.lastY.put(pp, Integer.MAX_VALUE);
        ItemFocusLiquefaction.lastZ.put(pp, Integer.MAX_VALUE);
        ItemFocusLiquefaction.breakcount.put(pp, 0.0f);
    }

    @Override
    public WandFocusAnimation getAnimation(final ItemStack focusstack) {
        return WandFocusAnimation.CHARGE;
    }

    public int isMeltableBlock(final Block block, final int meta) {
        if (block == Blocks.STONE) {
            return 6;
        }
        final Material mat = block.getStateFromMeta(meta).getMaterial();
        if (mat == Material.TNT) {
            return 4;
        }
        if (mat == Material.GRASS) {
            return 3;
        }
        if (mat == Material.ICE || mat == Material.PACKED_ICE) {
            return 2;
        }
        if (mat == Material.CRAFTED_SNOW || mat == Material.LEAVES
                || mat == Material.PLANTS
                || mat == Material.SNOW
                || mat == Material.VINE
                || mat == Material.WEB) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getFocusColor(final ItemStack focusstack) {
        return 16711680;
    }

    @Override
    public AspectList getVisCost(final ItemStack focusstack) {
        return ItemFocusLiquefaction.cost;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(final ItemStack focusstack, final int rank) {
        switch (rank) {
    case 1: case 4: case 2: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency,
                        ItemFocusLiquefaction.nuggets };
            } 
    case 3: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency,
                        FocusUpgradeType.enlarge };
            } 
    case 5: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency,
                        ItemFocusLiquefaction.purity };
            } 
    default: {
                return null;
            }
}
    }

    public String getItemStackDisplayName(final ItemStack stack) {
        return I18n.translateToLocal("item.focusLiquefaction.name");
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
        return pointedEntity;
    }

    void processBlock(final int x, final int y, final int z, final ItemWandCasting wand, final ItemStack stack,
            final EntityPlayer p, final String pp) {
        if (!com.kentington.thaumichorizons.common.lib.BlockInteraction.canBreak(p.world, p, new BlockPos(x, y, z))) return;
        final Block bi = p.world.getBlockState(new BlockPos(x, y, z)).getBlock();
        if (p.world.getBlockState(new BlockPos(x, y, z)).getBlockHardness(p.world, new BlockPos(x, y, z)) == -1.0f) {
            return;
        }
        final int md = p.world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(p.world.getBlockState(new BlockPos(x, y, z)));
        final int meltable = this.isMeltableBlock(bi, md);
        final boolean flammable = com.kentington.thaumichorizons.common.lib.BlockInteraction.isFlammable(bi, p.world, x, y, z, null);
        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(new ItemStack(bi, 1, md));
        // Meltable blocks (e.g. stone) take priority over any furnace smelting recipe they may have,
        // otherwise mods that add a smelting recipe for stone (such as Et Futurum Requiem's smooth
        // stone) would prevent the stone -> lava transmutation.
        if (meltable > 0 && wand.consumeAllVis(stack, p, this.getVisCost(stack), true, true)) {
            if (meltable == 1) {
                p.world.setBlockToAir(new BlockPos(x, y, z));
            } else if (meltable == 2) {
                if (p.world.provider.getDimension() != -1) {
                    p.world.setBlockState(new BlockPos(x, y, z), Blocks.WATER.getStateFromMeta(0), 3);
                } else {
                    p.world.setBlockToAir(new BlockPos(x, y, z));
                }
            } else if (meltable == 3) {
                p.world.setBlockState(new BlockPos(x, y, z), Blocks.DIRT.getStateFromMeta(0), 3);
            } else if (meltable == 4) {
                Blocks.TNT.onPlayerDestroy(p.world, new net.minecraft.util.math.BlockPos(x, y, z), net.minecraft.init.Blocks.TNT.getStateFromMeta(1));
                p.world.setBlockToAir(new BlockPos(x, y, z));
            } else if (meltable == 6) {
                p.world.setBlockState(new BlockPos(x, y, z), Blocks.LAVA.getStateFromMeta(0), 3);
            }
        } else if (!result.isEmpty() && wand.consumeAllVis(stack, p, this.getVisCost(stack), true, true)) {
            if (this.getUpgradeLevel(wand.getFocusItem(stack), ItemFocusLiquefaction.purity) > 0
                    && Utils.findSpecialMiningResult(new ItemStack(bi, 1, md), 9999.0f, p.world.rand) != null) {
                final ItemStack pure = Utils
                        .findSpecialMiningResult(new ItemStack(bi, 1, md), 9999.0f, p.world.rand);
                if (!FurnaceRecipes.instance().getSmeltingResult(pure).isEmpty()) {
                    result = FurnaceRecipes.instance().getSmeltingResult(pure);
                }
            }
            if (this.getUpgradeLevel(wand.getFocusItem(stack), ItemFocusLiquefaction.nuggets) > 0
                    && ThaumcraftApi.getSmeltingBonus(new ItemStack(bi, 1, md)) != null) {
                final ItemStack bonus = ThaumcraftApi.getSmeltingBonus(new ItemStack(bi, 1, md)).copy();
                for (int i = 0; i
                        < this.getUpgradeLevel(wand.getFocusItem(stack), ItemFocusLiquefaction.nuggets); ++i) {
                    if (p.world.rand.nextFloat() < 0.45f) {
                        bonus.grow(1);
                    }
                }
                if (bonus.getCount() > 0) {
                    final EntityItem bonusEntity = new EntityItem(p.world, x + 0.5f, y + 0.5f, z + 0.5f, bonus);
                    p.world.spawnEntity(bonusEntity);
                }
            }
            if (result.getItem() instanceof ItemBlock) {
                p.world.setBlockState(new BlockPos(x, y, z), Block.getBlockFromItem(result.getItem()).getDefaultState(), 3);
                com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(p.world, x, y, z, result.getItemDamage(), 3);
            } else {
                p.world.setBlockToAir(new BlockPos(x, y, z));
                final EntityItem theItem = new EntityItem(p.world, x + 0.5f, y + 0.5f, z + 0.5f);
                theItem.setItem(result.copy());
                p.world.spawnEntity(theItem);
            }
        } else if (flammable && wand.consumeAllVis(stack, p, this.getVisCost(stack), true, true)) {
            p.world.setBlockState(new BlockPos(x, y, z), Blocks.FIRE.getDefaultState(), 3);
        }
        ItemFocusLiquefaction.lastX.put(pp, Integer.MAX_VALUE);
        ItemFocusLiquefaction.lastY.put(pp, Integer.MAX_VALUE);
        ItemFocusLiquefaction.lastZ.put(pp, Integer.MAX_VALUE);
        ItemFocusLiquefaction.breakcount.put(pp, 0.0f);
        p.world.notifyBlockUpdate(new BlockPos(x, y, z), p.world.getBlockState(new BlockPos(x, y, z)), p.world.getBlockState(new BlockPos(x, y, z)), 3);
    }

    static {
        ItemFocusLiquefaction.nuggets = new FocusUpgradeType(
                FocusUpgradeType.types.length,
                new ResourceLocation("thaumichorizons", "textures/foci/nuggets.png"),
                "focus.upgrade.nuggets.name",
                "focus.upgrade.nuggets.text",
                new AspectList().add(Aspect.METAL, 8));
        ItemFocusLiquefaction.purity = new FocusUpgradeType(
                FocusUpgradeType.types.length,
                new ResourceLocation("thaumichorizons", "textures/foci/purity.png"),
                "focus.upgrade.purity.name",
                "focus.upgrade.purity.text",
                new AspectList().add(Aspect.ORDER, 8));
        cost = new AspectList().add(Aspect.FIRE, 40);
        costCritter = new AspectList().add(Aspect.FIRE, 15);
        ItemFocusLiquefaction.soundDelay = new HashMap<>();
        ItemFocusLiquefaction.beam = new HashMap<>();
        ItemFocusLiquefaction.breakcount = new HashMap<>();
        ItemFocusLiquefaction.lastX = new HashMap<>();
        ItemFocusLiquefaction.lastY = new HashMap<>();
        ItemFocusLiquefaction.lastZ = new HashMap<>();
    }
    @Override @SideOnly(Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getIconFromDamage(int metadata) {
        return this.icon;
    }

}
