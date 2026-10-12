package com.kentington.thaumichorizons.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderer;

public abstract class NativeBlockRenderer extends NativeItemRenderer {
    public abstract void renderInventoryBlock(Block block,int metadata,int id,NativeBlockMesh mesh);
    public abstract boolean renderWorldBlock(IBlockAccess world,int x,int y,int z,Block block,int id,NativeBlockMesh mesh);
    public abstract boolean shouldRender3DInInventory(int id);
    public abstract int getRenderId();
    public boolean handleRenderType(ItemStack item,ItemRenderType type) { return true; }
    public boolean shouldUseRenderHelper(ItemRenderType type,ItemStack item,ItemRendererHelper helper) { return false; }
    public void renderItem(ItemRenderType type,ItemStack stack,Object... data) {
        NativeBlockMesh mesh=new NativeBlockMesh();
        renderInventoryBlock(Block.getBlockFromItem(stack.getItem()),stack.getMetadata(),getRenderId(),mesh);
        mesh.drawInventory();
    }
}
