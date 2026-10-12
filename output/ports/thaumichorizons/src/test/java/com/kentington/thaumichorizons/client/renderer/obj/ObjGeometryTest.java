package com.kentington.thaumichorizons.client.renderer.obj;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjGeometryTest {
    @Test public void triangulatesQuadAndResolvesNegativeIndices() throws Exception {
        ObjGeometry geometry=ObjGeometry.read(new StringReader("v 0 0 0\nv 1 0 0\nv 1 1 0\nv 0 1 0\ng body\nf -4 -3 -2 -1\n"),"quad");
        assertEquals(2,geometry.triangles.size());
        assertEquals(1,geometry.triangles.get(1).vertices[1].x,0);
        assertEquals(1,geometry.triangles.get(1).vertices[2].y,0);
        assertEquals(1,geometry.triangles.get(0).vertices[0].nz,0);
        assertTrue(geometry.triangles.get(1).belongsTo("BODY"));
    }
    @Test public void preservesUvsAndSuppliedNormals() throws Exception {
        ObjGeometry geometry=ObjGeometry.read(new StringReader("v 0 0 0\nv 1 0 0\nv 0 1 0\nvt .2 .3\nvn 0 1 0\nf 1/1/1 2/1/1 3/1/1\n"),"uv");
        ObjGeometry.Vertex vertex=geometry.triangles.get(0).vertices[0];
        assertEquals(.2,vertex.u,1e-7); assertEquals(.7,vertex.v,1e-7); assertEquals(1,vertex.ny,0);
    }
    @Test public void reportsMalformedIndexWithLineAndResource() throws Exception {
        try { ObjGeometry.read(new StringReader("v 0 0 0\nf 1 2 3\n"),"broken.obj"); fail(); }
        catch(IOException e) { assertTrue(e.getMessage().contains("broken.obj:2:")); }
    }
    @Test public void parsesAllOriginalModelsAndAnimatedParts() throws Exception {
        String roots=System.getProperty("obj.resources");
        if(roots==null) roots="src/main/resources";
        int count=0;
        for(String root:roots.split(File.pathSeparator)) {
            try(Stream<Path> paths=Files.walk(Paths.get(root))) {
                for(Path path:(Iterable<Path>)paths.filter(p->p.toString().endsWith(".obj"))::iterator) {
                    ObjGeometry geometry;
                    try(Reader reader=Files.newBufferedReader(path,StandardCharsets.UTF_8)) { geometry=ObjGeometry.read(reader,path.toString()); }
                    assertFalse(path.toString(),geometry.triangles.isEmpty()); count++;
                    if(path.getFileName().toString().equals("node_stabilizer.obj")) {
                        assertTrue(geometry.triangles.stream().anyMatch(t->t.belongsTo("lock")));
                        assertTrue(geometry.triangles.stream().anyMatch(t->t.belongsTo("piston")));
                    }
                    for(ObjGeometry.Triangle triangle:geometry.triangles)
                        for(ObjGeometry.Vertex vertex:triangle.vertices) {
                            assertTrue(Double.isFinite(vertex.x)); assertTrue(Float.isFinite(vertex.nx));
                        }
                }
            }
        }
        assertTrue("No original model assets tested",count>=2);
        System.out.println("Original OBJ models verified: "+count);
    }
}
