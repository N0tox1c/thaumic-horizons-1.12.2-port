//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;
import net.minecraft.util.math.BlockPos;


import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityItemInvulnerable;
import com.kentington.thaumichorizons.common.tiles.TileCloud;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.research.ResearchManager;

public class BlockCloud extends LegacyBlockContainer {

    public TextureAtlasSprite[] icon;
    public TextureAtlasSprite[] icontop;
    boolean glow;

    public BlockCloud(final boolean glowy) {
        super(Material.CLOTH);
        this.icon = new TextureAtlasSprite[10];
        this.icontop = new TextureAtlasSprite[10];
        this.setHardness(Float.MAX_VALUE);
        this.setResistance(Float.MAX_VALUE);
        this.setTranslationKey("ThaumicHorizons_cloud");
        this.setBlockTextureName("ThaumicHorizons:cloud");
        this.setCreativeTab(ThaumicHorizons.tabTH);
        if (glowy) {
            this.setLightLevel(1.0f);
        }
        this.glow = glowy;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap par1IconRegister) {
        this.icon[0] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:cloud", "blocks"));
        this.icon[1] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:firecloud", "blocks"));
        this.icon[2] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:thundercloud", "blocks"));
        this.icon[3] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:acidcloud", "blocks"));
        this.icon[4] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:alloycloud", "blocks"));
        this.icon[5] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:fleshcloud", "blocks"));
        this.icon[6] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:viscloud", "blocks"));
        this.icon[7] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:glyphcloud", "blocks"));
        this.icon[8] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:sporecloud", "blocks"));
        this.icon[9] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:animuscloud", "blocks"));
        this.icontop[0] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:cloudtop", "blocks"));
        this.icontop[1] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:firecloudtop", "blocks"));
        this.icontop[2] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:thundercloudtop", "blocks"));
        this.icontop[3] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:acidcloudtop", "blocks"));
        this.icontop[4] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:alloycloudtop", "blocks"));
        this.icontop[5] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:fleshcloudtop", "blocks"));
        this.icontop[6] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:viscloudtop", "blocks"));
        this.icontop[7] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:glyphcloudtop", "blocks"));
        this.icontop[8] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:sporecloudtop", "blocks"));
        this.icontop[9] = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:animuscloudtop", "blocks"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(final int par1, final int par2) {
        if (par1 == 0 || par1 == 1) {
            return this.icontop[par2];
        }
        return this.icon[par2];
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

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World p_149734_1_, final int p_149734_2_, final int p_149734_3_,
            final int p_149734_4_, final Random p_149734_5_) {}

    private void func_150186_m(final World p_150186_1_, final int p_150186_2_, final int p_150186_3_,
            final int p_150186_4_) {
        final Random random = p_150186_1_.rand;
        final double d0 = 0.0625;
        for (int l = 0; l < 6; ++l) {
            double d2 = p_150186_2_ + random.nextFloat();
            double d3 = p_150186_3_ + random.nextFloat();
            double d4 = p_150186_4_ + random.nextFloat();
            if (l == 0 && !p_150186_1_.getBlockState(new net.minecraft.util.math.BlockPos(p_150186_2_, p_150186_3_ + 1, p_150186_4_)).isOpaqueCube()) {
                d3 = p_150186_3_ + 1 + d0;
            }
            if (l == 1 && !p_150186_1_.getBlockState(new net.minecraft.util.math.BlockPos(p_150186_2_, p_150186_3_ - 1, p_150186_4_)).isOpaqueCube()) {
                d3 = p_150186_3_ - d0;
            }
            if (l == 2 && !p_150186_1_.getBlockState(new net.minecraft.util.math.BlockPos(p_150186_2_, p_150186_3_, p_150186_4_ + 1)).isOpaqueCube()) {
                d4 = p_150186_4_ + 1 + d0;
            }
            if (l == 3 && !p_150186_1_.getBlockState(new net.minecraft.util.math.BlockPos(p_150186_2_, p_150186_3_, p_150186_4_ - 1)).isOpaqueCube()) {
                d4 = p_150186_4_ - d0;
            }
            if (l == 4 && !p_150186_1_.getBlockState(new net.minecraft.util.math.BlockPos(p_150186_2_ + 1, p_150186_3_, p_150186_4_)).isOpaqueCube()) {
                d2 = p_150186_2_ + 1 + d0;
            }
            if (l == 5 && !p_150186_1_.getBlockState(new net.minecraft.util.math.BlockPos(p_150186_2_ - 1, p_150186_3_, p_150186_4_)).isOpaqueCube()) {
                d2 = p_150186_2_ - d0;
            }
            if (random.nextInt(10) == 0 && (d2 < p_150186_2_ || d2 > p_150186_2_ + 1
                    || d3 < 0.0
                    || d3 > p_150186_3_ + 1
                    || d4 < p_150186_4_
                    || d4 > p_150186_4_ + 1)) {
                p_150186_1_.spawnParticle(net.minecraft.util.EnumParticleTypes.CLOUD, d2, d3, d4, 0.0, 0.0, 0.0);
            }
        }
    }

