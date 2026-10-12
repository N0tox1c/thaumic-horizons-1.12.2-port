package com.kentington.thaumichorizons.client.renderer.tile;

import com.kentington.thaumichorizons.common.tiles.TileVat;
import com.kentington.thaumichorizons.common.tiles.TileVatSlave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/** Displays the occupant, effigy or sample in the original assembled Vat. */
public class TileVatSlaveRender extends TileEntitySpecialRenderer<TileVatSlave> {
    private static final ResourceLocation EFFIGY =
            new ResourceLocation("thaumichorizons", "textures/models/corpseeffigy.png");
    private static final ResourceLocation REVIVED =
            new ResourceLocation("thaumichorizons", "textures/models/corpseeffigyrevived.png");
    private final ModelBiped corpse = new ModelBiped();

    @Override
    public void render(TileVatSlave tile, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha) {
        if (tile == null || tile.getWorld() == null) return;
        BlockPos here = tile.getPos();
        int meta = tile.getWorld().getBlockState(here).getBlock().getMetaFromState(
                tile.getWorld().getBlockState(here));
        BlockPos above = here.up();
        int aboveMeta = tile.getWorld().getBlockState(above).getBlock().getMetaFromState(
                tile.getWorld().getBlockState(above));
        if (meta != 0 || aboveMeta != 7) return;
        if (!(tile.getWorld().getTileEntity(above) instanceof TileVat)) return;
        TileVat vat = (TileVat) tile.getWorld().getTileEntity(above);
        float ticks = tile.getWorld().getTotalWorldTime() + partialTicks;
        float bob = 0.1F * MathHelper.cos((float) Math.toRadians(ticks));

        Entity occupant = vat.getEntityContained();
        if (occupant != null && !(occupant instanceof EntityPlayer)) {
            float fade = vat.mode == 1 ? MathHelper.clamp((800.0F - vat.progress) / 800.0F, 0.0F, 1.0F) : 1.0F;
            GlStateManager.pushMatrix();
            try {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.color(1.0F, 1.0F, 1.0F, fade);
                RenderManager manager = Minecraft.getMinecraft().getRenderManager();
                // RenderManager takes camera-relative coordinates. Never add the
                // contained entity's saved world coordinates to TESR coordinates.
                manager.renderEntity(occupant, x + 0.5D, y + 1.0D + (vat.mode == 1 ? 0.0F : bob),
                        z + 0.5D, occupant.rotationYaw, partialTicks, false);
            } finally {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        } else if (vat.mode == 3 || vat.mode == 4 || (vat.mode == 2 && vat.recipeType == 1)) {
            GlStateManager.pushMatrix();
            try {
                GlStateManager.translate(x + 0.5D, y + 1.5D + bob, z + 0.5D);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                bindTexture(vat.mode == 3 ? EFFIGY : REVIVED);
                corpse.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F);
            } finally {
                GlStateManager.popMatrix();
            }
        } else {
            ItemStack sample = vat.sample;
            if (sample != null && !sample.isEmpty()) {
                GlStateManager.pushMatrix();
                try {
                    GlStateManager.translate(x + 0.5D, y + 1.05D + bob, z + 0.5D);
                    GlStateManager.scale(0.85F, 0.85F, 0.85F);
                    RenderHelper.enableStandardItemLighting();
                    Minecraft.getMinecraft().getRenderItem().renderItem(sample,
                            ItemCameraTransforms.TransformType.GROUND);
                } finally {
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.popMatrix();
                }
            }
        }
    }
}
