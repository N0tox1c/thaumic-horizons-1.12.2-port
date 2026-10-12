//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.tiles.TileSyntheticNode;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemWispEssence;

public class BlockSyntheticNode extends LegacyBlockContainer {

    TextureAtlasSprite icon;

    public BlockSyntheticNode() {
        super(Material.GLASS);
        this.setHardness(0.7f);
        this.setResistance(1.0f);
        this.setLightLevel(0.5f);
        this.setTranslationKey("ThaumicHorizons_synthNode");
        this.setCreativeTab(ThaumicHorizons.tabTH);
        this.setBlockBounds(0.3f, 0.0f, 0.3f, 0.7f, 1.0f, 0.7f);
    }

    public TileEntity createNewTileEntity(final World world, final int md) {
        return this.createTileEntity(world, md);
    }

    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileSyntheticNode();
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return ThaumicHorizons.blockSyntheticNodeRI;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumcraft:crystal", "blocks"));
    }

    public TextureAtlasSprite getIcon(final int par1, final int par2) {
        return this.icon;
    }

    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
            final int p_149727_6_, final float p_149727_7_, final float p_149727_8_, final float p_149727_9_) {
        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == ConfigItems.itemWispEssence) {
            ((TileSyntheticNode) world.getTileEntity(new BlockPos(x, y, z))).addEssence(player);
            return true;
        }
        return false;
    }

    public void breakBlock(final World world, final int x, final int y, final int z, final Block block,
            final int meta) {
        if (!(world.getTileEntity(new BlockPos(x, y, z)) instanceof TileSyntheticNode)) {
            super.breakBlock(world, x, y, z, block, meta);
            return;
        }
            final TileSyntheticNode tile = ((TileSyntheticNode) (world.getTileEntity(new BlockPos(x, y, z))));

        for (final Aspect asp : tile.getMaxAspects().getAspects()) {
            final ItemStack essence = new ItemStack(
                    ConfigItems.itemWispEssence,
                    tile.getMaxAspects().getAmount(asp) / 4);
            ((ItemWispEssence) ConfigItems.itemWispEssence).setAspects(essence, new AspectList().add(asp, 2));
            world.spawnEntity(new EntityItem(world, x, y, z, essence));
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public boolean onBlockActivated(World world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState state, EntityPlayer player, net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing face, float hitX, float hitY, float hitZ) {
        return onBlockActivated(world,pos.getX(), pos.getY(), pos.getZ(),player,face.getIndex(),hitX,hitY,hitZ);
    }

    @Override
    public void breakBlock(World world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState state) {
        breakBlock(world,pos.getX(), pos.getY(), pos.getZ(),state.getBlock(),getMetaFromState(state));
    }
}
