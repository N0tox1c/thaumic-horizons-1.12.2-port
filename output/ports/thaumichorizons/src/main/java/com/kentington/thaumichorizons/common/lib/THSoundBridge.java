package com.kentington.thaumichorizons.common.lib;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/** Translation layer for legacy 1.7.10 sound strings; does not register dummy sounds. */
public final class THSoundBridge {
    private static final Set<String> WARNED = Collections.synchronizedSet(new HashSet<String>());
    private THSoundBridge() { }

    public static SoundEvent lookup(String old) {
        if (old == null || old.isEmpty()) return null;
        // Mojang moved these two legacy sound names to registry names in 1.12.2.
        if ("game.neutral.swim".equals(old)) return SoundEvents.ENTITY_PLAYER_SWIM;
        if ("random.fizz".equals(old)) return SoundEvents.BLOCK_FIRE_EXTINGUISH;
        if ("random.explode".equals(old)) return SoundEvents.ENTITY_GENERIC_EXPLODE;
        if ("random.glass".equals(old)) return SoundEvents.BLOCK_GLASS_BREAK;
        if ("random.burp".equals(old)) return SoundEvents.ENTITY_PLAYER_BURP;
        if ("random.bow".equals(old)) return SoundEvents.ENTITY_ARROW_SHOOT;
        if ("random.orb".equals(old)) return SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
        if ("game.tnt.primed".equals(old)) return SoundEvents.ENTITY_TNT_PRIMED;
        if ("mob.endermen.portal".equals(old)) return SoundEvents.ENTITY_ENDERMEN_TELEPORT;
        if ("mob.chicken.plop".equals(old)) return SoundEvents.ENTITY_CHICKEN_EGG;
        if ("mob.horse.land".equals(old)) return SoundEvents.ENTITY_HORSE_LAND;
        if ("mob.attack".equals(old)) return SoundEvents.ENTITY_PLAYER_ATTACK_STRONG;
        final String key = old.indexOf(':') >= 0 ? old : "minecraft:" + old;
        SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation(key));
        if (event == null && WARNED.add(key)) {
            System.err.println("[ThaumicHorizons] Unregistered legacy sound: " + key);
        }
        return event;
    }

    public static void playAtEntity(World world, net.minecraft.entity.Entity entity,
            String name, float volume, float pitch) {
        SoundEvent event = lookup(name);
        if (event == null) throw new IllegalArgumentException("Unregistered sound: " + name);
        world.playSound(null, entity.posX, entity.posY, entity.posZ,
                event, entity.getSoundCategory(), volume, pitch);
    }

    /** 1.7.10 com.kentington.thaumichorizons.common.lib.THSoundBridge.play(world, x, y, z, name, volume, pitch). */
    public static void play(World world, double x, double y, double z, String name, float volume, float pitch) {
        if (world == null) return;
        SoundEvent event = lookup(name);
        if (event != null) world.playSound(null, x, y, z, event, SoundCategory.BLOCKS, volume, pitch);
    }

    /** 1.7.10 playSound(x,y,z,name,volume,pitch,distanceDelay). */
    public static void playLegacy(World world, double x, double y, double z, String name,
                                  float volume, float pitch, boolean distanceDelay) {
        if (world == null) return;
        SoundEvent event = lookup(name);
        if (event == null) return;
        if (world.isRemote) {
            world.playSound(x, y, z, event, SoundCategory.BLOCKS, volume, pitch, distanceDelay);
        } else {
            world.playSound(null, x, y, z, event, SoundCategory.BLOCKS, volume, pitch);
        }
    }
}
