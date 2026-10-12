package fox.spiteful.forbidden.items.tools;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import thaumcraft.api.IRepairable;

import fox.spiteful.forbidden.Config;
import fox.spiteful.forbidden.Forbidden;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTaintPickaxe extends ItemPickaxe implements IRepairable {
    public TextureAtlasSprite icon;

    public ItemTaintPickaxe(ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.setCreativeTab(Forbidden.tab);
        this.setHarvestLevel("pickaxe", 5);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        this.icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:taintpickaxe"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int par1) {
        return this.icon;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.RARE;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack stack2) {
        return stack2.isItemEqual(new ItemStack(Config.thaumcraftResource.getItem(), 1, 2)) ? true : super.getIsRepairable(stack, stack2);
    }
}
