package com.kentington.thaumichorizons.common.lib;

import java.util.Collections;
import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

/** The original empty dimension. PocketPlaneData builds each artificial sphere
 * afterwards; native ChunkProviderServer owns loading, saving and unloading. */
public class ChunkProviderPocketPlane implements IChunkGenerator {
    private final World world;
    public ChunkProviderPocketPlane(World world, long seed) { this.world = world; }

    @Override public Chunk generateChunk(int x, int z) {
        Chunk chunk = new Chunk(world, new ChunkPrimer(), x, z);
        Biome[] biomes = world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
        byte[] stored = chunk.getBiomeArray();
        for (int i = 0; i < stored.length; i++) stored[i] = (byte) Biome.getIdForBiome(biomes[i]);
        chunk.generateSkylightMap();
        return chunk;
    }

    // No decoration, natural mobs or structures existed in the original void generator.
    @Override public void populate(int x, int z) {}
    @Override public boolean generateStructures(Chunk chunk, int x, int z) { return false; }
    @Override public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
        return Collections.emptyList();
    }
    @Override public BlockPos getNearestStructurePos(World world, String name, BlockPos pos, boolean unexplored) {
        return null;
    }
    @Override public void recreateStructures(Chunk chunk, int x, int z) {}
    @Override public boolean isInsideStructure(World world, String name, BlockPos pos) { return false; }
}
