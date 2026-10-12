package fox.spiteful.forbidden.items.baubles;

import net.minecraft.world.World;
import baubles.api.BaublesApi;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.compat.Compat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.translation.I18n;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import vazkii.botania.api.item.ICosmeticAttachable;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

@Optional.Interface(iface = "vazkii.botania.api.item.ICosmeticAttachable", modid = "Botania")
public class ItemSubCollar extends ItemAmuletVis implements ICosmeticAttachable {

    public TextureAtlasSprite icon;
    DecimalFormat myFormatter = new DecimalFormat("#######.##");

    public ItemSubCollar(){
        super();
        maxStackSize = 1;
        setCreativeTab(Forbidden.tab);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "item." + "SubCollar";
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:collar"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int par1) {
        return icon;
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    public int getMaxVis(ItemStack stack) {
        return 25000;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.RARE;
    }

    @Override
    public void addInformation(ItemStack stack, @javax.annotation.Nullable World world, List<String> list, net.minecraft.client.util.ITooltipFlag flags) {

        list.add(TextFormatting.GOLD + I18n.translateToLocal("item.capacity.text") + " " + this.getMaxVis(stack) / 100);
        if(stack.hasTagCompound()) {
            Iterator count = Aspect.getPrimalAspects().iterator();

            while(count.hasNext()) {
                Aspect aspect = (Aspect)count.next();
                if(stack.getTagCompound().hasKey(aspect.getTag())) {
                    String amount = this.myFormatter.format((double)((float)stack.getTagCompound().getInteger(aspect.getTag()) / 100.0F));
                    list.add("\u00a7" + aspect.getChatcolor() + aspect.getName() + TextFormatting.RESET + " x " + amount);
                }
            }
            if(stack.getTagCompound().hasKey("owner"))
                list.add(I18n.translateToLocal("tooltip.collar.owner") + " " + stack.getTagCompound().getString("owner"));

            if(Compat.botan && GuiScreen.isShiftKeyDown() && getCosmeticItem(stack) != null)
                list.add(String.format(I18n.translateToLocal("botaniamisc.hasCosmetic"), getCosmeticItem(stack).getDisplayName()).replaceAll("&", "\u00a7"));

        }

    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, net.minecraft.util.EnumHand hand)
    {
        if (entity.world.isRemote)
        {
            return false;
        }
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer sub = (EntityPlayer)entity;
            IInventory baubles = BaublesApi.getBaubles(sub);
            if(baubles.getStackInSlot(0) == null) {
                if(!itemstack.hasTagCompound()){
                    NBTTagCompound tag = new NBTTagCompound();
                    itemstack.setTagCompound(tag);
                }
                itemstack.getTagCompound().setString("owner", player.getName());
                baubles.setInventorySlotContents(0, itemstack.copy());
                itemstack.setCount(0);
                sub.sendMessage(new TextComponentString(I18n.translateToLocal("message.collar.placescollar").replace("%s", player.getName())));
                player.sendMessage(new TextComponentString(I18n.translateToLocal("message.collar.youplacecollar").replace("%s", sub.getName())));
                return true;
            }
            else
                player.sendMessage(new TextComponentString(I18n.translateToLocal("message.collar.alreadywearing").replace("%s", sub.getName())));
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(CreativeTabs xCreativeTabs, net.minecraft.util.NonNullList<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public ItemStack getCosmeticItem(ItemStack stack) {
        if(stack == null || stack.getTagCompound() == null)
            return null;
        if(!stack.getTagCompound().hasKey("cosmeticItem"))
            return null;
        NBTTagCompound cosmetic = stack.getTagCompound().getCompoundTag("cosmeticItem");
        return new ItemStack(cosmetic);
    }

    @Override
    public void setCosmeticItem(ItemStack stack, ItemStack cosmetic) {
        if(stack == null)
            return;
        NBTTagCompound cmp = new NBTTagCompound();
        if(cosmetic != null)
            cosmetic.writeToNBT(cmp);
        NBTTagCompound tag = stack.getTagCompound();
        if(tag == null){
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        tag.setTag("cosmeticItem", cmp);
    }

}
