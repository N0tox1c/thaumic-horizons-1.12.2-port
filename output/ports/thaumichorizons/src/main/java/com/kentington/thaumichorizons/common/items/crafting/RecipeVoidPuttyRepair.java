package com.kentington.thaumichorizons.common.items.crafting;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

/** Original void-putty repair result, with 1.12 inventory and safe invalid-input handling. */
public class RecipeVoidPuttyRepair extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        boolean foundTool = false;
        boolean foundPutty = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() == ThaumicHorizons.itemVoidPutty) {
                foundPutty = true;
            } else if (stack.getItem().isDamageable() && stack.getItem().isRepairable()
                    && stack.isItemDamaged()) {
                if (foundTool) return false;
                foundTool = true;
            } else {
                return false;
            }
        }
        return foundTool && foundPutty;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        if (!matches(inv, null)) return ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() != ThaumicHorizons.itemVoidPutty) {
                ItemStack result = stack.copy();
                result.setCount(1);
                result.setItemDamage(0);
                return result;
            }
        }
        return ItemStack.EMPTY;
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
