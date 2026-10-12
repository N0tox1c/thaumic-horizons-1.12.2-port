//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.lib.EntityInfusionProperties;
import com.kentington.thaumichorizons.common.lib.SelfInfusionRecipe;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.Thaumcraft;

public class ItemInfusionSelfCheat extends Item  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon1;

    public TextureAtlasSprite icon2;
    public TextureAtlasSprite icon3;
    public TextureAtlasSprite icon4;
    public TextureAtlasSprite icon5;
    public TextureAtlasSprite icon6;
    public TextureAtlasSprite icon7;
    public TextureAtlasSprite icon8;
    public TextureAtlasSprite icon9;
    public TextureAtlasSprite icon10;

    public ItemInfusionSelfCheat() {
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon1 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:quicksilverlimbs", "items"));
        this.icon2 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:morphicfingers", "items"));
        this.icon3 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:awakenedblood", "items"));
        this.icon4 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:diamondskin", "items"));
        this.icon5 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:silverwoodheart", "items"));
        this.icon6 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:synthskin", "items"));
        this.icon7 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("fish_cod_raw", "items"));
        this.icon8 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:warpedtumor", "items"));
        this.icon9 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:spiderclimb", "items"));
        this.icon10 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:chameleonskin", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        switch (par1) {
    case 1: {
                return this.icon1;
            }
    case 2: {
                return this.icon2;
            }
    case 3: {
                return this.icon3;
            }
    case 4: {
                return this.icon4;
            }
    case 5: {
                return this.icon5;
            }
    case 6: {
                return this.icon6;
            }
    case 7: {
                return this.icon7;
            }
    case 8: {
                return this.icon8;
            }
    case 9: {
                return this.icon9;
            }
    case 10: {
                return this.icon10;
            }
    default: {
                return null;
            }
}
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(this, 1, 1));
        par3List.add(new ItemStack(this, 1, 2));
        par3List.add(new ItemStack(this, 1, 3));
        par3List.add(new ItemStack(this, 1, 4));
        par3List.add(new ItemStack(this, 1, 5));
        par3List.add(new ItemStack(this, 1, 6));
        par3List.add(new ItemStack(this, 1, 7));
        par3List.add(new ItemStack(this, 1, 8));
        par3List.add(new ItemStack(this, 1, 9));
        par3List.add(new ItemStack(this, 1, 10));
    }

    public String getItemStackDisplayName(final ItemStack stack) {
        String stringy = "";
        switch (stack.getItemDamage()) {
    case 1: {
                stringy += I18n.translateToLocal("selfInfusions.quicksilver");
            } break;
    case 2: {
                stringy += I18n.translateToLocal("selfInfusions.morphic");
            } break;
    case 3: {
                stringy += I18n.translateToLocal("selfInfusions.awakeBlood");
            } break;
    case 4: {
                stringy += I18n.translateToLocal("selfInfusions.diamondSkin");
            } break;
    case 5: {
                stringy += I18n.translateToLocal("selfInfusions.silverHeart");
            } break;
    case 6: {
                stringy += I18n.translateToLocal("selfInfusions.synthSkin");
            } break;
    case 7: {
                stringy += I18n.translateToLocal("selfInfusions.amphibious");
            } break;
    case 8: {
                stringy += I18n.translateToLocal("selfInfusions.warpedTumor");
            } break;
    case 9: {
                stringy += I18n.translateToLocal("selfInfusions.spiderClimb");
            } break;
    case 10: {
                stringy += I18n.translateToLocal("selfInfusions.chameleonSkin");
            } break;
}
        return stringy;
    }

    public ItemStack onItemRightClick(final ItemStack p_77659_1_, final World world, final EntityPlayer p) {
        for (final SelfInfusionRecipe recipe : ThaumicHorizons.selfRecipes) {
            if (recipe.getID() == p_77659_1_.getItemDamage()
                    && !((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(p))
                            .hasPlayerInfusion(recipe.getID())) {
                if (world.isRemote) {
                    Thaumcraft.proxy.burst(world,p.posX,p.posY+p.getEyeHeight(),p.posZ,1.0F);
                    return p_77659_1_;
                }
                ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(p))
                        .addPlayerInfusion(recipe.getID());
                ThaumicHorizons.instance.eventHandlerEntity.applyInfusions(p);
                Thaumcraft.proxy.burst(world, p.posX, p.posY + ((EntityLivingBase) p).getEyeHeight(), p.posZ, 1.0f);
                return p_77659_1_;
            }
        }
        return p_77659_1_;
    }
    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, net.minecraft.util.NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) getSubItems(this, tab, items);
    }
    @Override
    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, net.minecraft.util.EnumHand hand) {
        return new net.minecraft.util.ActionResult<>(net.minecraft.util.EnumActionResult.SUCCESS,
                onItemRightClick(player.getHeldItem(hand),world,player));
    }

}
