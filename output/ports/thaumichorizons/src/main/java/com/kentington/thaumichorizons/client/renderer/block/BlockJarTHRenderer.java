//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.blocks.BlockSoulJar;




public class BlockJarTHRenderer extends BlockRenderer {

    public void renderInventoryBlock(final Block block, final int metadata, final int modelID,
            final NativeBlockMesh renderer) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        final TextureAtlasSprite i1 = ((BlockSoulJar) block).iconJarTop;
        final TextureAtlasSprite i2 = ((BlockSoulJar) block).iconJarSide;
        renderer.setRenderBounds(
                BlockJarTHRenderer.W3,
                0.0f,
                BlockJarTHRenderer.W3,
                BlockJarTHRenderer.W13,
                BlockJarTHRenderer.W12,
                BlockJarTHRenderer.W13);
        drawFaces(renderer, block, ((BlockSoulJar) block).iconJarBottom, i1, i2, i2, i2, i2, true);
        renderer.setRenderBounds(
                BlockJarTHRenderer.W5,
                BlockJarTHRenderer.W12,
                BlockJarTHRenderer.W5,
                BlockJarTHRenderer.W11,
                BlockJarTHRenderer.W14,
                BlockJarTHRenderer.W11);
        drawFaces(renderer, block, ((BlockSoulJar) block).iconJarBottom, i1, i2, i2, i2, i2, true);
        GL11.glPopMatrix();
    }

    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block,
            final int modelId, final NativeBlockMesh renderer) {
        final int bb = setBrightness(world, x, y, z, block);
        final int metadata = world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));
        renderer.setRenderBounds(
                BlockJarTHRenderer.W3,
                0.0f,
                BlockJarTHRenderer.W3,
                BlockJarTHRenderer.W13,
                BlockJarTHRenderer.W12,
                BlockJarTHRenderer.W13);
        drawFaces(renderer, block, ((BlockSoulJar) block).iconJarBottom, ((BlockSoulJar) block).iconJarTop, ((BlockSoulJar) block).iconJarSide, ((BlockSoulJar) block).iconJarSide, ((BlockSoulJar) block).iconJarSide, ((BlockSoulJar) block).iconJarSide, false);
        renderer.setRenderBounds(
                BlockJarTHRenderer.W5,
                BlockJarTHRenderer.W12,
                BlockJarTHRenderer.W5,
                BlockJarTHRenderer.W11,
                BlockJarTHRenderer.W14,
                BlockJarTHRenderer.W11);
        drawFaces(renderer, block, ((BlockSoulJar) block).iconJarBottom, ((BlockSoulJar) block).iconJarTop, ((BlockSoulJar) block).iconJarSide, ((BlockSoulJar) block).iconJarSide, ((BlockSoulJar) block).iconJarSide, ((BlockSoulJar) block).iconJarSide, false);
        
        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        return true;
    }

    public boolean shouldRender3DInInventory(final int modelId) {
        return true;
    }

    public int getRenderId() {
        return ThaumicHorizons.blockJarRI;
    }
}
