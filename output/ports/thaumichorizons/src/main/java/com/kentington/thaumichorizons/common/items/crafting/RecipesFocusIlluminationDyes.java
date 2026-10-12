package com.kentington.thaumichorizons.common.items.crafting;

import com.kentington.thaumichorizons.common.items.ItemFocusIllumination;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

/** Original single-focus recolouring mechanic, adapted to 1.12 non-null stacks. */
public class RecipesFocusIlluminationDyes extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        boolean foundFocus = false;
        boolean foundDye = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof ItemFocusIllumination) {
                if (foundFocus) return false;
                foundFocus = true;
            } else if (stack.getItem() == Items.DYE) {
                foundDye = true;
            } else {
                return false;
            }
        }
        return foundFocus && foundDye;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        if (!matches(inv, null)) return ItemStack.EMPTY;
        ItemStack result = ItemStack.EMPTY;
        int color = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof ItemFocusIllumination) {
                result = stack.copy();
                result.setCount(1);
            } else if (stack.getItem() == Items.DYE) {
                color = stack.getMetadata();
            }
        }
        result.setItemDamage(color);
        return result;
    }

    @Override public boolean canFit(int width, int height) { return width * height >= 2; }
    @Override public ItemStack getRecipeOutput() { return ItemStack.EMPTY; }
    @Override public boolean isDynamic() { return true; }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < remaining.size(); i++) {
            remaining.set(i, ForgeHooks.getContainerItem(inv.getStackInSlot(i)));
        }
        return remaining;
    }
}
