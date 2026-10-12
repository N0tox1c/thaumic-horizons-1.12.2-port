package com.kentington.thaumichorizons.client.renderer.tile;

import com.kentington.thaumichorizons.client.renderer.model.ModelBloodInfuser;
import com.kentington.thaumichorizons.client.renderer.model.ModelSyringe;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileBloodInfuser;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/** 1.12 TESR retaining the original Blood Infuser body and syringe meshes. */
public class TileBloodInfuserRender extends TileEntitySpecialRenderer<TileBloodInfuser> {
    private static final ResourceLocation BODY_TEXTURE =
            new ResourceLocation("thaumichorizons", "textures/models/bloodinfuser.png");
    private static final ResourceLocation SYRINGE_TEXTURE =
            new ResourceLocation("thaumichorizons", "textures/models/syringe.png");
    private final ModelBloodInfuser base = new ModelBloodInfuser();
    private final ModelSyringe syringe = new ModelSyringe();

    @Override
    public void render(TileBloodInfuser tile, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha) {
        if (tile == null) return;
        GlStateManager.pushMatrix();
        try {
            GlStateManager.translate(x + 0.5D, y + 1.5D, z + 0.5D);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            bindTexture(BODY_TEXTURE);
            base.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        } finally {
            GlStateManager.popMatrix();
        }
        if (tile.hasBlood()) {
            GlStateManager.pushMatrix();
            try {
                GlStateManager.translate(x + 1.90625D, y + 0.75D, z + 0.46875D);
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                bindTexture(SYRINGE_TEXTURE);
                syringe.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F,
                        0.0625F, new ItemStack(ThaumicHorizons.itemSyringeHuman));
            } finally {
                GlStateManager.popMatrix();
            }
        }
    }
}
