package fox.spiteful.forbidden.compat;

import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/** Called only when ProjectE is loaded, during Forbidden Magic post-initialization. */
public final class ProjectECompat {
    private ProjectECompat() {}

    public static void registerEMC(ItemStack stack, long value) {
        if (stack == null || stack.isEmpty()) {
            throw new IllegalArgumentException("Cannot assign EMC to an empty item stack");
        }
        // Use the item-stack overload: ProjectE treats arbitrary Objects as synthetic keys.
        ProjectEAPI.getEMCProxy().registerCustomEMC(stack, value);
    }

    public static void registerEMC(Item item, long value) {
        registerEMC(new ItemStack(item), value);
    }

    public static void registerEMC(Block block, long value) {
        registerEMC(new ItemStack(block), value);
    }
}
