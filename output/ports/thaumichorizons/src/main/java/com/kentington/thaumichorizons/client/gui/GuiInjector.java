package com.kentington.thaumichorizons.client.gui;

import com.kentington.thaumichorizons.common.container.ContainerInjector;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Original seven-chamber injector layout and texture on the 1.12 renderer. */
@SideOnly(Side.CLIENT)
public class GuiInjector extends GuiContainer {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("thaumichorizons", "textures/gui/guiinjector.png");

    public GuiInjector(EntityPlayer player) {
        super(new ContainerInjector(player));
        xSize = 175;
        ySize = 191;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
