//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.block.BlockTNT;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityAlchemitePrimed;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAlchemite extends BlockTNT implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider {

    private static TextureAtlasSprite blockIconTop;
    private static TextureAtlasSprite blockIconBottom;
    private TextureAtlasSprite blockIcon;
    private static final String TEXTURE = "ThaumicHorizons:alchemite";

    public BlockAlchemite() {
        this.setHardness(0.7f);
        this.setResistance(1.0f);
        this.setLightLevel(0.5f);
        this.setTranslationKey("ThaumicHorizons_alchemite");
        this.setCreativeTab(ThaumicHorizons.tabTH);
        this.setSoundType(SoundType.PLANT); // same place/break sound as TNT
    }

    @Override
    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosion) {
        onBlockDestroyedByExplosion(world, pos.getX(), pos.getY(), pos.getZ(), explosion);
    }

    @Override
    public void explode(World world, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
        func_150114_a(world, pos.getX(), pos.getY(), pos.getZ(), getMetaFromState(state), igniter);
    }

    public void onBlockDestroyedByExplosion(final World p_149723_1_, final int p_149723_2_, final int p_149723_3_,
            final int p_149723_4_, final Explosion p_149723_5_) {
        if (!p_149723_1_.isRemote) {
            final EntityAlchemitePrimed entitytntprimed = new EntityAlchemitePrimed(
                    p_149723_1_,
                    p_149723_2_ + 0.5f,
                    p_149723_3_ + 0.5f,
                    p_149723_4_ + 0.5f,
                    p_149723_5_.getExplosivePlacedBy());
            entitytntprimed.fuse = p_149723_1_.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
            p_149723_1_.spawnEntity(entitytntprimed);
        }
    }

    public void func_150114_a(final World p_150114_1_, final int p_150114_2_, final int p_150114_3_,
            final int p_150114_4_, final int p_150114_5_, final EntityLivingBase p_150114_6_) {
        if (!p_150114_1_.isRemote && (p_150114_5_ & 0x1) == 0x1) {
            final EntityAlchemitePrimed entitytntprimed = new EntityAlchemitePrimed(
                    p_150114_1_,
                    p_150114_2_ + 0.5f,
                    p_150114_3_ + 0.5f,
                    p_150114_4_ + 0.5f,
                    p_150114_6_);
            p_150114_1_.spawnEntity(entitytntprimed);
            com.kentington.thaumichorizons.common.lib.THSoundBridge.playAtEntity(p_150114_1_, entitytntprimed, "game.tnt.primed", 1.0f, 1.0f);
        }
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(final int p_149691_1_, final int p_149691_2_) {
        return (p_149691_1_ == 0) ? BlockAlchemite.blockIconBottom
                : ((p_149691_1_ == 1) ? BlockAlchemite.blockIconTop : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    @Override public void registerIcons(TextureMap atlas) { registerBlockIcons(atlas); }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final TextureMap p_149651_1_) {
        this.blockIcon = p_149651_1_.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite(TEXTURE + "_side", "blocks"));
        BlockAlchemite.blockIconTop = p_149651_1_.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite(TEXTURE + "_top", "blocks"));
        BlockAlchemite.blockIconBottom = p_149651_1_.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite(TEXTURE + "_bottom", "blocks"));
    }

}
