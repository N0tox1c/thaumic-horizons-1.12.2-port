package fox.spiteful.forbidden.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import fox.spiteful.forbidden.Forbidden;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.IRunicArmor;

/**
 * Adaptation of Forbidden Magic 1.7.10 ItemRingNutrition.
 * RING slot and historical cosmeticItem NBT remain compatible.
 * Food bonus belongs to the original FMEventHandler.onEat, NOT this class.
 * Botania's optional ICosmeticAttachable integration needs an actual Botania API.
 */
public class ItemRingNutrition extends Item implements IRunicArmor, IBauble {
    public ItemRingNutrition() {
        setMaxStackSize(1);
        setCreativeTab(Forbidden.tab);
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return BaubleType.RING;
    }

    @Override
    public int getRunicCharge(ItemStack stack) {
        return 0;
    }

    public ItemStack getCosmeticItem(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTagCompound()
                || !stack.getTagCompound().hasKey("cosmeticItem", 10)) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(stack.getTagCompound().getCompoundTag("cosmeticItem"));
    }

    public void setCosmeticItem(ItemStack stack, ItemStack cosmetic) {
        if (stack.isEmpty()) return;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();
        if (cosmetic == null || cosmetic.isEmpty()) {
            tag.removeTag("cosmeticItem");
        } else {
            tag.setTag("cosmeticItem", cosmetic.writeToNBT(new NBTTagCompound()));
        }
        stack.setTagCompound(tag);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        ItemStack cosmetic = getCosmeticItem(stack);
        if (!cosmetic.isEmpty() && Loader.isModLoaded("botania") && GuiScreen.isShiftKeyDown()) {
            tooltip.add(I18n.translateToLocalFormatted("botaniamisc.hasCosmetic", cosmetic.getDisplayName()));
        }
    }
}
