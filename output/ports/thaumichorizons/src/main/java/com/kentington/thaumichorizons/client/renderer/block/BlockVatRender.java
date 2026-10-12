//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client.renderer.block;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.blocks.BlockVat;
import com.kentington.thaumichorizons.common.tiles.TileVat;
import com.kentington.thaumichorizons.common.tiles.TileVatSlave;



public class BlockVatRender extends NativeBlockRenderer {

    public void renderInventoryBlock(final Block block, final int metadata, final int modelID,
            final NativeBlockMesh renderer) {}

    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block,
            final int modelId, final NativeBlockMesh renderer) {
        if (world.getTileEntity(new BlockPos(x, y, z)) instanceof TileVatSlave) {
            final TileVatSlave tco = ((TileVatSlave) (world.getTileEntity(new BlockPos(x, y, z))));

            final TileVat boss = tco.getBoss(-1);
            if (boss == null) {
                return false;
            }
    
            renderer.setColor(255, 255, 255, 192);

            renderer.enableAO = false;
            final int dx = boss.getPos().getX() - tco.getPos().getX();
            final int dy = boss.getPos().getY() - tco.getPos().getY();
            final int dz = boss.getPos().getZ() - tco.getPos().getZ();
            if (world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z))) == 10) {
                if (dy == 1) {
                    if (dx == -1 && dz == -1) {
                        renderer.renderFaceXPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassTL);
                        renderer.renderFaceZPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassTR);
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 0.75f, 1.0f, 0.75f);
                        renderer.setColor(255, 255, 255, 255);
                        renderer.renderFaceXPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.renderFaceZPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    } else if (dx == 1 && dz == -1) {
                        renderer.renderFaceXNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassTR);
                        renderer.renderFaceZPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassTL);
                        renderer.setRenderBounds(0.25f, 0.0f, 0.0f, 1.0f, 1.0f, 0.75f);
                        renderer.setColor(255, 255, 255, 255);
                        renderer.renderFaceXNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.renderFaceZPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    } else if (dx == 1 && dz == 1) {
                        renderer.renderFaceXNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassTL);
                        renderer.renderFaceZNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassTR);
                        renderer.setRenderBounds(0.25f, 0.0f, 0.25f, 1.0f, 1.0f, 1.0f);
                        renderer.setColor(255, 255, 255, 255);
                        renderer.renderFaceXNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.renderFaceZNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    } else if (dx == -1 && dz == 1) {
                        renderer.renderFaceXPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassTR);
                        renderer.renderFaceZNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassTL);
                        renderer.setRenderBounds(0.0f, 0.0f, 0.25f, 0.75f, 1.0f, 1.0f);
                        renderer.setColor(255, 255, 255, 255);
                        renderer.renderFaceXPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.renderFaceZNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    } else if (dx == 0) {
                        if (dz == -1) {
                            renderer.renderFaceZPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassT);
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.75f);
                            renderer.setColor(255, 255, 255, 255);
                            renderer.renderFaceZPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        } else if (dz == 1) {
                            renderer.renderFaceZNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassT);
                            renderer.setRenderBounds(0.0f, 0.0f, 0.25f, 1.0f, 1.0f, 1.0f);
                            renderer.setColor(255, 255, 255, 255);
                            renderer.renderFaceZNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        }
                    } else if (dz == 0) {
                        if (dx == -1) {
                            renderer.renderFaceXPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassT);
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 0.75f, 1.0f, 1.0f);
                            renderer.setColor(255, 255, 255, 255);
                            renderer.renderFaceXPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        } else if (dx == 1) {
                            renderer.renderFaceXNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassT);
                            renderer.setRenderBounds(0.25f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                            renderer.setColor(255, 255, 255, 255);
                            renderer.renderFaceXNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        }
                    }
                } else if (dy == 2) {
                    if (dx == -1 && dz == -1) {
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        renderer.renderFaceXPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassBL);
                        renderer.renderFaceZPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassBR);
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 0.75f, 1.0f, 0.75f);
                        renderer.setColor(255, 255, 255, 255);
                        renderer.renderFaceXPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.renderFaceZPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    } else if (dx == 1 && dz == -1) {
                        renderer.renderFaceXNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassBR);
                        renderer.renderFaceZPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassBL);
                        renderer.setRenderBounds(0.25f, 0.0f, 0.0f, 1.0f, 1.0f, 0.75f);
                        renderer.setColor(255, 255, 255, 255);
                        renderer.renderFaceXNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.renderFaceZPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    } else if (dx == 1 && dz == 1) {
                        renderer.renderFaceXNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassBL);
                        renderer.renderFaceZNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassBR);
                        renderer.setRenderBounds(0.25f, 0.0f, 0.25f, 1.0f, 1.0f, 1.0f);
                        renderer.setColor(255, 255, 255, 255);
                        renderer.renderFaceXNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.renderFaceZNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    } else if (dx == -1 && dz == 1) {
                        renderer.renderFaceXPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassBR);
                        renderer.renderFaceZNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassBL);
                        renderer.setRenderBounds(0.0f, 0.0f, 0.25f, 0.75f, 1.0f, 1.0f);
                        renderer.setColor(255, 255, 255, 255);
                        renderer.renderFaceXPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.renderFaceZNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                        renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    } else if (dx == 0) {
                        if (dz == -1) {
                            renderer.renderFaceZPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassB);
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.75f);
                            renderer.setColor(255, 255, 255, 255);
                            renderer.renderFaceZPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        } else if (dz == 1) {
                            renderer.renderFaceZNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassB);
                            renderer.setRenderBounds(0.0f, 0.0f, 0.25f, 1.0f, 1.0f, 1.0f);
                            renderer.setColor(255, 255, 255, 255);
                            renderer.renderFaceZNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        }
                    } else if (dz == 0) {
                        if (dx == -1) {
                            renderer.renderFaceXPos(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassB);
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 0.75f, 1.0f, 1.0f);
                            renderer.setColor(255, 255, 255, 255);
                            renderer.renderFaceXPos(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        } else if (dx == 1) {
                            renderer.renderFaceXNeg(block, x, y, z, ((BlockVat) ThaumicHorizons.blockVat).iconGlassB);
                            renderer.setRenderBounds(0.25f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                            renderer.setColor(255, 255, 255, 255);
                            renderer.renderFaceXNeg(block, x, y, z, net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still"));
                            renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        }
                    }
                }
                return renderer.enableAO = true;
            }
        }
        renderer.enableAO = true;
        return false;
    }

    public boolean shouldRender3DInInventory(final int modelId) {
        return false;
    }

    public int getRenderId() {
        return ThaumicHorizons.blockVatRI;
    }
}
