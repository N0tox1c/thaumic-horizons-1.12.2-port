//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import thaumcraft.common.lib.TCSounds;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.IWandTriggerManager;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.research.ResearchManager;

public class WandManagerTH implements IWandTriggerManager {

    @Override
    public boolean performTrigger(final World world, final ItemStack wand, final EntityPlayer player, final int x,
            final int y, final int z, final int side, final int event) {
        if (event == 0 && ResearchManager.isResearchComplete(player.getName(), "healingVat")) {
            return this.constructVat(world, wand, player, x, y, z, side);
        }
        return false;
    }

    boolean constructVat(final World world, final ItemStack itemstack, final EntityPlayer player, final int x,
            final int y, final int z, final int side) {
        if (itemstack == null || !(itemstack.getItem() instanceof ItemWandCasting)) return false;
        final ItemWandCasting wand = (ItemWandCasting) itemstack.getItem();
        for (int xx = x - 2; xx <= x; ++xx) {
            for (int yy = y - 3; yy <= y; ++yy) {
                int zz = z - 2;
                while (zz <= z) {
                    if (this.fitVat(world, xx, yy, zz) && (world.isRemote || wand.consumeAllVisCrafting(
                            itemstack,
                            player,
                            new AspectList().add(Aspect.WATER, 50).add(Aspect.EARTH, 50).add(Aspect.ORDER, 50),
                            true))) {
                        if (!world.isRemote) {
                            this.replaceVat(world, xx, yy, zz);
                            return true;
                        }
                        return true;
                    } else {
                        ++zz;
                    }
                }
            }
        }
        return false;
    }

    boolean fitVat(final World world, final int x, final int y, final int z) {
        if (!world.isAreaLoaded(new BlockPos(x, y, z), new BlockPos(x + 2, y + 3, z + 2))) return false;
        final Block g = Blocks.GLASS;
        final Block w = Blocks.WATER;
        final Block p = ConfigBlocks.blockWoodenDevice;
        final Block a = ConfigBlocks.blockMetalDevice;
        final Block[][][] blueprint = { { { p, p, p }, { p, a, p }, { p, p, p } },
                { { g, g, g }, { g, w, g }, { g, g, g } }, { { g, g, g }, { g, w, g }, { g, g, g } },
                { { p, p, p }, { p, a, p }, { p, p, p } } };
        for (int yy = 0; yy < 4; ++yy) {
            for (int xx = 0; xx < 3; ++xx) {
                for (int zz = 0; zz < 3; ++zz) {
                    BlockPos at = new BlockPos(x + xx, y - yy + 3, z + zz);
                    IBlockState state = world.getBlockState(at);
                    Block block = state.getBlock();
                    if (block != blueprint[yy][xx][zz]
                            || (block == p && block.getMetaFromState(state) != 6)
                            || (block == a && block.getMetaFromState(state) != 9)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    void replaceVat(final World world, final int x, final int y, final int z) {
        for (int yy = 0; yy < 4; ++yy) {
            for (int zz = 0; zz < 3; ++zz) {
                for (int xx = 0; xx < 3; ++xx) {
                    int md = 0; // Blocks.water || Blocks.flowing_water
                    if (world.getBlockState(new BlockPos(x + xx, y + yy, z + zz)).getBlock() == Blocks.GLASS) {
                        md = 10;
                    } else if (world.getBlockState(new BlockPos(x + xx, y + yy, z + zz)).getBlock() == ConfigBlocks.blockWoodenDevice) {
                        if (yy == 0 && ((xx == 1 && zz == 0) || (xx == 1 && zz == 2)
                                || (xx == 0 && zz == 1)
                                || (xx == 2 && zz == 1))) {
                            md = 4;
                        } else {
                            md = 5;
                        }
                    } else if (world.getBlockState(new BlockPos(x + xx, y + yy, z + zz)).getBlock() == ConfigBlocks.blockMetalDevice) {
                        if (yy == 0) {
                            md = 6;
                        } else {
                            md = 7;
                        }
                    }
                    if (!world.isAirBlock(new BlockPos(x + xx, y + yy, z + zz))) {
                        if (md == 4 || md == 5 || md == 6 || md == 7) {
                            world.setBlockState(new BlockPos(x + xx, y + yy, z + zz),
                                    ThaumicHorizons.blockVatSolid.getStateFromMeta(md), 3);
                        } else if (md != 0) {
                            world.setBlockState(new BlockPos(x + xx, y + yy, z + zz),
                                    ThaumicHorizons.blockVat.getStateFromMeta(md), 3);
                        } else {
                            world.setBlockState(new BlockPos(x + xx, y + yy, z + zz),
                                    ThaumicHorizons.blockVatInterior.getStateFromMeta(md), 3);
                        }
                        world.addBlockEvent(new BlockPos(x + xx, y + yy, z + zz), ThaumicHorizons.blockVat, 1, 4);
                    }
                }
            }
        }
        for (int yy = 0; yy < 4; ++yy) {
            for (int zz = 0; zz < 3; ++zz) {
                for (int xx = 0; xx < 3; ++xx) {
                    BlockPos at = new BlockPos(x + xx, y + yy, z + zz);
                    IBlockState state = world.getBlockState(at);
                    world.notifyBlockUpdate(at, state, state, 3);
                }
            }
        }
        world.playSound(null, new BlockPos(x + 1, y + 1, z + 1), TCSounds.WAND, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }
}
