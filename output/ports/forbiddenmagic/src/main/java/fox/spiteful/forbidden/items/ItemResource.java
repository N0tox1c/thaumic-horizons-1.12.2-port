package fox.spiteful.forbidden.items;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;

/** Original 5-resource metadata and black powder sheep dye behavior. */
public class ItemResource extends Item {
    public ItemResource() {
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(Forbidden.tab);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "." + stack.getMetadata();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int meta = 0; meta < 5; meta++) items.add(new ItemStack(this, 1, meta));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase living, EnumHand hand) {
        if (stack.getMetadata() != 1 || !(living instanceof EntitySheep)) return false;
        EntitySheep sheep = (EntitySheep) living;
        if (!sheep.getSheared() && sheep.getFleeceColor() != EnumDyeColor.WHITE) {
            if (!sheep.world.isRemote) {
                sheep.setFleeceColor(EnumDyeColor.WHITE);
                if (!player.capabilities.isCreativeMode) stack.shrink(1);
            }
            return true;
        }
        return false;
    }
}
