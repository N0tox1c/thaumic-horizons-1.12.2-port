//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.translation.I18n;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDummy extends Item  implements com.kentington.thaumichorizons.common.lib.OriginalItemAppearance , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider {

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconCow;

    public TextureAtlasSprite iconPig;
    public TextureAtlasSprite iconSheep;
    public TextureAtlasSprite iconChicken;
    public TextureAtlasSprite iconCat;
    public TextureAtlasSprite iconDog;
    public TextureAtlasSprite iconHuman;
    public TextureAtlasSprite iconHorse;
    public TextureAtlasSprite iconSpider;

    public ItemDummy() {
        this.setCreativeTab(null);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.iconCow = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:cow", "items"));
        this.iconPig = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:pig", "items"));
        this.iconSheep = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:sheep", "items"));
        this.iconChicken = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:chicken", "items"));
        this.iconCat = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:cat", "items"));
        this.iconDog = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:wolf", "items"));
        this.iconHuman = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:human", "items"));
        this.iconHorse = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:horse", "items"));
        this.iconSpider = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:spider", "items"));
    }

    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.getTagCompound() != null) {
            return I18n.translateToLocal(stack.getTagCompound().getString("infName"));
        }
        final int md = stack.getItemDamage();
        switch (md) {
    case 0: {
                return I18n.translateToLocal("entity.Cow.name");
            }
    case 1: {
                return I18n.translateToLocal("entity.Pig.name");
            }
    case 2: {
                return I18n.translateToLocal("entity.Sheep.name");
            }
    case 3: {
                return I18n.translateToLocal("entity.Chicken.name");
            }
    case 4: {
                return I18n.translateToLocal("entity.Cat.name");
            }
    case 5: {
                return I18n.translateToLocal("entity.Wolf.name");
            }
    case 6: {
                return I18n.translateToLocal("entity.horse.name");
            }
    case 8: {
                return I18n.translateToLocal("entity.Spider.name");
            }
    case 15: {
                return I18n.translateToLocal("thaumichorizons.revived");
            }
    default: {
                return "Creature";
            }
}
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int md) {
        switch (md) {
    case 0: {
                return this.iconCow;
            }
    case 1: {
                return this.iconPig;
            }
    case 2: {
                return this.iconSheep;
            }
    case 3: {
                return this.iconChicken;
            }
    case 5: {
                return this.iconDog;
            }
    case 6: {
                return this.iconHorse;
            }
    case 8: {
                return this.iconSpider;
            }
    case 15: {
                return this.iconHuman;
            }
    default: {
                return this.iconCat;
            }
}
    }
}
