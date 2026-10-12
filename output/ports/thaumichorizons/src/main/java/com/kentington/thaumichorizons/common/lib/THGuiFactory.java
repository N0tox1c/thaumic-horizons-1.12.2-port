//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.fml.client.IModGuiFactory;

public class THGuiFactory implements IModGuiFactory {

    public void initialize(final Minecraft minecraftInstance) {}

    public boolean hasConfigGui() { return true; }

    public GuiScreen createConfigGui(GuiScreen parent) { return new THConfigGUI(parent); }

    public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

}
