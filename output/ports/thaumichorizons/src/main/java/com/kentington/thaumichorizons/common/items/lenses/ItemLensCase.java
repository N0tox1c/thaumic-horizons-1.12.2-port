package com.kentington.thaumichorizons.common.items.lenses;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/** Original 18-slot wearable lens case; NBT Inventory/Slot keys remain unchanged. */
public class ItemLensCase extends Item implements IBauble {
    public ItemLensCase() {
        setMaxStackSize(1);
        setHasSubtypes(false);
        setMaxDamage(0);
        setCreativeTab(ThaumicHorizons.tabTH);
    }

    @Override public EnumRarity getRarity(ItemStack stack) { return EnumRarity.RARE; }
    @Override public boolean hasEffect(ItemStack stack) { return false; }
    @Override public String getTranslationKey(ItemStack stack) { return "item.lensCase"; }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (hand == EnumHand.MAIN_HAND && !world.isRemote) {
            player.openGui(ThaumicHorizons.instance, 8, world,
                    MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ));
        }
        return new ActionResult<>(hand == EnumHand.MAIN_HAND ? EnumActionResult.SUCCESS : EnumActionResult.PASS, held);
    }

    public ItemStack[] getInventory(ItemStack caseStack) {
        ItemStack[] result = new ItemStack[18];
        for (int i = 0; i < result.length; i++) result[i] = ItemStack.EMPTY;
        if (!caseStack.isEmpty() && caseStack.hasTagCompound()) {
            NBTTagList entries = caseStack.getTagCompound().getTagList("Inventory", 10);
            for (int i = 0; i < entries.tagCount(); i++) {
                NBTTagCompound entry = entries.getCompoundTagAt(i);
                int slot = entry.getByte("Slot") & 0xff;
                if (slot < result.length) result[slot] = new ItemStack(entry);
            }
        }
        return result;
    }

    public void setInventory(ItemStack caseStack, ItemStack[] contents) {
        if (caseStack.isEmpty()) return;
        NBTTagList entries = new NBTTagList();
        for (int slot = 0; slot < Math.min(18, contents.length); slot++) {
            ItemStack stack = contents[slot];
            if (stack != null && !stack.isEmpty()) {
                NBTTagCompound entry = new NBTTagCompound();
                stack.writeToNBT(entry);
                entry.setByte("Slot", (byte) slot);
                entries.appendTag(entry);
            }
        }
        NBTTagCompound tag = caseStack.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();
        tag.setTag("Inventory", entries);
        caseStack.setTagCompound(tag);
    }

    @Override public BaubleType getBaubleType(ItemStack stack) { return BaubleType.BELT; }
}
