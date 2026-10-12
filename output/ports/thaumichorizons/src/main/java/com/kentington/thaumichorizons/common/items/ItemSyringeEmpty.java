package com.kentington.thaumichorizons.common.items;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Direct 1.12 port of the original empty syringe.
 * * No hit -> self blood sample (original behavior)
 * * Eligible passive/ambient/water living creature -> genetic blood sample
 * * Undead, NPC and merchant targets are rejected, as in original
 * * Original "critterName" + "critter" NBT keys retained for Vat readers
 *
 * The original ItemFocusContainment ray helper is replaced with a standalone
 * server-side eye raycast so this class does not depend on its unfinished port.
 */
public class ItemSyringeEmpty extends Item {
    private static final double REACH = 1.5D;

    public ItemSyringeEmpty() {
        setCreativeTab(ThaumicHorizons.tabTH);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> stacks) {
        if (isInCreativeTab(tab)) stacks.add(new ItemStack(this, 1, 2));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "item.syringeEmpty";
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (world.isRemote || held.isEmpty()) {
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }

        EntityLiving target = pointedLiving(world, player);
        ItemStack result;
        if (target != null) {
            if (!eligible(target)) return new ActionResult<>(EnumActionResult.PASS, held);
            result = new ItemStack(ThaumicHorizons.itemSyringeBloodSample);
            NBTTagCompound sampleTag = new NBTTagCompound();
            NBTTagCompound creatureTag = new NBTTagCompound();
            target.writeToNBT(creatureTag);
            if (EntityList.getKey(target) == null) {
                return new ActionResult<>(EnumActionResult.FAIL, held);
            }
            creatureTag.setString("id", EntityList.getKey(target).toString());
            stripVolatileEntityData(creatureTag);
            sampleTag.setString("critterName", target.getName());
            sampleTag.setTag("critter", creatureTag);
            result.setTagCompound(sampleTag);
        } else {
            // Original self-sampling path. ItemSyringeHuman is the blood item,
            // not the creature sample item.
            result = new ItemStack(ThaumicHorizons.itemSyringeHuman);
        }
        if (!player.inventory.addItemStackToInventory(result)) {
            return new ActionResult<>(EnumActionResult.FAIL, held);
        }
        held.shrink(1);
        player.inventory.markDirty();
        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }

    private static boolean eligible(EntityLiving entity) {
        return entity.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD
                && !(entity instanceof INpc)
                && !(entity instanceof IMerchant)
                && (entity.isCreatureType(EnumCreatureType.CREATURE, false)
                || entity.isCreatureType(EnumCreatureType.AMBIENT, false)
                || entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false));
    }

    /**
     * Preserve genetic state and CreatureInfusion; discard live identity,
     * inventory and world position so a later clone does not inherit them.
     */
    private static void stripVolatileEntityData(NBTTagCompound nbt) {
        for (String key : new String[] {
                "Items", "ArmorItem", "ArmorItems", "HandItems", "SaddleItem",
                "ChestedHorse", "UUID", "UUIDMost", "UUIDLeast", "Pos",
                "Motion", "Rotation", "Passengers", "Riding", "Leash", "Leashed",
                "Dimension", "PortalCooldown", "Fire", "FallDistance"}) {
            nbt.removeTag(key);
        }
    }

    /** Resolve the nearest entity hit, without through-wall sampling. */
    private static EntityLiving pointedLiving(World world, EntityPlayer player) {
        Vec3d eye = player.getPositionEyes(1.0F);
        Vec3d end = eye.add(player.getLookVec().scale(REACH));
        AxisAlignedBB area = player.getEntityBoundingBox()
                .expand(player.getLookVec().x * REACH, player.getLookVec().y * REACH,
                        player.getLookVec().z * REACH).grow(1.0D);
        EntityLiving nearest = null;
        double distance = REACH * REACH;
        List<EntityLiving> candidates = world.getEntitiesWithinAABB(EntityLiving.class, area);
        for (EntityLiving candidate : candidates) {
            if (!candidate.canBeCollidedWith() || !player.canEntityBeSeen(candidate)) continue;
            AxisAlignedBB bb = candidate.getEntityBoundingBox().grow(candidate.getCollisionBorderSize());
            RayTraceResult collision = bb.calculateIntercept(eye, end);
            if (collision == null) continue;
            double d = eye.squareDistanceTo(collision.hitVec);
            if (d < distance) {
                nearest = candidate;
                distance = d;
            }
        }
        return nearest;
    }
}
