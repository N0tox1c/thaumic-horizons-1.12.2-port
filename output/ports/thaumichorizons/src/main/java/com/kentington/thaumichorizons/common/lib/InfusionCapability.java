package com.kentington.thaumichorizons.common.lib;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Persistent creature and player infusions, backed by the native Forge capability system. */
public final class InfusionCapability {
    @CapabilityInject(EntityInfusionProperties.class)
    public static Capability<EntityInfusionProperties> CAPABILITY;
    private static final ResourceLocation KEY=new ResourceLocation("thaumichorizons", "creature_infusion");

    public static void register() {
        CapabilityManager.INSTANCE.register(EntityInfusionProperties.class,
            new Capability.IStorage<EntityInfusionProperties>() {
                public NBTBase writeNBT(Capability<EntityInfusionProperties> cap, EntityInfusionProperties value, EnumFacing side) {
                    return value.serializeNBT();
                }
                public void readNBT(Capability<EntityInfusionProperties> cap, EntityInfusionProperties value, EnumFacing side, NBTBase tag) {
                    if(!(tag instanceof NBTTagCompound)) throw new IllegalArgumentException("Invalid infusion NBT");
                    value.deserializeNBT((NBTTagCompound)tag);
                }
            }, EntityInfusionProperties::new);
    }

    public static EntityInfusionProperties get(Entity entity) {
        return entity==null?null:entity.getCapability(CAPABILITY,null);
    }

    @SubscribeEvent
    public void attach(AttachCapabilitiesEvent<Entity> event) {
        Entity entity=event.getObject();
        if(!(entity instanceof EntityLivingBase)) return;
        EntityInfusionProperties properties=new EntityInfusionProperties();
        properties.init(entity,entity.world);
        event.addCapability(KEY,new ICapabilitySerializable<NBTTagCompound>() {
            public boolean hasCapability(Capability<?> cap, EnumFacing side) { return cap==CAPABILITY; }
            public <T> T getCapability(Capability<T> cap, EnumFacing side) { return cap==CAPABILITY?CAPABILITY.cast(properties):null; }
            public NBTTagCompound serializeNBT() { return properties.serializeNBT(); }
            public void deserializeNBT(NBTTagCompound tag) { properties.deserializeNBT(tag); }
        });
    }

    @SubscribeEvent
    public void clonePlayer(PlayerEvent.Clone event) {
        EntityInfusionProperties previous=get(event.getOriginal()), next=get(event.getEntityPlayer());
        if(previous!=null&&next!=null) next.deserializeNBT(previous.serializeNBT());
    }
}
