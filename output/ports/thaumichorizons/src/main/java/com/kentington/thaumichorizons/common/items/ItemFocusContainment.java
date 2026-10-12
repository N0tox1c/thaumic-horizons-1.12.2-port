//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntitySoul;
import com.kentington.thaumichorizons.common.lib.networking.PacketFXContainment;
import com.kentington.thaumichorizons.common.lib.networking.PacketHandler;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.InventoryUtils;

public class ItemFocusContainment extends ItemFocusBasic  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider, com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    public static FocusUpgradeType slow;
    public static HashMap<String, Object> beam;
    public static HashMap<String, Entity> hitCritters;
    public static HashMap<String, Float> contain;
    public static HashMap<String, Long> soundDelay;
    TextureAtlasSprite depthIcon;
    private static final AspectList cost;

    public ItemFocusContainment() {
        this.depthIcon = null;
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public String getItemStackDisplayName(final ItemStack stack) {
        return I18n.translateToLocal("item.focusContainment.name");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.depthIcon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:focus_containment_depth", "items"));
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:focus_containment", "items"));
    }

    @Override
    public TextureAtlasSprite getFocusDepthLayerIcon(final ItemStack itemstack) {
        return this.depthIcon;
    }

    @Override
    public int getFocusColor(final ItemStack itemstack) {
        return 29631;
    }

    @Override
    public String getSortingHelper(final ItemStack itemstack) {
        return "CN" + super.getSortingHelper(itemstack);
    }

    @Override
    public AspectList getVisCost(final ItemStack focusstack) {
        return ItemFocusContainment.cost.copy();
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(final ItemStack focusstack, final int rank) {
        switch (rank) {
    case 1: case 5: case 3: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency };
            } 
    case 2: case 4: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency,
                        ItemFocusContainment.slow };
            } 
    default: {
                return null;
            }
}
    }

    @Override
    public ItemStack onFocusRightClick(final ItemStack itemstack, final World world, final EntityPlayer p,
            final RayTraceResult movingobjectposition) {
        final ItemWandCasting wand = (ItemWandCasting) itemstack.getItem();
        com.kentington.thaumichorizons.common.lib.WandInteraction.startUsing(p, itemstack);
        return itemstack;
    }

    @Override
    public void onUsingFocusTick(final ItemStack stack, final EntityPlayer p, final int count) {
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();
        if (!this.canJarEntity(p)) {
            if (p.ticksExisted % 5 == 0) {
                p.sendMessage(
                        new TextComponentString(
                                TextFormatting.ITALIC + ""
                                        + TextFormatting.GRAY
                                        + I18n.translateToLocal("thaumichorizons.noJar")));
            }
            p.stopActiveHand();
            return;
        }
        if (!wand.consumeAllVis(stack, p, this.getVisCost(stack), false, false)) {
            p.stopActiveHand();
            return;
        }
        final String pp = "R" + p.getDisplayName();
        final Entity ent = getPointedEntity(p.world, p, 10.0);
        final RayTraceResult mop = com.kentington.thaumichorizons.common.lib.WandInteraction.rayTrace(p.world, p, true);
        final Vec3d v = p.getLookVec();
        double tx = p.posX + v.x * 10.0;
        double ty = p.posY + v.y * 10.0;
        double tz = p.posZ + v.z * 10.0;
        byte impact = 0;
        if (ent instanceof EntityLiving) {
            tx = ent.posX;
            ty = ent.posY + (ent.getEntityBoundingBox().maxY - ent.getEntityBoundingBox().minY) / 2.0;
            tz = ent.posZ;
            impact = 5;
            if (p != null && p.world != null
                    && !p.world.isRemote
                    && (ItemFocusContainment.soundDelay.get(pp) == null
                            || ItemFocusContainment.soundDelay.get(pp) < System.currentTimeMillis())) {
                com.kentington.thaumichorizons.common.lib.THSoundBridge.play(ent.world, tx, ty, tz, "thaumcraft:jacobs", 0.3f, 1.0f);
                ItemFocusContainment.soundDelay.put(pp, System.currentTimeMillis() + 1200L);
            }
        } else if (mop != null) {
            tx = mop.hitVec.x;
            ty = mop.hitVec.y;
            tz = mop.hitVec.z;
            impact = 5;
        } else {
            ItemFocusContainment.soundDelay.put(pp, 0L);
        }
        if (p.world.isRemote) {
            ItemFocusContainment.beam.put(
                    pp,
                    Thaumcraft.proxy.beamCont(
                            p.world,
                            p,
                            tx,
                            ty,
                            tz,
                            2,
                            4482815,
                            true,
                            (impact > 0) ? 2.0f : 0.0f,
                            ItemFocusContainment.beam.get(pp),
                            impact));
        }
        if (ent != null && ent instanceof EntityLiving
                && !(ent instanceof EntityPlayer)
                && !ent.isDead
                && ent.isNonBoss()
                && !(ent instanceof EntityGolemBase)
                && wand.consumeAllVis(stack, p, this.getVisCost(stack), true, false)) {
            if (this.getUpgradeLevel(wand.getFocusItem(stack), ItemFocusContainment.slow) > 0) {
                ((EntityLiving) ent).addPotionEffect(
                        new PotionEffect(
                                net.minecraft.init.MobEffects.SLOWNESS,
                                40,
                                this.getUpgradeLevel(wand.getFocusItem(stack), ItemFocusContainment.slow) - 1));
            }
            if (ItemFocusContainment.hitCritters.get(pp) == null
                    || ent.getEntityId() != ItemFocusContainment.hitCritters.get(pp).getEntityId()) {
                ItemFocusContainment.hitCritters.put(pp, ent);
                ItemFocusContainment.contain.put(pp, 2.0f + wand.getFocusPotency(stack) / 2.0f);
            } else {
                ItemFocusContainment.contain
                        .put(pp, ItemFocusContainment.contain.get(pp) + 2.0f + wand.getFocusPotency(stack) / 3);
            }
            if (!p.world.isRemote && ItemFocusContainment.contain.get(pp) > ((EntityLiving) ent).getHealth() * 20.0f
                    && !(ent instanceof EntitySoul)) {
                final NBTTagCompound entityData = new NBTTagCompound();
                entityData.setString("id", EntityList.getEntityString(ent));
                ent.writeToNBT(entityData);
                this.jarEntity(
                        p,
                        entityData,
                        ent.getName(),
                        ent.posX,
                        ent.posY + ent.height / 2.0f,
                        ent.posZ);
                com.kentington.thaumichorizons.common.lib.THSoundBridge.play(p.world, ent.posX, ent.posY + (ent.getEntityBoundingBox().maxY - ent.getEntityBoundingBox().minY) / 2.0, ent.posZ, "thaumcraft:craftfail", 1.0f, 1.0f);
                ItemFocusContainment.contain.remove(pp);
                ItemFocusContainment.hitCritters.remove(pp);
                p.world.removeEntity(ent);
            } else if (!p.world.isRemote
                    && ItemFocusContainment.contain.get(pp) > ((EntityLiving) ent).getHealth() * 20.0f) {
                        com.kentington.thaumichorizons.common.lib.THSoundBridge.play(p.world, ent.posX, ent.posY + (ent.getEntityBoundingBox().maxY - ent.getEntityBoundingBox().minY) / 2.0, ent.posZ, "thaumcraft:craftfail", 1.0f, 1.0f);
                        p.inventory.decrStackSize(
                                InventoryUtils.isPlayerCarrying(p, new ItemStack(ConfigBlocks.blockJar, 1, 0)),
                                1);
                        final ItemStack jar = new ItemStack(ThaumicHorizons.blockJar);
                        jar.setTagCompound(new NBTTagCompound());
                        jar.getTagCompound().setBoolean("isSoul", true);
                        if (!p.inventory.addItemStackToInventory(jar)) {
                            p.entityDropItem(jar, 1.0f);
                        }
                        if (!p.world.isRemote) {
                            PacketHandler.INSTANCE.sendToAllAround(
                                    new PacketFXContainment(ent.posX, ent.posY, ent.posZ),
                                    new NetworkRegistry.TargetPoint(
                                            p.world.provider.getDimension(),
                                            ent.posX,
                                            ent.posY,
                                            ent.posZ,
                                            32.0));
                        }
                        ItemFocusContainment.contain.remove(pp);
                        ItemFocusContainment.hitCritters.remove(pp);
                        p.world.removeEntity(ent);
                    }
        }
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
            if (entity.canBeCollidedWith() && world.rayTraceBlocks(
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

    boolean canJarEntity(final EntityPlayer p) {
        return InventoryUtils
                .inventoryContains(p.inventory, new ItemStack(ConfigBlocks.blockJar, 1, 0), 0, true, true, false)
                && InventoryUtils
                        .placeItemStackIntoInventory(new ItemStack(ThaumicHorizons.blockJar), p.inventory, 0, false)
                        == null;
    }

    void jarEntity(final EntityPlayer p, final NBTTagCompound tag, final String name, final double x, final double y,
            final double z) {
        p.inventory.decrStackSize(InventoryUtils.isPlayerCarrying(p, new ItemStack(ConfigBlocks.blockJar, 1, 0)), 1);
        final ItemStack jar = new ItemStack(ThaumicHorizons.blockJar);
        jar.setTagCompound(tag);
        jar.getTagCompound().setString("jarredCritterName", name);
        jar.getTagCompound().setBoolean("isSoul", false);
        if (!p.inventory.addItemStackToInventory(jar)) {
            p.entityDropItem(jar, 1.0f);
        }
        if (!p.world.isRemote) {
            PacketHandler.INSTANCE.sendToAllAround(
                    new PacketFXContainment(x, y, z),
                    new NetworkRegistry.TargetPoint(p.world.provider.getDimension(), x, y, z, 32.0));
        }
    }

    @Override
    public boolean isVisCostPerTick(final ItemStack focusstack) {
        return true;
    }

    @Override
    public WandFocusAnimation getAnimation(final ItemStack focusstack) {
        return WandFocusAnimation.CHARGE;
    }

    static {
        ItemFocusContainment.slow = new FocusUpgradeType(
                FocusUpgradeType.types.length,
                new ResourceLocation("thaumichorizons", "textures/foci/slow.png"),
                "focus.upgrade.slow.name",
                "focus.upgrade.slow.text",
                new AspectList().add(Aspect.TRAP, 8));
        ItemFocusContainment.beam = new HashMap<>();
        ItemFocusContainment.hitCritters = new HashMap<>();
        ItemFocusContainment.contain = new HashMap<>();
        ItemFocusContainment.soundDelay = new HashMap<>();
        cost = new AspectList().add(Aspect.AIR, 10).add(Aspect.ENTROPY, 10);
    }
    @Override @SideOnly(Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getIconFromDamage(int metadata) {
        return this.icon;
    }

}
