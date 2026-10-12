//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.config.ConfigBlocks;

public class BiomeGenPocket extends Biome {

    public BiomeGenPocket(final int inty) {
        super(new Biome.BiomeProperties("Pocket Plane").setRainDisabled());
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.topBlock = ConfigBlocks.blockEldritchNothing.getDefaultState();
        this.fillerBlock = ConfigBlocks.blockEldritchNothing.getDefaultState();
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(final float p_76731_1_) {
        return 0;
    }

    public void decorate(final World world, final Random random, final net.minecraft.util.math.BlockPos pos) {}

    public Biome createMutation() {
        return null;
    }
}
