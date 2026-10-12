package fox.spiteful.forbidden.items;

import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.Config;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

/** Original seven metadata values, including Config.noLust -> despair. */
public class ItemDeadlyShard extends Item {
    public static final String[] VICES = {"wrath", "envy", "taint", "pride", "lust", "sloth", "greed"};
    public ItemDeadlyShard() {
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(Forbidden.tab);
    }

    public static String vice(int metadata) {
        int slot = MathHelper.clamp(metadata, 0, VICES.length - 1);
        return (slot == 4 && Config.noLust) ? "despair" : VICES[slot];
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "." + vice(stack.getMetadata());
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int meta = 0; meta < VICES.length; meta++) items.add(new ItemStack(this, 1, meta));
    }
}
