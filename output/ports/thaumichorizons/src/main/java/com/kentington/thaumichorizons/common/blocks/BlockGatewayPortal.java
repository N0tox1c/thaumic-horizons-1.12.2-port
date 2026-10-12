package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.util.math.BlockPos;


import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileSlot;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.config.ConfigBlocks;

public class BlockGatewayPortal extends LegacyBlock {

    public TextureAtlasSprite CornerTR;
    public TextureAtlasSprite CornerTL;
    public TextureAtlasSprite CornerBR;
    public TextureAtlasSprite CornerBL;
    public TextureAtlasSprite topR;
    public TextureAtlasSprite topL;
    public TextureAtlasSprite R;
    public TextureAtlasSprite L;
    public TextureAtlasSprite B;
    public TextureAtlasSprite lapiz;
    public TextureAtlasSprite stone;

    public BlockGatewayPortal() {
        super(Material.ROCK);
        this.setHardness(2.5f);
        this.setResistance(2.5f);
        this.setTranslationKey("ThaumicHorizons_gateway");
    }

    public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int md) {
        int slotX = 0;
        int slotY = 0;
        int slotZ = 0;
        if (md < 5) {
            slotY = y + md;
            if (world.getBlockState(new BlockPos(x + 1, y, z)).getBlock() == ThaumicHorizons.blockPortal
                    || world.getBlockState(new BlockPos(x + 1, y, z)).getBlock() == ThaumicHorizons.blockGateway) {
                slotX = x + 2;
                slotZ = z;
            } else {
                slotX = x;
                slotZ = z + 2;
            }
        } else if (md == 5) {
            slotY = y;
            if (world.getBlockState(new BlockPos(x + 1, y, z)).getBlock() == ThaumicHorizons.blockSlot) {
                slotX = x + 1;
                slotZ = z;
            } else {
                slotX = x;
                slotZ = z + 1;
            }
        } else if (md == 8) {
            slotY = y;
            if (world.getBlockState(new BlockPos(x - 1, y, z)).getBlock() == ThaumicHorizons.blockSlot) {
                slotX = x - 1;
                slotZ = z;
            } else {
                slotX = x;
                slotZ = z - 1;
            }
        } else if (md == 6 || md == 7 || md == 9) {
            slotY = y + 4;
            if (world.getBlockState(new BlockPos(x + 1, y, z)).getBlock() == ThaumicHorizons.blockGateway) {
                slotZ = z;
                switch (md) {
    case 6: slotX = x + 1; break;
    case 7: slotX = x; break;
    case 9: slotX = x - 1; break;
}
            } else {
                slotX = x;
                switch (md) {
    case 6: slotZ = z + 1; break;
    case 7: slotZ = z; break;
    case 9: slotZ = z - 1; break;
}
            }
        } else {
            slotY = y + md - 10;
            if (world.getBlockState(new BlockPos(x - 1, y, z)).getBlock() == ThaumicHorizons.blockPortal
                    || world.getBlockState(new BlockPos(x - 1, y, z)).getBlock() == ThaumicHorizons.blockGateway) {
                slotX = x - 2;
                slotZ = z;
            } else {
                slotX = x;
                slotZ = z - 2;
            }
        }
        final TileEntity te = world.getTileEntity(new BlockPos(slotX, slotY, slotZ));
        if (te instanceof TileSlot && ((TileSlot) te).portalOpen) {
            ((TileSlot) te).destroyPortal();
        }
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(final World p_149694_1_, final int p_149694_2_, final int p_149694_3_, final int p_149694_4_) {
        return Item.getItemById(0);
    }

    public int quantityDropped(final Random p_149745_1_) {
        return 0;
    }

    public void registerBlockIcons(final TextureMap ir) {
        this.CornerTR = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:gateway_topright", "blocks"));
        this.CornerTL = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:gateway_topleft", "blocks"));
        this.CornerBR = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:gateway_bottomright", "blocks"));
        this.CornerBL = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:gateway_bottomleft", "blocks"));
        this.topR = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:gateway_top2", "blocks"));
        this.topL = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:gateway_top", "blocks"));
        this.R = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:gateway_right", "blocks"));
        this.L = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:gateway_left", "blocks"));
        this.B = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:gateway_bottom", "blocks"));
        // Stitch Pre runs before BlockModelShapes is baked. These are the actual
        // sprite paths of vanilla lapis and TC4 cosmetic-solid metadata 11.
        this.lapiz = ir.registerSprite(new net.minecraft.util.ResourceLocation("minecraft", "blocks/lapis_block"));
        this.stone = ir.registerSprite(new net.minecraft.util.ResourceLocation("thaumcraft", "blocks/es_1"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(final IBlockAccess world, final int x, final int y, final int z, final int side) {
        final boolean isXAligned = world.getBlockState(new BlockPos(x + 1, y, z)).getBlock() == ThaumicHorizons.blockGateway
                || world.getBlockState(new BlockPos(x + 1, y, z)).getBlock() == ThaumicHorizons.blockPortal
                || world.getBlockState(new BlockPos(x - 1, y, z)).getBlock() == ThaumicHorizons.blockGateway
                || world.getBlockState(new BlockPos(x - 1, y, z)).getBlock() == ThaumicHorizons.blockPortal;
        return selectIcon(isXAligned, getMetaFromState(world.getBlockState(new BlockPos(x,y,z))), side);
    }

    @Override @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(int side,int metadata) {
        return selectIcon(false,metadata,side);
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite selectIcon(boolean isXAligned,int metadata,int side) {
        switch (metadata) {
    case 0: switch (side) {
    case 0: return this.lapiz;
    case 1: return this.stone;
    case 2: return isXAligned ? this.CornerTR : this.stone;
    case 3: return isXAligned ? this.CornerTL : this.stone;
    case 4: return isXAligned ? this.stone : this.CornerTL;
    case 5: return isXAligned ? this.stone : this.CornerTR;
    default: return this.leftSide(isXAligned, side);
}
    case 1: case 2: case 3: return this.leftSide(isXAligned, side);
    case 4: switch (side) {
    case 0: return this.stone;
    case 1: return this.lapiz;
    case 2: return isXAligned ? this.CornerBR : this.stone;
    case 3: return isXAligned ? this.CornerBL : this.stone;
    case 4: return isXAligned ? this.stone : this.CornerBL;
    case 5: return isXAligned ? this.stone : this.CornerBR;
    default: return this.lapiz;
}
    case 5: switch (side) {
    case 0: return this.lapiz;
    case 1: return this.stone;
    case 2: return isXAligned ? this.topR : this.stone;
    case 3: return isXAligned ? this.topL : this.stone;
    case 4: return isXAligned ? this.stone : this.topL;
    case 5: return isXAligned ? this.stone : this.topR;
    default: return this.lapiz;
}
    case 6: case 7: case 9: return this.bottomSide(isXAligned, side);
    case 8: switch (side) {
    case 0: return this.lapiz;
    case 1: return this.stone;
    case 2: return isXAligned ? this.topL : this.stone;
    case 3: return isXAligned ? this.topR : this.stone;
    case 4: return isXAligned ? this.stone : this.topR;
    case 5: return isXAligned ? this.stone : this.topL;
    default: return this.lapiz;
}
    case 10: switch (side) {
    case 0: return this.lapiz;
    case 1: return this.stone;
    case 2: return isXAligned ? this.CornerTL : this.stone;
    case 3: return isXAligned ? this.CornerTR : this.stone;
    case 4: return isXAligned ? this.stone : this.CornerTR;
    case 5: return isXAligned ? this.stone : this.CornerTL;
    default: return this.rightSide(isXAligned, side);
}
    case 11: case 12: case 13: return this.rightSide(isXAligned, side);
    case 14: switch (side) {
    case 0: return this.stone;
    case 1: return this.lapiz;
    case 2: return isXAligned ? this.CornerBL : this.stone;
    case 3: return isXAligned ? this.CornerBR : this.stone;
    case 4: return isXAligned ? this.stone : this.CornerBR;
    case 5: return isXAligned ? this.stone : this.CornerBL;
    default: return this.lapiz;
}
    default: return this.lapiz;
}
    }

    TextureAtlasSprite leftSide(final boolean xAligned, final int side) {
        switch (side) {
    case 2: return xAligned ? this.R : this.stone;
    case 3: return xAligned ? this.L : this.lapiz;
    case 4: return xAligned ? this.stone : this.L;
    case 5: return xAligned ? this.lapiz : this.R;
    default: return this.lapiz;
}
    }

    TextureAtlasSprite rightSide(final boolean xAligned, final int side) {
        switch (side) {
    case 2: return xAligned ? this.L : this.lapiz;
    case 3: return xAligned ? this.R : this.stone;
    case 4: return xAligned ? this.lapiz : this.R;
    case 5: return xAligned ? this.stone : this.L;
    default: return this.lapiz;
}
    }

    TextureAtlasSprite bottomSide(final boolean xAligned, final int side) {
        switch (side) {
    case 0: return this.stone;
    case 1: return this.lapiz;
    default: return this.B;
}
    }

    @Override
    public void breakBlock(World world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState state) {
        breakBlock(world,pos.getX(), pos.getY(), pos.getZ(),state.getBlock(),getMetaFromState(state));
    }
}
