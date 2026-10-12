package fox.spiteful.forbidden;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DarkResearchItem extends ResearchItem {
    String inter = null;

    public DarkResearchItem(String par1, String x) {
        super(par1, x);
    }

    public DarkResearchItem(String par1, String x, AspectList tags, int y, int z, int par5, ResourceLocation icon) {
        super(par1, x, tags, y, z, par5, icon);
    }

    public DarkResearchItem(String par1, String x, AspectList tags, int y, int z, int par5, ItemStack icon) {
        super(par1, x, tags, y, z, par5, icon);
    }

    public DarkResearchItem(String par1, String x, String mod, AspectList tags, int y, int z, int par5, ResourceLocation icon) {
        super(par1, x, tags, y, z, par5, icon);
        inter = mod;
    }

    public DarkResearchItem(String par1, String x, String mod, AspectList tags, int y, int z, int par5, ItemStack icon) {
        super(par1, x, tags, y, z, par5, icon);
        inter = mod;
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return I18n.translateToLocal("forbidden.research_name." + key);
    }

    @SideOnly(Side.CLIENT)
    public String getText() {
        if (Config.tagResearch) {
            if (inter == null)
                return "[FM] " + I18n.translateToLocal("forbidden.research_text." + key);
            else
                return "[FM] " + inter + " " + I18n.translateToLocal("forbidden.research_text." + key);
        } else
            return I18n.translateToLocal("forbidden.research_text." + key);
    }
}