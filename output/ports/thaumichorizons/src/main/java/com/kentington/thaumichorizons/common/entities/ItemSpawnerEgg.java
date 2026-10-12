//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;
import net.minecraft.util.math.BlockPos;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpawnerEgg extends Item  implements com.kentington.thaumichorizons.common.lib.OriginalTextureProvider, com.kentington.thaumichorizons.common.lib.OriginalItemColor, com.kentington.thaumichorizons.common.lib.OriginalItemAppearance {

    static ArrayList<EntityEggStuff> spawnList;

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite theIcon;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite baseIcon;

    public static void addMapping(final String name, final int c1, final int c2) {
        ItemSpawnerEgg.spawnList.add(new EntityEggStuff("ThaumicHorizons." + name, c1, c2));
    }

    public ItemSpawnerEgg() {
        this.setHasSubtypes(true);
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    public String getItemStackDisplayName(final ItemStack par1ItemStack) {
        String s = ("" + I18n.translateToLocal("item.monsterPlacer.name")).trim();
        if (par1ItemStack.getItemDamage() < 0 || par1ItemStack.getItemDamage() >= spawnList.size()) return s;
        final String s2 = ItemSpawnerEgg.spawnList.get(par1ItemStack.getItemDamage()).name;
        if (s2 != null) {
            s = s + " " + I18n.translateToLocal("entity." + s2 + ".name");
        }
        return s;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack stack, final int layer) {
        if (stack.getItemDamage() < 0 || stack.getItemDamage() >= spawnList.size()) return 16777215;
        final EntityEggStuff entityegginfo = ItemSpawnerEgg.spawnList.get(stack.getItemDamage());
        return layer == 0 ? entityegginfo.color1 : entityegginfo.color2;
    }

    @Override
    public net.minecraft.util.EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
            net.minecraft.util.EnumHand hand, net.minecraft.util.EnumFacing side, float hitX, float hitY, float hitZ) {
        return onItemUse(player.getHeldItem(hand), player, world, pos.getX(), pos.getY(), pos.getZ(), side.getIndex(), hitX, hitY, hitZ)
                ? net.minecraft.util.EnumActionResult.SUCCESS : net.minecraft.util.EnumActionResult.FAIL;
    }

    @Override
    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, net.minecraft.util.EnumHand hand) {
        return new net.minecraft.util.ActionResult<>(net.minecraft.util.EnumActionResult.PASS,
                onItemRightClick(player.getHeldItem(hand), world, player));
    }

    @Override
    public void getSubItems(CreativeTabs tab, net.minecraft.util.NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) getSubItems(this, tab, items);
    }

    public boolean onItemUse(final ItemStack stack, final EntityPlayer player, final World world, int x, int y, int z,
            final int side, final float par8, final float par9, final float par10) {
        if (world.isRemote) {
            return true;
        }
        final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
        x += net.minecraft.util.EnumFacing.byIndex(side).getXOffset();
        y += net.minecraft.util.EnumFacing.byIndex(side).getYOffset();
        z += net.minecraft.util.EnumFacing.byIndex(side).getZOffset();
        double d0 = 0.0;
        if (side == 1 && block instanceof net.minecraft.block.BlockFence) {
            d0 = 0.5;
        }
        BlockPos spawnPos = new BlockPos(x, y, z);
        if (!world.isBlockModifiable(player, spawnPos)
                || !player.canPlayerEdit(spawnPos, net.minecraft.util.EnumFacing.byIndex(side), stack)) return false;
        final Entity entity = spawnCreature(world, stack.getItemDamage(), x + 0.5, y + d0, z + 0.5);
        if (entity != null) {
            if (entity instanceof EntityLiving && stack.hasDisplayName()) {
                ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
            }
            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }
        return true;
    }

    public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
        if (world.isRemote) {
            return stack;
        }
        final RayTraceResult movingobjectposition = this.rayTrace(world, player, true);
        if (movingobjectposition == null) {
            return stack;
        }
        if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
            final int i = movingobjectposition.getBlockPos().getX();
            final int j = movingobjectposition.getBlockPos().getY();
            final int k = movingobjectposition.getBlockPos().getZ();
            if (!world.isBlockModifiable(player, new net.minecraft.util.math.BlockPos(i, j, k))) {
                return stack;
            }
            if (!player.canPlayerEdit(new net.minecraft.util.math.BlockPos(i, j, k), movingobjectposition.sideHit, stack)) {
                return stack;
            }
            if (world.getBlockState(new BlockPos(i, j, k)).getBlock() instanceof BlockLiquid) {
                final Entity entity = spawnCreature(world, stack.getItemDamage(), i, j, k);
                if (entity != null) {
                    if (entity instanceof EntityLiving && stack.hasDisplayName()) {
                        ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
                    }
                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                }
            }
        }
        return stack;
    }

    public static Entity spawnCreature(final World par0World, final int par1, final double par2, final double par4,
            final double par6) {
        if (par0World.isRemote || par1 < 0 || par1 >= spawnList.size()) {
            return null;
        }
        Entity entity = null;
        for (int j = 0; j < 1; ++j) {
            entity = EntityList.createEntityByIDFromName(ItemSpawnerEgg.spawnList.get(par1).registryId, par0World);
            if (entity instanceof EntityLiving) {
                final EntityLiving entityliving = (EntityLiving) entity;
                entity.setLocationAndAngles(
                        par2,
                        par4,
                        par6,
                        MathHelper.wrapDegrees(par0World.rand.nextFloat() * 360.0f),
                        0.0f);
                entityliving.rotationYawHead = entityliving.rotationYaw;
                entityliving.renderYawOffset = entityliving.rotationYaw;
                entityliving.onInitialSpawn(par0World.getDifficultyForLocation(new BlockPos(entity)), null);
                if (!par0World.spawnEntity(entity)) return null;
                entityliving.playLivingSound();
            }
        }
        return entity;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamageForRenderPass(final int par1, final int par2) {
        return (par2 > 0) ? this.theIcon : this.baseIcon;
    }

    @Override @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(int metadata) { return this.baseIcon; }
    @Override @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getOriginalIcon(ItemStack stack,int pass) { return getIconFromDamageForRenderPass(stack.getMetadata(),pass); }
    @Override @SideOnly(Side.CLIENT)
    public int getOriginalRenderPasses() { return 2; }

    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item p_150895_1_, final CreativeTabs p_150895_2_, final List p_150895_3_) {
        for (int a = 0; a < ItemSpawnerEgg.spawnList.size(); ++a) {
            p_150895_3_.add(new ItemStack(p_150895_1_, 1, a));
        }
    }

    protected String getIconString() {
        return "spawn_egg";
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap par1IconRegister) {
        this.baseIcon = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("minecraft:spawn_egg", "items"));
        this.theIcon = par1IconRegister.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite(this.getIconString() + "_overlay", "items"));
    }

    static {
        ItemSpawnerEgg.spawnList = new ArrayList<>();
    }

    static class EntityEggStuff {

        String name;
        final net.minecraft.util.ResourceLocation registryId;
        int color1;
        int color2;

        public EntityEggStuff(final String name, final int color1, final int color2) {
            this.name = name;
            this.registryId = new net.minecraft.util.ResourceLocation("thaumichorizons", name.substring(name.lastIndexOf('.') + 1).toLowerCase(java.util.Locale.ROOT));
            this.color1 = color1;
            this.color2 = color2;
        }
    }
}
