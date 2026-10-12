package fox.spiteful.forbidden.items;

import java.util.List;

import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.compat.Compat;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import fox.spiteful.forbidden.compat.BloodRecipes;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.orb.IBloodOrb;
import fox.spiteful.forbidden.compat.BloodNetwork;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.IWarpingGear;

public class ItemDivineOrb extends Item implements IBloodOrb, IBindable, IWarpingGear {
    @Override
    public WayofTime.bloodmagic.core.data.Binding getBinding(ItemStack stack) {
        return WayofTime.bloodmagic.core.data.Binding.fromStack(stack);
    }

    private net.minecraft.client.renderer.texture.TextureAtlasSprite itemIcon;
    private static final WayofTime.bloodmagic.orb.BloodOrb DIVINE_ORB = new WayofTime.bloodmagic.orb.BloodOrb("forbidden_divine", 6, 80000000, 140);
    @Override public WayofTime.bloodmagic.orb.BloodOrb getOrb(ItemStack stack) { return DIVINE_ORB; }
    public ItemDivineOrb() {
        setMaxStackSize(1);
        setCreativeTab(Forbidden.tab);
        BloodRecipes.registerAltarOrbRecipe(new ItemStack(this), 6, 140);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap iconRegister) {
        this.itemIcon = iconRegister.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:divineorb"));
    }

    @Override
    public void addInformation(ItemStack stack, @javax.annotation.Nullable World world, List<String> list, net.minecraft.client.util.ITooltipFlag flags) {
        list.add(I18n.translateToLocal("tooltip.divineorb"));

        if (!(stack.getTagCompound() == null)) {
            list.add(I18n.translateToLocal("tooltip.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
        }

    }

    @Override
    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, net.minecraft.util.EnumHand hand) {
        return new net.minecraft.util.ActionResult<>(net.minecraft.util.EnumActionResult.SUCCESS, legacyRightClick(player.getHeldItem(hand), world, player));
    }
    public ItemStack legacyRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        if (!Compat.bm)
            return itemstack;

        BloodNetwork.checkAndSetItemOwner(itemstack, player);

        if (world != null) {
            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;
            fox.spiteful.forbidden.compat.NativeCalls.playSound(world, (double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        }
        if (world.isRemote) {
            return itemstack;
        }

        NBTTagCompound itemTag = itemstack.getTagCompound();

        if (itemTag == null || itemTag.getString("ownerName").equals("")) {
            return itemstack;
        }

        if(itemTag.getString("ownerName").equals(BloodNetwork.getUsername(player)))
        {
            BloodNetwork.setMaxOrbToMax(itemTag.getString("ownerName"), 6);
        }

        BloodNetwork.addCurrentEssenceToMaximum(itemTag.getString("ownerName"), 200, 700000000);
        hurtPlayer(player, 200);

        return itemstack;
    }

    public int getMaxEssence() {
        return 80000000;
    }

    public int getOrbLevel() {
        return 6;
    }

    @Override
    public int getWarp(ItemStack itemstack, EntityPlayer player) {
        return 5;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.EPIC;
    }

    @Override
    public boolean hasContainerItem()
    {
        return true;
    }

    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack)
    {
        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return itemStack;
    }

    private void hurtPlayer(EntityPlayer user, int energySyphoned)
    {
        if (energySyphoned < 100 && energySyphoned > 0)
        {
            if (!user.capabilities.isCreativeMode)
            {
                user.setHealth((user.getHealth() - 1));
                if (user.getHealth() <= 0.0005f)
                {
                    user.getCombatTracker().trackDamage(new DamageSource("blooderp"), 1, 1);
                    user.onDeath(new DamageSource("blooderp"));
                }
            }
        } else if (energySyphoned >= 100)
        {
            if (!user.capabilities.isCreativeMode)
            {
                for (int i = 0; i < ((energySyphoned + 99) / 100); i++)
                {
                    user.setHealth((user.getHealth() - 1));
                    if (user.getHealth() <= 0.0005f)
                    {
                        user.getCombatTracker().trackDamage(new DamageSource("blooderp"), 1, 1);
                        user.onDeath(new DamageSource("blooderp"));
                        break;
                    }
                }
            }
        }
    }
}