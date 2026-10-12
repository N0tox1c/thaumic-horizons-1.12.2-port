package com.kentington.thaumichorizons.common.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Original EntityBlastPhial uses the vanilla splash-potion collision logic.
 * The 1.12 port retains its launch offset and scaled velocity, while potion
 * effects come from CustomPotionEffects on a VANILLA SPLASH_POTION stack.
 * Vanilla EntityPotion rejects non-splash items in getPotion(), so directly
 * passing ItemSyringeInjection would silently discard its potion effects.
 * Entity registration/rendering must be ported separately.
 */
public class EntityBlastPhial extends EntityPotion {
    public EntityBlastPhial(World world) {
        super(world);
    }

    public EntityBlastPhial(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, asVanillaSplash(stack));
    }

    private static ItemStack asVanillaSplash(ItemStack injection) {
        ItemStack potion = new ItemStack(Items.SPLASH_POTION);
        if (injection != null && !injection.isEmpty() && injection.hasTagCompound()) {
            // Preserve CustomPotionEffects and custom color NBT from Blood Infuser.
            // Never mutate the source held item or its saved NBT.
            NBTTagCompound tag = injection.getTagCompound().copy();
            potion.setTagCompound(tag);
        }
        return potion;
    }

    public EntityBlastPhial(World world, EntityLivingBase thrower, float power, ItemStack stack) {
        super(world, thrower, asVanillaSplash(stack));
        setSize(0.5F, 0.5F);
        setLocationAndAngles(thrower.posX, thrower.posY + thrower.getEyeHeight(), thrower.posZ,
                thrower.rotationYaw, thrower.rotationPitch);
        float yaw = thrower.rotationYaw * 0.017453292F;
        float pitch = thrower.rotationPitch * 0.017453292F;
        setPosition(posX - MathHelper.cos(yaw) * 0.16F,
                posY - 0.1D, posZ - MathHelper.sin(yaw) * 0.16F);
        // Original: setThrowableHeading(direction, power * 1.5f, 1.0f)
        shoot(-MathHelper.sin(yaw) * MathHelper.cos(pitch),
                -MathHelper.sin(pitch),
                MathHelper.cos(yaw) * MathHelper.cos(pitch), power * 1.5F, 1.0F);
    }
}
