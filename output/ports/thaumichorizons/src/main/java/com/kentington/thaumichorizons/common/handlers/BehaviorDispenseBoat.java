package com.kentington.thaumichorizons.common.handlers;
import net.minecraft.util.math.BlockPos;


import java.util.Set;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorDispenseBoat extends BehaviorDefaultDispenseItem {

    public interface BoatFactory {

        EntityBoat create(World world, double x, double y, double z);
    }

    private final Set<Material> validMaterials;
    private final BoatFactory boatFactory;

    public BehaviorDispenseBoat(Set<Material> validMaterials, BoatFactory boatFactory) {
        this.validMaterials = validMaterials;
        this.boatFactory = boatFactory;
    }

    @Override
    protected ItemStack dispenseStack(IBlockSource dispenser, ItemStack stack) {
        EnumFacing facing = dispenser.getBlockState().getValue(BlockDispenser.FACING);
        World world = dispenser.getWorld();

        // Calculate spawn position slightly in front of the dispenser
        double spawnX = dispenser.getX() + (facing.getXOffset() * 1.25F);
        double spawnY = dispenser.getY() + (facing.getYOffset() * 1.125F) - 0.5F;
        double spawnZ = dispenser.getZ() + (facing.getZOffset() * 1.25F);

        // Block coordinates in front of the dispenser
        int targetX = dispenser.getBlockPos().getX() + facing.getXOffset();
        int targetY = dispenser.getBlockPos().getY() + facing.getYOffset();
        int targetZ = dispenser.getBlockPos().getZ() + facing.getZOffset();

        Material frontMaterial = world.getBlockState(new BlockPos(targetX, targetY, targetZ)).getMaterial();

        if (validMaterials.contains(frontMaterial)) {
            spawnY++;
        } else if (!Material.AIR.equals(frontMaterial)) {
            return stack;
        }

        EntityBoat boat = boatFactory.create(world, spawnX, spawnY, spawnZ);

        switch (facing) {
    case NORTH: boat.rotationYaw = 90.0F; break;
    case WEST: boat.rotationYaw = 180.0F; break;
    case EAST: boat.rotationYaw = 0.0F; break;
    default: boat.rotationYaw = -90.0F; break; //SOUTH or other
}

        if (world.spawnEntity(boat)) stack.shrink(1);
        return stack;
    }
}
