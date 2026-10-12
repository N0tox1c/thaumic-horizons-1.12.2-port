//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items.lenses;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.lib.networking.PacketHandler;
import com.kentington.thaumichorizons.common.lib.networking.PacketRemoveNightvision;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLensWater extends Item implements ILens  , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    TextureAtlasSprite icon;

    public ItemLensWater() {
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public String lensName() {
        return "LensWater";
    }

    public void handleRender(final Minecraft mc, final float partialTicks) {
        final boolean inWater = mc.player.isInsideOfMaterial(Material.WATER);
        if (inWater
                && (mc.player.getActivePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION) == null
                        || mc.player.getActivePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION).getDuration() < 242)
                && Minecraft.getSystemTime() > LensManager.nightVisionOffTime) {
            LensManager.nightVisionOffTime = Minecraft.getSystemTime();
            mc.player.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION, 255, 0, true, true));
        } else if (!inWater) {
            mc.player.removePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION);
        }
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.LensWater";
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:lenswater", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public void handleRemoval(final EntityPlayer p) {
        p.removePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION);
        PacketHandler.INSTANCE.sendTo(new PacketRemoveNightvision(), (EntityPlayerMP) p);
    }
}
