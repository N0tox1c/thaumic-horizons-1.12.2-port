package fox.spiteful.forbidden.items.wands;

import java.util.List;

import fox.spiteful.forbidden.Config;
import fox.spiteful.forbidden.Forbidden;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import thaumcraft.api.ItemApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

import fox.spiteful.forbidden.compat.Compat;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWandCores extends Item {

    public final String[] types = {
            "tainted",
            "infernal",
            "soul",
            "blood",
            "witchwood",
            "profane",
            "blood_inert",
            "livingwood",
            "livingwood_inert",
            "blood_staff",
            "witchwood_staff",
            "dreamwood",
            "dreamwood_inert",
            "dreamwood_staff" };
    public TextureAtlasSprite[] icon;

    public ItemWandCores() {
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(Forbidden.tab);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        icon = new TextureAtlasSprite[types.length];
        for (int x = 0; x < types.length; x++)
            this.icon[x] = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:wand_rod_" + types[x]));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int meta) {
        return this.icon[meta];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(CreativeTabs xCreativeTabs, net.minecraft.util.NonNullList<ItemStack> list) {
        for (int x = 0; x < types.length; x++) {
            list.add(new ItemStack(this, 1, x));
        }

        ItemStack wand = new ItemStack(ConfigItems.itemWandCasting, 1, 72);
        ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("thaumium"));
        ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("tainted"));
        list.add(wand);
        wand = new ItemStack(ConfigItems.itemWandCasting, 1, 72);
        ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("thaumium"));
        ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("infernal"));
        list.add(wand);
        wand = new ItemStack(ConfigItems.itemWandCasting, 1, 2000);
        ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("orichalcum"));
        ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("neutronium"));
        list.add(wand);
        wand = new ItemStack(ConfigItems.itemWandCasting, 1, 2000);
        ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("orichalcum"));
        ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("neutronium_staff"));
        list.add(wand);
        wand = new ItemStack(ConfigItems.itemWandCasting, 1, 36);
        ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("iron"));
        ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("profane"));
        ((ItemWandCasting) wand.getItem()).storeAllVis(wand, new AspectList().add(Aspect.FIRE, 5000).add(Aspect.WATER, 5000).add(Aspect.EARTH, 5000).add(Aspect.AIR, 5000).add(Aspect.ORDER, 5000).add(Aspect.ENTROPY, 5000));
        list.add(wand);
        if (Compat.bm && Config.crossWand) {
            wand = new ItemStack(ConfigItems.itemWandCasting, 1, 84);
            ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("alchemical"));
            ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("blood"));
            list.add(wand);
            wand = new ItemStack(ConfigItems.itemWandCasting, 1, 168);
            ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("alchemical"));
            ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("blood_staff"));
            list.add(wand);
        }
        if (Compat.am2 && Config.crossWand) {
            wand = new ItemStack(ConfigItems.itemWandCasting, 1, 72);
            ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("vinteum"));
            ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("witchwood"));
            list.add(wand);
            wand = new ItemStack(ConfigItems.itemWandCasting, 1, 144);
            ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("vinteum"));
            ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("witchwood_staff"));
            list.add(wand);
        }
        if (Compat.botan && Config.crossWand) {
            wand = new ItemStack(ConfigItems.itemWandCasting, 1, 84);
            ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("manasteel"));
            ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("livingwood"));
            list.add(wand);
            wand = new ItemStack(ConfigItems.itemWandCasting, 1, 84);
            ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("elementium"));
            ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("dreamwood"));
            list.add(wand);
            wand = new ItemStack(ConfigItems.itemWandCasting, 1, 144);
            ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("elementium"));
            ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("dreamwood_staff"));
            list.add(wand);
            wand = new ItemStack(ConfigItems.itemWandCasting, 1, 1);
            ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("terrasteel"));
            ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("wood"));
            list.add(wand);
        }
        wand = new ItemStack(ConfigItems.itemWandCasting, 1, 84);
        ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("void"));
        ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("equivalent"));
        list.add(wand);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "." + types[stack.getItemDamage()];
    }
}
