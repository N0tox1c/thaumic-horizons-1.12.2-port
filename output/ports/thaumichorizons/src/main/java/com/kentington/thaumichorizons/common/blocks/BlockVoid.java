//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import thaumcraft.common.config.Config;

public class BlockVoid extends LegacyBlock {

    public BlockVoid() {
        super(Config.airyMaterial);
        this.setHardness(-1.0f);
        this.setResistance(60000.0f);
        this.setTranslationKey("ThaumicHorizons_void");
        this.setBlockTextureName("ThaumicHorizons:void");
        this.setStepSound(new net.minecraft.block.SoundType(0f,1f,net.minecraft.init.SoundEvents.BLOCK_CLOTH_BREAK,net.minecraft.init.SoundEvents.BLOCK_CLOTH_STEP,net.minecraft.init.SoundEvents.BLOCK_CLOTH_PLACE,net.minecraft.init.SoundEvents.BLOCK_CLOTH_HIT,net.minecraft.init.SoundEvents.BLOCK_CLOTH_FALL));
        this.setLightLevel(1.0f);
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public void registerBlockIcons(final TextureMap register) {
        this.blockIcon = register.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:void", "blocks"));
    }

}
