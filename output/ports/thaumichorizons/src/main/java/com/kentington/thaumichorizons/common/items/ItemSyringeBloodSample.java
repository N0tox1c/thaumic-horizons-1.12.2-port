package com.kentington.thaumichorizons.common.items;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.capabilities.IPlayerKnowledge;
import thaumcraft.common.lib.capabilities.PlayerKnowledgeProvider;

/**
 * Faithful 1.12 port of the original sample display and clone-cost tooltip.
 * The old NBT keys critter/CreatureInfusion/InfusionCosts/Aspects remain unchanged.
 * The actual Vat clone operation is NOT implemented by this class.
 */
public class ItemSyringeBloodSample extends Item {
    public ItemSyringeBloodSample() {
        setCreativeTab(ThaumicHorizons.tabTH);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String base=I18n.translateToLocal("item.syringeSample.name");
        if (stack.hasTagCompound()) {
            return base + ": " + stack.getTagCompound().getString("critterName");
        }
        return base + ": INVALID";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack sample, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add("Essentia required to clone:");
        AspectList aspects = new AspectList().add(Aspect.LIFE, 4);
        if (sample.hasTagCompound()) {
            NBTTagCompound critter = sample.getTagCompound().getCompoundTag("critter");
            NBTTagCompound infusion = critter.getCompoundTag("CreatureInfusion");
            NBTTagCompound costs = infusion.getCompoundTag("InfusionCosts");
            if (costs.hasKey("Aspects", 9)) {
                NBTTagList list = costs.getTagList("Aspects", 10);
                for (int i = 0; i < list.tagCount(); ++i) {
                    NBTTagCompound entry = list.getCompoundTagAt(i);
                    if (entry.hasKey("key", 8)) {
                        Aspect aspect = Aspect.getAspect(entry.getString("key"));
                        int amount = entry.getInteger("amount");
                        if (aspect != null && amount > 0) aspects.add(aspect, amount);
                    }
                }
            }
        }
        EntityPlayer player = Minecraft.getMinecraft().player;
        IPlayerKnowledge knowledge = player == null ? null : player.getCapability(PlayerKnowledgeProvider.PLAYER_KNOWLEDGE, null);
        for (Aspect a : aspects.getAspectsSorted()) {
            if (a == null) continue;
            if (knowledge != null && knowledge.hasDiscoveredAspect(a)) {
                tooltip.add(a.getName() + " x" + aspects.getAmount(a));
            } else {
                tooltip.add(I18n.translateToLocal("tc.aspect.unknown"));
            }
        }
    }
}
