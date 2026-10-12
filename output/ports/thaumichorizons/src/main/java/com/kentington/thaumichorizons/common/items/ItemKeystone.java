//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;
import net.minecraft.util.math.BlockPos;


import java.util.List;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.lib.PocketPlaneData;
import com.kentington.thaumichorizons.common.tiles.TileVortex;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKeystone extends Item  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider, com.kentington.thaumichorizons.common.lib.OriginalItemColor , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon;

    public ItemKeystone() {
        this.setCreativeTab(ThaumicHorizons.tabTH);
        this.setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:keystone", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        if (par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().getInteger("dimension") != 0) {
            return "item.keystoneTH";
        }
        return "item.keystoneBlank";
    }

    @Override
    public void addInformation(final ItemStack par1ItemStack, final World par2EntityPlayer, final List<String> par3List,
            final net.minecraft.client.util.ITooltipFlag par4) {
        if (par1ItemStack.getTagCompound() != null) {
            final int dim = par1ItemStack.getTagCompound().getInteger("dimension");
            if (dim >= 0 && dim < PocketPlaneData.planes.size()) {
                par3List.add(PocketPlaneData.planes.get(dim).name);
            }
        }
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int p_82790_2_) {
        if (par1ItemStack.getTagCompound() != null) {
            int dimID = par1ItemStack.getTagCompound().getInteger("dimension");
            if (dimID >= 0 && dimID < PocketPlaneData.planes.size()) return PocketPlaneData.planes.get(dimID).color;
            else par1ItemStack = null;
        }
        return 16777215;
    }

    @Override
    public net.minecraft.util.EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos,
            net.minecraft.util.EnumFacing side, float hitX, float hitY, float hitZ, net.minecraft.util.EnumHand hand) {
        return onItemUseFirst(player.getHeldItem(hand), player, world, pos.getX(), pos.getY(), pos.getZ(), side.getIndex(), hitX, hitY, hitZ)
                ? net.minecraft.util.EnumActionResult.SUCCESS : net.minecraft.util.EnumActionResult.PASS;
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (stack.getTagCompound() == null && player.dimension == ThaumicHorizons.dimensionPocketId
                && !world.isRemote) {
            if (world.getTileEntity(new BlockPos(x, y, z)) instanceof TileVortex) {
            final TileVortex vortex = ((TileVortex) (world.getTileEntity(new BlockPos(x, y, z))));

                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setInteger("dimension", vortex.dimensionID);
                player.inventory.markDirty();
                return true;
            }
            return false;
        }
        return false;
    }
}
