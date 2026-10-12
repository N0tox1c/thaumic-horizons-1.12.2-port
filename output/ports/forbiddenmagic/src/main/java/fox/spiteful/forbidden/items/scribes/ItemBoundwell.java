package fox.spiteful.forbidden.items.scribes;

import WayofTime.bloodmagic.iface.IBindable;
import fox.spiteful.forbidden.compat.BloodNetwork;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fox.spiteful.forbidden.Forbidden;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thaumcraft.api.IScribeTools;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.Iterator;
import java.util.List;

public class ItemBoundwell extends Item implements IScribeTools, IBindable{
    @Override public boolean hasEffect(ItemStack stack) { return hasEffect(stack,0); }

    @Override
    public WayofTime.bloodmagic.core.data.Binding getBinding(ItemStack stack) {
        return WayofTime.bloodmagic.core.data.Binding.fromStack(stack);
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon;

    public ItemBoundwell() {
        maxStackSize = 1;
        canRepair = false;
        setMaxDamage(100);
        setCreativeTab(Forbidden.tab);
        setHasSubtypes(false);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        this.icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:boundwell"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int par1) {
        return this.icon;
    }

    @Override
    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, net.minecraft.util.EnumHand hand) {
        return new net.minecraft.util.ActionResult<>(net.minecraft.util.EnumActionResult.SUCCESS, legacyRightClick(player.getHeldItem(hand), world, player));
    }
    public ItemStack legacyRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        BloodNetwork.checkAndSetItemOwner(itemstack, player);
        if (itemstack.getItemDamage() >= 100) {
            player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
            if (!world.isRemote) {
                if (player.capabilities.isCreativeMode)
                    itemstack.setItemDamage(0);
                else {
                    BloodNetwork.syphonAndDamageFromNetwork(itemstack, player, 10000);
                    if(player.getHealth() > 0){
                        itemstack.setItemDamage(0);
                    }
                    else
                        return itemstack;
                }
                Aspect aspect;
                short amount;
                for(Iterator count = Aspect.getPrimalAspects().iterator(); count.hasNext(); PacketHandler.INSTANCE.sendTo(new PacketAspectPool(aspect.getTag(), Short.valueOf(amount), (short)(Thaumcraft.proxy.getPlayerKnowledge(player).getAspectPoolFor( aspect))), (EntityPlayerMP)player))
                {
                    aspect = (Aspect)count.next();
                    amount = (short)(world.rand.nextInt(4) + 4);
                    Thaumcraft.proxy.getPlayerKnowledge(player).addAspectPool( aspect, amount);
                    ResearchManager.updateCache(player);
                }
            }
        }
        return itemstack;
    }

    @Override
    public void addInformation(ItemStack stack, @javax.annotation.Nullable World world, List<String> list, net.minecraft.client.util.ITooltipFlag flags) {
        if (stack.hasTagCompound()) {
            list.add("");
            list.add(I18n.translateToLocal("tooltip.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
        }
    }

    /**
     * Render Pass sensitive version of hasEffect()
     */
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {
        return stack.getItemDamage() >= 100;
    }

}
