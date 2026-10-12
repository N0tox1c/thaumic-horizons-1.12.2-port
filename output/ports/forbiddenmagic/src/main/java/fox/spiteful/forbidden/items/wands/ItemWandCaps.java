package fox.spiteful.forbidden.items.wands;

import java.util.List;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWandCaps extends Item {

    public final String[] types = {
            "alchemical",
            "vinteum",
            "terrasteel",
            "manasteel",
            "manasteel_inert",
            "elementium",
            "elementium_inert"
    };
    public TextureAtlasSprite[] icon;

    public ItemWandCaps() {
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(Forbidden.tab);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        icon = new TextureAtlasSprite[types.length];
        for(int x = 0; x < types.length; x++)
            this.icon[x] = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:wand_cap_" + types[x]));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int meta) {
        return this.icon[meta];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(CreativeTabs xCreativeTabs, net.minecraft.util.NonNullList<ItemStack> list) {
        for(int x = 0; x < types.length; x++){
            list.add(new ItemStack(this, 1, x));
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "." + types[stack.getItemDamage()];
    }
}
