//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

public class BlockChocolate extends LegacyBlock {

    public TextureAtlasSprite coloredGrass;

    public BlockChocolate() {
        super(Material.CAKE);
        this.setHardness(0.5f);
        this.setResistance(0.5f);
        this.setTranslationKey("ThaumicHorizons_chocolate");
        this.setBlockTextureName("ThaumicHorizons:chocolate");
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public void registerBlockIcons(final TextureMap register) {
        this.blockIcon = register.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:chocolate", "blocks"));
        this.coloredGrass = register.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:grasscolorized", "blocks"));
    }

}
