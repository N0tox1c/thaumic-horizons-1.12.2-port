package fox.spiteful.forbidden.items.tools;

import fox.spiteful.forbidden.compat.BloodNetwork;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fox.spiteful.forbidden.compat.Compat;
import fox.spiteful.forbidden.potions.DarkPotions;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.config.ConfigItems;

public class ItemBloodRapier extends ItemSword implements IRepairable, IWarpingGear{

    static final ToolMaterial bloodsucker = EnumHelper.addToolMaterial("BLOODSUCKER", 3, 700, 10F, -3F, 18);
    private TextureAtlasSprite icon;

    public ItemBloodRapier(){
        super(bloodsucker);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase player) {
        stack.damageItem(1, player);
        victim.motionY *= 0.8;
        if (victim.hurtResistantTime > 18)
            victim.hurtResistantTime -= 5;
        if (Compat.bm & victim instanceof EntityPlayer) {
            String target = ((EntityPlayer) victim).getName();
            int lp = BloodNetwork.getCurrentEssence(target);
            int damage = Math.max(4000, lp / 4);
            if (lp >= damage)
                lp -= damage;
            else
                lp = 0;
            BloodNetwork.setCurrentEssence(target, lp);
            victim.addPotionEffect(new PotionEffect(DarkPotions.bloodSeal, 1200));
        }
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int wat, boolean fuckObfuscation) {
        super.onUpdate(stack, world, entity, wat, fuckObfuscation);
        if(stack.isItemDamaged() && entity != null && entity.ticksExisted % 20 == 0 && entity instanceof EntityLivingBase) {
            stack.damageItem(-1, (EntityLivingBase)entity);
        }
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int par1) {
        return this.icon;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        this.icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:blood_rapier"));
    }

    @Override
    public int getWarp(ItemStack itemstack, EntityPlayer player) {
        return 2;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return par2ItemStack.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 15))?true:super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.RARE;
    }
}
