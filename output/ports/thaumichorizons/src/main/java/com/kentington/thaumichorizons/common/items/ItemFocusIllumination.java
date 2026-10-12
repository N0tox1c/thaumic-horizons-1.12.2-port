//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;
import net.minecraft.util.math.BlockPos;


import java.util.List;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ItemFocusIllumination extends ItemFocusBasic  implements com.kentington.thaumichorizons.common.lib.OriginalItemAppearance , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider {

    public static FocusUpgradeType solar;
    public static final int[] colors;
    private static final AspectList cost;
    public static final TextureAtlasSprite[] icons;

    public ItemFocusIllumination() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        for (int i = 0; i < 16; ++i) {
            ItemFocusIllumination.icons[i] = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:focus_illumination." + i, "items"));
        }
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int damage) {
        if (damage > 15) {
            return null;
        }
        return ItemFocusIllumination.icons[damage];
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item p_150895_1_, final CreativeTabs p_150895_2_, final List p_150895_3_) {
        for (int i = 0; i < 16; ++i) {
            p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
        }
    }

    @Override
    public String getSortingHelper(final ItemStack itemstack) {
        return "I" + itemstack.getItemDamage() + super.getSortingHelper(itemstack);
    }

    public String getItemStackDisplayName(final ItemStack p_77653_1_) {
        return I18n.translateToLocal("item.focusIllumination." + p_77653_1_.getItemDamage() + ".name");
    }

    @Override
    public int getFocusColor(final ItemStack focusstack) {
        if (focusstack.getItemDamage() < 16) {
            return ItemFocusIllumination.colors[focusstack.getItemDamage()];
        }
        return 420;
    }

    @Override
    public AspectList getVisCost(final ItemStack focusstack) {
        return ItemFocusIllumination.cost;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(final ItemStack focusstack, final int rank) {
        switch (rank) {
    case 1: case 5: case 4: case 2: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal };
            } 
    case 3: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, ItemFocusIllumination.solar };
            } 
    default: {
                return null;
            }
}
    }

    @Override
    public ItemStack onFocusRightClick(final ItemStack itemstack, final World world, final EntityPlayer player,
            final RayTraceResult mop) {
        final ItemWandCasting wand = (ItemWandCasting) itemstack.getItem();
        if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
            int x = mop.getBlockPos().getX();
            int y = mop.getBlockPos().getY();
            int z = mop.getBlockPos().getZ();
            switch (mop.sideHit.getIndex()) {
    case 0: {
                    --y;
                } break;
    case 1: {
                    ++y;
                } break;
    case 2: {
                    --z;
                } break;
    case 3: {
                    ++z;
                } break;
    case 4: {
                    --x;
                } break;
    case 5: {
                    ++x;
                } break;
}
            if (!world.isRemote && world.isBlockModifiable(player,new BlockPos(x,y,z))
                    && player.canPlayerEdit(new BlockPos(x,y,z),mop.sideHit,itemstack)
                    && world.isAirBlock(new BlockPos(x, y, z))
                    && wand.consumeAllVis(itemstack, player, this.getVisCost(itemstack), true, false)) {
                if (this.getUpgradeLevel(wand.getFocusItem(itemstack), ItemFocusIllumination.solar) > 0) {
                    world.setBlockState(new BlockPos(x, y, z), ThaumicHorizons.blockLightSolar.getStateFromMeta(wand.getFocusItem(itemstack).getItemDamage()), 3);
                } else {
                    world.setBlockState(new BlockPos(x, y, z), ThaumicHorizons.blockLight.getStateFromMeta(wand.getFocusItem(itemstack).getItemDamage()), 3);
                }
                player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
                if (!world.isRemote) {
                    com.kentington.thaumichorizons.common.lib.THSoundBridge.play(world, x + 0.5, y + 0.5, z + 0.5, "thaumcraft:wand", 1.0f, 1.0f);
                }
            }
        }
        return itemstack;
    }

    static {
        ItemFocusIllumination.solar = new FocusUpgradeType(
                FocusUpgradeType.types.length,
                new ResourceLocation("thaumichorizons", "textures/foci/solar.png"),
                "focus.upgrade.solar.name",
                "focus.upgrade.solar.text",
                new AspectList().add(Aspect.ORDER, 8).add(Aspect.VOID, 4));
        colors = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131,
                14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320 };
        cost = new AspectList().add(Aspect.FIRE, 100).add(Aspect.AIR, 100);
        icons = new TextureAtlasSprite[16];
    }
    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, net.minecraft.util.NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) getSubItems(this, tab, items);
    }

}
