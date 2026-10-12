//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;

import java.util.List;
import java.util.Set;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.lib.CreatureInfusionRecipe;
import com.kentington.thaumichorizons.common.lib.EntityInfusionProperties;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class ItemInfusionCheat extends Item  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

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

    public ItemInfusionCheat() {
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon1 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:quicksilverlimbs", "items"));
        this.icon2 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:thaumclaws", "items"));
        this.icon3 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:awakenedblood", "items"));
        this.icon4 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:diamondskin", "items"));
        this.icon5 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:enderheart", "items"));
        this.icon6 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:shockskin", "items"));
        this.icon7 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:instilledloyalty", "items"));
        this.icon8 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:runichide", "items"));
        this.icon9 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:eldritchfangs", "items"));
        this.icon10 = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:portability", "items"));
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
                stringy += I18n.translateToLocal("critterInfusions.quicksilver");
            } break;
    case 2: {
                stringy += I18n.translateToLocal("critterInfusions.thaumClaws");
            } break;
    case 3: {
                stringy += I18n.translateToLocal("critterInfusions.awakeBlood");
            } break;
    case 4: {
                stringy += I18n.translateToLocal("critterInfusions.diamondSkin");
            } break;
    case 5: {
                stringy += I18n.translateToLocal("critterInfusions.enderHeart");
            } break;
    case 6: {
                stringy += I18n.translateToLocal("critterInfusions.shockSkin");
            } break;
    case 7: {
                stringy += I18n.translateToLocal("critterInfusions.instilledLoyalty");
            } break;
    case 8: {
                stringy += I18n.translateToLocal("critterInfusions.runicHide");
            } break;
    case 9: {
                stringy += I18n.translateToLocal("critterInfusions.eldritchFangs");
            } break;
    case 10: {
                stringy += I18n.translateToLocal("critterInfusions.portability");
            } break;
}
        return stringy;
    }

    public ItemStack onItemRightClick(final ItemStack p_77659_1_, final World world, final EntityPlayer p) {
        final Entity ent = ItemFocusContainment.getPointedEntity(world, p, 1.5);
        if (ent instanceof EntityLiving && this.isValidInfusionTarget((EntityLiving) ent)) {
            final EntityLiving critter = ((EntityLiving) (ent));

            for (final CreatureInfusionRecipe recipe : ThaumicHorizons.critterRecipes) {
                boolean blockLoyalty = false;
                if (recipe.getID(null) == p_77659_1_.getItemDamage()
                        && recipe.getRecipeOutput() instanceof NBTTagCompound
                        && !((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(critter))
                                .hasInfusion(recipe.getID(null))) {
                    if (world.isRemote) {
                        Thaumcraft.proxy.burst(world,critter.posX,critter.posY+critter.getEyeHeight(),critter.posZ,1.0F);
                        return p_77659_1_;
                    }
            final NBTTagCompound tagMods = ((NBTTagCompound) (recipe.getRecipeOutput()));

                    final Multimap map = HashMultimap.create();
                    if (tagMods.getDouble("generic.movementSpeed") > 0.0) {
                        map.put(
                                "generic.movementSpeed",
                                new AttributeModifier(
                                        "generic.movementSpeed",
                                        tagMods.getDouble("generic.movementSpeed") / 10.0,
                                        1));
                    }
                    if (tagMods.getDouble("generic.maxHealth") > 0.0) {
                        map.put(
                                "generic.maxHealth",
                                new AttributeModifier("generic.maxHealth", tagMods.getDouble("generic.maxHealth"), 1));
                    }
                    if (tagMods.getDouble("generic.attackDamage") > 0.0) {
                        map.put(
                                "generic.attackDamage",
                                new AttributeModifier(
                                        "generic.attackDamage",
                                        tagMods.getDouble("generic.attackDamage"),
                                        1));
                    }
                    if (map.size() > 0) {
                        critter.getAttributeMap().applyAttributeModifiers(map);
                    }
                    final Set<String> keys = (Set<String>) tagMods.getKeySet();
                    for (final String s : keys) {
                        if (!s.startsWith("generic.")) {
                            if (tagMods.getInteger(s) == 7) {
                                if (critter.tasks.taskEntries.size() == 0) {
                                    blockLoyalty = true;
                                } else {
                                    ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(critter))
                                            .setOwner(p.getName());
                                }
                            }
                            if (blockLoyalty) {
                                continue;
                            }
                            ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(critter))
                                    .addInfusion(tagMods.getInteger(s));
                        }
                    }
                    if (!blockLoyalty) {
                        ((EntityInfusionProperties) com.kentington.thaumichorizons.common.lib.InfusionCapability.get(critter))
                                .addCost(recipe.getAspects());
                        critter.enablePersistence();
                        ThaumicHorizons.instance.eventHandlerEntity.applyInfusions(critter);
                        Thaumcraft.proxy
                                .burst(world, critter.posX, critter.posY + critter.getEyeHeight(), critter.posZ, 1.0f);
                    }
                    return p_77659_1_;
                }
            }
        }
        return p_77659_1_;
    }

    public boolean isValidInfusionTarget(final EntityLiving ent) {
        if (ent != null && ent.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD
                && !(ent instanceof EntityGolem)
                && !(ent instanceof EntityGolemBase)
                && !(ent instanceof IMerchant)
                && !(ent instanceof INpc)) {
            for (final Class clazz : ThaumicHorizons.classBanList) {
                if (ent.getClass().isAssignableFrom(clazz)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    @Override
    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, net.minecraft.util.EnumHand hand) {
        return new net.minecraft.util.ActionResult<>(net.minecraft.util.EnumActionResult.SUCCESS,
                onItemRightClick(player.getHeldItem(hand),world,player));
    }
    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, net.minecraft.util.NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) getSubItems(this, tab, items);
    }

}
