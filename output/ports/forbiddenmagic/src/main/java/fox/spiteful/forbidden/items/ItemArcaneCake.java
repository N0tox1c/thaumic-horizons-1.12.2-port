package fox.spiteful.forbidden.items;

import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.blocks.ForbiddenBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Uses the original cake block and a hand-held placing item. */
public class ItemArcaneCake extends Item {
    public ItemArcaneCake() { setCreativeTab(Forbidden.tab); }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty()) return EnumActionResult.FAIL;
        IBlockState hitState = world.getBlockState(pos);
        Block hit = hitState.getBlock();
        // Original 1.7.10 behavior: replace one-layer snow / plants in place.
        if (hit != Blocks.SNOW_LAYER || hitState.getValue(net.minecraft.block.BlockSnow.LAYERS) > 1) {
            if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
                pos = pos.offset(facing);
            }
        }
        if (!player.canPlayerEdit(pos, facing, stack)) return EnumActionResult.FAIL;
        Block cake = ForbiddenBlocks.arcaneCake;
        if (!world.mayPlace(cake, pos, false, facing, null)) return EnumActionResult.FAIL;
        if (!world.isRemote) {
            IBlockState placed = cake.getDefaultState();
            if (!world.setBlockState(pos, placed, 11)) return EnumActionResult.FAIL;
            cake.onBlockPlacedBy(world, pos, placed, player, stack);
            world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!player.capabilities.isCreativeMode) stack.shrink(1);
        }
        return EnumActionResult.SUCCESS;
    }
}
