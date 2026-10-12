package com.kentington.thaumichorizons.client.renderer.obj;

import java.io.*;
import java.util.*;

/** Geometry only; textures and transforms remain under the original renderer's control. */
public final class ObjGeometry {
    public static final class Vertex {
        public final double x, y, z, u, v;
        public final float nx, ny, nz;
        Vertex(double[] p, double[] t, double[] n) {
            x=p[0]; y=p[1]; z=p[2]; u=t[0]; v=1-t[1];
            nx=(float)n[0]; ny=(float)n[1]; nz=(float)n[2];
        }
    }
    public static final class Triangle {
        public final Vertex[] vertices;
        public final Set<String> groups;
        Triangle(Vertex a, Vertex b, Vertex c, Set<String> groups) {
            vertices=new Vertex[]{a,b,c};
            this.groups=Collections.unmodifiableSet(new HashSet<>(groups));
        }
        public boolean belongsTo(String group) { return groups.contains(group.toLowerCase(Locale.ROOT)); }
    }
    public final List<Triangle> triangles;
    private ObjGeometry(List<Triangle> triangles) { this.triangles=Collections.unmodifiableList(triangles); }
    private static double[] values(String[] tokens, int count) {
        double[] out=new double[count];
        for(int i=0;i<count && i+1<tokens.length;i++) out[i]=Double.parseDouble(tokens[i+1]);
        return out;
    }
    private static int index(String text, int size) {
        int i=Integer.parseInt(text); int resolved=i>0?i-1:size+i;
        if(i==0 || resolved<0 || resolved>=size) throw new IllegalArgumentException("Invalid OBJ index: "+text);
        return resolved;
    }
    public static ObjGeometry read(Reader source, String name) throws IOException {
        List<double[]> positions=new ArrayList<>(), textures=new ArrayList<>(), normals=new ArrayList<>();
        List<Triangle> triangles=new ArrayList<>();
        Set<String> groups=new HashSet<>(); groups.add("default");
        BufferedReader reader=new BufferedReader(source); String line; int number=0;
        while((line=reader.readLine())!=null) {
            number++; line=line.split("#",2)[0].trim(); if(line.isEmpty()) continue;
            String[] tokens=line.split("\\s+");
            try {
                switch(tokens[0]) {
                    case "v": positions.add(values(tokens,3)); break;
                    case "vt": textures.add(values(tokens,2)); break;
                    case "vn": normals.add(values(tokens,3)); break;
                    case "g": case "o":
                        groups=new HashSet<>();
                        for(int i=1;i<tokens.length;i++) groups.add(tokens[i].toLowerCase(Locale.ROOT));
                        break;
                    case "f":
                        if(tokens.length<4) throw new IllegalArgumentException("Face needs at least three vertices");
                        int[][] indices=new int[tokens.length-1][3];
                        for(int i=1;i<tokens.length;i++) {
                            String[] parts=tokens[i].split("/",-1);
                            indices[i-1][0]=index(parts[0],positions.size());
                            indices[i-1][1]=parts.length>1&&!parts[1].isEmpty()?index(parts[1],textures.size()):-1;
                            indices[i-1][2]=parts.length>2&&!parts[2].isEmpty()?index(parts[2],normals.size()):-1;
                        }
                        double[] a=positions.get(indices[0][0]), b=positions.get(indices[1][0]), c=positions.get(indices[2][0]);
                        double[] normal={(b[1]-a[1])*(c[2]-a[2])-(b[2]-a[2])*(c[1]-a[1]),
                            (b[2]-a[2])*(c[0]-a[0])-(b[0]-a[0])*(c[2]-a[2]),
                            (b[0]-a[0])*(c[1]-a[1])-(b[1]-a[1])*(c[0]-a[0])};
                        double length=Math.sqrt(normal[0]*normal[0]+normal[1]*normal[1]+normal[2]*normal[2]);
                        if(length>0) for(int i=0;i<3;i++) normal[i]/=length;
                        Vertex[] vertices=new Vertex[indices.length];
                        for(int i=0;i<indices.length;i++) vertices[i]=new Vertex(positions.get(indices[i][0]),
                            indices[i][1]<0?new double[]{0,1}:textures.get(indices[i][1]),
                            indices[i][2]<0?normal:normals.get(indices[i][2]));
                        for(int i=1;i+1<vertices.length;i++) triangles.add(new Triangle(vertices[0],vertices[i],vertices[i+1],groups));
                        break;
                    default: break; // Material selection belongs to the TESR's bound texture.
                }
            } catch(RuntimeException e) { throw new IOException(name+":"+number+": "+e.getMessage(),e); }
        }
        if(triangles.isEmpty()) throw new IOException(name+": no faces");
        return new ObjGeometry(triangles);
    }
}
