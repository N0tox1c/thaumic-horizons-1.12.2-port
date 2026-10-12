package fox.spiteful.forbidden.client;

import fox.spiteful.forbidden.tiles.TileEntityWrathCage;
import fox.spiteful.forbidden.tiles.WrathSpawnerLogic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

/** Original hovering mob preview; no entity may be spawned by the renderer. */
public class TileEntityWrathCageRenderer extends TileEntitySpecialRenderer<TileEntityWrathCage> {
    @Override
    public void render(TileEntityWrathCage cage, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha) {
        if (cage == null || cage.getWorld() == null) return;
        WrathSpawnerLogic logic = cage.getSpawnerLogic();
        if (!logic.isMobSet()) return;
        Entity preview = logic.getEntityForRender();
        if (preview == null) return; // old saved entity name may not exist in 1.12

        GlStateManager.pushMatrix();
        try {
            GlStateManager.translate(x + 0.5D, y + 0.4D, z + 0.5D);
            String id = logic.getEntityNameToSpawn();
            float scale = "Ghast".equals(id) ? 0.1F
                    : ("Slime".equals(id) || "ThaumSlime".equals(id)) ? 0.4F
                    : "Enderman".equals(id) ? 0.3F : 0.4375F;
            BlockPos pos = cage.getPos();
            if (!cage.getWorld().isBlockPowered(pos)) {
                float spin = (float)(logic.field_98284_d
                        + (logic.field_98287_c - logic.field_98284_d) * partialTicks) * 10.0F;
                GlStateManager.rotate(spin, 0.0F, 1.0F, 0.0F);
            }
            GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(scale, scale, scale);
            RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
            renderManager.renderEntity(preview, 0.0D, 0.0D, 0.0D,
                    preview.rotationYaw, partialTicks, false);
        } finally {
            GlStateManager.popMatrix();
        }
    }
}
