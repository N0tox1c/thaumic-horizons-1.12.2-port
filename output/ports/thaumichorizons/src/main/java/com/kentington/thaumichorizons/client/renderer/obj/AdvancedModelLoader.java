package com.kentington.thaumichorizons.client.renderer.obj;

import java.io.*;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/** Draws original Wavefront meshes using the actual 1.12 BufferBuilder API. */
public final class AdvancedModelLoader {
    private AdvancedModelLoader() {}
    public static IModelCustom loadModel(ResourceLocation location) {
        final ObjGeometry geometry;
        try(InputStream stream=Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
            Reader reader=new InputStreamReader(stream,StandardCharsets.UTF_8)) {
            geometry=ObjGeometry.read(reader,location.toString());
        } catch(IOException e) { throw new IllegalStateException("Cannot load model "+location,e); }
        return new IModelCustom() {
            public void renderAll() { draw(null); }
            public void renderPart(String name) { draw(name); }
            private void draw(String name) {
                Tessellator tessellator=Tessellator.getInstance(); BufferBuilder buffer=tessellator.getBuffer();
                buffer.begin(GL11.GL_TRIANGLES,DefaultVertexFormats.POSITION_TEX_NORMAL);
                for(ObjGeometry.Triangle triangle:geometry.triangles) {
                    if(name!=null&&!triangle.belongsTo(name)) continue;
                    for(ObjGeometry.Vertex vertex:triangle.vertices)
                        buffer.pos(vertex.x,vertex.y,vertex.z).tex(vertex.u,vertex.v)
                            .normal(vertex.nx,vertex.ny,vertex.nz).endVertex();
                }
                tessellator.draw();
            }
        };
    }
}
