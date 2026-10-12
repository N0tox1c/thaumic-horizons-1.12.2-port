package com.kentington.thaumichorizons.common.entities;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * Original Thaumic Horizons injection projectile ported onto the native 1.12
 * arrow physics implementation. Vanilla handles block/entity collision, arrow
 * pickup, shooter immunity, critical hits, knockback, water and gravity.
 * The mod-specific potion payload and colour remain fully serialized.
 */
public class EntitySyringe extends EntityArrow implements IEntityAdditionalSpawnData {
    public int color = 0xFFFF0000;
    /** Kept as a source-level compatibility field for original integrations. */
    public int canBePickedUp;
    public NBTTagCompound effects = new NBTTagCompound();

    public EntitySyringe(World world) {
        super(world);
        setDamage(1.5D);
        setSize(0.5F, 0.5F);
    }

    public EntitySyringe(World world, double x, double y, double z) {
        super(world, x, y, z);
        setDamage(1.5D);
        setSize(0.5F, 0.5F);
    }

    public EntitySyringe(World world, EntityLivingBase shooter, float velocity, NBTTagCompound tag) {
        super(world, shooter);
        initializePayload(tag);
        setDamage(1.5D);
        setSize(0.5F, 0.5F);
        if (shooter instanceof EntityPlayer) {
            canBePickedUp = 1;
            pickupStatus = PickupStatus.ALLOWED;
        }
        shoot(shooter, shooter.rotationPitch, shooter.rotationYaw, 0.0F, velocity * 1.5F, 1.0F);
    }

    public EntitySyringe(World world, EntityLivingBase shooter, EntityLivingBase target,
                         float velocity, float inaccuracy, NBTTagCompound tag) {
        super(world, shooter);
        initializePayload(tag);
        setDamage(1.5D);
        setSize(0.5F, 0.5F);
        if (shooter instanceof EntityPlayer) {
            canBePickedUp = 1;
            pickupStatus = PickupStatus.ALLOWED;
        }
        double x = target.posX - shooter.posX;
        double y = target.getEntityBoundingBox().minY + target.height / 3.0F - this.posY;
        double z = target.posZ - shooter.posZ;
        double flatDistance = Math.sqrt(x * x + z * z);
        // Original 1.7.10 aimed slightly higher to compensate for gravity.
        shoot(x, y + flatDistance * 0.2D, z, velocity, inaccuracy);
    }

    public EntitySyringe(World world, double x, double y, double z, NBTTagCompound tag) {
        this(world, x, y, z);
        initializePayload(tag);
        canBePickedUp = 1;
        pickupStatus = PickupStatus.ALLOWED;
    }

    private void initializePayload(NBTTagCompound tag) {
        effects = tag == null ? new NBTTagCompound() : tag.copy();
        color = effects.hasKey("color", 3) ? effects.getInteger("color") : 0xFFFF0000;
    }

    /** Called by vanilla EntityArrow only when an entity is actually damaged. */
    @Override
    protected void arrowHit(EntityLivingBase living) {
        super.arrowHit(living);
        if (world.isRemote || living instanceof EntityEnderman) {
            return;
        }
        ItemStack syringe = getArrowStack();
        for (PotionEffect effect : PotionUtils.getEffectsFromStack(syringe)) {
            living.addPotionEffect(new PotionEffect(effect));
        }
    }

    /** Vanilla pickup will return the injection payload, not a normal arrow. */
    @Override
    protected ItemStack getArrowStack() {
        if (ThaumicHorizons.itemSyringeInjection == null) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = new ItemStack(ThaumicHorizons.itemSyringeInjection);
        stack.setTagCompound(effects == null ? new NBTTagCompound() : effects.copy());
        return stack;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setTag("effects", effects == null ? new NBTTagCompound() : effects.copy());
        tag.setInteger("color", color);
        // Base EntityArrow already writes xTile/yTile/zTile/life/pickup/damage.
        canBePickedUp = pickupStatus == PickupStatus.ALLOWED ? 1 :
                pickupStatus == PickupStatus.CREATIVE_ONLY ? 2 : 0;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        initializePayload(tag.getCompoundTag("effects"));
        if (tag.hasKey("color", 3)) {
            color = tag.getInteger("color");
        }
        canBePickedUp = pickupStatus == PickupStatus.ALLOWED ? 1 :
                pickupStatus == PickupStatus.CREATIVE_ONLY ? 2 : 0;
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        data.writeInt(color);
        ByteBufUtils.writeTag(data, effects == null ? new NBTTagCompound() : effects);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        color = data.readInt();
        NBTTagCompound tag = ByteBufUtils.readTag(data);
        effects = tag == null ? new NBTTagCompound() : tag;
    }
}
