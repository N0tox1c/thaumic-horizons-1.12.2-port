//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityGolemTH;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGolemPowder extends Item  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon;

    public ItemGolemPowder() {
        this.setMaxStackSize(64);
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:golempowder", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.golemPowder";
    }

    @Override
    public net.minecraft.util.EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
            net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing side, float hitX, float hitY, float hitZ) {
        return onItemUse(player.getHeldItem(hand), player, world, pos.getX(), pos.getY(), pos.getZ(), side.getIndex(), hitX, hitY, hitZ)
                ? net.minecraft.util.EnumActionResult.SUCCESS : net.minecraft.util.EnumActionResult.FAIL;
    }

    public boolean onItemUse(final ItemStack p_77648_1_, final EntityPlayer player, final World world,
            final int p_77648_4_, final int p_77648_5_, final int p_77648_6_, final int p_77648_7_,
            final float p_77648_8_, final float p_77648_9_, final float p_77648_10_) {
        final Block blocky = world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)).getBlock();
        final int md = world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)));
        if (!player.canPlayerEdit(new net.minecraft.util.math.BlockPos(p_77648_4_, p_77648_5_, p_77648_6_), net.minecraft.util.EnumFacing.byIndex(p_77648_7_), p_77648_1_)) {
            return false;
        }
        if (world.isRemote) {
            return true;
        }
        if (!blocky.hasTileEntity(world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_))) && !blocky.isAir(world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)), world, new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_))
                && (world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)).isOpaqueCube() || ItemFocusAnimation.isWhitelisted(blocky, md))
                && world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)).getBlockHardness(world, new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)) != -1.0f) {
if (!world.isRemote) {
                final EntityGolemTH golem = new EntityGolemTH(world);
                golem.loadGolem(p_77648_4_ + 0.5, p_77648_5_, p_77648_6_ + 0.5, blocky, md, 1200, false, false, false);
                if (!com.kentington.thaumichorizons.common.lib.BlockInteraction.canBreak(world, player, new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_))) {
                    golem.setDead();
                    return false;
                }
                world.setBlockToAir(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_));
                com.kentington.thaumichorizons.common.lib.THSoundBridge.play(world, p_77648_4_ + 0.5, p_77648_5_ + 0.5, p_77648_6_ + 0.5, "thaumcraft:wand", 1.0f, 1.0f);
                golem.setHomePosAndDistance(new net.minecraft.util.math.BlockPos((int) golem.posX, (int) golem.posY, (int) golem.posZ), 32);
                golem.setOwner(player.getName());
                world.spawnEntity(golem);
                world.setEntityState(golem, (byte) 7);
            } else {
                Minecraft.getMinecraft().effectRenderer
                        .addBlockDestroyEffects(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_), world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)));
                player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
            }
            return true;
        }
        return false;
    }
}
