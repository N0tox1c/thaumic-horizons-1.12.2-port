//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.entities.EntityGolemTH;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.math.MathHelper;
import thaumcraft.common.entities.golems.ItemGolemBell;

public class ItemGolemPlacer extends thaumcraft.common.entities.golems.ItemGolemPlacer  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider, com.kentington.thaumichorizons.common.lib.OriginalItemColor , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    public TextureAtlasSprite icon;
    public TextureAtlasSprite newBell;

    public ItemGolemPlacer() {
        this.setCreativeTab(null);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:golem", "items"));
        this.newBell = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:newbell", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack stack, final int p_82790_2_) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("block")) {
            return 0;
        }
        net.minecraft.block.state.IBlockState state = com.kentington.thaumichorizons.common.lib.GolemMaterial.read(stack.getTagCompound());
        if (state.getBlock() == Blocks.AIR) return 0;
        final int color = state.getMapColor(net.minecraft.client.Minecraft.getMinecraft().world, net.minecraft.util.math.BlockPos.ORIGIN).colorValue;
        if (color != 0) {
            return color;
        }
        return -1;
    }

    @Override
    public void addInformation(final ItemStack stack, final World world, final List<String> list,
            final net.minecraft.client.util.ITooltipFlag flag) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("block")) {
            net.minecraft.block.state.IBlockState material = com.kentington.thaumichorizons.common.lib.GolemMaterial.read(stack.getTagCompound());
            Block block = material.getBlock();
            final ItemStack blockStack = new ItemStack(block, 1, block.getMetaFromState(material));
            if (!blockStack.isEmpty()) {
                list.add(blockStack.getDisplayName());
            } else if (block == Blocks.AIR) {
                list.add("Voidling");
            } else {
                list.add(block.getLocalizedName());
            }
        }
        super.addInformation(stack, world, list, flag);
    }

    public boolean spawnCreature(final World par0World, final double par2, final double par4, final double par6,
            final int side, final ItemStack stack, final EntityPlayer player) {
        if (par0World.isRemote) return false;
        boolean adv = stack.hasTagCompound() && stack.getTagCompound().hasKey("advanced");
        final EntityGolemTH golem = new EntityGolemTH(par0World);
        if (golem != null) {
            golem.setLocationAndAngles(par2, par4, par6, par0World.rand.nextFloat() * 360.0f, 0.0f);
            golem.playLivingSound();
            golem.setHomePosAndDistance(new net.minecraft.util.math.BlockPos(MathHelper.floor(par2), MathHelper.floor(par4), MathHelper.floor(par6)), 32);
            net.minecraft.block.state.IBlockState material = com.kentington.thaumichorizons.common.lib.GolemMaterial.read(stack.getTagCompound());
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("core")) {
                golem.setCore(stack.getTagCompound().getByte("core"));
            }
            String deco = "";
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("deco")) {
                deco = stack.getTagCompound().getString("deco");
                golem.decoration = deco;
            }
            golem.setup(side);
            golem.loadGolem(
                    golem.posX,
                    golem.posY,
                    golem.posZ,
                    material.getBlock(),
                    material.getBlock().getMetaFromState(material),
                    600,
                    adv,
                    stack.hasTagCompound() && stack.getTagCompound().getBoolean("berserk"),
                    stack.hasTagCompound() && stack.getTagCompound().getBoolean("explosive"));
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("upgrades")) {
                final int ul = golem.upgrades.length;
                golem.upgrades = stack.getTagCompound().getByteArray("upgrades");
                if (ul != golem.upgrades.length) {
                    final byte[] tt = new byte[ul];
                    for (int a = 0; a < ul; ++a) {
                        tt[a] = -1;
                    }
                    for (int a = 0; a < golem.upgrades.length; ++a) {
                        if (a < ul) {
                            tt[a] = golem.upgrades[a];
                        }
                    }
                    golem.upgrades = tt;
                }
            }
            golem.setGolemDecoration(deco);
            golem.setOwner(player.getName());
            golem.setMarkers(ItemGolemBell.getMarkers(stack));
            int a2 = 0;
            for (final byte b : golem.upgrades) {
                golem.setUpgrade(a2, b);
                ++a2;
            }
            if (stack.hasDisplayName()) {
                golem.setCustomNameTag(stack.getDisplayName());
                golem.enablePersistence();
            }
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Inventory")) {
                final NBTTagList nbttaglist2 = stack.getTagCompound().getTagList("Inventory", 10);
                golem.inventory.readFromNBT(nbttaglist2);
            }
        }
        return par0World.spawnEntity(golem);
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.golemPlacer";
    }
}
