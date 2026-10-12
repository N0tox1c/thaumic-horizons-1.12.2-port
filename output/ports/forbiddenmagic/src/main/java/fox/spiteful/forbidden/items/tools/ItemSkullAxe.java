package fox.spiteful.forbidden.items.tools;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import thaumcraft.api.IRepairable;

import fox.spiteful.forbidden.Config;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSkullAxe extends ItemSword implements IRepairable {
    @Override public float getDestroySpeed(ItemStack stack, net.minecraft.block.state.IBlockState state) { return getDigSpeed(stack,state.getBlock(),state.getBlock().getMetaFromState(state)); }

    public TextureAtlasSprite icon;

    public ItemSkullAxe(ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.setCreativeTab(Forbidden.tab);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        this.icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:skullaxe"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int par1) {
        return this.icon;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack stack2) {
        return stack2.isItemEqual(new ItemStack(Config.thaumcraftResource.getItem(), 1, 2)) || super.getIsRepairable(stack, stack2);
    }

    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return 1.0F;
    }
}
