package com.kentington.thaumichorizons.client.gui;

import com.kentington.thaumichorizons.common.container.ContainerCase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Original case layout with TC4 focus pouch's original texture. */
@SideOnly(Side.CLIENT)
public class GuiCase extends GuiContainer {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("thaumcraft", "textures/gui/gui_focuspouch.png");
    private final int selectedSlot;

    public GuiCase(InventoryPlayer inventory, World world, int x, int y, int z) {
        super(new ContainerCase(inventory, world, x, y, z));
        selectedSlot = inventory.currentItem;
        xSize = 176;
        ySize = 233;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.enableBlend();
        drawTexturedModalRect(8 + selectedSlot * 18, 209, 240, 0, 16, 16);
        GlStateManager.disableBlend();
    }

    @Override
    protected boolean checkHotbarKeys(int keyCode) { return false; }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // The 1.12 mainInventory is a List, not an ItemStack[].
        if (mc.player.inventory.getCurrentItem().isEmpty()) {
            mc.player.closeScreen();
            return;
        }
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
