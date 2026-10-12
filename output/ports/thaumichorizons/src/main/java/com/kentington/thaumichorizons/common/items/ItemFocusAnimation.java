//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityGolemTH;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ItemFocusAnimation extends ItemFocusBasic  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider, com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    private static final AspectList cost;
    public static FocusUpgradeType berserk;
    public static FocusUpgradeType detonation;
    public TextureAtlasSprite ornamentIcon;

    public ItemFocusAnimation() {
        this.setMaxDamage(0);
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    @Override
    public TextureAtlasSprite getOrnament(final ItemStack focusstack) {
        return this.ornamentIcon;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:focus_animation", "items"));
        this.ornamentIcon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumcraft:focus_whatever_orn", "items"));
    }

    public String getItemStackDisplayName(final ItemStack stack) {
        return I18n.translateToLocal("item.focusAnimation.name");
    }

    @Override
    public int getFocusColor(final ItemStack focusstack) {
        return 15054592;
    }

    @Override
    public AspectList getVisCost(final ItemStack focusstack) {
        return ItemFocusAnimation.cost;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(final ItemStack focusstack, final int rank) {
        switch (rank) {
    case 1: case 4: case 2: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.extend };
            } 
    case 3: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.extend,
                        ItemFocusAnimation.berserk };
            } 
    case 5: {
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.extend,
                        ItemFocusAnimation.detonation };
            } 
    default: {
                return null;
            }
}
    }

    @Override
    public ItemStack onFocusRightClick(final ItemStack itemstack, final World world, final EntityPlayer player,
            final RayTraceResult mop) {
        final ItemWandCasting wand = (ItemWandCasting) itemstack.getItem();
        if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
            final int x = mop.getBlockPos().getX();
            final int y = mop.getBlockPos().getY();
            final int z = mop.getBlockPos().getZ();
            final Block blocky = world.getBlockState(new BlockPos(x, y, z)).getBlock();
            final int md = world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));
            if (!blocky.hasTileEntity(world.getBlockState(new BlockPos(x, y, z))) && !blocky.isAir(world.getBlockState(new BlockPos(x, y, z)), world, new BlockPos(x, y, z))
                    && (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube() || isWhitelisted(blocky, md))
                    && world.getBlockState(new BlockPos(x, y, z)).getBlockHardness(world, new BlockPos(x, y, z)) != -1.0f) {
if (!world.isRemote) {
                    final EntityGolemTH golem = new EntityGolemTH(world);
                    golem.loadGolem(
                            x + 0.5,
                            y,
                            z + 0.5,
                            blocky,
                            md,
                            600 + wand.getFocusExtend(itemstack) * 200,
                            false,
                            this.getUpgradeLevel(wand.getFocusItem(itemstack), ItemFocusAnimation.berserk) > 0,
                            this.getUpgradeLevel(wand.getFocusItem(itemstack), ItemFocusAnimation.detonation) > 0);
                    final AspectList cost = new AspectList().add(Aspect.FIRE, golem.type.visCost)
                            .add(Aspect.ORDER, golem.type.visCost).add(Aspect.AIR, golem.type.visCost)
                            .add(Aspect.EARTH, golem.type.visCost).add(Aspect.ENTROPY, golem.type.visCost)
                            .add(Aspect.WATER, golem.type.visCost);
                    if (!wand.consumeAllVis(itemstack, player, cost, false, false)) {
                        golem.setDead();
                        return itemstack;
                    }
                    if (!com.kentington.thaumichorizons.common.lib.BlockInteraction.canBreak(world, player, new BlockPos(x, y, z)) || !wand.consumeAllVis(itemstack, player, cost, true, false)) {
                        golem.setDead();
                        return itemstack;
                    }
                    world.setBlockToAir(new BlockPos(x, y, z));
                    com.kentington.thaumichorizons.common.lib.THSoundBridge.play(world, x + 0.5, y + 0.5, z + 0.5, "thaumcraft:wand", 1.0f, 1.0f);
                    golem.setHomePosAndDistance(new net.minecraft.util.math.BlockPos((int) golem.posX, (int) golem.posY, (int) golem.posZ), 32);
                    golem.setOwner(player.getName());
                    world.spawnEntity(golem);
                    world.setEntityState(golem, (byte) 7);
                } else {
                    Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)));
                    player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
                }
            }
        }
        return itemstack;
    }

    public static boolean isWhitelisted(final Block blocky, final int md) {
        return blocky == Blocks.CAKE || blocky == Blocks.CACTUS
                || blocky == Blocks.GLASS
                || blocky == Blocks.PACKED_ICE
                || blocky == Blocks.ICE
                || blocky == Blocks.WEB
                || (blocky == ConfigBlocks.blockCosmeticOpaque && md < 2)
                || (blocky == ConfigBlocks.blockWoodenDevice && md == 6)
                || md == 7;
    }

    static {
        cost = new AspectList().add(Aspect.FIRE, 1000).add(Aspect.AIR, 1000).add(Aspect.ORDER, 1000)
                .add(Aspect.WATER, 1000).add(Aspect.EARTH, 1000).add(Aspect.ENTROPY, 1000);
        ItemFocusAnimation.berserk = new FocusUpgradeType(
                FocusUpgradeType.types.length,
                new ResourceLocation("thaumichorizons", "textures/foci/berserk.png"),
                "focus.upgrade.berserk.name",
                "focus.upgrade.berserk.text",
                new AspectList().add(Aspect.WEAPON, 8));
        ItemFocusAnimation.detonation = new FocusUpgradeType(
                FocusUpgradeType.types.length,
                new ResourceLocation("thaumichorizons", "textures/foci/detonation.png"),
                "focus.upgrade.detonation.name",
                "focus.upgrade.detonation.text",
                new AspectList().add(Aspect.WEAPON, 8));
    }
    @Override @SideOnly(Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getIconFromDamage(int metadata) {
        return this.icon;
    }

}
