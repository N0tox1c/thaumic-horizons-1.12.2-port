package com.kentington.thaumichorizons.client;

import com.google.common.collect.ImmutableList;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.blocks.LegacyBlock;
import com.kentington.thaumichorizons.common.blocks.BlockAlchemite;
import com.kentington.thaumichorizons.common.lib.OriginalItemAppearance;
import com.kentington.thaumichorizons.client.renderer.block.NativeBlockMesh;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import java.util.*;
import java.util.function.Function;

/** Native models fed by the original sprite selectors. No JSON texture guesses. */
public final class NativeLegacyModels implements ICustomModelLoader {
    public void onResourceManagerReload(IResourceManager manager) {}
    public boolean accepts(ResourceLocation location) {
        return "thaumichorizons".equals(location.getNamespace()) && location.getPath().contains("legacy/");
    }
    public IModel loadModel(ResourceLocation location) {
        String path=location.getPath().substring(location.getPath().indexOf("legacy/")+7);
        boolean item=path.startsWith("item/");
        String name=path.substring(path.indexOf('/')+1);
        int metadata=0;
        if (!item && name.indexOf('/')>=0) {
            metadata=Integer.parseInt(name.substring(name.indexOf('/')+1));
            name=name.substring(0,name.indexOf('/'));
        }
        ResourceLocation id=new ResourceLocation("thaumichorizons",name);
        return new Definition(item?ForgeRegistries.ITEMS.getValue(id):ForgeRegistries.BLOCKS.getValue(id),metadata);
    }
    @SubscribeEvent public void register(ModelRegistryEvent event) {
        for(Block block:ForgeRegistries.BLOCKS) {
            if (!"thaumichorizons".equals(block.getRegistryName().getNamespace())) continue;
            if (block instanceof LegacyBlock || block instanceof BlockAlchemite) {
                ModelLoader.setCustomStateMapper(block, ignored->{
                    Map<IBlockState,ModelResourceLocation> states=new HashMap<>();
                    for(IBlockState state:block.getBlockState().getValidStates()) states.put(state,
                            new ModelResourceLocation(new ResourceLocation("thaumichorizons",
                                    "legacy/block/"+block.getRegistryName().getPath()+"/"+block.getMetaFromState(state)),"normal"));
                    return states;
                });
            }
        }
        for(Item item:ForgeRegistries.ITEMS) {
            if (!"thaumichorizons".equals(item.getRegistryName().getNamespace())) continue;
            if (!(item instanceof OriginalItemAppearance) && !(item instanceof ItemBlock)
                    && item!=ThaumicHorizons.itemWandCastingDisposable) continue;
            ModelResourceLocation model=new ModelResourceLocation(new ResourceLocation("thaumichorizons",
                    "legacy/item/"+item.getRegistryName().getPath()),"inventory");
            ModelLoader.registerItemVariants(item,model);
            ModelLoader.setCustomMeshDefinition(item,stack->model);
        }
    }
    private static class Definition implements IModel {
        final Object object;
        final int metadata;
        Definition(Object object,int metadata) { this.object=Objects.requireNonNull(object,"Unregistered legacy model");this.metadata=metadata; }
        public IBakedModel bake(IModelState state,VertexFormat format,Function<ResourceLocation,TextureAtlasSprite> textures) {
            try {
                IModel blockTemplate=ModelLoaderRegistry.getModel(new ResourceLocation("minecraft","block/block"));
                CubeModel.transforms=blockTemplate.bake(blockTemplate.getDefaultState(),format,textures).getItemCameraTransforms();
                state=ModelLoaderRegistry.getModel(new ResourceLocation("minecraft","item/generated")).getDefaultState();
            } catch(Exception error) { throw new IllegalStateException("Cannot load vanilla item transforms",error); }
            if (object instanceof Block) return new CubeModel((Block)object,((Block)object).getStateFromMeta(metadata),null);
            Item item=(Item)object;
            if (item instanceof ItemBlock) return new CubeModel(((ItemBlock)item).getBlock(),((ItemBlock)item).getBlock().getDefaultState(),item);
            if (item instanceof OriginalItemAppearance) return new IconModel((OriginalItemAppearance)item,state,format,textures);
            // Disposable wands use the actual TC4 wand base model and native TEISR.
            try { return ModelLoaderRegistry.getModel(new ResourceLocation("thaumcraft","item/wandcasting")).bake(state,format,textures); }
            catch(Exception error) { throw new IllegalStateException("Cannot load TC4 wand model",error); }
        }
    }
    private static class IconModel implements IBakedModel {
        final OriginalItemAppearance appearance;
        final IModelState state;
        final VertexFormat format;
        final Function<ResourceLocation,TextureAtlasSprite> textures;
        final Map<List<ResourceLocation>,IBakedModel> models=new HashMap<>();
        IconModel(OriginalItemAppearance appearance,IModelState state,VertexFormat format,Function<ResourceLocation,TextureAtlasSprite> textures) {
            this.appearance=appearance; this.state=state; this.format=format; this.textures=textures;
        }
        private IBakedModel model(ItemStack stack) {
            List<ResourceLocation> icons=new ArrayList<>();
            for(int pass=0;pass<appearance.getOriginalRenderPasses();pass++) {
                TextureAtlasSprite sprite=Objects.requireNonNull(appearance.getOriginalIcon(stack,pass),"Unstitched original item sprite");
                icons.add(new ResourceLocation(sprite.getIconName()));
            }
            return models.computeIfAbsent(icons,key->new ItemLayerModel(ImmutableList.copyOf(key)).bake(state,format,textures));
        }
        public List<BakedQuad> getQuads(IBlockState state,EnumFacing side,long seed) {
            return model(new ItemStack((Item)appearance)).getQuads(state,side,seed);
        }
        public boolean isAmbientOcclusion() { return false; }
        public boolean isGui3d() { return false; }
        public boolean isBuiltInRenderer() { return false; }
        public TextureAtlasSprite getParticleTexture() { return model(new ItemStack((Item)appearance)).getParticleTexture(); }
        public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
        public ItemOverrideList getOverrides() {
            return new ItemOverrideList(Collections.emptyList()) {
                @Override public IBakedModel handleItemState(IBakedModel original,ItemStack stack,World world,EntityLivingBase entity) { return model(stack); }
            };
        }
    }
    private static class CubeModel implements IBakedModel {
        static ItemCameraTransforms transforms=ItemCameraTransforms.DEFAULT;
        final Block block; final IBlockState fixed; final Item item;
        CubeModel(Block block,IBlockState fixed,Item item) { this.block=block;this.fixed=fixed;this.item=item; }
        private TextureAtlasSprite icon(EnumFacing side,int meta) {
            if (block instanceof LegacyBlock) return ((LegacyBlock)block).getIcon(side.getIndex(),meta);
            if (block instanceof BlockAlchemite) return ((BlockAlchemite)block).getIcon(side.getIndex(),meta);
            return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block.getStateFromMeta(meta));
        }
        public List<BakedQuad> getQuads(IBlockState state,EnumFacing side,long seed) {
            if (side==null) return Collections.emptyList();
            IBlockState actual=state==null?fixed:state;
            if (block.getRenderType(actual)==net.minecraft.util.EnumBlockRenderType.INVISIBLE) return Collections.emptyList();
            int meta=block.getMetaFromState(actual);
            NativeBlockMesh mesh=new NativeBlockMesh();
            TextureAtlasSprite sprite=icon(side,meta);
            if (block instanceof LegacyBlock && actual instanceof net.minecraftforge.common.property.IExtendedBlockState) {
                net.minecraftforge.common.property.IExtendedBlockState extended=(net.minecraftforge.common.property.IExtendedBlockState)actual;
                if (extended.getUnlistedNames().contains(com.kentington.thaumichorizons.common.lib.BlockRenderContext.WORLD)) {
                    net.minecraft.world.IBlockAccess world=extended.getValue(com.kentington.thaumichorizons.common.lib.BlockRenderContext.WORLD);
                    net.minecraft.util.math.BlockPos pos=extended.getValue(com.kentington.thaumichorizons.common.lib.BlockRenderContext.POSITION);
                    if (world!=null && pos!=null) {
                        sprite=((LegacyBlock)block).getIcon(world,pos.getX(),pos.getY(),pos.getZ(),side.getIndex());
                        AxisAlignedBB bounds=block.getBoundingBox(actual,world,pos);
                        mesh.setRenderBounds(bounds.minX,bounds.minY,bounds.minZ,bounds.maxX,bounds.maxY,bounds.maxZ);
                    }
                }
            }
            Objects.requireNonNull(sprite,"Unstitched cube face for "+block.getRegistryName());
            switch(side) {
                case DOWN:mesh.renderFaceYNeg(block,0,0,0,sprite);break;
                case UP:mesh.renderFaceYPos(block,0,0,0,sprite);break;
                case NORTH:mesh.renderFaceZNeg(block,0,0,0,sprite);break;
                case SOUTH:mesh.renderFaceZPos(block,0,0,0,sprite);break;
                case WEST:mesh.renderFaceXNeg(block,0,0,0,sprite);break;
                case EAST:mesh.renderFaceXPos(block,0,0,0,sprite);break;
            }
            return mesh.quads();
        }
        public boolean isAmbientOcclusion() { return true; }
        public boolean isGui3d() { return true; }
        public boolean isBuiltInRenderer() { return false; }
        public TextureAtlasSprite getParticleTexture() {
            TextureAtlasSprite sprite=icon(EnumFacing.NORTH,block.getMetaFromState(fixed));
            return sprite==null?Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite():sprite;
        }
        public ItemCameraTransforms getItemCameraTransforms() { return transforms; }
        public ItemOverrideList getOverrides() {
            if (item==null) return ItemOverrideList.NONE;
            return new ItemOverrideList(Collections.emptyList()) {
                @Override public IBakedModel handleItemState(IBakedModel original,ItemStack stack,World world,EntityLivingBase entity) {
                    return new CubeModel(block,block.getStateFromMeta(stack.getMetadata()),item);
                }
            };
        }
    }
}
