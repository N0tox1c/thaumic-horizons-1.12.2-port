//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.items.ItemFocusAnimation;

public class EntityLightningBoltFinite extends EntityLightningBolt {

    public int boltLength;
    public boolean animate;

    public EntityLightningBoltFinite(final World p_i1703_1_, final double p_i1703_2_, final double p_i1703_4_,
            final double p_i1703_6_, final int boltLength, final boolean animate) {
        super(p_i1703_1_, p_i1703_2_, p_i1703_4_, p_i1703_6_, false);
        this.boltLength = boltLength;
        this.animate = animate;
    }

    public EntityLightningBoltFinite(final World w) {
        super(w, 0.0, 0.0, 0.0, false);
    }

    protected void readEntityFromNBT(final NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.boltLength = tag.getInteger("length");
        this.animate = tag.getBoolean("animate");
    }

    protected void writeEntityToNBT(final NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setBoolean("animate", this.animate);
        tag.setInteger("length", this.boltLength);
    }

    public void onUpdate() {
        super.onUpdate();
        ThaumicHorizons.proxy.lightningBolt(this.world, this.posX, this.posY, this.posZ, this.boltLength);
        if (!this.animate) {
            return;
        }
        final int p_77648_4_ = (int) Math.floor(this.posX);
        final int p_77648_5_ = (int) this.posY;
        final int p_77648_6_ = (int) Math.floor(this.posZ);
        final BlockPos target = new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_);
        final net.minecraft.block.state.IBlockState state = this.world.getBlockState(target);
        final Block blocky = this.world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)).getBlock();
        final int md = this.world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)).getBlock().getMetaFromState(this.world.getBlockState(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_)));
        if (this.world.isRemote) {
            return;
        }
        if (!blocky.hasTileEntity(state) && !blocky.isAir(state, this.world, target)
                && (state.isOpaqueCube() || ItemFocusAnimation.isWhitelisted(blocky, md))
                && state.getBlockHardness(this.world, target) != -1.0f) {
            if (!this.world.isRemote
                    && ThaumicHorizons.blockCloud.getDefaultState().getBlockHardness(this.world, target)
                            > 0.0f) {
                final EntityGolemTH golem = new EntityGolemTH(this.world);
                golem.loadGolem(p_77648_4_ + 0.5, p_77648_5_, p_77648_6_ + 0.5, blocky, md, 1200, false, false, false);
                this.world.setBlockToAir(new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_));
                com.kentington.thaumichorizons.common.lib.THSoundBridge.play(this.world, p_77648_4_ + 0.5, p_77648_5_ + 0.5, p_77648_6_ + 0.5, "thaumcraft:wand", 1.0f, 1.0f);
                golem.setHomePosAndDistance(new net.minecraft.util.math.BlockPos((int) golem.posX, (int) golem.posY, (int) golem.posZ), 32);
                golem.setOwner("");
                golem.berserk = true;
                golem.extinguish();
                golem.heal(100.0f);
                this.world.spawnEntity(golem);
                this.world.setEntityState(golem, (byte) 7);
            } else {
                ((net.minecraft.world.WorldServer) this.world).spawnParticle(net.minecraft.util.EnumParticleTypes.BLOCK_CRACK,
                        target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5, 32, 0.4, 0.4, 0.4, 0.1, Block.getStateId(state));
            }
        }
    }
}
