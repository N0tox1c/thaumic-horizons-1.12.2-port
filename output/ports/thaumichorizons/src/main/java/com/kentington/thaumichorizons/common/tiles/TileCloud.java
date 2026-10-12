//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;
import net.minecraft.util.math.BlockPos;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.util.EnumFacing;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityItemInvulnerable;
import com.kentington.thaumichorizons.common.entities.EntityLightningBoltFinite;
import com.kentington.thaumichorizons.common.items.ItemFocusLiquefaction;
import com.kentington.thaumichorizons.common.lib.networking.PacketHandler;
import com.kentington.thaumichorizons.common.lib.networking.PacketRainState;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.monster.EntityFireBat;

public class TileCloud extends TileThaumcraft  implements net.minecraft.util.ITickable {

    public int md;
    int dropTimer;
    public static boolean raining;
    public int howManyDown;
    public Block cachedBlock;
    int cachedMD;
    static int[] dropTimers;
    boolean rainstate;
    int timer = 0;

    public TileCloud() {
        this.md = -1;
        this.dropTimer = -1;
        raining = false;
    }

    public boolean isRaining() {
        if (!world.isRemote) PacketHandler.INSTANCE.sendToDimension(
                new PacketRainState(DimensionManager.getWorld(0) != null && DimensionManager.getWorld(0).isRaining()),
                this.world.provider.getDimension());
        return TileCloud.raining;
    }

