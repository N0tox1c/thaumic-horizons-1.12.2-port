package fox.spiteful.forbidden.items.scribes;

import java.util.Iterator;

import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.LogHandler;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import thaumcraft.api.IScribeTools;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.lib.research.ResearchManager;

public class ItemCrystalwell extends Item implements IScribeTools {
    @Override public boolean hasEffect(ItemStack stack) { return hasEffect(stack,0); }


    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon;

    public ItemCrystalwell() {
        maxStackSize = 1;
        canRepair = false;
        if(Config.researchDifficulty == -1)
            setMaxDamage(50);
        else
            setMaxDamage(100);
        setCreativeTab(Forbidden.tab);
        setHasSubtypes(false);

    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        this.icon = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:crystalwell"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int par1) {
        return this.icon;
    }

    @Override
    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, net.minecraft.util.EnumHand hand) {
        return new net.minecraft.util.ActionResult<>(net.minecraft.util.EnumActionResult.SUCCESS, legacyRightClick(player.getHeldItem(hand), world, player));
    }
    public ItemStack legacyRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (Config.researchDifficulty != -1 && stack.getItemDamage() >= 100) {
            if (!world.isRemote) {
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
            player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
            return new ItemStack(ConfigItems.itemInkwell, 1, 100);
        } else
            return stack;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {

        if(world.isRemote)
            return;
        if(Config.researchDifficulty == -1 && entity.ticksExisted % 200 == 0 && (entity instanceof EntityPlayer)){
            EntityPlayer player = (EntityPlayer)entity;
            Aspect aspect = (Aspect)Aspect.getPrimalAspects().get(world.rand.nextInt(6));
            short amount = (short)(world.rand.nextInt(4) + 1);
            Thaumcraft.proxy.getPlayerKnowledge(player).addAspectPool( aspect, amount);
            PacketHandler.INSTANCE.sendTo(new PacketAspectPool(aspect.getTag(), amount, (short)(Thaumcraft.proxy.getPlayerKnowledge(player).getAspectPoolFor( aspect))), (EntityPlayerMP) player);
            ResearchManager.updateCache(player);
            stack.setItemDamage(stack.getItemDamage() + 1);
            if(stack.getItemDamage() >= stack.getMaxDamage())
                ((EntityPlayer)entity).inventory.setInventorySlotContents(slot, null);
        }
    }

    /**
     * Render Pass sensitive version of hasEffect()
     */
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {
        return Config.researchDifficulty != -1 && stack.getItemDamage() >= 100;
    }

}