    public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
        return new TileCloud();
    }

    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
            final int p_149727_6_, final float p_149727_7_, final float p_149727_8_, final float p_149727_9_) {
        if (!world.isRemote && player.getHeldItemMainhand() != null
                && player.getHeldItemMainhand().getItem() instanceof ItemWandCasting
                && ResearchManager.isResearchComplete(player.getName(), "planarClouds")
                && ((ItemWandCasting) player.getHeldItemMainhand().getItem())
                        .consumeVis(player.getHeldItemMainhand(), player, Aspect.AIR, 100, false)) {
            world.spawnEntity(
                    new EntityItemInvulnerable(
                            world,
                            x + 0.5,
                            y + 0.5,
                            z + 0.5,
                            new ItemStack(this, 1, world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z))))));
            world.setBlockToAir(new BlockPos(x, y, z));
            world.notifyBlockUpdate(new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)), world.getBlockState(new BlockPos(x, y, z)), 3);
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        if (!this.glow) {
            par3List.add(new ItemStack(this, 1, 0));
            par3List.add(new ItemStack(this, 1, 2));
            par3List.add(new ItemStack(this, 1, 3));
            par3List.add(new ItemStack(this, 1, 5));
            par3List.add(new ItemStack(this, 1, 8));
        } else {
            par3List.add(new ItemStack(this, 1, 1));
            par3List.add(new ItemStack(this, 1, 4));
            par3List.add(new ItemStack(this, 1, 6));
            par3List.add(new ItemStack(this, 1, 7));
            par3List.add(new ItemStack(this, 1, 9));
        }
    }

    public int damageDropped(final int par1) {
        return par1;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockAccess p_149646_1_, final int p_149646_2_, final int p_149646_3_,
            final int p_149646_4_, final int p_149646_5_) {
        final Block block = p_149646_1_.getBlockState(new BlockPos(p_149646_2_, p_149646_3_, p_149646_4_)).getBlock();
        return block != this
                || com.kentington.thaumichorizons.common.lib.WorldAccess.getBlockMetadata(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_) != com.kentington.thaumichorizons.common.lib.WorldAccess.getBlockMetadata(p_149646_1_, p_149646_2_ - net.minecraft.util.EnumFacing.byIndex(p_149646_5_).getXOffset(), p_149646_3_ - net.minecraft.util.EnumFacing.byIndex(p_149646_5_).getYOffset(), p_149646_4_ - net.minecraft.util.EnumFacing.byIndex(p_149646_5_).getZOffset());
    }

    public boolean getBlocksMovement(final IBlockAccess world, final int x, final int y, final int z) {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        return null;
    }

    public void onEntityCollidedWithBlock(final World p_149670_1_, final int p_149670_2_, final int p_149670_3_,
            final int p_149670_4_, final Entity ent) {
        final int md = com.kentington.thaumichorizons.common.lib.WorldAccess.getBlockMetadata(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_);
        if (md == 1 || md == 4) {
            ent.setFire(6);
        } else if (md == 3) {
            ent.attackEntityFrom(DamageSourceThaumcraft.dissolve, 1.0f);
        }
    }

    @Override
    public boolean onBlockActivated(World world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState state, EntityPlayer player, net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing face, float hitX, float hitY, float hitZ) {
        return onBlockActivated(world,pos.getX(), pos.getY(), pos.getZ(),player,face.getIndex(),hitX,hitY,hitZ);
    }

    @Override
    public void randomDisplayTick(net.minecraft.block.state.IBlockState state,World world,net.minecraft.util.math.BlockPos pos,Random random) {
        randomDisplayTick(world,pos.getX(), pos.getY(), pos.getZ(),random);
    }

    @Override
    public void onEntityCollision(World world,net.minecraft.util.math.BlockPos pos,net.minecraft.block.state.IBlockState state,Entity entity) {
        onEntityCollidedWithBlock(world,pos.getX(), pos.getY(), pos.getZ(),entity);
    }

    @Override
    public int damageDropped(net.minecraft.block.state.IBlockState state) {
        return damageDropped(getMetaFromState(state));
    }

    @Override
    public boolean isPassable(IBlockAccess world,net.minecraft.util.math.BlockPos pos) {
        return getBlocksMovement(world,pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public net.minecraft.util.math.AxisAlignedBB getCollisionBoundingBox(net.minecraft.block.state.IBlockState state,IBlockAccess world,net.minecraft.util.math.BlockPos pos) {
        if (!(world instanceof World)) return getBoundingBox(state,world,pos);
        net.minecraft.util.math.AxisAlignedBB box=getCollisionBoundingBoxFromPool((World)world,pos.getX(), pos.getY(), pos.getZ());
        return box==null ? null : box.offset(-pos.getX(),-pos.getY(),-pos.getZ());
    }

    @Override
    public boolean shouldSideBeRendered(net.minecraft.block.state.IBlockState state,IBlockAccess world,net.minecraft.util.math.BlockPos pos,net.minecraft.util.EnumFacing face) {
        net.minecraft.util.math.BlockPos other=pos.offset(face); return shouldSideBeRendered(world,other.getX(),other.getY(),other.getZ(),face.getIndex());
    }

    @Override
    public void getSubBlocks(CreativeTabs tab,net.minecraft.util.NonNullList<ItemStack> items) {
        getSubBlocks(net.minecraft.item.Item.getItemFromBlock(this),tab,items);
    }
}
