//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;
import net.minecraft.util.math.BlockPos;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.IRunicArmor;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileMirror;

public class ItemAmuletMirror extends Item implements IBauble, IRunicArmor  , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider {

    public TextureAtlasSprite icon;

    public ItemAmuletMirror() {
        this.maxStackSize = 1;
        this.canRepair = false;
        this.setMaxDamage(0);
        this.setCreativeTab(ThaumicHorizons.tabTH);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:amuletmirror", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.amuletMirror";
    }

    public EnumRarity getRarity(final ItemStack itemstack) {
        return EnumRarity.RARE;
    }

    public BaubleType getBaubleType(final ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    public int getRunicCharge(final ItemStack itemstack) {
        return 0;
    }

    public boolean canEquip(final ItemStack arg0, final EntityLivingBase arg1) {
        return true;
    }

    public boolean canUnequip(final ItemStack arg0, final EntityLivingBase arg1) {
        return true;
    }

    public void onEquipped(final ItemStack arg0, final EntityLivingBase arg1) {}

    public void onUnequipped(final ItemStack arg0, final EntityLivingBase arg1) {}

    public void onWornTick(final ItemStack arg0, final EntityLivingBase arg1) {}

    public boolean getShareTag() {
        return true;
    }

    public boolean hasEffect(final ItemStack par1ItemStack) {
        return par1ItemStack.hasTagCompound();
    }

    @Override
    public net.minecraft.util.EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos,
            net.minecraft.util.EnumFacing side, float hitX, float hitY, float hitZ, net.minecraft.util.EnumHand hand) {
        return onItemUseFirst(player.getHeldItem(hand), player, world, pos.getX(), pos.getY(), pos.getZ(), side.getIndex(), hitX, hitY, hitZ)
                ? net.minecraft.util.EnumActionResult.SUCCESS : net.minecraft.util.EnumActionResult.PASS;
    }

    public boolean onItemUseFirst(final ItemStack itemstack, final EntityPlayer player, final World world, final int x,
            final int y, final int z, final int par7, final float par8, final float par9, final float par10) {
        final Block bi = world.getBlockState(new BlockPos(x, y, z)).getBlock();
        if (bi != ConfigBlocks.blockMirror) {
            return false;
        }
        if (world.isRemote) {
            player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
            return false;
        }
        final TileEntity tm = world.getTileEntity(new BlockPos(x, y, z));
        if (tm instanceof TileMirror) {
            itemstack.setTagInfo("linkX", new NBTTagInt(tm.getPos().getX()));
            itemstack.setTagInfo("linkY", new NBTTagInt(tm.getPos().getY()));
            itemstack.setTagInfo("linkZ", new NBTTagInt(tm.getPos().getZ()));
            itemstack.setTagInfo("linkDim", new NBTTagInt(world.provider.getDimension()));
            itemstack.setTagInfo(
                    "dimname",
                    new NBTTagString(world.provider.getDimensionType().getName()));
            com.kentington.thaumichorizons.common.lib.THSoundBridge.play(world, x, y, z, "thaumcraft:jar", 1.0f, 2.0f);
            player.sendMessage(
                    new TextComponentString(
                            TextFormatting.GRAY + I18n.translateToLocal("tc.handmirrorlinked")));
            player.inventoryContainer.detectAndSendChanges();
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(final ItemStack item, final World par2EntityPlayer, final List<String> list,
            final net.minecraft.client.util.ITooltipFlag par4) {
        if (item.hasTagCompound()) {
            final int lx = item.getTagCompound().getInteger("linkX");
            final int ly = item.getTagCompound().getInteger("linkY");
            final int lz = item.getTagCompound().getInteger("linkZ");
            final int ldim = item.getTagCompound().getInteger("linkDim");
            final String dimname = item.getTagCompound().getString("dimname");
            list.add(
                    I18n.translateToLocal(
                            "tc.handmirrorlinkedto") + " " + lx + "," + ly + "," + lz + " in " + dimname);
        }
    }
}