    public void update() {
        timer++;
        if (this.md == -1) {
            this.md = this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())).getBlock().getMetaFromState(this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())));
            this.dropTimer = TileCloud.dropTimers[this.md];
            this.findBlockBelow();
        }
        if (timer % 100 == 0) {
            if (rainstate != isRaining()) {
                rainstate = raining;
                this.markDirty();
                this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
            }
        }
        if (timer > 200) timer = 0;
        Label_4978: {
            if (this.rainstate) {
                if (this.world.getTotalWorldTime() % 10L == 0L) {
                    this.findBlockBelow();
                    switch (this.md) {
    case 1: case 4: {
                            final int meltable = ((ItemFocusLiquefaction) ThaumicHorizons.itemFocusLiquefaction)
                                    .isMeltableBlock(this.cachedBlock, this.cachedMD);
                            if (meltable == 1) {
                                this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()), Blocks.FIRE.getDefaultState(), 3);
                                ThaumicHorizons.proxy.smeltFX(
                                        this.pos.getX(),
                                        this.pos.getY() - this.howManyDown,
                                        this.pos.getZ(),
                                        this.world,
                                        25,
                                        false);
                                break;
                            }
                            if (meltable == 2) {
                                if (this.world.provider.getDimension() != -1) {
                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()), Blocks.WATER.getStateFromMeta(0), 3);
                                    break;
                                }
                                this.world.setBlockToAir(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()));
                            } else {
                                if (meltable == 3) {
                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()), Blocks.DIRT.getStateFromMeta(0), 3);
                                    break;
                                }
                                if (meltable == 4) {
                                    Blocks.TNT.onPlayerDestroy(this.world, new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()), net.minecraft.init.Blocks.TNT.getStateFromMeta(1));
                                    this.world.setBlockToAir(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()));
                                    break;
                                }
                                if (com.kentington.thaumichorizons.common.lib.BlockInteraction.isFlammable(this.cachedBlock, this.world, this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ(), null)) {
                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()), Blocks.FIRE.getDefaultState(), 3);
                                    ThaumicHorizons.proxy.smeltFX(
                                            this.pos.getX(),
                                            this.pos.getY() - this.howManyDown,
                                            this.pos.getZ(),
                                            this.world,
                                            25,
                                            false);
                                    break;
                                }
                            }
                        } break;
    case 3: {
                            final int meltable = ((ItemFocusLiquefaction) ThaumicHorizons.itemFocusLiquefaction)
                                    .isMeltableBlock(this.cachedBlock, this.cachedMD);
                            if (meltable == 1) {
                                this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()), Blocks.AIR.getDefaultState(), 3);
                                ThaumicHorizons.proxy.disintegrateExplodeFX(
                                        this.world,
                                        this.pos.getX() + 0.5,
                                        this.pos.getY() - this.howManyDown + 0.5,
                                        this.pos.getZ() + 0.5);
                                break;
                            }
                            if (meltable == 3) {
                                this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()), Blocks.DIRT.getStateFromMeta(0), 3);
                                break;
                            }
                            if (com.kentington.thaumichorizons.common.lib.BlockInteraction.isFlammable(this.cachedBlock, this.world, this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ(), null)) {
                                this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()), Blocks.AIR.getDefaultState(), 3);
                                ThaumicHorizons.proxy.disintegrateExplodeFX(
                                        this.world,
                                        this.pos.getX() + 0.5,
                                        this.pos.getY() - this.howManyDown + 0.5,
                                        this.pos.getZ() + 0.5);
                                break;
                            }
                        } break;
    default: {
                            if (this.world.getBiome(new BlockPos(this.pos.getX(), 0, this.pos.getZ()))
                                    .getTemperature(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())) >= 0.15) {
                                if (this.cachedBlock == Blocks.FARMLAND && this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ())).getBlock().getMetaFromState(this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ())))
                                        != 7) {
                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ(), 7, 3);
                                    break;
                                }
                                if (this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ())).getBlock()
                                        == Blocks.FIRE) {
                                    this.world.setBlockToAir(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()));
                                    break;
                                }
                                if (this.cachedBlock == Blocks.CAULDRON) {
                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ(), 3, 3);
                                    break;
                                }
                            } else {
                                if (this.cachedBlock.getStateFromMeta(this.cachedMD).isOpaqueCube() && this.world.isAirBlock(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()))) {
                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.SNOW_LAYER.getDefaultState(), 3);
                                    break;
                                }
                            }
                        } break;
}
                }
                switch (this.md) {
    case 1: case 4: {
                        final List<Entity> critters = this.getCrittersBelow();
                        for (final Entity ent : critters) {
                            ent.setFire(6);
                        }
                    } break;
    case 3: {
                        final List<Entity> critters = this.getCrittersBelow();
                        for (final Entity ent : critters) {
                            ent.attackEntityFrom(DamageSourceThaumcraft.dissolve, 1.0f);
                        }
                    } break;
    default: {
                        if (this.world.getBiome(new BlockPos(this.pos.getX(), 0, this.pos.getZ()))
                                .getTemperature(new net.minecraft.util.math.BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())) >= 0.15) {
                            final List<Entity> critters = this.getCrittersBelow();
                            for (final Entity ent : critters) {
                                if (ent instanceof EntityEnderman || ent instanceof EntityBlaze
                                        || ent instanceof EntityFireBat) {
                                    ent.attackEntityFrom(DamageSource.DROWN, 1.0f);
                                }
                                if (ent.isBurning()) {
                                    ent.extinguish();
                                }
                                if (!this.world.isRemote && this.md == 8
                                        && ent instanceof EntityCow
                                        && !(ent instanceof EntityMooshroom)) {
                                    ent.setDead();
                                    final EntityMooshroom entitycow = new EntityMooshroom(this.world);
                                    entitycow.setLocationAndAngles(
                                            ent.posX,
                                            ent.posY,
                                            ent.posZ,
                                            ent.rotationYaw,
                                            ent.rotationPitch);
                                    entitycow.setHealth(((EntityCow) ent).getHealth());
                                    entitycow.renderYawOffset = ((EntityCow) ent).renderYawOffset;
                                    this.world.spawnEntity(entitycow);
                                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_LARGE, ent.posX, ent.posY + ent.height / 2.0f, ent.posZ, 0.0, 0.0, 0.0);
                                }
                            }
                            break;
                        }
                    } break;
}
                if (this.dropTimer != -1) {
                    --this.dropTimer;
                    if (this.dropTimer == 0 && !this.world.isRemote) {
                        this.dropTimer = TileCloud.dropTimers[this.md] / 2
                                + this.world.rand.nextInt(TileCloud.dropTimers[this.md] / 2);
                        switch (this.md) {
    case 2: {
                                this.world.spawnEntity(
                                        new EntityLightningBoltFinite(
                                                this.world,
                                                this.pos.getX() + 0.5,
                                                this.pos.getY() - this.howManyDown,
                                                this.pos.getZ() + 0.5,
                                                this.howManyDown,
                                                false));
                            } break;
    case 4: {
                                final int type = this.world.rand.nextInt(75);
                                if (type < 6) {
                                    this.entityDropItem(new ItemStack(Items.GOLD_NUGGET), 0.3f);
                                    break;
                                }
                                if (type < 12) {
                                    if (Config.foundSilverIngot) {
                                        this.entityDropItem(new ItemStack(ConfigItems.itemNugget, 1, 3), 0.3f);
                                        break;
                                    }
                                    this.entityDropItem(new ItemStack(ConfigItems.itemNugget, 1, 0), 0.3f);
                                } else if (type < 20) {
                                    if (Config.foundCopperIngot) {
                                        this.entityDropItem(new ItemStack(ConfigItems.itemNugget, 1, 1), 0.3f);
                                        break;
                                    }
                                    this.entityDropItem(new ItemStack(ConfigItems.itemNugget, 1, 0), 0.3f);
                                } else if (type < 30) {
                                    if (Config.foundTinIngot) {
                                        this.entityDropItem(new ItemStack(ConfigItems.itemNugget, 1, 2), 0.3f);
                                        break;
                                    }
                                    this.entityDropItem(new ItemStack(ConfigItems.itemNugget, 1, 0), 0.3f);
                                } else if (type < 40) {
                                    if (Config.foundLeadIngot) {
                                        this.entityDropItem(new ItemStack(ConfigItems.itemNugget, 1, 4), 0.3f);
                                        break;
                                    }
                                    this.entityDropItem(new ItemStack(ConfigItems.itemNugget, 1, 0), 0.3f);
                                } else {
                                    if (type < 50) {
                                        this.entityDropItem(new ItemStack(ConfigItems.itemNugget, 1, 5), 0.3f);
                                        break;
                                    }
                                    this.entityDropItem(new ItemStack(ConfigItems.itemNugget, 1, 0), 0.3f);
                                }
                            } break;
    case 5: {
                                switch (this.world.rand.nextInt(10)) {
    case 0: {
                                        this.entityDropItem(new ItemStack(Items.BEEF), 0.3f);
                                        break Label_4978;
                                    }
    case 1: {
                                        this.entityDropItem(new ItemStack(Items.PORKCHOP), 0.3f);
                                        break Label_4978;
                                    }
    case 2: {
                                        this.entityDropItem(new ItemStack(Items.CHICKEN), 0.3f);
                                        break Label_4978;
                                    }
    case 3: {
                                        switch (this.world.rand.nextInt(3)) {
    case 0: {
                                                this.entityDropItem(new ItemStack(Items.FISH), 0.3f);
                                            } break;
    case 1: {
                                                this.entityDropItem(new ItemStack(Items.FISH, 1, 1), 0.3f);
                                            } break;
    case 2: {
                                                this.entityDropItem(new ItemStack(Items.FISH, 1, 2), 0.3f);
                                            } break;
}
                                        break Label_4978;
                                    }
    default: {
                                        this.entityDropItem(new ItemStack(ThaumicHorizons.itemMeat), 0.3f);
                                        break Label_4978;
                                    }
}
                            }
    case 6: {
                                Aspect asp = null;
                                switch (this.world.rand.nextInt(6)) {
    case 1: {
                                        asp = Aspect.FIRE;
                                    } break;
    case 2: {
                                        asp = Aspect.ORDER;
                                    } break;
    case 3: {
                                        asp = Aspect.ENTROPY;
                                    } break;
    case 4: {
                                        asp = Aspect.AIR;
                                    } break;
    case 5: {
                                        asp = Aspect.EARTH;
                                    } break;
    default: {
                                        asp = Aspect.WATER;
                                    } break;
}
                                final EntityAspectOrb orb = new EntityAspectOrb(
                                        this.world,
                                        this.pos.getX() + this.world.rand.nextDouble(),
                                        this.pos.getY() + 0.5,
                                        this.pos.getZ() + this.world.rand.nextDouble(),
                                        asp,
                                        1);
                                this.world.spawnEntity(orb);
                            } break;
    case 7: {
                                final EntityXPOrb xporb = new EntityXPOrb(
                                        this.world,
                                        this.pos.getX() + this.world.rand.nextDouble(),
                                        this.pos.getY() + 0.5,
                                        this.pos.getZ() + this.world.rand.nextDouble(),
                                        this.world.rand.nextInt(4));
                                this.world.spawnEntity(xporb);
                            } break;
    case 8: {
                                this.findBlockBelow();
                                if (!this.world.isAirBlock(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()))) {
                                    break;
                                }
                                if (this.cachedBlock == Blocks.FARMLAND) {
                                    switch (this.world.rand.nextInt(8)) {
                                        case 1: {
                                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.MELON_STEM.getDefaultState(), 3);
                                            break Label_4978;
                                        }
                                        case 2: {
                                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.PUMPKIN_STEM.getDefaultState(), 3);
                                            break Label_4978;
                                        }
                                        case 3: {
                                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.CARROTS.getDefaultState(), 3);
                                        }
                                        case 4: {
                                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.POTATOES.getDefaultState(), 3);
                                            break;
                                        }
                                    }
                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.WHEAT.getDefaultState(), 3);
                                    break;
                                }
                                if (this.cachedBlock == Blocks.DIRT) {
                                    if (this.world.rand.nextInt(10) == 4) {
                                        this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()), Blocks.MYCELIUM.getDefaultState(), 3);
                                        break Label_4978;
                                    }
                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown, this.pos.getZ()), Blocks.GRASS.getDefaultState(), 3);
                                    break Label_4978;
                                } else if (this.cachedBlock == Blocks.STONE || this.cachedBlock == Blocks.MYCELIUM) {
                                    if (this.world.rand.nextInt(3) == 1) {
                                        this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.BROWN_MUSHROOM.getDefaultState(), 3);
                                        break Label_4978;
                                    }
                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.RED_MUSHROOM.getDefaultState(), 3);
                                    break Label_4978;
                                } else {
                                    if (this.cachedBlock == Blocks.GRASS) {
                                        final int plant = this.world.rand.nextInt(1000);
                                        if (plant == 666) {
                                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), ConfigBlocks.blockCustomPlant.getDefaultState(), 3);
                                            com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 1, 3);
                                        } else if (plant < 750) {
                                            switch (this.world.rand.nextInt(14)) {
    case 0: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.TALLGRASS.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 1, 3);
                                                } break;
    case 1: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.TALLGRASS.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 2, 3);
                                                } break;
    case 2: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.YELLOW_FLOWER.getDefaultState(), 3);
                                                } break;
    case 3: case 4: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.RED_FLOWER.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 0, 3);
                                                } break;
    case 5: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.RED_FLOWER.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 1, 3);
                                                } break;
    case 6: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.RED_FLOWER.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 2, 3);
                                                } break;
    case 7: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.RED_FLOWER.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 3, 3);
                                                } break;
    case 8: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.RED_FLOWER.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 4, 3);
                                                } break;
    case 9: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.RED_FLOWER.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 5, 3);
                                                } break;
    case 10: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.RED_FLOWER.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 6, 3);
                                                } break;
    case 11: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.RED_FLOWER.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 7, 3);
                                                } break;
    case 12: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.RED_FLOWER.getDefaultState(), 3);
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 8, 3);
                                                } break;
    case 13: {
                                                    this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.REEDS.getDefaultState(), 3);
                                                } break;
}
                                        } else if (plant < 950) {
                                            final int sapling = this.world.rand.nextInt(100);
                                            if (sapling < 10) {
                                                this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), ConfigBlocks.blockCustomPlant.getDefaultState(), 3);
                                                com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 0, 3);
                                            } else {
                                                this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.SAPLING.getDefaultState(), 3);
                                                if (sapling < 25) {
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 0, 3);
                                                } else if (sapling < 40) {
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 1, 3);
                                                } else if (sapling < 55) {
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 2, 3);
                                                } else if (sapling < 70) {
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 3, 3);
                                                } else if (sapling < 85) {
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 4, 3);
                                                } else {
                                                    com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 5, 3);
                                                }
                                            }
                                        } else if (plant < 975) {
                                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), ConfigBlocks.blockCustomPlant.getDefaultState(), 3);
                                            com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 2, 3);
                                        } else {
                                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), ConfigBlocks.blockCustomPlant.getDefaultState(), 3);
                                            com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 5, 3);
                                        }
                                        break;
                                    }
                                    if (this.cachedBlock == Blocks.SAND) {
                                        if (this.world.rand.nextInt(10) == 4) {
                                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), ConfigBlocks.blockCustomPlant.getDefaultState(), 3);
                                            com.kentington.thaumichorizons.common.lib.WorldAccess.setBlockMetadataWithNotify(this.world, this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ(), 3, 3);
                                            break Label_4978;
                                        }
                                        this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.CACTUS.getDefaultState(), 3);
                                        break Label_4978;
                                    } else {
                                        if (this.cachedBlock == Blocks.SOUL_SAND) {
                                            this.world.setBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - this.howManyDown + 1, this.pos.getZ()), Blocks.NETHER_WART.getDefaultState(), 3);
                                            break;
                                        }
                                    }
                                }
                            } break;
    case 9: {
                                this.world.spawnEntity(
                                        new EntityLightningBoltFinite(
                                                this.world,
                                                this.pos.getX() + 0.5,
                                                this.pos.getY() - this.howManyDown,
                                                this.pos.getZ() + 0.5,
                                                this.howManyDown,
                                                true));
                            } break;
}
                    }
                }
            }
        }
    }

    private void entityDropItem(final ItemStack itemStack, final float f) {
        final EntityItemInvulnerable theItem = new EntityItemInvulnerable(
                this.world,
                this.pos.getX() + this.world.rand.nextDouble(),
                this.pos.getY() + 0.5,
                this.pos.getZ() + this.world.rand.nextDouble(),
                itemStack);
        this.world.spawnEntity(theItem);
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setBoolean("raining", this.rainstate);
        nbttagcompound.setInteger("dropTimer", this.dropTimer);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.rainstate = nbttagcompound.getBoolean("raining");
        this.dropTimer = nbttagcompound.getInteger("dropTimer");
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos.getX() - 32, 0.0, this.pos.getZ() - 32, this.pos.getX() + 32, 256.0, this.pos.getZ() + 32);
    }

    public void findBlockBelow() {
        final RayTraceResult mop = this.world.rayTraceBlocks(
                new Vec3d(this.pos.getX() + 0.5, this.pos.getY() - 0.5, this.pos.getZ() + 0.5),
                new Vec3d(this.pos.getX() + 0.5, this.pos.getY() - 256, this.pos.getZ() + 0.5));
        if (mop != null) {
            this.howManyDown = this.pos.getY() - mop.getBlockPos().getY();
            this.cachedBlock = this.world.getBlockState(new BlockPos(this.pos.getX(), mop.getBlockPos().getY(), this.pos.getZ())).getBlock();
            this.cachedMD = this.world.getBlockState(new BlockPos(this.pos.getX(), mop.getBlockPos().getY(), this.pos.getZ())).getBlock().getMetaFromState(this.world.getBlockState(new BlockPos(this.pos.getX(), mop.getBlockPos().getY(), this.pos.getZ())));
        } else {
            this.howManyDown = 256;
            this.cachedBlock = Blocks.AIR;
        }
    }

    public List getCrittersBelow() {
        return this.world.getEntitiesWithinAABBExcludingEntity(
                null,
                new AxisAlignedBB(
                        this.pos.getX(),
                        this.pos.getY() - this.howManyDown,
                        this.pos.getZ(),
                        this.pos.getX() + 1,
                        this.pos.getY(),
                        this.pos.getZ() + 1));
    }

    static {
        TileCloud.dropTimers = new int[] { -1, -1, 120, -1, 480, 480, 80, 360, 280, 550 };
    }
}
