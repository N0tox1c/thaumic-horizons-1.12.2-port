package com.kentington.thaumichorizons.client.renderer;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.blocks.BlockVat;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Register all original vat atlas sprites; later TESR/baked model can reuse them. */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = ThaumicHorizons.MODID, value = Side.CLIENT)
public final class VatTextureStitch {
    private VatTextureStitch() {}

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (ThaumicHorizons.blockVat instanceof BlockVat)
            ((BlockVat) ThaumicHorizons.blockVat).registerVatSprites(event.getMap());
        if (ThaumicHorizons.blockVatSolid instanceof BlockVat)
            ((BlockVat) ThaumicHorizons.blockVatSolid).registerVatSprites(event.getMap());
    }
}
