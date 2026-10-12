package fox.spiteful.forbidden.items;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;

/**
 * Port of the original Forbidden Magic tainted fruit behaviour.
 * Original balance: 4 hunger, 0.8 saturation, +1 sticky warp,
 * 30s flux taint / hunger; 40% chance of thaumarrhia for 30s.
 * Item models/textures are registered separately in Forge 1.12.2.
 */
public class ItemFruitTainted extends ItemFood {
    public ItemFruitTainted() {
        super(4, 0.8F, false);
        setCreativeTab(Forbidden.tab);
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        super.onFoodEaten(stack, world, player);
        if (world.isRemote) return;

        Thaumcraft.addStickyWarpToPlayer(player, 1);
        applyUncurable(player, Config.potionFluxTaint, 600, false);
        applyUncurable(player, MobEffects.HUNGER, 600, false);

        if (world.rand.nextFloat() < 0.4F) {
            player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE
                    + I18n.translateToLocal("warp.text.15")));
            applyUncurable(player, Config.potionThaumarhia, 600, true);
        }
    }

    private static void applyUncurable(EntityPlayer player, Potion potion, int duration, boolean ambient) {
        if (potion == null) return;
        PotionEffect effect = new PotionEffect(potion, duration, 0, ambient, true);
        effect.getCurativeItems().clear();
        player.addPotionEffect(effect);
    }
}
