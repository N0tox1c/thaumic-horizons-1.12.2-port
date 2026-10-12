package fox.spiteful.forbidden.items.scribes;

import java.util.List;

import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.compat.Compat;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import thaumcraft.api.IScribeTools;
import WayofTime.bloodmagic.iface.IBindable;
import fox.spiteful.forbidden.compat.BloodNetwork;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBloodwell extends Item implements IScribeTools, IBindable {
    @Override
    public WayofTime.bloodmagic.core.data.Binding getBinding(ItemStack stack) {
        return WayofTime.bloodmagic.core.data.Binding.fromStack(stack);
    }


    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon;

    public ItemBloodwell() {
        maxStackSize = 1;
        canRepair = false;
        setMaxDamage(5);
        setCreativeTab(Forbidden.tab);
        setHasSubtypes(false);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        this.icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:bloodwell"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int par1) {
        return this.icon;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity fauxplayer, int z, boolean par5) {
        if (Compat.bm && stack.getItemDamage() > 0 && stack.hasTagCompound()) {
            EntityPlayer player = null;
            if (fauxplayer instanceof EntityPlayer)
                player = (EntityPlayer) fauxplayer;
            else
                return;

            if (player.capabilities.isCreativeMode)
                stack.setItemDamage(0);
            else if (BloodNetwork.syphonFromNetwork(stack, 25) > 0)
                stack.setItemDamage(stack.getItemDamage() - 1);
            else if (player.getHealth() > 6) {
                player.setHealth(player.getHealth() - 2);
                stack.setItemDamage(stack.getItemDamage() - 1);
            }
        }
    }

    @Override
    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, net.minecraft.util.EnumHand hand) {
        return new net.minecraft.util.ActionResult<>(net.minecraft.util.EnumActionResult.SUCCESS, legacyRightClick(player.getHeldItem(hand), world, player));
    }
    public ItemStack legacyRightClick(ItemStack itemstack, World world, EntityPlayer player) {
           BloodNetwork.checkAndSetItemOwner(itemstack, player);
        return itemstack;
    }

    @Override
    public void addInformation(ItemStack stack, @javax.annotation.Nullable World world, List<String> list, net.minecraft.client.util.ITooltipFlag flags) {
        if (stack.hasTagCompound()) {
            list.add("");
            list.add(I18n.translateToLocal("tooltip.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (damage > 0) {
            if (BloodNetwork.syphonFromNetwork(stack, 25 * damage) > 0)
                super.setDamage(stack, 0);
            else
                super.setDamage(stack, damage);
        } else
            super.setDamage(stack, damage);
    }
}