package com.kentington.thaumichorizons.common.items;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityBlastPhial;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * Direct port of ItemSyringeInjection behaviour, not a substitute potion.
 * Metadata 0 is a self-injection; nonzero metadata throws a phial.
 * Vanilla PotionUtils reads original "CustomPotionEffects" NBT entries.
 * Original GUI/injection color overlays still need 1.12 model registrations.
 */
public class ItemSyringeInjection extends ItemPotion {
    public ItemSyringeInjection() {
        setHasSubtypes(true);
        setMaxStackSize(1);
        setCreativeTab(ThaumicHorizons.tabTH);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        // Original injections are created through Blood Infuser, not creative tab.
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 8;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal("item.injection." + stack.getMetadata() + ".name");
    }

    private static void applyInjection(ItemStack stack, EntityLivingBase target) {
        if (target.world.isRemote) return;
        for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
            target.addPotionEffect(new PotionEffect(effect));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack=player.getHeldItem(hand);
        if (stack.isEmpty()) return new ActionResult<>(EnumActionResult.PASS,stack);
        if (stack.getMetadata()!=0) {
            world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F,
                    0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if (!world.isRemote) {
                world.spawnEntity(new EntityBlastPhial(world, player, 0.5F, stack.copy()));
                if (!player.capabilities.isCreativeMode) stack.shrink(1);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS,stack);
        }
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS,stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (!world.isRemote) applyInjection(stack, entity);
        if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).capabilities.isCreativeMode) {
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        applyInjection(stack,target);
        stack.shrink(1); // matches original on hit, including in creative
        return true;
    }

    /** For an IItemColor registered by the future 1.12 client proxy. */
    public int getInjectionColor(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("color",3)
                ? stack.getTagCompound().getInteger("color") : 0xFFFFFF;
    }
}
