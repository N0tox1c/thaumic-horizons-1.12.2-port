package com.kentington.thaumichorizons.common.lib;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/** Uses GregTech's material ore names without linking a removed 1.7 GregTech API. */
public final class OreIngredients {
    private OreIngredients() {}
    public static boolean available(String... names) {
        for(String name:names) if(OreDictionary.getOres(name).isEmpty()) return false;
        return true;
    }
    public static ItemStack get(String name) {
        if(!available(name)) throw new IllegalArgumentException("Missing ore ingredient: "+name);
        ItemStack stack=OreDictionary.getOres(name).get(0).copy(); stack.setCount(1);
        return stack;
    }
}
