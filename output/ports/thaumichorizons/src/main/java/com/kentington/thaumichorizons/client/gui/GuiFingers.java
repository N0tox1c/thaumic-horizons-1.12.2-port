package com.kentington.thaumichorizons.client.gui;

import java.util.ArrayList;
import com.kentington.thaumichorizons.common.container.ContainerFingers;
import com.kentington.thaumichorizons.common.container.InventoryFingers;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.items.wands.ItemWandCasting;

/** TC4 1.12-compatible renderer for the original Fingers 190 x 234 UI. */
@SideOnly(Side.CLIENT)
public class GuiFingers extends GuiContainer {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("thaumichorizons", "textures/gui/guifingers.png");
    private final InventoryPlayer playerInventory;
    private final ContainerFingers fingers;
    private final InventoryFingers inventory;
    private final int[][] aspectLocations = {
            {72, 21}, {24, 43}, {24, 102}, {72, 124}, {120, 102}, {120, 43}
    };
    private final ArrayList<Aspect> primals = Aspect.getPrimalAspects();

    public GuiFingers(InventoryPlayer playerInventory) {
        this(new ContainerFingers(playerInventory), playerInventory);
    }

    private GuiFingers(ContainerFingers container, InventoryPlayer playerInventory) {
        super(container);
        this.playerInventory = playerInventory;
        this.fingers = container;
        this.inventory = container.tileEntity;
        this.xSize = 190;
        this.ySize = 234;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.enableBlend();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        GlStateManager.disableBlend();

        this.fingers.refreshResult();
        ItemStack wandStack = this.inventory.getStackInSlot(10);
        ItemWandCasting wand = !wandStack.isEmpty() && wandStack.getItem() instanceof ItemWandCasting
                && !((ItemWandCasting) wandStack.getItem()).isStaff(wandStack)
                ? (ItemWandCasting) wandStack.getItem() : null;
        AspectList cost = this.fingers.getArcanePreviewCost();
        if (cost.size() <= 0) return;

        for (int i = 0; i < this.primals.size() && i < this.aspectLocations.length; i++) {
            Aspect primal = this.primals.get(i);
            if (primal == null || cost.getAmount(primal) <= 0) continue;
            float amount = cost.getAmount(primal);
            float alpha = 0.5f + MathHelper.sin((this.playerInventory.player.ticksExisted + i * 10) / 2f) * 0.2f - 0.2f;
            if (wand != null) {
                amount *= wand.getConsumptionModifier(wandStack, this.playerInventory.player, primal, true);
                if (amount * 100f <= wand.getVis(wandStack, primal)) alpha = 1f;
            }
            UtilsFX.drawTag(this.guiLeft + aspectLocations[i][0] - 8,
                    this.guiTop + aspectLocations[i][1] - 8, primal, amount, 0, this.zLevel, 771, alpha, false);
        }

        if (wand == null || !wand.consumeAllVisCrafting(wandStack, this.playerInventory.player, cost, false)) {
            // The TC4 container already hides non-craftable output from the slot.
            // Render the original greyed-out recipe hint without granting an item.
            ItemStack hint = this.fingers.getArcanePreviewResult();
            if (!hint.isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.color(0.33f, 0.33f, 0.33f, 0.66f);
                this.itemRender.renderItemAndEffectIntoGUI(hint, this.guiLeft + 160, this.guiTop + 64);
                GlStateManager.popMatrix();
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.guiLeft + 168f, this.guiTop + 46f, 0f);
            GlStateManager.scale(0.5f, 0.5f, 1f);
            String message = "Insufficient vis";
            this.fontRenderer.drawString(message, -this.fontRenderer.getStringWidth(message) / 2, 0, 0xEE6E6E);
            GlStateManager.popMatrix();
        }
    }
}
