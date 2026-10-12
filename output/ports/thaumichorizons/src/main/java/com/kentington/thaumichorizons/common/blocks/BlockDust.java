//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.world.IBlockAccess;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

public class BlockDust extends LegacyBlock {

    public BlockDust() {
        super(Material.SAND);
        this.setHardness(0.5f);
        this.setStepSound(net.minecraft.block.SoundType.SAND);
        this.setTranslationKey("ThaumicHorizons_dust");
        this.setBlockTextureName("ThaumicHorizons:dust");
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World p_149668_1_, final int p_149668_2_,
            final int p_149668_3_, final int p_149668_4_) {
        final float f = 0.125f;
        return new AxisAlignedBB(
                p_149668_2_,
                p_149668_3_,
                p_149668_4_,
                p_149668_2_ + 1,
                p_149668_3_ + 1 - f,
                p_149668_4_ + 1);
    }

    public void onEntityCollidedWithBlock(final World p_149670_1_, final int p_149670_2_, final int p_149670_3_,
            final int p_149670_4_, final Entity p_149670_5_) {
        p_149670_5_.motionX *= 0.4;
        p_149670_5_.motionZ *= 0.4;
    }

    @Override
    public void onEntityCollision(World world,net.minecraft.util.math.BlockPos pos,net.minecraft.block.state.IBlockState state,Entity entity) {
        onEntityCollidedWithBlock(world,pos.getX(), pos.getY(), pos.getZ(),entity);
    }

    @Override
    public net.minecraft.util.math.AxisAlignedBB getCollisionBoundingBox(net.minecraft.block.state.IBlockState state,IBlockAccess world,net.minecraft.util.math.BlockPos pos) {
        if (!(world instanceof World)) return getBoundingBox(state,world,pos);
        net.minecraft.util.math.AxisAlignedBB box=getCollisionBoundingBoxFromPool((World)world,pos.getX(), pos.getY(), pos.getZ());
        return box==null ? null : box.offset(-pos.getX(),-pos.getY(),-pos.getZ());
    }
}
