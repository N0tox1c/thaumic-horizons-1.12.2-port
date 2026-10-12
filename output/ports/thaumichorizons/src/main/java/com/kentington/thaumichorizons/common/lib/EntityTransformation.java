package com.kentington.thaumichorizons.common.lib;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

/** Transfers common living state while retaining the new creature's identity and type. */
public final class EntityTransformation {
    private EntityTransformation() {}
    private static final String[] SHARED_KEYS = {
        "Health", "AbsorptionAmount", "HurtTime", "HurtByTimestamp", "DeathTime",
        "FallDistance", "Fire", "Air", "OnGround", "NoGravity", "Silent", "Invulnerable",
        "CustomName", "CustomNameVisible", "PersistenceRequired", "CanPickUpLoot",
        "HandItems", "ArmorItems", "HandDropChances", "ArmorDropChances", "ActiveEffects",
        "Attributes", "Age", "ForcedAge", "InLove",
        "OwnerUUID", "Tame", "Sitting"
    };

    public static void copyLivingState(EntityLivingBase source, EntityLivingBase target) {
        NBTTagCompound from = source.writeToNBT(new NBTTagCompound());
        NBTTagCompound merged = target.writeToNBT(new NBTTagCompound());
        for (String key : SHARED_KEYS) {
            if (from.hasKey(key)) merged.setTag(key, from.getTag(key).copy());
        }
        if (from.hasKey("ForgeData",10)) {
            NBTTagCompound data=merged.getCompoundTag("ForgeData");
            data.merge(from.getCompoundTag("ForgeData"));
            merged.setTag("ForgeData",data);
        }
        // Transfer only capabilities supported by the destination. Its unique
        // capabilities must retain their default data instead of disappearing.
        if (from.hasKey("ForgeCaps",10) && merged.hasKey("ForgeCaps",10)) {
            NBTTagCompound sourceCaps=from.getCompoundTag("ForgeCaps");
            NBTTagCompound targetCaps=merged.getCompoundTag("ForgeCaps");
            for (String key:targetCaps.getKeySet()) {
                if (sourceCaps.hasKey(key)) targetCaps.setTag(key,sourceCaps.getTag(key).copy());
            }
        }
        // UUID, dimension, portal state, passengers, type-specific NBT and AI remain target-owned.
        target.readFromNBT(merged);
        target.copyLocationAndAnglesFrom(source);
        target.motionX = source.motionX;
        target.motionY = source.motionY;
        target.motionZ = source.motionZ;
    }
}
