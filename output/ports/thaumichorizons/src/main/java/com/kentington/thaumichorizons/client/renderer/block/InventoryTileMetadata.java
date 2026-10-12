package com.kentington.thaumichorizons.client.renderer.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/** Sets the native cached metadata for temporary, worldless inventory tile models. */
public final class InventoryTileMetadata {
    private InventoryTileMetadata() {}
    public static void set(TileEntity tile,int metadata) {
        ObfuscationReflectionHelper.setPrivateValue(TileEntity.class,tile,metadata,"blockMetadata","field_145847_g");
    }
}
