//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.util.math.BlockPos;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileSoulJar;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSoulJarItem extends ItemBlock  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider {

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon;

    public BlockSoulJarItem(final Block p_i45328_1_) {
        super(p_i45328_1_);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        final ItemStack soul = new ItemStack(this, 1, 0);
        soul.setTagCompound(new NBTTagCompound());
        soul.getTagCompound().setBoolean("isSoul", true);
        par3List.add(soul);
    }

    public String getItemStackDisplayName(final ItemStack stack) {
        String stringy = I18n.translateToLocal("item.jarredSoul.jarred");
        if (stack.hasTagCompound()) {
            stringy = stringy + " " + stack.getTagCompound().getString("jarredCritterName");
            if (stack.getTagCompound().getBoolean("isSoul")) {
                stringy = stringy + " " + I18n.translateToLocal("item.jarredSoul.soul");
            }
        } else {
            stringy = I18n.translateToLocal("item.jarredSoul.0.name");
        }
        return stringy;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumcraft:blank", "blocks"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public int getMetadata(final int par1) {
        return par1;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
            net.minecraft.util.EnumFacing side, float hitX, float hitY, float hitZ,
            net.minecraft.block.state.IBlockState state) {
        if (!super.placeBlockAt(stack,player,world,pos,side,hitX,hitY,hitZ,state)) return false;
        TileEntity tile=world.getTileEntity(pos);
        if (tile instanceof TileSoulJar && stack.hasTagCompound()) {
            TileSoulJar jar=(TileSoulJar)tile;
            jar.jarTag=stack.getTagCompound().copy();
            jar.markDirty();
            com.kentington.thaumichorizons.common.lib.WorldAccess.markBlockForUpdate(world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    public boolean getShareTag() {
        return true;
    }
    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, net.minecraft.util.NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) getSubItems(this, tab, items);
    }

}
