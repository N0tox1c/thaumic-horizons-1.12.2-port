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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileVortex;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.tiles.TileNode;

public class ItemNodeCheat extends Item  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon1;

    public ItemNodeCheat() {
        this.setCreativeTab(ThaumicHorizons.tabTH);
        this.setTranslationKey("nodeCheat");
    }

    public boolean onItemUse(final ItemStack p_77648_1_, final EntityPlayer p_77648_2_, final World p_77648_3_,
            final int p_77648_4_, final int p_77648_5_, final int p_77648_6_, final int p_77648_7_,
            final float p_77648_8_, final float p_77648_9_, final float p_77648_10_) {
        if (p_77648_3_.isRemote) return p_77648_3_.getTileEntity(new BlockPos(p_77648_4_,p_77648_5_,p_77648_6_)) instanceof TileNode;
        if (!com.kentington.thaumichorizons.common.lib.BlockInteraction.canBreak(p_77648_3_,p_77648_2_,
                new BlockPos(p_77648_4_,p_77648_5_,p_77648_6_))) return false;
        final TileEntity te = p_77648_3_.getTileEntity(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_));
        if (te instanceof TileNode) {
            final AspectList aspects = ((TileNode) te).getAspectsBase();
            p_77648_3_.setBlockState(new net.minecraft.util.math.BlockPos(p_77648_4_, p_77648_5_, p_77648_6_), (ThaumicHorizons.blockVortex).getDefaultState(), 3);
            final TileVortex tco = (TileVortex) p_77648_3_.getTileEntity(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_));
            tco.aspects = aspects.copy();
            return tco.cheat = true;
        }
        return false;
    }
    @Override
    public net.minecraft.util.EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
            net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing side,float hitX,float hitY,float hitZ) {
        return onItemUse(player.getHeldItem(hand),player,world,pos.getX(),pos.getY(),pos.getZ(),side.getIndex(),hitX,hitY,hitZ)
                ? net.minecraft.util.EnumActionResult.SUCCESS : net.minecraft.util.EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon1 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:nodecheat", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon1;
    }

    @Override
    public void addInformation(final ItemStack par1ItemStack, final World par2EntityPlayer, final List<String> par3List,
            final net.minecraft.client.util.ITooltipFlag par4) {
        par3List.add("Use on a node to transform it");
        par3List.add("into a self-stabilized vortex");
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }
}
