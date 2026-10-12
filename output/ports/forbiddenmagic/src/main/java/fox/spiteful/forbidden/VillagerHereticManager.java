package fox.spiteful.forbidden;

import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraft.entity.IMerchant;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;
import fox.spiteful.forbidden.items.ForbiddenItems;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.Random;

@Mod.EventBusSubscriber(modid = "forbiddenmagic")
public class VillagerHereticManager implements EntityVillager.ITradeList {
    public static VillagerRegistry.VillagerProfession profession;
    @SubscribeEvent
    public static void registerProfession(RegistryEvent.Register<VillagerRegistry.VillagerProfession> event) {
        profession = new VillagerRegistry.VillagerProfession("forbiddenmagic:heretic", "forbidden:textures/models/heretic.png", "minecraft:textures/entity/zombie_villager/zombie_villager.png");
        event.getRegistry().register(profession);
        new VillagerRegistry.VillagerCareer(profession, "heretic").addTrade(1, new VillagerHereticManager());
    }
    @Override
    public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
        if (merchant instanceof EntityVillager) manipulateTradesForVillager((EntityVillager) merchant, recipeList, random);
    }


    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
        if(villager.getProfessionForge() == profession) {
            /*recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 20 + random.nextInt(3), 18), new ItemStack(Items.emerald)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemResource, 1, 9)));
            recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 4 + random.nextInt(3), 3), new ItemStack(Items.emerald)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemResource, 1, 0)));
            recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 4 + random.nextInt(3), 6), new ItemStack(Items.emerald)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemResource, 1, 1)));
            recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemNuggetChicken, 24 + random.nextInt(8), 0), new ItemStack(Items.emerald)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.book, 4 + random.nextInt(3), 0), new ItemStack(ConfigItems.itemResource, 1, 9)));
            recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemNuggetBeef, 24 + random.nextInt(8), 0), new ItemStack(Items.emerald)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemShard, 2 + random.nextInt(2), random.nextInt(6))));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemManaBean, 1 + random.nextInt(2), 0)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 5 + random.nextInt(3)), new ItemStack(ConfigItems.itemBathSalts, 1, 0)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 5 + random.nextInt(3)), new ItemStack(ConfigItems.itemRingRunic, 1, 0)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 5 + random.nextInt(3)), new ItemStack(ConfigItems.itemAmuletVis, 1, 0)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 5 + random.nextInt(3)), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 3 + random.nextInt(6))));
            */
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 1 + random.nextInt(2)), new ItemStack(ForbiddenItems.deadlyShards, 3 + random.nextInt(2), 6)));
            if(!Config.noLust)
                recipeList.add(new MerchantRecipe(new ItemStack(ForbiddenItems.deadlyShards, 2 + random.nextInt(4), 4), new ItemStack(Items.EMERALD)));
            recipeList.add(new MerchantRecipe(new ItemStack(ForbiddenItems.deadlyShards, 4 + random.nextInt(6), 0), new ItemStack(Items.EMERALD)));
            recipeList.add(new MerchantRecipe(new ItemStack(ForbiddenItems.gluttonyShard, 4 + random.nextInt(6), 0), new ItemStack(Items.EMERALD)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 1 + random.nextInt(3)), new ItemStack(Items.SKULL, 1, 0)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 4 + random.nextInt(4)), new ItemStack(Items.GHAST_TEAR, 1)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.GLOWSTONE_DUST, 8 + random.nextInt(9))));

            ItemStack wand = new ItemStack(ConfigItems.itemWandCasting, 1, 36);
            ((ItemWandCasting) wand.getItem()).setCap(wand, (WandCap) WandCap.caps.get("iron"));
            ((ItemWandCasting) wand.getItem()).setRod(wand, (WandRod) WandRod.rods.get("profane"));
            ((ItemWandCasting) wand.getItem()).storeAllVis(wand, new AspectList().add(Aspect.FIRE, 5000).add(Aspect.WATER, 5000).add(Aspect.EARTH, 5000).add(Aspect.AIR, 5000).add(Aspect.ORDER, 5000).add(Aspect.ENTROPY, 5000));
            MerchantRecipe profane = new MerchantRecipe(new ItemStack(Items.EMERALD, 8 + random.nextInt(5)), new ItemStack(Items.STICK), wand);
            profane.increaseMaxTradeUses(-6);
            recipeList.add(profane);
            MerchantRecipe fire = new MerchantRecipe(new ItemStack(Items.EMERALD, 4 + random.nextInt(5)), new ItemStack(Items.DIAMOND, 1), new ItemStack(ConfigItems.focusFire, 1));
            fire.increaseMaxTradeUses(-6);
            recipeList.add(fire);
        }

    }
}
