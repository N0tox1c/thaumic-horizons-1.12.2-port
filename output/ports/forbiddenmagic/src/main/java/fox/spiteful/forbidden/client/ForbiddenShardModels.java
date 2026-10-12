package fox.spiteful.forbidden.client;

import fox.spiteful.forbidden.Config;
import fox.spiteful.forbidden.items.ForbiddenItems;
import fox.spiteful.forbidden.blocks.ForbiddenBlocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/** Minimal 1.12 item models for all variants of the original shards/resources. */
@Mod.EventBusSubscriber(modid = "forbiddenmagic", value = Side.CLIENT)
public final class ForbiddenShardModels {
    private ForbiddenShardModels() {}
    private static void bind(Item item, int meta, String model) {
        if (item == null) return;
        ModelLoader.setCustomModelResourceLocation(item, meta,
                new ModelResourceLocation(new ResourceLocation("forbiddenmagic", model), "inventory"));
    }

    @SubscribeEvent
    public static void onModels(ModelRegistryEvent event) {
        for (int meta = 0; meta < 7; ++meta) {
            String name = meta == 4 && Config.noLust ? "nethershard_4_despair" : "nethershard_" + meta;
            bind(ForbiddenItems.deadlyShards, meta, name);
        }
        for (int meta = 0; meta < 5; ++meta) bind(ForbiddenItems.resource, meta, "fmresource_" + meta);
        bind(ForbiddenItems.gluttonyShard, 0, "gluttonyshard");
        // These two original textures depend on the same mob-crystal NBT tag.
        if (ForbiddenItems.mobCrystal != null) {
            final Item crystal = ForbiddenItems.mobCrystal;
            final ResourceLocation empty = new ResourceLocation("forbiddenmagic", "mobcrystal_empty");
            final ResourceLocation full = new ResourceLocation("forbiddenmagic", "mobcrystal_filled");
            ModelBakery.registerItemVariants(crystal, empty, full);
            ModelLoader.setCustomMeshDefinition(crystal, stack ->
                    new ModelResourceLocation(stack.hasTagCompound()
                            && stack.getTagCompound().hasKey("mob") ? full : empty, "inventory"));
        }
        // The original tainted stone has TWO item metadata variants.
        Item stone = Item.getItemFromBlock(ForbiddenBlocks.taintStone);
        bind(stone, 0, "taintblock_smooth");
        bind(stone, 1, "taintblock_brick");
    }
}
