package com.kentington.thaumichorizons.client.renderer.entity;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityBlastPhial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;

/**
 * The original vial used an IIcon/Tessellator renderer from 1.7.10.
 * RenderSnowball is the 1.12 equivalent. The displayed custom syringe/phial
 * receives a defensive copy of the real projectile's NBT for colour tinting.
 */
public class BlastPhialRender extends RenderSnowball<EntityBlastPhial> {
    public BlastPhialRender(RenderManager manager) {
        super(manager, ThaumicHorizons.itemSyringeInjection,
                Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    public ItemStack getStackToRender(EntityBlastPhial entity) {
        ItemStack phial = new ItemStack(ThaumicHorizons.itemSyringeInjection, 1, 1);
        ItemStack projectilePotion = entity.getPotion();
        if (!projectilePotion.isEmpty() && projectilePotion.hasTagCompound()) {
            phial.setTagCompound(projectilePotion.getTagCompound().copy());
        }
        return phial;
    }
}
