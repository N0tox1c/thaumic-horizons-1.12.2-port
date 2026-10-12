//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;


import java.util.List;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityBoatThaumium;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBoatThaumium extends ItemBoat  implements com.kentington.thaumichorizons.common.lib.OriginalItemAppearance , com.kentington.thaumichorizons.common.lib.OriginalTextureProvider {

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon;

    public ItemBoatThaumium() {
        super(EntityBoat.Type.OAK);
        this.maxStackSize = 1;
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World p_77659_2_,
            final EntityPlayer p_77659_3_, final EnumHand hand) {
        final ItemStack p_77659_1_ = p_77659_3_.getHeldItem(hand);
        final float f = 1.0f;
        final float f2 = p_77659_3_.prevRotationPitch + (p_77659_3_.rotationPitch - p_77659_3_.prevRotationPitch) * f;
        final float f3 = p_77659_3_.prevRotationYaw + (p_77659_3_.rotationYaw - p_77659_3_.prevRotationYaw) * f;
        final double d0 = p_77659_3_.prevPosX + (p_77659_3_.posX - p_77659_3_.prevPosX) * f;
        final double d2 = p_77659_3_.prevPosY + (p_77659_3_.posY - p_77659_3_.prevPosY) * f + p_77659_3_.getEyeHeight();
        final double d3 = p_77659_3_.prevPosZ + (p_77659_3_.posZ - p_77659_3_.prevPosZ) * f;
        final Vec3d vec3 = new Vec3d(d0, d2, d3);
        final float f4 = MathHelper.cos(-f3 * 0.017453292f - (float) Math.PI);
        final float f5 = MathHelper.sin(-f3 * 0.017453292f - (float) Math.PI);
        final float f6 = -MathHelper.cos(-f2 * 0.017453292f);
        final float f7 = MathHelper.sin(-f2 * 0.017453292f);
        final float f8 = f5 * f6;
        final float f9 = f4 * f6;
        final double d4 = 5.0;
        final Vec3d vec4 = vec3.add(f8 * d4, f7 * d4, f9 * d4);
        final RayTraceResult movingobjectposition = p_77659_2_.rayTraceBlocks(vec3, vec4, true);
        if (movingobjectposition == null) {
            return new ActionResult<>(EnumActionResult.PASS, p_77659_1_);
        }
        final Vec3d vec5 = p_77659_3_.getLook(f);
        boolean flag = false;
        final float f10 = 1.0f;
        final List list = p_77659_2_.getEntitiesWithinAABBExcludingEntity(
                p_77659_3_,
                p_77659_3_.getEntityBoundingBox().expand(vec5.x * d4, vec5.y * d4, vec5.z * d4)
                        .grow(f10, f10, f10));
        for (Object o : list) {
            final Entity entity = (Entity) o;
            if (entity.canBeCollidedWith()) {
                final float f11 = entity.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow(f11, f11, f11);
                if (axisalignedbb.contains(vec3)) {
                    flag = true;
                }
            }
        }
        if (flag) {
            return new ActionResult<>(EnumActionResult.PASS, p_77659_1_);
        }
        if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
            final int i = movingobjectposition.getBlockPos().getX();
            int j = movingobjectposition.getBlockPos().getY();
            final int k = movingobjectposition.getBlockPos().getZ();
            if (p_77659_2_.getBlockState(new BlockPos(i, j, k)).getBlock() == Blocks.SNOW_LAYER) {
                --j;
            }
            final EntityBoatThaumium entityboat = new EntityBoatThaumium(p_77659_2_, i + 0.5f, j + 1.0f, k + 0.5f);
            entityboat.rotationYaw = ((MathHelper.floor(p_77659_3_.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3) - 1)
                    * 90;
            if (!p_77659_2_.getCollisionBoxes(entityboat, entityboat.getEntityBoundingBox().grow(-0.1, -0.1, -0.1))
                    .isEmpty()) {
                return new ActionResult<>(EnumActionResult.PASS, p_77659_1_);
            }
            if (!p_77659_2_.isRemote) {
                if (!p_77659_2_.spawnEntity(entityboat)) {
                    return new ActionResult<>(EnumActionResult.FAIL, p_77659_1_);
                }
                if (!p_77659_3_.capabilities.isCreativeMode) p_77659_1_.shrink(1);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, p_77659_1_);
        }
        return new ActionResult<>(EnumActionResult.PASS, p_77659_1_);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final TextureMap ir) {
        this.icon = ir.registerSprite(com.kentington.thaumichorizons.client.LegacyTextureRegistration.sprite("thaumichorizons:boatthaumium", "items"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamage(final int par1) {
        return this.icon;
    }

    public String getTranslationKey(final ItemStack par1ItemStack) {
        return "item.boatThaumium";
    }
}
