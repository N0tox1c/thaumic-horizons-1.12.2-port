package fox.spiteful.forbidden;

import fox.spiteful.forbidden.blocks.ForbiddenBlocks;
import fox.spiteful.forbidden.items.ForbiddenItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

/** Registers every original vanilla-workbench recipe during the 1.12 Forge recipe registry window. */
public final class ForbiddenVanillaRecipes {
    private static final String MODID = "forbiddenmagic";
    private ForbiddenVanillaRecipes() {}

    private static IRecipe shaped(RegistryEvent.Register<IRecipe> event, String id,
                                  ItemStack output, Object[] ingredients) {
        // Preserve the 1.7.10 shaped recipe layout and OreDictionary ingredients.
        IRecipe recipe = new ShapedOreRecipe(new ResourceLocation(MODID, id), output, ingredients);
        recipe.setRegistryName(new ResourceLocation(MODID, id));
        event.getRegistry().register(recipe);
        return recipe;
    }

    public static void register(RegistryEvent.Register<IRecipe> event) {
        ForbiddenResearch.recipes.put("TaintPlank", shaped(event, "taint_plank", new ItemStack(ForbiddenBlocks.taintPlanks, 4, 0), new Object[]{"#", Character.valueOf('#'), new ItemStack(ForbiddenBlocks.taintLog, 1, 0)}));
        ForbiddenResearch.recipes.put("TaintBrick", shaped(event, "taint_brick", new ItemStack(ForbiddenBlocks.taintStone, 4, 1), new Object[] { "##", "##", Character.valueOf('#'), new ItemStack(ForbiddenBlocks.taintStone, 1, 0) }));
        ForbiddenResearch.recipes.put("BlackInk", shaped(event, "black_ink", new ItemStack(ForbiddenItems.resource, 2, 1), new Object[]{"#", Character.valueOf('#'), new ItemStack(ForbiddenBlocks.blackFlower, 1, 0)}));
        shaped(event, "emerald_block", new ItemStack(Items.EMERALD, 1, 0), new Object[] { "###", "###", "###", Character.valueOf('#'), new ItemStack(ForbiddenItems.resource, 1, 0) });
        shaped(event, "emerald_unblock", new ItemStack(ForbiddenItems.resource, 9, 0), new Object[] { "#", Character.valueOf('#'), new ItemStack(Items.EMERALD, 1, 0) });
        shaped(event, "star_block", new ItemStack(ForbiddenBlocks.starBlock, 1, 0), new Object[] { "###", "###", "###", Character.valueOf('#'), new ItemStack(Items.NETHER_STAR, 1, 0) });
        shaped(event, "star_unblock", new ItemStack(Items.NETHER_STAR, 9, 0), new Object[]{"#", Character.valueOf('#'), new ItemStack(ForbiddenBlocks.starBlock, 1, 0)});
        ForbiddenResearch.recipes.put("RidingCrop", shaped(event, "riding_crop", new ItemStack(ForbiddenItems.ridingCrop, 1, 0), new Object[]{"X", "#", "#", Character.valueOf('#'), Items.STICK, Character.valueOf('X'), Items.LEATHER}));
    }
}
