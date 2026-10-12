package fox.spiteful.forbidden.items.tools;
import net.minecraft.util.math.BlockPos;


import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.blocks.ForbiddenBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import thaumcraft.api.IRepairable;

import fox.spiteful.forbidden.Config;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

public class ItemTaintShovel extends ItemSpade implements IRepairable
{
    @Override public float getDestroySpeed(ItemStack stack, net.minecraft.block.state.IBlockState state) { return getDigSpeed(stack,state.getBlock(),state.getBlock().getMetaFromState(state)); }

    public TextureAtlasSprite icon;

    public ItemTaintShovel(ToolMaterial enumtoolmaterial)
    {
        super(enumtoolmaterial);
        this.setCreativeTab(Forbidden.tab);
        this.setHarvestLevel("shovel", 3);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir)
    {
        this.icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:taintshovel"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int par1)
    {
        return this.icon;
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.RARE;
    }

    public boolean getIsRepairable(ItemStack stack, ItemStack stack2)
    {
        return stack2.isItemEqual(new ItemStack(Config.thaumcraftResource.getItem(), 1, 2)) ? true : super.getIsRepairable(stack, stack2);
    }

    public float getDigSpeed(ItemStack stack, Block block, int meta)
    {
        if (block.isToolEffective("shovel", block.getStateFromMeta(meta)) ||
                (block.getDefaultState().getMaterial() == Config.taintMaterial && block != ForbiddenBlocks.taintLog && block != ForbiddenBlocks.taintPlanks
                && block != ForbiddenBlocks.taintStone))
        {
            return efficiency;
        }

        return super.getDestroySpeed(stack, block.getStateFromMeta(meta));
    }
    
    @Override
    public net.minecraft.util.EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing facing, float par8, float par9, float par10) {
        ItemStack itemstack=player.getHeldItem(hand); int x=pos.getX(), y=pos.getY(), z=pos.getZ();
        int purified = 0;
        for(int ex = x - 5;ex < x + 6;ex++){
            for(int wy = y - 4;wy < y + 5;wy++){
                for(int zee = z - 5;zee < z + 6;zee++){
                    Block target = world.getBlockState(new BlockPos(ex, wy, zee)).getBlock();
                    if(target != null && (target == ConfigBlocks.blockFluxGoo || target == ConfigBlocks.blockFluxGas)){
                        purified++;
                        world.setBlockToAir(new BlockPos(ex, wy, zee));
                        float d1 = ((float) ex + world.rand.nextFloat());
                        float d2 = ((float) wy + world.rand.nextFloat());
                        float d0 = ((float) zee + world.rand.nextFloat());
                        Thaumcraft.proxy.nodeBolt(world, (float)player.posX, (float)player.posY, (float)player.posZ, d1, d2, d0);
                    }
                }
            }
        }
        if(purified > 0){
            itemstack.damageItem(Math.min(purified, 15), player);
            player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
            fox.spiteful.forbidden.compat.NativeCalls.playSound(world, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "thaumcraft:wandfail", 0.2F, 0.2F + world.rand.nextFloat() * 0.2F);
            return net.minecraft.util.EnumActionResult.SUCCESS;
        }
        else
            return super.onItemUse(player,world,pos,hand,facing,par8,par9,par10);
    }
}
