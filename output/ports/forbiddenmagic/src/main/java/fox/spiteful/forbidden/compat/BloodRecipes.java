package fox.spiteful.forbidden.compat;
import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.core.registry.AltarRecipeRegistry;
import net.minecraft.item.ItemStack;
public final class BloodRecipes {
    private BloodRecipes() {}
    public static void registerAltarRecipe(ItemStack output, ItemStack input, int tier, int cost, int consume, int drain, boolean fill) {
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(input, output, AltarTier.values()[tier - 1], cost, consume, drain, fill));
    }
    public static void registerAltarOrbRecipe(ItemStack orb, int tier, int fillRate) {
        AltarRecipeRegistry.registerFillRecipe(orb, AltarTier.values()[tier - 1], 0, fillRate, fillRate);
    }
}
