package com.kentington.thaumichorizons.common.entities.ai;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityTaintPig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.Utils;

/**
 * Taint-feeding behaviour, ported from the original Thaumic Horizons AI.
 * The search ranges, taint preference, healing and cooldowns are preserved.
 * Vanilla 1.12 path navigation uses BlockPos and tryMoveToXYZ.
 */
public class EntityAIEatTaint extends EntityAIBase {
    private final EntityTaintPig pig;
    private BlockPos target;
    private int cooldown;
    private int remainingTicks;

    public EntityAIEatTaint(EntityTaintPig pig) {
        this.pig = pig;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (pig.world.isRemote) return false;
        if (cooldown > 0) {
            --cooldown;
            return false;
        }
        target = findTaint();
        return target != null;
    }

    private boolean isTaintedBiome(BlockPos pos) {
        return Biome.getIdForBiome(pig.world.getBiome(pos)) == Config.biomeTaintID;
    }

    private boolean isEdible(BlockPos pos) {
        IBlockState state = pig.world.getBlockState(pos);
        return state.getMaterial() == Config.taintMaterial
            || (state.getBlock() == Blocks.GRASS && isTaintedBiome(pos));
    }

    private BlockPos findTaint() {
        BlockPos origin = pig.getPosition();
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos pos = origin.add(x, y, z);
                    if (isEdible(pos)) return pos;
                }
            }
        }
        for (int i = 0; i < 30; i++) {
            BlockPos pos = origin.add(pig.world.rand.nextInt(17) - 8,
                    pig.world.rand.nextInt(5) - 2,
                    pig.world.rand.nextInt(17) - 8);
            if (pig.world.isAirBlock(pos.up()) && isEdible(pos)) return pos;
        }
        return null;
    }

    @Override
    public void startExecuting() {
        remainingTicks = 120;
        if (target != null) {
            pig.getNavigator().tryMoveToXYZ(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, 0.6D);
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return target != null && remainingTicks-- > 0 && cooldown <= 0;
    }

    @Override
    public void resetTask() {
        target = null;
        remainingTicks = 0;
        pig.getNavigator().clearPath();
    }

    @Override
    public void updateTask() {
        if (target == null) return;
        pig.getLookHelper().setLookPosition(target.getX() + 0.5, target.getY() + 0.5,
                target.getZ() + 0.5, 30.0F, 30.0F);
        if (pig.getDistanceSq(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5) <= 4.0D) {
            eatTaint();
        }
    }

    private void eatTaint() {
        if (target == null || pig.world.isRemote) return;
        IBlockState state = pig.world.getBlockState(target);
        if (state.getMaterial() == Config.taintMaterial) {
            ThaumicHorizons.proxy.blockSplosionFX(target.getX(), target.getY(), target.getZ(),
                    state.getBlock(), state.getBlock().getMetaFromState(state));
            pig.world.setBlockToAir(target);
            finishEating(20);
        } else if (state.getBlock() == Blocks.GRASS && isTaintedBiome(target)) {
            pig.world.setBlockState(target, Blocks.DIRT.getDefaultState(), 3);
            finishEating(10);
        } else {
            resetTask();
        }
    }

    private void finishEating(int nextCooldown) {
        Utils.setBiomeAt(pig.world, target.getX(), target.getZ(), Biomes.PLAINS);
        pig.world.playSound(null, target, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.HOSTILE,
                0.2F, (pig.world.rand.nextFloat() - pig.world.rand.nextFloat()) * 0.7F + 2.0F);
        pig.heal(1.0F);
        cooldown = nextCooldown;
        resetTask();
    }
}
