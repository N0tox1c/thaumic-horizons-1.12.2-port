package com.kentington.thaumichorizons.common.lib;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

/** Adds stable profession names while retaining the original soul-jar integer key. */
public final class VillagerSoulData {
    private VillagerSoulData() {}

    public static void write(NBTTagCompound tag, VillagerRegistry.VillagerProfession profession) {
        tag.setInteger("villagerType", VillagerRegistry.getId(profession));
        tag.setString("villagerProfession", profession.getRegistryName().toString());
    }

    public static void restore(EntityVillager villager, NBTTagCompound tag) {
        VillagerRegistry.VillagerProfession profession;
        if (tag.hasKey("villagerProfession", 8)) {
            profession = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation(tag.getString("villagerProfession")));
        } else {
            // Existing jars retain their numeric path. Future saves acquire a stable name.
            profession = VillagerRegistry.getById(tag.getInteger("villagerType"));
        }
        if (profession == null) throw new IllegalStateException("Unknown soul-jar profession: " + tag);
        villager.setProfession(profession);
        write(tag, profession);
    }
}
