//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;


import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockVatSolid extends BlockVat {

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconLidCenterTop;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconLidSideCenter;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconLidLeftRight;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconInnerCenter;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconInnerPosZ;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconInnerNegZ;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconInnerPosX;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconInnerNegX;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconInnerCornerA;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconInnerCornerB;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconInnerCornerC;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconInnerCornerD;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconBaseCenter;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconBaseLeftRight;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconBaseSide;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconBaseSideBottom;
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite iconGreatwood;

    public BlockVatSolid() {
        super(Material.WOOD);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.SOLID; }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerVatSprites(TextureMap atlas) {
        super.registerVatSprites(atlas);
        iconLidCenterTop = sprite(atlas, "vatlidtopcenter");
        iconLidSideCenter = sprite(atlas, "vatlidcenter");
        iconLidLeftRight = sprite(atlas, "vatlidleftright");
        iconInnerCenter = sprite(atlas, "vatinnercenter");
        iconInnerPosZ = sprite(atlas, "vatinnerposz");
        iconInnerNegZ = sprite(atlas, "vatinnernegz");
        iconInnerPosX = sprite(atlas, "vatinnerposx");
        iconInnerNegX = sprite(atlas, "vatinnernegx");
        iconInnerCornerA = sprite(atlas, "vatinnercornera");
        iconInnerCornerB = sprite(atlas, "vatinnercornerb");
        iconInnerCornerC = sprite(atlas, "vatinnercornerc");
        iconInnerCornerD = sprite(atlas, "vatinnercornerd");
        iconBaseCenter = sprite(atlas, "vatbasecenter");
        iconBaseLeftRight = sprite(atlas, "vatbaseleftright");
        iconBaseSide = sprite(atlas, "vatbasesidecenter");
        iconBaseSideBottom = sprite(atlas, "vatbasesidebottom");
        iconGreatwood = atlas.registerSprite(new ResourceLocation("thaumcraft", "blocks/planks_greatwood"));
    }

    @SideOnly(Side.CLIENT)
    private static TextureAtlasSprite sprite(TextureMap atlas, String path) {
        return atlas.registerSprite(new ResourceLocation("thaumichorizons", "blocks/" + path));
    }

}
