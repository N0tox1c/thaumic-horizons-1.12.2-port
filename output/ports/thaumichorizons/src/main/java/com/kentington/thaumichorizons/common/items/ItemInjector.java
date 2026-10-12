package com.kentington.thaumichorizons.common.items;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityBlastPhial;
import com.kentington.thaumichorizons.common.entities.EntitySyringe;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.IRepairable;

/** Original seven-round injector; maintains ordered ammo NBT and charge behavior. */
public class ItemInjector extends ItemBow implements IRepairable {
    private static final int CHAMBERS = 7;

    public ItemInjector() {
        setMaxStackSize(1);
        setMaxDamage(1000);
        setCreativeTab(ThaumicHorizons.tabTH);
    }

    @Override public String getTranslationKey(ItemStack stack) { return "item.injector"; }
    @Override public int getItemEnchantability() { return 3; }
    @Override public EnumAction getItemUseAction(ItemStack stack) { return EnumAction.NONE; }
    @Override public int getMaxItemUseDuration(ItemStack stack) { return 72000; }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (hand != EnumHand.MAIN_HAND) return new ActionResult<>(EnumActionResult.PASS, stack);
        if (player.isSneaking()) {
            if (!world.isRemote) player.openGui(ThaumicHorizons.instance, 6, world,
                    MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ));
        } else if (getAmmo(stack, 0).isEmpty()) {
            if (!world.isRemote) rotateAmmo(stack);
            world.playSound(null, player.posX, player.posY + player.getEyeHeight(), player.posZ,
                    SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, 1.0F, 1.0F);
        } else {
            player.setActiveHand(hand);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase user, int timeLeft) {
        if (!(user instanceof EntityPlayer) || world.isRemote) return;
        EntityPlayer player = (EntityPlayer) user;
        ItemStack ammo = getAmmo(stack, 0);
        if (ammo.isEmpty()) return;
        int ticks = getMaxItemUseDuration(stack) - timeLeft;
        float charge = ticks / 30.0F;
        charge = Math.min(1.0F, (charge * charge + charge * 2.0F) / 3.0F);
        NBTTagCompound tag = ensureTag(stack);
        tag.setInteger("usetime", 0);
        if (charge < 0.1F) return;

        Entity projectile;
        if (ammo.getItemDamage() == 0) {
            NBTTagCompound effects = ammo.getTagCompound();
            EntitySyringe syringe = new EntitySyringe(world, player, charge * 2.0F,
                    effects == null ? new NBTTagCompound() : effects.copy());
            if (charge == 1.0F) syringe.setIsCritical(true);
            int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
            if (power > 0) syringe.setDamage(syringe.getDamage() + power * 0.5 + 0.5);
            int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
            if (punch > 0) syringe.setKnockbackStrength(punch);
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) syringe.setFire(100);
            projectile = syringe;
        } else {
            projectile = new EntityBlastPhial(world, player, charge * 2.0F, ammo);
        }

        if (!world.spawnEntity(projectile)) return;
        stack.damageItem(1, player);
        world.playSound(null, player.posX, player.posY, player.posZ,
                SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
                1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + charge * 0.5F);
        rotateAmmo(stack);
        tag.setInteger("rotationTarget", (tag.getInteger("rotationTarget") + 90) % 360);
    }

    public ItemStack getAmmo(ItemStack stack, int slot) {
        if (stack == null || stack.isEmpty() || slot < 0 || slot >= CHAMBERS || !stack.hasTagCompound())
            return ItemStack.EMPTY;
        NBTTagList ammo = stack.getTagCompound().getTagList("ammo", 10);
        if (slot >= ammo.tagCount()) return ItemStack.EMPTY;
        NBTTagCompound entry = ammo.getCompoundTagAt(slot);
        return entry.hasKey("id", 8) ? new ItemStack(entry) : ItemStack.EMPTY;
    }

    /** Rotate one chamber, retaining the original seven ordered NBT compounds. */
    public void rotateAmmo(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        NBTTagCompound tag = ensureTag(stack);
        NBTTagList original = tag.getTagList("ammo", 10);
        NBTTagList rotated = new NBTTagList();
        for (int i = 1; i < CHAMBERS; i++) {
            rotated.appendTag(i < original.tagCount()
                    ? original.getCompoundTagAt(i).copy() : new NBTTagCompound());
        }
        rotated.appendTag(new NBTTagCompound());
        tag.setTag("ammo", rotated);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (!player.world.isRemote || stack.isEmpty()) return;
        NBTTagCompound tag = ensureTag(stack);
        tag.setInteger("usetime", tag.getInteger("usetime") + 1);
        tag.setInteger("rotationTarget", tag.getInteger("rotation"));
    }

    private static NBTTagCompound ensureTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        return tag;
    }
}
