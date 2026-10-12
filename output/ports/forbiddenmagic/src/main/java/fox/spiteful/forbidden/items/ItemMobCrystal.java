package fox.spiteful.forbidden.items;

import java.util.List;
import fox.spiteful.forbidden.Config;
import fox.spiteful.forbidden.Forbidden;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;

/** Original named mob crystals, including old 1.7.10 mob NBT keys. */
public class ItemMobCrystal extends Item {
    public ItemMobCrystal() {
        setMaxStackSize(1);
        setMaxDamage(0);
        setCreativeTab(Forbidden.crysTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("mob")) return;
        Aspect aspect = Config.spawnerMobs.get(stack.getTagCompound().getString("mob"));
        if (aspect != null) tooltip.add(aspect.getName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        items.add(new ItemStack(this));
        for (String mob : Config.spawnerMobs.keySet()) {
            ItemStack crystal = new ItemStack(this);
            crystal.setTagCompound(new NBTTagCompound());
            crystal.getTagCompound().setString("mob", mob);
            items.add(crystal);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("mob")) {
            String mob = stack.getTagCompound().getString("mob");
            return I18n.translateToLocal("item.MobCrystal.name")
                    .replace("%s", I18n.translateToLocal("entity." + mob + ".name")).trim();
        }
        return I18n.translateToLocal("item.MobCrystalEmpty.name").trim();
    }
}
