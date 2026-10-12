package com.kentington.thaumichorizons.client.renderer.tile;

import com.kentington.thaumichorizons.client.renderer.model.ModelVat;
import com.kentington.thaumichorizons.common.tiles.TileVat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

/** Original large Vat model: preserved, not replaced by a baked vanilla cube. */
public class TileVatRender extends TileEntitySpecialRenderer<TileVat> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("thaumichorizons", "textures/models/vat.png");
    private final ModelVat model = new ModelVat();

    @Override
    public void render(TileVat tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (tile == null) return;
        GlStateManager.pushMatrix();
        try {
            GlStateManager.translate(x + 0.5D, y - 0.5D, z + 0.5D);
            GlStateManager.enableRescaleNormal();
            bindTexture(TEXTURE);
            model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        } finally {
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
        }
    }
}
