package com.kentington.thaumichorizons.client.renderer.item;

import java.util.*;
import javax.vecmath.Matrix4f;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

public final class NativeItemRenderRegistration {
    private static final NativeItemRenderRegistration INSTANCE=new NativeItemRenderRegistration();
    private final Map<Item,TileEntityItemStackRenderer> renderers=new IdentityHashMap<>();
    private boolean registered;
    private NativeItemRenderRegistration() {}
    public static boolean hasRenderer(Item item) { return INSTANCE.renderers.containsKey(item); }
    public static void registerItemRenderer(Item item, TileEntityItemStackRenderer renderer) {
        item.setTileEntityItemStackRenderer(renderer);
        INSTANCE.renderers.put(item,renderer);
        if(!INSTANCE.registered) { MinecraftForge.EVENT_BUS.register(INSTANCE); INSTANCE.registered=true; }
    }
    @SubscribeEvent public void bake(ModelBakeEvent event) {
        for(Map.Entry<Item,TileEntityItemStackRenderer> entry:renderers.entrySet()) {
            ResourceLocation itemName=entry.getKey().getRegistryName();
            for(ModelResourceLocation location:new ArrayList<>(event.getModelRegistry().getKeys())) {
                if(itemName==null||!location.getNamespace().equals(itemName.getNamespace())
                        || !(location.getPath().equals(itemName.getPath()) || location.getPath().equals("legacy/item/"+itemName.getPath()))) continue;
                IBakedModel original=event.getModelRegistry().getObject(location);
                event.getModelRegistry().putObject(location,new AnimatedModel(original,entry.getValue()));
            }
        }
    }
    private static final class AnimatedModel implements IBakedModel {
        private final IBakedModel original;
        private final TileEntityItemStackRenderer renderer;
        private net.minecraft.item.ItemStack stack;
        AnimatedModel(IBakedModel original,TileEntityItemStackRenderer renderer) { this.original=original; this.renderer=renderer; }
        public List<BakedQuad> getQuads(IBlockState state,EnumFacing side,long seed) { return original.getQuads(state,side,seed); }
        public boolean isAmbientOcclusion() { return false; }
        public boolean isGui3d() { return true; }
        public boolean isBuiltInRenderer() { return true; }
        public TextureAtlasSprite getParticleTexture() { return original.getParticleTexture(); }
        public ItemCameraTransforms getItemCameraTransforms() { return original.getItemCameraTransforms(); }
        public ItemOverrideList getOverrides() {
            return new ItemOverrideList(Collections.emptyList()) {
                @Override public IBakedModel handleItemState(IBakedModel model, net.minecraft.item.ItemStack item,
                        net.minecraft.world.World world, net.minecraft.entity.EntityLivingBase entity) {
                    AnimatedModel result=new AnimatedModel(original.getOverrides().handleItemState(original,item,world,entity),renderer);
                    result.stack=item;
                    return result;
                }
            };
        }
        public Pair<? extends IBakedModel,Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
            if(!(renderer instanceof NativeItemRenderer)) {
                if(renderer instanceof thaumcraft.client.renderers.item.ItemWandRenderer)
                    thaumcraft.client.renderers.item.ItemWandRenderer.setTransformType(type);
                return Pair.of(this,original.handlePerspective(type).getRight());
            }
            ((NativeItemRenderer)renderer).setPerspective(type);
            if (stack!=null && !((NativeItemRenderer)renderer).handlesPerspective(stack))
                return original.handlePerspective(type);
            Matrix4f identity=new Matrix4f(); identity.setIdentity();
            return Pair.of(this,identity);
        }
    }
}
