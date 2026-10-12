package fox.spiteful.forbidden.compat;

import fox.spiteful.forbidden.Config;
import fox.spiteful.forbidden.items.ForbiddenItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

/** Original Botania cross-mod crafting recipes, registered during Forge's recipe phase. */
public final class ForbiddenBotaniaRecipes {
    private static final String MODID = "forbiddenmagic";
    private static IRecipe terrasteelCap;

    private ForbiddenBotaniaRecipes() {}

    private static IRecipe shaped(RegistryEvent.Register<IRecipe> event, String name,
                                  ItemStack result, Object... components) {
        ResourceLocation id = new ResourceLocation(MODID, name);
        IRecipe recipe = new ShapedOreRecipe(id, result, components).setRegistryName(id);
        event.getRegistry().register(recipe);
        return recipe;
    }

    public static void register(RegistryEvent.Register<IRecipe> event) {
        terrasteelCap = null;
        if (!Compat.botan) return;
        // This is the exact condition originally tested by ForbiddenBotany.
        boolean originalBotaniaHasNuggets;
        try {
            Class<?> oreDict = Class.forName("vazkii.botania.common.lib.LibOreDict");
            oreDict.getDeclaredField("MANASTEEL_NUGGET");
            originalBotaniaHasNuggets = true;
        } catch (ReflectiveOperationException | LinkageError error) {
            originalBotaniaHasNuggets = false;
        }
        if (!originalBotaniaHasNuggets) {
            Item mana = net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation("botania", "manaresource"));
            if (mana != null) {
                shaped(event, "botania_manasteel_from_nuggets", new ItemStack(mana, 1, 0),
                        "###", "###", "###", '#', new ItemStack(ForbiddenItems.resource, 1, 2));
                shaped(event, "botania_manasteel_to_nuggets", new ItemStack(ForbiddenItems.resource, 9, 2),
                        "#", '#', new ItemStack(mana, 1, 0));
                shaped(event, "botania_elementium_from_nuggets", new ItemStack(mana, 1, 7),
                        "###", "###", "###", '#', new ItemStack(ForbiddenItems.resource, 1, 4));
                shaped(event, "botania_elementium_to_nuggets", new ItemStack(ForbiddenItems.resource, 9, 4),
                        "#", '#', new ItemStack(mana, 1, 7));
            }
        }
        if (Config.crossWand) {
            terrasteelCap = shaped(event, "botania_terrasteel_cap",
                    new ItemStack(ForbiddenItems.wandCap, 1, 2),
                    "NNN", "N N", 'N', "nuggetTerrasteel");
        }
    }

    public static IRecipe getTerrasteelCap() {
        return terrasteelCap;
    }
}
