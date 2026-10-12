package com.kentington.thaumichorizons.client.lib;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class BlockSprites {
    private BlockSprites() {}

    public static TextureAtlasSprite face(IBlockState state, EnumFacing face) {
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        List<BakedQuad> quads = model.getQuads(state, face, 0L);
        if (quads.isEmpty()) {
            for (BakedQuad quad : model.getQuads(state, null, 0L)) {
                if (quad.getFace() == face) return quad.getSprite();
            }
            return model.getParticleTexture();
        }
        return quads.get(0).getSprite();
    }
}
