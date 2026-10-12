package fox.spiteful.forbidden.client;

import net.minecraftforge.fml.common.registry.VillagerRegistry;
import fox.spiteful.forbidden.CommonProxy;
import fox.spiteful.forbidden.Config;
import fox.spiteful.forbidden.tiles.TileEntityWrathCage;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderInfo() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWrathCage.class, new TileEntityWrathCageRenderer());
        // Skin belongs to the registered heretic profession.
    }

}