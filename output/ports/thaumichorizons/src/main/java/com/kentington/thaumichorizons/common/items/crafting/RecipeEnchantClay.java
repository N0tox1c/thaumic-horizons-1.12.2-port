//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/** Unregistered placeholder in the original sources; enchanted clay recipes are registered by ThaumicHorizons. */
public class RecipeEnchantClay extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override public boolean canFit(int width, int height) { return width * height >= getRecipeSize(); }

    public boolean matches(final InventoryCrafting p_77569_1_, final World p_77569_2_) {
        return false;
    }

    public ItemStack getCraftingResult(final InventoryCrafting p_77572_1_) {
        return ItemStack.EMPTY;
    }

    public int getRecipeSize() {
        return 0;
    }

    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}
