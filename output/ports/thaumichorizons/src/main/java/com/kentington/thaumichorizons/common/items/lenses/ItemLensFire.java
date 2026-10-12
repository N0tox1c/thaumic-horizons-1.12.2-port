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

public class ItemLensFire extends Item implements ILens  , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider , com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    TextureAtlasSprite icon;

    public ItemLensFire() {
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public String lensName() {
        return "LensFire";
    }

    public void handleRender(final Minecraft mc, final float partialTicks) {
        final boolean inWater = mc.player.isInsideOfMaterial(Material.WATER);
        final PotionEffect effect = mc.player.getActivePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION);

        if (!inWater) {
            // Apply effect.
            if ((effect == null || (isEffectGrantedByLens(effect) && effect.getDuration() < 242))
                    && Minecraft.getSystemTime() > LensManager.nightVisionOffTime) {
                LensManager.nightVisionOffTime = Minecraft.getSystemTime();
                mc.player.addPotionEffect(new IlluminePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION, 255, -1, true));
            }
        } else {
            // Remove effect.
            if (isEffectGrantedByLens(effect)) {
                mc.player.removePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION);
            }
        }
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.LensFire";
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:lensfire", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public void handleRemoval(final EntityPlayer p) {
        if (isEffectGrantedByLens(p.getActivePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION))) {
            p.removePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION);
        }
        // The lens effect is created by the client renderer, so the server
        // cannot use its own potion list to decide whether to notify that client.
        if (p instanceof EntityPlayerMP) {
            PacketHandler.INSTANCE.sendTo(new PacketRemoveNightvision(), (EntityPlayerMP) p);
        }
    }

    public static boolean isEffectGrantedByLens(PotionEffect effect) {
        return effect instanceof IlluminePotionEffect && ((IlluminePotionEffect) (effect)).isGrantedByLens;
    }

    private static class IlluminePotionEffect extends PotionEffect {

        public boolean isGrantedByLens;

        public IlluminePotionEffect(Potion potionID, int duration, int amplifier, boolean isAmbient) {
            super(potionID, duration, amplifier, isAmbient, false);
            isGrantedByLens = true;
        }

        public void combine(PotionEffect effect) {
            // If this is replaced by another night vision effect, for example from drinking a potion, it should not be
            // turned off by the lens.
            super.combine(effect);
            isGrantedByLens = effect instanceof IlluminePotionEffect;
        }
    }
}
