package fox.spiteful.forbidden.potions;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Original Blood Seal effect registered without modifying the vanilla potion array. */
@Mod.EventBusSubscriber(modid = "forbiddenmagic")
public final class DarkPotions {
    private DarkPotions() {}
    public static final ResourceLocation icons = new ResourceLocation("forbidden", "textures/misc/potions.png");
    public static Potion bloodSeal;
    public static Potion dragonwrack; // Reserved for original optional content.

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        // Stable registry identity even when Blood Magic isn't installed.
        // Only the Blood Magic-dependent crafting and effects remain conditional.
        bloodSeal = new PotionBloodSeal();
        bloodSeal.setRegistryName("forbidden", "blood_seal");
        event.getRegistry().register(bloodSeal);
    }

    @Deprecated
    public static void alchemize() {
        // 1.7.10 legacy hook: registration is exclusively via RegistryEvent.
    }
}
