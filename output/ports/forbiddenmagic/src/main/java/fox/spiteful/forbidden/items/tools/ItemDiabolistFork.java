package fox.spiteful.forbidden.items.tools;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import thaumcraft.api.IRepairable;

import fox.spiteful.forbidden.Config;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDiabolistFork extends ItemSword implements IRepairable {
    @Override public float getDestroySpeed(ItemStack stack, net.minecraft.block.state.IBlockState state) { return getDigSpeed(stack,state.getBlock(),state.getBlock().getMetaFromState(state)); }


    public TextureAtlasSprite icon;

    public ItemDiabolistFork(ToolMaterial mat) {
        super(mat);
        this.setCreativeTab(Forbidden.tab);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        if (Config.spork)
            this.icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:spork"));
        else
            this.icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:fork"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int meta) {
        return this.icon;
    }

    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return 1.0F;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 0;
    }

    @Override
    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, net.minecraft.util.EnumHand hand) {
        return new net.minecraft.util.ActionResult<>(net.minecraft.util.EnumActionResult.SUCCESS, legacyRightClick(player.getHeldItem(hand), world, player));
    }
    public ItemStack legacyRightClick(ItemStack stack, World world, EntityPlayer player) {
        return stack;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack stack2) {
        return stack2.getItem() == Items.QUARTZ ? true : super.getIsRepairable(stack, stack2);
    }

    @Override
    public boolean canHarvestBlock(net.minecraft.block.state.IBlockState state, ItemStack itemStack) {
        return false;
    }
}