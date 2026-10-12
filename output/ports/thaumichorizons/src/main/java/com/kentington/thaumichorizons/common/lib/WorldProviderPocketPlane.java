//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderPocketPlane extends WorldProvider {

    @Override protected void init() {
        this.biomeProvider = new BiomeProviderSingle(net.minecraft.init.Biomes.HELL);
        this.hasSkyLight = false;
    }

    @Override public net.minecraft.world.gen.IChunkGenerator createChunkGenerator() {
        return new ChunkProviderPocketPlane(this.world, this.world.getSeed());
    }

    public int getAverageGroundLevel() {
        return 128;
    }

    public BlockPos getEntrancePortalLocation() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(final float p_76562_1_, final float p_76562_2_) {
        return new Vec3d(0.02734375, 0.01171875, 0.16015625);
    }

    protected void generateLightBrightnessTable() {
        final float f = 0.3f;
        for (int i = 0; i <= 15; ++i) {
            final float f2 = 1.0f - i / 15.0f;
            this.lightBrightnessTable[i] = (1.0f - f2) / (f2 * 3.0f + 1.0f) * (1.0f - f) + f;
        }
    }

    public boolean isSurfaceWorld() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(final float p_76560_1_, final float p_76560_2_) {
        return null;
    }

    public String getWelcomeMessage() {
        return "Entering pocket plane...";
    }

    public String getDepartMessage() {
        return "Leaving pocket plane...";
    }

    public boolean shouldMapSpin(final String entity, final double x, final double y, final double z) {
        return true;
    }

    public boolean canBlockFreeze(final BlockPos pos, final boolean byWater) {
        return false;
    }

    public boolean canSnowAt(final BlockPos pos, final boolean checkLight) {
        return false;
    }

    public boolean canDoLightning(final Chunk chunk) {
        return false;
    }

    public boolean canDoRainSnowIce(final Chunk chunk) {
        return false;
    }

    public boolean canCoordinateBeSpawn(final int p_76566_1_, final int p_76566_2_) {
        BlockPos surface = world.getTopSolidOrLiquidBlock(new BlockPos(p_76566_1_, 0, p_76566_2_));
        return world.getBlockState(surface.down()).getMaterial().blocksMovement();
    }

    public float calculateCelestialAngle(final long p_76563_1_, final float p_76563_3_) {
        return 0.5f;
    }

    public boolean canRespawnHere() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(final int p_76568_1_, final int p_76568_2_) {
        return false;
    }

    public String getDimensionName() {
        return "Pocket Plane";
    }
    @Override public net.minecraft.world.DimensionType getDimensionType() {
        return net.minecraftforge.common.DimensionManager.getProviderType(ThaumicHorizons.dimensionPocketId);
    }

}
