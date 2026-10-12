package com.kentington.thaumichorizons.client.renderer.block;

import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.kentington.thaumichorizons.common.lib.BlockRenderContext;
import com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderRegistration;

public final class NativeBlockRenderRegistration {
    private static final NativeBlockRenderRegistration INSTANCE=new NativeBlockRenderRegistration();
    private final Map<Block,NativeBlockRenderer> renderers=new IdentityHashMap<>();
    private boolean registered;
    private static int nextId=1;
    private NativeBlockRenderRegistration() {}
    public static int getNextAvailableRenderId() { return nextId++; }
    public static void registerBlockHandler(Block block,NativeBlockRenderer renderer) {
        INSTANCE.renderers.put(block,renderer);
        if(!INSTANCE.registered) { MinecraftForge.EVENT_BUS.register(INSTANCE); INSTANCE.registered=true; }
    }
    @SubscribeEvent public void register(net.minecraftforge.client.event.ModelRegistryEvent event) {
        for (Map.Entry<Block,NativeBlockRenderer> entry:renderers.entrySet()) {
            Item item=Item.getItemFromBlock(entry.getKey());
            if (item==net.minecraft.init.Items.AIR) throw new IllegalStateException("Missing ItemBlock for "+entry.getKey().getRegistryName());
            if (!NativeItemRenderRegistration.hasRenderer(item)) NativeItemRenderRegistration.registerItemRenderer(item,entry.getValue());
        }
    }
    @SubscribeEvent public void bake(ModelBakeEvent event) {
        for(Map.Entry<Block,NativeBlockRenderer> entry:renderers.entrySet()) {
            ResourceLocation name=entry.getKey().getRegistryName();
            for(ModelResourceLocation location:new ArrayList<>(event.getModelRegistry().getKeys())) {
                if(name==null||location.getVariant().equals("inventory")||!location.getNamespace().equals(name.getNamespace())
                        || !(location.getPath().equals(name.getPath()) || location.getPath().startsWith("legacy/block/"+name.getPath()+"/"))) continue;
                IBakedModel original=event.getModelRegistry().getObject(location);
                event.getModelRegistry().putObject(location,new WorldModel(original,entry.getKey(),entry.getValue()));
            }
        }
    }
    private static final class WorldModel implements IBakedModel {
        private final IBakedModel original; private final Block block; private final NativeBlockRenderer renderer;
        WorldModel(IBakedModel original,Block block,NativeBlockRenderer renderer) { this.original=original; this.block=block; this.renderer=renderer; }
        public List<BakedQuad> getQuads(IBlockState state,EnumFacing side,long seed) {
            if(side!=null||!(state instanceof IExtendedBlockState)) return Collections.emptyList();
            IExtendedBlockState extended=(IExtendedBlockState)state;
            if(!extended.getUnlistedNames().contains(BlockRenderContext.WORLD)) return Collections.emptyList();
            IBlockAccess world=extended.getValue(BlockRenderContext.WORLD); BlockPos pos=extended.getValue(BlockRenderContext.POSITION);
            if(world==null||pos==null) return Collections.emptyList();
            NativeBlockMesh mesh=new NativeBlockMesh();
            renderer.renderWorldBlock(world,pos.getX(),pos.getY(),pos.getZ(),block,renderer.getRenderId(),mesh);
            return mesh.quads();
        }
        public boolean isAmbientOcclusion() { return false; }
        public boolean isGui3d() { return true; }
        public boolean isBuiltInRenderer() { return false; }
        public TextureAtlasSprite getParticleTexture() { return original.getParticleTexture(); }
        public ItemCameraTransforms getItemCameraTransforms() { return original.getItemCameraTransforms(); }
        public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }
    }
}
