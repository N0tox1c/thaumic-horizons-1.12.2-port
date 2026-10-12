package fox.spiteful.forbidden.items.tools;

import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.items.ForbiddenItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

import fox.spiteful.forbidden.Config;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRidingCrop extends ItemSword {
    @Override public float getDestroySpeed(ItemStack stack, net.minecraft.block.state.IBlockState state) { return getDigSpeed(stack,state.getBlock(),state.getBlock().getMetaFromState(state)); }


    public TextureAtlasSprite icon;

    public ItemRidingCrop(ToolMaterial mat) {
        super(mat);
        this.setCreativeTab(Forbidden.tab);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        this.icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:crop"));
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase player) {
        stack.damageItem(1, player);
        if (victim instanceof EntityHorse || victim instanceof EntityPig)
            victim.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.SPEED, 200, 5));
        else if (victim instanceof EntityPlayer || victim instanceof EntityGolem) {
            victim.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.SPEED, 200, 1));
            victim.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.HASTE, 200, 1));
            victim.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.STRENGTH, 200, 1));
        }
        if (!player.world.isRemote && !Config.noLust && player.world.provider.getDimension() == -1 && player.world.rand.nextInt(15) == 1) {
            EntityItem ent = victim.entityDropItem(new ItemStack(ForbiddenItems.deadlyShards, 1, 4), 1.0F);
            ent.motionY += player.world.rand.nextFloat() * 0.05F;
            ent.motionX += (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.1F;
            ent.motionZ += (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.1F;
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int par1) {
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
        if (player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityLivingBase) {
            EntityLivingBase mount = (EntityLivingBase) player.getRidingEntity();
            stack.damageItem(1, player);
            player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
            // mount.attackEntityFrom(DamageSource.CAUSEPLAYERDAMAGE(player),
            // 4.0F);
            mount.attackEntityFrom(DamageSource.GENERIC, 1.0F);
            mount.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.SPEED, 200, 5));
        }
        return stack;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack stack2) {
        return stack2.getItem() == Items.LEATHER || super.getIsRepairable(stack, stack2);
    }

    @Override
    public boolean canHarvestBlock(net.minecraft.block.state.IBlockState state, ItemStack itemStack) {
        return false;
    }
}