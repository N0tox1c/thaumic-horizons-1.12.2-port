package com.kentington.thaumichorizons.common.lib;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/** Named material for new golem items, with the original block int-array as migration input. */
public final class GolemMaterial {
    private GolemMaterial() {}
    public static IBlockState read(NBTTagCompound tag) {
        if (tag == null) return Blocks.AIR.getDefaultState(); // Original voidling material.
        int[] legacy = tag.getIntArray("block");
        Block block;
        if (tag.hasKey("blockName", 8)) {
            ResourceLocation key = new ResourceLocation(tag.getString("blockName"));
            if (!Block.REGISTRY.containsKey(key)) throw new IllegalArgumentException("Unknown golem material: " + key);
            block = Block.REGISTRY.getObject(key);
        } else {
            block = legacy.length >= 2 ? Block.getBlockById(legacy[0]) : Blocks.AIR;
        }
        return block.getStateFromMeta(legacy.length >= 2 ? legacy[1] : 0);
    }
}
