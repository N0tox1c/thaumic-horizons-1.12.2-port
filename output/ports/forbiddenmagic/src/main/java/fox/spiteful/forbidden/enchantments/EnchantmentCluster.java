package fox.spiteful.forbidden.enchantments;

import fox.spiteful.forbidden.items.tools.ItemMorphPickaxe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.init.Enchantments;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

public class EnchantmentCluster extends Enchantment {
    public EnchantmentCluster() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        setRegistryName("forbiddenmagic", "cluster");
        setName("cluster");
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench) && ench != Enchantments.FORTUNE;
    }

    @Override
    public boolean canApply(ItemStack item) {
        if (item.getItem() instanceof ItemMorphPickaxe)
            return true;
        else
            return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }
}